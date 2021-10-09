package de.throwstnt.developing.cvc_collector.stats.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import com.google.gson.JsonObject;
import net.hypixel.api.reply.PlayerReply;
import net.minecraft.util.text.TextFormatting;

public class CvcStats {
	
	private UUID uuid;
	
	// generic stats
	public int coins; // coins
	
	//global stats
	public int gameWins; //virtual
	public int gamesLost; //virtual
	public int kills; // virtual
	public int shotsFired; // "shots_fired"
	public int headshotKills; // "headshot_kills"
	public int grenadeKills; // "grenade_kills"
	public int assists; // virtual
	public int deaths; // virtual
	public int killsAsCop; // virtual
	public int killsAsCriminal; // virtual
	
	//defusal
	public int defusalGamesPlayed; // "game_plays"
	public int defusalGameWins; // "game_wins"
	public int defusalRoundWins; // "round_wins"
	public int defusalGamesLost; // virtual
	public int defusalKills; // "kills"
	public int defusalAssists; // "assists"
	public int defusalDeaths; // "deaths"
	public int defusalKillsAsCop; // "cop_kills"
	public int defusalKillsAsCriminal; // "criminal_kills"
	public int defusalBombsDefused; // "bombs_defused"
	public int defusalBombsPlanted; // "bombs_planted"
	
	//tdm
	public int tdmGamesPlayed; // "game_plays_deathmatch"
	public int tdmGameWins; // "game_wins_deathmatch"
	public int tdmGamesLost; // virtual
	public int tdmKills; // "kills_deathmatch"
	public int tdmAssists; // assists_deathmatch
	public int tdmDeaths; // "deaths_deatchmatch"
	public int tdmKillsAsCop; // cop_kills_deathmatch
	public int tdmKillsAsCriminal; // criminal_kills_deathmatch
	
	//virtual ratios
	public double virtualKillDeathRatio; 
	public double virtualDefusalKillDeathRatio;
	public double virtualTdmKillDeathRatio;
	public double virtualWinLooseRatio;
	public double virtualDefusalWinLooseRatio;
	public double virtualTdmWinLooseRatio;

	public double virtualHeadshotRatio;
	
	
	public List<WeaponStat> weapons;
	
	public WeaponStat possiblePrimaryWeapon;
	public WeaponStat possibleSecondaryWeapon;
	
	public int characterPocketChangeUpgrade;
	public int characterStrengthTrainingUpgrade;
	public int characterBountyHounterUpgrade;
	public int armorCostReductionUpgrade;
	
	/**
	 * Generates a CvcStats object from a player api reply
	 * @param reply the reply
	 * @return the CvcStats or null
	 */
	public static CvcStats fromReply(PlayerReply reply) {
		if(reply != null) {
			if(reply.getPlayer() != null) {
				if(reply.getPlayer().has("stats")) {
					if(reply.getPlayer().get("stats").getAsJsonObject().has("MCGO")) {
						return new CvcStats(reply.getPlayer().get("stats").getAsJsonObject().get("MCGO").getAsJsonObject());
					}
				}
			}
		}
		
		return null;
	}
	
	public CvcStats(JsonObject o) {
		if(o != null) {
			this.coins = this._getStat(o, "coins", 0);
			
			//general
			this.shotsFired = this._getStat(o, "shots_fired", 0); 
			this.headshotKills = this._getStat(o, "headshot_kills", 0);
			this.grenadeKills = this._getStat(o, "grenade_kills", 0);
			
			//defusal
			this.defusalGamesPlayed = this._getStat(o, "game_plays", 0); 
			this.defusalGameWins = this._getStat(o, "game_wins", 0); 
			this.defusalRoundWins = this._getStat(o, "round_wins", 0); 
			this.defusalKills = this._getStat(o, "kills", 0); 
			this.defusalAssists = this._getStat(o, "assists", 0); 
			this.defusalDeaths = this._getStat(o, "deaths", 0); 
			this.defusalKillsAsCop = this._getStat(o, "cop_kills", 0);
			this.defusalKillsAsCriminal = this._getStat(o, "criminal_kills", 0);
			this.defusalBombsDefused = this._getStat(o, "bombs_defused", 0);
			this.defusalBombsPlanted = this._getStat(o, "bombs_planted", 0);
			
			//tdm
			this.tdmGamesPlayed = this._getStat(o, "game_plays_deathmatch", 0); // "game_plays_deathmatch"
			this.tdmGameWins = this._getStat(o, "game_wins_deathmatch", 0); // "game_wins_deathmatch"
			this.tdmKills = this._getStat(o, "kills_deathmatch", 0); // "kills_deathmatch"
			this.tdmAssists = this._getStat(o, "assists_deathmatch", 0); // assists_deathmatch
			this.tdmDeaths = this._getStat(o, "deaths_deathmatch", 0); // "deaths_deatchmatch"
			this.tdmKillsAsCop = this._getStat(o, "cop_kills_deathmatch", 0); // cop_kills_deathmatch
			this.tdmKillsAsCriminal = this._getStat(o, "criminal_kills_deathmatch", 0); // criminal_kills_deathmatch
			
			//virtual
			this.gameWins = this.defusalGameWins + this.tdmGameWins;
			this.kills = this.defusalKills + this.tdmKills; // virtual
			this.assists = this.defusalAssists + this.tdmAssists;
			this.deaths = this.defusalDeaths + this.tdmDeaths; // virtual
			this.killsAsCop = this.defusalKillsAsCop + this.tdmKillsAsCop; // virtual
			this.killsAsCriminal = this.defusalKillsAsCriminal + this.tdmKillsAsCriminal; // virtual
			
			this.defusalGamesLost = this.defusalGamesPlayed - this.defusalGameWins;
			this.tdmGamesLost = this.tdmGamesPlayed - this.tdmGameWins;
			this.gamesLost = this.defusalGamesLost + this.tdmGamesLost;
			
			//virtual ratios
			this.virtualKillDeathRatio = (double) this.kills / Math.max((double) this.deaths, 1.0);
			this.virtualDefusalKillDeathRatio = (double) this.defusalKills / Math.max((double) this.defusalDeaths, 1.0);
			this.virtualTdmKillDeathRatio = (double) this.tdmKills / Math.max((double) this.tdmDeaths, 1.0);

			this.virtualWinLooseRatio = (double) this.gameWins / Math.max((double) this.gamesLost, 1.0);
			this.virtualDefusalWinLooseRatio = (double) this.defusalGameWins / Math.max((double) this.defusalGamesLost, 1.0);
			this.virtualTdmWinLooseRatio = (double) this.tdmGameWins / Math.max((double) this.tdmGamesLost, 1.0);
			
			this.virtualHeadshotRatio = (double) this.headshotKills / Math.max((double) this.kills, 1.0);
			
			//character
			this.characterPocketChangeUpgrade = this._getStat(o, "pocket_change", 1);
			this.characterStrengthTrainingUpgrade = this._getStat(o, "strength_training", 1);
			this.characterBountyHounterUpgrade = this._getStat(o, "bounty_hunter", 1);
			this.armorCostReductionUpgrade = this._getStat(o, "armor_cost", 1);
			
			List<WeaponType> allWeapons = Arrays.asList(WeaponType.values());
			
			this.weapons = allWeapons.stream().map((type) -> {
				return _makeWeaponStat(o, type);
			}).collect(Collectors.toList());
			this.weapons.sort((a, b) -> Integer.compare(b.kills, a.kills));
			
			Optional<WeaponStat> optionalPrimaryWeapon = this.weapons.stream().filter((weapon) -> weapon.isPrimary).sorted((a, b) -> Integer.compare(b.kills, a.kills)).findFirst();
			if(optionalPrimaryWeapon.isPresent()) this.possiblePrimaryWeapon = optionalPrimaryWeapon.get();
			
			Optional<WeaponStat> optionalSecondaryWeapon = this.weapons.stream().filter((weapon) -> weapon.isSecondary).sorted((a, b) -> Integer.compare(b.kills, a.kills)).findFirst();
			if(optionalSecondaryWeapon.isPresent()) this.possibleSecondaryWeapon= optionalSecondaryWeapon.get();
		}
	}
	
	public int getScore() {
		if(this.shotsFired == 0) return 0;
		
		return (this.kills / 2) + ((this.defusalBombsDefused + this.defusalBombsPlanted) / 3) + this.gameWins + ((this.kills / this.shotsFired) * 200);
	}
	
	private int _getStat(JsonObject o, String memberName, int defaultValue) {
		if(o.has(memberName)) {
			return o.get(memberName).getAsInt();
		} 
		return defaultValue;
	}
	
	/**
	 * Returns the required weapon name for retrieving data
	 * @param type the weapons type
	 * @return the name
	 */
	private String _weaponTypeToName(WeaponType type) {
		switch(type) {
			default: return "";
			case AUTO_SHOTGUN: return "auto_shotgun";
			case SCOPED_RIFLE: return "scoped_rifle";
			case BULLPUP: return "bullpup";
			case CARBINE: return "carbine";
			case HANDGUN: return "handgun";
			case MAGNUM: return "magnum";
			case PISTOL: return "pistol";
			case RIFLE: return "rifle";
			case SMG: return "smg";
			case SNIPER: return "sniper";
			case KNIFE: return "knife";
			case SHOTGUN: return "shotgun";
		}
	}
	
	/**
	 * Returns the required weapon name for retrieving kills and headshots
	 * @param type the weapons type
	 * @return the name
	 */
	private String _weaponTypeToCamelCaseName(WeaponType type) {
		switch(type) {
			default: return _weaponTypeToName(type);
			case AUTO_SHOTGUN: return "autoShotgun";
			case SCOPED_RIFLE: return "scopedRifle";
		}
	}
	
	/**
	 * Returns the appropriate color code for the current score
	 * @return
	 */
	public TextFormatting getColorCodeFromScore() {
		int score = this.getScore();
		
		if(score >= 0 && score < 2500) {
			//gray
			return TextFormatting.GRAY;
		} else if(score >= 2500 && score < 5000) {
			//white
			return TextFormatting.WHITE;
		} else if(score >= 5000 && score < 20000) {
			//yellow
			return TextFormatting.YELLOW;
		} else if(score >= 20000 && score < 50000) {
			//gold
			return TextFormatting.GOLD;
		} else if(score >= 50000 && score < 100000) {
			//dark aqua
			return TextFormatting.DARK_AQUA;
		} else if(score >= 100000) {
			//red
			return TextFormatting.RED;
		}
		return TextFormatting.GRAY;
	}
	
	/**
	 * Creates a weapons stats
	 * @param o the data object
	 * @param type the weapons type
	 * @return the weapons stats
	 */
	private WeaponStat _makeWeaponStat(JsonObject o, WeaponType type) {
		WeaponStat stat = new WeaponStat();
		List<WeaponUpgrade> upgrades = new ArrayList<>();
		
		String weaponKillsSuffix = "Kills";
		String weaponHeadshotsSuffix = "Headshots";
		
		String genericDamageIncreaseUpgradeSuffix = "_damage_increase";
		String genericCostReductionUpgradeSuffix = "_cost_reduction";
		String weaponReloadSpeedReductionUpgradeSuffix = "_reload_speed_reduction";
		String weaponRecoilReductionUpgradeSuffix = "_recoil_reduction";
		String knifeAttackDelayReductionUpgradeSuffix = "_attack_delay";
		String sniperChargeBonusUpgradeSuffix = "sniper_charge_bonus";
		
		String weaponName = this._weaponTypeToName(type);
		String weaponNameAsCamelCase = this._weaponTypeToCamelCaseName(type);
		
		if(type == WeaponType.KNIFE) {
			int damageIncreaseUpgrade =  this._getStat(o, weaponName + genericDamageIncreaseUpgradeSuffix, 1);
			int knifeAttackDelayReductionUpgrade = this._getStat(o, weaponName + knifeAttackDelayReductionUpgradeSuffix, 1);
		
			upgrades.add(new WeaponUpgrade(damageIncreaseUpgrade, WeaponUpgradeType.GENERIC_DAMAGE_INCREASE));
			upgrades.add(new WeaponUpgrade(knifeAttackDelayReductionUpgrade, WeaponUpgradeType.KNIFE_ATTACK_DELAY));
		} else {
			if(type == WeaponType.SNIPER) {
				int targetAquiryUpgrade = this._getStat(o, sniperChargeBonusUpgradeSuffix, 1);
				upgrades.add(new WeaponUpgrade(targetAquiryUpgrade, WeaponUpgradeType.SNIPER_CHARGE_UPGRADE));
			} else {
				int recoilReductionUpgrade = this._getStat(o, weaponName + weaponRecoilReductionUpgradeSuffix, 1);
				upgrades.add(new WeaponUpgrade(recoilReductionUpgrade, WeaponUpgradeType.GUN_RECOIL_REDUCTION));
			}
			
			int damageIncreaseUpgrade = this._getStat(o, weaponName + genericDamageIncreaseUpgradeSuffix, 1);
			int reloadSpeedReductionUpgrade = this._getStat(o, weaponName + weaponReloadSpeedReductionUpgradeSuffix, 1);

			upgrades.add(new WeaponUpgrade(damageIncreaseUpgrade, WeaponUpgradeType.GENERIC_DAMAGE_INCREASE));
			upgrades.add(new WeaponUpgrade(reloadSpeedReductionUpgrade, WeaponUpgradeType.GUN_RELOAD_SPEED_REDUCTION));
			
			if(type != WeaponType.PISTOL) {
				int costReductionUpgrade = this._getStat(o, weaponName + genericCostReductionUpgradeSuffix, 1);

				upgrades.add(new WeaponUpgrade(costReductionUpgrade, WeaponUpgradeType.GENERIC_COST_REDUCTION));
			}
		}
		
		stat.type = type; 
		stat.kills = this._getStat(o, weaponNameAsCamelCase + weaponKillsSuffix, 0);
		stat.headshots = this._getStat(o, weaponNameAsCamelCase + weaponHeadshotsSuffix, 0);
		stat.virtualHeadshotAccuracy = (double) stat.headshots / Math.max((double) stat.kills, 1.0);
		stat.upgrades = upgrades;
		stat.isPrimary = isPrimaryWeapon(type);
		stat.isSecondary = isSecondaryWeapon(type);
		
		stat.upgrades.sort((a, b) -> Integer.compare(b.level, a.level));
		
		return stat;
	}
	
	/**
	 * Returns true if the weapon is a primary weapon
	 * @param type the weapon
	 * @return true if it is primary
	 */
	public static boolean isPrimaryWeapon(WeaponType type) {
		return type == WeaponType.AUTO_SHOTGUN || type == WeaponType.BULLPUP || type == WeaponType.CARBINE 
				|| type == WeaponType.RIFLE || type == WeaponType.SCOPED_RIFLE || type == WeaponType.SMG || type == WeaponType.SNIPER || type == WeaponType.SHOTGUN;
	}
	
	/**
	 * Returns true if the weapon is a secondary weapon
	 * @param type the weapon
	 * @return true if it is secondary
	 */
	public static boolean isSecondaryWeapon(WeaponType type) {
		return type == WeaponType.HANDGUN || type == WeaponType.MAGNUM || type == WeaponType.PISTOL;
	}

  public UUID getUuid() {
    return uuid;
  }
}
