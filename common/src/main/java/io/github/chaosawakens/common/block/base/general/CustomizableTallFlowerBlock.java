package io.github.chaosawakens.common.block.base.general;

import io.github.chaosawakens.common.block.base.general.config.PlantVegetationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CustomizableTallFlowerBlock extends CustomizableDoublePlantBlock implements BonemealableBlock {

    public CustomizableTallFlowerBlock(Properties properties, Supplier<PlantVegetationConfig> plantCnfg) {
        super(properties, plantCnfg);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader curLevel, BlockPos targetPos, BlockState targetState, boolean onClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level curLevel, RandomSource rand, BlockPos targetPos, BlockState targetState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel curServerLevel, RandomSource randSrc, BlockPos targetPos, BlockState targetState) {
        popResource(curServerLevel, targetPos, asItem().getDefaultInstance());
    }
}
