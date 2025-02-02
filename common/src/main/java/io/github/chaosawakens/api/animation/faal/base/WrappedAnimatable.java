package io.github.chaosawakens.api.animation.faal.base;

import io.github.chaosawakens.api.animation.faal.entity.WrappedAnimatableEntity;
import io.github.chaosawakens.api.animation.faal.item.WrappedAnimatableItem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;

/**
 * The base {@code interface} representing any type of object that wants to be animated utilising VKF and FAAL. This interface contains the lowest-level implementations required to
 * implement animatable objects of any given type. When applied to fleshed-out objects on its own, this is effectively useless. See this interface's implementations if you want to
 * work with natively supported (or otherwise existing) animatable types.
 *
 * @see WrappedAnimatableEntity
 * @see WrappedAnimatableItem
 */
public interface WrappedAnimatable {

    void playAnimation(String animationStateName, boolean forcePose);
    void stopAnimation(String animationStateName);

    default void playAnimation(String animationStateName) {
        playAnimation(animationStateName, false);
    }

    void playAnimation(ExtendedAnimationState animationState, boolean forcePose);
    void stopAnimation(ExtendedAnimationState animationState);

    default void playAnimation(ExtendedAnimationState animationState) {
        playAnimation(animationState, false);
    }

    default ExtendedAnimationState wrapState(String animationName) {
        return wrapState(animationName, -1);
    }

    ExtendedAnimationState wrapState(String animationName, int tickDuration);

    @Nullable
    ExtendedAnimationState getAnimation(String animName);

    ObjectArrayList<ExtendedAnimationState> getCachedAnimationStates();

    default boolean requiresServerAnimationTicking() {
        return true;
    }
}
