package me.imdanix.drop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class JustRemoveDrops extends JavaPlugin implements Listener {
    private boolean entity;
    private boolean blocks;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        reloadValues();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(blocks) event.setDropItems(false);
    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if(entity && event.getEntityType() != EntityType.PLAYER) event.getDrops().clear();
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
    }
}
