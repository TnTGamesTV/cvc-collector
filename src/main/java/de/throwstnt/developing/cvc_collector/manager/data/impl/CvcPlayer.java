package de.throwstnt.developing.cvc_collector.manager.data.impl;

import java.util.UUID;
import de.throwstnt.developing.cvc_collector.db.DatabaseManager;
import de.throwstnt.developing.cvc_collector.db.data.PlayerEntry;
import de.throwstnt.developing.cvc_collector.manager.CvcPlayerManager;
import de.throwstnt.developing.cvc_collector.manager.TeamManager;
import de.throwstnt.developing.cvc_collector.manager.data.Data;
import de.throwstnt.developing.cvc_collector.stats.api.ApiCoordinator;
import de.throwstnt.developing.cvc_collector.stats.data.CvcStats;

public class CvcPlayer extends Data<UUID> {

    private PlayerEntry entry;

    private UUID uuid;

    private CvcStats stats;

    public CvcPlayer(final String entryId, UUID uuid) {
        this.uuid = uuid;

        ApiCoordinator.getPlayer(uuid, reply -> {
            this.stats = CvcStats.fromReply(reply);

            String team = TeamManager.getInstance().getTeam(uuid);
            int score = this.stats.getScore();
            double virtualKillDeathRatio = this.stats.virtualKillDeathRatio;
            long createdAt = System.currentTimeMillis();

            this.entry = new PlayerEntry(entryId, this.uuid.toString(), team, score,
                    virtualKillDeathRatio, createdAt);

            DatabaseManager.getInstance().getPlayerMap().put(entryId, this.entry);
        });
    }

    @Override
    public UUID getIdentification() {
        return this.uuid;
    }

    @Override
    public boolean identify(UUID identification) {
        return identification.equals(this.uuid);
    }

    @Override
    public void remove() {
        CvcPlayerManager.getInstance().remove(this);
    }

    public PlayerEntry getEntry() {
        return entry;
    }

    public UUID getUuid() {
        return uuid;
    }

    public CvcStats getStats() {
        return stats;
    }
}
