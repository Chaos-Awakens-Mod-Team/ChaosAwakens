package io.github.chaosawakens.common.block.vegetation.general.config;

import com.google.common.base.Suppliers;
import io.github.chaosawakens.common.block.base.general.config.DirtVegetationConfig;
import io.github.chaosawakens.util.RegistryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultDirtVegetationConfig implements DirtVegetationConfig {
    private final Supplier<Block> baseDirtBlock;
    private final boolean allowSnowyVariant;

    public DefaultDirtVegetationConfig(Supplier<Block> baseDirtBlock, boolean allowSnowyVariant) {
        this.baseDirtBlock = baseDirtBlock;
        this.allowSnowyVariant = allowSnowyVariant;
    }

    public DefaultDirtVegetationConfig(Supplier<Block> baseDirtBlock) {
        this(baseDirtBlock, false);
    }

    public DefaultDirtVegetationConfig() {
        this(Suppliers.ofInstance(null));
    }

    @Override
    public @NotNull Supplier<Block> getBaseDirtBlock(Supplier<Block> curBlock) {
        return baseDirtBlock != null && baseDirtBlock.get() != null ? baseDirtBlock : Objects.requireNonNull(RegistryUtil.getBlockBasedOnSuffix(curBlock, "_grass_block", "_dirt") == null ? RegistryUtil.getBlockBasedOnSuffix(curBlock, "_farmland", "_dirt") : RegistryUtil.getBlockBasedOnSuffix(curBlock, "_grass_block", "_dirt")); // Arbitrary ahh fallbacks
    }

    @Override
    public boolean allowSnowyVariant(LevelAccessor curServerLevel, BlockState targetState, BlockPos targetPos, RandomSource randSrc) {
        return allowSnowyVariant && targetState.is(BlockTags.SNOW);
    }

    @Override
    public boolean allowGrassBlockConversion(LevelAccessor curLevel, BlockState targetState, BlockPos targetPos, RandomSource randSrc) {
        BlockPos aboveTargetPos = targetPos.above();
        BlockState aboveTargetState = curLevel.getBlockState(aboveTargetPos);

        boolean snowSpreadGrass = allowSnowyVariant(curLevel, targetState, targetPos, randSrc) && aboveTargetState.is(BlockTags.SNOW) && aboveTargetState.hasProperty(SnowLayerBlock.LAYERS) && aboveTargetState.getValue(SnowLayerBlock.LAYERS) == 1;
        boolean lightSpreadGrass = LightEngine.getLightBlockInto(curLevel, targetState, targetPos, aboveTargetState, aboveTargetPos, Direction.UP, aboveTargetState.getLightBlock(curLevel, aboveTargetPos)) < curLevel.getMaxLightLevel();

        return snowSpreadGrass || (lightSpreadGrass && aboveTargetState.getFluidState().getAmount() != 8);
    }

    @Override
    public boolean allowPropagation(LevelAccessor curServerLevel, BlockState targetState, BlockPos targetPos, RandomSource randSrc) {
        return curServerLevel.getMaxLocalRawBrightness(targetPos.above()) > 9;
    }

    @Override
    public void performSpread(LevelAccessor serverLevel, BlockState targetState, BlockPos targetPos, RandomSource randSrc, Supplier<Block> curBlock) {
        for (int attempt = 0; attempt < 4; attempt++) {
            BlockPos offsetPos = targetPos.offset(randSrc.nextInt(3) - 1, randSrc.nextInt(5) - 3, randSrc.nextInt(3) - 1);

            if (serverLevel instanceof ServerLevel curServerLevel && curServerLevel.getBlockState(offsetPos).is(getBaseDirtBlock(curBlock).get())) {
                curServerLevel.setBlockAndUpdate(offsetPos, targetState.setValue(SnowyDirtBlock.SNOWY, allowSnowyVariant(serverLevel, targetState, targetPos, randSrc)));
            }
        }
    }
}
