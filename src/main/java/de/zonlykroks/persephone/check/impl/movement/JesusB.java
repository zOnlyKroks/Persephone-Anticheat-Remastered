package de.zonlykroks.persephone.check.impl.movement;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.MathUtil;
import de.zonlykroks.persephone.util.PersephonePlayer;
import de.zonlykroks.persephone.util.PlayerUtils;

@CheckData(name = "Jesus", checkType = "B")
public class JesusB extends Check {
    public JesusB(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        // Get the player's on ground and make sure he is stationary
        final boolean onGround = player.isServerGround();
        final boolean touchingLiquid = PlayerUtils.isInLiquid(player.bukkitPlayer);
        final boolean stationary = player.getDeltaX() % 1.0 == 0.0 && player.getDeltaZ() % 1.0 == 0.0;

        // If the delta is greater than 0.0 and the player is stationary
        if (player.getDeltaY() > 0.0 && !onGround && !touchingLiquid && stationary) {
            final double horizontalDistance = MathUtil.magnitude(player.getDeltaX(), player.getDeltaZ());

            // If the player is moving too, flag
            if (horizontalDistance > 0.1) {
                flag("hD=" + horizontalDistance + " >0.1");
            }
        }
    }
}
