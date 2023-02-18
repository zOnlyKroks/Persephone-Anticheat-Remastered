package de.zonlykroks.persephone.check.impl.protocoll;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;

@CheckData(name = "Protocoll", checkType = "D")
public class ProtocollD extends Check {

    public ProtocollD(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(event);

            if(wrapperPlayClientInteractEntity.getEntityId() == player.bukkitPlayer.getEntityId()) {
                this.flag("Player self interact");
            }
        }
    }
}
