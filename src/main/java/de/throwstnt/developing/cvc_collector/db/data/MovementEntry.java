package de.throwstnt.developing.cvc_collector.db.data;

public class MovementEntry {

  final PlayerEntry player;
  final Location location;
  
  final long millisSinceRoundStart;
  
  public MovementEntry(PlayerEntry player, Location location, long millisSinceRoundStart) {
    this.player = player;
    this.location = location;
    this.millisSinceRoundStart = millisSinceRoundStart;
  }

  public PlayerEntry getPlayer() {
    return player;
  }

  public Location getLocation() {
    return location;
  }

  public long getMillisSinceRoundStart() {
    return millisSinceRoundStart;
  }
}
