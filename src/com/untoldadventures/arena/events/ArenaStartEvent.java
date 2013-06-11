package com.untoldadventures.arena.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import com.untoldadventures.arena.Arena;

public class ArenaStartEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Arena arena = null;
    private Plugin plugin = null;

    public ArenaStartEvent(Arena arena, Plugin plugin)
    {
	this.arena = arena;
	this.plugin = plugin;
    }

    public Arena getArena()
    {
	return this.arena;
    }

    public Plugin getPlugin()
    {
	return this.plugin;
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
