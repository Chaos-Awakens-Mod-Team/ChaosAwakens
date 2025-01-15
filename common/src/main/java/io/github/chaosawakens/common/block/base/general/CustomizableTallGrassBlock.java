package io.github.chaosawakens.common.block.base.general;

import com.google.common.base.Suppliers;
import io.github.chaosawakens.common.block.base.general.config.PlantVegetationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CustomizableTallGrassBlock extends TallGrassBlock {
    private final Supplier<PlantVegetationConfig> plantCnfg;

    public CustomizableTallGrassBlock(Properties properties, Supplier<PlantVegetationConfig> plantCnfg) {
        super(properties);
        this.plantCnfg = plantCnfg;
    }

    @Nullable
    public Supplier<PlantVegetationConfig> getPlantConfig() {
        return plantCnfg;
    }

    @Override
    protected boolean mayPlaceOn(BlockState targetState, BlockGetter curLevel, BlockPos targetPos) {
        return getPlantConfig() != null && getPlantConfig().get().allowPlacementOn(curLevel, targetPos, targetState);
    }

    @Override
    public boolean canSurvive(BlockState targetState, LevelReader curLevel, BlockPos targetPos) { // Needed because Forge patch go brr
        return mayPlaceOn(curLevel.getBlockState(targetPos.below()), curLevel, targetPos.below());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader curLevel, BlockPos targetPos, BlockState targetState, boolean onClient) {
        return getPlantConfig() != null && getPlantConfig().get().canBeBonemealed(curLevel, targetPos, targetState, onClient);
    }

    @Override
    public boolean isBonemealSuccess(Level curLevel, RandomSource rand, BlockPos targetPos, BlockState targetState) {
        return getPlantConfig() != null && getPlantConfig().get().canBeBonemealed(curLevel, targetPos, targetState, curLevel.isClientSide());
    }

    @Override
    public void performBonemeal(ServerLevel curServerLevel, RandomSource randSrc, BlockPos targetPos, BlockState targetState) {
        if (getPlantConfig() != null && getPlantConfig().get().canBeBonemealed(curServerLevel, targetPos, targetState, false)) getPlantConfig().get().performBonemeal(curServerLevel, randSrc, targetPos, targetState, Suppliers.ofInstance(this));
    }
}
