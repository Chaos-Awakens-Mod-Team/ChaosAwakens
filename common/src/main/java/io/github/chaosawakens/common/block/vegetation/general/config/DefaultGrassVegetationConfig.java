package io.github.chaosawakens.common.block.vegetation.general.config;

import io.github.chaosawakens.common.block.base.general.config.GrassVegetationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class DefaultGrassVegetationConfig implements GrassVegetationConfig {
    private final Optional<Function<RegistryAccess, Holder.Reference<PlacedFeature>>> bonemealPlacedFeature;
    private final boolean allowBiomeFeaturePlacement;

    public DefaultGrassVegetationConfig(Optional<Function<RegistryAccess, Holder.Reference<PlacedFeature>>> bonemealPlacedFeature, boolean allowBiomeFeaturePlacement) {
        this.bonemealPlacedFeature = bonemealPlacedFeature;
        this.allowBiomeFeaturePlacement = allowBiomeFeaturePlacement;
    }

    public DefaultGrassVegetationConfig(Optional<Function<RegistryAccess, Holder.Reference<PlacedFeature>>> bonemealPlacedFeature) {
        this(bonemealPlacedFeature, true);
    }

    public DefaultGrassVegetationConfig() {
        this(Optional.empty());
    }

    @Override
    public @NotNull Optional<Function<RegistryAccess, Holder.Reference<PlacedFeature>>> getBonemealPlacedFeature() {
        return bonemealPlacedFeature;
    }

    @Override
    public boolean canBeBonemealed(LevelReader curLevel, BlockPos targetPos, BlockState targetState, boolean onClient) {
        return curLevel.getBlockState(targetPos.above()).isAir();
    }

    @Override
    public boolean allowBiomeFeaturePlacement(LevelReader curLevel, BlockPos targetPos, BlockState targetState) {
        return allowBiomeFeaturePlacement;
    }

    @Override
    public void performBonemeal(LevelReader curServerLevel, RandomSource randSrc, BlockPos targetPos, BlockState targetState, Supplier<Block> curBlock) {
        if (curServerLevel instanceof ServerLevel serverLevel) {
            BlockPos abovePos = targetPos.above();

            attemptLoop:
            for (int i = 0; i < 128; ++i) {
                BlockPos currentPos = abovePos;

                for (int j = 0; j < i / 16; ++j) {
                    currentPos = currentPos.offset(randSrc.nextInt(3) - 1, (randSrc.nextInt(3) - 1) * randSrc.nextInt(3) / 2, randSrc.nextInt(3) - 1);
                    if (!serverLevel.getBlockState(currentPos.below()).is(curBlock.get()) || serverLevel.getBlockState(currentPos).isCollisionShapeFullBlock(serverLevel, currentPos)) {
                        continue attemptLoop;
                    }
                }

                BlockState currentState = serverLevel.getBlockState(currentPos);

                if (currentState.getBlock() instanceof BonemealableBlock targetBonemealableBlock && randSrc.nextInt(10) == 0) targetBonemealableBlock.performBonemeal(serverLevel, randSrc, currentPos, currentState);
                if (currentState.isAir()) {
                    Holder<PlacedFeature> placedFeatureHolder;

                    if (allowBiomeFeaturePlacement(serverLevel, currentPos, currentState) && randSrc.nextInt(8) == 0) {
                        List<ConfiguredFeature<?, ?>> flowerFeatures = serverLevel.getBiome(currentPos).value().getGenerationSettings().getFlowerFeatures();

                        if (flowerFeatures.isEmpty()) continue;

                        placedFeatureHolder = ((RandomPatchConfiguration) flowerFeatures.get(0).config()).feature(); // Presumptuous cast moment
                    } else {
                        if (getBonemealPlacedFeature().isEmpty()) continue;

                        placedFeatureHolder = getBonemealPlacedFeature().get().apply(serverLevel.registryAccess());
                    }

                    placedFeatureHolder.value().place(serverLevel, serverLevel.getChunkSource().getGenerator(), randSrc, currentPos);
                }
            }
        }
    }
}
