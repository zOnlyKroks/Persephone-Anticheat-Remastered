package de.zonlykroks.persephone.check.impl.movement;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;
import de.zonlykroks.persephone.util.PlayerUtils;

@CheckData(name = "Speed", checkType = "A")
public class SpeedA extends Check {

    private double lastDist;
    private boolean lastOnGround;

    public static final double AIR_FRICTION = 0.91f;

    public SpeedA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            double deltaX  = player.deltaX;
            double deltaZ = player.deltaZ;

            double dist = (deltaX * deltaX) + (deltaZ * deltaZ);
            double lastDist = this.lastDist;
            this.lastDist = dist;

            boolean onGround = PlayerUtils.isOnGround(player.bukkitPlayer.getLocation());
            boolean lastOnGround = this.lastOnGround;
            this.lastOnGround = onGround;

            double shiftedLastDist = lastDist * AIR_FRICTION;
            double equallnes = dist - shiftedLastDist;
            double scaledEquallnes =  equallnes * 137; //Magic Number i tested

            if(!onGround && !lastOnGround && scaledEquallnes >= 1) {
                this.flag("velocity=" + scaledEquallnes + "! max: 1 (scaled)");
            }
        }
    }
}
