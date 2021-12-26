package de.throwstnt.developing.cvc_collector.db.data.serializer;

import java.io.IOException;
import java.io.Serializable;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import de.throwstnt.developing.cvc_collector.db.DatabaseManager;
import de.throwstnt.developing.cvc_collector.db.data.Location;
import de.throwstnt.developing.cvc_collector.db.data.MovementEntry;
import de.throwstnt.developing.cvc_collector.db.data.PlayerEntry;

public class MovementEntrySerializer implements Serializer<MovementEntry>, Serializable {

    private static final long serialVersionUID = 2634087900794908824L;

    @Override
    public MovementEntry deserialize(DataInput2 in, int available) throws IOException {
        String id = in.readUTF();
        Location location = new LocationSerializer().deserialize(in);
        long millisSinceRoundStart = in.readLong();

        PlayerEntry playerEntry = DatabaseManager.getInstance().getPlayerMap().get(id);

        return new MovementEntry(playerEntry, location, millisSinceRoundStart);
    }

    @Override
    public void serialize(DataOutput2 out, MovementEntry item) throws IOException {
        out.writeUTF(item.getPlayer().getId());
        new LocationSerializer().serialize(out, item.getLocation());
        out.writeLong(item.getMillisSinceRoundStart());
    }
}
