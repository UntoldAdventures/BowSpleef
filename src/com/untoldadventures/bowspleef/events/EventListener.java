package com.untoldadventures.bowspleef.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.untoldadventures.bowspleef.BowSpleef;
import com.untoldadventures.bowspleef.Methods;

public class EventListener implements Listener
{

	BowSpleef plugin;
	int cntDwn = 10;

	public EventListener(BowSpleef plugin)
	{
		this.plugin = plugin;
	}

	public void pm(String message, Player player)
	{
		player.sendMessage(ChatColor.AQUA + "[BowSpleef] " + ChatColor.GRAY + message);
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPlaceBlock(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		if (BowSpleef.invConfig.contains(player.getName()))
		{
			event.setCancelled(true);
			this.pm("You can't place blocks!", player);
		}
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerPlaceBlock(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		if (BowSpleef.invConfig.contains(player.getName()))
		{
			event.setCancelled(true);
			this.pm("You can't break blocks!", player);
		}
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();

		if (BowSpleef.invConfig.contains(player.getName()))
		{
			// Getting the arena they are in
			String arena = BowSpleef.invConfig.getString(player.getName() + ".arena");
			// Replaceing their Gamemode
			int gmnum = BowSpleef.invConfig.getInt(player.getName() + ".return.gamemode");
			GameMode gm = GameMode.getByValue(gmnum);
			player.setGameMode(gm);
			// Replacing their Inventory
			Methods.loadInv(player);
			List<String> players = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".players");
			List<String> voted = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".voted");
			players.remove(player.getName());
			voted.remove(player.getName());
			BowSpleef.arenaConfig.set("arenas." + arena + ".players", players);
			BowSpleef.arenaConfig.set("arenas." + arena + ".voted", voted);

			event.setDeathMessage("");

			if (players.size() == 1)
			{
				Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "[BowSpleef] " + ChatColor.GRAY + players.get(0) + " won " + arena + "!");
				BowSpleef.arenaConfig.set("arenas." + arena + ".inGame", false);

				Methods.regen(arena, plugin);

				Player winner = Bukkit.getPlayer(players.get(0));

				// Getting the arena they are in
				String arena1 = BowSpleef.invConfig.getString(winner.getName() + ".arena");
				// Replaceing their Gamemode
				int gmnum1 = BowSpleef.invConfig.getInt(winner.getName() + ".return.gamemode");
				GameMode gm1 = GameMode.getByValue(gmnum1);
				winner.setGameMode(gm1);
				// Replacing their Inventory
				Methods.loadInv(winner);

				// Teleporting them back to their orig loc
				int x = BowSpleef.invConfig.getInt(winner.getName() + ".return.x");
				int y = BowSpleef.invConfig.getInt(winner.getName() + ".return.y");
				int z = BowSpleef.invConfig.getInt(winner.getName() + ".return.z");
				World w = Bukkit.getWorld(BowSpleef.invConfig.getString(winner.getName() + ".return.world"));
				Location retpos = new Location(w, x, y, z);
				winner.teleport(retpos);
				// Removing them from the config
				BowSpleef.invConfig.set(winner.getName(), null);

				List<String> players1 = BowSpleef.arenaConfig.getStringList("arenas." + arena1 + ".players");
				List<String> voted1 = BowSpleef.arenaConfig.getStringList("arenas." + arena1 + ".voted");
				players1.remove(winner.getName());
				voted1.remove(winner.getName());
				BowSpleef.arenaConfig.set("arenas." + arena1 + ".players", players1);
				BowSpleef.arenaConfig.set("arenas." + arena1 + ".voted", voted1);

			}

			plugin.saveConfig();

		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onRespawn(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();

		if (BowSpleef.invConfig.contains(player.getName()))
		{
			// Teleporting them back to their orig loc
			int x1 = BowSpleef.invConfig.getInt(player.getName() + ".return.x");
			int y1 = BowSpleef.invConfig.getInt(player.getName() + ".return.y");
			int z1 = BowSpleef.invConfig.getInt(player.getName() + ".return.z");
			World w1 = Bukkit.getWorld(BowSpleef.invConfig.getString(player.getName() + ".return.world"));
			Location retpos1 = new Location(w1, x1, y1, z1);
			event.setRespawnLocation(retpos1);
			// Removing them from the config

			// Replaceing their Gamemode
			int gmnum1 = BowSpleef.invConfig.getInt(player.getName() + ".return.gamemode");
			GameMode gm1 = GameMode.getByValue(gmnum1);
			player.setGameMode(gm1);
			// Replacing their Inventory
			player.getInventory().clear();
			Methods.loadInv(player);

			BowSpleef.invConfig.set(player.getName(), null);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onDropItem(PlayerDropItemEvent event)
	{
		Player player = event.getPlayer();

		if (BowSpleef.invConfig.contains(player.getName()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onDamage(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Player && event.getCause() != DamageCause.VOID && event.getCause() != DamageCause.LAVA && event.getCause() != DamageCause.FIRE)
		{
			Player player = (Player) event.getEntity();

			if (BowSpleef.invConfig.contains(player.getName()))
			{
				event.setCancelled(true);
			}
		}

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onCommand(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
		if (BowSpleef.invConfig.contains(event.getPlayer().getName()))
		{
			List<String> cmds = BowSpleef.bowtntConfig.getStringList("whitelisted-commands");
			String cmd = event.getMessage();
			for (int i = 0; i < cmds.size(); i++)
			{
				if (cmd.contains(cmds.get(i)))
				{
					event.setCancelled(true);
					this.pm("You cannot use that command in an game!", player);
				}
			}
		}

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInteract(PlayerInteractEvent e)
	{
		Player player = e.getPlayer();

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if (e.getClickedBlock().getState() instanceof Sign)
			{
				Sign s = (Sign) e.getClickedBlock().getState();

				if (s.getLine(0).equalsIgnoreCase(ChatColor.DARK_BLUE + "[BowSpleef]") && s.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Join"))
				{
					String arena = s.getLine(2);

					if (arena != null)
					{
						if (BowSpleef.arenaConfig.getBoolean("arenas." + arena + ".enabled"))
						{

							if (!BowSpleef.arenaConfig.getBoolean("arenas." + arena + ".inGame"))
							{
								Methods.join(player, arena, plugin);
								return;
							}

							this.pm("That arena is in game!", player);

							return;
						}

						this.pm("That arena is Disabled", player);
					}
				}

			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void signCreation(SignChangeEvent event)
	{
		if (event.getLine(0).equalsIgnoreCase("[BowSpleef]") && event.getLine(1).equalsIgnoreCase("Join"))
		{
			if (BowSpleef.arenaConfig.contains("arenas." + event.getLine(2)))
			{
				this.pm("Sign creation successful!", event.getPlayer());
				event.setLine(0, ChatColor.DARK_BLUE + "[BowSpleef]");
				event.setLine(1, ChatColor.GREEN + "Join");
				return;
			}

		}
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();

		Methods.quit(player, plugin);

		plugin.saveConfig();

	}

	public void onPlayerConnect(PlayerJoinEvent event)
	{
		// Player player = event.getPlayer();

	}
}