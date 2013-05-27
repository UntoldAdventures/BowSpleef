package com.untoldadventures.bowspleef;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.untoldadventures.bowspleef.events.EventListener;

public class BowSpleef extends JavaPlugin {
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
	// Command Executor
	getCommand("bs").setExecutor(new BowSpleefCommandExecutor(this));

	// EventListener
	getServer().getPluginManager().registerEvents(new EventListener(this), this);

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
		cmds.add("tp");
		cmds.add("tpa");
		cmds.add("home");
		cmds.add("sethome");
		BowSpleef.bowtntConfig.set("whitelisted-commands", cmds);
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
	List<String> arenas = BowSpleef.arenaConfig.getStringList("List");

	for (String arena : arenas)
	{
	    BowSpleef.arenaConfig.set("arenas." + arena + ".inGame", false);

	    List<String> players = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".players");

	    for (String player : players)
	    {
		Methods.quitNoWin(Bukkit.getPlayer(player), this);
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
