package uk.untone;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiscordJoinLeaveMessages extends JavaPlugin {
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        config.addDefault("webhook-url", "https://discord.com/api/webhooks/your-webhook");
        config.options().copyDefaults(true);
        saveConfig();


        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerListener(config.getString("webhook-url")), this);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
