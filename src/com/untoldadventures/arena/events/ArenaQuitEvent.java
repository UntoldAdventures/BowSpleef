package com.untoldadventures.arena.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import com.untoldadventures.arena.Arena;

public class ArenaQuitEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private Player player = null;
	private Arena arena = null;
	private Plugin plugin = null;
	private FileConfiguration arenaConfig = null;
	private FileConfiguration playerConfig = null;

	public ArenaQuitEvent(Player player, Arena arena, Plugin plugin, FileConfiguration arenaConfig, FileConfiguration playerConfig)
	{
		this.player = player;
		this.arena = arena;
		this.plugin = plugin;
		this.arenaConfig = arenaConfig;
		this.playerConfig = playerConfig;
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public Arena getArena()
	{
		return this.arena;
	}
	public Plugin getPlugin()
	{
		return this.plugin;
	}
	public FileConfiguration getArenaConfig()
	{
		return this.arenaConfig;
	}
	
	public FileConfiguration getPlayerConfig()
	{
		return this.playerConfig;
	}
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
