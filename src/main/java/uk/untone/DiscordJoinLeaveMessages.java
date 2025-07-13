package uk.untone;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiscordJoinLeaveMessages extends JavaPlugin {
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        config.addDefault("webhook-url", "https://discord.com/api/webhooks/your-webhook");
        config.addDefault("death-webhook-url", "");
        config.options().copyDefaults(true);
        saveConfig();

        if(config.getString("webhook-url") == "https://discord.com/api/webhooks/your-webhook") {
            getLogger().warning("Server does not have Webhook URL defined, please set up in config");
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(config.getString("webhook-url"), config.getString("death-webhook-url")), this);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
