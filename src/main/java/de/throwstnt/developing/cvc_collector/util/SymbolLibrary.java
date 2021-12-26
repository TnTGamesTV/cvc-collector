package de.throwstnt.developing.cvc_collector.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import de.throwstnt.developing.cvc_collector.stats.data.OtherType;
import de.throwstnt.developing.cvc_collector.stats.data.WeaponType;

public class SymbolLibrary {

    public static final String COP_SYMBOL = "銐";

    public static final String CRIM_SYMBOL = "銑";

    private static Map<String, WeaponType> symbolToWeaponMap;

    private static Map<String, OtherType> symbolToOtherMap;

    static {
        symbolToWeaponMap = new HashMap<>();

        symbolToWeaponMap.put("éŠœéŠ?", WeaponType.AUTO_SHOTGUN);
        symbolToWeaponMap.put("éŠ”éŠ•", WeaponType.BULLPUP);
        symbolToWeaponMap.put("éŠˆéŠ‰", WeaponType.CARBINE); // facing right
        symbolToWeaponMap.put("é‰¦é‰§", WeaponType.CARBINE); // facing left
        symbolToWeaponMap.put("éŠŸ", WeaponType.HANDGUN);
        symbolToWeaponMap.put("éŠŒ", WeaponType.KNIFE); // facing right
        symbolToWeaponMap.put("é‰¯", WeaponType.KNIFE); // facing left
        symbolToWeaponMap.put("éŠŽ", WeaponType.MAGNUM); // facing right
        symbolToWeaponMap.put("é‰¡", WeaponType.MAGNUM); // facing left
        symbolToWeaponMap.put("éŠ?", WeaponType.PISTOL); // facing right
        symbolToWeaponMap.put("é‰ ", WeaponType.PISTOL); // facing left
        symbolToWeaponMap.put("éŠ†éŠ‡", WeaponType.RIFLE); // facing right
        symbolToWeaponMap.put("é‰¨é‰©", WeaponType.RIFLE); // facing left
        symbolToWeaponMap.put("éŠ˜éŠ™", WeaponType.SCOPED_RIFLE);
        symbolToWeaponMap.put("éŠŠéŠ‹", WeaponType.SHOTGUN); // facing right
        symbolToWeaponMap.put("é‰¤é‰¥", WeaponType.SHOTGUN); // facing left
        symbolToWeaponMap.put("éŠ?", WeaponType.SMG); // facing right
        symbolToWeaponMap.put("é‰¢", WeaponType.SMG); // facing left
        symbolToWeaponMap.put("éŠ„éŠ…", WeaponType.SNIPER); // facing right
        symbolToWeaponMap.put("é‰ªé‰«", WeaponType.SNIPER); // facing left

        symbolToOtherMap = new HashMap<>();

        symbolToOtherMap.put("é‰¼", OtherType.ARMOR_CHESTPLATE); // same as one below
        symbolToOtherMap.put("é‰¾", OtherType.ARMOR_CHESTPLATE);
        symbolToOtherMap.put("é‰½", OtherType.ARMOR_HELMET); // same as one below
        symbolToOtherMap.put("é‰¿", OtherType.ARMOR_HELMET);
        symbolToOtherMap.put("é‰¶", OtherType.C4);
        symbolToOtherMap.put("é‰º", OtherType.DECOY_GRENADE);
        symbolToOtherMap.put("é‰±é‰²", OtherType.FALL_DAMAGE);
        symbolToOtherMap.put("é‰·", OtherType.FLASH_GRENADE);
        symbolToOtherMap.put("éŠƒ", OtherType.FRAG_GRENADE); // facing right
        symbolToOtherMap.put("é‰¬", OtherType.FRAG_GRENADE); // facing left
        symbolToOtherMap.put("é‰°", OtherType.HEADSHOT);
        symbolToOtherMap.put("éŠ€", OtherType.HEALTH);
        symbolToOtherMap.put("é‰µ", OtherType.OBJECTIVE_COMPLETE);
        symbolToOtherMap.put("é‰´", OtherType.OBJECTIVE_INCOMPLETE);
        symbolToOtherMap.put("é‰¸", OtherType.SMOKE_GRENADE);
        symbolToOtherMap.put("éŠ?", OtherType.TEAM_COPS);
        symbolToOtherMap.put("éŠ‘", OtherType.TEAM_CRIMS);
        symbolToOtherMap.put("é‰»", OtherType.WIRE_CUTTERS);
    }

    /**
     * Gets the first symbol of a given weapon
     * 
     * @param type the weapon
     * @return the first symbol
     */
    public static String weaponToSymbol(WeaponType type) {
        Optional<String> symbol =
                symbolToWeaponMap.entrySet().stream().filter(entry -> entry.getValue().equals(type))
                        .map(entry -> entry.getKey()).findFirst();

        return symbol.isPresent() ? symbol.get() : null;
    }

    /**
     * Gets the first symbol of a given other object
     * 
     * @param type the other object
     * @return the first symbol
     */
    public static String otherToSymbol(OtherType type) {
        Optional<String> symbol =
                symbolToOtherMap.entrySet().stream().filter(entry -> entry.getValue().equals(type))
                        .map(entry -> entry.getKey()).findFirst();

        return symbol.isPresent() ? symbol.get() : null;
    }

    /**
     * Gets the weapon from a chat message or null if nothing detected
     * 
     * @param message the message
     * @return the weapon or null if nothing detected
     */
    public static WeaponType weaponFromChatMessage(String message) {
        Optional<WeaponType> type = symbolToWeaponMap.entrySet().stream()
                .filter(entry -> message.contains(entry.getKey())).map(entry -> entry.getValue())
                .findFirst();

        return type.isPresent() ? type.get() : null;
    }

    /**
     * Gets another object from a chat message or null if nothing detected
     * 
     * @param message the message
     * @return another object or null if nothing detected
     */
    public static OtherType otherFromChatMessage(String message) {
        Optional<OtherType> type = symbolToOtherMap.entrySet().stream()
                .filter(entry -> message.contains(entry.getKey())).map(entry -> entry.getValue())
                .findFirst();

        return type.isPresent() ? type.get() : null;
    }
}
