package de.throwstnt.developing.cvc_collector.stats.data;

public enum OtherType {
	FLAME_GRENADE,
	FLASH_GRENADE,
	DECOY_GRENADE,
	SMOKE_GRENADE,
	FRAG_GRENADE,
	
	OBJECTIVE_COMPLETE,
	OBJECTIVE_INCOMPLETE,
	
	HEALTH,
	HEADSHOT,
	FALL_DAMAGE,
	
	TEAM_COPS,
	TEAM_CRIMS,
	
	ARMOR_HELMET,
	ARMOR_CHESTPLATE,
	
	WIRE_CUTTERS,
	C4;
	
	/**
	 * Returns true if the given type is a grenade
	 * @param type a type
	 * @return true if the given type is a grenade
	 */
	public static boolean isGrenade(OtherType type) {
		return type == FLAME_GRENADE || type == FLASH_GRENADE || type == DECOY_GRENADE || type == SMOKE_GRENADE || type == FRAG_GRENADE;
	}
}
