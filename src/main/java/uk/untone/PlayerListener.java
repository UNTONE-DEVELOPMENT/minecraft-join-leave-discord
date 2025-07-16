package uk.untone;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.io.IOException;

public class PlayerListener implements Listener {
    public String webhook;
    public String deathWebhook;

    PlayerListener(String webhook, String deathWebhook) {
        this.webhook = webhook;
        this.deathWebhook = deathWebhook;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent p) {
        Player player = p.getPlayer();

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
        embed.setTitle(player.getName() + " has joined the server");
        embed.setColor(Color.GREEN);
        embed.setThumbnail("https://mc-heads.net/avatar/" + player.getName());

        DiscordWebhook hook = new DiscordWebhook(webhook);
        hook.setUsername(player.getName());
        hook.setAvatarUrl("https://mc-heads.net/avatar/" + player.getName());
        hook.addEmbed(embed);

        try {
            hook.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent p) {
        Player player = p.getPlayer();

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
        embed.setTitle(player.getName() + " has left the server");
        embed.setColor(Color.RED);
        embed.setThumbnaily("https://mc-heads.net/avatar/" + player.getName());

        DiscordWebhook hook = new DiscordWebhook(webhook);
        hook.setUsername(player.getName());
        hook.setAvatarUrl("https://mc-heads.net/avatar/" + player.getName());
        hook.addEmbed(embed);

        try {
            hook.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (deathWebhook == "") return;

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

        DiscordWebhook hook = new DiscordWebhook(deathWebhook);
        hook.setUsername(player.getName());
        hook.setAvatarUrl("https://mc-heads.net/avatar/" + player.getName());
        hook.addEmbed(embed);

        try {
            hook.execute();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
