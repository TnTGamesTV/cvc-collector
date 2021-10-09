package de.throwstnt.developing.cvc_collector.module;

import de.throwstnt.developing.cvc_collector.CvcCollector;
import de.throwstnt.developing.cvc_collector.db.DatabaseManager;
import de.throwstnt.developing.cvc_collector.manager.RoundManager;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.moduletypes.SimpleModule;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Material;

public class LocationEntryCountModule extends SimpleModule {

    @Override
    public String getDisplayName() {
        return "Entries";
    }

    @Override
    public String getDisplayValue() {
        return DatabaseManager.getInstance().getCountSinceLastCommit() + "";
    }

    @Override
    public String getDefaultValue() {
        return "-";
    }

    @Override
    public boolean isShown() {
        return RoundManager.getInstance().isRoundActive();
    }

    @Override
    public ModuleCategory getCategory() {
        return CvcCollector.HYPIXEL_SERVICES;
    }

    @Override
    public ControlElement.IconData getIconData() {
        return new ControlElement.IconData(Material.DIAMOND);
    }

    @Override
    public void loadSettings() {

    }

    @Override
    public String getControlName() {
        return "Entries Since Last Commit";
    }

    @Override
    public String getSettingName() {
        return "Entries Since Last Commit";
    }

    @Override
    public String getDescription() {
        return "Displays all saved entries since the last commit ";
    }

    @Override
    public int getSortingId() {
        return 0;
    }


}
