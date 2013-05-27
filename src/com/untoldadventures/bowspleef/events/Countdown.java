package com.untoldadventures.bowspleef.events;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.untoldadventures.bowspleef.BowSpleef;
import com.untoldadventures.bowspleef.Methods;

public class Countdown extends BukkitRunnable {
    private String arena;
    private int cntDwn = 10;

    public Countdown(String arena)
    {
	this.arena = arena;
    }

    public void run()
    {

	List<String> players = BowSpleef.arenaConfig.getStringList("arenas." + this.arena + ".players");

	if (cntDwn > 0)
	{
	    Iterator<String> i = players.iterator();
	    while (i.hasNext())
	    {
		this.pm("The Game will start in " + cntDwn + " seconds!", Bukkit.getPlayer(i.next()));
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

	    // Starting the Arena
	    BowSpleefStartEvent event = new BowSpleefStartEvent(arena);
	    Bukkit.getServer().getPluginManager().callEvent(event);

	    Methods.start(arena);

	    this.cancel();
	}

    }

    private void pm(String message, Player player)
    {
	player.sendMessage(ChatColor.AQUA + "[BowSpleef] " + ChatColor.GRAY + message);
    }
}