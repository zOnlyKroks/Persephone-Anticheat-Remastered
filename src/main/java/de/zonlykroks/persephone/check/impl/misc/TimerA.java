package de.zonlykroks.persephone.check.impl.misc;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;

@CheckData(name = "Check", checkType = "A")
public class TimerA extends Check {

    private long lastFlying;

    public TimerA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        final boolean flying = WrapperPlayClientPlayerFlying.isFlying(event.getPacketType());
        if (flying) {
            long now = System.currentTimeMillis();
            if (lastFlying + 47L > now) {
                double timerSpeed = 50.0 / (now - lastFlying);
                if (++buffer > 7) {
                    this.flag("timerSpeed:" + timerSpeed);
                }
            } else if (lastFlying + 70L < now) {
                buffer -= 3;
            } else if (buffer > 0) {
                buffer--;
            }
            lastFlying = now;
        }else if(event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION || event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            buffer = -10;
        }
    }
}
