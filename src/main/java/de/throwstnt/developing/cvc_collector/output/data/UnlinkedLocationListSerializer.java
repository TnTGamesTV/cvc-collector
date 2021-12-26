package de.throwstnt.developing.cvc_collector.output.data;

import java.io.IOException;
import java.io.Serializable;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

public class UnlinkedLocationListSerializer implements Serializer<UnlinkedLocation>, Serializable {

    private static final long serialVersionUID = 5078642824392685819L;

    @Override
    public void serialize(DataOutput2 out, UnlinkedLocation value) throws IOException {
        out.writeInt(value.getX());
        out.writeInt(value.getY());
        out.writeInt(value.getZ());
    }

    @Override
    public UnlinkedLocation deserialize(DataInput2 in, int available) throws IOException {
        int x = in.readInt();
        int y = in.readInt();
        int z = in.readInt();

        return new UnlinkedLocation(x, y, z);
    }
}
