package io.github.the4neko.hopperTheftProtection_v2;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.popcraft.bolt.BoltAPI;


public final class HopperTheftProtection_v2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Plugin provider = Bukkit.getServer().getPluginManager().getPlugin("Bolt");
        if (provider == null) {
            getLogger().severe("Bolt plugin not found! Make sure it's installed.");
            this.getServer().getPluginManager().disablePlugin(this);
        }
        if(provider instanceof BoltAPI) {
            getServer().getPluginManager().registerEvents(new HopperListener((BoltAPI) provider), this);
        }else{
            getLogger().warning("Bolt plugin not found! Make sure it's installed.");
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    static class HopperListener implements Listener {
        BoltAPI bolt;

        public HopperListener(BoltAPI bolt) {
            this.bolt = bolt;
        }

        @EventHandler
        void onHopperPlace(BlockPlaceEvent event) {
            // If block placed is not hopper, no action needed
            if (event.getBlock().getType() != Material.HOPPER) return;

            Block hopperBlock = event.getBlock();
            Block aboveBlock = hopperBlock.getRelative(0, 1, 0);

            BlockState state = aboveBlock.getState();
            if (state instanceof Container) {
                // If player does not have permission to withdraw, cancel block placement event
                if (!bolt.canAccess(aboveBlock, event.getPlayer(), "withdraw")) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§cYou don't have permission to place hopper under this container!");
                }
            }
        }
    }
}
