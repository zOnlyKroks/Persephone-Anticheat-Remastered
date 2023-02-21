package de.zonlykroks.persephone.check.impl.combat;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import de.zonlykroks.persephone.Persephone;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.util.RayTraceResult;

@CheckData(name = "Killaura", checkType = "D",setbackVl = 0)
public class KillauraD extends Check {

    public KillauraD(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(event.getPacketType() != PacketType.Play.Client.INTERACT_ENTITY) return;

        WrapperPlayClientInteractEntity wrapperPlayClientInteractEntity = new WrapperPlayClientInteractEntity(event);

        if(wrapperPlayClientInteractEntity.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) return;

        Bukkit.getScheduler().callSyncMethod(Persephone.persephone, () -> {
            RayTraceResult rayTraceResult = player.bukkitPlayer.getWorld().rayTrace(player.bukkitPlayer.getEyeLocation(),player.bukkitPlayer.getLocation().getDirection(),5, FluidCollisionMode.NEVER,false,0.6,entity -> entity != player.bukkitPlayer);

            if(rayTraceResult == null) {
                this.flag(" raytrace is null, still hit entity");
            }else if(rayTraceResult.getHitEntity() == null) {
                this.flag(" raytrace not null, still hit entity tho he is out of sight");
            }
            return true;
        });
    }
}
