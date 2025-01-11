package io.github.chaosawakens.mixins.common.block;

import io.github.chaosawakens.common.registry.CATags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public final class CropBlockMixin { // Screw you arbitrary loader hooks (Literally only Forge adds hooks, not worth the platform-segregation implementation effort)

    private CropBlockMixin() {
        throw new IllegalAccessError("Attempted to construct standalone Mixin Class!");
    }

    @Inject(method = "mayPlaceOn", at = @At("RETURN"), cancellable = true)
    private void chaosawakens$mayPlaceOn(BlockState targetState, BlockGetter curLevel, BlockPos targetPos, CallbackInfoReturnable<Boolean> cir) {
        boolean existingReturnCondition = Boolean.TRUE.equals(cir.getReturnValue());
        boolean modifiedReturn = existingReturnCondition || targetState.is(CATags.CABlockTags.FARMLAND_BLOCKS.get());

        cir.setReturnValue(modifiedReturn);
    }

    @Redirect(method = "getGrowthSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0))
    private static boolean chaosawakens$getGrowthSpeed(BlockState originalState, Block originalBlock) {
        return originalState.is(CATags.CABlockTags.FARMLAND_BLOCKS.get()) || originalState.is(originalBlock);
    }
}
