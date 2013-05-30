package com.untoldadventures.bowspleef;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
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
	private WorldEditPlugin WorldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

	@Override
	public void onEnable()
	{
		// Command Executor
		getCommand("bs").setExecutor(new BowSpleefCommandExecutor(this));

		// EventListener
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		// Permissions
		this.getServer().getPluginManager().addPermission(new Permission("bs.join"));
		this.getServer().getPluginManager().addPermission(new Permission("bs.quit"));
		this.getServer().getPluginManager().addPermission(new Permission("bs.vote"));
		this.getServer().getPluginManager().addPermission(new Permission("bs.set"));
		this.getServer().getPluginManager().addPermission(new Permission("bs.create"));
		this.getServer().getPluginManager().addPermission(new Permission("bs.regen"));
		this.getServer().getPluginManager().addPermission(new Permission("bs.reload"));
		if (WorldEdit == null)
		{
			this.getServer().getLogger().info("WorldEdit not found!");
		} else
		{
			this.getServer().getLogger().info("WorldEdit found!");
		}
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
				List<String> cmds = new ArrayList<String>();
				cmds.add("bs join");
				cmds.add("bs quit");
				cmds.add("bs vote");
				cmds.add("bs");
				BowSpleef.bowtntConfig.set("allowed-commands", cmds);
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
		for (Player p: Bukkit.getOnlinePlayers())
		{
			if (BowSpleef.invConfig.contains(p.getName()))
			{
				Methods.quitNoWin(p, this);
			}
		}
			List<String> arenas = BowSpleef.arenaConfig.getStringList("List");
		for (String arena : arenas)
		{
			BowSpleef.arenaConfig.set("arenas." + arena + ".inGame", false);
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

	public void loadConfig()
	{
		try
		{
			bowtntConfig.load(configFile);
			arenaConfig.load(arenaFile);
			invConfig.load(invFile);

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
