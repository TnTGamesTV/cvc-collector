package de.throwstnt.developing.cvc_collector.manager.data.detector;

import java.util.UUID;
import com.mojang.authlib.GameProfile;
import de.throwstnt.developing.cvc_collector.state.ScoreboardManager;
import de.throwstnt.developing.cvc_collector.util.PlayerListUtil;
import de.throwstnt.developing.cvc_collector.util.SymbolLibrary;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

public class TeamDetector {

    private static TeamDetector instance;

    public static TeamDetector getInstance() {
        if (instance == null)
            instance = new TeamDetector();
        return instance;
    }

    /**
     * Returns the team for a given player
     * 
     * @param uuid the uuid of the player
     * @return the team ("cops"/"crims") or null if unknown
     */
    public String getTeam(UUID uuid) {
        GameProfile profile = PlayerListUtil.getAllPlayers().stream()
                .filter(gameProfile -> gameProfile.getId().equals(uuid)).findFirst().orElse(null);

        if (profile != null) {
            String name = profile.getName();

            Scoreboard scoreboard = ScoreboardManager.getInstance().getSidebar();

            if (scoreboard != null) {
                ScorePlayerTeam possibleTeam = scoreboard.getTeams().stream()
                        .filter(team -> team.getName().equals(name)).findFirst().orElse(null);

                if (possibleTeam != null) {
                    String teamId = null;
                    if (possibleTeam.getPrefix().toString().contains(SymbolLibrary.COP_SYMBOL))
                        teamId = "cops";
                    if (possibleTeam.getPrefix().toString().contains(SymbolLibrary.CRIM_SYMBOL))
                        teamId = "crims";

                    return teamId;
                }
            }
        }

        return null;
    }
}
