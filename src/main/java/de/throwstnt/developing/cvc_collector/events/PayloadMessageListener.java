package de.throwstnt.developing.cvc_collector.events;

import java.util.Timer;
import java.util.TimerTask;
import de.throwstnt.developing.cvc_collector.CvcCollector;
import de.throwstnt.developing.cvc_collector.state.StateManager;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.network.PayloadMessageEvent;

public class PayloadMessageListener {

  @Subscribe
  public void onPayloadMessage(PayloadMessageEvent event) {
    if(CvcCollector.getInstance().enabled) {
      if(event.getChannelName().equalsIgnoreCase("minecraft:brand")) {
         ChatUtil.log("Received minecraft:brand");
         ChatUtil.log("loggedIntoHypixel: " + CvcCollector.getInstance().loggedIntoHypixel);
        
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                StateManager.getInstance().updateState();
            }}, 500);
    }
    }
  }
}
