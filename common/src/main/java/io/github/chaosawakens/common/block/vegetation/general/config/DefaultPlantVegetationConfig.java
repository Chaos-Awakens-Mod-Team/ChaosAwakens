package io.github.chaosawakens.common.block.vegetation.general.config;

import io.github.chaosawakens.common.block.base.general.config.PlantVegetationConfig;
import io.github.chaosawakens.common.registry.CATags;
import io.github.chaosawakens.util.RegistryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class DefaultPlantVegetationConfig implements PlantVegetationConfig {
    private final Supplier<Block> plantBlock;
    private final Supplier<Block> tallPlantBlock;
    private Predicate<BlockState> placementPredicate = state -> state.is(BlockTags.DIRT) || state.is(CATags.CABlockTags.FARMLAND_BLOCKS.get());

    public DefaultPlantVegetationConfig(Supplier<Block> plantBlock, Supplier<Block> tallPlantBlock) {
        this.plantBlock = plantBlock;
        this.tallPlantBlock = tallPlantBlock;
    }

    public DefaultPlantVegetationConfig(Supplier<Block> plantBlock) {
        this(plantBlock.get().getDescriptionId().contains("tall_") ? RegistryUtil.getBlockBasedOnPrefix(plantBlock, "tall_", "") : plantBlock, plantBlock.get().getDescriptionId().contains("tall_") ? plantBlock : RegistryUtil.getBlockBasedOnPrefix(plantBlock, "", "tall_"));
    }

    public DefaultPlantVegetationConfig() {
        this(null, null);
    }

    public DefaultPlantVegetationConfig withPlacementPredicate(Predicate<BlockState> placementPredicate) {
        this.placementPredicate = placementPredicate;
        return this;
    }

    @Override
    public boolean allowPlacementOn(BlockGetter curLevel, BlockPos targetPos, BlockState targetState) {
        return placementPredicate != null && placementPredicate.test(targetState);
    }

    @Override
    public boolean canBeBonemealed(LevelReader curLevel, BlockPos targetPos, BlockState targetState, boolean onClient) {
        return tallPlantBlock != null && tallPlantBlock.get() != null && tallPlantBlock.get() instanceof DoublePlantBlock && plantBlock != null && plantBlock.get() != null && plantBlock.get() != tallPlantBlock.get();
    }

    @Override
    public void performBonemeal(LevelAccessor curServerLevel, RandomSource randSrc, BlockPos targetPos, BlockState targetState, Supplier<Block> curBlock) {
        if (curServerLevel instanceof ServerLevel serverLevel) {
            if (tallPlantBlock.get().defaultBlockState().canSurvive(curServerLevel, targetPos) && serverLevel.isEmptyBlock(targetPos.above())) {
                DoublePlantBlock.placeAt(curServerLevel, tallPlantBlock.get().defaultBlockState(), targetPos, Block.UPDATE_CLIENTS);
            }
        }
    }
}
