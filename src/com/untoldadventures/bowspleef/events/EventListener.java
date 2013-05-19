package com.untoldadventures.bowspleef.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.untoldadventures.bowspleef.Arena;
import com.untoldadventures.bowspleef.BowSpleef;
import com.untoldadventures.bowspleef.api.EnumBSEvent;

public class EventListener implements Listener
{

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBowSpleefEvent(BowSpleefEvent event)
	{

		this.pm(event.getArena() + ", " + event.getPlayer() + ", " + event.getType(), event.getPlayer());

		if (event.getType() == EnumBSEvent.JOIN)
		{
			for (Arena arena : BowSpleef.arenas)
			{
				if (arena.getName() == event.getArena())
				{
					this.pm(arena.getName(), event.getPlayer());
					// if (arena.players.contains(event.getPlayer()))
					// {
					this.pm("You already voted for this arena!", event.getPlayer());
					// return;
					// }

					arena.players.add(event.getPlayer());
				}
			}
		}

		if (event.getType() == EnumBSEvent.VOTE)
		{
			for (Arena arena : BowSpleef.arenas)
			{
				if (arena.getName() == event.getArena())
				{
					// if (arena.players.contains(event.getPlayer()))
					// {
					this.pm("You already voted for this arena!", event.getPlayer());
					// return;
					// }

					arena.players.add(event.getPlayer());
					arena.addVotes(1);
				}
			}

		}
	}

	public void pm(String message, Player player)
	{
		player.sendMessage(ChatColor.AQUA + "[BowSpleef] " + ChatColor.GRAY + message);
	}
}