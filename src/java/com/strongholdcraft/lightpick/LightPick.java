package com.strongholdcraft.lightpick;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public class LightPick extends JavaPlugin 
{
	public void onEnable()
	{
		PluginManager pm = Bukkit.getServer().getPluginManager();
	    pm.registerEvents(new Listeners(), this);
	}
	
}
