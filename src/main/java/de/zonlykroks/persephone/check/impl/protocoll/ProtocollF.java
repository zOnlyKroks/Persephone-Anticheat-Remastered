package de.zonlykroks.persephone.check.impl.protocoll;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientSteerVehicle;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;

@CheckData(name = "Protocoll", checkType = "F")
public class ProtocollF extends Check {

    public ProtocollF(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {
            WrapperPlayClientSteerVehicle wrapperPlayClientSteerVehicle = new WrapperPlayClientSteerVehicle(event);

            float forward = wrapperPlayClientSteerVehicle.getForward();
            float side = wrapperPlayClientSteerVehicle.getSideways();

            boolean invalid = Math.abs(forward) > .98F || Math.abs(side) > .98F;

            if(invalid)
                this.flag("forward=" + forward + " side=" + side);
        }
    }
}
