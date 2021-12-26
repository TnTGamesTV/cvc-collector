package de.throwstnt.developing.cvc_collector;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import de.throwstnt.developing.cvc_collector.db.DatabaseManager;
import de.throwstnt.developing.cvc_collector.events.ChatMessageListener;
import de.throwstnt.developing.cvc_collector.events.DisconnectServerListener;
import de.throwstnt.developing.cvc_collector.events.IncomingPacketListener;
import de.throwstnt.developing.cvc_collector.events.LoginServerListener;
import de.throwstnt.developing.cvc_collector.events.PayloadMessageListener;
import de.throwstnt.developing.cvc_collector.manager.CvcPlayerManager;
import de.throwstnt.developing.cvc_collector.manager.RoundManager;
import de.throwstnt.developing.cvc_collector.manager.UUIDManager;
import de.throwstnt.developing.cvc_collector.manager.data.detector.MapDetector;
import de.throwstnt.developing.cvc_collector.module.LocationEntryCountModule;
import de.throwstnt.developing.cvc_collector.state.StateManager;
import de.throwstnt.developing.cvc_collector.state.StateManager.State;
import de.throwstnt.developing.cvc_collector.stats.api.ApiCoordinator;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;
import net.labymod.api.LabyModAddon;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.StringElement;
import net.labymod.utils.Material;

public class CvcCollector extends LabyModAddon {

    public static final ModuleCategory HYPIXEL_SERVICES = new ModuleCategory("Hypixel Services",
            true, new ControlElement.IconData(Material.GOLD_BLOCK));

    private static CvcCollector instance;

    public boolean loggedIntoHypixel = false;

    public boolean enabled;

    public String apiKey = "";

    private Timer timer;
    private TimerTask checkStateTask;

    private TimerTask updateLocationEntryCounterTask;

    public static CvcCollector getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        ModuleCategoryRegistry.loadCategory(HYPIXEL_SERVICES);

        getApi().getEventService().registerListener(new LoginServerListener());
        getApi().getEventService().registerListener(new DisconnectServerListener());
        getApi().getEventService().registerListener(new IncomingPacketListener());
        getApi().getEventService().registerListener(new PayloadMessageListener());
        getApi().getEventService().registerListener(new ChatMessageListener());

        getApi().registerModule(new LocationEntryCountModule());

        // init singletons
        DatabaseManager.getInstance();
        MapDetector.getInstance();

        this.checkStateTask = new TimerTask() {

            @Override
            public void run() {
                try {
                    StateManager.getInstance().updateState();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };

        this.timer = new Timer("CvcCollector-Timer");
        this.timer.schedule(checkStateTask, 500, 500);

        this.timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (RoundManager.getInstance().isRoundActive()) {
                    DatabaseManager.getInstance().updateLocationEntryCounter();
                }
            }

        }, 0, 500);

        StateManager.getInstance().register((oldState, newState) -> {
            if (State.isInGame(newState)) {
                // fetch all players and create CvcPlayer's for them
                UUIDManager.getInstance().getUUIDList()
                        .forEach(CvcPlayerManager.getInstance()::get);
            }
        });

        StateManager.getInstance().register((oldState, newState) -> {
            ChatUtil.log("Updated state from " + oldState + " to " + newState);
            ChatUtil.sendChatMessage("Updated state from " + oldState + " to " + newState);
        });
    }

    @Override
    public void onDisable() {
        this.timer.cancel();
    }

    @Override
    public void loadConfig() {
        this.enabled =
                getConfig().has("enabled") ? getConfig().get("enabled").getAsBoolean() : false;

        this.apiKey = getConfig().has("apiKey") ? getConfig().get("apiKey").getAsString() : "";

        if (this.apiKey.length() > 0) {
            ApiCoordinator.onApiKey(apiKey);

            ChatUtil.log("Updated the apiKey");
        }
    }

    @Override
    protected void fillSettings(List<SettingsElement> subSettings) {
        subSettings.add(new BooleanElement("Enabled", this,
                new ControlElement.IconData(Material.LEVER), "enabled", this.enabled));
        subSettings.add(new StringElement("ApiKey", this,
                new ControlElement.IconData(Material.REDSTONE_TORCH), "apiKey", this.apiKey));
    }
}
