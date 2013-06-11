package com.untoldadventures.arena;

import org.bukkit.plugin.Plugin;

public class Arena {
    private String name = null;
    private Plugin plugin = null;

    public Arena(String name, Plugin plugin)
    {
	this.name = name;

	this.plugin = plugin;

	plugin.saveConfig();
    }

    public String getName()
    {
	return name;
    }

    public Plugin getPlugin()
    {
	return plugin;
    }

}