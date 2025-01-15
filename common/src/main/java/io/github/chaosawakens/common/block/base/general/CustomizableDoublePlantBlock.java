package io.github.chaosawakens.common.block.base.general;

import io.github.chaosawakens.common.block.base.general.config.PlantVegetationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.function.Supplier;

public class CustomizableDoublePlantBlock extends DoublePlantBlock {
    private final Supplier<PlantVegetationConfig> plantCnfg;

    public CustomizableDoublePlantBlock(Properties properties, Supplier<PlantVegetationConfig> plantCnfg) {
        super(properties);
        this.plantCnfg = plantCnfg;
    }

    @Override
    protected boolean mayPlaceOn(BlockState targetState, BlockGetter curLevel, BlockPos targetPos) {
        return plantCnfg != null && plantCnfg.get() != null && plantCnfg.get().allowPlacementOn(curLevel, targetPos, targetState);
    }

    @Override
    public boolean canSurvive(BlockState targetState, LevelReader curLevel, BlockPos targetPos) {
        return targetState.getValue(HALF) != DoubleBlockHalf.UPPER ? mayPlaceOn(curLevel.getBlockState(targetPos.below()), curLevel, targetPos.below()) : curLevel.getBlockState(targetPos.below()).is(this);
    }
}
