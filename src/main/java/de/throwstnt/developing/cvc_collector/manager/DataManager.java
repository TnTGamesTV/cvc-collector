package de.throwstnt.developing.cvc_collector.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import de.throwstnt.developing.cvc_collector.manager.data.Data;
import de.throwstnt.developing.cvc_collector.manager.data.IAmRemovable;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;
import net.labymod.support.util.Debug;
import net.labymod.support.util.Debug.EnumDebugMode;

public abstract class DataManager<DataType extends Data<IdentificationType>, IdentificationType>
        implements IAmRemovable {

    public static class DataListener<DataType> {

        private Predicate<DataType> filter;

        private Consumer<List<DataType>> consumer;

        private int lastHashCode;

        public DataListener(Predicate<DataType> filter, Consumer<List<DataType>> consumer) {
            this(filter, consumer, Integer.MIN_VALUE);
        }

        private DataListener(Predicate<DataType> filter, Consumer<List<DataType>> consumer,
                int lastHashCode) {
            this.filter = filter;
            this.consumer = consumer;
            this.lastHashCode = lastHashCode;
        }

        public Predicate<DataType> getFilter() {
            return this.filter;
        }

        public int getLastHashCode() {
            return this.lastHashCode;
        }

        public void setLastHashCode(int lastHashCode) {
            this.lastHashCode = lastHashCode;
        }

        /**
         * Sends an update to the listener if the hash codes do not match
         * 
         * @param newObjects the new objects
         */
        public void update(List<DataType> newObjects) {
            synchronized (newObjects) {
                if (this.lastHashCode != newObjects.hashCode()) {
                    this.consumer.accept(newObjects);
                    this.lastHashCode = newObjects.hashCode();
                }
            }
        }
    }

    private ExecutorService service = Executors.newSingleThreadExecutor();
    private ExecutorService listenerService = Executors.newSingleThreadExecutor();

    private List<DataType> list;

    private List<DataListener<DataType>> listeners;

    protected DataManager() {
        this.list = new ArrayList<>();

        this.listeners = new ArrayList<>();
    }

    private List<DataType> _find(Predicate<DataType> filter) {
        if (filter == null)
            filter = item -> true;

        synchronized (this.list) {
            return this.list.stream().filter(filter).collect(Collectors.toList());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void _find(Predicate<DataType> filter, Consumer<List<DataType>> consumer,
            boolean makeListener) {
        if (filter == null)
            filter = item -> true;

        DataListener listener = new DataListener<>(filter, consumer);

        List<DataType> list = this._find(filter);

        listener.update(list);

        if (makeListener) {
            this.listenerService.execute(() -> {
                this.listeners.add(listener);
            });
        }
    }

    public synchronized List<DataType> find(Predicate<DataType> filter) {
        return this._find(filter);
    }

    public void find(Predicate<DataType> filter, Consumer<List<DataType>> consumer) {
        this.service.execute(() -> {
            this._find(filter, consumer, true);
        });
    }

    public void find(Predicate<DataType> filter, Consumer<List<DataType>> consumer,
            boolean makeListener) {
        this.service.execute(() -> {
            this._find(filter, consumer, makeListener);
        });
    }

    private Predicate<DataType> _getIdentificationPredicate(IdentificationType identification) {
        return item -> item.identify(identification);
    }

    private synchronized DataType _get(IdentificationType identification) {
        synchronized (this.list) {
            DataType result =
                    this.list.stream().filter(this._getIdentificationPredicate(identification))
                            .findFirst().orElse(null);

            if (result == null)
                result = this._create(identification);

            return result;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void _get(IdentificationType identification, Consumer<DataType> consumer,
            boolean makeListener) {
        Consumer<List<DataType>> listConsumer = (list) -> {
            if (list.size() > 0)
                consumer.accept(list.get(0));
        };

        DataListener listener =
                new DataListener<>(this._getIdentificationPredicate(identification), listConsumer);

        List<DataType> list = Lists.newArrayList(this._get(identification));

        listener.update(list);

        if (makeListener) {
            this.listenerService.execute(() -> {
                this.listeners.add(listener);
            });
        }
    }

    public synchronized DataType get(IdentificationType identification) {
        return this._get(identification);
    }

    public void get(IdentificationType identification, Consumer<DataType> consumer) {
        this.service.execute(() -> {
            this._get(identification, consumer, true);
        });
    }

    public void get(IdentificationType identification, Consumer<DataType> consumer,
            boolean makeListener) {
        this.service.execute(() -> {
            this._get(identification, consumer, makeListener);
        });
    }

    public void remove(DataType item) {
        this.service.execute(() -> {
            synchronized (this.list) {
                this.list.removeIf(listItem -> listItem.identify(item.getIdentification()));

                this._callListeners();
            }
        });
    }

    public void remove(IdentificationType identification) {
        this.service.execute(() -> {
            synchronized (this.list) {
                this.list.removeIf(listItem -> listItem.identify(identification));

                this._callListeners();
            }
        });
    }

    /**
     * Updates a given item using its identifier
     * 
     * @param item the item
     */
    public void update(DataType item) {
        this.service.execute(() -> {
            synchronized (this.list) {
                this.list.removeIf(listItem -> listItem.identify(item.getIdentification()));

                if (this.isAllowedToAdd(item)) {
                    this.list.add(item);

                    this._callListeners();
                } else {
                    Debug.log(EnumDebugMode.ADDON,
                            "isAllowedToAdd for " + item.getIdentification() + " = false");
                }
            }
        });
    }

    public void updateAll(List<DataType> list) {
        this.service.execute(() -> {
            List<IdentificationType> identificationList = list.stream()
                    .map(item -> item.getIdentification()).collect(Collectors.toList());

            synchronized (this.list) {
                this.list.removeIf(
                        listItem -> identificationList.contains(listItem.getIdentification()));

                this.list.addAll(
                        list.stream().filter(this::isAllowedToAdd).collect(Collectors.toList()));

                this._callListeners();
            }
        });
    }

    public boolean isAllowedToAdd(DataType item) {
        return true;
    }

    private void _callListeners() {
        this.listenerService.execute(() -> {
            this.listeners.forEach(listener -> {
                listener.update(list);
            });
        });
    }

    private synchronized DataType _create(IdentificationType identification) {
        DataType item = this.create(identification);

        synchronized (this.list) {
            int oldSize = this.list.size();
            this.list.add(item);
            int newSize = this.list.size();

            ChatUtil.log("Added item (oldSize=" + oldSize + ", newSize=" + newSize + ")");

            this._callListeners();

            return item;
        }
    }

    public abstract DataType create(IdentificationType identification);

    @Override
    public void remove() {
        this.service.execute(() -> {
            synchronized (this.list) {
                this.list.clear();

                this._callListeners();
            }
        });
    }
}
