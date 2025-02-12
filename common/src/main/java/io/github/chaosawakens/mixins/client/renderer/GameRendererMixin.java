package io.github.chaosawakens.mixins.client.renderer;

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
        ScreenShakeEffect.getEnqueuedShakes().removeIf(shake -> shake == null || shake.getData().getDuration() <= 0);

        for (ScreenShakeEffect shake : ScreenShakeEffect.getEnqueuedShakes()) {
            if (shake != null && shake.getData().getDuration() > 0 && !Minecraft.getInstance().isPaused()) {
                shake.getData().setDuration(shake.getData().getDuration() - 1); // Allow screen shake to continue updating even if the player is out of range

                if (mainCam.getEntity().distanceToSqr(Vec3.atCenterOf(shake.getData().getOriginPos())) <= Math.pow(shake.getData().getRange(), 2)) {
                    float delta = Minecraft.getInstance().getDeltaFrameTime();
                    float ticksExistedDelta = (float) mainCam.getEntity().tickCount + delta;

                    double distance = mainCam.getEntity().getEyePosition().distanceTo(Vec3.atCenterOf(shake.getData().getOriginPos()));
                    float distanceScale = Math.max(0, 1 - (float)(distance / shake.getData().getRange()));

                    float finalAmp = (float) ((shake.getData().getMagnitude() * (shake.getData().getDuration() / (shake.getData().getFadeOut() == 0 ? 1 : shake.getData().getFadeOut()))) * Math.pow(distanceScale, 2)); // Avoid division by 0

                    mainCam.setRotation((float) (mainCam.getYRot() + finalAmp * Math.cos(ticksExistedDelta * 5.0F + 1.0F) * 25.0D), (float) (mainCam.getXRot() + finalAmp * Math.cos(ticksExistedDelta * 3.0F + 2.0F) * 25.0D));
                    stack.mulPose(Axis.ZP.rotationDegrees((float) (finalAmp * Math.cos(ticksExistedDelta * 4.0F) * 25.0D)));
                }
            }
        }
    }
}
