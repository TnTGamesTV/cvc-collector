package de.throwstnt.developing.cvc_collector;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.throwstnt.developing.cvc_collector.state.ScoreboardManager;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ISuggestionProvider;

public class CommandHandler {

  private static CommandHandler instance;
  public static CommandHandler getInstance() {
    if(instance == null) instance = new CommandHandler();
    return instance;
  }
  
  /**
   * Initialize all brigadier commands
   */
  @SuppressWarnings("unchecked")
  public void init() {
    LiteralArgumentBuilder<?> collectorCommand = literal("collector").executes(e -> {
      ChatUtil.sendChatMessage("CvcCollector Addon by ThrowsTnT");
      ChatUtil.sendChatMessage("Addon enabled: " + CvcCollector.getInstance().enabled);
      ChatUtil.sendChatMessage("Client connected to Hypixel: " + CvcCollector.getInstance().loggedIntoHypixel);
      return 1;
    }).then(literal("scoreboard").executes(e -> {
      ChatUtil.sendChatMessage("Dumping scoreboard into log :D");
      ScoreboardManager.checkScoreboard();
      return 1;
    }));
    
    Minecraft.getInstance().getConnection().getCommandDispatcher().register((LiteralArgumentBuilder<ISuggestionProvider>) collectorCommand);
  }
}
