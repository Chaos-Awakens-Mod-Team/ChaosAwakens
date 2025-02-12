package io.github.chaosawakens.api.animation.faal.base;

/**
 * FAAL's implementation of more robust/central animation management.
 * <br></br>
 * This iteration of FAAL aims to unify both VKF and GeckoLib animation registration and modification. Such animations are either data-driven (as in the case of GeckoLib and some Modded Entity animations), whereas others
 * are hardcoded (primarily standard Vanilla Keyframe, hereby referred to as VKF, animations).
 * <br></br>
 * In terms of bringing GeckoLib support into the mix, Chaos Awakens has its own parser and interpreter of GeckoLib animations. That is, more specifically, a revised implementation of GeckoLib's animation state management
 * by means of optimization and end-developer implementation without necessarily bringing some of the cons GeckoLib suffers from (inadequate animation control outside the model/renderer, render optimizations, etc.).
 * <br></br>
 * The idea behind this concept is to allow for further iterations/updates made to FAAL to support more complex ideas, such as inverse kinematics, without needing to hack into the existing GeckoLib source code and potentially
 * break other mods using GeckoLib. In other words, animations here may either be baked keyframe animations that can be considered forward kinematics at best, or they may be data-driven dynamic animations primarily defined
 * by mathematical functions and molang that support inverse kinematics if need be.
 * <br></br>
 * Another key point to take note of is the added support of item/block-entity vanilla keyframe animations. Since those objects seldom require the complexity of inverse kinematics and/or per-frame computations,
 * baked pre-calculated keyframe animations are natively supported in order to reduce the overhead introduced by manually caching and re-computing their keyframes.
 * <br></br>
 * In addition to added VKF support for items and block entities, support for Bézier curves and step interpolation has also been added. The end-developer may choose to either bake these animations or compute them per-frame
 * as they wish.
 * <br></br>
 * Now, to actually define the usage of this class, a comprehensive code block (as seen below) describes the process behind animation registration and management:
 */
public final class AnimationManager {
}
