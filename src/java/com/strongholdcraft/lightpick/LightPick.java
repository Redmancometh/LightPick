package com.strongholdcraft.lightpick;

import com.massivecraft.factions.*;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class LightPick extends JavaPlugin {
	private WorldGuardPlugin wg = null;
	private List<String> pickaxes = new ArrayList<String>();

	@Override
	public void onLoad() {
		if (!this.getDataFolder().exists() && this.getConfig() == null)
			this.saveDefaultConfig();
		FileConfiguration config = this.getConfig();
		pickaxes = config.getStringList("pickaxes");
	}

	@Override
	public void onEnable() {
		wg = (WorldGuardPlugin) this.getServer().getPluginManager()
				.getPlugin("WorldGuard");
		PluginManager pm = Bukkit.getServer().getPluginManager();

		pm.registerEvents(new Listener() {
			@EventHandler
			public void placeTorch(final PlayerInteractEvent event) {
				final Action action = event.getAction();
				final Material material = event.getPlayer().getItemInHand()
						.getType();
				if (isPickaxe(material)
						&& action.equals(Action.RIGHT_CLICK_BLOCK)) {
					final Player p = event.getPlayer();
					final FPlayer fp = FPlayers.i.get(p);
					final Faction faction = Board.getFactionAt(new FLocation(
							event.getClickedBlock()));
					if ((fp.getFaction() == faction || faction.isNone())
							&& wg.canBuild(p, event.getClickedBlock())) {
						Inventory pi = p.getInventory();
						if (pi.contains(Material.TORCH)) {
							Block relative = event.getClickedBlock()
									.getRelative(event.getBlockFace());
							if (relative.getType() != Material.TORCH) {
								relative.setType(Material.TORCH);
								pi.remove(new ItemStack(Material.TORCH, 1));
							}
						} else
							p.sendMessage(ChatColor.DARK_RED
									+ "You're out of torches!");
					} else
						p.sendMessage(ChatColor.DARK_RED
								+ "You can't place a torch here");
				}
			}
		}, this);
	}

	/**
	 * @param material
	 * @return
	 */
	private boolean isPickaxe(Material material) {
		for (String pickaxe : pickaxes)
			if (Material.valueOf(pickaxe) == material)
				return true;
		return false;
	}
}
