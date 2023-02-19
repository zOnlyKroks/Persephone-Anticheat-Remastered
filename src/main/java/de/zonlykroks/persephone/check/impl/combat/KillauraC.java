package de.zonlykroks.persephone.check.impl.combat;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.MathUtil;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.entity.Player;

@CheckData(name = "Killaura", checkType = "C",setback = false)
public class KillauraC extends Check {

    public KillauraC(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(event);

            if(wrapperPlayClientInteractEntity.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                Player p = player.bukkitPlayer;

                if (p.isBlocking()) {
                    flag(" is blocking + attacking in the same moment");
                }

                if (p.isSleeping()) {
                    flag(" is sleeping while attacking");
                }

                if (p.isDead()) {
                    flag(" is dead while hitting");
                }
            }
        }
    }
}
