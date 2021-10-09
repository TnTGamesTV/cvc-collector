package de.throwstnt.developing.cvc_collector.mixin;

import java.util.Timer;
import java.util.TimerTask;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import de.throwstnt.developing.cvc_collector.manager.RecordingPacketManager;
import de.throwstnt.developing.cvc_collector.manager.RoundManager;
import de.throwstnt.developing.cvc_collector.util.SoundUtil;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.play.server.SEntityPacket;
import net.minecraft.network.play.server.SPlaySoundPacket;

@Mixin(value = ClientPlayNetHandler.class)
public class NetworkPlayerMixin {

    @Inject(method = "handleEntityMovement", at = @At("TAIL"))
    public void handleEntityMovement(SEntityPacket packetIn, CallbackInfo info) {
        if (RoundManager.getInstance().isRoundActive()) {
            new Thread(() -> RecordingPacketManager.getInstance().handleMovePacket(packetIn))
                    .start();
        }
    }

    @Inject(method = "handleCustomSound", at = @At("TAIL"))
    public void handleCustomSound(SPlaySoundPacket packetIn, CallbackInfo info) {
        switch (packetIn.getSoundName().getPath()) {
            case SoundUtil.SOUND_COPS_WON_GAME:
            case SoundUtil.SOUND_CRIMS_WON_GAME:
                if (RoundManager.getInstance().isRoundActive()) {
                    new Thread(() -> RoundManager.getInstance().stopRoundAndGame()).start();
                }
                break;

            case SoundUtil.SOUND_COPS_WON_ROUND:
            case SoundUtil.SOUND_CRIMS_WON_ROUND:
                new Timer().schedule(new TimerTask() {

                    @Override
                    public void run() {
                        if (RoundManager.getInstance().isRoundActive()) {
                            new Thread(() -> RoundManager.getInstance().stopRound()).start();
                        }
                    }
                }, 7000);
                break;

            case SoundUtil.SOUND_ROUND_STARTED:
                new Thread(() -> RoundManager.getInstance().startRound()).start();
                break;
        }
    }
}
