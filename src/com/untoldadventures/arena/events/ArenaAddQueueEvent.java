package com.untoldadventures.arena.events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;


public class ArenaAddQueueEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player = null;
    private Plugin plugin = null;
    private FileConfiguration arenaConfig = null;

    public ArenaAddQueueEvent(Player player, Plugin plugin, FileConfiguration arenaConfig)
    {
	this.player = player;
	this.plugin = plugin;
	this.arenaConfig = arenaConfig;
    }

    public Player getPlayer()
    {
	return this.player;
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
