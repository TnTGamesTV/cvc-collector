package de.throwstnt.developing.cvc_collector.events;

import de.throwstnt.developing.cvc_collector.CvcCollector;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.network.server.DisconnectServerEvent;

public class DisconnectServerListener {

  @Subscribe
  public void onDisconnectServer(DisconnectServerEvent event) {
    CvcCollector.getInstance().loggedIntoHypixel = false;
  }
}
