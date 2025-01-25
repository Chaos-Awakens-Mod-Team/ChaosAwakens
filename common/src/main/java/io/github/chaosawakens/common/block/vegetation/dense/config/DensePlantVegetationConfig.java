package io.github.chaosawakens.common.block.vegetation.dense.config;

import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.common.block.vegetation.general.config.DefaultPlantVegetationConfig;
import io.github.chaosawakens.common.registry.CABlocks;
import io.github.chaosawakens.common.registry.CATags;
import io.github.chaosawakens.util.RegistryUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DensePlantVegetationConfig extends DefaultPlantVegetationConfig {
    private static final ObjectArrayList<Supplier<Block>> VALID_DENSE_PLANT_BLOCKS = BlockPropertyWrapper.getMappedBpws().keySet().stream()
            .filter(curBlockSup -> curBlockSup.get().defaultBlockState().is(CATags.CABlockTags.DENSE_VEGETATION.get()) && !curBlockSup.get().defaultBlockState().is(CABlocks.DENSE_GRASS.get()) && !curBlockSup.get().defaultBlockState().is(CABlocks.TALL_DENSE_GRASS.get()))
            .collect(Collectors.toCollection(ObjectArrayList::new));

    public DensePlantVegetationConfig() {
        super(CABlocks.DENSE_GRASS);
    }

    @Override
    public boolean canBeBonemealed(LevelReader curLevel, BlockPos targetPos, BlockState targetState, boolean onClient) {
        Supplier<Block> targetBlockSup = targetState::getBlock;

        if (VALID_DENSE_PLANT_BLOCKS.stream().anyMatch(curBlockSup -> curBlockSup.get() == targetBlockSup.get())) { // Guard excess computation instead of inlining with ternary op
            Supplier<Block> plantBlock = targetBlockSup.get().getDescriptionId().contains("tall_") ? RegistryUtil.getBlockBasedOnPrefix(targetBlockSup, "tall_", "") : targetBlockSup;
            Supplier<Block> tallPlantBlock = targetBlockSup.get().getDescriptionId().contains("tall_") ? targetBlockSup : RegistryUtil.getBlockBasedOnPrefix(plantBlock, "", "tall_");

            return plantBlock != null && tallPlantBlock != null && plantBlock.get() != null && tallPlantBlock.get() != null && plantBlock.get() != tallPlantBlock.get() && tallPlantBlock.get() instanceof DoublePlantBlock;
        } else return super.canBeBonemealed(curLevel, targetPos, targetState, onClient);
    }

    @Override
    public void performBonemeal(LevelAccessor curServerLevel, RandomSource randSrc, BlockPos targetPos, BlockState targetState, Supplier<Block> curBlock) {
        if (curServerLevel instanceof ServerLevel serverLevel) {
            if (VALID_DENSE_PLANT_BLOCKS.stream().anyMatch(curBlockSup -> curBlockSup.get() == curBlock.get())) {
                Supplier<Block> tallPlantBlock = curBlock.get().getDescriptionId().contains("tall_") ? curBlock : RegistryUtil.getBlockBasedOnPrefix(curBlock, "", "tall_");

                if (tallPlantBlock.get().defaultBlockState().canSurvive(curServerLevel, targetPos) && serverLevel.isEmptyBlock(targetPos.above())) DoublePlantBlock.placeAt(curServerLevel, tallPlantBlock.get().defaultBlockState(), targetPos, Block.UPDATE_CLIENTS);
            } else super.performBonemeal(curServerLevel, randSrc, targetPos, targetState, curBlock);
        }
    }
}
