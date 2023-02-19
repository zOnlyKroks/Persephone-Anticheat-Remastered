package de.zonlykroks.persephone.check.impl.combat;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

@CheckData(name = "Killaura", checkType = "B",setback = false)
public class KillauraB extends Check {

    public KillauraB(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(player.attackedEntity == null) return;

        if(event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(event);

            if(wrapperPlayClientInteractEntity.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                if(entityBehindPlayer(player.attackedEntity)) {
                    this.flag("entity=" + player.attackedEntity.getName() + " , behind player");
                }
            }
        }
    }

    public boolean entityBehindPlayer(Entity entity) {
        double yaw = 2*Math.PI-Math.PI*player.bukkitPlayer.getLocation().getYaw()/180;
        Vector v = entity.getLocation().toVector().subtract(player.bukkitPlayer.getLocation().toVector());
        Vector r = new Vector(Math.sin(yaw),0, Math.cos(yaw));
        float theta = r.angle(v);
        return Math.PI / 2 < theta && theta < 3 * Math.PI / 2;
    }
}
