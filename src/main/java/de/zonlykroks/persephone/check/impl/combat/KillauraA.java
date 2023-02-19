package de.zonlykroks.persephone.check.impl.combat;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;

@CheckData(name = "KillAura", checkType = "A",setback = false)
public class KillauraA extends Check {

    private int ticks, lastEntityId;

    public KillauraA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(player.attackedEntity == null) return;

        if(event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(event);

            if(wrapperPlayClientInteractEntity.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) return;

            if (wrapperPlayClientInteractEntity.getEntityId() != lastEntityId) {
                if(++ticks > 1) {
                    this.flag("Multi Aura Failed, attacked multiple entities in one tick");
                }
            }

            lastEntityId = wrapperPlayClientInteractEntity.getEntityId();
        }else if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            ticks = 0;
        }
    }
}
