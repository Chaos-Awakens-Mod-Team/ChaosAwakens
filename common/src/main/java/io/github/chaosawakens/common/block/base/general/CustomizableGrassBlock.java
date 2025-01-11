package io.github.chaosawakens.common.block.base.general;

import com.google.common.base.Suppliers;
import io.github.chaosawakens.common.block.base.general.config.StandardVegetationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CustomizableGrassBlock extends CustomizableSpreadingDirtBlock implements BonemealableBlock {

    public CustomizableGrassBlock(Properties properties, Supplier<StandardVegetationConfig> stndrdCnfg) {
        super(properties, stndrdCnfg);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader curLevel, BlockPos targetPos, BlockState targetState, boolean onClient) {
        return getGrassConfig() != null && getGrassConfig().get().canBeBonemealed(curLevel, targetPos, targetState, onClient);
    }

    @Override
    public boolean isBonemealSuccess(Level curLevel, RandomSource rand, BlockPos targetPos, BlockState targetState) {
        return getGrassConfig() != null && getGrassConfig().get().canBeBonemealed(curLevel, targetPos, targetState, curLevel.isClientSide());
    }

    @Override
    public void performBonemeal(ServerLevel curServerLevel, RandomSource randSrc, BlockPos targetPos, BlockState targetState) {
        if (getGrassConfig() != null && getGrassConfig().get().canBeBonemealed(curServerLevel, targetPos, targetState, false)) getGrassConfig().get().performBonemeal(curServerLevel, randSrc, targetPos, targetState, Suppliers.ofInstance(this));
    }
}
