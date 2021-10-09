package de.throwstnt.developing.cvc_collector.db.data.serializer;

import java.io.IOException;
import java.io.Serializable;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import de.throwstnt.developing.cvc_collector.db.data.PlayerEntry;

public class PlayerEntrySerializer implements Serializer<PlayerEntry>, Serializable {

  private static final long serialVersionUID = -7434776991328923096L;

  @Override
  public void serialize(DataOutput2 out, PlayerEntry value) throws IOException {
    out.writeUTF(value.getId());
    out.writeUTF(value.getUuid());
    out.writeUTF(value.getTeam());
    
    out.writeInt(value.getScore());

    out.writeDouble(value.getVirtualKillDeathRatio());
    
    out.writeLong(value.getCreatedAt());
  }

  @Override
  public PlayerEntry deserialize(DataInput2 in, int available) throws IOException {
    String id = in.readUTF();
    String uuid = in.readUTF();
    String map = in.readUTF();
    
    int score = in.readInt();
    double virtualKillDeathRatio = in.readDouble();
    
    long createdAt = in.readLong();
    
    return new PlayerEntry(id, uuid, map, score, virtualKillDeathRatio, createdAt);
  }
}
