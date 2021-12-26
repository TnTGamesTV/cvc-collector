package de.throwstnt.developing.cvc_collector.stats.api;

import java.util.UUID;
import net.labymod.utils.UUIDFetcher;

public class MojangApiCoordinator {

    public static final int CONST_UUID_CACHE_TTL = 60 * 60;

    public static final UUID CONST_RANDOM_NULL_UUID = UUID.randomUUID();

    private static MojangApiCoordinator instance;

    public static MojangApiCoordinator getInstance() {
        if (instance == null)
            instance = new MojangApiCoordinator();
        return instance;
    }

    private UUIDCache uuidCache;

    private MojangApiCoordinator() {
        this.uuidCache = new UUIDCache();
    }

    public UUID getUUID(String name) {
        UUID result = this.uuidCache.get(name);

        if (result == null) {
            result = UUIDFetcher.getUUID(name);

            if (result == null) {
                this.uuidCache.put(name, CONST_RANDOM_NULL_UUID, 0);
            } else {
                this.uuidCache.put(name, result, 0);
            }
        }

        if (result == CONST_RANDOM_NULL_UUID) {
            return null;
        }

        return result;
    }

    public void clearCache() {
        this.uuidCache.clear();
    }
}
