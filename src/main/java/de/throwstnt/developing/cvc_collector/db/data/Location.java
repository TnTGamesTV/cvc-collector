package de.throwstnt.developing.cvc_collector.db.data;

import de.throwstnt.developing.cvc_collector.output.data.UnlinkedLocation;

public class Location {

    final String map;

    final double x;
    final double y;
    final double z;

    public Location(String map, double x, double y, double z) {
        this.map = map;

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getMap() {
        return map;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public UnlinkedLocation toUnlinkedLocation() {
        return new UnlinkedLocation((int) x, (int) y, (int) z);
    }
}
