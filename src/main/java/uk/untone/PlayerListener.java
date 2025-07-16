package uk.untone;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;

public class PlayerListener implements Listener {
    private final DiscordJoinLeaveMessages plugin;

    PlayerListener(DiscordJoinLeaveMessages plugin) {
        this.plugin = plugin;
    }

    private void sendWebhookAsync(DiscordWebhook hook) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                hook.execute();
            } catch (IOException ex) {
                plugin.getLogger().warning("Failed to send webhook: " + ex.getMessage());;
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent p) {
        Player player = p.getPlayer();
        long time = Instant.now().getEpochSecond();

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
        embed.setTitle(player.getName() + " has joined the server");
        embed.setColor(Color.GREEN);
        embed.setThumbnail("https://mc-heads.net/avatar/" + player.getName());
        if (plugin.getConfig().getBoolean("show-timestamp")) embed.addField("Time", String.format("<t:%d:f>", time), false);
        if (player.getClientBrandName() != null && plugin.getConfig().getBoolean("show-client-brand")) embed.addField("Client Brand", player.getClientBrandName(), false);
        if (plugin.getConfig().getBoolean("show-uuid")) embed.setFooter(player.getUniqueId().toString(), null);

        DiscordWebhook hook = new DiscordWebhook(plugin.getConfig().getString("webhook-url"));
        hook.setUsername(player.getName());
        hook.setAvatarUrl("https://mc-heads.net/avatar/" + player.getName());
        hook.addEmbed(embed);

        sendWebhookAsync(hook);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent p) {
        Player player = p.getPlayer();
        long time = Instant.now().getEpochSecond();

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
        embed.setTitle(player.getName() + " has left the server");
        embed.setColor(Color.RED);
        embed.setThumbnail("https://mc-heads.net/avatar/" + player.getName());
        if (plugin.getConfig().getBoolean("show-timestamp")) embed.addField("Time", String.format("<t:%d:f>", time), false);
        if (plugin.getConfig().getBoolean("show-uuid")) embed.setFooter(player.getUniqueId().toString(), null);

        DiscordWebhook hook = new DiscordWebhook(plugin.getConfig().getString("webhook-url"));
        hook.setUsername(player.getName());
        hook.setAvatarUrl("https://mc-heads.net/avatar/" + player.getName());
        hook.addEmbed(embed);

        sendWebhookAsync(hook);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (plugin.getConfig().getString("death-webhook-url") == "") return;
        long time = Instant.now().getEpochSecond();

        Player player = e.getEntity();
        Player killer = player.getKiller();

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();

        if (killer != null) {
            embed.setTitle(player.getName() + " was killed by " + killer.getName());
        } else {
            embed.setTitle(player.getName() + " died");
        }

        embed.setDescription(e.getDeathMessage());
        embed.setColor(Color.GRAY);
        embed.setThumbnail("https://mc-heads.net/avatar/" + player.getName());
        if (plugin.getConfig().getBoolean("show-timestamp")) embed.addField("Time", String.format("<t:%d:f>", time), false);

        DiscordWebhook hook = new DiscordWebhook(plugin.getConfig().getString("death-webhook-url"));
        hook.setUsername(player.getName());
        hook.setAvatarUrl("https://mc-heads.net/avatar/" + player.getName());
        hook.addEmbed(embed);

        sendWebhookAsync(hook);
    }
}
