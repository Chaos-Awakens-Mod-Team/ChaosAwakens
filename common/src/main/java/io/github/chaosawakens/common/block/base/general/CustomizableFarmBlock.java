package io.github.chaosawakens.common.block.base.general;

import com.google.common.base.Suppliers;
import io.github.chaosawakens.common.block.base.general.config.DirtVegetationConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CustomizableFarmBlock extends FarmBlock {
    private final Supplier<DirtVegetationConfig> config;

    public CustomizableFarmBlock(Properties properties, Supplier<DirtVegetationConfig> config) {
        super(properties);
        this.config = config;
    }

    public Supplier<DirtVegetationConfig> getConfig() {
        return config;
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return !defaultBlockState().canSurvive(ctx.getLevel(), ctx.getClickedPos()) && config.get().getBaseDirtBlock(Suppliers.ofInstance(this)).get() != null ? config.get().getBaseDirtBlock(Suppliers.ofInstance(this)).get().defaultBlockState() : super.getStateForPlacement(ctx);
    }

    @Override
    public void tick(BlockState targetState, ServerLevel curServerLevel, BlockPos targetPos, RandomSource randSrc) {
        if (!targetState.canSurvive(curServerLevel, targetPos)) convertToMappedDirt(null, targetState, curServerLevel, targetPos);
    }

    @Override
    public void randomTick(BlockState targetState, ServerLevel curServerLevel, BlockPos targetPos, RandomSource randSrc) {
        int curMoisture = targetState.getValue(MOISTURE);

        if (!isNearWater(curServerLevel, targetPos) && !curServerLevel.isRainingAt(targetPos.above())) {
            if (curMoisture > 0) curServerLevel.setBlock(targetPos, targetState.setValue(MOISTURE, curMoisture - 1), Block.UPDATE_CLIENTS);
            else if (!shouldMaintainFarmland(curServerLevel, targetPos)) convertToMappedDirt(null, targetState, curServerLevel, targetPos);
        } else if (curMoisture < 7) curServerLevel.setBlock(targetPos, targetState.setValue(MOISTURE, 7), Block.UPDATE_CLIENTS);
    }

    @Override
    public void fallOn(Level curLevel, BlockState targetState, BlockPos targetPos, Entity fallingEntity, float fallDistance) {
        if (!curLevel.isClientSide && curLevel.random.nextFloat() < fallDistance - 0.5F && fallingEntity instanceof LivingEntity && (fallingEntity instanceof Player || curLevel.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) && fallingEntity.getBbWidth() * fallingEntity.getBbWidth() * fallingEntity.getBbHeight() > 0.512F) {
            convertToMappedDirt(fallingEntity, targetState, curLevel, targetPos);
        }

        fallingEntity.causeFallDamage(fallDistance, 1.0F, fallingEntity.damageSources().fall()); // No super() call to prevent it from being reverted to normal dirt afterwards (Sorry mixin mods [lol])
    }

    public static void convertToMappedDirt(@Nullable Entity responsibleEntity, BlockState targetState, Level curLevel, BlockPos targetPos) {
        if (targetState.getBlock() instanceof CustomizableFarmBlock customizableFarmBlock) {
            Supplier<Block> dirtBlock = customizableFarmBlock.config.get().getBaseDirtBlock(Suppliers.ofInstance(customizableFarmBlock));
            BlockState updatedState = pushEntitiesUp(targetState, dirtBlock.get() == null ? Blocks.DIRT.defaultBlockState() : dirtBlock.get().defaultBlockState(), curLevel, targetPos);

            curLevel.setBlockAndUpdate(targetPos, updatedState);
            curLevel.gameEvent(GameEvent.BLOCK_CHANGE, targetPos, GameEvent.Context.of(responsibleEntity, updatedState));
        }
    }

    public static boolean shouldMaintainFarmland(BlockGetter blockGetter, BlockPos targetPos) {
        return blockGetter.getBlockState(targetPos.above()).is(BlockTags.MAINTAINS_FARMLAND);
    }

    public static boolean isNearWater(LevelReader curLevel, BlockPos targetPos) {
        for (BlockPos curPos : BlockPos.betweenClosed(targetPos.offset(-4, 0, -4), targetPos.offset(4, 1, 4))) {
            if (curLevel.getFluidState(curPos).is(FluidTags.WATER)) return true; // No direct return statement cuz it'd break the loop on first iteration regardless (duh) :HEHEHEHA:
        }

        return false;
    }
}
