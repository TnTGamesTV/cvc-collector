package de.throwstnt.developing.cvc_collector.manager;

import de.throwstnt.developing.cvc_collector.db.DatabaseManager;
import de.throwstnt.developing.cvc_collector.state.StateManager;
import de.throwstnt.developing.cvc_collector.state.StateManager.State;
import de.throwstnt.developing.cvc_collector.util.ChatUtil;

public class RoundManager {

    private static RoundManager instance;

    public static RoundManager getInstance() {
        if (instance == null)
            instance = new RoundManager();
        return instance;
    }

    private long lastRoundStartAt;

    private boolean isRoundActive;

    public RoundManager() {
        this.lastRoundStartAt = 0;
        this.isRoundActive = false;

        StateManager.getInstance().register((oldState, newState) -> {
            if (State.isInGame(oldState)) {
                this.isRoundActive = false;
                this.lastRoundStartAt = 0;

                ChatUtil.log("Stopped the current round (reason: state-change)");
            }
        });
    }

    public void startRound() {
        this.lastRoundStartAt = System.currentTimeMillis();
        this.isRoundActive = true;

        int size = CvcPlayerManager.getInstance().find(null).size();

        ChatUtil.sendChatMessage("Started a new round with " + size + " players");
    }

    /**
     * Returns true if a current round is active
     */
    public boolean isRoundActive() {
        return this.isRoundActive;
    }

    /**
     * Returns the milliseconds since the last round started
     */
    public long getMillisSinceRoundStart() {
        return System.currentTimeMillis() - this.lastRoundStartAt;
    }

    public void stopRound() {
        this.isRoundActive = false;
        this.lastRoundStartAt = 0;

        // update cvc player list
        UUIDManager.getInstance().getUUIDList()
                .forEach(uuid -> ChatUtil.log("Playing with: " + uuid));

        UUIDManager.getInstance().getUUIDList().forEach(CvcPlayerManager.getInstance()::get);

        ChatUtil.log("Stopped the current round (reason: win-or-loss)");

        ChatUtil.log("Committing " + DatabaseManager.getInstance().getCountSinceLastCommit()
                + " entries to file system");
        DatabaseManager.getInstance().save();
    }

    public void stopRoundAndGame() {
        this.isRoundActive = false;
        this.lastRoundStartAt = 0;

        CvcPlayerManager.getInstance().remove();

        ChatUtil.log("Stopped the current round (reason: end-of-game)");
    }
}
