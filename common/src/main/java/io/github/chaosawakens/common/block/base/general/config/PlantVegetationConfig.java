package io.github.chaosawakens.common.block.base.general.config;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public interface PlantVegetationConfig {

    boolean allowPlacementOn(BlockGetter curLevel, BlockPos targetPos, BlockState targetState);
    
    boolean canBeBonemealed(LevelReader curLevel, BlockPos targetPos, BlockState targetState, boolean onClient);

    void performBonemeal(LevelAccessor curServerLevel, RandomSource randSrc, BlockPos targetPos, BlockState targetState, Supplier<Block> curBlock);
}
