package net.bobmandude9889.iZenith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.bobmandude9889.Commands.Donator;
import net.bobmandude9889.Commands.End;
import net.bobmandude9889.Commands.IZCommand;
import net.bobmandude9889.Commands.NoMobs;
import net.bobmandude9889.Commands.NoOutFire;
import net.bobmandude9889.Commands.Pvp;
import net.bobmandude9889.Commands.Ranks;
import net.bobmandude9889.Commands.SetNoMobs;
import net.bobmandude9889.Events.BlockBreakEventHandler;
import net.bobmandude9889.Events.BlockFadeEventHandler;
import net.bobmandude9889.Events.CreatureSpawnEventHandler;
import net.bobmandude9889.Events.EntityDamageByEntityEventHandler;
import net.bobmandude9889.Events.EntityDamageEventHandler;
import net.bobmandude9889.Events.EventRegisterer;
import net.bobmandude9889.Events.PlayerCommandPreprocessEventHandler;
import net.bobmandude9889.Events.PlayerInteractEventHandler;
import net.bobmandude9889.Events.PlayerJoinEventHandler;
import net.bobmandude9889.Events.PlayerQuitEventHandler;
import net.bobmandude9889.Events.PluginEnableEventHandler;
import net.bobmandude9889.GUI.GUI;
import net.bobmandude9889.GUI.GUIHandler;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Variables {
	
	public final HashMap<String, Location> selectors = new HashMap<String, Location>();
	public final HashMap<String, Location> selection1 = new HashMap<String, Location>();
	public final HashMap<String, Location> selection2 = new HashMap<String, Location>();
	public List<Location> fires = new ArrayList<Location>();
	public GUIHandler handler = null;
	public GUI donate = null;
	public GUI vote = null;
	public EventRegisterer er = null;
	public int broadcastMessage = 0;
	@SuppressWarnings("unused")
	private JavaPlugin plugin = null;
	public List<Listener> listeners = new ArrayList<Listener>();
	public List<IZCommand> commands = new ArrayList<IZCommand>();
	
	public Variables(JavaPlugin plugin){
		this.plugin = plugin;
		er = new EventRegisterer(plugin,this);
		
		listeners.add(new BlockBreakEventHandler(plugin,this));
		listeners.add(new BlockFadeEventHandler(plugin,this));
		listeners.add(new CreatureSpawnEventHandler(plugin,this));
		listeners.add(new EntityDamageEventHandler(plugin,this));
		listeners.add(new PlayerCommandPreprocessEventHandler(plugin,this));
		listeners.add(new PlayerInteractEventHandler(plugin,this));
		listeners.add(new PlayerJoinEventHandler(plugin,this));
		listeners.add(new PlayerQuitEventHandler(plugin,this));
		listeners.add(new EntityDamageByEntityEventHandler());
		listeners.add(new PluginEnableEventHandler(er));
		commands.add(new Donator());
		commands.add(new End());
		commands.add(new NoMobs(this));
		commands.add(new NoOutFire());
		commands.add(new Pvp());
		commands.add(new Ranks(plugin, this));
		commands.add(new SetNoMobs(plugin, this));
	}
	
}