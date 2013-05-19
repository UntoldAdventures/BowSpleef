package com.untoldadventures.bowspleef.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.untoldadventures.bowspleef.api.EnumBSEvent;


public class BowSpleefEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private Player player = null;
	private String arena = null;
	private EnumBSEvent type = null;

	public BowSpleefEvent(EnumBSEvent type, Player player, String arena)
	{
		this.player = player;
		this.arena = arena;
		this.type = type;
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public String getArena()
	{
		return this.arena;
	}

	public EnumBSEvent getType()
	{
		return this.type;
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