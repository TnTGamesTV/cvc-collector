package de.throwstnt.developing.cvc_collector.manager;

import java.util.UUID;
import de.throwstnt.developing.cvc_collector.manager.data.detector.TeamDetector;
import de.throwstnt.developing.cvc_collector.stats.api.AbstractCache;

public class TeamManager {

    public static final int CACHE_TTL = 5;

    public class TeamCache extends AbstractCache<UUID, String> {

        public TeamCache() {
            super(32);
        }
    }

    private static TeamManager instance;

    public static TeamManager getInstance() {
        if (instance == null)
            instance = new TeamManager();
        return instance;
    }

    private TeamCache cache;

    public TeamManager() {
        this.cache = new TeamCache();
    }

    /**
     * Get the cached team of the given user
     * 
     * @param uuid the user
     * @return
     */
    public String getTeam(UUID uuid) {
        String cachedTeam = this.cache.get(uuid);

        if (cachedTeam == null) {
            String currentTeam = TeamDetector.getInstance().getTeam(uuid);

            if (currentTeam != null) {
                // we do not cache null values as they equal an unknown team
                this.cache.put(uuid, currentTeam, CACHE_TTL);
            }

            return currentTeam;
        } else {
            return cachedTeam;
        }
    }

    /**
     * Clears the cache duh
     */
    public void clearCache() {
        this.cache.clear();
    }
}
