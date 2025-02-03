package io.github.chaosawakens.common.entity.hostile.robo;

import io.github.chaosawakens.api.animation.faal.base.ExtendedAnimationState;
import io.github.chaosawakens.api.vfx.basic.ScreenShakeEffect;
import io.github.chaosawakens.common.entity.ai.goal.hostile.AnimatableAttackGoal;
import io.github.chaosawakens.common.entity.ai.goal.hostile.BandaidMoveToTargetGoal;
import io.github.chaosawakens.common.entity.base.AnimatableMonster;
import io.github.chaosawakens.util.EntityUtil;
import io.github.chaosawakens.util.MathUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class RoboPounder extends AnimatableMonster {
    public static final byte PISTON_PUNCH_ATTACK_ID = 1;
    public static final byte SIDE_SWEEP_ATTACK_ID = 2;
    public static final byte DYSON_DASH_ATTACK_ID = 3;
    public static final byte DOME_STOMP_ATTACK_ID = 4;
    public static final byte GROUND_SLAM_ATTACK_ID = 5;
    public static final byte RAGE_RUN_ATTACK_ID = 6;
    public static final String IDLE_ANIM = "Idle";
    public static final String DEATH_ANIM = "Death";
    public static final String LEFT_PISTON_PUNCH_ATTACK_ANIM = "Piston Punch Attack (Left)";
    public static final String RIGHT_PISTON_PUNCH_ATTACK_ANIM = "Piston Punch Attack (Right)";
    public static final String LEFT_SIDE_SWEEP_ATTACK_ANIM = "Side Sweep Attack (Left)";
    public static final String RIGHT_SIDE_SWEEP_ATTACK_ANIM = "Side Sweep Attack (Right)";
    public static final String DYSON_DASH_ATTACK_ANIM = "Dyson Dash Attack";
    public static final String LEFT_DOME_STOMP_ATTACK_ANIM = "Dome Stomp Attack (Right)";
    public static final String RIGHT_DOME_STOMP_ATTACK_ANIM = "Dome Stomp Attack (Right)";
    public static final String GROUND_SLAM_ATTACK_ANIM = "Ground Slam Attack";
    public final ExtendedAnimationState idleAnimState = wrapState(IDLE_ANIM);
    public final ExtendedAnimationState deathAnimState = wrapState(DEATH_ANIM);
    public final ExtendedAnimationState leftPistonPunchAttackAnim = wrapState(LEFT_PISTON_PUNCH_ATTACK_ANIM);
    public final ExtendedAnimationState rightPistonPunchAttackAnim = wrapState(RIGHT_PISTON_PUNCH_ATTACK_ANIM);
    public final ExtendedAnimationState leftSideSweepAttackAnim = wrapState(LEFT_SIDE_SWEEP_ATTACK_ANIM);
    public final ExtendedAnimationState rightSideSweepAttackAnim = wrapState(RIGHT_SIDE_SWEEP_ATTACK_ANIM);
    public final ExtendedAnimationState dysonDashAttackAnim = wrapState(DYSON_DASH_ATTACK_ANIM);
    public final ExtendedAnimationState leftDomeStompAttackAnim = wrapState(LEFT_DOME_STOMP_ATTACK_ANIM);
    public final ExtendedAnimationState rightDomeStompAttackAnim = wrapState(RIGHT_DOME_STOMP_ATTACK_ANIM);
    public final ExtendedAnimationState groundSlamAttackAnim = wrapState(GROUND_SLAM_ATTACK_ANIM);

    public RoboPounder(EntityType<? extends RoboPounder> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.noCulling = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300)
                .add(Attributes.ARMOR, 20)
                .add(Attributes.ARMOR_TOUGHNESS, 8)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 360.0D)
                .add(Attributes.ATTACK_DAMAGE, 20)
                .add(Attributes.ATTACK_KNOCKBACK, 10)
                .add(Attributes.FOLLOW_RANGE, 60);
    }

    @Override
    public boolean isFunctionallyAnimatingAttack() {
        return (leftPistonPunchAttackAnim.isStarted() && leftPistonPunchAttackAnim.getAccumulatedTime() <= 1950L) || (rightPistonPunchAttackAnim.isStarted() && rightPistonPunchAttackAnim.getAccumulatedTime() <= 1950L) || (leftSideSweepAttackAnim.isStarted() && leftSideSweepAttackAnim.getAccumulatedTime() <= 2160L) || (rightSideSweepAttackAnim.isStarted() && rightSideSweepAttackAnim.getAccumulatedTime() <= 2160L) || (leftDomeStompAttackAnim.isStarted() && leftDomeStompAttackAnim.getAccumulatedTime() <= 1770L) || (rightDomeStompAttackAnim.isStarted() && rightDomeStompAttackAnim.getAccumulatedTime() <= 1770L);
    }

    @Override
    protected void registerGoals() {
        targetSelector.addGoal(0, new HurtByTargetGoal(this));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractGolem.class, false));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));

        goalSelector.addGoal(0, new AnimatableAttackGoal<RoboPounder>(this, ObjectArrayList.of(() -> leftPistonPunchAttackAnim, () -> rightPistonPunchAttackAnim), 34, true, PISTON_PUNCH_ATTACK_ID)
                .attackArc(95.0D)
                .potentialTargetRadius(5.0D)
                .attackFrame(12.0D, 18.0D)
                .attackTickCooldown(20.0D)
                .initiationRange(4.0D)
                .additionalStartConditions(animatable -> animatable.getRandom().nextDouble() < 0.8D)
                .actionOnStart((animatable, target, potentialTargets, curTick) -> {
              //      animatable.playSound(getRandom().nextBoolean() ? DRSoundEvents.REDSTONE_GOLEM_SIDE_SWEEP_ATTACK_RIGHT.get() : DRSoundEvents.REDSTONE_GOLEM_SIDE_SWEEP_ATTACK_LEFT.get(), 1.0F, 1.0F);
                    animatable.stopAnimation(idleAnimState);
                })
                .actionOnAttack((animatable, target, potentialTargets, curTick) -> new ScreenShakeEffect(animatable.blockPosition(), 15.5D, 0.0028F, 12.5F, 1.0F).enqueue(animatable.level()))
                .actionOnEnd((animatable, target, potentialTargets, curTick) -> animatable.playAnimation(idleAnimState, true)));
        goalSelector.addGoal(0, new AnimatableAttackGoal<RoboPounder>(this, ObjectArrayList.of(() -> leftSideSweepAttackAnim, () -> rightSideSweepAttackAnim), 39, true, SIDE_SWEEP_ATTACK_ID)
                .attackArc(245.0D)
                .potentialTargetRadius(4.0D)
                .attackTickCooldown(30.0D)
                .attackFrame(12.0D, 20.0D)
                .initiationRange(3.0D)
                .additionalStartConditions(animatable -> animatable.getRandom().nextDouble() < 0.3D)
                .actionOnStart((animatable, target, potentialTargets, curTick) -> animatable.stopAnimation(idleAnimState))
                .actionOnEnd((animatable, target, potentialTargets, curTick) -> animatable.playAnimation(idleAnimState, true)));
        goalSelector.addGoal(0, new AnimatableAttackGoal<RoboPounder>(this, ObjectArrayList.of(() -> leftDomeStompAttackAnim, () -> rightDomeStompAttackAnim), 34, true, DOME_STOMP_ATTACK_ID)
                .performDefaultAttack(false)
                .forcePose(true)
                .potentialTargetRadius(7.0D)
                .attackFrame(7.0D, 13.0D)
                .attackTickCooldown(30.0D)
                .initiationRange(5.0D)
                .additionalStartConditions(animatable -> animatable.getRandom().nextDouble() < 0.6D && EntityUtil.getAllEntitiesAround(animatable, 7.0D, 5.0D, 7.0D, 7.0D).size() >= 2)
                .actionOnStart((animatable, target, potentialTargets, curTick) -> {
                    //      animatable.playSound(getRandom().nextBoolean() ? DRSoundEvents.REDSTONE_GOLEM_SIDE_SWEEP_ATTACK_RIGHT.get() : DRSoundEvents.REDSTONE_GOLEM_SIDE_SWEEP_ATTACK_LEFT.get(), 1.0F, 1.0F);
                    animatable.stopAnimation(idleAnimState);
                })
                .actionOnAttack((animatable, target, potentialTargets, curTick) -> {
                    if (curTick == 7.0D) animatable.playSound(SoundEvents.GENERIC_EXPLODE);

                    potentialTargets.forEach(animatable::doHurtTarget);
                    new ScreenShakeEffect(animatable.blockPosition(), 15.5D, 0.0028F, 12.5F, 1.0F).enqueue(animatable.level());
                })
                .actionOnEnd((animatable, target, potentialTargets, curTick) -> animatable.playAnimation(idleAnimState, true)));

        goalSelector.addGoal(0, new BandaidMoveToTargetGoal(this)
                .satisfactoryDist(3.5D));
        goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.2D, 0.2F) {
            @Override
            public boolean canUse() {
                return super.canUse() && getTarget() == null;
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && getTarget() == null;
            }
        });
    }

    @Override
    public int getDeathDuration() {
        return 110;
    }

    @Override
    public void tickClientAnimations() {
        if (!isMoving() && !isFunctionallyAnimatingAttack() && !isDeadOrDying()) playAnimation(idleAnimState);
        else if (isMoving()) stopAnimation(idleAnimState);

        if (isDeadOrDying()) playAnimation(deathAnimState, true);
        else stopAnimation(deathAnimState);
    }

    @Override
    public void tickServerAnimations() {

    }

    @Override
    public boolean canBeKnockedBack() {
        return false;
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    protected boolean canRide(@NotNull Entity vehicle) {
        return false;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected float getJumpPower() {
        return 0.0F;
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distToClosestPlayer) {
        return false;
    }

    @Override
    public void tickGeneralClient() {
        if (MathUtil.isBetween(deathAnimState.getElapsedSeconds(), 1.54D, 2.02D)) new ScreenShakeEffect(blockPosition(), 15.5D, 0.0028F, 12.5F, 1.0F).enqueue(level());
        if (MathUtil.isBetween(deathAnimState.getElapsedSeconds(), 2.71D, 3.17D)) new ScreenShakeEffect(blockPosition(), 15.5D, 0.0028F, 12.5F, 1.0F).enqueue(level());
    }
}
