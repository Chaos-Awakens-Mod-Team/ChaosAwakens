package io.github.chaosawakens.mixins;

import io.github.chaosawakens.common.enchantments.HoplologyEnchantment;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    private LivingEntityMixin() {
        throw new IllegalAccessError("Attempted to construct standalone Mixin Class!");
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;actuallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)V"))
    public void injectHopology(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        HoplologyEnchantment.updateProtection(LivingEntity.class.cast(this));
    }
}
