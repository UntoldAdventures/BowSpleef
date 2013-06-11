package com.untoldadventures.bowspleef.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.untoldadventures.arena.Arena;
import com.untoldadventures.arena.CustomException;
import com.untoldadventures.arena.Methods;
import com.untoldadventures.arena.events.ArenaEnableEvent;
import com.untoldadventures.arena.events.ArenaStartEvent;
import com.untoldadventures.arena.events.ArenaStopEvent;
import com.untoldadventures.bowspleef.BowSpleef;

public class EventListener implements Listener {

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
    public void onArenaEnable(ArenaEnableEvent event)
    {
	String type = BowSpleef.arenaConfig.getString("arenas." + event.getArena().getName().toLowerCase() + ".type");
	if (type.equalsIgnoreCase("bowspleef"))
	{
	    int pos1X1 = event.getArenaConfig().getInt("arenas." + event.getArena().getName() + ".positions.pos1.x");
	    int pos1Y1 = event.getArenaConfig().getInt("arenas." + event.getArena().getName() + ".positions.pos1.y");
	    int pos1Z1 = event.getArenaConfig().getInt("arenas." + event.getArena().getName() + ".positions.pos1.z");
	    int pos2X1 = event.getArenaConfig().getInt("arenas." + event.getArena().getName() + ".positions.pos2.x");
	    int pos2Y1 = event.getArenaConfig().getInt("arenas." + event.getArena().getName() + ".positions.pos2.y");
	    int pos2Z1 = event.getArenaConfig().getInt("arenas." + event.getArena().getName() + ".positions.pos2.z");
	    World world = Bukkit.getWorld(event.getArenaConfig().getString("arenas." + event.getArena().getName() + ".positions.pos2.world"));

	    Location pos11 = new Location(world, pos1X1, pos1Y1, pos1Z1);
	    Location pos21 = new Location(world, pos2X1, pos2Y1, pos2Z1);

	    this.regen(pos11, pos21, world);

	    plugin.saveConfig();
	}
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onArenaStart(ArenaStartEvent event)
    {
	String type = BowSpleef.arenaConfig.getString("arenas." + event.getArena().getName().toLowerCase() + ".type");
	if (type.equalsIgnoreCase("bowspleef"))
	{

	    List<String> players = BowSpleef.arenaConfig.getStringList("arenas." + event.getArena().getName().toLowerCase() + ".players");

	    for (String givee : players)
	    {
		Player pgivee = Bukkit.getPlayer(givee);
		giveItems(pgivee);
	    }

	    plugin.saveConfig();
	}
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onArenaStop(ArenaStopEvent event)
    {
	String type = BowSpleef.arenaConfig.getString("arenas." + event.getArena().getName().toLowerCase() + ".type");
	if (type.equalsIgnoreCase("bowspleef"))
	{

	    int pos1X1 = event.getArenaConfig().getInt("arenas." + event.getArena().getName() + ".positions.pos1.x");
	    int pos1Y1 = event.getArenaConfig().getInt("arenas." + event.getArena().getName() + ".positions.pos1.y");
	    int pos1Z1 = event.getArenaConfig().getInt("arenas." + event.getArena().getName() + ".positions.pos1.z");
	    int pos2X1 = event.getArenaConfig().getInt("arenas." + event.getArena().getName() + ".positions.pos2.x");
	    int pos2Y1 = event.getArenaConfig().getInt("arenas." + event.getArena().getName() + ".positions.pos2.y");
	    int pos2Z1 = event.getArenaConfig().getInt("arenas." + event.getArena().getName() + ".positions.pos2.z");
	    World world = Bukkit.getWorld(event.getArenaConfig().getString("arenas." + event.getArena().getName() + ".positions.pos2.world"));

	    Location pos11 = new Location(world, pos1X1, pos1Y1, pos1Z1);
	    Location pos21 = new Location(world, pos2X1, pos2Y1, pos2Z1);

	    this.regen(pos11, pos21, world);

	    plugin.saveConfig();
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

			try
			{
			    Methods.joinArena(plugin, arena, player, BowSpleef.arenaConfig, BowSpleef.invConfig);
			    plugin.saveConfig();
			} catch (CustomException e1)
			{
			    e1.printStackTrace();
			}

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
	    if (BowSpleef.arenaConfig.contains("arenas." + event.getLine(2).toLowerCase()))
	    {
		this.pm("Sign creation successful!", event.getPlayer());
		event.setLine(0, ChatColor.DARK_BLUE + "[BowSpleef]");
		event.setLine(1, ChatColor.GREEN + "Join");
		plugin.saveConfig();
		return;
	    }

	}
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent e)
    {
	Player p = e.getPlayer();

	if (BowSpleef.invConfig.contains("players." + p.getName()))
	{
	    Arena arena = new Arena(BowSpleef.invConfig.getString("players." + p.getName() + ".arena"), plugin);
	    String type = BowSpleef.arenaConfig.getString("arenas." + arena.getName().toLowerCase() + ".type");
	    if (type.equalsIgnoreCase("bowspleef"))
	    {
		if (e.getMessage().contains("/bs") || e.getMessage().contains("/bowspleef"))
		{
		    plugin.saveConfig();
		    return;
		}

		pm("You cannot use commands in an arena!", p);
		e.setCancelled(true);
	    }
	}

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDropItem(PlayerDropItemEvent event)
    {
	Player player = event.getPlayer();

	Arena arena = new Arena(BowSpleef.invConfig.getString("players." + player.getName() + ".arena"), plugin);
	String type = BowSpleef.arenaConfig.getString("arenas." + arena.getName().toLowerCase() + ".type");
	if (type.equalsIgnoreCase("bowspleef"))
	{
	    if (BowSpleef.invConfig.contains("players." + player.getName()))
	    {
		event.setCancelled(true);
	    }
	}
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamage(EntityDamageEvent event) throws CustomException
    {
	if (event.getEntity() instanceof Player)
	{
	    Player player = (Player) event.getEntity();

	    Arena arena = new Arena(BowSpleef.invConfig.getString("players." + player.getName() + ".arena"), plugin);
	    String type = BowSpleef.arenaConfig.getString("arenas." + arena.getName().toLowerCase() + ".type");
	    if (type.equalsIgnoreCase("bowspleef"))
	    {
		if (BowSpleef.invConfig.contains("players." + player.getName()))
		{

		    switch (event.getCause())
		    {
			case CONTACT:
			    Methods.quitArena(plugin, player, BowSpleef.arenaConfig, BowSpleef.invConfig);
			    break;
			case DROWNING:
			    Methods.quitArena(plugin, player, BowSpleef.arenaConfig, BowSpleef.invConfig);
			    break;
			case FALL:
			    Methods.quitArena(plugin, player, BowSpleef.arenaConfig, BowSpleef.invConfig);
			    break;
			case FIRE:
			    Methods.quitArena(plugin, player, BowSpleef.arenaConfig, BowSpleef.invConfig);
			    break;
			case FIRE_TICK:
			    Methods.quitArena(plugin, player, BowSpleef.arenaConfig, BowSpleef.invConfig);
			    break;
			case LAVA:
			    Methods.quitArena(plugin, player, BowSpleef.arenaConfig, BowSpleef.invConfig);
			    break;
			case VOID:
			    Methods.quitArena(plugin, player, BowSpleef.arenaConfig, BowSpleef.invConfig);
			    break;
			default:
			    break;
		    }
		    plugin.saveConfig();
		    event.setCancelled(true);
		}
	    }
	}
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) throws CustomException
    {
	Player player = event.getPlayer();

	if (BowSpleef.invConfig.contains(player.getName()))
	{
	    Methods.quitArena(plugin, player, BowSpleef.arenaConfig, BowSpleef.invConfig);
	    plugin.saveConfig();
	}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlowUp(ExplosionPrimeEvent event)
    {

	List<String> arenas = BowSpleef.arenaConfig.getStringList("list.arenas");
	for (String name : arenas)
	{
	    int pos1X1 = BowSpleef.arenaConfig.getInt("arenas." + name.toLowerCase() + ".positions.pos1.x");
	    int pos1Y1 = BowSpleef.arenaConfig.getInt("arenas." + name.toLowerCase() + ".positions.pos1.y");
	    int pos1Z1 = BowSpleef.arenaConfig.getInt("arenas." + name.toLowerCase() + ".positions.pos1.z");
	    int pos2X1 = BowSpleef.arenaConfig.getInt("arenas." + name.toLowerCase() + ".positions.pos2.x");
	    int pos2Y1 = BowSpleef.arenaConfig.getInt("arenas." + name.toLowerCase() + ".positions.pos2.y");
	    int pos2Z1 = BowSpleef.arenaConfig.getInt("arenas." + name.toLowerCase() + ".positions.pos2.z");
	    World world = Bukkit.getWorld(BowSpleef.arenaConfig.getString("arenas." + name.toLowerCase() + ".positions.pos2.world"));

	    Location loc1 = new Location(world, pos1X1, pos1Y1, pos1Z1);
	    Location loc2 = new Location(world, pos2X1, pos2Y1, pos2Z1);

	    int minx = Math.min(loc1.getBlockX(), loc2.getBlockX()), miny = Math.min(loc1.getBlockY(), loc2.getBlockY()), minz = Math.min(loc1.getBlockZ(), loc2.getBlockZ()), maxx = Math.max(loc1.getBlockX(), loc2.getBlockX()), maxy = Math.max(loc1.getBlockY(), loc2.getBlockY()), maxz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
	    for (int x = minx; x <= maxx; x++)
	    {
		for (int y = miny; y <= maxy; y++)
		{
		    for (int z = minz; z <= maxz; z++)
		    {
			Block block = world.getBlockAt(x, y, z);
			if (block.getChunk().equals(event.getEntity().getLocation().getChunk()))
			{
			    event.setCancelled(true);
			    return;
			}
		    }
		}
	    }
	}

	plugin.saveConfig();
    }
    /*
     * @EventHandler(priority = EventPriority.HIGHEST) public void
     * onArrowHitBlock(ProjectileHitEvent e) { LivingEntity entity =
     * e.getEntity().getShooter(); if ((entity instanceof Player)) { Player
     * shooter = (Player) entity;
     * 
     * BlockIterator bi = new BlockIterator(e.getEntity().getWorld(),
     * e.getEntity().getLocation().toVector(),
     * e.getEntity().getVelocity().normalize(), 0.0D, 4); Block block = null;
     * while (bi.hasNext()) { block = bi.next(); if (block.getTypeId() != 0) {
     * break; } }
     * 
     * block.setType(Material.AIR); e.getEntity().remove(); } }
     */
}