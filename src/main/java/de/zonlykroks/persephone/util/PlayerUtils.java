package de.zonlykroks.persephone.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@UtilityClass
public class PlayerUtils {

    public static final double PLAYER_WIDTH = .6;
    private static final MaterialCheck CHECK_STAIR, CHECK_STEP;

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
                if (location.clone().add(x, -0.5001, z).getBlock().isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isStepping(Location location) {
        return isColliding(location, CHECK_STAIR) || isColliding(location, CHECK_STEP);
    }

    public static boolean isColliding(Location location, MaterialCheck material) {
        double d = PLAYER_WIDTH/2;
        return material.checkMaterial(location)
                || material.checkMaterial(location.clone().add( d, 0, 0))
                || material.checkMaterial(location.clone().add(-d, 0, 0))
                || material.checkMaterial(location.clone().add( d, 0,  d))
                || material.checkMaterial(location.clone().add(-d, 0,  d))
                || material.checkMaterial(location.clone().add( d, 0, -d))
                || material.checkMaterial(location.clone().add(-d, 0, -d))
                || material.checkMaterial(location.clone().add(0,  0, -d))
                || material.checkMaterial(location.clone().add(0,  0,  d));
    }

    static {
        CHECK_STAIR = new MaterialCheck() {

            @Override
            public boolean checkMaterial(Material material) {
                return switch (material) {
                    case ACACIA_STAIRS, BRICK_STAIRS, COBBLESTONE_STAIRS, DARK_OAK_STAIRS, NETHER_BRICK_STAIRS, QUARTZ_STAIRS, RED_SANDSTONE_STAIRS, SANDSTONE_STAIRS ->
                            true;
                    default -> false;
                };
            }
        };
        CHECK_STEP = new MaterialCheck() {

            @Override
            public boolean checkMaterial(Material material) {
                return switch (material) {
                    case LEGACY_STEP, LEGACY_WOOD_STEP -> true;
                    default -> false;
                };
            }
        };
    }
}
