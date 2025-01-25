package io.github.chaosawakens.common.block.base.general;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.github.chaosawakens.common.registry.CATags;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class MultiLayerPlantBlock extends BushBlock implements BonemealableBlock {
    private IntegerProperty level;
    private final IntOpenHashSet modularLevels = new IntOpenHashSet();
    private final int maxLevel;

    public MultiLayerPlantBlock(Properties properties, int maxLevel, IntOpenHashSet modularLevels) {
        super(properties);
        this.maxLevel = maxLevel;

        registerDefaultState(getStateDefinition().any().setValue(getLevelProperty(), 0));

        modularLevels.intStream()
                .filter(curPlantLayerLevel -> curPlantLayerLevel > getPossibleLevels().asList().get(0) && curPlantLayerLevel < getPossibleLevels().asList().get(getPossibleLevels().size() - 1))
                .forEach(this.modularLevels::add);
    }

    public MultiLayerPlantBlock(Properties properties, int maxLevel) {
        this(properties, maxLevel, IntOpenHashSet.of());
    }

    public MultiLayerPlantBlock(Properties properties) {
        this(properties, 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(getLevelProperty());
    }

    @Override
    public void setPlacedBy(Level curLevel, BlockPos targetPos, BlockState targetState, @Nullable LivingEntity placerEntity, ItemStack blockItemStack) {
        placeAt(curLevel, targetState, targetPos, Block.UPDATE_ALL);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level curLevel = ctx.getLevel();
        BlockPos clickedPos = ctx.getClickedPos();
        int maxLevelForBuildHeight = (getModularLevels().isEmpty() ? getPossibleLevels().asList().get(getPossibleLevels().size() - 1) : getModularLevels().stream().findFirst().get()) + 1;

        return clickedPos.getY() < curLevel.getMaxBuildHeight() - maxLevelForBuildHeight && curLevel.getBlockState(clickedPos.above()).canBeReplaced() ? super.getStateForPlacement(ctx) : null;
    }

    @Override
    public BlockState updateShape(BlockState targetState, Direction facingDir, BlockState adjacentState, LevelAccessor curLevel, BlockPos curPos, BlockPos adjacentPos) { // Specifically responsible for updating the base of the multi-layer plant
        int curPlantLayerLevel = getLevelForState(targetState);

        if (curPlantLayerLevel != -1 && (facingDir.getAxis() != Direction.Axis.Y || (curPlantLayerLevel == getPossibleLevels().asList().get(getPossibleLevels().size() - 1)) == (facingDir == Direction.UP) || (adjacentState.is(this) && getLevelForState(adjacentState) != curPlantLayerLevel))) {
            return curPlantLayerLevel == 0 && facingDir == Direction.DOWN && !targetState.canSurvive(curLevel, curPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(targetState, facingDir, adjacentState, curLevel, curPos, adjacentPos);
        } else return Blocks.AIR.defaultBlockState();
    }

    @Override
    public void playerDestroy(Level curLevel, Player responsiblePlayer, BlockPos curPos, BlockState droppedState, @Nullable BlockEntity targetBlockEntity, ItemStack usedStack) {
        super.playerDestroy(curLevel, responsiblePlayer, curPos, Blocks.AIR.defaultBlockState(), targetBlockEntity, usedStack);
    }

    @Override
    protected boolean mayPlaceOn(BlockState targetState, BlockGetter curLevel, BlockPos targetPos) {
        return targetState.is(BlockTags.DIRT) || targetState.is(CATags.CABlockTags.FARMLAND_BLOCKS.get());
    }

    @Override
    public boolean canSurvive(BlockState targetState, LevelReader curLevel, BlockPos targetPos) {
        return getLevelForState(targetState) == 0 ? mayPlaceOn(curLevel.getBlockState(targetPos.below()), curLevel, targetPos.below()) : curLevel.getBlockState(targetPos.below()).is(this);
    }

    @Override
    public long getSeed(BlockState targetState, BlockPos targetPos) {
        return Mth.getSeed(targetPos.getX(), targetPos.below(getLevelForState(targetState)).getY(), targetPos.getZ());
    }

    public int getLevelForState(BlockState targetState) {
        return targetState.hasProperty(getLevelProperty()) ? targetState.getValue(getLevelProperty()) : -1;
    }

    public IntegerProperty getLevelProperty() {
        return level == null ? level = IntegerProperty.create("level", 0, maxLevel == 0 ? 2 : maxLevel) : level != null && ImmutableList.copyOf(level.getPossibleValues()).get(level.getPossibleValues().size() - 1) != maxLevel ? level = IntegerProperty.create("level", 0, maxLevel) : level;
    }

    public ImmutableSet<Integer> getPossibleLevels() {
        return ImmutableSet.copyOf(getLevelProperty().getPossibleValues());
    }

    public ImmutableSet<Integer> getModularLevels() {
        return ImmutableSet.copyOf(modularLevels);
    }

    public static void placeAt(LevelAccessor curLevel, BlockState targetBaseState, BlockPos targetBasePos, int updateFlags) {
        Block targetBaseBlock = targetBaseState.getBlock();

        if (targetBaseBlock instanceof MultiLayerPlantBlock targetMultiLayerPlantBaseBlock) {
            int maxPlantLayerLevel = targetMultiLayerPlantBaseBlock.getPossibleLevels().asList().get(targetMultiLayerPlantBaseBlock.getPossibleLevels().size() - 1);
            int curPlantLayerLevel = targetMultiLayerPlantBaseBlock.getLevelForState(targetBaseState);

            if (curPlantLayerLevel == -1) return; // Invalid state (somehow)

            curLevel.setBlock(targetBasePos, DoublePlantBlock.copyWaterloggedFrom(curLevel, targetBasePos, targetBaseState.setValue(targetMultiLayerPlantBaseBlock.getLevelProperty(), 0)), updateFlags);

            if (!targetMultiLayerPlantBaseBlock.getModularLevels().isEmpty()) {
                int minModularLevel = targetMultiLayerPlantBaseBlock.getModularLevels().asList().get(0);

                if (minModularLevel > 0) {
                    for (int plantLayerLevel = 1; plantLayerLevel <= minModularLevel; plantLayerLevel++) {
                        curLevel.setBlock(targetBasePos.above(plantLayerLevel), DoublePlantBlock.copyWaterloggedFrom(curLevel, targetBasePos.above(plantLayerLevel), targetBaseState.setValue(targetMultiLayerPlantBaseBlock.getLevelProperty(), plantLayerLevel)), updateFlags);
                    }
                }
            } else {
                for (int plantLayerLevel = 1; plantLayerLevel <= maxPlantLayerLevel; plantLayerLevel++) {
                    curLevel.setBlock(targetBasePos.above(plantLayerLevel), DoublePlantBlock.copyWaterloggedFrom(curLevel, targetBasePos.above(plantLayerLevel), targetBaseState.setValue(targetMultiLayerPlantBaseBlock.getLevelProperty(), plantLayerLevel)), updateFlags);
                }
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader curLevel, BlockPos targetPos, BlockState targetState, boolean onClient) {
        return !modularLevels.isEmpty();
    }

    @Override
    public boolean isBonemealSuccess(Level curLevel, RandomSource rand, BlockPos targetPos, BlockState targetState) {
        return !modularLevels.isEmpty() && getLevelForState(targetState) < getPossibleLevels().asList().get(getPossibleLevels().size() - 1);
    }

    @Override
    public void performBonemeal(ServerLevel curServerLevel, RandomSource randSrc, BlockPos targetPos, BlockState targetState) {
    }
}
