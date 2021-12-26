package de.throwstnt.developing.cvc_collector.events;

import de.throwstnt.developing.cvc_collector.manager.RoundManager;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.network.IncomingPacketEvent;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;

public class IncomingPacketListener {

  @Subscribe
  public void onIncomingPacket(IncomingPacketEvent event) {
     Object packet = event.getPacket();
     
     if(packet instanceof SPlaySoundEffectPacket) {
       SPlaySoundEffectPacket castedPacket = (SPlaySoundEffectPacket) packet;
       
       if(castedPacket.getSound().getName().equals("mcgo.gamesounds.roundstart")) {
           //triggers when the countdown for the round is done
           new Thread(new Runnable() {

               @Override
               public void run() {
                 RoundManager.getInstance().startRound();
               }}).start();
       }
   }
  }
}
