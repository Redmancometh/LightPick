package com.strongholdcraft.lightpick;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Listeners implements Listener
{
	@EventHandler
	public void placeTorch(PlayerInteractEvent event)
	{
		Material m = event.getPlayer().getItemInHand().getType();
		if((m==Material.WOOD_PICKAXE||m==Material.STONE_PICKAXE||m==Material.IRON_PICKAXE||m==Material.GOLD_PICKAXE||m==Material.DIAMOND_PICKAXE)&&event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			Player p = event.getPlayer();
			FPlayer fp = FPlayers.i.get(p);
			WorldGuardPlugin wg =  (WorldGuardPlugin) p.getServer().getPluginManager().getPlugin("WorldGuard");
		    Faction faction = Board.getFactionAt(new FLocation(event.getClickedBlock()));
			if((fp.getFaction()==faction||faction.isNone())&&wg.canBuild(p, event.getClickedBlock()))
			{
				Inventory pi = p.getInventory();
				if(pi.contains(Material.TORCH))
				{
					Block relative = event.getClickedBlock().getRelative(event.getBlockFace());
					if(relative.getType()!=Material.TORCH)
					{
						relative.setType(Material.TORCH);
						removeInventoryItems(p, p.getInventory(), Material.TORCH, 1);
						p.updateInventory();
					}
				}
				else{p.sendMessage(ChatColor.DARK_RED+"You're out of torches!");}
			}
			else{p.sendMessage(ChatColor.DARK_RED+"You can't place a torch here");}
		}
	}
	public static void removeInventoryItems(Player p, PlayerInventory inv, Material type, int amount) {
		for (ItemStack is : inv.getContents()) 
		{
			if (is != null && is.getType() == type) 
			{
				p.updateInventory();
				int newamount = is.getAmount() - amount;
				if (newamount > 0) 
				{
					p.updateInventory();
					is.setAmount(newamount);
					break;
				}
				else 
				{
					inv.remove(is);
					amount = -newamount;
					p.updateInventory();
					if (amount == 0) break;
					p.updateInventory();
				}
			}
		}
	}
}
