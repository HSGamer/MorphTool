package me.hsgamer.morphtool;

import me.hsgamer.hscore.bukkit.utils.ItemUtils;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GiveItemCommand extends Command {
    private static final Permission PERMISSION = new Permission("morphtool.give", PermissionDefault.OP);

    static {
        Bukkit.getPluginManager().addPermission(PERMISSION);
    }

    private final MorphTool instance;

    public GiveItemCommand(MorphTool instance) {
        super("givemorphtool", "Give a morph tool", "/givemorphtool <type> [player]", Collections.emptyList());
        this.instance = instance;
        setPermission(PERMISSION.getName());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return false;
        if (args.length == 0) {
            MessageUtils.sendMessage(sender, getUsage());
            return false;
        }

        String type = args[0].toUpperCase(Locale.ROOT);
        MineralType mineralType;
        try {
            mineralType = MineralType.valueOf(type);
        } catch (Exception e) {
            MessageUtils.sendMessage(sender, "&cThe type is not found");
            return false;
        }

        Material material = mineralType.getMaterial("PICKAXE");
        if (material == null) {
            MessageUtils.sendMessage(sender, "&cThe material is not found");
            return false;
        }
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.getPersistentDataContainer().set(instance.getItemKey(), PersistentDataType.BYTE, (byte) 1);
        itemStack.setItemMeta(itemMeta);

        CommandSender toSend = sender;
        if (args.length > 1) {
            String name = args[1];
            Player player = Bukkit.getPlayer(name);
            if (player != null) {
                toSend = player;
            } else {
                MessageUtils.sendMessage(sender, "&cThe player is not found");
                return false;
            }
        }
        if (!(toSend instanceof Player)) {
            MessageUtils.sendMessage(sender, "&cOnly the player can use this command");
            return false;
        }
        Player player = (Player) toSend;
        ItemUtils.giveItem(player, itemStack);
        MessageUtils.sendMessage(sender, "&aSuccess");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        List<String> strings = Collections.emptyList();
        if (args.length == 1) {
            strings = new ArrayList<>();
            String type = args[0].toLowerCase(Locale.ROOT);
            for (MineralType mineralType : MineralType.values()) {
                if (mineralType == MineralType.UNKNOWN) continue;
                if (type.isEmpty() || mineralType.name().toLowerCase(Locale.ROOT).startsWith(type)) {
                    strings.add(mineralType.name());
                }
            }
        } else if (args.length == 2) {
            String name = args[1];
            strings = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName)
                    .filter(playerName -> name.isEmpty() || playerName.startsWith(name))
                    .collect(Collectors.toList());
        }
        return strings;
    }
}
