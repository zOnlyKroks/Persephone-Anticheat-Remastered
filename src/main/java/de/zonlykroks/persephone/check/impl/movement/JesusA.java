package de.zonlykroks.persephone.check.impl.movement;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;
import de.zonlykroks.persephone.util.PlayerUtils;

@CheckData(name = "Jesus",checkType = "A")
public class JesusA extends Check {
    public JesusA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(PlayerUtils.isOnClimbable(player.bukkitPlayer) || player.isPlayerExempt()) return;

        if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            if(player.to.clone().add(0,-1,0).getBlock().isLiquid() &&
                    player.from.clone().add(0,-1,0).getBlock().isLiquid() &&
                    !PlayerUtils.isOnGround(player.from) &&
                    !PlayerUtils.isOnGround(player.to) &&
                    !player.to.add(0,1,0).getBlock().getType().isSolid()) {
                if (Double.toString(player.deltaY).contains("00000000") || player.deltaY < 0.001) {
                    this.flag("Player walking on water, deltaY too small : " + player.deltaY);
                }
            }
        }
    }
}