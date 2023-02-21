package de.zonlykroks.persephone.check.impl.misc;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.EvictingList;
import de.zonlykroks.persephone.util.MathUtil;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Collection;

@CheckData(name = "Timer", checkType = "A")
public class TimerA extends Check {

    private final EvictingList<Long> samples = new EvictingList<>(50);
    private long lastFlyingTime;

    public TimerA(PersephonePlayer player) {
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

                double flag = 1.01;

                if(player.bukkitPlayer.getInventory().getItem(EquipmentSlot.CHEST) != null) {
                    if (player.bukkitPlayer.getInventory().getItem(EquipmentSlot.CHEST).getType() == Material.ELYTRA && !player.serverGround && !player.lastServerGround && !player.lastLastServerGround) {
                        flag = 1.1;
                    }
                }

                if (speed >= flag) {
                    if (++buffer > 30) {
                        buffer = Math.min(buffer, 50);
                        this.flag(String.format("speed=%.4f, delta=%o, buffer=%.2f", speed, delta, buffer));
                    }
                } else {
                    buffer -= buffer > 0 ? 1 : 0;
                }
            }

            lastFlyingTime = now;
        } else if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            samples.add(135L); //Magic value. 100L doesn't completely fix it for some reason.
        }
    }

    public double getAverage(final Collection<? extends Number> data) {
        return data.stream().mapToDouble(Number::doubleValue).average().orElse(0D);
    }
}
