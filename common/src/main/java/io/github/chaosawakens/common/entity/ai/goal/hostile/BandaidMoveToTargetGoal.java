package io.github.chaosawakens.common.entity.ai.goal.hostile;

import io.github.chaosawakens.common.entity.base.AnimatableMonster;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.BiConsumer;

public class BandaidMoveToTargetGoal extends Goal {
    protected final Mob owner;
    protected final double speedMultiplier;
    protected final int checkRate;
    protected int pathCheckRate;
    protected int failedIterations = 0;
    protected int maxFailedIterations = 20;
    protected double satisDist = 0.0D;
    @Nullable
    protected BiConsumer<Mob, LivingEntity> onFail;

    public BandaidMoveToTargetGoal(Mob owner, double speedMultiplier, int checkRate) {
        this.owner = owner;
        this.speedMultiplier = speedMultiplier;
        this.checkRate = checkRate;

        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    public BandaidMoveToTargetGoal(Mob owner, double speedMultiplier) {
        this(owner, speedMultiplier, 3);
    }

    public BandaidMoveToTargetGoal(Mob owner) {
        this(owner, 1.2D);
    }

    public BandaidMoveToTargetGoal satisfactoryDist(double dist) {
        this.satisDist = dist;
        return this;
    }

    public BandaidMoveToTargetGoal maxFailedIterations(int iterations) {
        this.maxFailedIterations = iterations;
        return this;
    }

    public BandaidMoveToTargetGoal onFail(BiConsumer<Mob, LivingEntity> onFail) {
        this.onFail = onFail;
        return this;
    }

    @Override
    public boolean canUse() {
        return isExecutable(owner.getTarget());
    }

    @Override
    public boolean canContinueToUse() {
        return isExecutable(owner.getTarget()) && owner.isWithinRestriction(owner.getTarget().blockPosition()) && failedIterations < maxFailedIterations && owner.distanceToSqr(owner.getTarget()) > (satisDist != 0.0D ? satisDist * satisDist : owner.getMeleeAttackRangeSqr(owner.getTarget()));
    }

    @Override
    public void start() {
        this.pathCheckRate = checkRate;
        this.failedIterations = 0;

        if (owner.getTarget() != null) owner.getNavigation().moveTo(owner.getTarget(), this.speedMultiplier);
    }

    @Override
    public void stop() {
        this.pathCheckRate = 1;
        LivingEntity target = this.owner.getTarget();

        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) this.owner.setTarget(null);
        if (onFail != null && failedIterations >= maxFailedIterations) onFail.accept(this.owner, target);

        this.owner.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.owner.getTarget();

        if (target == null || !EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target) || !target.isAlive()) return;
        if (this.owner.level().getGameTime() < 1 || this.owner.tickCount < 1) return;
        if (pathCheckRate > 0) pathCheckRate--;

        this.owner.getLookControl().setLookAt(target, 30F, 30F);

        if ((pathCheckRate <= 0 || owner.getNavigation().isDone() || owner.getNavigation().isStuck()) && owner.getSensing().hasLineOfSight(target) && owner.distanceToSqr(target) > (satisDist != 0.0D ? satisDist * satisDist : owner.getMeleeAttackRangeSqr(target))) {
            this.pathCheckRate = Mth.nextInt(owner.getRandom(), 4, 11);

            if (!owner.getNavigation().moveTo(owner.getTarget(), speedMultiplier)) this.pathCheckRate += 15;
        }
    }

    protected boolean isExecutable(LivingEntity target) {
        if (target == null) return false;
        if (target.isAlive() && !target.isSpectator()) {
            if (target instanceof Player playerTarget && playerTarget.isCreative()) return false;
            if (owner instanceof AnimatableMonster emOwner) return emOwner.getSensing().hasLineOfSight(target) && emOwner.distanceToSqr(target) > (satisDist != 0.0D ? satisDist * satisDist : emOwner.getMeleeAttackRangeSqr(target)) && !emOwner.isAttackingStatically();

            return owner.getSensing().hasLineOfSight(target);
        }
        return false;
    }
}
