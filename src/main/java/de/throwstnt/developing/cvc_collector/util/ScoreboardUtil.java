package de.throwstnt.developing.cvc_collector.util;

import java.util.Optional;

import net.minecraft.scoreboard.Scoreboard;

public class ScoreboardUtil {

	public static String getCvcScoreboardText(Scoreboard scoreboard) {
		Optional<String> optionalTotalScoreboard = scoreboard.getTeams().stream().filter(team -> team.getName().contains("team_")).map(team -> team.getPrefix().getString() + team.getSuffix().getString()).reduce((result, element) -> {
			if(result == null) return element;
			return result + "\n" + element;
		});
		
		if(optionalTotalScoreboard.isPresent()) {
			return ChatUtil.cleanColorCoding(optionalTotalScoreboard.get());
		}
	
		return null;
	}
}
