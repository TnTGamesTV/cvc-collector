package de.throwstnt.developing.cvc_collector.db;

import java.io.File;
import java.nio.file.Path;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import de.throwstnt.developing.cvc_collector.db.data.MovementEntry;
import de.throwstnt.developing.cvc_collector.db.data.PlayerEntry;
import de.throwstnt.developing.cvc_collector.db.data.serializer.MovementEntrySerializer;
import de.throwstnt.developing.cvc_collector.db.data.serializer.PlayerEntrySerializer;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;
import net.minecraft.client.Minecraft;

public class DatabaseManager {

    private static DatabaseManager instance;

    public static DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();
        return instance;
    }

    private File dbFile;

    private DB db;

    private long counterLastCommit = 0;
    private long counterSinceLastCommitCalculated;

    private HTreeMap<String, MovementEntry> locationMap;

    private HTreeMap<String, PlayerEntry> playerMap;

    @SuppressWarnings("resource")
    public DatabaseManager() {
        Path databaseFolder =
                Minecraft.getInstance().gameDir.toPath().resolve("/CvC-Collector").normalize();

        databaseFolder.toFile().mkdirs();

        this.dbFile = databaseFolder.resolve("test.db").toFile();

        ChatUtil.log("Opening database at: " + this.dbFile.getAbsolutePath());

        this.db = DBMaker.fileDB(this.dbFile).transactionEnable().closeOnJvmShutdown().make();

        this.locationMap =
                this.db.hashMap("locations", Serializer.STRING, new MovementEntrySerializer())
                        .counterEnable().createOrOpen();

        this.playerMap = this.db.hashMap("players", Serializer.STRING, new PlayerEntrySerializer())
                .counterEnable().createOrOpen();

        this.db.getStore().fileLoad();

        this.counterLastCommit = this.locationMap.sizeLong();
    }

    public File getDbFile() {
        return dbFile;
    }

    public DB getDb() {
        return db;
    }

    public HTreeMap<String, MovementEntry> getLocationMap() {
        return locationMap;
    }

    public HTreeMap<String, PlayerEntry> getPlayerMap() {
        return playerMap;
    }

    public void updateLocationEntryCounter() {
        this.counterSinceLastCommitCalculated =
                this.locationMap.sizeLong() - this.counterLastCommit;
    }

    public long getCountSinceLastCommit() {
        return this.counterSinceLastCommitCalculated;
    }

    public void save() {
        this.db.commit();
        this.counterLastCommit = this.locationMap.sizeLong();
    }
}
