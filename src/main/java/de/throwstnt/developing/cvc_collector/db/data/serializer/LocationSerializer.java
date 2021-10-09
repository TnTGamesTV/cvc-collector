package de.throwstnt.developing.cvc_collector.db.data.serializer;

import java.io.IOException;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import de.throwstnt.developing.cvc_collector.db.data.Location;

public class LocationSerializer implements SmallSerializer<Location> {

  @Override
  public Location deserialize(DataInput2 in) throws IOException {
    String map = in.readUTF();
    
    double x = in.readDouble();
    double y = in.readDouble();
    double z = in.readDouble();
    
    return new Location(map, x, y, z);
  }

  @Override
  public void serialize(DataOutput2 out, Location item) throws IOException {
    out.writeUTF(item.getMap());

    out.writeDouble(item.getX());
    out.writeDouble(item.getY());
    out.writeDouble(item.getZ());
  }
}
