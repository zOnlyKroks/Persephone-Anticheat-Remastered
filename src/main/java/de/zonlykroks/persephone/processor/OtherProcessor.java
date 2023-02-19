package de.zonlykroks.persephone.processor;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCloseWindow;
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
        if(event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            WrapperPlayClientCloseWindow wrapperPlayClientCloseWindow = new WrapperPlayClientCloseWindow(event);

            persephonePlayer.isWindowOpen = false;
        }
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if(event.getPacketType() == PacketType.Play.Server.OPEN_BOOK) {
            WrapperPlayServerOpenWindow wrapperPlayServerOpenWindow = new WrapperPlayServerOpenWindow(event);

            persephonePlayer.isWindowOpen = true;
        }
    }
}
