package com.untoldadventures.bowspleef.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.untoldadventures.bowspleef.BowSpleef;
import com.untoldadventures.bowspleef.api.EnumBSEvent;

public class EventListener implements Listener
{

	public static List<Player> players = new ArrayList<Player>();
	public static List<Player> voted = new ArrayList<Player>();

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBowSpleefEvent(BowSpleefEvent event)
	{
		if (event.getType() == EnumBSEvent.JOIN)
		{
			if (!players.contains(event.getPlayer()))
			{
				players.add(event.getPlayer());
				if (players.size() >= BowSpleef.arenaConfig.getInt("arenas." + event.getArena() + ".minPlayers"))
				{
					// Start Countdown
				}

			}
			else
			{
				return;
			}
		}
		if (event.getType() == EnumBSEvent.VOTE)
		{
			if (!players.contains(event.getPlayer()))
			{
				voted.add(event.getPlayer());
			}
			else
			{
				return;
			}
		}
	}
}