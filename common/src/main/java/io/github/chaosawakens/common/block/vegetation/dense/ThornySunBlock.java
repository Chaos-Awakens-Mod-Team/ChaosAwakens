package io.github.chaosawakens.common.block.vegetation.dense;

import io.github.chaosawakens.common.block.base.general.CustomizableTallFlowerBlock;
import io.github.chaosawakens.common.block.base.general.config.VegetationConfig;
import io.github.chaosawakens.common.registry.CADamageEntries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ThornySunBlock extends CustomizableTallFlowerBlock {

    public ThornySunBlock(Properties properties) {
        super(properties, VegetationConfig.DENSE.getPlantConfig());
    }

    @Override
    public void entityInside(BlockState targetState, Level curLevel, BlockPos targetPos, Entity intersectingEntity) {
        intersectingEntity.hurt(CADamageEntries.CADamageSources.thornySun(curLevel), 1.0F);
    }
}
