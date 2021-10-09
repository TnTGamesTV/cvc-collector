package de.throwstnt.developing.cvc_collector.events;

import de.throwstnt.developing.cvc_collector.CommandHandler;
import de.throwstnt.developing.cvc_collector.CvcCollector;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.network.server.LoginServerEvent;
import net.labymod.main.LabyMod;

public class LoginServerListener {

  @Subscribe
  public void onLoginServer(LoginServerEvent event) {
    if(CvcCollector.getInstance().enabled) {
      ChatUtil.log("Server event with ip = " + event.getServerData().getIp());
      
      if(event.getServerData().getIp().contains("hypixel.net")) {
        if(!CvcCollector.getInstance().loggedIntoHypixel) {
          CvcCollector.getInstance().loggedIntoHypixel = true;
          
          //register addon commands
          CommandHandler.getInstance().init();
          
          LabyMod.getInstance().displayMessageInChat("§1[§9CvcCollector§1] §7Addon is ready.");
        }
      }
    }
  }
}
