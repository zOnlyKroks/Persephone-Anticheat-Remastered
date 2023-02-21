package de.zonlykroks.persephone.check.impl.movement;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;
import de.zonlykroks.persephone.util.PlayerUtils;

@CheckData(name = "NoFall", checkType = "A",damage = true)
public class NoFallA extends Check {

    public NoFallA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            final boolean exempt = player.bukkitPlayer.isInsideVehicle() || PlayerUtils.isOnClimbable(player.bukkitPlayer) || player.serverGround || player.lastServerGround || player.lastLastServerGround;

            if(player.clientOnGround != player.serverGround && player.clientLastOnGround != player.lastServerGround && !exempt) {
                if(++buffer > 4) {
                    this.flag("aT=" + player.airTicks);
                }
            }else if(buffer > 0) buffer -= 0.25D;
        }
    }
}
