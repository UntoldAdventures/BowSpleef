package com.untoldadventures.bowspleef;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.untoldadventures.bowspleef.events.EventListener;

public class BowSpleef extends JavaPlugin
{
	public String Version = "0.1";

	public static File pluginFolder;
	public static File configFile;
	public static File arenaFile;
	public static File invFile;
	public static FileConfiguration bowtntConfig;
	public static FileConfiguration arenaConfig;
	public static FileConfiguration invConfig;

	public static List<Arena> arenas = new ArrayList<Arena>();

	@Override
	public void onEnable()
	{
		// Get the Arenas Info

		// Command Executor
		getCommand("bs").setExecutor(new BowSpleefCommandExecutor(this));

		// EventListener
		getServer().getPluginManager().registerEvents(new EventListener(), this);

		// Config File
		pluginFolder = getDataFolder();
		configFile = new File(pluginFolder, "config.yml");
		arenaFile = new File(pluginFolder, "arenas.yml");
		invFile = new File(pluginFolder, "inventories.yml");
		bowtntConfig = new YamlConfiguration();
		arenaConfig = new YamlConfiguration();
		invConfig = new YamlConfiguration();

		if (!pluginFolder.exists())
		{
			try
			{
				pluginFolder.mkdir();
			} catch (Exception ex)
			{
			}
		}
		if (!configFile.exists())
		{
			try
			{
				configFile.createNewFile();
			} catch (Exception ex)
			{
			}
		}
		if (!arenaFile.exists())
		{
			try
			{
				arenaFile.createNewFile();
			} catch (Exception ex)
			{
			}
		}
		if (!invFile.exists())
		{
			try
			{
				invFile.createNewFile();
			} catch (Exception ex)
			{
			}
		}
		try
		{
			bowtntConfig.load(configFile);
			arenaConfig.load(arenaFile);
			invConfig.load(invFile);
		} catch (Exception ex)
		{
		}

	}
	@Override
	public void onDisable()
	{
		for (Player player : Bukkit.getServer().getOnlinePlayers())
		{
			List<String> arenas = BowSpleef.arenaConfig.getStringList("List");
			for (int i = 0; i <= arenas.size(); i++)
			{
				String arena = arenas.get(i);
				List<String> players = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".players");
				if (players.contains(player.getName()))
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
					players.remove(player.getName());
					BowSpleef.arenaConfig.set("arenas." + arena + ".players", players);
					saveConfig();
				}
			}
		}
	}
	public void saveConfig()
	{
		try
		{
			bowtntConfig.save(configFile);
			arenaConfig.save(arenaFile);
			invConfig.save(invFile);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	public void getArenas()
	{
		arenas.clear();
		int nofa = BowSpleef.arenaConfig.getStringList("List").size();
		for (int i = 0; i <= nofa; i++)
		{
			String name = BowSpleef.arenaConfig.getStringList("List").get(i);

			Arena arena = new Arena(name, this);

			arenas.add(arena);
		}

	}

}
