package de.zonlykroks.persephone.check.impl.misc;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.EvictingList;
import de.zonlykroks.persephone.util.PersephonePlayer;

import java.util.Collection;

@CheckData(name = "Check", checkType = "B")
public class TimerB extends Check {

    private final EvictingList<Long> samples = new EvictingList<>(50);
    private long lastFlyingTime;

    public TimerB(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            final long now = System.currentTimeMillis();
            final long delta = now - lastFlyingTime;

            if (delta > 1) {
                samples.add(delta);
            }

            if (samples.isFull()) {
                final double average = getAverage(samples);
                final double speed = 50 / average;

                if (speed >= 1.01) {
                    if (++buffer > 30) {
                        buffer = Math.min(buffer, 50);
                        this.flag(String.format("speed=%.4f, delta=%o, buffer=%.2f", speed, delta, buffer));
                    }
                } else {
                    buffer = Math.max(0, buffer - 1);
                }
            }

            lastFlyingTime = now;
        } else if (event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION || event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            samples.add(135L); //Magic value. 100L doesn't completely fix it for some reason.
        }
    }

    public static double getAverage(final Collection<? extends Number> data) {
        return data.stream().mapToDouble(Number::doubleValue).average().orElse(0D);
    }
}
