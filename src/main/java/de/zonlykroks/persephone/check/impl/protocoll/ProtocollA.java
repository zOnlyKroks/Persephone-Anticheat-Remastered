package de.zonlykroks.persephone.check.impl.protocoll;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;

@CheckData(name = "Protocoll", checkType = "A")
public class ProtocollA extends Check {

    public ProtocollA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            if(player.pitch > 90) {
                this.flag("Player pitch too high, max: 90, current:" + player.pitch);
            }
        }
    }
}
