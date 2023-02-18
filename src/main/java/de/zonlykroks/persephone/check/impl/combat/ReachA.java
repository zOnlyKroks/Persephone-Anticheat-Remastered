package de.zonlykroks.persephone.check.impl.combat;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.Material;

@CheckData(name = "Reach", checkType = "A")
public class ReachA extends Check {

    private int reachThreshold = 0;

    private final float baseReach = 3.4F;

    public ReachA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;

        if(player.attackedEntity == null || player.lastAttackedEntity == null) return;

        double dist = player.attackedEntity.getLocation().distance(player.bukkitPlayer.getLocation());

        if(dist > baseReach) {
            if(reachThreshold++ > 10) {
                this.flag("Player Reach to high, allowed: " + baseReach + " , current:" + dist);
            }
        }else reachThreshold -= reachThreshold > 0 ? 1 : 0;
    }
}
