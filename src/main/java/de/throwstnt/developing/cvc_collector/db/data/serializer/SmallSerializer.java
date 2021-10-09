package de.throwstnt.developing.cvc_collector.db.data.serializer;

import java.io.IOException;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;

public interface SmallSerializer<T> {

  public T deserialize(DataInput2 in) throws IOException;

  public void serialize(DataOutput2 out, T item) throws IOException;
}
