package io.github.chaosawakens.common.block.base.general.config;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface GrassVegetationConfig {

    @NotNull
    Optional<Function<RegistryAccess, Holder.Reference<PlacedFeature>>> getBonemealPlacedFeature();

    boolean canBeBonemealed(LevelReader curLevel, BlockPos targetPos, BlockState targetState, boolean onClient);
    boolean allowBiomeFeaturePlacement(LevelReader curLevel, BlockPos targetPos, BlockState targetState);

    void performBonemeal(LevelReader curServerLevel, RandomSource randSrc, BlockPos targetPos, BlockState targetState, Supplier<Block> curBlock);
}
