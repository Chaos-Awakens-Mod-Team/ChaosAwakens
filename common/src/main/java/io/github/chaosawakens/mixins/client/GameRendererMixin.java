package io.github.chaosawakens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.chaosawakens.api.vfx.basic.ScreenShakeEffect;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    private GameRendererMixin() {
        throw new IllegalAccessError("Attempted to construct standalone Mixin Class!");
    }

    @Shadow
    public abstract Camera getMainCamera();

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V", shift = At.Shift.AFTER), cancellable = true)
    private void chaosawakens$renderLevel(float partialTicks, long endTimeNanos, PoseStack stack, CallbackInfo ci) {
        if (ScreenShakeEffect.getEnqueuedShakes().isEmpty()) return;

        Camera mainCam = getMainCamera();
        ScreenShakeEffect shake = ScreenShakeEffect.getEnqueuedShakes().first();

        if (shake != null && shake.getData().getDuration() > 0 && mainCam.getEntity().distanceToSqr(Vec3.atCenterOf(shake.getData().getOriginPos())) <= Math.pow(shake.getData().getRange(), 2)) {
            shake.getData().setDuration(shake.getData().getDuration() - 1);

            float delta = Minecraft.getInstance().getDeltaFrameTime();
            float ticksExistedDelta = (float) mainCam.getEntity().tickCount + delta;
            float finalAmount = shake.getData().getMagnitude() * (shake.getData().getDuration() / shake.getData().getFadeOut());

            mainCam.setRotation((float) (mainCam.getYRot() + finalAmount * Math.cos(ticksExistedDelta * 5.0F + 1.0F) * 25.0D), (float) (mainCam.getXRot() + finalAmount * Math.cos(ticksExistedDelta * 3.0F + 2.0F) * 25.0D));
            stack.mulPose(Axis.ZP.rotationDegrees((float) (finalAmount * Math.cos(ticksExistedDelta * 4.0F) * 25.0D)));
        } else if (shake == null || shake.getData().getDuration() <= 0) ScreenShakeEffect.getEnqueuedShakes().dequeue();
    }
}
