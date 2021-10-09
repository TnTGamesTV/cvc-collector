package de.throwstnt.developing.cvc_collector.stats.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.AbstractReply;
import net.hypixel.api.reply.KeyReply;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.reply.StatusReply;

public class ApiCoordinator {

  public interface IApiChangedListener {
    void onChanged(HypixelAPI hypixelApi);
  }

  public interface IGetReply<R extends AbstractReply> {
    void getReply(HypixelAPI api, UUID uuid, Consumer<R> consumer);
  }

  public interface IConsumeInProgress<R extends AbstractReply> {
    void consume(String request, UUID uuid, R reply);
  }

  public static final int CACHE_TTL = 60 * 10;

  private static HypixelAPI hypixelApi;

  private static ApiCache apiCache = new ApiCache();

  private static List<String> cacheIdsInProgress = new ArrayList<>();
  @SuppressWarnings("rawtypes")
  private static HashMap<String, List<IConsumeInProgress>> cacheIdsInProgressConsumers =
      new HashMap<>();

  private static List<IApiChangedListener> listeners = new ArrayList<>();

  /**
   * Checks if a request is currently in progress
   * 
   * @param request the request
   * @param uuid the user id
   * @return true if the request is already in progress
   */
  private static boolean _isInProgress(String request, UUID uuid) {
    String id = _makeCacheId(request, uuid);

    return cacheIdsInProgress.contains(id);
  }

  @SuppressWarnings("rawtypes")
  private static void _addInProgress(String request, UUID uuid) {
    String id = _makeCacheId(request, uuid);

    cacheIdsInProgress.add(id);
    List<IConsumeInProgress> consumerList = new ArrayList<>();
    cacheIdsInProgressConsumers.put(id, consumerList);
  }

  @SuppressWarnings("rawtypes")
  private static <R extends AbstractReply> void _addInProgressConsumer(String request, UUID uuid,
      IConsumeInProgress<R> consumer) {
    String id = _makeCacheId(request, uuid);

    if (cacheIdsInProgressConsumers.containsKey(id)) {
      List<IConsumeInProgress> consumerList = cacheIdsInProgressConsumers.get(id);
      consumerList.add(consumer);
    }
  }

  private static void _clearInProgress(String request, UUID uuid) {
    String id = _makeCacheId(request, uuid);

    cacheIdsInProgressConsumers.remove(id);
    cacheIdsInProgress.remove(id);
  }

  /**
   * Creates a id for cache
   * 
   * @param request the request
   * @param uuid the uuid of the player
   * @return the id
   */
  private static String _makeCacheId(String request, UUID uuid) {
    if (uuid != null) {
      return request + "_" + uuid.toString();
    } else {
      return request + "_" + UUID.randomUUID().toString();
    }
  }

  public static void onApiKey(String apiKey) {
    hypixelApi = new HypixelAPI(UUID.fromString(apiKey));

    hypixelApi.getApiKey();

    if (hypixelApi != null) {
      listeners.forEach((listener) -> listener.onChanged(hypixelApi));
      listeners.clear();
    }
  }

  public static void whenAvailable(IApiChangedListener listener) {
    if (hypixelApi == null) {
      listeners.add(listener);
    } else {
      listener.onChanged(hypixelApi);
    }
  }

  private static boolean _isCached(String request, UUID uuid) {
    return apiCache.get(_makeCacheId(request, uuid)) != null;
  }

  @SuppressWarnings("unchecked")
  private static <R extends AbstractReply> R _getCached(String request, UUID uuid) {
    ApiRequestAndResponse aRa = apiCache.get(_makeCacheId(request, uuid));

    if (aRa != null) {
      return (R) aRa.reply;
    } else {
      return null;
    }
  }

  private static void setOrReplace(ApiRequestAndResponse rAr) {
    String id = _makeCacheId(rAr.request, rAr.uuid);


    apiCache.put(id, rAr, CACHE_TTL);
  }

  @SuppressWarnings("unchecked")
  private static <R extends AbstractReply> void get(String request, UUID uuid,
      IGetReply<R> GetReply, Consumer<R> consumer, boolean force) {
    boolean wasConsumed = false;

    if (_isCached(request, uuid) && !force) {
      R reply = _getCached(request, uuid);

      if (reply != null) {
        consumer.accept(reply);
        wasConsumed = true;
      }
    }

    if (!wasConsumed) {
      if (_isInProgress(request, uuid)) {
        _addInProgressConsumer(request, uuid, (string, uuid2, reply) -> {
          consumer.accept((R) reply);
        });
      } else {
        _addInProgress(request, uuid);
        _addInProgressConsumer(request, uuid, (string, uuid2, reply) -> {
          consumer.accept((R) reply);
        });

        whenAvailable((api) -> {
          GetReply.getReply(api, uuid, (reply) -> {
            setOrReplace(new ApiRequestAndResponse(request, uuid, reply));

            cacheIdsInProgressConsumers.get(_makeCacheId(request, uuid)).forEach((consumer2) -> {
              consumer2.consume(request, uuid, reply);
            });

            _clearInProgress(request, uuid);
          });
        });
      }
    }
  }

  public static void clearCache() {
    apiCache.clear();
  }

  public static void getStatus(UUID uuid, Consumer<StatusReply> consumer) {
    getStatus(uuid, consumer, false);
  }

  public static void getStatus(UUID uuid, Consumer<StatusReply> consumer, boolean force) {
    get("status", uuid, (api, uuidInternal, consumerInternal) -> {
      api.getStatus(uuidInternal).whenComplete((result, t) -> {
        consumerInternal.accept(result);
      });
    }, consumer, force);
  }

  public static void getPlayer(UUID uuid, Consumer<PlayerReply> consumer) {
    getPlayer(uuid, consumer, false);
  }

  public static void getPlayer(UUID uuid, Consumer<PlayerReply> consumer, boolean force) {
    get("player", uuid, (api, uuidInternal, consumerInternal) -> {
      api.getPlayerByUuid(uuidInternal).whenComplete((result, t) -> {
        consumerInternal.accept(result);
      });
    }, consumer, force);
  }

  public static void getKey(Consumer<KeyReply> consumer) {
    get("key", null, (api, uuidInternal, consumerInternal) -> {
      api.getKey().whenComplete((result, t) -> {
        consumerInternal.accept(result);
      });
    }, consumer, true);
  }
}
