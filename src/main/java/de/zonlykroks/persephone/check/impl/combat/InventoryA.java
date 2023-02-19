package de.zonlykroks.persephone.check.impl.combat;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.util.PersephonePlayer;

public class InventoryA extends Check {

    private long lastFlying = System.currentTimeMillis();
    public InventoryA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            if(player.isWindowOpen) {
                final long now = System.currentTimeMillis();

                final boolean lagging = now - lastFlying > 60L;
                final boolean attacking = player.isAttacking;
                final boolean swinging = player.isSwinging;

                if (!lagging && (attacking || swinging)) {
                    flag("invalidInvAction");
                } else {
                    buffer = 0;
                }
            }
        }else if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            lastFlying = System.currentTimeMillis();
        }
    }
}
