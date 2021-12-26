package de.throwstnt.developing.cvc_collector.output.data;

public class UnlinkedEntry {

    final PlayerSkillType skillType;
    final PlayerTeamType teamType;

    public UnlinkedEntry(PlayerSkillType skillType, PlayerTeamType teamType) {
        this.skillType = skillType;
        this.teamType = teamType;
    }

    public PlayerSkillType getSkillType() {
        return skillType;
    }

    public PlayerTeamType getTeamType() {
        return teamType;
    }
}
