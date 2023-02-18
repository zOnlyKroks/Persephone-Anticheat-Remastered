package de.zonlykroks.persephone.check.impl.protocoll;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;

@CheckData(checkType = "Protocoll", name = "H")
public class ProtocollH extends Check {

    public ProtocollH(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {

            /*
             * Get the placing variable from the Action Processor. This variable gets reset to false every flying packet
             * sent by the client, so we can check if they are sending a Held Item Slot packet while this variable
             * while this is true, which wouldn't be possible to a Vanilla client.
             */
            final boolean placing = player.isPlacing();

            if(!placing)
                this.flag("Held item change without flying packet resetting");
        }
    }
}
