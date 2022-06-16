package io.github.chaosawakens.common.entity.ai;

import java.util.EnumSet;

import java.util.function.BiFunction;

import io.github.chaosawakens.common.entity.AnimatableMonsterEntity;
import io.github.chaosawakens.common.entity.robo.RoboPounderEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;

public class AnimatableRoboPunchGoal extends AnimatableGoal {
	protected final double animationLength;
	protected final BiFunction<Double, Double, Boolean> attackPredicate;
	private boolean hasHit;
	public static float punchingTicks;

	public AnimatableRoboPunchGoal(AnimatableMonsterEntity entity, RoboPounderEntity robo, double animationLength, double attackBegin, double attackEnd) {
		this.entity = entity;
		this.roboPounder = robo;
		this.animationLength = animationLength;
//		AnimatableRoboPunchGoal.punchingTicks = (float) (attackEnd - attackBegin);
		this.attackPredicate = (progress, length) -> attackBegin < progress / (length) && progress / (length) < attackEnd;
		this.setFlags(EnumSet.of(Goal.Flag.LOOK));
	}

	public static double getAttackReachSq(AnimatableMonsterEntity attacker, LivingEntity target) {
		return attacker.getBbWidth() * 2F * attacker.getBbWidth() * 2F + target.getBbWidth() / 2;
	}

	private static boolean checkIfValid(AnimatableRoboPunchGoal animatableRoboPunchGoal, RoboPounderEntity attacker, LivingEntity target) {
		if (target == null) return false;
		if (target.isAlive() && !target.isSpectator()) {
			if (target instanceof PlayerEntity && ((PlayerEntity) target).isCreative()) {
				attacker.setAttacking(false);
				return false;
			}
			double distance = animatableRoboPunchGoal.roboPounder.distanceToSqr(target.getX(), target.getY(), target.getZ());
//			if (punchingTicks == 0) return false;
			if (distance <= getAttackReachSq(attacker, target)) return true;
		}
		attacker.setAttacking(false);
//        attacker.setPunching(false);
		return false;
	}

	/*
	 * public boolean canPunchTarget(Entity target) {
	 *	 if (!roboPounder.isAggressive()) return false;
	 *	 if (!roboPounder.canSee(target)) return false;
	 * 
	 * Vector3d attackerVector = new Vector3d(roboPounder.getX(), roboPounder.getEyeY(), roboPounder.getZ()); Vector3d targetVector = new Vector3d(target.getX(), target.getEyeY(), target.getZ());
	 * 
	 * if (target.level != roboPounder.level || targetVector.distanceToSqr(attackerVector) > 128.0D * 128.0D) return false;
	 * 
	 * return roboPounder.level.clip(new RayTraceContext(attackerVector, targetVector, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, roboPounder)).getType() == RayTraceResult.Type.MISS; }
	 */

	@Override
	public boolean canUse() {
		if (Math.random() <= 0.1) return false;
        return AnimatableRoboPunchGoal.checkIfValid(this, roboPounder, this.roboPounder.getTarget()) && !this.roboPounder.getNavigation().isDone();
    }

	@Override
	public boolean canContinueToUse() {
		if (Math.random() <= 0.1) return true;
		return AnimatableRoboPunchGoal.checkIfValid(this, roboPounder, this.roboPounder.getTarget()) && !this.roboPounder.getNavigation().isDone();
	}

	@Override
	public void start() {
		this.roboPounder.setAttacking(true);
		this.roboPounder.setAggressive(true);
		this.roboPounder.setPunching(true);
		this.animationProgress = 0;
		AnimatableRoboPunchGoal.punchingTicks = 0;
    }

	@Override
	public void stop() {
		LivingEntity target = this.roboPounder.getTarget();
		if (!EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(target)) {
			this.entity.setTarget(null);
		}
		this.roboPounder.setAttacking(false);
		this.roboPounder.setAggressive(false);
		this.roboPounder.setPunching(false);
		this.hasHit = false;
		this.animationProgress = 0;
		AnimatableRoboPunchGoal.punchingTicks = 0;
	}

	public void animateAttack(LivingEntity target) {

	}

	@Override
	public void tick() {
		this.baseTick();
		LivingEntity target = this.roboPounder.getTarget();
		if (target != null) {
//			ChaosAwakens.LOGGER.debug("GOAL", this.animationProgress + " " + this.animationLength + " " + this.tickDelta + " " + this.animationProgress/this.animationLength);
			if (this.attackPredicate.apply(this.animationProgress, this.animationLength) && !this.hasHit) {
				this.roboPounder.lookAt(target, 30.0F, 30.0F);
				if (this.roboPounder.distanceTo(target) >= 12.0F) {
					this.roboPounder.getTarget().moveTo(target.blockPosition(), 3.0F, 10.0F);
					this.roboPounder.getLookControl().setLookAt(target, 30.0F, 30.0F);
//					this.entity.swing(Hand.MAIN_HAND);
//					this.entity.doHurtTarget(target);
//					this.hasHit = true;
				}
//				AnimatableRoboPunchGoal.punchingTicks += this.animationProgress;
//				ChaosAwakens.LOGGER.debug(AnimatableRoboPunchGoal.punchingTicks);
//				if (!canPunchTarget(target)) return;
				this.roboPounder.swing(Hand.MAIN_HAND);
				this.roboPounder.doHurtTarget(target);
				this.hasHit = true;
			}

			if (this.animationProgress > this.animationLength) {
				this.animationProgress = 0;
//				AnimatableRoboPunchGoal.punchingTicks = 0;
				this.hasHit = false;
//				this.roboPounder.setMoving(true);
			}
		}
	}
}
