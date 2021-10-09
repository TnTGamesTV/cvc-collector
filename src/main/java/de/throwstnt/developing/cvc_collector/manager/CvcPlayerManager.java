package de.throwstnt.developing.cvc_collector.manager;

import java.util.UUID;
import de.throwstnt.developing.cvc_collector.manager.data.impl.CvcPlayer;

public class CvcPlayerManager extends DataManager<CvcPlayer, UUID> {

    private static CvcPlayerManager instance;

    public static CvcPlayerManager getInstance() {
        if (instance == null)
            instance = new CvcPlayerManager();
        return instance;
    }

    @Override
    public CvcPlayer create(UUID identification) {
        String entryId = UUID.randomUUID().toString();

        return new CvcPlayer(entryId, identification);
    }

    @Override
    public boolean isAllowedToAdd(CvcPlayer item) {
        if (item == null)
            return false;

        return UUIDManager.getInstance().doesUUIDExist(item.getIdentification());
    }
}
