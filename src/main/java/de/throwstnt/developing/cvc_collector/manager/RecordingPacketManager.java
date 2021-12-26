package de.throwstnt.developing.cvc_collector.manager;

import java.util.UUID;
import de.throwstnt.developing.cvc_collector.db.DatabaseManager;
import de.throwstnt.developing.cvc_collector.db.data.Location;
import de.throwstnt.developing.cvc_collector.db.data.MovementEntry;
import de.throwstnt.developing.cvc_collector.db.data.PlayerEntry;
import de.throwstnt.developing.cvc_collector.manager.data.detector.MapDetector;
import de.throwstnt.developing.cvc_collector.manager.data.impl.CvcPlayer;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.server.SEntityPacket;
import net.minecraft.util.math.vector.Vector3d;

public class RecordingPacketManager {

    private static RecordingPacketManager instance;

    public static RecordingPacketManager getInstance() {
        if (instance == null)
            instance = new RecordingPacketManager();
        return instance;
    }

    @SuppressWarnings("resource")
    public void handleMovePacket(SEntityPacket packetIn) {
        Entity entity = packetIn.getEntity(Minecraft.getInstance().world);
        if (entity != null) {
            if (packetIn.func_229745_h_()) {
                Vector3d vector = packetIn.func_244300_a(entity.func_242274_V());

                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;

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

                            MovementEntry movementEntry = new MovementEntry(playerEntry, location,
                                    RoundManager.getInstance().getMillisSinceRoundStart());

                            DatabaseManager.getInstance().getLocationMap()
                                    .put(UUID.randomUUID().toString(), movementEntry);
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
}
