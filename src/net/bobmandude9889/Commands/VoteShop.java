package net.bobmandude9889.Commands;

import java.util.List;

import net.bobmandude9889.GUI.GUI;
import net.bobmandude9889.Methods.Item;
import net.bobmandude9889.Methods.VotePoints;
import net.bobmandude9889.iZenith.Variables;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

public class VoteShop implements IZCommand {

	Variables vars;

	public VoteShop(Variables vars) {
		this.vars = vars;
	}

	@Override
	public String getName() {
		return "voteshop";
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		try {
			if (sender instanceof Player) {
				switch (args[0]) {
				case "create":
					vars.createVoteShop.add((Player) sender);
					sender.sendMessage(ChatColor.GREEN + "Place a block to create a voteshop!");
					break;
				case "delete":
					vars.deleteVoteShop.add((Player) sender);
					sender.sendMessage(ChatColor.GREEN + "Right click a voteshop to delete it!");
					break;
				case "set":
					VotePoints.set(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]), vars);
					VotePoints.saveConfig(vars);
					sender.sendMessage(Bukkit.getPlayer(args[1]).getName() + " now has " + VotePoints.get(Bukkit.getPlayer(args[1]), vars) + " points.");
					break;
				case "add":
					VotePoints.set(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]) + VotePoints.get(Bukkit.getPlayer(args[1]), vars), vars);
					VotePoints.saveConfig(vars);
					sender.sendMessage(Bukkit.getPlayer(args[1]).getName() + " now has " + VotePoints.get(Bukkit.getPlayer(args[1]), vars) + " points.");
					break;
				case "get":
					Player player = Bukkit.getPlayer(args[1]);
					sender.sendMessage(player.getName() + " has " + VotePoints.get(player, vars) + " points.");
					break;
				case "open":
					VoteShop.openVoteShop((Player) sender, vars);
					break;
				default:
					sender.sendMessage(ChatColor.RED + "Invalid arguments");
				}
			} else {
				switch (args[0]) {
				case "set":
					VotePoints.set(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]), vars);
					VotePoints.saveConfig(vars);
					sender.sendMessage(Bukkit.getPlayer(args[1]).getName() + " now has " + VotePoints.get(Bukkit.getPlayer(args[1]), vars) + " points.");
					break;
				case "add":
					VotePoints.set(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]) + VotePoints.get(Bukkit.getPlayer(args[1]), vars), vars);
					VotePoints.saveConfig(vars);
					sender.sendMessage(Bukkit.getPlayer(args[1]).getName() + " now has " + VotePoints.get(Bukkit.getPlayer(args[1]), vars) + " points.");
					break;
				case "get":
					Player player = Bukkit.getPlayer(args[1]);
					sender.sendMessage(player.getName() + " has " + VotePoints.get(player, vars) + " points.");
					break;
				default:
					sender.sendMessage(ChatColor.RED + "Invalid arguments");
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			sender.sendMessage(ChatColor.RED + "Invalid arguments");
		}
	}

	@Override
	public boolean onlyPlayers() {
		return false;
	}

	@Override
	public boolean hasPermission() {
		return true;
	}

	@Override
	public Permission getPermission() {
		return new Permission("izenith.voteshop");
	}

	public static void openVoteShop(Player player, Variables vars) {
		GUI shop = vars.voteShopMain;
		shop.addButton(Item.newItemMeta(Material.DIAMOND, ChatColor.BLUE + "" + VotePoints.get(player, vars) + " Points", null, 1), 8, new Runnable() {
			public void run() {
			}
		});
		shop.open(player);
	}

	public static void openKitShop(final Player player, final Variables vars) {
		final List<String> kitStringList = vars.voteShopConfig.getStringList("kit_shop");
		final GUI shop = vars.voteShopKits;
		for (String s : kitStringList) {
			final String[] split = s.split(";");
			final String name = split[0];
			final String lore = split[1];
			final Material material = Material.getMaterial(split[2]);
			final String kit = split[3];
			final int cost = Integer.parseInt(split[4]);
			shop.addButton(Item.newItemMeta(material, (VotePoints.get(player, vars) >= cost ? ChatColor.GREEN : ChatColor.RED) + name + ChatColor.DARK_PURPLE + " [" + cost + "]", lore, 1), kitStringList.indexOf(s), new Runnable() {
				public void run() {
					if (VotePoints.get(player, vars) >= cost) {
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "kit " + kit + " " + player.getName());
						VotePoints.set(player, VotePoints.get(player, vars) - cost, vars);
						for (String s : kitStringList) {
							if (VotePoints.get(player, vars) < Integer.parseInt(s.split(";")[4])) {
								openKitShop(player, vars);
								break;
							}
						}
					} else {
						player.sendMessage(ChatColor.RED + "You do not have enough points to buy that.");
					}
				}
			});
		}
		shop.open(player);
	}

	public static void openSpawnerShop(final Player player, final Variables vars) {
		final List<String> spawnerStringList = vars.voteShopConfig.getStringList("spawner_shop");
		final GUI shop = vars.voteShopSpawners;
		for (String s : spawnerStringList) {
			final String[] split = s.split(";");
			final String name = split[0];
			final String lore = split[1];
			final short type = Short.parseShort(split[2]);
			final int cost = Integer.parseInt(split[3]);
			shop.addButton(Item.newItemMeta(Material.MOB_SPAWNER, (VotePoints.get(player, vars) >= cost ? ChatColor.GREEN : ChatColor.RED) + name + ChatColor.DARK_PURPLE + " [" + cost + "]", lore, 1), spawnerStringList.indexOf(s), new Runnable() {
				public void run() {
					if (VotePoints.get(player, vars) >= cost) {
						ItemStack spawner = new ItemStack(Material.MOB_SPAWNER, 1, type);
						player.getInventory().addItem(spawner);
						VotePoints.set(player, VotePoints.get(player, vars) - cost, vars);
						for (String s : spawnerStringList) {
							if (VotePoints.get(player, vars) < Integer.parseInt(s.split(";")[3])) {
								openSpawnerShop(player, vars);
								break;
							}
						}
					} else {
						player.sendMessage(ChatColor.RED + "You do not have enough points to buy that.");
					}
				}
			});
			shop.open(player);
		}
	}

}