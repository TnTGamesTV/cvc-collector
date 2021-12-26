package de.throwstnt.developing.cvc_collector.output.data;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

public class UnlinkedEntrySerializer implements Serializer<List<UnlinkedEntry>>, Serializable {

    private static final long serialVersionUID = 5078642824392685818L;

    @Override
    public void serialize(DataOutput2 out, List<UnlinkedEntry> values) throws IOException {
        out.packInt(values.size());
        for (UnlinkedEntry value : values) {
            out.writeUTF(value.skillType.name());
            out.writeUTF(value.teamType.name());
        }
    }

    @Override
    public List<UnlinkedEntry> deserialize(DataInput2 in, int available) throws IOException {
        int size = in.unpackInt();

        UnlinkedEntry[] values = new UnlinkedEntry[size];
        for (int i = 0; i < size; i++) {
            PlayerSkillType skillType = PlayerSkillType.valueOf(in.readUTF());
            PlayerTeamType teamType = PlayerTeamType.valueOf(in.readUTF());

            values[i] = new UnlinkedEntry(skillType, teamType);
        }

        return Arrays.asList(values);
    }
}
