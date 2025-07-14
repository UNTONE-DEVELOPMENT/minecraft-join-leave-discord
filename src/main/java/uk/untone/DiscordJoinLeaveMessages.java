package uk.untone;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public final class DiscordJoinLeaveMessages extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getConfig().options().copyDefaults(true);
        saveConfig();

        if(getConfig().getString("webhook-url") == "https://discord.com/api/webhooks/your-webhook") {
            getLogger().warning("Server does not have Webhook URL defined, please set up in config");
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("djlm") && args.length == 0) {
            sender.sendMessage("DiscordJoinLeaveMessages version " + getDescription().getVersion());
            return true;
        }

        if (command.getName().equalsIgnoreCase("djlm") && args[0].equals("reload")) {
            if (!sender.hasPermission("djlm.reload")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                return true;
            }

            reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
            return true;
        }


        return false;
    }
}
