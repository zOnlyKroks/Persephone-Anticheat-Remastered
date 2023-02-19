package de.zonlykroks.persephone.check.impl.combat;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.MathUtil;
import de.zonlykroks.persephone.util.PersephonePlayer;
import org.bukkit.entity.Player;

@CheckData(name = "Killaura", checkType = "C")
public class KillauraC extends Check {

    public KillauraC(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(player.isPlayerExempt()) return;

        if(event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            Player p = player.bukkitPlayer;

            double oy = player.lastYaw;
            double op = player.lastPitch;
            double cy = player.yaw;
            double cp = player.pitch;
            boolean f_yaw = Math.abs(MathUtil.trim(1, cy - oy)) > 35;
            boolean f_pitch = Math.abs(MathUtil.trim(1, cp - op)) > 35;

            if (f_yaw || f_pitch) {
                flag(" moved his head to quickly");
            }

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
