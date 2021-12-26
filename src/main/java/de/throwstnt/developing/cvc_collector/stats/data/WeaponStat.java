package de.throwstnt.developing.cvc_collector.stats.data;

import java.util.List;

public class WeaponStat {
	
	/**
	 * The weapon
	 */
	public WeaponType type;
	
	/**
	 * The total kills with this weapon
	 */
	public int kills;
	
	/**
	 * The total headshots with this weapon
	 */
	public int headshots;

	/**
	 * The accuracy with the weapon (0-1)
	 */
	public double virtualHeadshotAccuracy;
	
	/**
	 * All upgrades
	 */
	public List<WeaponUpgrade> upgrades;
		
	/**
	 * if the weapon is the primary weapon (e.g. m4 or ak47)
	 */
	public boolean isPrimary;
	
	/**
	 * if the weapon is the secondary weapon (e.g. pistol or handgun)
	 */
	public boolean isSecondary;
	
	/**
	 * Updates all virtual scores
	 */
	public void updateVirtuals() {
		this.virtualHeadshotAccuracy = (double) this.headshots / Math.max((double) this.kills, 1.0);
	}
}
