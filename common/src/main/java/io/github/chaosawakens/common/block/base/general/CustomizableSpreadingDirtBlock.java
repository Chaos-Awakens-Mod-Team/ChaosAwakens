package io.github.chaosawakens.common.block.base.general;

import com.google.common.base.Suppliers;
import io.github.chaosawakens.common.block.base.general.config.DirtVegetationConfig;
import io.github.chaosawakens.common.block.base.general.config.GrassVegetationConfig;
import io.github.chaosawakens.common.block.base.general.config.StandardVegetationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CustomizableSpreadingDirtBlock extends SpreadingSnowyDirtBlock {
    protected final Supplier<StandardVegetationConfig> stndrdCnfg;

    public CustomizableSpreadingDirtBlock(Properties properties, Supplier<StandardVegetationConfig> stndrdCnfg) {
        super(properties);
        this.stndrdCnfg = stndrdCnfg;
    }

    public Supplier<StandardVegetationConfig> getVegetationConfig() {
        return stndrdCnfg;
    }

    @Nullable
    public Supplier<DirtVegetationConfig> getDirtConfig() {
        return stndrdCnfg.get().getDirtConfig();
    }

    @Nullable
    public Supplier<GrassVegetationConfig> getGrassConfig() {
        return stndrdCnfg.get().getGrassConfig();
    }

    @Override
    public BlockState updateShape(BlockState targetState, Direction facingDir, BlockState facingState, LevelAccessor curLevel, BlockPos targetPos, BlockPos facingPos) {
        return facingDir == Direction.UP && getDirtConfig() != null ? targetState.setValue(SNOWY, getDirtConfig().get().allowSnowyVariant(curLevel, targetState, targetPos, curLevel.getRandom())) : super.updateShape(targetState, facingDir, facingState, curLevel, targetPos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState clickedState = ctx.getLevel().getBlockState(ctx.getClickedPos().above());

        return defaultBlockState().setValue(SNOWY, getDirtConfig() != null ? getDirtConfig().get().allowSnowyVariant(ctx.getLevel(), clickedState, ctx.getClickedPos().above(), ctx.getLevel().getRandom()) : clickedState.is(BlockTags.SNOW));
    }

    @Override
    public void randomTick(BlockState targetState, ServerLevel curServerLevel, BlockPos targetPos, RandomSource randSrc) {
        Supplier<Block> dirtBlock = getDirtConfig() != null && getDirtConfig().get().getBaseDirtBlock(Suppliers.ofInstance(this)).get() != null ? getDirtConfig().get().getBaseDirtBlock(Suppliers.ofInstance(this)) : Suppliers.ofInstance(this);

        if (getDirtConfig() != null && dirtBlock.get() != null) {
            if (!getDirtConfig().get().allowGrassBlockConversion(curServerLevel, targetState, targetPos, randSrc)) curServerLevel.setBlockAndUpdate(targetPos, dirtBlock.get().defaultBlockState());
            else if (getDirtConfig().get().allowPropagation(curServerLevel, targetState, targetPos, randSrc)) getDirtConfig().get().performSpread(curServerLevel, defaultBlockState(), targetPos, randSrc, Suppliers.ofInstance(this));
        }
    }
}
