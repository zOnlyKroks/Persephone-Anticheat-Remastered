package de.zonlykroks.persephone.check.impl.combat.rotation;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;

import java.util.ArrayDeque;
import java.util.Deque;

@CheckData(name = "Rotation",checkType = "B")
public class RotationB extends Check {

    private Deque<Float> yawVelocities = new ArrayDeque<>();
    private Deque<Float> pitchVelocities = new ArrayDeque<>();
    private final int sampleSize = 10;
    private final float maxYawVelocity = 200.0f;
    private final float maxPitchVelocity = 200.0f;
    private WrapperPlayClientPlayerFlying lastPacket;
    private long lastPacketTime;

    public RotationB(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLAYER_ROTATION || event.getPacketType() == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
            WrapperPlayClientPlayerFlying packet = new WrapperPlayClientPlayerFlying(event);
            long currentTime = System.currentTimeMillis();

            if (lastPacket != null) {
                long deltaTime = currentTime - lastPacketTime;
                float deltaYaw = Math.abs(packet.getLocation().getYaw() - lastPacket.getLocation().getYaw());
                float deltaPitch = Math.abs(packet.getLocation().getPitch() - lastPacket.getLocation().getPitch());
                float yawVelocity = deltaYaw / deltaTime;
                float pitchVelocity = deltaPitch / deltaTime;

                // Add the current angular velocities to the deque
                yawVelocities.addLast(yawVelocity);
                pitchVelocities.addLast(pitchVelocity);

                // If the average angular velocity is too high, flag the player
                if (getAverage(yawVelocities) > maxYawVelocity || getAverage(pitchVelocities) > maxPitchVelocity) {
                    flag("Impossibly large rotation velocity, yawVelAvg=" + getAverage(yawVelocities) + ",pitchVelAvg=" + getAverage(pitchVelocities));
                }

                // Check if the change in yaw or pitch is below a certain threshold
                if (deltaYaw < 0.0001f && deltaPitch < 0.0001f) {
                    flag("Impossibly small rotation deltaYaw=" + deltaYaw + ",deltaPitch=" + deltaPitch);
                }
            }

            // Remove old angular velocities from the deque
            while (yawVelocities.size() >= sampleSize) {
                yawVelocities.removeFirst();
                pitchVelocities.removeFirst();
            }

            // Store the current packet for the next iteration
            lastPacket = packet;
            lastPacketTime = currentTime;
        }
    }

    private float getAverage(Deque<Float> values) {
        if (values.isEmpty()) {
            return 0.0f;
        }

        float sum = 0.0f;
        for (float value : values) {
            sum += value;
        }

        return sum / values.size();
    }
}
