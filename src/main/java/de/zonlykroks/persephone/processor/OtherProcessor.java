package de.zonlykroks.persephone.processor;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import de.zonlykroks.persephone.util.PersephonePlayer;

public class OtherProcessor extends PacketListenerAbstract {

    private final PersephonePlayer persephonePlayer;

    public OtherProcessor(PersephonePlayer persephonePlayer) {
        this.persephonePlayer = persephonePlayer;
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(persephonePlayer.bukkitPlayer.getEntityId() == event.getUser().getEntityId()) {
            if(event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
                WrapperPlayClientCloseWindow wrapperPlayClientCloseWindow = new WrapperPlayClientCloseWindow(event);

                persephonePlayer.isWindowOpen = false;
            }

            if(event.getPacketType() == PacketType.Play.Client.USE_ITEM) {
                WrapperPlayClientUseItem wrapperPlayClientUseItem = new WrapperPlayClientUseItem(event);

                persephonePlayer.useItemHand = wrapperPlayClientUseItem.getHand();
            }
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if(persephonePlayer.bukkitPlayer.getEntityId() == event.getUser().getEntityId()) {
            if(event.getPacketType() == PacketType.Play.Server.OPEN_BOOK) {
                WrapperPlayServerOpenWindow wrapperPlayServerOpenWindow = new WrapperPlayServerOpenWindow(event);

                persephonePlayer.isWindowOpen = true;
            }
        }
    }
}
