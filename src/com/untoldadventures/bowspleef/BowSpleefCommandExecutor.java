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

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.untoldadventures.arena.CustomException;
import com.untoldadventures.arena.Methods;

public class BowSpleefCommandExecutor implements CommandExecutor
{
	public static WorldEditPlugin WorldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

	BowSpleef plugin;
	public static List<String> arenaNames = new ArrayList<String>();

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

			if (args[0].equalsIgnoreCase("create"))
			{
				if (args.length == 2)
				{
					String arena = args[1];
					Methods.createArena(plugin, arena, player, BowSpleef.arenaConfig);
					BowSpleef.arenaConfig.set("arenas." + arena + ".type", "bowspleef");
				}

				plugin.saveConfig();
				return true;
			}

			if (args[0].equalsIgnoreCase("delete"))
			{
				if (args.length == 2)
				{
					String arena = args[1];
					try
					{
						Methods.deleteArena(plugin, arena, player, BowSpleef.arenaConfig, BowSpleef.invConfig);
						plugin.saveConfig();
						return true;
					} catch (CustomException e)
					{
					}
				}
			}

			if (args[0].equalsIgnoreCase("regen"))
			{
				if (args.length == 2)
				{
					if (player.hasPermission("bowspleef.admin.regen"))
					{
						if (BowSpleef.arenaConfig.contains("arenas." + args[1]))
						{
							int pos1X1 = BowSpleef.arenaConfig.getInt("arenas." + args[1] + ".positions.pos1.x");
							int pos1Y1 = BowSpleef.arenaConfig.getInt("arenas." + args[1] + ".positions.pos1.y");
							int pos1Z1 = BowSpleef.arenaConfig.getInt("arenas." + args[1] + ".positions.pos1.z");
							int pos2X1 = BowSpleef.arenaConfig.getInt("arenas." + args[1] + ".positions.pos2.x");
							int pos2Y1 = BowSpleef.arenaConfig.getInt("arenas." + args[1] + ".positions.pos2.y");
							int pos2Z1 = BowSpleef.arenaConfig.getInt("arenas." + args[1] + ".positions.pos2.z");
							World world = Bukkit.getWorld(BowSpleef.arenaConfig.getString("arenas." + args[1] + ".positions.pos2.world"));

							Location pos11 = new Location(world, pos1X1, pos1Y1, pos1Z1);
							Location pos21 = new Location(world, pos2X1, pos2Y1, pos2Z1);

							this.regen(pos11, pos21, world);
							return true;
						}
					}
				}
			}

			if (args[0].equalsIgnoreCase("list"))
			{
				Methods.listArenas(plugin, player, BowSpleef.arenaConfig);
				return true;
			}

			if (args[0].equalsIgnoreCase("set"))
			{
				if (args.length == 3 || args.length == 4)
				{
					String arena = args[1];

					if (args[2].equalsIgnoreCase("spawn"))
					{
						Methods.setSpawn(plugin, player, arena, BowSpleef.arenaConfig);
						plugin.saveConfig();
						return true;
					}

					if (args[2].equalsIgnoreCase("lobby"))
					{
						Methods.setLobby(plugin, player, arena, BowSpleef.arenaConfig);
						plugin.saveConfig();
						return true;
					}

					if (args[2].equalsIgnoreCase("floor"))
					{
						if (WorldEdit != null)
						{
							Selection floor = WorldEdit.getSelection(player);
							if (floor != null)
							{

								BSMethods.setPos1(plugin, player, arena, BowSpleef.arenaConfig, floor.getMaximumPoint());
								BSMethods.setPos2(plugin, player, arena, BowSpleef.arenaConfig, floor.getMinimumPoint());
							} else
							{
								pm("Select your points!", player);
							}

						} else
						{
							pm("World Edit isn't installed. Use pos1/pos2 instead!", player);
						}
						return true;
					}

					if (args[2].equalsIgnoreCase("pos1"))
					{
						Location loc = player.getLocation();
						loc.setY(loc.getY() - 1);
						BSMethods.setPos1(plugin, player, arena, BowSpleef.arenaConfig, loc);
						plugin.saveConfig();
						return true;
					}

					if (args[2].equalsIgnoreCase("pos2"))
					{
						Location loc = player.getLocation();
						loc.setY(loc.getY() - 1);
						BSMethods.setPos2(plugin, player, arena, BowSpleef.arenaConfig, loc);
						plugin.saveConfig();
						return true;
					}

					if (args[2].equalsIgnoreCase("enabled"))
					{
						BSMethods.setEnabled(plugin, player, arena, BowSpleef.arenaConfig);
						plugin.saveConfig();
						return true;
					}
					if (args[2].equalsIgnoreCase("enable"))
					{
						BSMethods.setEnabled(plugin, player, arena, BowSpleef.arenaConfig);
						plugin.saveConfig();
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

					if (args.length == 4)
					{
						if (args[2].equalsIgnoreCase("min"))
						{
							// Saving Position 1
							BowSpleef.arenaConfig.set("arenas." + arena + ".min-players", Integer.parseInt(args[3]));
							this.pm("Minimum Players set!", player);
							plugin.saveConfig();
							return true;
						}
						if (args[2].equalsIgnoreCase("max"))
						{
							// Saving Position 1
							BowSpleef.arenaConfig.set("arenas." + arena + ".max-players", Integer.parseInt(args[3]));
							this.pm("Maximum Players set!", player);
							plugin.saveConfig();
							return true;
						}
					}

					return false;

				}

				return false;
			}

			if (args[0].equalsIgnoreCase("join"))
			{
				if (args.length == 2)
				{
					try
					{
						Methods.joinArena(plugin, args[1], player, BowSpleef.arenaConfig, BowSpleef.invConfig);
						return true;
					} catch (CustomException e)
					{
						e.printStackTrace();
					}

					plugin.saveConfig();

				}

			}
			if (args[0].equals("vote"))
			{
				if (args.length == 1)
				{
					try
					{
						Methods.addVote(plugin, BowSpleef.invConfig.getString("players." + player.getName() + ".arena"), player, BowSpleef.arenaConfig, BowSpleef.invConfig);
						return true;
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				return false;
			}
			if (args[0].equalsIgnoreCase("quit"))
			{
				if (args.length == 1)
				{
					try
					{
						Methods.quitArena(plugin, player, BowSpleef.arenaConfig, BowSpleef.invConfig);
						return true;
					} catch (CustomException e)
					{
						e.printStackTrace();
					}
				}

				plugin.saveConfig();

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