package io.github.chaosawakens.common.block.vegetation.general;

import io.github.chaosawakens.common.registry.CATags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LeafCarpetBlock extends MultifaceBlock implements SimpleWaterloggedBlock {
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    private final MultifaceSpreader leafCarpetSpreader = new MultifaceSpreader(this);

    public LeafCarpetBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState targetState, Direction facingDir, BlockState neighbourState, LevelAccessor curLevel, BlockPos targetPos, BlockPos neighbourPos) {
        if (isWaterLogged(targetState)) curLevel.scheduleTick(targetPos, Fluids.WATER, Fluids.WATER.getTickDelay(curLevel));

        return super.updateShape(targetState, facingDir, neighbourState, curLevel, targetPos, neighbourPos);
    }

    @Override
    public boolean canBeReplaced(BlockState targetState, BlockPlaceContext ctx) {
        return !ctx.getItemInHand().is(CATags.CAItemTags.LEAF_CARPETS) || super.canBeReplaced(targetState, ctx);
    }

    @Override
    public FluidState getFluidState(BlockState targetState) {
        return isWaterLogged(targetState) ? Fluids.WATER.getSource(false) : super.getFluidState(targetState);
    }

    @NotNull
    @Override
    public MultifaceSpreader getSpreader() {
        return leafCarpetSpreader;
    }

    public static boolean isWaterLogged(BlockState targetState) {
        return targetState.hasProperty(WATERLOGGED) && targetState.getValue(WATERLOGGED);
    }

    public static BlockState setWaterlogged(BlockState targetState, boolean waterlogged) {
        return targetState.hasProperty(WATERLOGGED) ? targetState.setValue(WATERLOGGED, waterlogged) : targetState;
    }

    public static BlockState setWaterlogged(BlockState targetState) {
        return setWaterlogged(targetState, true);
    }

    public static Map<Direction, BooleanProperty> getMappedFaces() {
        return PROPERTY_BY_DIRECTION;
    }
}
