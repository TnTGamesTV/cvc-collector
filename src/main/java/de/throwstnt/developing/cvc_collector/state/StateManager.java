package de.throwstnt.developing.cvc_collector.state;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import de.throwstnt.developing.cvc_collector.CvcCollector;

public class StateManager {

  public static enum State {
      UNKNOWN, CVC_LOBBY, CVC_WAITING, CVC_GAME_DEFUSAL, CVC_GAME_TDM;
    
    public static boolean isInGame(State state) {
      return state == CVC_GAME_DEFUSAL || state == CVC_GAME_TDM;
    }
  }
  
  private static StateManager instance;
  
  public static StateManager getInstance() {
      if(instance == null) instance = new StateManager();
      return instance;
  }
  
  private State state;
  
  private List<BiConsumer<State, State>> listeners;
  
  private StateManager() {
      this.state = State.UNKNOWN;
      
      this.listeners = new ArrayList<>();
  }
  
  /**
   * Gets the current state
   * @return the current state
   */
  public State getState() {
      return this.state;
  }
  
  /**
   * Checks if the state has changed and updates
   */
  public synchronized void updateState() {
      if(CvcCollector.getInstance().loggedIntoHypixel) {
          State newState = ScoreboardManager.getInstance().getStateFromSidebar();
          
          this.updateState(newState);
      }
  }
  
  /**
   * Updates the current state to the new state (if changed)
   * @param newState the new state
   */
  public synchronized void updateState(State newState) {
      if(this.state != newState) {
          State oldState = this.state;
          
          this.state = newState;
          
          synchronized (this.listeners) {
              for(BiConsumer<State,State> listener : this.listeners) {
                  listener.accept(oldState, newState);
              }
          }
      }
  }
  
  /**
   * Registers a given listener in the format of (oldState, newState) -> {}
   * @param listener the listener
   */
  public void register(BiConsumer<State, State> listener) {
      synchronized (this.listeners) {
          this.listeners.add(listener);
      }
  }
}