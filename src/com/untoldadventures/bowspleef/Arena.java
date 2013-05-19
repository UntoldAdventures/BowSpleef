package com.untoldadventures.bowspleef;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Arena
{
	private String name = null;
	private BowSpleef plugin;
	public List<Player> players = new ArrayList<Player>();

	public Arena(String name, BowSpleef plugin)
	{
		this.name = name;

		this.plugin = plugin;

		BowSpleef.arenaConfig.set("arenas." + name + ".votes", 0);

		plugin.saveConfig();
	}

	public String getName()
	{
		return this.name;
	}

	public Location getPos1()
	{
		World world = Bukkit.getServer().getWorld(BowSpleef.arenaConfig.getString("arenas." + name + ".world"));
		int x = BowSpleef.arenaConfig.getInt("arenas." + name + ".pos1.x");
		int y = BowSpleef.arenaConfig.getInt("arenas." + name + ".pos1.y");
		int z = BowSpleef.arenaConfig.getInt("arenas." + name + ".pos2.z");

		return new Location(world, x, y, z);
	}
	public Location getPos2()
	{
		World world = Bukkit.getServer().getWorld(BowSpleef.arenaConfig.getString("arenas." + name + ".world"));
		int x = BowSpleef.arenaConfig.getInt("arenas." + name + ".pos2.x");
		int y = BowSpleef.arenaConfig.getInt("arenas." + name + ".pos2.y");
		int z = BowSpleef.arenaConfig.getInt("arenas." + name + ".pos2.z");

		return new Location(world, x, y, z);
	}

	public Location getSpawn()
	{
		World world = Bukkit.getServer().getWorld(BowSpleef.arenaConfig.getString("arenas." + name + ".world"));
		int x = BowSpleef.arenaConfig.getInt("arenas." + name + ".spawn.x");
		int y = BowSpleef.arenaConfig.getInt("arenas." + name + ".spawn.y");
		int z = BowSpleef.arenaConfig.getInt("arenas." + name + ".spawn.z");

		return new Location(world, x, y, z);
	}

	public Location getLobby()
	{
		World world = Bukkit.getServer().getWorld(BowSpleef.arenaConfig.getString("arenas." + name + ".world"));
		int x = BowSpleef.arenaConfig.getInt("arenas." + name + ".lobby.x");
		int y = BowSpleef.arenaConfig.getInt("arenas." + name + ".lobby.y");
		int z = BowSpleef.arenaConfig.getInt("arenas." + name + ".lobby.z");

		return new Location(world, x, y, z);
	}

	public void setVotes(int votes)
	{
		BowSpleef.arenaConfig.set("arenas." + name + ".votes", votes);
		plugin.saveConfig();
	}

	public void addVotes(int votes)
	{
		BowSpleef.arenaConfig.set("arenas." + name + ".votes", BowSpleef.arenaConfig.getInt("arenas." + name + ".votes") + votes);
		plugin.saveConfig();
	}

	public int getVotes()
	{
		return BowSpleef.arenaConfig.getInt("arenas." + name + ".votes");
	}
}