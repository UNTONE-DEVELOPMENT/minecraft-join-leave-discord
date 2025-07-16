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
                plugin.getLogger().warning("Failed to send webhook: " + ex.getMessage());
            }
        });
    }


    public DiscordWebhook.EmbedObject prepareCommonEmbedContent(DiscordWebhook.EmbedObject embed, Player player) {
        return prepareCommonEmbedContent(embed, player, false, true);
    }

    public DiscordWebhook.EmbedObject prepareCommonEmbedContent(DiscordWebhook.EmbedObject embed, Player player, boolean left, boolean showPlayers) {
        long time = Instant.now().getEpochSecond();

        embed.setThumbnail("https://mc-heads.net/avatar/" + player.getName());

        if (plugin.getConfig().getBoolean("show-timestamp"))
            embed.addField("Time", String.format("<t:%d:f>", time), false);
        if (plugin.getConfig().getBoolean("show-uuid"))
            embed.addField("Player UUID", "`" + player.getUniqueId() + "`", false);

        int players = plugin.getServer().getOnlinePlayers().size() - (left ? 1 : 0); // if a player's just left, we need to subtract one, since that hasn't occured yet
        embed.setFooter(players + "/" + plugin.getServer().getMaxPlayers() + " players online", null);

        return embed;
    }

    public void sendHook(DiscordWebhook.EmbedObject embed, Player player, String url) {
        DiscordWebhook hook = new DiscordWebhook(url);
        hook.setUsername(player.getName());
        hook.setAvatarUrl("https://mc-heads.net/avatar/" + player.getName());
        hook.addEmbed(embed);

        sendWebhookAsync(hook);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent p) {
        Player player = p.getPlayer();

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
        embed.setTitle(player.getName() + " has joined the server");
        embed.setColor(Color.GREEN);

        embed = prepareCommonEmbedContent(embed, player);

        if (plugin.getConfig().getBoolean("show-client-brand")) {
            String brand = player.getClientBrandName() != null ? player.getClientBrandName() : "Unknown";
            embed.addField("Client Brand", brand, false);
        }

        sendHook(embed, player, plugin.getConfig().getString("webhook-url"));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent p) {
        Player player = p.getPlayer();

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
        embed.setTitle(player.getName() + " has left the server");
        embed.setColor(Color.RED);

        embed = prepareCommonEmbedContent(embed, player, true, true);

        sendHook(embed, player, plugin.getConfig().getString("webhook-url"));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if (plugin.getConfig().getString("death-webhook-url") == "") return;

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
        embed.setTitle(e.getDeathMessage());
        embed.setColor(Color.GRAY);

        embed = prepareCommonEmbedContent(embed, player, false, false);

        sendHook(embed, player, plugin.getConfig().getString("death-webhook-url"));
    }
}
