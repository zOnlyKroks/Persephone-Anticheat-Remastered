package de.zonlykroks.persephone.check.impl.protocoll;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;
import de.zonlykroks.persephone.util.PlayerUtils;

@CheckData(name = "Protocoll", checkType = "B")
public class ProtocollG extends Check {

    public ProtocollG(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(event);

            if(wrapperPlayClientInteractEntity.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) return;

            final boolean placing = player.placing;

            final boolean sword = PlayerUtils.isHoldingSword(player.bukkitPlayer);

            if(sword && placing) {
                if(buffer++ > 2) {
                    this.flag("Player placing blocks and having sword in hand");
                }
            }else {
                buffer-= 0.25;
            }
        }
    }
}
