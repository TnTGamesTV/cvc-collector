package de.throwstnt.developing.cvc_collector.stats.data;

import java.util.Arrays;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.TextFormatting;

public class WeaponUpgrade {
	
	/**
	 * The level of the upgrade (1-9)
	 */
	public int level;
	
	/**
	 * The type of WeaponUpgrade
	 */
	public WeaponUpgradeType type;
	
	public WeaponUpgrade(int level, WeaponUpgradeType type) {
		this.level = level;
		this.type = type;
	}
	
	public ItemStack toItemStack() {
		ItemStack baseStack = this._typeToBaseItem(this.type);
		baseStack.setCount(this.level);
		return baseStack;
	}
	
	public List<String> toHoverText() {
		return Arrays.asList(TextFormatting.WHITE + this._typeToTitle(type) + " " + TextFormatting.BLUE + _levelToRomanLevel(level));
	}
	
	private String _levelToRomanLevel(int level) {
		switch(level) {
			case 1: return "I";
			case 2: return "II";
			case 3: return "III";
			case 4: return "IV";
			case 5: return "V";
			case 6: return "VI";
			case 7: return "VII";
			case 8: return "VIII";
			case 9: return "IX (Maxed)";
			default: return "-";
		}
	}
	
	private String _typeToTitle(WeaponUpgradeType type) {
		switch(type) {
		case GENERIC_COST_REDUCTION:
			return "Cost Reduction";
		case GENERIC_DAMAGE_INCREASE:
			return "Damage Increase";
		case GUN_RECOIL_REDUCTION:
			return "Recoil Reduction";
		case GUN_RELOAD_SPEED_REDUCTION:
			return "Reload Speed Reduction";
		case KNIFE_ATTACK_DELAY:
			return "Attack Delay";
		case SNIPER_CHARGE_UPGRADE:
			return "Target Aquiry";
		default:
			return "Unknown";
		}
	}
	
	private ItemStack _typeToBaseItem(WeaponUpgradeType type) {
		switch(type) {
			case GENERIC_COST_REDUCTION:
			    return Items.EMERALD.getDefaultInstance().copy();
			case GENERIC_DAMAGE_INCREASE:
              return Items.FIRE_CHARGE.getDefaultInstance().copy();
			case GUN_RECOIL_REDUCTION:
              return Items.FEATHER.getDefaultInstance().copy();
			case GUN_RELOAD_SPEED_REDUCTION:
              return Items.SNOWBALL.getDefaultInstance().copy();
			case KNIFE_ATTACK_DELAY:
              return Items.CLOCK.getDefaultInstance().copy();
			case SNIPER_CHARGE_UPGRADE:
              return Items.ENDER_PEARL.getDefaultInstance().copy();
			default:
              return Items.STONE.getDefaultInstance().copy();
		}
	}
}