package com.untoldadventures.bowspleef.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BowSpleefQuitEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player = null;
    private String arena = null;

    public BowSpleefQuitEvent(Player player, String arena)
    {
	this.player = player;
	this.arena = arena;
    }

    public Player getPlayer()
    {
	return this.player;
    }

    public String getArena()
    {
	return this.arena;
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