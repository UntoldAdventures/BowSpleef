package com.untoldadventures.arena.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import com.untoldadventures.arena.Arena;

public class ArenaStopEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena = null;
    private Plugin plugin = null;
    private FileConfiguration arenaConfig = null;

    public ArenaStopEvent(Arena arena, Plugin plugin, FileConfiguration arenaConfig)
    {
	this.arena = arena;
	this.plugin = plugin;
	this.arenaConfig = arenaConfig;
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

    public HandlerList getHandlers()
    {
	return handlers;
    }

    public static HandlerList getHandlerList()
    {
	return handlers;
    }
}
