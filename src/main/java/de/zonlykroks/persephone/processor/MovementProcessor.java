package de.zonlykroks.persephone.processor;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.util.PersephonePlayer;
import de.zonlykroks.persephone.util.PlayerUtils;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Location;

public class MovementProcessor extends PacketListenerAbstract {

    private final PersephonePlayer persephonePlayer;

    public MovementProcessor(PersephonePlayer persephonePlayer) {
        this.persephonePlayer = persephonePlayer;
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying = new WrapperPlayClientPlayerFlying(event);

            persephonePlayer.clientLastOnGround = persephonePlayer.clientOnGround;
            persephonePlayer.clientOnGround = wrapperPlayClientPlayerFlying.isOnGround();

            persephonePlayer.lastLastServerGround = persephonePlayer.lastServerGround;
            persephonePlayer.lastServerGround = persephonePlayer.serverGround;
            persephonePlayer.serverGround = PlayerUtils.isOnGround(SpigotConversionUtil.toBukkitLocation(persephonePlayer.bukkitPlayer.getWorld(),wrapperPlayClientPlayerFlying.getLocation()));

            persephonePlayer.from = persephonePlayer.to;
            persephonePlayer.to = new Location(persephonePlayer.bukkitPlayer.getWorld(),wrapperPlayClientPlayerFlying.getLocation().getX(),wrapperPlayClientPlayerFlying.getLocation().getY(),wrapperPlayClientPlayerFlying.getLocation().getZ());

            persephonePlayer.lastY = persephonePlayer.currentY;
            persephonePlayer.currentY = wrapperPlayClientPlayerFlying.getLocation().getY();

            persephonePlayer.lastPitch = persephonePlayer.pitch;
            persephonePlayer.pitch = wrapperPlayClientPlayerFlying.getLocation().getPitch();

            persephonePlayer.lastYaw = persephonePlayer.yaw;
            persephonePlayer.yaw = wrapperPlayClientPlayerFlying.getLocation().getYaw();

            persephonePlayer.lastDeltaY = persephonePlayer.deltaY;
            persephonePlayer.deltaY = Math.abs(persephonePlayer.to.getY() - persephonePlayer.from.getY());

            persephonePlayer.accel = Math.abs(persephonePlayer.deltaY - persephonePlayer.lastDeltaY);

            persephonePlayer.lastDeltaPitch = persephonePlayer.deltaPitch;
            persephonePlayer.deltaPitch = Math.abs(persephonePlayer.deltaPitch - persephonePlayer.lastDeltaPitch);

            persephonePlayer.lastDeltaYaw = persephonePlayer.deltaYaw;
            persephonePlayer.deltaYaw = Math.abs(persephonePlayer.deltaYaw - persephonePlayer.lastDeltaYaw);

            persephonePlayer.lastX = persephonePlayer.currentX;
            persephonePlayer.currentX = wrapperPlayClientPlayerFlying.getLocation().getX();

            persephonePlayer.lastY = persephonePlayer.currentY;
            persephonePlayer.currentY = wrapperPlayClientPlayerFlying.getLocation().getY();

            persephonePlayer.getActionProcessor().handleFlying();

            if(PlayerUtils.isOnGround(persephonePlayer.bukkitPlayer.getLocation())) {
                if(persephonePlayer.airTicks > 0) {
                    persephonePlayer.airTicks--;
                }
            }else {
                persephonePlayer.airTicks++;
            }
        }
    }
}
