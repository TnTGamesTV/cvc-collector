package de.throwstnt.developing.cvc_collector.db.data;

public class PlayerEntry {

  final String id;
  
  final String uuid;
  final String team;
  
  final int score;
  final double virtualKillDeathRatio;
  
  final long createdAt;
  
  public PlayerEntry(String id, String uuid, String team, int score, double virtualKillDeathRatio, long createdAt) {
    this.id = id;
    this.uuid = uuid;
    this.team = team;
    this.score = score;
    this.virtualKillDeathRatio = virtualKillDeathRatio;
    
    this.createdAt = createdAt;
  }

  public String getId() {
    return id;
  }

  public String getUuid() {
    return uuid;
  }

  public String getTeam() {
    return team;
  }

  public int getScore() {
    return score;
  }

  public double getVirtualKillDeathRatio() {
    return virtualKillDeathRatio;
  }

  public long getCreatedAt() {
    return createdAt;
  }
}
