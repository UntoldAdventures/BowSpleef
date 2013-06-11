package com.untoldadventures.arena.events;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.untoldadventures.arena.Methods;

public class Countdown extends BukkitRunnable {
    private String arena;
    private Plugin plugin;
    private int cntDwn = 15;
    private FileConfiguration arenaConfig;

    public Countdown(String arena, Plugin plugin, FileConfiguration arenaConfig)
    {
	this.arena = arena;
	this.plugin = plugin;
	this.arenaConfig = arenaConfig;
    }

    public void run()
    {

	List<String> players = arenaConfig.getStringList("arenas." + this.arena.toLowerCase() + ".players");

	if (cntDwn > 0)
	{
	    if (cntDwn == 15 || cntDwn == 10 || cntDwn <= 5)
	    {
		Iterator<String> i = players.iterator();
		while (i.hasNext())
		{
		    this.pm("The Game will start in " + cntDwn + " seconds!", Bukkit.getPlayer(i.next()));
		    if (cntDwn == 5)
		    {
			List<String> players1 = arenaConfig.getStringList("arenas." + this.arena.toLowerCase() + ".players");
			World world = Bukkit.getServer().getWorld(arenaConfig.getString("arenas." + arena.toLowerCase() + ".positions.spawn.world"));
			int x = arenaConfig.getInt("arenas." + arena.toLowerCase() + ".positions.spawn.x");
			int y = arenaConfig.getInt("arenas." + arena.toLowerCase() + ".positions.spawn.y");
			int z = arenaConfig.getInt("arenas." + arena.toLowerCase() + ".positions.spawn.z");
			arenaConfig.set("arenas." + arena.toLowerCase() + ".inGame", true);

			Location spawn = new Location(world, x, y, z);
			for (String givee : players1)
			{
			    Player pgivee = Bukkit.getPlayer(givee);
			    pgivee.teleport(spawn);
			}
		    }
		}

	    }

	    cntDwn--;

	}
	if (cntDwn == 0)
	{
	    Iterator<String> i = players.iterator();
	    while (i.hasNext())
	    {
		this.pm("The Game has Begun!", Bukkit.getPlayer(i.next()));
	    }

	    Methods.startArena(plugin, arena, arenaConfig);
	    this.cancel();
	}

    }

    private void pm(String message, Player player)
    {
	player.sendMessage(ChatColor.AQUA + "[" + plugin.getName() + "] " + ChatColor.GRAY + message);
    }
}