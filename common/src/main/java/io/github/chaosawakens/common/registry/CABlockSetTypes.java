package io.github.chaosawakens.common.registry;

import net.minecraft.world.level.block.state.properties.BlockSetType;

public final class CABlockSetTypes {

    public static final BlockSetType APPLE = registerBlockSetType(new BlockSetType("apple"));

    public static final BlockSetType CRYSTALWOOD = registerBlockSetType(new BlockSetType("crystalwood"));
    public static final BlockSetType DENSEWOOD = registerBlockSetType(new BlockSetType("densewood"));

    public static final BlockSetType DUPLICATOR = registerBlockSetType(new BlockSetType("duplicator"));

    public static final BlockSetType GINKGO = registerBlockSetType(new BlockSetType("ginkgo"));
    public static final BlockSetType MESOZOIC = registerBlockSetType(new BlockSetType("mesozoic"));

    public static final BlockSetType MINING = registerBlockSetType(new BlockSetType("mining"));

    public static final BlockSetType PEACH = registerBlockSetType(new BlockSetType("peach"));
    public static final BlockSetType SKYWOOD = registerBlockSetType(new BlockSetType("skywood"));

    private static BlockSetType registerBlockSetType(BlockSetType blockSetType) {
        return BlockSetType.register(blockSetType);
    }
}
