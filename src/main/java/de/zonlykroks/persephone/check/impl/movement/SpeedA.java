package de.zonlykroks.persephone.check.impl.movement;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import de.zonlykroks.persephone.check.Check;
import de.zonlykroks.persephone.check.CheckData;
import de.zonlykroks.persephone.util.MathUtil;
import de.zonlykroks.persephone.util.PersephonePlayer;
import de.zonlykroks.persephone.util.PlayerUtils;
import de.zonlykroks.persephone.util.YMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

@CheckData(name = "Speed", checkType = "A")
public class SpeedA extends Check {

    public boolean wasGoingUp;
    public int oldYModifier;
    public int ticksUp,oldTicksUp;

    public SpeedA(PersephonePlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if(!WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) return;

        if (player.to.getY() > player.from.getY() || player.deltaY == 0) {
            wasGoingUp = false;
            oldYModifier = 0;
            ticksUp = 0;
            return;
        }
        wasGoingUp = true;
        ticksUp++;
        oldTicksUp = ticksUp;

        final double speed = MathUtil.round(player.deltaY,6);

        int id = getYModifier(player.bukkitPlayer);
        if (id > oldYModifier)
            oldYModifier = id;
        id = oldYModifier;
        YMap map = YMap.get(id);

        if (player.from.getY() < player.to.getY() && (player.to.getX() != player.from.getX() || player.from.getZ() != player.to.getZ())) {
            boolean step = PlayerUtils.isStepping(player.getFrom()) || PlayerUtils.isStepping(player.getTo());
            boolean yMap = map != null && map.contains(speed);
            if (step) {
                if (speed > .5)
                    this.flag("speed > .5");
                return;
            }
        }

        if (map == null) {
            Bukkit.getLogger().warning("Modifier '" + id + "' has no contents!");
            return;
        }

        if (!map.hasSpeed(ticksUp)) {
            this.flag("reason: long, s: " + ticksUp + ", m: " + map.size());
            return;
        }

        if (map.size() <= ticksUp)
            if (!(id != 0 && (player.to.getX() != player.from.getX() || player.from.getZ() != player.to.getZ()) && map.size() == ticksUp && speed == map.getSpeed(ticksUp))) {
                this.flag("reason: too high (ticksUp: " + ticksUp + ", max: " + (map.size() - 1));
                return;
            }
        if (map.size() < ticksUp)
            return;
        double expected = map.getSpeed(ticksUp);

        if (expected != speed) {
            //debug(ticksUp);
            this.flag("reason: normal, type: " + (expected < speed ? "high" : "low") + " (speed: " + speed + ", expected: " + expected);
        }
    }

    public int getYModifier(Player user) {
        if (user.hasPotionEffect(PotionEffectType.JUMP))
            for (PotionEffect pe : user.getPlayer().getActivePotionEffects())
                if (pe.getType().equals(PotionEffectType.JUMP))
                    return pe.getAmplifier() + 1;
        return 0;
    }
}
