package de.zonlykroks.persephone.check.impl.movement;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.util.Vector;

@CheckData(name = "UltraVelocity", checkType = "A")
public class UltraVelocityA extends Check {

    public UltraVelocityA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            if(player.bukkitPlayer.isGliding()){
                if (player.bukkitPlayer.getVelocity().getX() > 100 || player.bukkitPlayer.getVelocity().getX() < -100 || player.bukkitPlayer.getVelocity().getZ() > 100 || player.bukkitPlayer.getVelocity().getZ() < -100) {
                    player.bukkitPlayer.setVelocity(new Vector(0, 0, 0));
                    flag("Flagged for ultraVelocity"   );
                }else if (player.bukkitPlayer.getVelocity().equals(new Vector(0, 0, 0)) ||player.lastY == player.currentY || player.lastX == player.currentX || player.lastZ == player.currentZ){
                    player.bukkitPlayer.setGliding(false);
                    flag("Flagged for elytra ooga booga");
                }

            }else if (player.bukkitPlayer.getVelocity().getX() > 10 || player.bukkitPlayer.getVelocity().getX() < -10 || player.getBukkitPlayer().getVelocity().getZ() > 10 || player.bukkitPlayer.getVelocity().getZ() < -10) {
                player.bukkitPlayer.setVelocity(new Vector(0,0,0));
                flag("Flagged for ultraVelocity");
            }
        }
    }
}
