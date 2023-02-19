package de.zonlykroks.persephone.check.impl.protocoll;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerAbilities;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;

@CheckData(name = "Protocoll", checkType = "C")
public class ProtocollC extends Check {

    public ProtocollC(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {

        if(player.isPlayerExempt()) return;

        if(event.getPacketType() == PacketType.Play.Client.PLAYER_ABILITIES) {
            WrapperPlayClientPlayerAbilities wrapperPlayClientPlayerAbilities = new WrapperPlayClientPlayerAbilities(event);

            boolean allowFlight = player.bukkitPlayer.getAllowFlight();

            if(allowFlight) return;

            boolean flightClientAllowedPresent = wrapperPlayClientPlayerAbilities.isFlightAllowed().isPresent();

            if(flightClientAllowedPresent) {
                if(wrapperPlayClientPlayerAbilities.isFlightAllowed().get()) {
                    this.flag("Player changed abilities");
                    player.bukkitPlayer.kickPlayer("");
                }
            }
        }
    }
}
