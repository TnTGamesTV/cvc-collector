package de.throwstnt.developing.cvc_collector.events;

import java.util.Locale;
import de.throwstnt.developing.cvc_collector.CvcCollector;
import de.throwstnt.developing.cvc_collector.db.DatabaseManager;
import de.throwstnt.developing.cvc_collector.manager.RoundManager;
import de.throwstnt.developing.cvc_collector.state.ScoreboardManager;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.events.client.chat.MessageSendEvent;


public class ChatMessageListener {

  @Subscribe
  public void onChatMessage(MessageSendEvent event) {
    if(CvcCollector.getInstance().enabled) {
      if(event.getMessage().startsWith("/cvcdumpscoreboard")) {
        event.setCancelled(true);
        
        ChatUtil.sendChatMessage("Dumping scoreboard");
        ScoreboardManager.checkScoreboard();
      } else if(event.getMessage().startsWith("/cvcdatabase")) {
        event.setCancelled(true);
        String size = ChatUtil.Bytes.format(DatabaseManager.getInstance().getDbFile().length(), Locale.GERMANY);

        ChatUtil.sendChatMessage("Database size: " + size);
        ChatUtil.sendChatMessage("Database path: " + DatabaseManager.getInstance().getDbFile().getAbsolutePath());
      } else if(event.getMessage().startsWith("/startround")) {
        event.setCancelled(true);

        ChatUtil.sendChatMessage("Starting round");
        
        RoundManager.getInstance().startRound();
      } else if(event.getMessage().startsWith("/stopround")) {
        event.setCancelled(true);

        ChatUtil.sendChatMessage("Stopping round");
        
        RoundManager.getInstance().stopRound();
      }
    }
  }
}
