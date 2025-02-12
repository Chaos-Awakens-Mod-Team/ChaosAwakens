package io.github.chaosawakens.api.animation.faal.base;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

import java.util.function.Consumer;

/**
 * The base {@code interface} responsible for providing blueprint methods utilised by all classes extending/implementing {@link AnimationState}.
 */
public interface ExtendedAnimationState {

    /**
     * Gets the animation name (arbitrarily decided) by which this instance is identified.
     *
     * @return This instance's arbitrarily-set animation name. May be formatted with pretty much any UTF-8 characters.
     */
    String getAnimationName();

    /**
     * Gets the speed multiplier by which this animation instance is updated.
     *
     * @return The speed multiplier by which this animation instance is updated.
     */
    double getAnimationSpeedMultiplier();

    /**
     * Defines a speed multiplier by which progress is multiplied.
     *
     * @param animationSpeed The animation speed by which animation tick count is incremented/multiplied.
     *
     * @return {@code this} (builder method).
     */
    ExtendedAnimationState withAnimSpeed(double animationSpeed);

    /**
     * Gets the arbitrarily-set tick duration of this animation instance. This is controlled by the server and set on the client based on that.
     * <br></br>
     * If no duration is specified by the server (more specifically, this instance), the client assumes its own pre-defined length and the animation is not ticked (though it can be triggered and modified) on the server.
     *
     * @return This instance's arbitrarily-set animation tick duration.
     *
     * @see #getAnimationMilliDuration()
     * @see #getAnimationSecondDuration()
     */
    double getAnimationTickDuration();

    /**
     * Variant getter of {@link #getAnimationTickDuration()} that returns the current length in milliseconds.
     *
     * @return This instance's arbitrarily-set animation milli duration.
     *
     * @see #getAnimationTickDuration()
     * @see #getAnimationSecondDuration()
     */
    default long getAnimationMilliDuration() {
        return (long) getAnimationTickDuration() * 1000L / 20L;
    }

    /**
     * Variant getter of {@link #getAnimationTickDuration()} that returns the current length in seconds.
     *
     * @return This instance's arbitrarily-set animation second duration.
     *
     * @see #getAnimationTickDuration()
     * @see #getAnimationSecondDuration()
     */
    default double getAnimationSecondDuration() {
        return getAnimationTickDuration() / 20.0D;
    }

    /**
     * Starts this animation based on the provided {@code tickCount}. This is usually the parent {@linkplain Entity Entity's} (or otherwise object's) tick age.
     *
     * @param tickCount The value to use as delta for updating animation progress.
     *
     * @see Entity#tickCount
     */
    void start(int tickCount);

    /**
     * Effectively marks this animation instance as having been stopped.
     */
    void stop();

    /**
     * The method responsible for updating this instance's animation progress.
     * <br></br>
     * On the client, this is updated per-partial tick to provide more accurate frame time-based results. On the server, this is updated according to the server's tick-rate (generally 20 TPS).
     *
     * @param tickCount The {@code tickCount} used to set and updated the delta increment for this animation instance's millisecond progress.
     * @param speedMultiplier The speed multiplier (usually {@link #getAnimationSpeedMultiplier()}) by which the incremental update delta is multiplied.
     */
    void updateTime(float tickCount, float speedMultiplier);

    /**
     * Gets this animation instance's milli progress.
     * <br></br>
     * On the client, this provides an accurate millisecond reading since it's updated per partial tick/frame time. On the server, this usually returns thousand-divisible
     * millisecond reading based on the TPS (e.g. 1000, 2000, 3000 at 20, 40, and 60 ticks respectively).
     *
     * @return This animation instance's side-specific milli progress.
     *
     * @see #getAccumulatedTicks()
     * @see #getElapsedSeconds()
     */
    long getAccumulatedTime();

    /**
     * Variant getter of {@link #getAccumulatedTime()}. Gets this animation instance's tick progress.
     *
     * @return This animation instance's side-specific tick progress.
     *
     * @see #getAccumulatedTime()
     * @see #getElapsedSeconds()
     */
    default double getAccumulatedTicks() {
        return (double) getAccumulatedTime() / 1000L * 20L;
    }

    /**
     * Variant getter of {@link #getAccumulatedTime()}. Gets this animation instance's second progress.
     *
     * @return This animation instance's side-specific second progress.
     *
     * @see #getAccumulatedTime()
     * @see #getElapsedSeconds()
     */
    default double getElapsedSeconds() {
        return (double) getAccumulatedTime() / 1000L;
    }

    /**
     * Whether this animation instance is currently playing at all.
     * <br></br>
     * This is usually determined by a basic condition (e.g. {@code lastTime} being reset to {@link Long#MAX_VALUE}), though this is implementation-specific.
     *
     * @return Whether this animation instance is currently playing at all.
     */
    boolean isStarted();

    /**
     * Whether this animation instance may be ticked on the server.
     * <br></br>
     * By default, animation instances are ticked on the server if their length is greater than 0 (as specified in {@link #getAnimationTickDuration()}). Some implementations may override this
     * functionality to provide more fine-grained control based on their own needs.
     *
     * @return Whether this animation instance may be ticked on the server.
     */
    boolean isServerTickable();

    /**
     * Extended variant of {@link AnimationState#ifStarted(Consumer)} for EAS instances. Performs a certain task only if this animation instance is running.
     *
     * @param instance {@code this} instance.
     */
    void ifStartedExt(Consumer<ExtendedAnimationState> instance);
}
