package de.zonlykroks.persephone.check.impl.combat.rotation;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;

@CheckData(name = "Rotation", checkType = "A")
public class RotationA extends Check {

    private long lastRotationTime;
    private float lastYaw;
    private float lastPitch;
    public RotationA(PersephonePlayer player) {
        super(player);
        this.lastRotationTime = 0;
        this.lastYaw = player.yaw;
        this.lastPitch = player.pitch;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION || event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            WrapperPlayClientPlayerFlying packet = new WrapperPlayClientPlayerFlying(event);

            // Calculate the time since the last rotation packet
            long currentTime = System.currentTimeMillis();
            long timeSinceLastRotation = currentTime - lastRotationTime;
            lastRotationTime = currentTime;

            // Calculate the difference between the current and last yaw/pitch
            float deltaYaw = Math.abs(packet.getLocation().getYaw() - lastYaw);
            float deltaPitch = Math.abs(packet.getLocation().getPitch() - lastPitch);

            // If the time since the last rotation is too short and the yaw/pitch changes are too small, flag the player
            if (timeSinceLastRotation < 50 && deltaYaw < 0.1 && deltaPitch < 0.1) {
                flag("Inhumanly quick rotation: deltaYaw=" + deltaYaw);
            }

            // If the yaw/pitch changes are too perfect, flag the player
            if ((deltaYaw == 0.0 || deltaYaw == 360.0) && deltaPitch == 0.0) {
                flag("Suspiciously perfect rotation (Rotation)");
            }

            // Update the last yaw/pitch values
            lastYaw = packet.getLocation().getPitch();
            lastPitch = packet.getLocation().getYaw();
        }
    }
}
