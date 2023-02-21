package de.zonlykroks.persephone.listener;

import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new PersephonePlayer(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PersephonePlayer.removePlayer(event.getPlayer().getUniqueId());
    }
}
