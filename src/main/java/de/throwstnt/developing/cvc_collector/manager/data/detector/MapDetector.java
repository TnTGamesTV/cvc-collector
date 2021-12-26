package de.throwstnt.developing.cvc_collector.manager.data.detector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.throwstnt.developing.cvc_collector.state.ScoreboardManager;
import de.throwstnt.developing.cvc_collector.state.StateManager;
import de.throwstnt.developing.cvc_collector.state.StateManager.State;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;
import de.throwstnt.developing.cvc_collector.util.ScoreboardUtil;
import net.minecraft.scoreboard.Scoreboard;

public class MapDetector {

    private static final Pattern PATTERN_MAP = Pattern.compile("Map: ([a-zA-Z]+)\\n");

    private static MapDetector instance;

    public static MapDetector getInstance() {
        if (instance == null)
            instance = new MapDetector();
        return instance;
    }

    private String currentMap;

    private List<Consumer<String>> consumers;

    public MapDetector() {
        this.currentMap = null;

        this.consumers = new ArrayList<>();

        StateManager.getInstance().register((oldState, newState) -> {
            if (newState == State.CVC_WAITING) {
                Scoreboard scoreboard = ScoreboardManager.getInstance().getSidebar();

                if (scoreboard != null) {
                    String totalText = ScoreboardUtil.getCvcScoreboardText(scoreboard);

                    Matcher mapMatcher = PATTERN_MAP.matcher(totalText);

                    if (mapMatcher.find()) {
                        this.currentMap = mapMatcher.group(1);

                        this.consumers.forEach(consumer -> consumer.accept(this.currentMap));
                        this.consumers.clear();

                        ChatUtil.sendChatMessage("Detected the map '" + this.currentMap + "'");
                    }
                }
            }

            if (State.isInGame(oldState)) {
                // clean up
                this.currentMap = null;
            }
        });
    }

    /**
     * Returns the current map that is detected
     * 
     * @return
     */
    public String getCurrentMap() {
        if (this.currentMap == null)
            return "unknown";

        return this.currentMap;
    }

    public void getCurrentMap(Consumer<String> consumer) {
        if (this.currentMap != null) {
            consumer.accept(this.currentMap);
        } else {
            this.consumers.add(consumer);
        }
    }
}
