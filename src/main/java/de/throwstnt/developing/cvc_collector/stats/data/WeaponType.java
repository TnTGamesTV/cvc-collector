package de.throwstnt.developing.cvc_collector.stats.data;

public enum WeaponType {
	CARBINE("minecraft:iron_axe", "M4"), PISTOL("minecraft:wooden_pickaxe", "USP"), 
	SCOPED_RIFLE("minecraft:golden_axe", "Steyr AUG"), HANDGUN("minecraft:stone_pickaxe", "HK45"), 
	MAGNUM("minecraft:golden_pickaxe", "Deagle"), SNIPER("minecraft:bow", "50 Cal"), 
	RIFLE("minecraft:stone_hoe", "AK-47"), AUTO_SHOTGUN("minecraft:wooden_axe", "SPAS-12"), 
	SHOTGUN("minecraft:diamond_shovel", "Pump Action"), BULLPUP("minecraft:golden_shovel", "P90"), 
	SMG("minecraft:stone_shovel", "MP5"), KNIFE("minecraft:wooden_sword", "Knife");

	private String itemId;
	private String name;
	
	WeaponType(String itemId, String name) {
		this.itemId = itemId;
		this.name = name;
	}
	
	public String getItemId() {
		return itemId;
	}
	
	public String getName() {
		return name;
	}
}
