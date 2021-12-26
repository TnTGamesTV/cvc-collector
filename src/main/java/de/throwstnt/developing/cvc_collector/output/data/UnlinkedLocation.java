package de.throwstnt.developing.cvc_collector.output.data;

public class UnlinkedLocation {

    final int x;
    final int y;
    final int z;

    public UnlinkedLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
