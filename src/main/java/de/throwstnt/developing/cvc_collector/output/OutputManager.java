package de.throwstnt.developing.cvc_collector.output;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import de.throwstnt.developing.cvc_collector.db.DatabaseManager;
import de.throwstnt.developing.cvc_collector.db.data.MovementEntry;
import de.throwstnt.developing.cvc_collector.output.data.PlayerTeamType;
import de.throwstnt.developing.cvc_collector.output.data.UnlinkedEntry;
import de.throwstnt.developing.cvc_collector.output.data.UnlinkedEntrySerializer;
import de.throwstnt.developing.cvc_collector.output.data.UnlinkedLocation;
import de.throwstnt.developing.cvc_collector.output.data.UnlinkedLocationListSerializer;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;
import net.minecraft.client.Minecraft;

public class OutputManager {

    private static OutputManager instance;

    public static OutputManager getInstance() {
        if (instance == null)
            instance = new OutputManager();
        return instance;
    }

    /**
     * Opens the database for generating output
     * 
     * @return the database
     */
    private DB _openDB() {
        Path databaseFolder =
                Minecraft.getInstance().gameDir.toPath().resolve("/CvC-Collector").normalize();

        databaseFolder.toFile().mkdirs();

        File dbFile = databaseFolder.resolve("output.db").toFile();

        return DBMaker.fileDB(dbFile).transactionEnable().closeOnJvmShutdown().make();
    }

    /**
     * Opens or creates all HTreeMaps for the given map names
     * 
     * @param db the database
     * @param mapNames the map names
     * @return the map of HTreeMaps
     */
    private Map<String, HTreeMap<UnlinkedLocation, List<UnlinkedEntry>>> _createMap(DB db,
            List<String> mapNames) {
        HashMap<String, HTreeMap<UnlinkedLocation, List<UnlinkedEntry>>> maps = new HashMap<>();

        mapNames.forEach(mapName -> {
            HTreeMap<UnlinkedLocation, List<UnlinkedEntry>> map =
                    db.hashMap(mapName, new UnlinkedLocationListSerializer(),
                            new UnlinkedEntrySerializer()).counterEnable().createOrOpen();

            maps.put(mapName, map);
        });

        return maps;
    }

    /**
     * Converts a given MovementEntry into an UnlinkedEntry
     * 
     * @param entry the movement entry
     * @return the unlinked entry
     */
    private UnlinkedEntry _makeUnlinkedEntry(MovementEntry entry) {
        double virtualKillDeathRatio = entry.getPlayer().getVirtualKillDeathRatio();
        int score = entry.getPlayer().getScore();



        PlayerTeamType teamType =
                entry.getPlayer().getTeam() == "cops" ? PlayerTeamType.COPS : PlayerTeamType.CRIMS;

        return new UnlinkedEntry(null, teamType);
    }

    private double _calculateCorrectScore(int score, double virtualKillDeathRatio) {
        double expectedScore = 10000;

        double lowestKillDeathRatio = 1.5;
        double baseKillDeathRatio = 2;
        double highestKillDeathRatio = 3;

        double baseScore = score * 0.5;
        double adjustedKillDeathRatio = Math.min(highestKillDeathRatio,
                Math.max(lowestKillDeathRatio, virtualKillDeathRatio));
        double underperformanceExpectedScore = 1 - Math.min(score / expectedScore, 1);
        double overperformanceKillDeathRatio =
                Math.max(0, (adjustedKillDeathRatio / baseKillDeathRatio) - 1);

        double scoreBoost =
                underperformanceExpectedScore * overperformanceKillDeathRatio * expectedScore;
        double boostedScore = baseScore + scoreBoost;

        return baseScore + boostedScore;
    }


    public void generate() {
        DB db = this._openDB();

        Map<String, HTreeMap<UnlinkedLocation, List<UnlinkedEntry>>> maps =
                this._createMap(db, Arrays.asList("Carrier"));

        DatabaseManager.getInstance().getLocationMap().forEach((id, entry) -> {
            String mapName = entry.getLocation().getMap();

            if (maps.containsKey(mapName)) {
                HTreeMap<UnlinkedLocation, List<UnlinkedEntry>> map = maps.get(mapName);

                UnlinkedLocation key = entry.getLocation().toUnlinkedLocation();

                if (map.containsKey(key)) {
                    List<UnlinkedEntry> entries = map.get(key);

                    entries.add(this._makeUnlinkedEntry(entry));
                } else {
                    List<UnlinkedEntry> entries = new ArrayList<>();

                    entries.add(this._makeUnlinkedEntry(entry));
                }
            } else {
                ChatUtil.log("The mapName '" + mapName + "' was not found");
            }
        });
    }
}
