package com.untoldadventures.bowspleef;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.untoldadventures.bowspleef.events.BowSpleefCountdownEvent;
import com.untoldadventures.bowspleef.events.BowSpleefJoinEvent;
import com.untoldadventures.bowspleef.events.BowSpleefQuitEvent;
import com.untoldadventures.bowspleef.events.Countdown;

public class Methods {
    public static void quitNoWin(Player player, BowSpleef plugin)
    {
	if (BowSpleef.invConfig.contains(player.getName()))
	{
	    // Getting the arena they are in
	    String arena = BowSpleef.invConfig.getString(player.getName() + ".arena");
	    // Replaceing their Gamemode
	    int gmnum = BowSpleef.invConfig.getInt(player.getName() + ".return.gamemode");
	    GameMode gm = GameMode.getByValue(gmnum);
	    player.setGameMode(gm);
	    // Replacing their Inventory
	    loadInv(player);

	    // Teleporting them back to their orig loc
	    int x = BowSpleef.invConfig.getInt(player.getName() + ".return.x");
	    int y = BowSpleef.invConfig.getInt(player.getName() + ".return.y");
	    int z = BowSpleef.invConfig.getInt(player.getName() + ".return.z");
	    World w = Bukkit.getWorld(BowSpleef.invConfig.getString(player.getName() + ".return.world"));
	    Location retpos = new Location(w, x, y, z);
	    player.teleport(retpos);
	    // Removing them from the config
	    BowSpleef.invConfig.set(player.getName(), null);

	    List<String> players = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".players");
	    List<String> voted = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".voted");
	    players.remove(player.getName());
	    voted.remove(player.getName());
	    BowSpleef.arenaConfig.set("arenas." + arena + ".players", players);
	    BowSpleef.arenaConfig.set("arenas." + arena + ".voted", voted);

	}
    }

    public static void quit(Player player, BowSpleef plugin)
    {
	if (BowSpleef.invConfig.contains(player.getName()))
	{

	    // Getting the arena they are in
	    String arena = BowSpleef.invConfig.getString(player.getName() + ".arena");
	    // Replaceing their Gamemode
	    int gmnum = BowSpleef.invConfig.getInt(player.getName() + ".return.gamemode");
	    GameMode gm = GameMode.getByValue(gmnum);
	    player.setGameMode(gm);
	    // Replacing their Inventory
	    loadInv(player);

	    // Teleporting them back to their orig loc
	    int x = BowSpleef.invConfig.getInt(player.getName() + ".return.x");
	    int y = BowSpleef.invConfig.getInt(player.getName() + ".return.y");
	    int z = BowSpleef.invConfig.getInt(player.getName() + ".return.z");
	    World w = Bukkit.getWorld(BowSpleef.invConfig.getString(player.getName() + ".return.world"));
	    Location retpos = new Location(w, x, y, z);
	    player.teleport(retpos);
	    // Removing them from the config
	    BowSpleef.invConfig.set(player.getName(), null);

	    List<String> players = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".players");
	    List<String> voted = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".voted");
	    players.remove(player.getName());
	    voted.remove(player.getName());
	    BowSpleef.arenaConfig.set("arenas." + arena + ".players", players);
	    BowSpleef.arenaConfig.set("arenas." + arena + ".voted", voted);

	    BowSpleefQuitEvent eventS = new BowSpleefQuitEvent(player, arena);
	    Bukkit.getServer().getPluginManager().callEvent(eventS);

	    if (players.size() == 1)
	    {
		Player player1 = Bukkit.getPlayer(players.get(0));

		Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "[BowSpleef] " + ChatColor.GRAY + players.get(0) + " Won BowSpleef!");
		BowSpleef.arenaConfig.set("arenas." + arena + ".inGame", false);

		Methods.regen(arena, plugin);

		// Getting the arena they are in
		String arena1 = BowSpleef.invConfig.getString(player1.getName() + ".arena");
		// Replaceing their Gamemode
		int gmnum1 = BowSpleef.invConfig.getInt(player1.getName() + ".return.gamemode");
		GameMode gm1 = GameMode.getByValue(gmnum1);
		player1.setGameMode(gm1);
		// Replacing their Inventory
		loadInv(player1);
		// Teleporting them back to their orig loc
		int x1 = BowSpleef.invConfig.getInt(player1.getName() + ".return.x");
		int y1 = BowSpleef.invConfig.getInt(player1.getName() + ".return.y");
		int z1 = BowSpleef.invConfig.getInt(player1.getName() + ".return.z");
		World w1 = Bukkit.getWorld(BowSpleef.invConfig.getString(player1.getName() + ".return.world"));
		Location retpos1 = new Location(w1, x1, y1, z1);
		player1.teleport(retpos1);
		// Removing them from the config
		BowSpleef.invConfig.set(player1.getName(), null);

		List<String> players1 = BowSpleef.arenaConfig.getStringList("arenas." + arena1 + ".players");
		List<String> voted1 = BowSpleef.arenaConfig.getStringList("arenas." + arena1 + ".voted");
		players1.remove(player1.getName());
		voted1.remove(player1.getName());
		BowSpleef.arenaConfig.set("arenas." + arena1 + ".players", players1);
		BowSpleef.arenaConfig.set("arenas." + arena1 + ".voted", voted1);
		return;
	    }
	    plugin.saveConfig();
	    return;
	}
	Methods.pm("You aren't in an Arena!", player);
	return;
    }

    public static boolean join(Player player, String arena, BowSpleef plugin)
    {
	if (player.hasPermission("bs.join"))
	{
	    if (BowSpleef.arenaConfig.contains("arenas." + arena))
	    {
		if (BowSpleef.arenaConfig.getBoolean("arenas." + arena + ".enabled"))
		{
		    if (!BowSpleef.arenaConfig.getBoolean("arenas." + arena + ".inGame"))
		    {
			if (!BowSpleef.invConfig.contains(player.getName()))
			{
			    List<String> players = BowSpleef.arenaConfig.getStringList("arenas." + arena + ".players");
			    int max = BowSpleef.arenaConfig.getInt("arenas." + arena + ".max-players");
			    if (players.size() == max)
			    {
				Methods.pm("That arena is already full!", player);
			    }
			    if (players.size() >= Math.round(max * .66))
			    {
				// Start the Arena

				BowSpleefCountdownEvent event = new BowSpleefCountdownEvent(arena);

				Bukkit.getServer().getPluginManager().callEvent(event);

				new Countdown(event.getArena()).runTaskTimer(plugin, 0L, 20L);

				BowSpleef.arenaConfig.set("arenas." + arena + ".inGame", true);

				plugin.saveConfig();

			    }
			    // Storage for Return GM
			    int gm = player.getGameMode().getValue();

			    BowSpleef.invConfig.set(player.getName() + ".return.gamemode", gm);
			    // Storage for Returning to Pos
			    Location returnPos = player.getLocation();
			    BowSpleef.invConfig.set(player.getName() + ".return.x", returnPos.getBlockX());
			    BowSpleef.invConfig.set(player.getName() + ".return.y", returnPos.getBlockY());
			    BowSpleef.invConfig.set(player.getName() + ".return.z", returnPos.getBlockZ());
			    BowSpleef.invConfig.set(player.getName() + ".return.world", returnPos.getWorld().getName());

			    Inventory storage = InventoryUtil.getArmorInventory(player.getInventory());
			    Inventory GlobalInv = InventoryUtil.getContentInventory(player.getInventory());
			    BowSpleef.invConfig.set(player.getName() + ".inventory", InventoryUtil.toBase64(GlobalInv));
			    BowSpleef.invConfig.set(player.getName() + ".armor", InventoryUtil.toBase64(storage));

			    // Inventory Storage
			    String inv = InventoryUtil.toBase64(player.getInventory());

			    Inventory armorInv = InventoryUtil.getArmorInventory(player.getInventory());

			    String armor = InventoryUtil.toBase64(armorInv);

			    BowSpleef.invConfig.set(player.getName() + ".armor", armor);
			    BowSpleef.invConfig.set(player.getName() + ".inventory", inv);
			    BowSpleef.invConfig.set(player.getName() + ".arena", arena);
			    // Clearing Inventory
			    player.getInventory().clear();
			    player.getInventory().setHelmet(null);
			    player.getInventory().setChestplate(null);
			    player.getInventory().setLeggings(null);
			    player.getInventory().setBoots(null);

			    // Teleporting to the Lobby
			    World world = Bukkit.getServer().getWorld(BowSpleef.arenaConfig.getString("arenas." + arena + ".world"));
			    int x = BowSpleef.arenaConfig.getInt("arenas." + arena + ".lobby.x"), y = BowSpleef.arenaConfig.getInt("arenas." + arena + ".lobby.y"), z = BowSpleef.arenaConfig.getInt("arenas." + arena + ".lobby.z");
			    Location lobby = new Location(world, x, y, z);
			    player.teleport(lobby);
			    // Setting them to Adventure Time
			    player.setGameMode(GameMode.ADVENTURE);
			    // Event
			    BowSpleefJoinEvent event = new BowSpleefJoinEvent(player, arena);
			    Bukkit.getServer().getPluginManager().callEvent(event);

			    players.add(player.getName());
			    BowSpleef.arenaConfig.set("arenas." + arena + ".players", players);
			    if (players.size() == Math.round((BowSpleef.arenaConfig.getInt("arenas." + arena + ".max-players") * 2) / 3))
			    {
				BowSpleefJoinEvent eventS = new BowSpleefJoinEvent(player, arena);
				Bukkit.getServer().getPluginManager().callEvent(eventS);
			    }
			    for (int p = 0; p < players.size(); p++)
			    {
				Methods.pm(player.getName() + " joined the arena!" + " " + players.size() + "/" + max, Bukkit.getPlayer(players.get(p)));
			    }
			    plugin.saveConfig();
			    return true;
			}
			pm("You are already in an arena!", player);
			return true;
		    }

		    pm("That arena is in game!", player);
		    return true;
		}
		pm("That arena is Disabled", player);
		return true;
	    }
	    pm("That Arena doesn't Exist!", player);
	    return true;
	}
	pm("You do not have the required permissions!", player);
	return true;
    }

    public static void regen(String arena, BowSpleef plugin)
    {
	if (BowSpleef.arenaConfig.contains("arenas." + arena))
	{

	    World world = Bukkit.getWorld(BowSpleef.arenaConfig.getString("arenas." + arena + ".world"));

	    int pos1X = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos1.x");
	    int pos1Y = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos1.y");
	    int pos1Z = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos1.z");
	    int pos2X = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos2.x");
	    int pos2Y = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos2.y");
	    int pos2Z = BowSpleef.arenaConfig.getInt("arenas." + arena + ".pos2.z");
	    Location pos1 = new Location(world, pos1X, pos1Y, pos1Z);
	    Location pos2 = new Location(world, pos2X, pos2Y, pos2Z);

	    regen(pos1, pos2, world);

	    plugin.saveConfig();
	}
    }

    public static void start(String arenaName)
    {
	List<String> players = BowSpleef.arenaConfig.getStringList("arenas." + arenaName + ".players");
	World world = Bukkit.getServer().getWorld(BowSpleef.arenaConfig.getString("arenas." + arenaName + ".world"));
	int x = BowSpleef.arenaConfig.getInt("arenas." + arenaName + ".spawn.x");
	int y = BowSpleef.arenaConfig.getInt("arenas." + arenaName + ".spawn.y");
	int z = BowSpleef.arenaConfig.getInt("arenas." + arenaName + ".spawn.z");

	Location spawn = new Location(world, x, y, z);
	for (String givee : players)
	{
	    Player pgivee = Bukkit.getPlayer(givee);
	    pgivee.teleport(spawn);
	    giveItems(pgivee);
	}

	BowSpleef.arenaConfig.set("arenas." + arenaName + ".inGame", true);

    }

    private static void regen(Location loc1, Location loc2, World w)
    {
	int minx = Math.min(loc1.getBlockX(), loc2.getBlockX()), miny = Math.min(loc1.getBlockY(), loc2.getBlockY()), minz = Math.min(loc1.getBlockZ(), loc2.getBlockZ()), maxx = Math.max(loc1.getBlockX(), loc2.getBlockX()), maxy = Math.max(loc1.getBlockY(), loc2.getBlockY()), maxz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
	for (int x = minx; x <= maxx; x++)
	{
	    for (int y = miny; y <= maxy; y++)
	    {
		for (int z = minz; z <= maxz; z++)
		{
		    Block b = w.getBlockAt(x, y, z);
		    b.setType(Material.TNT);
		}
	    }
	}
    }

    public static void pm(String message, Player player)
    {
	player.sendMessage(ChatColor.AQUA + "[BowSpleef] " + ChatColor.GRAY + message);
    }

    private static void giveItems(Player player)
    {
	ItemStack bow = new ItemStack(Material.BOW);
	bow.addEnchantment(Enchantment.ARROW_FIRE, 1);
	bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
	bow.addEnchantment(Enchantment.DURABILITY, 3);
	player.getInventory().addItem(bow);
	player.getInventory().addItem(new ItemStack(Material.ARROW));
    }

    public static void loadInv(Player player)
    {
	if (BowSpleef.invConfig.getString(player.getName() + ".inventory") == null)
	    return;
	Inventory a = InventoryUtil.fromBase64(BowSpleef.invConfig.getString(player.getName() + ".armor"));
	if (a != null)
	{
	    player.getInventory().setArmorContents(a.getContents());
	}
	Inventory i = InventoryUtil.fromBase64(BowSpleef.invConfig.getString(player.getName() + ".inventory"));
	if (i != null)
	{
	    player.getInventory().setContents(i.getContents());
	}
    }

}