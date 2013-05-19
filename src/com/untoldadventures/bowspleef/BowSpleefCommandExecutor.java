package com.untoldadventures.bowspleef;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.untoldadventures.bowspleef.api.EnumBSEvent;
import com.untoldadventures.bowspleef.events.BowSpleefEvent;
import com.untoldadventures.bowspleef.events.EventListener;

public class BowSpleefCommandExecutor implements CommandExecutor
{
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
						if (BowSpleef.arenaConfig.contains("arenas." + arena))
						{
							if (BowSpleef.arenaConfig.getBoolean("arenas." + arena + ".enabled"))
							{
								// Storage for Return GM
								int gm = player.getGameMode().getValue();
								BowSpleef.invConfig.set(player.getName() + ".return.gamemode", gm);
								// Storage for Returning to Pos
								Location returnPos = player.getLocation();
								BowSpleef.invConfig.set(player.getName() + ".return.x", returnPos.getBlockX());
								BowSpleef.invConfig.set(player.getName() + ".return.y", returnPos.getBlockY());
								BowSpleef.invConfig.set(player.getName() + ".return.z", returnPos.getBlockZ());
								BowSpleef.invConfig.set(player.getName() + ".return.world", returnPos.getWorld().getName());
								// Inventory Storage
								String inv = InventoryStringDeSerializer.InventoryToString(player.getInventory());
								BowSpleef.invConfig.set(player.getName() + ".inventory", inv);
								this.plugin.saveConfig();
								// Clearing Inventory
								player.getInventory().clear();
								// Teleporting to the Lobby
								World world = Bukkit.getServer().getWorld(BowSpleef.arenaConfig.getString("arenas." + arena + ".world"));
								int x = BowSpleef.arenaConfig.getInt("arenas." + arena + ".lobby.x"), y = BowSpleef.arenaConfig.getInt("arenas." + arena + ".lobby.y"), z = BowSpleef.arenaConfig.getInt("arenas." + arena + ".lobby.z");
								Location lobby = new Location(world, x, y, z);
								player.teleport(lobby);
								// Setting them to Adventure Time
								player.setGameMode(GameMode.ADVENTURE);
								ItemStack bow = new ItemStack(Material.BOW, 1);
								bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
								bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
								player.getInventory().addItem(bow);
								player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
								// Event
								BowSpleefEvent event = new BowSpleefEvent(EnumBSEvent.JOIN, player, arena);
								Bukkit.getServer().getPluginManager().callEvent(event);
								return true;
							}
							this.pm("That arena is Disabled", player);
							return true;
						}
						this.pm("That Arena doesn't Exist!", player);
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
						if (BowSpleef.invConfig.contains(player.getName()))
						{
							// Replaceing their Gamemode
							int gmnum = BowSpleef.invConfig.getInt(player.getName() + ".return.gamemode");
							GameMode gm = GameMode.getByValue(gmnum);
							player.setGameMode(gm);
							// Replacing their Inventory
							Inventory inv = InventoryStringDeSerializer.StringToInventory(BowSpleef.invConfig.getString(player.getName() + ".inventory"));
							player.getInventory().setContents(inv.getContents());
							// Teleporting them back to their orig loc
							int x = BowSpleef.invConfig.getInt(player.getName() + ".return.x");
							int y = BowSpleef.invConfig.getInt(player.getName() + ".return.y");
							int z = BowSpleef.invConfig.getInt(player.getName() + ".return.z");
							World w = Bukkit.getWorld(BowSpleef.invConfig.getString(player.getName() + ".return.world"));
							Location retpos = new Location(w, x, y, z);
							player.teleport(retpos);
							// Removing them from the config
							BowSpleef.invConfig.set(player.getName(), null);
						}
					}
				}
			}
			if (args[0].equalsIgnoreCase("vote"))
			{
				String arena = args[1];
				if (player instanceof Player)
				{
					if (player.hasPermission("bs.vote"))
					{
						if (BowSpleef.arenaConfig.contains("arenas." + arena))
						{
							if (BowSpleef.arenaConfig.contains("arenas." + arena + ".players." + player) || EventListener.players.contains(player))
							{
								// Event
								BowSpleefEvent event = new BowSpleefEvent(EnumBSEvent.VOTE, player, arena);
								Bukkit.getServer().getPluginManager().callEvent(event);
								int votesNeeded = Math.round(EventListener.players.size() * 2/3);
								int amountVoted = EventListener.voted.size();
								int remaining = votesNeeded - amountVoted;
								this.pm("You have voted to start! Only " + remaining + " votes remain!", player);
							}
							this.pm("You aren't in an Arena!", player);
							return true;
						}
						this.pm("That arena doesn't exist!", player);
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
							return true;
						}
						player.sendMessage(ChatColor.AQUA + "[BowSpleef]" + ChatColor.GRAY + " Incorrect Usage!");
						return false;
					}
					return true;
				}
				sender.sendMessage("[BowSpleef] This command can only be run by a Player!");
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
							if (BowSpleef.arenaConfig.getList("arenas.list").size() == 1)
							{
								this.pm("You can only have one arena in this version!", player);
								return true;
							}
							String arena = args[1];
							if (BowSpleef.arenaConfig.contains("arenas." + arena))
							{
								sender.sendMessage(ChatColor.AQUA + "[BowSpleef]" + ChatColor.GRAY + " This arena already exists!");
							}
							sender.sendMessage(ChatColor.AQUA + "[BowSpleef]" + ChatColor.GRAY + " Creating!");
							BowSpleef.arenaConfig.createSection("arenas." + arena);
							BowSpleef.arenaConfig.set("arenas." + arena + ".enabled", false);
							
							// Arenas List
							arenaNames.add(arena);
							BowSpleef.arenaConfig.set("arenas.list", arenaNames);

							plugin.saveConfig();
							return true;
						}
						return false;
					}
					return true;
				}
				sender.sendMessage("[BowSpleef] This command can only be run by a Player!");
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
			if (args[0].equalsIgnoreCase("set"))
			{
				if (sender instanceof Player)
				{
					if (sender.hasPermission("bowspleef.admin.set"))
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
								plugin.saveConfig();
								sender.sendMessage(ChatColor.AQUA + "[BowSpleef]" + ChatColor.GRAY + " Pos1 Set!");
								return true;
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