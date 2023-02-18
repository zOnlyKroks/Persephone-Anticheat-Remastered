package de.zonlykroks.persephone.check.impl.protocoll;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;

@CheckData(name = "Protocoll", checkType = "E")
public class ProtocollE extends Check {

    private int lastSlot = -1;

    public ProtocollE(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
            WrapperPlayClientHeldItemChange wrapperPlayClientHeldItemChange = new WrapperPlayClientHeldItemChange(event);

            if(wrapperPlayClientHeldItemChange.getSlot() == lastSlot) {
                this.flag("slot=" + wrapperPlayClientHeldItemChange.getSlot() + " lastslot=" + lastSlot);
            }

            lastSlot = wrapperPlayClientHeldItemChange.getSlot();
        }
    }
}
