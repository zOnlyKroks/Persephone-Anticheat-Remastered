package de.zonlykroks.persephone.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@UtilityClass
public class PlayerUtils {

    public static boolean isOnClimbable(Player player) {
        if(player.getLocation().getBlock().getType() == Material.LADDER || player.getLocation().getBlock().getType() == Material.VINE ){
            return true;
        }
        return false;
    }

    public boolean isOnGround(Location loc) {
        final double limit = 0.3;
        for (double x = -limit; x <= limit; x += limit) {
            for (double z = -limit; z <= limit; z += limit) {
                if (loc.clone().add(x, -0.301, z).getBlock().getType().isSolid()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isHoldingSword(final Player player) {
        return player.getInventory().getItemInMainHand().getType().toString().contains("SWORD") || player.getInventory().getItemInOffHand().getType().toString().contains("SWORD");
    }

}
