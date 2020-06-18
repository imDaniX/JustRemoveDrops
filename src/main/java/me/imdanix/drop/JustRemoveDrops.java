package me.imdanix.drop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class JustRemoveDrops extends JavaPlugin implements Listener {
    private boolean entity;
    private boolean blocks;

    private RestrictList<EntityType> restrictEntities;
    private RestrictList<Material> restrictBlocks;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        reloadValues();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(blocks && restrictBlocks.isRestricted(event.getBlock().getType()))
            event.setDropItems(false);
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if(entity && restrictEntities.isRestricted(event.getEntityType()) && event.getEntityType() != EntityType.PLAYER)
            event.getDrops().clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("jrd.reload")) {
            reloadValues();
            sender.sendMessage(ChatColor.GREEN + "JDR was reloaded!");
            return true;
        }
        return false;
    }

    private void reloadValues() {
        reloadConfig();
        entity = getConfig().getBoolean("remove-entity-drop", true);
        blocks = getConfig().getBoolean("remove-block-drop", true);

        restrictEntities = new RestrictList<>(getEnumSet(EntityType.class, getConfig().getStringList("entities")), getConfig().getBoolean("entities-blacklist", true));
        restrictBlocks = new RestrictList<>(getEnumSet(Material.class, getConfig().getStringList("blocks")), getConfig().getBoolean("blocks-blacklist", true));
    }


    private static <T extends Enum<T>> Set<T> getEnumSet(Class<T> clazz, Collection<String> enumStrColl) {
        Set<T> enums = new HashSet<>();
        for(String enumStr : enumStrColl) {
            T t = getEnum(clazz, enumStr.toUpperCase(Locale.ENGLISH));
            if(t != null) enums.add(t);
        }
        return enums.isEmpty() ? Collections.emptySet() : EnumSet.copyOf(enums);
    }

    private static <T extends Enum<T>> T getEnum(Class<T> clazz, String name) {
        try {
            return Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
