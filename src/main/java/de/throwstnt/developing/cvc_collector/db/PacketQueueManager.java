package de.throwstnt.developing.cvc_collector.db;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import de.throwstnt.developing.cvc_collector.db.data.Location;
import de.throwstnt.developing.cvc_collector.db.data.MovementEntry;
import de.throwstnt.developing.cvc_collector.db.data.PlayerEntry;
import de.throwstnt.developing.cvc_collector.manager.CvcPlayerManager;
import de.throwstnt.developing.cvc_collector.manager.RoundManager;
import de.throwstnt.developing.cvc_collector.manager.UUIDManager;
import de.throwstnt.developing.cvc_collector.manager.data.detector.MapDetector;
import de.throwstnt.developing.cvc_collector.manager.data.impl.CvcPlayer;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.server.SEntityPacket;
import net.minecraft.util.math.vector.Vector3d;

public class PacketQueueManager {

    private static PacketQueueManager instance;

    public static PacketQueueManager getInstance() {
        if (instance == null)
            instance = new PacketQueueManager();
        return instance;
    }

    private Queue<SEntityPacket> packetQueue;

    private Queue<MovementEntry> entryQueue;

    private ExecutorService packetExecutor = Executors.newFixedThreadPool(32);
    private ExecutorService entryExecutor = Executors.newFixedThreadPool(32);

    private PacketQueueManager() {
        this.packetQueue = new ConcurrentLinkedQueue<>();
        this.entryQueue = new ConcurrentLinkedQueue<>();
    }

    public void queue(SEntityPacket packetIn) {
        this.packetQueue.add(packetIn);

        packetExecutor.submit(() -> {
            if (!this.packetQueue.isEmpty()) {
                SEntityPacket packet = this.packetQueue.remove();

                MovementEntry possibleMovementEntry = this._convertToEntry(packet);

                if (possibleMovementEntry != null) {
                    this.entryQueue.add(possibleMovementEntry);

                    this.entryExecutor.submit(() -> {
                        MovementEntry entry = this.entryQueue.poll();

                        if (entry != null) {
                            DatabaseManager.getInstance().getLocationMap()
                                    .put(UUID.randomUUID().toString(), entry);
                        }
                    });
                }
            }
        });
    }

    @SuppressWarnings("resource")
    private MovementEntry _convertToEntry(SEntityPacket packet) {
        Entity entity = packet.getEntity(Minecraft.getInstance().world);
        if (entity != null) {
            if (packet.func_229745_h_()) {
                Vector3d vector = packet.func_244300_a(entity.func_242274_V());

                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;

                    UUID uuid = UUIDManager.getInstance().get(player.getGameProfile().getName());

                    /*
                     * A null uuid means this user is unknown (and likely nicked). As everyone
                     * should we ignore nicked players because they are impossible to deal with. In
                     * code and in real life!
                     */
                    if (uuid != null) {
                        CvcPlayer cvcPlayer =
                                CvcPlayerManager.getInstance().get(player.getGameProfile().getId());

                        if (cvcPlayer != null) {
                            double x = vector.x;
                            double y = vector.y;
                            double z = vector.z;

                            String map = MapDetector.getInstance().getCurrentMap();

                            PlayerEntry playerEntry = cvcPlayer.getEntry();

                            if (playerEntry != null) {
                                Location location = new Location(map, x, y, z);

                                MovementEntry movementEntry = new MovementEntry(playerEntry,
                                        location,
                                        RoundManager.getInstance().getMillisSinceRoundStart());

                                return movementEntry;
                            } else {
                                ChatUtil.log("Player '" + player.getGameProfile().getName()
                                        + "' has no PlayerEntry");
                            }
                        } else {
                            ChatUtil.log("Player '" + player.getGameProfile().getName()
                                    + "' did not exist as a CvcPlayer");
                        }
                    }
                }
            }
        }

        return null;
    }
}
