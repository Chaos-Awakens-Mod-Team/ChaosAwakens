package io.github.chaosawakens.common.block.base.general;

import io.github.chaosawakens.common.block.base.general.config.PlantVegetationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CustomizableFlowerBlock extends FlowerBlock {
    private final Supplier<PlantVegetationConfig> plantCnfg;
    private final Supplier<MobEffect> suspiciousStewEffect;

    public CustomizableFlowerBlock(Supplier<MobEffect> suspiciousStewEffect, int effectDurationSeconds, Properties properties, Supplier<PlantVegetationConfig> plantCnfg) {
        super(suspiciousStewEffect == null ? MobEffects.CONFUSION : suspiciousStewEffect.get(), effectDurationSeconds, properties); // Need to plug a default value  in (not gonna be used anyway but screw you Vanilla nullity checks!!!)
        this.plantCnfg = plantCnfg;
        this.suspiciousStewEffect = suspiciousStewEffect;
    }

    public CustomizableFlowerBlock(Properties properties, Supplier<PlantVegetationConfig> plantCnfg) {
        this(null, 0, properties, plantCnfg);
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
    public MobEffect getSuspiciousEffect() {
        return suspiciousStewEffect == null ? null : suspiciousStewEffect.get();
    }
}
