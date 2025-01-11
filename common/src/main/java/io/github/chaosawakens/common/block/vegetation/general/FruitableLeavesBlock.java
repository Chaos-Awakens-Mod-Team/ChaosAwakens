package io.github.chaosawakens.common.block.vegetation.general;

import io.github.chaosawakens.common.registry.CABlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FruitableLeavesBlock extends LeavesBlock {
    public static final BooleanProperty RIPE = CABlockStateProperties.RIPE;
    protected final Supplier<Item> fruitItemSup;
    protected final int probability; // 1 in {probability}
    protected final int minFruitCount;
    protected final int maxFruitCount;

    public FruitableLeavesBlock(Supplier<Item> fruitItemSup, int probability, int minFruitCount, int maxFruitCount, Properties properties) {
        super(properties);
        this.fruitItemSup = fruitItemSup;
        this.probability = probability;
        this.minFruitCount = minFruitCount;
        this.maxFruitCount = maxFruitCount;

        registerDefaultState(getStateDefinition().any().setValue(DISTANCE, 7).setValue(PERSISTENT, false).setValue(WATERLOGGED, false).setValue(RIPE, false));
    }

    public FruitableLeavesBlock(Supplier<Item> fruitItemSup, int probability, Properties properties) {
        this(fruitItemSup, probability, 1, 3, properties);
    }

    public FruitableLeavesBlock(Supplier<Item> fruitItemSup, Properties properties) {
        this(fruitItemSup, 15, properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(RIPE);
    }

    @Override
    public boolean isRandomlyTicking(BlockState targetState) {
        return super.isRandomlyTicking(targetState) || !isRipe(targetState);
    }

    @Override
    public void randomTick(BlockState targetState, ServerLevel curLevel, BlockPos targetPos, RandomSource random) {
        super.randomTick(targetState, curLevel, targetPos, random);

        if (!isRipe(targetState) && random.nextInt(probability) == 0 && (!decaying(targetState) || isPersistent(targetState))) curLevel.setBlockAndUpdate(targetPos, setRipe(targetState));
    }

    @Override
    public @NotNull InteractionResult use(BlockState targetState, Level curLevel, BlockPos targetPos, Player interactingPlayer, InteractionHand usedHand, BlockHitResult result) {
        return extractFruit(targetState, targetPos, curLevel);
    }

    protected InteractionResult extractFruit(BlockState targetState, BlockPos targetPos, Level curLevel) {
        if (isRipe(targetState)) {
            curLevel.setBlockAndUpdate(targetPos, setRipe(targetState, false));
            popResource(curLevel, targetPos, new ItemStack(fruitItemSup.get(), calculateRandomFruitDrop()));

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS; // Not the best practice to not return super() method instead but eh, future problem for future me (if ever)
    }

    public Supplier<Item> getFruitItem() {
        return fruitItemSup;
    }

    public int getMinFruitCount() {
        return minFruitCount;
    }

    public int getMaxFruitCount() {
        return maxFruitCount;
    }

    public int getFruitDropProbability() {
        return probability;
    }

    public int calculateRandomFruitDrop() {
        return maxFruitCount - minFruitCount < 0 ? 1 : RandomSource.create().nextInt(minFruitCount, maxFruitCount);
    }

    public static boolean isRipe(BlockState targetState) {
        return targetState.hasProperty(RIPE) && targetState.getValue(RIPE);
    }

    public static BlockState setRipe(BlockState targetState, boolean ripe) {
        return targetState.hasProperty(RIPE) ? targetState.setValue(RIPE, ripe) : targetState;
    }

    public static BlockState setRipe(BlockState targetState) {
        return setRipe(targetState, true);
    }

    public static boolean isPersistent(BlockState targetState) {
        return targetState.hasProperty(PERSISTENT) && targetState.getValue(PERSISTENT);
    }

    public static BlockState setPersistent(BlockState targetState, boolean persistent) {
        return targetState.hasProperty(PERSISTENT) ? targetState.setValue(PERSISTENT, persistent) : targetState;
    }

    public static BlockState setPersistent(BlockState targetState) {
        return setPersistent(targetState, true);
    }
}
