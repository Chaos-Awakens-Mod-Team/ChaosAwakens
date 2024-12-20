package io.github.chaosawakens.common.registry;

import net.minecraft.world.level.block.state.properties.WoodType;

public final class CAWoodTypes {

    public static final WoodType APPLE = registerWoodType(new WoodType("apple", CABlockSetTypes.APPLE));
    public static final WoodType CRYSTALWOOD = registerWoodType(new WoodType("crystalwood", CABlockSetTypes.CRYSTALWOOD));
    public static final WoodType DENSEWOOD = registerWoodType(new WoodType("densewood", CABlockSetTypes.DENSEWOOD));
    public static final WoodType DUPLICATOR = registerWoodType(new WoodType("duplicator", CABlockSetTypes.DUPLICATOR));
    public static final WoodType GINKGO = registerWoodType(new WoodType("ginkgo", CABlockSetTypes.GINKGO));
    public static final WoodType MESOZOIC = registerWoodType(new WoodType("mesozoic", CABlockSetTypes.MESOZOIC));
    public static final WoodType MINING = registerWoodType(new WoodType("mining", CABlockSetTypes.MINING));
    public static final WoodType PEACH = registerWoodType(new WoodType("peach", CABlockSetTypes.PEACH));
    public static final WoodType SKYWOOD = registerWoodType(new WoodType("skywood", CABlockSetTypes.SKYWOOD));

    private static WoodType registerWoodType(WoodType woodType) {
        return WoodType.register(woodType);
    }
}
