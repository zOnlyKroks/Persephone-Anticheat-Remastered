package de.zonlykroks.persephone;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import de.zonlykroks.persephone.check.PlayerSpecificCheckInitiator;
import de.zonlykroks.persephone.listener.PlayerJoinQuitListener;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
public final class Persephone extends JavaPlugin {

    public static Persephone persephone;

    @Override
    public void onEnable() {
        persephone = this;
        PacketEventsSettings packetEventsSettings = new PacketEventsSettings().bStats(true).checkForUpdates(false).debug(true);
        PacketEventsAPI<Plugin> eventsAPI = SpigotPacketEventsBuilder.build(this,packetEventsSettings);
        PacketEvents.setAPI(eventsAPI);
        PacketEvents.getAPI().init();

        PlayerSpecificCheckInitiator.packagesToScan.add("de.zonlykroks.persephone.check.impl");

        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(),this);
    }

    @Override
    public void onDisable() {
    }
}
