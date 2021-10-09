package de.throwstnt.developing.cvc_collector.util;

import java.util.Locale;
import net.labymod.main.LabyMod;
import net.labymod.support.util.Debug;
import net.labymod.support.util.Debug.EnumDebugMode;
import net.minecraft.util.text.TextFormatting;

public class ChatUtil {

	public static void sendChatMessage(String message) {
	  LabyMod.getInstance().displayMessageInChat(TextFormatting.DARK_BLUE + "[" + TextFormatting.BLUE + "CvcCollector" + TextFormatting.DARK_BLUE + "] " + TextFormatting.GRAY + message);
	}
	
	public static void log(String message) {
		Debug.log(EnumDebugMode.ADDON, "[CvcCollector] " + message);
	}
		
	/**
	 * Removes all color codes from a message
	 * @param message the message
	 * @return the cleaned message
	 */
	public static String cleanColorCoding(String message) {
		return message.replaceAll("§[0-9a-z]{1}", "");
	}
	
	public static final class Bytes {

	  private Bytes() {
	  }

	  public static String format(long value, Locale locale) {
	    if (value < 1024) {
	      return value + " B";
	    }
	    int z = (63 - Long.numberOfLeadingZeros(value)) / 10;
	    return String.format(locale, "%.1f %siB", (double) value / (1L << (z * 10)), " KMGTPE".charAt(z));
	  }
	}
}
