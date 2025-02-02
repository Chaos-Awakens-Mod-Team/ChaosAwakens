package io.github.chaosawakens.client.model.base;

import io.github.chaosawakens.api.animation.faal.base.ExtendedAnimationState;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public abstract class WrappedHierarchicalModel<E extends Entity> extends HierarchicalModel<E> {

    public WrappedHierarchicalModel(Function<ResourceLocation, RenderType> pRenderType) {
        super(pRenderType);
    }

    public WrappedHierarchicalModel() {
        super(RenderType::entityCutoutNoCull);
    }

    @Override
    public void setupAnim(E pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);

        applyHeadRotation(pNetHeadYaw, pHeadPitch);
    }

    @Override
    protected void animate(AnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks, float pSpeed) {
        animate(pAnimationState, pAnimationDefinition, pAnimationState.getAccumulatedTime(), 1.0F, pAgeInTicks, pSpeed);
    }

    protected void animate(AnimationState animState, AnimationDefinition animDefinition, long animTimeMilli, float interpolationScale, float tickAge, float animSpeedMultiplier) {
        animState.updateTime(tickAge, animSpeedMultiplier);
        animState.ifStarted(curState -> KeyframeAnimations.animate(this, animDefinition, animTimeMilli, interpolationScale, ANIMATION_VECTOR_CACHE));
    }

    protected void animate(ExtendedAnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks) {
        animate(pAnimationState, pAnimationDefinition, pAgeInTicks, (float) pAnimationState.getAnimationSpeedMultiplier());
    }

    protected void animate(ExtendedAnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks, float pSpeed) {
        animate(pAnimationState, pAnimationDefinition, pAnimationState.getAccumulatedTime(), 1.0F, pAgeInTicks, pSpeed);
    }

    protected void animate(ExtendedAnimationState animState, AnimationDefinition animDefinition, long animTimeMilli, float interpolationScale, float tickAge, float animSpeedMultiplier) {
        animState.updateTime(tickAge, animSpeedMultiplier);
        animState.ifStartedExt(curState -> KeyframeAnimations.animate(this, animDefinition, animTimeMilli, interpolationScale, ANIMATION_VECTOR_CACHE));
    }

    protected void applyHeadRotation(float pNetHeadYaw, float pHeadPitch) {
        if (head() != null) {
            Objects.requireNonNull(head()).yRot = pNetHeadYaw * ((float) Math.PI / 180F);
            Objects.requireNonNull(head()).xRot = pHeadPitch * ((float) Math.PI / 180F);
        }
    }

    @Nullable
    public abstract ModelPart head();
}
