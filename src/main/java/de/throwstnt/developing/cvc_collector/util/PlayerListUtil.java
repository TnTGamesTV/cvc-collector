package de.throwstnt.developing.cvc_collector.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.overlay.PlayerTabOverlayGui;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.NetworkPlayerInfo;

public class PlayerListUtil {

	/**
	 * Returns all players that are available to the client
	 * @return the list of game profiles
	 */
	public static List<GameProfile> getAllPlayers() {
		final ClientPlayNetHandler clientPlayNetHandler = Minecraft.getInstance().getConnection();

		if (clientPlayNetHandler != null) {
			final Collection<NetworkPlayerInfo> playerInfoMap = clientPlayNetHandler.getPlayerInfoMap();
			final PlayerTabOverlayGui tabOverlay = Minecraft.getInstance().ingameGUI.getTabList();
			
			return playerInfoMap.stream().map((playerInfo) -> {
				return playerInfo.getGameProfile();
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
}
