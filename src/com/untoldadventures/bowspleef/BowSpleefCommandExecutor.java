package com.untoldadventures.bowspleef;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.untoldadventures.bowspleef.events.BowSpleefCountdownEvent;
import com.untoldadventures.bowspleef.events.BowSpleefVoteEvent;
import com.untoldadventures.bowspleef.events.Countdown;

public class BowSpleefCommandExecutor implements CommandExecutor {
    public static List<String> arenaNames = new ArrayList<String>();

    BowSpleef plugin;

    public BowSpleefCommandExecutor(BowSpleef plugin)
    {
	this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
	if (sender instanceof Player)
	{
	    if (args.length == 0)
	    {
		return false;
	    }

	    Player player = (Player) sender;
	    if (args[0].equalsIgnoreCase("join"))
	    {
		if (player.hasPermission("bs.join"))
		{
		    if (args.length == 2)
		    {
			String arena = args[1];

			Methods.join(player, arena, plugin);
			return true;
		    }

		    this.pm("Incorrect Usage!", player);
		    return false;
		}
		return true;
	    }
	    if (args[0].equalsIgnoreCase("quit"))
	    {
		if (player instanceof Player)
		{
		    if (player.hasPermission("bs.quit"))
		    {

			Methods.quit(player, plugin);
			return true;
		    }
		}
	    }
	    if (args[0].equalsIgnoreCase("vote"))
	    {
		if (player instanceof Player)
		{
		    if (player.hasPermission("bs.vote"))
		    {
			// Getting the arena they are in
			String arena = BowSpleef.invConfig.getString(player.getName() + ".arena");
			if (arena != null)
			{
			    if (BowSpleef.arenaConfig.getBoolean("arenas." + arena + ".enabled"))
			    {
				if (!BowSpleef.arenaConfig.getBoolean("arenas." + arena + ".inGame"))
				{
				    List<String> players = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".players");
				    if (players.contains(player.getName()))
				    {
					// Adding player to voted list
					List<String> voted = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".voted");

					if (!voted.contains(player.getName()))
					{
					    voted.add(player.getName());
					    BowSpleef.arenaConfig.set("arenas." + arena + ".voted", voted);
					    // Event
					    BowSpleefVoteEvent event = new BowSpleefVoteEvent(player, arena);
					    Bukkit.getServer().getPluginManager().callEvent(event);

					    int votesNeeded = Math.round(players.size() * 2 / 3);
					    int amountVoted = voted.size();
					    int remaining = votesNeeded - amountVoted;

					    this.pm("You voted to start the arena!", event.getPlayer());

					    if (remaining <= 0 && players.size() > 1)
					    {
						BowSpleef.arenaConfig.set("arenas." + arena + ".inGame", true);

						// Calling CountDown Event
						BowSpleefCountdownEvent eventCount = new BowSpleefCountdownEvent(arena);
						Bukkit.getServer().getPluginManager().callEvent(eventCount);

						new Countdown(event.getArena()).runTaskTimer(plugin, 0L, 20L);
					    }

					    plugin.saveConfig();
					    return true;
					}

					this.pm("You already voted!", player);
					return true;
				    }

				    this.pm("You aren't in an Arena!", player);
				    plugin.saveConfig();
				    return true;
				}

				this.pm("The arena is in-game!", player);
				return true;
			    }

			    this.pm("The arena is not enabled!", player);
			    return true;
			}
			this.pm("You aren't in an Arena!", player);
			return true;
		    }
		    return true;
		}

		sender.sendMessage("[BowSpleef] This command can only be run by a Player");
		return true;
	    }
	    if (args[0].equalsIgnoreCase("delete"))
	    {
		if (player instanceof Player)
		{
		    if (player.hasPermission("bs.delete"))
		    {
			if (args.length == 2)
			{
			    String arena = args[1];
			    if (BowSpleef.arenaConfig.contains("arenas." + arena))
			    {
				BowSpleef.arenaConfig.set("arenas." + arena, null);
				this.pm("The arena, " + arena + ", has been deleted!", player);
				plugin.saveConfig();
				return true;

			    }
			    this.pm("That arena doesnt exist!", player);
			    plugin.saveConfig();
			    return true;
			}
			player.sendMessage(ChatColor.AQUA + "[BowSpleef]" + ChatColor.GRAY + " Incorrect Usage!");
			return false;
		    }
		    plugin.saveConfig();
		    return true;
		}
		sender.sendMessage("[BowSpleef] This command can only be run by a Player!");
		plugin.saveConfig();
		return true;
	    }
	    if (args[0].equalsIgnoreCase("create"))
	    {
		if (player instanceof Player)
		{
		    if (player.hasPermission("bs.create"))
		    {
			if (args.length == 2)
			{
			    String arena = args[1];
			    if (BowSpleef.arenaConfig.contains("arenas." + arena))
			    {
				sender.sendMessage(ChatColor.AQUA + "[BowSpleef]" + ChatColor.GRAY + " This arena already exists!");
				return true;
			    }
			    sender.sendMessage(ChatColor.AQUA + "[BowSpleef]" + ChatColor.GRAY + " The arena, " + arena + ", has been created!");
			    BowSpleef.arenaConfig.createSection("arenas." + arena);
			    BowSpleef.arenaConfig.createSection("arenas." + arena + ".players");
			    BowSpleef.arenaConfig.set("arenas." + arena + ".enabled", false);
			    BowSpleef.arenaConfig.set("arenas." + arena + ".inGame", false);
			    BowSpleef.arenaConfig.set("arenas." + arena + ".min-players", 2);
			    BowSpleef.arenaConfig.set("arenas." + arena + ".max-players", 10);

			    // Arenas List
			    arenaNames.add(arena);
			    BowSpleef.arenaConfig.set("List", arenaNames);

			    plugin.saveConfig();
			    return true;
			}
			return false;
		    }
		    plugin.saveConfig();
		    return true;
		}
		sender.sendMessage("[BowSpleef] This command can only be run by a Player!");
		plugin.saveConfig();
		return true;
	    }
	    if (args[0].equalsIgnoreCase("regen") && args.length == 2)
	    {
		String arena = args[1];
		World world = player.getWorld();
		if (sender instanceof Player)
		{
		    if (player.hasPermission("bowspleef.admin.regen"))
		    {
			if (BowSpleef.arenaConfig.contains("arenas." + arena))
			{
			    int pos1X = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos1.x");
			    int pos1Y = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos1.y");
			    int pos1Z = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos1.z");
			    int pos2X = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos2.x");
			    int pos2Y = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos2.y");
			    int pos2Z = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos2.z");
			    Location pos1 = new Location(world, pos1X, pos1Y, pos1Z);
			    Location pos2 = new Location(world, pos2X, pos2Y, pos2Z);
			    this.regen(pos1, pos2, world);

			    sender.sendMessage(ChatColor.AQUA + "[BowSpleef]" + ChatColor.GRAY + " Arena Regenerated!");

			    plugin.saveConfig();
			    return true;
			}
			sender.sendMessage(ChatColor.AQUA + "[BowSpleef]" + ChatColor.GRAY + " Arena doesn't exist!");
			return true;
		    }
		    return true;
		}
		sender.sendMessage("[BowSpleef] This command can only be run by a Player!");
		return true;

	    }
	    if (args[0].equalsIgnoreCase("reload"))
	    {
		if (sender instanceof Player)
		{
		    if (sender.hasPermission("cf.reload"))
		    {
			if (args.length == 1)
			{
			    this.plugin.loadConfig();
			    this.pm("Config Reloaded!", player);
			    return true;
			}
			this.pm("Invalid Arguments", player);
			return false;
		    }
		    return true;
		}
		sender.sendMessage("[ChatFilter] This Command may Only be run by a Player!");
		return true;
	    }
	    if (args[0].equalsIgnoreCase("set"))
	    {
		if (sender instanceof Player)
		{
		    if (sender.hasPermission("bowspleef.admin.set"))
		    {
			if (args.length == 3 || args.length == 4)
			{
			    Location pos1 = player.getLocation(), pos2 = player.getLocation();
			    int pos1X = pos1.getBlockX(), pos1Y = pos1.getBlockY() - 1, pos1Z = pos1.getBlockZ(), pos2X = pos2.getBlockX(), pos2Y = pos2.getBlockY() - 1, pos2Z = pos2.getBlockZ();
			    String arena = args[1];

			    // Multi-World Support
			    BowSpleef.arenaConfig.set("arenas." + arena + ".world", pos1.getWorld().getName());

			    if (BowSpleef.arenaConfig.contains("arenas." + arena))
			    {
				if (args[2].equalsIgnoreCase("pos1"))
				{
				    // Saving Position 1
				    BowSpleef.arenaConfig.set("arenas." + arena + ".pos1.x", pos1X);
				    BowSpleef.arenaConfig.set("arenas." + arena + ".pos1.y", pos1Y);
				    BowSpleef.arenaConfig.set("arenas." + arena + ".pos1.z", pos1Z);
				    sender.sendMessage(ChatColor.AQUA + "[BowSpleef]" + ChatColor.GRAY + " Pos1 Set!");
				    plugin.saveConfig();
				    return true;
				}
				if (args.length == 4)
				{
				    if (args[2].equalsIgnoreCase("min"))
				    {
					// Saving Position 1
					BowSpleef.arenaConfig.set("arenas." + arena + ".min", args[3]);
					this.pm("Minimum Players set!", player);
					plugin.saveConfig();
					return true;
				    }
				    if (args[2].equalsIgnoreCase("max"))
				    {
					// Saving Position 1
					BowSpleef.arenaConfig.set("arenas." + arena + ".max", args[3]);
					this.pm("Maximum Players set!", player);
					plugin.saveConfig();
					return true;
				    }
				}
				if (args[2].equalsIgnoreCase("pos2"))
				{
				    // Saving Position 2
				    BowSpleef.arenaConfig.set("arenas." + arena + ".pos2.x", pos2X);
				    BowSpleef.arenaConfig.set("arenas." + arena + ".pos2.y", pos2Y);
				    BowSpleef.arenaConfig.set("arenas." + arena + ".pos2.z", pos2Z);
				    plugin.saveConfig();
				    sender.sendMessage(ChatColor.AQUA + "[BowSpleef]" + ChatColor.GRAY + " Pos2 Set!");
				    return true;
				}
				if (args[2].equalsIgnoreCase("spawn"))
				{
				    int x = player.getLocation().getBlockX(), y = player.getLocation().getBlockY(), z = player.getLocation().getBlockZ();
				    String world = player.getLocation().getWorld().getName();
				    if (world == BowSpleef.arenaConfig.getString("arenas." + arena + ".world"))
				    {
					BowSpleef.arenaConfig.set("arenas." + arena + ".spawn.x", x);
					BowSpleef.arenaConfig.set("arenas." + arena + ".spawn.y", y);
					BowSpleef.arenaConfig.set("arenas." + arena + ".spawn.z", z);

					plugin.saveConfig();
					this.pm("Spawn Postion Set!", player);
					return true;
				    }
				    this.pm("You are in the wrong world!", player);
				    plugin.saveConfig();
				    return true;
				}
				if (args[2].equalsIgnoreCase("enable"))
				{
				    if (BowSpleef.arenaConfig.contains("arenas." + arena + ".pos1"))
				    {
					if (BowSpleef.arenaConfig.contains("arenas." + arena + ".pos2"))
					{
					    if (BowSpleef.arenaConfig.contains("arenas." + arena + ".spawn"))
					    {
						if (BowSpleef.arenaConfig.contains("arenas." + arena + ".lobby"))
						{
						    BowSpleef.arenaConfig.set("arenas." + arena + ".enabled", true);
						    this.plugin.saveConfig();
						    this.pm("The Arena, " + arena + ", has been enabled!", player);
						    plugin.saveConfig();

						    World world = player.getWorld();

						    int pos1X1 = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos1.x");
						    int pos1Y1 = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos1.y");
						    int pos1Z1 = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos1.z");
						    int pos2X1 = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos2.x");
						    int pos2Y1 = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos2.y");
						    int pos2Z1 = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos2.z");
						    Location pos11 = new Location(world, pos1X1, pos1Y1, pos1Z1);
						    Location pos21 = new Location(world, pos2X1, pos2Y1, pos2Z1);

						    this.regen(pos11, pos21, world);

						    return true;
						}
						this.pm("No Lobby Point Set!", player);
						return true;
					    }
					    this.pm("Missing Spawn Point", player);
					    return true;
					}
					this.pm("Missing Pos2", player);
					return true;
				    }
				    this.pm("Missing Pos1", player);
				    return true;
				}
				if (args[2].equalsIgnoreCase("enabled"))
				{
				    if (BowSpleef.arenaConfig.contains("arenas." + arena + ".pos1"))
				    {
					if (BowSpleef.arenaConfig.contains("arenas." + arena + ".pos2"))
					{
					    if (BowSpleef.arenaConfig.contains("arenas." + arena + ".spawn"))
					    {
						if (BowSpleef.arenaConfig.contains("arenas." + arena + ".lobby"))
						{
						    BowSpleef.arenaConfig.set("arenas." + arena + ".enabled", true);
						    this.plugin.saveConfig();
						    this.pm("The Arena, " + arena + ", has been enabled!", player);

						    World world = player.getWorld();

						    int pos1X1 = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos1.x");
						    int pos1Y1 = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos1.y");
						    int pos1Z1 = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos1.z");
						    int pos2X1 = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos2.x");
						    int pos2Y1 = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos2.y");
						    int pos2Z1 = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos2.z");
						    Location pos11 = new Location(world, pos1X1, pos1Y1, pos1Z1);
						    Location pos21 = new Location(world, pos2X1, pos2Y1, pos2Z1);

						    this.regen(pos11, pos21, world);

						    plugin.saveConfig();

						    return true;
						}
						this.pm("No Lobby Point Set!", player);
						return true;
					    }
					    this.pm("Missing Spawn Point", player);
					    return true;
					}
					this.pm("Missing Pos2", player);
					return true;
				    }
				    this.pm("Missing Pos1", player);
				    return true;
				}
				if (args[2].equalsIgnoreCase("disable"))
				{
				    BowSpleef.arenaConfig.set("arenas." + arena + ".enabled", false);
				    this.plugin.saveConfig();
				    this.pm("The Arena, " + arena + ", has been disabled!", player);
				    return true;
				}
				if (args[2].equalsIgnoreCase("disabled"))
				{
				    BowSpleef.arenaConfig.set("arenas." + arena + ".enabled", false);
				    this.plugin.saveConfig();
				    this.pm("The Arena, " + arena + ", has been disabled!", player);
				    return true;
				}
				if (args[2].equalsIgnoreCase("lobby"))
				{
				    Location lobby = player.getLocation();
				    int x = lobby.getBlockX(), y = lobby.getBlockY(), z = lobby.getBlockZ();
				    String world = player.getWorld().getName();
				    if (world == BowSpleef.arenaConfig.getString("arenas." + arena + ".world"))
				    {
					BowSpleef.arenaConfig.set("arenas." + arena + ".lobby.x", x);
					BowSpleef.arenaConfig.set("arenas." + arena + ".lobby.y", y);
					BowSpleef.arenaConfig.set("arenas." + arena + ".lobby.z", z);
					this.pm("Lobby Point Set!", player);
					plugin.saveConfig();
					return true;
				    }
				    this.pm("You are in the wrong world!", player);
				    return true;
				}
				this.pm("Incorrect Usage!", player);
				return false;
			    }
			    sender.sendMessage(ChatColor.AQUA + "[BowSpleef]" + ChatColor.GRAY + " Arena doesn't Exist!");
			    return true;
			}
			this.pm("Incorrect Usage!", player);
			return false;
		    }
		    plugin.saveConfig();
		    return true;
		}
		sender.sendMessage("[BowSpleef] This command can only be run by a Player!");
		return true;
	    }
	    return false;
	}

	sender.sendMessage("[BowSpleef] This command can only be run by a Player!");

	return true;
    }

    public void regen(Location loc1, Location loc2, World w)
    {
	int minx = Math.min(loc1.getBlockX(), loc2.getBlockX()), miny = Math.min(loc1.getBlockY(), loc2.getBlockY()), minz = Math.min(loc1.getBlockZ(), loc2.getBlockZ()), maxx = Math.max(loc1.getBlockX(), loc2.getBlockX()), maxy = Math.max(loc1.getBlockY(), loc2.getBlockY()), maxz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
	for (int x = minx; x <= maxx; x++)
	{
	    for (int y = miny; y <= maxy; y++)
	    {
		for (int z = minz; z <= maxz; z++)
		{
		    Block b = w.getBlockAt(x, y, z);
		    if (b.getType() == Material.AIR)
			b.setType(Material.TNT);
		}
	    }
	}
    }

    public void pm(String message, Player player)
    {
	player.sendMessage(ChatColor.AQUA + "[BowSpleef] " + ChatColor.GRAY + message);
    }

}