package de.zonlykroks.persephone.check.impl.protocoll;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;

@CheckData(name = "Procoll", checkType = "B")
public class ProtocollB extends Check {

    private int ticks;

    public ProtocollB(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {

        if(player.isPlayerExempt()) return;

        if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            WrapperPlayClientPlayerFlying wrapperPlayClientPlayerFlying = new WrapperPlayClientPlayerFlying(event);

            if(wrapperPlayClientPlayerFlying.hasPositionChanged() || player.bukkitPlayer.isInsideVehicle()) {
                ticks = 0;
                return;
            }

            if(ticks++ > 20) {
                this.flag("Did not send movement packet every 20 ticks, instead only every: " + ticks);
            }
        }else if(event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {
            ticks = 0;
        }
    }
}
