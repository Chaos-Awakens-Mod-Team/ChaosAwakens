package io.github.chaosawakens.api.animation.faal.entity;

import io.github.chaosawakens.api.animation.faal.base.WrappedAnimatable;

public interface WrappedAnimatableEntity extends WrappedAnimatable {

    void tickClientAnimations();
    void tickServerAnimations();

    void tickGeneralClient();

    double getMovementThreshold();
}
