package de.zonlykroks.persephone.processor;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.Location;

public class BlockProcessor extends PacketListenerAbstract {

    private final PersephonePlayer persephonePlayer;

    public BlockProcessor(PersephonePlayer persephonePlayer) {
        this.persephonePlayer = persephonePlayer;
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {

            persephonePlayer.getActionProcessor().handleBlockPlace();

            WrapperPlayClientPlayerBlockPlacement wrapperPlayClientPlayerBlockPlacement = new WrapperPlayClientPlayerBlockPlacement(event);
            persephonePlayer.placedBlockPosition = new Location(persephonePlayer.bukkitPlayer.getWorld(),wrapperPlayClientPlayerBlockPlacement.getBlockPosition().x,wrapperPlayClientPlayerBlockPlacement.getBlockPosition().y,wrapperPlayClientPlayerBlockPlacement.getBlockPosition().z);
            persephonePlayer.placedBlockFace = wrapperPlayClientPlayerBlockPlacement.getFace();
        }
    }
}
