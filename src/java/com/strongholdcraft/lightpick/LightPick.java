package com.strongholdcraft.lightpick;

import com.massivecraft.factions.*;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LightPick extends JavaPlugin {
	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Listener() {
			@EventHandler
			public void placeTorch(final PlayerInteractEvent event) {
				Material m = event.getPlayer().getItemInHand().getType();
				if ((m == Material.WOOD_PICKAXE || m == Material.STONE_PICKAXE || m == Material.IRON_PICKAXE || m == Material.GOLD_PICKAXE || m == Material.DIAMOND_PICKAXE) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					Player p = event.getPlayer();
					FPlayer fp = FPlayers.i.get(p);
					WorldGuardPlugin wg = (WorldGuardPlugin) p.getServer().getPluginManager().getPlugin("WorldGuard");
					Faction faction = Board.getFactionAt(new FLocation(event.getClickedBlock()));
					if ((fp.getFaction() == faction || faction.isNone()) && wg.canBuild(p, event.getClickedBlock())) {
						Inventory pi = p.getInventory();
						if (pi.contains(Material.TORCH)) {
							Block relative = event.getClickedBlock().getRelative(event.getBlockFace());
							if (relative.getType() != Material.TORCH) {
								relative.setType(Material.TORCH);
								pi.remove(new ItemStack(Material.TORCH, 1));
							}
						} else {
							p.sendMessage(ChatColor.DARK_RED + "You're out of torches!");
						}
					} else {
						p.sendMessage(ChatColor.DARK_RED + "You can't place a torch here");
					}
				}
			}
		}, this);
	}
}
