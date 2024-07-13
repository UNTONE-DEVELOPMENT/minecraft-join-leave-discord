package uk.untone;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.io.IOException;

public class PlayerListener implements Listener {
    public String webhook;
    PlayerListener(String webhook) {
        this.webhook = webhook;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent p) {
        Player player = p.getPlayer();

        DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
        embed.setTitle(player.getName() + " has joined the server");
        embed.setColor(Color.GREEN);
        embed.setImage("https://mc-heads.net/avatar/" + player.getName());

        DiscordWebhook hook = new DiscordWebhook(webhook);
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
        embed.setImage("https://mc-heads.net/avatar/" + player.getName());

        DiscordWebhook hook = new DiscordWebhook(webhook);
        hook.addEmbed(embed);
        try {
            hook.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
