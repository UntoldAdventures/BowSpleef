package com.untoldadventures.bowspleef;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.untoldadventures.arena.Arena;
import com.untoldadventures.arena.events.ArenaEnableEvent;

public class BSMethods
{

	public static void setEnabled(Plugin plugin, Player player, String name, FileConfiguration arenaConfig)
	{
		Arena arena = new Arena(name, plugin);
		if (player.hasPermission(plugin.getName() + ".admin.set.enabled"))
		{
			if (arenaConfig.contains("arenas." + arena.getName().toLowerCase()))
			{
				if (arenaConfig.contains("arenas." + arena.getName().toLowerCase() + ".positions.pos1"))
				{
					if (arenaConfig.contains("arenas." + arena.getName() + ".positions.pos2"))
					{
						if (arenaConfig.contains("arenas." + arena.getName() + ".positions.spawn"))
						{
							if (arenaConfig.contains("arenas." + arena.getName() + ".positions.lobby"))
							{
								arenaConfig.set("arenas." + arena.getName().toLowerCase() + ".enabled", true);
								pm(player, "The Arena, " + arena.getName() + ", has been enabled!", plugin);

								ArenaEnableEvent event = new ArenaEnableEvent(player, arena, plugin, arenaConfig);
								Bukkit.getServer().getPluginManager().callEvent(event);
								return;
							}
							pm(player, "No Lobby Point Set!", plugin);
							return;
						}
						pm(player, "Missing Spawn Point", plugin);
						return;
					}
					pm(player, "Missing Pos2", plugin);
					return;
				}
				pm(player, "Missing Pos1", plugin);
				return;
			}
			pm(player, "That arena doesn't exist!", plugin);
			return;
		}
		noPerm(player, plugin);
		return;
	}

	public static void setPos1(Plugin plugin, Player player, String name, FileConfiguration arenaConfig, Location spawn)
	{
		Arena arena = new Arena(name, plugin);

		if (player.hasPermission(plugin.getName() + ".admin.set.pos"))
		{
			if (arenaConfig.contains("arenas." + arena.getName().toLowerCase()))
			{
				int X = spawn.getBlockX(), Y = spawn.getBlockY(), Z = spawn.getBlockZ();
				String world = spawn.getWorld().getName();
				arenaConfig.set("arenas." + arena.getName().toLowerCase() + ".positions.pos1.x", X);
				arenaConfig.set("arenas." + arena.getName().toLowerCase() + ".positions.pos1.y", Y);
				arenaConfig.set("arenas." + arena.getName().toLowerCase() + ".positions.pos1.z", Z);
				arenaConfig.set("arenas." + arena.getName().toLowerCase() + ".positions.pos1.world", world);
				pm(player, "Pos1 position Set!", plugin);
				return;
			}
			pm(player, "That arena doesn't exist!", plugin);
			return;
		}
		noPerm(player, plugin);
		return;
	}

	public static void setPos2(Plugin plugin, Player player, String name, FileConfiguration arenaConfig, Location spawn)
	{
		Arena arena = new Arena(name, plugin);

		if (player.hasPermission(plugin.getName() + ".admin.set.pos"))
		{
			if (arenaConfig.contains("arenas." + arena.getName().toLowerCase()))
			{
				int X = spawn.getBlockX(), Y = spawn.getBlockY(), Z = spawn.getBlockZ();
				String world = spawn.getWorld().getName();
				arenaConfig.set("arenas." + arena.getName().toLowerCase() + ".positions.pos2.x", X);
				arenaConfig.set("arenas." + arena.getName().toLowerCase() + ".positions.pos2.y", Y);
				arenaConfig.set("arenas." + arena.getName().toLowerCase() + ".positions.pos2.z", Z);
				arenaConfig.set("arenas." + arena.getName().toLowerCase() + ".positions.pos2.world", world);
				pm(player, "Pos2 position Set!", plugin);
				return;
			}
			pm(player, "That arena doesn't exist!", plugin);
			return;
		}
		noPerm(player, plugin);
		return;
	}

	public static void pm(Player player, String message, Plugin plugin)
	{
		player.sendMessage(ChatColor.AQUA + "[" + plugin.getName() + "] " + ChatColor.GRAY + message);
	}

	public static void noPerm(Player player, Plugin plugin)
	{
		player.sendMessage(ChatColor.AQUA + "[" + plugin.getName() + "] " + ChatColor.GRAY + "You can't do that!");
	}
}