package io.github.chaosawakens.common.block.base.general.config;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface DirtVegetationConfig {

    @NotNull
    Supplier<Block> getBaseDirtBlock(Supplier<Block> curBlock);

    boolean allowSnowyVariant(LevelAccessor curServerLevel, BlockState targetState, BlockPos targetPos, RandomSource randSrc);
    boolean allowGrassBlockConversion(LevelAccessor curLevel, BlockState targetState, BlockPos targetPos, RandomSource randSrc);
    boolean allowPropagation(LevelAccessor curServerLevel, BlockState targetState, BlockPos targetPos, RandomSource randSrc);

    void performSpread(LevelAccessor serverLevel, BlockState targetState, BlockPos targetPos, RandomSource randSrc, Supplier<Block> curBlock);
}
