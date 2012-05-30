package net.minekingdom.snaipe.Thieves.managers;

import net.minekingdom.snaipe.Thieves.ItemValues;
import net.minekingdom.snaipe.Thieves.Language;
import net.minekingdom.snaipe.Thieves.Thieves;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor
{
	
	public final Thieves plugin;
	
	public CommandManager()
	{
		plugin = Thieves.getInstance();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (command.getName().equals("thieves"))
		{
			Player player = null;
			
			if (sender instanceof Player)
				player = (Player) sender;
			
			if (args.length > 0)
			{
				if(args[0].toUpperCase().equals("HELP")) {
					if(player != null) {
						player.sendMessage(ChatColor.GREEN + "===Thieves Help===");
						if(player.hasPermission("thieves.toggle"))
							player.sendMessage(ChatColor.GREEN + "/thieves toggle [Server/Self] - Toggles thievery for all players (Server) or just you (Self)");
						else
							player.sendMessage(ChatColor.GREEN + "/thieves toggle - Toggles thievery on and off. While off, Shift-Clicking someone will not attempt to steal.");
						
						player.sendMessage(ChatColor.GREEN + "/thieves level (lvl) - Displays the current thievery level and remaning experience to the next level.");
						player.sendMessage(ChatColor.GREEN + "/thieves valueof - Displays the item value of a given item.");
						player.sendMessage(ChatColor.GREEN + "/thieves maxvalue - Displays the maximum item value that can be stolen at any one time.");
						player.sendMessage(ChatColor.GREEN + "/thieves cooldown (cd) - Displays the the time remaning until thievery is availible again.");
						
						if(player.hasPermission("thieves.reload"))
							player.sendMessage(ChatColor.GREEN + "/thieves reload - Reloads config files for thievery and all players with access to thievery");
					}
					else {
						Thieves.log("/thieves toggle [Server/Self] - Toggles thievery for all players (Server) or just you (Self)");
						Thieves.log("/thieves reload - Reloads config files for thievery and all players with access to thievery");
					}
				}
				
				if(args[0].toUpperCase().equals("LEVEL") || args[0].toUpperCase().equals("LVL")) {
					if(player != null) {
						int level = plugin.getPlayerManager().getPlayer(player).getLevel();
						long xp = plugin.getPlayerManager().getPlayer(player).getThiefExperience();
						long nextXP = plugin.getPlayerManager().getPlayer(player).getExperienceToNextLevel();
						player.sendMessage(ChatColor.GREEN + "[Thieves] Current Level " + level +".");
						player.sendMessage(ChatColor.GREEN + "[Thieves] Current Experience: " + xp + "/" + nextXP +".");
					}
				}
				
				if(args[0].toUpperCase().equals("COOLDOWN") || args[0].toUpperCase().equals("CD")) {
					if(player != null) {
						int cooldown = (int) (plugin.getPlayerManager().getPlayer(player).getCooldown() / 1000);
						if(cooldown > 0) {
							player.sendMessage(ChatColor.GREEN + "[Thieves] Cooldown Remaining: " + cooldown + " seconds.");
						}
						else {
							player.sendMessage(ChatColor.GREEN + "[Thieves] No Cooldown Remaining.");
						}
					}
				}
				
				if(args[0].toUpperCase().equals("VALUEOF")) {
					if(args.length >= 2) {
						Material material;
						try {
							material = Material.getMaterial(Integer.parseInt(args[1]));
						} catch (NumberFormatException e) {
							material = Material.getMaterial(args[1].toUpperCase());
						}
						if(player != null) {
							if(material == null)
								player.sendMessage(ChatColor.GREEN + "[Thieves] Item not recognized. Try typing the item ID.");
							else
								player.sendMessage(ChatColor.GREEN + "[Thieves] Item value of " + material.toString() + " is " + ItemValues.valueOf(material) +".");
						}
						else {
							if(material == null)
								Thieves.log("Invalid item type. Try typing the item ID");
							else
								Thieves.log("Item value of " + material.toString() + " is " + ItemValues.valueOf(material) +".");
						}
					}
					else {
						if(player != null) {
							player.sendMessage(ChatColor.GREEN + "/thieves valueof <Item>");
						}
						else {
							Thieves.log("/thieves valueof <Item>");
						}
					}
				}
				
				if(args[0].toUpperCase().equals("MAXVALUE")) {
					if(player != null) {
						player.sendMessage(ChatColor.GREEN + "You may steal a maximum item value of " + plugin.getPlayerManager().getPlayer(player).getMaxItemWealth() + " from your targets.");
					}
				}
				
				if (args[0].toUpperCase().equals("TOGGLE"))
				{
					if (player != null)
					{
						if (player.hasPermission("thieves.toggle"))
						{
							if(args.length >= 2) {
								if(args[1].toUpperCase().equals("SERVER")) {
									if (Thieves.isTheftEnabled)
									{
										Thieves.isTheftEnabled = false;
										plugin.getPlayerManager().removeAllThieves();
										
										player.sendMessage(ChatColor.GREEN + "[Thieves] " + Language.disabled);
									}
									else
									{
										Thieves.isTheftEnabled = true;
										
										player.sendMessage(ChatColor.GREEN + "[Thieves] " + Language.enabled);
									}
								}
								else {
									if(args[1].toUpperCase().equals("SELF")) {
										boolean newEnabledState = !plugin.getPlayerManager().getPlayer(player).getEnabled();
										plugin.getPlayerManager().getPlayer(player).setEnabled(newEnabledState);
										player.sendMessage(ChatColor.GREEN + "[Thieves] Stealing is now " + (newEnabledState ? "Enabled." : "Disabled."));
									}
								}
							}
							else {
								boolean newEnabledState = !plugin.getPlayerManager().getPlayer(player).getEnabled();
								plugin.getPlayerManager().getPlayer(player).setEnabled(newEnabledState);
								player.sendMessage(ChatColor.GREEN + "[Thieves] Stealing is now " + (newEnabledState ? "Enabled." : "Disabled."));
							}
						}
						else {
							boolean newEnabledState = !plugin.getPlayerManager().getPlayer(player).getEnabled();
							plugin.getPlayerManager().getPlayer(player).setEnabled(newEnabledState);
							player.sendMessage(ChatColor.GREEN + "[Thieves] Stealing is now " + (newEnabledState ? "Enabled." : "Disabled."));
						}
					}
					else
					{
						if (Thieves.isTheftEnabled)
						{
							Thieves.isTheftEnabled = false;
							plugin.getPlayerManager().removeAllThieves();
							
							Thieves.log(Language.disabled);
						}
						else
						{
							Thieves.isTheftEnabled = true;
							
							Thieves.log(Language.enabled);
						}
					}
					
					return true;
				}
				
				if (args[0].toUpperCase().equals("RELOAD"))
				{
					
					if (player != null)
					{
						if (player.hasPermission("thieves.reload"))
						{
							plugin.getPlayerManager().removeAllThieves();
							plugin.getSettingManager().loadConfig();
							
							player.sendMessage(ChatColor.GREEN + "[Thieves] Configuration reloaded.");
						}
					}
					else
					{
						plugin.getPlayerManager().removeAllThieves();
						plugin.getSettingManager().loadConfig();
						
						Thieves.log("Configuration reloaded.");
					}
					
					return true;
				}
			}
		}
		return false;
	}
	
}
