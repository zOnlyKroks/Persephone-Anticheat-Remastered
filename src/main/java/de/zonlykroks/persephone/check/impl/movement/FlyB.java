package de.zonlykroks.persephone.check.impl.movement;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.MathUtil;
import de.zonlykroks.persephone.util.PersephonePlayer;
import de.zonlykroks.persephone.util.PlayerUtils;

@CheckData(name = "Fly", checkType = "B")
public class FlyB extends Check {
    public FlyB(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            if(PlayerUtils.isInLiquid(player.bukkitPlayer) || PlayerUtils.isOnClimbable(player.bukkitPlayer)) return;

            double predict = (player.lastDeltaY - 0.08D) * 0.9800000190734863D;

            if(!player.serverGround && !player.lastServerGround && !player.lastLastServerGround && Math.abs(predict) >= 0.05D && !MathUtil.isRoughlyEqual(player.deltaY,predict)) {
                flag("distY=" + player.deltaY + " predict=" + predict);
            }
        }
    }
}
