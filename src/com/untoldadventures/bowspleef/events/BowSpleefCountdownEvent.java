package com.untoldadventures.bowspleef.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BowSpleefCountdownEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String arena = null;

    public BowSpleefCountdownEvent(String arena)
    {
	this.arena = arena;
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