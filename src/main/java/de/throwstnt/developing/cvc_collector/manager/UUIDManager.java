package de.throwstnt.developing.cvc_collector.manager;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;

public class UUIDManager {

    private static UUIDManager instance;

    public static UUIDManager getInstance() {
        if (instance == null)
            instance = new UUIDManager();
        return instance;
    }

    private UUIDManager() {

    }

    public List<UUID> getUUIDList() {
        if (Minecraft.getInstance() != null) {
            if (Minecraft.getInstance().getConnection() != null) {
                if (Minecraft.getInstance().getConnection().getPlayerInfoMap() != null) {
                    return Minecraft.getInstance().getConnection().getPlayerInfoMap().stream()
                            .map(playerInfo -> playerInfo.getGameProfile().getId())
                            .collect(Collectors.toList());
                }
            }
        }
        return Lists.newArrayList();
    }

    /**
     * Returns the name of a possibly nicked player from the tab list
     * 
     * @param uuid the uuid of the nicked player
     * @return the name
     */
    public String getNameFromUUID(UUID uuid) {
        if (Minecraft.getInstance() != null) {
            if (Minecraft.getInstance().getConnection() != null) {
                if (Minecraft.getInstance().getConnection().getPlayerInfoMap() != null) {
                    List<GameProfile> gameProfiles =
                            Minecraft.getInstance().getConnection().getPlayerInfoMap().stream()
                                    .map(playerInfo -> playerInfo.getGameProfile())
                                    .collect(Collectors.toList());

                    synchronized (gameProfiles) {
                        return gameProfiles.stream()
                                .filter(gameProfile -> gameProfile.getId().equals(uuid))
                                .map(GameProfile::getName).findFirst().orElse("");
                    }
                }
            }
        }
        return "";
    }

    /**
     * Returns true if the given UUID does exist in the lobby
     * 
     * @param uuid the uuid
     * @return true if the uuid does exist in the lobby
     */
    public boolean doesUUIDExist(UUID uuid) {
        if (Minecraft.getInstance() != null) {
            if (Minecraft.getInstance().getConnection() != null) {
                if (Minecraft.getInstance().getConnection().getPlayerInfoMap() != null) {
                    return Minecraft.getInstance().getConnection().getPlayerInfoMap().stream()
                            .anyMatch(
                                    playerInfo -> playerInfo.getGameProfile().getId().equals(uuid));
                }
            }
        }
        return false;
    }
}
