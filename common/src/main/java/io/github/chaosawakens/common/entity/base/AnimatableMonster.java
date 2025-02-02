package io.github.chaosawakens.common.entity.base;

import io.github.chaosawakens.api.animation.faal.base.ExtendedAnimationState;
import io.github.chaosawakens.api.animation.faal.base.WrappedAnimation;
import io.github.chaosawakens.api.animation.faal.entity.WrappedAnimatableEntity;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.common.entity.ai.body_rotation_control.BandaidBodyRotationControl;
import io.github.chaosawakens.common.entity.ai.move_control.ReinforcedMoveControl;
import io.github.chaosawakens.common.entity.ai.path_navigation.DirectGroundPathNavigation;
import io.github.chaosawakens.common.networking.packets.s2c.AnimationPlayPacket;
import io.github.chaosawakens.common.networking.packets.s2c.AnimationStopPacket;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AnimatableMonster extends Monster implements WrappedAnimatableEntity {
    private static final EntityDataAccessor<Byte> ATTACK_ID = SynchedEntityData.defineId(AnimatableMonster.class, EntityDataSerializers.BYTE);
    private final ObjectArrayList<ExtendedAnimationState> cachedAnimationStates = new ObjectArrayList<>();
    public static final byte NO_ATTACK_ID = 0;
    protected int customDeathTime = 20;
    protected float yDeathRot = 0.0F;
    private boolean requiresServerAnimTicking = true;

    protected AnimatableMonster(EntityType<? extends AnimatableMonster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        setMaxUpStep(1.0F);
        this.moveControl = new ReinforcedMoveControl(this);
    }

    public abstract boolean isFunctionallyAnimatingAttack();

    @Override
    public boolean requiresServerAnimationTicking() {
        return requiresServerAnimTicking;
    }

    @Override
    public ExtendedAnimationState wrapState(String animationName, int tickDuration) {
        ExtendedAnimationState defState = new WrappedAnimation(animationName, tickDuration);

        if (!cachedAnimationStates.contains(defState)) cachedAnimationStates.add(defState);

        return defState;
    }

    @Override
    public void playAnimation(String animationStateName, boolean forcePose) {
        Optional<ExtendedAnimationState> targetAnimOptional = getCachedAnimationStates().stream().filter(curAnimState -> curAnimState.getAnimationName().equals(animationStateName)).findFirst();

        targetAnimOptional.ifPresent(targetAnim -> {
            if (!level().isClientSide()) {
                CAServices.NETWORK_MANAGER.sendToTrackingClients(new AnimationPlayPacket(getId(), animationStateName, forcePose), this);

                if (targetAnim.isServerTickable() && !targetAnim.isStarted()) targetAnim.start(tickCount);
            } else { // Extra guard check
                if (forcePose) getCachedAnimationStates().stream().filter(curAnimState -> !curAnimState.getAnimationName().equals(animationStateName)).forEach(this::stopAnimation);
                if (!targetAnim.isStarted()) targetAnim.start(tickCount);
            }
        });
    }

    @Override
    public @Nullable ExtendedAnimationState getAnimation(String animName) {
        return getCachedAnimationStates().stream().filter(curAnimState -> curAnimState.getAnimationName().equals(animName)).findFirst().get();
    }

    @Override
    public void stopAnimation(String animationStateName) {
        Optional<ExtendedAnimationState> targetAnimOptional = getCachedAnimationStates().stream().filter(curAnimState -> curAnimState.getAnimationName().equals(animationStateName)).findFirst();

        targetAnimOptional.ifPresent(targetAnim -> {
            if (!level().isClientSide()) {
                CAServices.NETWORK_MANAGER.sendToTrackingClients(new AnimationStopPacket(getId(), animationStateName), this);

                if (targetAnim.isServerTickable() && targetAnim.isStarted()) targetAnim.stop();
            } else targetAnim.stop();
        });
    }

    @Override
    public void playAnimation(ExtendedAnimationState animationState, boolean forcePose) {
        playAnimation(animationState.getAnimationName(), forcePose);
    }

    @Override
    public void stopAnimation(ExtendedAnimationState animationState) {
        stopAnimation(animationState.getAnimationName());
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new BandaidBodyRotationControl(this);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new DirectGroundPathNavigation(this, pLevel);
    }

    public boolean canBeKnockedBack() {
        return true;
    }

    public int getDeathDuration() {
        return 20;
    }

    public boolean useCustomDeathTime() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_ID, NO_ATTACK_ID);
    }

    public byte getAttackId() {
        return this.entityData.get(ATTACK_ID);
    }

    public void setAttackId(byte attackId) {
        this.entityData.set(ATTACK_ID, attackId);
    }

    public void resetAttackId() {
        setAttackId(NO_ATTACK_ID);
    }

    public boolean isAttacking() {
        return getAttackId() != NO_ATTACK_ID && !isDeadOrDying();
    }

    public boolean isAttackingStatically() {
        return getAttackId() != NO_ATTACK_ID;
    }

    public boolean isStuck() {
        double dx = getX() - xo;
        double dz = getZ() - zo;
        double dxSqr = dx * dx;
        double dzSqr = dz * dz;

        return dxSqr + dzSqr < getMovementThreshold();
    }

    public boolean isMoving() {
        return !isStuck();
    }

    @Override
    public double getMovementThreshold() {
        return 2.500000277905201E-7;
    }

    public double getCustomDeathTime() {
        return customDeathTime;
    }

    @Override
    public void tick() {
        if (level().isClientSide()) {
            tickClientAnimations();
            tickGeneralClient();
        } else {
            if (requiresServerAnimationTicking()) {
                if (cachedAnimationStates.stream().noneMatch(ExtendedAnimationState::isServerTickable)) this.requiresServerAnimTicking = false;

                cachedAnimationStates.stream()
                        .filter(curAnimState -> curAnimState.isServerTickable() && curAnimState.isStarted())
                        .peek(curAnimState -> {
                            if (curAnimState.getAccumulatedTicks() >= curAnimState.getAnimationTickDuration()) stopAnimation(curAnimState);
                        })
                        .forEach(curAnimState -> curAnimState.updateTime(tickCount, (float) curAnimState.getAnimationSpeedMultiplier()));
            }

            tickServerAnimations();
        }

        super.tick();
    }

    @Override
    protected void tickDeath() {
        setYRot(yDeathRot);
        setYHeadRot(yDeathRot);

        if (useCustomDeathTime()) customDeathTime++;
        else deathTime++;

        if ((useCustomDeathTime() ? customDeathTime : deathTime) >= getDeathDuration() && !level().isClientSide() && !isRemoved()) {
            level().broadcastEntityEvent(this, EntityEvent.POOF);
            remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() != null && !canBeKnockedBack()) {
            super.hurt(pSource, pAmount);
            return false;
        }

        return super.hurt(pSource, pAmount);
    }

    @Override
    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);

        this.yDeathRot = getYRot();
    }

    @Override
    public boolean isPushable() {
        return canBeKnockedBack();
    }

    @Override
    public void push(double pX, double pY, double pZ) {
        if (!canBeKnockedBack()) return;
        super.push(pX, pY, pZ);
    }

    @Override
    public void knockback(double pStrength, double pRatioX, double pRatioZ) {
        if (!canBeKnockedBack()) return;
        super.knockback(pStrength, pRatioX, pRatioZ);
    }

    @Override
    public ObjectArrayList<ExtendedAnimationState> getCachedAnimationStates() {
        return cachedAnimationStates;
    }
}
