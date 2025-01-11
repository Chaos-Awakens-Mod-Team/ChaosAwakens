package io.github.chaosawakens.common.block.vegetation.dense;

import io.github.chaosawakens.common.block.base.general.CustomizableFarmBlock;
import io.github.chaosawakens.common.block.base.general.config.VegetationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class TerraPretaFarmBlock extends CustomizableFarmBlock {

    public TerraPretaFarmBlock(Properties properties) {
        super(properties, VegetationConfig.TERRA_PRETA.getDirtConfig());
    }

    @Override
    public void randomTick(BlockState targetState, ServerLevel curServerLevel, BlockPos targetPos, RandomSource randSrc) {
        super.randomTick(targetState, curServerLevel, targetPos, randSrc);

        BlockPos abovePos = targetPos.above();
        BlockState aboveState = curServerLevel.getBlockState(abovePos);

        if (aboveState.is(BlockTags.CROPS) && aboveState.getValues().keySet().stream().map(Property::getName).anyMatch("age"::equals)) {
            IntegerProperty curAgeProperty = (IntegerProperty) aboveState.getProperties().stream().filter(curProperty -> curProperty.getName().equals("age")).findFirst().get();
            int maxAge = curAgeProperty.getPossibleValues().size() - 1;

            if (randSrc.nextInt(4) == 0 && aboveState.getValue(curAgeProperty) < maxAge) curServerLevel.setBlockAndUpdate(abovePos, aboveState.setValue(curAgeProperty, aboveState.getValue(curAgeProperty) + 1));
        }
    }
}
