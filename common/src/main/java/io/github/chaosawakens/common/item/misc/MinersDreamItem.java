package io.github.chaosawakens.common.item.misc;

import io.github.chaosawakens.common.registry.CABlocks;
import io.github.chaosawakens.common.registry.CATags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class MinersDreamItem extends Item {
    private static final int HOLE_LENGTH = 45;
    private static final int HOLE_WIDTH = 5;
    private static final int HOLE_HEIGHT = 8;

    public MinersDreamItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext ctx) {
        Direction targetDirection = ctx.getHorizontalDirection();
        Level currentWorld = ctx.getLevel();

        if (currentWorld.isClientSide) return InteractionResult.FAIL;
        if (targetDirection == Direction.UP || targetDirection == Direction.DOWN) return InteractionResult.FAIL;

        BlockPos breakPos = ctx.getClickedPos();
        int targetY = ((breakPos.getY() % 8) + 8) % 8;
        Player currentPlayer = ctx.getPlayer();

        Vec3i targetFacingDirection = targetDirection.getNormal();

        currentPlayer.playNotifySound(SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1.0F, 1.5F);
        currentWorld.addParticle(ParticleTypes.EXPLOSION.getType(), breakPos.getX(), breakPos.getY(), breakPos.getZ(), 0.25F, 0.25F, 0.25F);

        for (int i = 0; i < HOLE_LENGTH; i++) {
            for (int j = 0; j < HOLE_HEIGHT; j++) {
                for (int k = -HOLE_WIDTH; k <= HOLE_WIDTH; k++) {
                    int lengthDelta = i * targetFacingDirection.getX() + k * targetFacingDirection.getZ();
                    int widthDelta = i * targetFacingDirection.getZ() + k * targetFacingDirection.getX();
                    BlockPos targetPos = breakPos.offset(lengthDelta, targetY < 0 ? (targetY + j) + 2 : -targetY + j, widthDelta);
                    BlockState targetBlockState = currentWorld.getBlockState(targetPos);
                    FluidState targetFluidState = currentWorld.getFluidState(targetPos);

                    if (targetBlockState.is(CATags.CABlockTags.MINERS_DREAM_MINEABLE) || !targetFluidState.is(Fluids.EMPTY)) {
                        if (currentPlayer.isShiftKeyDown()) placeWoodPillars(currentWorld, targetPos, i, j, k);
                        else placeAndPatchWoodPillars(currentWorld, targetPos, i, j, k);
                    }
                }
            }
        }

        ctx.getPlayer().awardStat(Stats.ITEM_USED.get(this));
        ctx.getItemInHand().shrink(1);
        ctx.getPlayer().getCooldowns().addCooldown(this, 20);

        return InteractionResult.SUCCESS;
    }

    private static void placeWoodPillars(Level targetLevel, BlockPos targetPos, int l, int h, int w) {
        if (l != 0 && l % 8 == 0) {
            if (w == -HOLE_WIDTH + 1 || w == HOLE_WIDTH - 1) {
                if (h == HOLE_HEIGHT - 1) {
                    targetLevel.setBlockAndUpdate(targetPos, CABlocks.MINING_PLANKS.get().defaultBlockState());
                    return;
                }

                targetLevel.setBlockAndUpdate(targetPos, CABlocks.MINING_FENCE.get().defaultBlockState());
                return;
            }

            if (h == HOLE_HEIGHT - 1) {
                if (w == 0) {
                    targetLevel.setBlockAndUpdate(targetPos, CABlocks.MINING_LAMP.get().defaultBlockState());
                    return;
                }

                targetLevel.setBlockAndUpdate(targetPos, CABlocks.MINING_SLAB.get().defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP));
                return;
            }
        }

        if (w != -HOLE_WIDTH && w != HOLE_WIDTH) targetLevel.removeBlock(targetPos, false);
    }

    private static void placeAndPatchWoodPillars(Level targetLevel, BlockPos targetPos, int l, int h, int w) {
        placeWoodPillars(targetLevel, targetPos, l, h, w);

        if (w == -HOLE_WIDTH || w == HOLE_WIDTH) {
            BlockPos currentPos = targetPos.offset(0, 0, 0);

            if (targetLevel.getBlockState(currentPos).canBeReplaced() || targetLevel.getBlockState(currentPos).isAir()) {
                targetLevel.setBlockAndUpdate(currentPos, CABlocks.MINING_PLANKS.get().defaultBlockState());
                return;
            }
        }

        if (h == HOLE_HEIGHT - 1) {
            BlockPos currentPos = targetPos.offset(0, 1, 0);

            if (targetLevel.getBlockState(currentPos).canBeReplaced() || targetLevel.getBlockState(currentPos).isAir() || targetLevel.getBlockState(targetPos).getBlock() instanceof LiquidBlock || isBorderedByFallingBlock(targetLevel, currentPos)) {
                targetLevel.setBlockAndUpdate(currentPos, CABlocks.MINING_PLANKS.get().defaultBlockState());
            }
        }

        if (targetLevel.getBlockState(targetPos).getBlock() instanceof LiquidBlock || isBorderedByFallingBlock(targetLevel, targetPos)) {
            targetLevel.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);

            if (targetLevel.getBlockEntity(targetPos) != null) targetLevel.removeBlockEntity(targetPos);
        }
    }

    private static boolean isBorderedByFallingBlock(Level targetLevel, BlockPos targetPos) {
        BlockState targetBlockState = targetLevel.getBlockState(targetPos);

        return targetBlockState.is(Blocks.GRAVEL) || targetBlockState.is(Blocks.SUSPICIOUS_GRAVEL) || targetBlockState.is(Blocks.SAND) || targetBlockState.is(Blocks.RED_SAND) || targetBlockState.getBlock() instanceof FallingBlock;
    }
}
