package de.throwstnt.developing.cvc_collector.stats.api;

import java.util.UUID;

public class UUIDCache extends AbstractCache<String, UUID> {

    public UUIDCache() {
        super(32);
    }
}
