package io.github.chaosawakens.api.animation.faal.base;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;

import java.util.function.Consumer;

/**
 * Extended variant of {@link AnimationState} that provides a basic control layer over animations from both the client and the server. Versatile in nature, meaning you can safely define behaviour segregated between the client
 * and the server.
 * <br></br>
 * This extension of {@link AnimationState} follows a leniency-first implementation. That is to say, the server has final control over general metadata such as animation speed, length, and modifying animation progress, but both
 * the server and the client are updated separately. Different implementations/extensions may otherwise follow stricter enforcement policies depending on their requirements. Although like this impl, they all aim for maximum
 * interface-ability with Vanilla.
 */
public class WrappedAnimation extends AnimationState implements ExtendedAnimationState {
    private static final long STOPPED = Long.MAX_VALUE;
    protected long lastTime = Long.MAX_VALUE;
    protected long accumulatedTime;
    protected final String animationName;
    protected final double animationTickDuration;
    protected double animationSpeed = 1.0D;

    public WrappedAnimation(String animationName, double animationTickDuration) {
        this.animationName = animationName;
        this.animationTickDuration = animationTickDuration;
    }

    /**
     * Defines a speed multiplier by which progress is multiplied.
     *
     * @param animationSpeed The animation speed by which animation tick count is incremented/multiplied.
     *
     * @return {@code this} (builder method).
     */
    public WrappedAnimation withAnimSpeed(double animationSpeed) {
        this.animationSpeed = animationSpeed;
        return this;
    }

    @Override
    public String getAnimationName() {
        return animationName;
    }

    @Override
    public double getAnimationSpeedMultiplier() {
        return animationSpeed;
    }

    @Override
    public double getAnimationTickDuration() {
        return animationTickDuration;
    }

    @Override
    public long getAnimationMilliDuration() {
        return (long) getAnimationTickDuration() * 1000L / 20L;
    }

    @Override
    public double getAnimationSecondDuration() {
        return getAnimationTickDuration() / 20.0D;
    }

    @Override
    public void start(int tickCount) {
        this.lastTime = (long) tickCount * 1000L / 20L;
        this.accumulatedTime = 0L;
    }

    @Override
    public void stop() {
        this.lastTime = STOPPED;
    }

    @Override
    public void updateTime(float tickCount, float speedMultiplier) {
        if (isStarted()) {
            long milliProgress = Mth.lfloor(tickCount * 1000.0D / 20.0D);

            this.accumulatedTime += (long) ((float) (milliProgress - lastTime) * speedMultiplier);
            this.lastTime = milliProgress;
        }
    }

    @Override
    public long getAccumulatedTime() {
        return accumulatedTime;
    }

    @Override
    public double getAccumulatedTicks() {
        return (double) getAccumulatedTime() / 1000L * 20L;
    }

    @Override
    public double getElapsedSeconds() {
        return (double) getAccumulatedTime() / 1000L;
    }

    @Override
    public boolean isStarted() {
        return lastTime != STOPPED;
    }

    @Override
    public boolean isServerTickable() {
        return animationTickDuration > 0;
    }

    @Override
    public void ifStartedExt(Consumer<ExtendedAnimationState> instance) {
        if (isStarted()) instance.accept(this);
    }
}
