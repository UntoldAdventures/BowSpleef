package com.untoldadventures.bowspleef.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.untoldadventures.bowspleef.BowSpleef;
import com.untoldadventures.bowspleef.api.EnumBSEvent;

public class EventListener implements Listener
{

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBowSpleefEvent(BowSpleefEvent event)
	{
		Player player = event.getPlayer();
		if (event.getType() == EnumBSEvent.JOIN)
		{

		}
		if (event.getType() == EnumBSEvent.VOTE)
		{
			String arena = BowSpleef.invConfig.getString(player.getName() + ".arena");
			List<String> players = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".players");
			List<String> voted = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".voted");
			int votesNeeded = Math.round(players.size() * 2 / 3);
			int amountVoted = voted.size();
			int remaining = votesNeeded - amountVoted;
			if (remaining <= 0)
			{
				// Calling Start Event
				BowSpleefEvent eventStart = new BowSpleefEvent(EnumBSEvent.START, player, arena);
				Bukkit.getServer().getPluginManager().callEvent(eventStart);
			}

		}
	}

	public void pm(String message, Player player)
	{
		player.sendMessage(ChatColor.AQUA + "[BowSpleef] " + ChatColor.GRAY + message);
	}
}