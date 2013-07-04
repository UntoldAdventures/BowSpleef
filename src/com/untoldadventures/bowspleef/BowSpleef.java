package com.untoldadventures.bowspleef;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.untoldadventures.arena.CustomException;
import com.untoldadventures.arena.Methods;
import com.untoldadventures.bowspleef.events.EventListener;

public class BowSpleef extends JavaPlugin
{
	public String Version = "0.2";
	private static File pluginFolder;
	private static File configFile;
	private static File arenaFile;
	private static File invFile;
	public static FileConfiguration bowtntConfig;
	public static FileConfiguration arenaConfig;
	public static FileConfiguration invConfig;
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
			this.getLogger().info("WorldEdit not found!");
		} else
		{
			this.getLogger().info("WorldEdit found!");
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
		saveConfig();
	}

	@Override
	public void onDisable()
	{
		List<String> arenas = arenaConfig.getStringList("arenas.list");
		for (String name : arenas)
		{
			List<String> players = arenaConfig.getStringList("arenas." + name + ".players");
			for (String pname : players)
			{
				try
				{
					Methods.quitNoWinArena(this, Bukkit.getPlayer(pname), arenaConfig, invConfig);
				} catch (CustomException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
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

}
