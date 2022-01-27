package me.hsgamer.morphtool;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class InteractListener implements Listener {
    private final MorphTool instance;

    public InteractListener(MorphTool instance) {
        this.instance = instance;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack == null) return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || isNotValidMorphTool(itemMeta)) return;

        MineralType mineralType = MineralType.getMineralType(itemStack.getType());
        if (mineralType == MineralType.UNKNOWN) return;

        Block block = event.getClickedBlock();
        if (block == null) return;
        Material blockType = block.getType();

        String type;
        if (Tag.MINEABLE_AXE.isTagged(blockType)) {
            type = "AXE";
        } else if (Tag.MINEABLE_PICKAXE.isTagged(blockType)) {
            type = "PICKAXE";
        } else if (Tag.MINEABLE_SHOVEL.isTagged(blockType)) {
            type = "SHOVEL";
        } else if (Tag.MINEABLE_HOE.isTagged(blockType)) {
            type = "HOE";
        } else {
            type = "SWORD";
        }
        Material material = mineralType.getMaterial(type);
        if (material != null) {
            itemStack.setType(material);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || isNotValidMorphTool(itemMeta)) return;

        MineralType mineralType = MineralType.getMineralType(itemStack.getType());
        if (mineralType == MineralType.UNKNOWN) return;

        Material type = mineralType.getMaterial("SWORD");
        if (type == null) return;

        itemStack.setType(type);
    }

    private boolean isNotValidMorphTool(ItemMeta itemMeta) {
        byte state = itemMeta.getPersistentDataContainer().getOrDefault(instance.getItemKey(), PersistentDataType.BYTE, (byte) 0);
        return state == (byte) 0;
    }
}
