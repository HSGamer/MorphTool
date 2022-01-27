package me.hsgamer.morphtool;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.NamespacedKey;

public final class MorphTool extends BasePlugin {
    private final NamespacedKey itemKey = new NamespacedKey(this, "morph_tool_assigned");

    @Override
    public void enable() {
        MessageUtils.setPrefix("&7[&eMorphTool&7] &f");
        registerListener(new InteractListener(this));
        registerCommand(new GiveItemCommand(this));
    }

    public NamespacedKey getItemKey() {
        return itemKey;
    }
}
