package de.throwstnt.developing.cvc_collector.state;

import de.throwstnt.developing.cvc_collector.state.StateManager.State;
import de.throwstnt.developing.cvc_collector.util.ScoreboardUtil;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;
import de.throwstnt.developing.cvc_collector.util.SymbolLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

public class ScoreboardManager {
  
  private static ScoreboardManager instance;
  
  public static ScoreboardManager getInstance() {
      if(instance == null) instance = new ScoreboardManager();
      return instance;
  }
  
  @SuppressWarnings("resource")
  public Scoreboard getSidebar() {
      if(Minecraft.getInstance() != null) {
          if(Minecraft.getInstance().world != null) {
              return Minecraft.getInstance().world.getScoreboard();
          }
      }
      return null;
  }
  
  public State getStateFromSidebar() {
      Scoreboard scoreboard = this.getSidebar();
      
      if(scoreboard != null) {
          ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);
              
          if(sidebarObjective != null) {
              String sidebarTitle = sidebarObjective.getDisplayName().getString();
              
              sidebarTitle = ChatUtil.cleanColorCoding(sidebarTitle);
              
              boolean isInCvc = sidebarTitle.contains("COPS AND CRIMS") || sidebarTitle.contains("TEAM DEATHMATCH") || sidebarTitle.contains(SymbolLibrary.COP_SYMBOL);
              
              if(isInCvc) {
                  String totalText = ScoreboardUtil.getCvcScoreboardText(scoreboard);
                  
                  if(totalText != null) {
                      //is in cvc lobby
                      if(totalText.contains("Coins:")) return State.CVC_LOBBY;
                      
                      //is waiting for game to start
                      if(totalText.contains("Mode:")) return State.CVC_WAITING;

                      //is in game (defusal) 
                      if(totalText.contains("Armor:")) return State.CVC_GAME_DEFUSAL;
                      
                      //is in game (tdm)
                      if(totalText.contains("Points:")) return State.CVC_GAME_TDM;
                  }
              } 
          } 
      }
      
      return State.UNKNOWN;
  }
  
  
  public static void checkScoreboard() {
      Scoreboard scoreboard = ScoreboardManager.getInstance().getSidebar();
      
      if(scoreboard != null) {
          ScoreObjective sidebarObjective = scoreboard.getObjectiveInDisplaySlot(1);
              
          if(sidebarObjective != null) {
              ChatUtil.log("Sidebar: " + sidebarObjective.getDisplayName());
              String totalText = ScoreboardUtil.getCvcScoreboardText(scoreboard);
              ChatUtil.log(totalText);
              
              scoreboard.getTeams().forEach((team) -> {
                  ChatUtil.log("Team " + team.getName() + "; prefix=" + team.getPrefix().getString() + "; suffix=" + team.getSuffix().getString());
              });
          }
      }
  }
}