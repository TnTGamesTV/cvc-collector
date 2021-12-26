package de.throwstnt.developing.cvc_collector.stats.api;

import java.util.UUID;

public class PlayerObject {

    public String name;
    public UUID uuid;

    public PlayerObject(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
