package de.zonlykroks.persephone.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

    public boolean isInLiquid(final Player player) {
        final double expand = 0.31;
        final Location location = player.getLocation();
        for (double x = -expand; x <= expand; x += expand) {
            for (double z = -expand; z <= expand; z += expand) {
                if (getBlockAsync(location.clone().add(x, -0.5001, z)).isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }

    private Block getBlockAsync(final Location loc) {
        if (loc.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4))
            return loc.getBlock();
        return null;
    }
}
