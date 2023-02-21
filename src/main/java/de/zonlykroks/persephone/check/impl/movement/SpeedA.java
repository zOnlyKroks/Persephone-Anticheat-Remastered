package de.zonlykroks.persephone.check.impl.movement;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;

@CheckData(name = "Speed", checkType = "A")
public class SpeedA extends Check {

    public boolean onIce;
    public SpeedA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(!WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) return;

        double xz_speed = Math.max(player.getDeltaX(), player.getDeltaZ());

        String name = player.bukkitPlayer.getLocation().add(0,-1,0).getBlock().getType().name();

        onIce = name.contains("ICE");

        if(name.contains("AIR"))
            onIce = true;

        double modifiedNew = onIce ? 0.455D : 0.38D;

        if(xz_speed > modifiedNew) {
           if(++buffer > 4) {
               this.flag("tried to move faster than normal, speed=(" + xz_speed + "), max=" + modifiedNew);
           }
        }else buffer -= buffer > 0 ? .5F : 0;
    }
}
