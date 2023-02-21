package de.zonlykroks.persephone.util;

import com.github.retrooper.packetevents.protocol.world.BlockFace;

public class LocationUtils {

    public static org.bukkit.block.BlockFace convertBlockFace(BlockFace face) {
        return switch (face) {
            case DOWN -> org.bukkit.block.BlockFace.DOWN;
            case UP -> org.bukkit.block.BlockFace.UP;
            case EAST -> org.bukkit.block.BlockFace.EAST;
            case WEST -> org.bukkit.block.BlockFace.WEST;
            case NORTH -> org.bukkit.block.BlockFace.NORTH;
            case SOUTH -> org.bukkit.block.BlockFace.SOUTH;
            case OTHER -> null;
        };
    }

}
