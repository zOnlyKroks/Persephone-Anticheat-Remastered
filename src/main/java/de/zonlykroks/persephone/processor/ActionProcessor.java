package de.zonlykroks.persephone.processor;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import de.zonlykroks.persephone.util.PersephonePlayer;

public class ActionProcessor extends PacketListenerAbstract {

    private final PersephonePlayer persephonePlayer;

    public ActionProcessor(PersephonePlayer persephonePlayer) {
        this.persephonePlayer = persephonePlayer;
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
            WrapperPlayClientEntityAction wrapperPlayClientEntityAction = new WrapperPlayClientEntityAction(event);
            persephonePlayer.sendingAction = true;

            switch (wrapperPlayClientEntityAction.getAction()) {
                case START_SPRINTING:
                    persephonePlayer.sprinting = true;
                    break;
                case STOP_SPRINTING:
                    persephonePlayer.sprinting = false;
                    break;
                case START_SNEAKING:
                    persephonePlayer.sneaking = true;
                    break;
                case STOP_SNEAKING:
                    persephonePlayer.sneaking = false;
                    break;
            }
        }
    }

    public void handleFlying() {
        persephonePlayer.sendingAction = false;
        persephonePlayer.placing = false;
    }

    public void handleBlockPlace() {
        persephonePlayer.placing = true;
    }
}
