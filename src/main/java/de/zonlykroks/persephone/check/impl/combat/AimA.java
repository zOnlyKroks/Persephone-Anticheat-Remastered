package de.zonlykroks.persephone.check.impl.combat;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;

@CheckData(name = "Aim", checkType = "A", setback = false)
public class AimA extends Check {

    public AimA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType()) && player.lastHitTicks < 5) {
            float deltaYaw = player.deltaYaw;
            float deltaPitch = player.deltaPitch;

            /*
             * The logic behind this is that a player's rotations should be somewhat linear. You can't move your
             * mouse an exponentially small amount on one axis and a large amount on another. Some aimbots, killauras,
             * and even aim assists fail this. We really don't even need to account for Cinematic or Optifine here
             * since with those, both of the rotations will be exponentially small, not just one. I've seen some
             * Kill Auras that go as far as deltaPitch 1e-20 or something ridiculous, so this can flag those very fast.
             */
            final boolean invalid = deltaYaw > .5F && deltaPitch < .0001 && deltaPitch > 0;

            if(invalid) {
                if(buffer++ > 4) {
                    this.flag("deltaYaw=" + deltaYaw + " deltaPitch=" + deltaPitch);
                }
            }else {
                buffer-= .25F;
            }
        }
    }
}
