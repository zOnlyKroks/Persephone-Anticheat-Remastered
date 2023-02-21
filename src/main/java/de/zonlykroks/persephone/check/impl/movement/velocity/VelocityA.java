package de.zonlykroks.persephone.check.impl.movement.velocity;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;
import de.zonlykroks.persephone.util.PlayerUtils;

@CheckData(name = "Velocity", checkType = "A")
public class VelocityA extends Check {

    private double vY;

    public VelocityA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(!WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) return;

        if(player.getBukkitPlayer().getVelocity().getY() > 0.1)
            vY = player.getBukkitPlayer().getVelocity().getY();

        if(vY > 0 && !PlayerUtils.isOnClimbable(player.bukkitPlayer) && !PlayerUtils.isInLiquid(player.bukkitPlayer) && player.bukkitPlayer.getLocation().add(0,1,0).getBlock().isPassable()) {
            double pct = player.deltaY / vY * 100;

            if ((pct < 99.999 || pct > 400)) {
                if(++buffer > 15) {
                    flag("pct=" + pct + "buffer=" + buffer);
                }
            } else buffer-= buffer > 0 ? 0.5 : 0;

            vY-= 0.08;
            vY*= 0.98;

            if(vY < 0.005)
                    vY = 0;
        }
    }
}
