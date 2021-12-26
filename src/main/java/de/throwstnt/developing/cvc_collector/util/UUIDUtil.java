package de.throwstnt.developing.cvc_collector.util;

import java.util.UUID;

public class UUIDUtil {

	public static String convert(UUID uuid) {
		return uuid.toString().replace("-", "");
	}
}
