package de.zonlykroks.persephone.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Friction data library to prevent using Reflection or NMS
 */
public enum Friction {

    DEFAULT(0.6f),
    ICE(0.98f),
    BLUE_ICE(0.989f),
    PACKED_ICE(0.98f),
    SLIME_BLOCK(0.8f),
    ;

    private final float factor;

    Friction(final float factor) {
        this.factor = factor;
    }

    /**
     * Gets the friction factor
     *
     * @return the factor
     */
    public float getFactor() {
        return this.factor;
    }

    /**
     * Gets the friction factor for the given block
     *
     * @param block The block
     * @return the friction factor
     */
    public static float getFactor(final Block block) {
        final Material type = block.getType();
        try {
            return valueOf(type.name()).getFactor();
        } catch (final Exception exception) {
            return DEFAULT.getFactor();
        }
    }

}
