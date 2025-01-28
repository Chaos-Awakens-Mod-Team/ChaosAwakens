package io.github.chaosawakens.common.block.base.general;

import io.github.chaosawakens.common.block.base.general.config.PlantVegetationConfig;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CustomizableMultiLayerPlantBlock extends MultiLayerPlantBlock {
    private final Supplier<PlantVegetationConfig> plantCnfg;

    public CustomizableMultiLayerPlantBlock(Properties properties, int maxLevel, IntOpenHashSet modularLevels, Supplier<PlantVegetationConfig> plantCnfg) {
        super(properties, maxLevel, modularLevels);
        this.plantCnfg = plantCnfg;
    }

    public CustomizableMultiLayerPlantBlock(Properties properties, int maxLevel, Supplier<PlantVegetationConfig> plantCnfg) {
        super(properties, maxLevel);
        this.plantCnfg = plantCnfg;
    }

    public CustomizableMultiLayerPlantBlock(Properties properties, Supplier<PlantVegetationConfig> plantCnfg) {
        super(properties);
        this.plantCnfg = plantCnfg;
    }

    @Override
    protected boolean mayPlaceOn(BlockState targetState, BlockGetter curLevel, BlockPos targetPos) {
        return plantCnfg != null && plantCnfg.get() != null && plantCnfg.get().allowPlacementOn(curLevel, targetPos, targetState);
    }
}
