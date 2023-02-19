package de.zonlykroks.persephone.check.impl.movement;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;
import de.zonlykroks.persephone.util.PlayerUtils;

@CheckData(name = "Fly", checkType = "A")
public class FlyA extends Check {

    private float flyThreshold = 0;

    public FlyA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(!WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) return;

        if(player.bukkitPlayer.getVehicle() != null || player.bukkitPlayer.getLocation().getBlock().isLiquid() ||  player.bukkitPlayer.getLocation().add(0,-1,0).getBlock().isLiquid() || PlayerUtils.isOnGround(player.bukkitPlayer.getLocation())) return;

        if(player.airTicks > 2 && player.accelY < 0.01) {
            if(flyThreshold++ > 2) {
                this.flag("Player acceleration too small for " + player.airTicks + " ticks, current :" + player.accelY);
            }
        }else flyThreshold -= flyThreshold > 0 ? 0.5f : 0f;
    }
}
