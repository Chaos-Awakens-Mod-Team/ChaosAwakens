package io.github.chaosawakens.common.entity.ai.goals.hostile.robo.robopounder;

import io.github.chaosawakens.api.animation.SingletonAnimationBuilder;
import io.github.chaosawakens.common.entity.hostile.robo.RoboPounderEntity;
import io.github.chaosawakens.common.entity.misc.CAScreenShakeEntity;
import io.github.chaosawakens.common.registry.CAEffects;
import io.github.chaosawakens.common.registry.CASoundEvents;
import io.github.chaosawakens.common.registry.CATags;
import io.github.chaosawakens.common.util.BlockPosUtil;
import io.github.chaosawakens.common.util.EntityUtil;
import io.github.chaosawakens.common.util.MathUtil;
import io.github.chaosawakens.common.util.ObjectUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.function.Supplier;

public class RoboPounderRageRunGoal extends Goal {
	private final RoboPounderEntity owner;
	private final Supplier<SingletonAnimationBuilder> rageBeginAnim;
	private final Supplier<SingletonAnimationBuilder> rageRunAnim;
	private final Supplier<SingletonAnimationBuilder> rageCooldownAnim;
	private final Supplier<SingletonAnimationBuilder> rageRestartAnim;
	private final Supplier<SingletonAnimationBuilder> rageCrashAnim;
	private final Supplier<SingletonAnimationBuilder> rageCrashRestartAnim;
	private final byte rageRunAttackId;
	private final int presetMaxCooldown;
	private int curCooldown;
	private final int probability;
	private BlockPos targetRageRunPos;
	private boolean isPathingRageRun;
	private boolean hasPlayedCooldownSound = false;
	private boolean hasPlayedCrashSound = false;
	private boolean hasPlayedRestartSound = false;
	private boolean hasPlayedCrashRestartSound = false;
	private boolean foundCrashCollision = false;
	private int targetInterval = 0;
	private int pathTries = 0;
	private double base = -0.8D;

	public RoboPounderRageRunGoal(RoboPounderEntity owner, Supplier<SingletonAnimationBuilder> rageBeginAnim, Supplier<SingletonAnimationBuilder> rageRunAnim, Supplier<SingletonAnimationBuilder> rageCooldownAnim, Supplier<SingletonAnimationBuilder> rageRestartAnim, Supplier<SingletonAnimationBuilder> rageCrashAnim, Supplier<SingletonAnimationBuilder> rageCrashRestartAnim, byte rageRunAttackId, int presetMaxCooldown, int probability) {
		this.owner = owner;
		this.rageBeginAnim = rageBeginAnim;
		this.rageRunAnim = rageRunAnim;
		this.rageCooldownAnim = rageCooldownAnim;
		this.rageRestartAnim = rageRestartAnim;
		this.rageCrashAnim = rageCrashAnim;
		this.rageCrashRestartAnim = rageCrashRestartAnim;
		this.rageRunAttackId = rageRunAttackId;
		this.presetMaxCooldown = presetMaxCooldown;
		this.probability = probability;
	}
	
	public RoboPounderRageRunGoal(RoboPounderEntity owner, Supplier<SingletonAnimationBuilder> rageBeginAnim, Supplier<SingletonAnimationBuilder> rageRunAnim, Supplier<SingletonAnimationBuilder> rageCooldownAnim, Supplier<SingletonAnimationBuilder> rageRestartAnim, Supplier<SingletonAnimationBuilder> rageCrashAnim, Supplier<SingletonAnimationBuilder> rageCrashRestartAnim, byte rageRunAttackId, int presetMaxCooldown) {
		this(owner, rageBeginAnim, rageRunAnim, rageCooldownAnim, rageRestartAnim, rageCrashAnim, rageCrashRestartAnim, rageRunAttackId, presetMaxCooldown, 1);
	}
	
	public RoboPounderRageRunGoal(RoboPounderEntity owner, Supplier<SingletonAnimationBuilder> rageBeginAnim, Supplier<SingletonAnimationBuilder> rageRunAnim, Supplier<SingletonAnimationBuilder> rageCooldownAnim, Supplier<SingletonAnimationBuilder> rageRestartAnim, Supplier<SingletonAnimationBuilder> rageCrashAnim, Supplier<SingletonAnimationBuilder> rageCrashRestartAnim, byte rageRunAttackId, Integer probability) {
		this(owner, rageBeginAnim, rageRunAnim, rageCooldownAnim, rageRestartAnim, rageCrashAnim, rageCrashRestartAnim, rageRunAttackId, 400, probability);
	}
	
	public RoboPounderRageRunGoal(RoboPounderEntity owner, Supplier<SingletonAnimationBuilder> rageBeginAnim, Supplier<SingletonAnimationBuilder> rageRunAnim, Supplier<SingletonAnimationBuilder> rageCooldownAnim, Supplier<SingletonAnimationBuilder> rageRestartAnim, Supplier<SingletonAnimationBuilder> rageCrashAnim, Supplier<SingletonAnimationBuilder> rageCrashRestartAnim, byte rageRunAttackId) {
		this(owner, rageBeginAnim, rageRunAnim, rageCooldownAnim, rageRestartAnim, rageCrashAnim, rageCrashRestartAnim, rageRunAttackId, 400, 1);
	}

	@Override
	public boolean canUse() {
		if (curCooldown > 0) curCooldown--;
		
		return ObjectUtil.performNullityChecks(false, owner, owner.getTarget()) && !owner.isOnAttackCooldown() && curCooldown <= 0 && !owner.getTarget().isInvulnerable()
				&& owner.isAlive() && !owner.isAttacking() && owner.getTarget().isAlive()
				&& (owner.getHealth() <= owner.getMaxHealth() / 2 || owner.distanceTo(owner.getTarget()) > owner.getMeleeAttackReach() * 5) && owner.distanceTo(owner.getTarget()) <= owner.getFollowRange()
				&& owner.shouldRageRunBasedOnChance() && owner.getRandom().nextInt(probability) == 0;
	}
	
	@Override
	public boolean canContinueToUse() {
		return ObjectUtil.performNullityChecks(false, owner) && !owner.isDeadOrDying() && (rageRestartAnim.get().isPlaying() ? !rageRestartAnim.get().hasAnimationFinished() : !rageCrashRestartAnim.get().hasAnimationFinished());
	}
	
	@Override
	public boolean isInterruptable() {
		return owner.isDeadOrDying();
	}
	
	@Override
	public void start() { //TODO Figure out how to get this to always tick
		owner.setAttackID(rageRunAttackId);
		owner.setRageRunning(true);
		owner.getNavigation().stop();
		owner.setRageRunAttributes();
		owner.playAnimation(rageBeginAnim.get(), true);

		owner.level.playSound(null, owner.blockPosition(), CASoundEvents.ROBO_POUNDER_RAGE_RUN_WINDUP.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
		CAScreenShakeEntity.shakeScreen(owner.level, owner.position(), 55F, 0.14F, 22, 16);

		this.isPathingRageRun = false;
		this.hasPlayedCooldownSound = false;
		this.hasPlayedRestartSound = false;
		this.hasPlayedCrashSound = false;
		this.hasPlayedCrashRestartSound = false;
		this.foundCrashCollision = false;
		this.targetInterval = 0;
		this.pathTries = 0;
		this.base = -0.8D;
	}
	
	@Override
	public void stop() {
		owner.setAttackID((byte) 0);
		owner.setAttackCooldown(10);
		owner.setRageRunning(false);
		owner.resetAttributes();
		owner.getNavigation().stop();
		
		this.curCooldown = presetMaxCooldown;
	}
	
	private void createRageRunPath() {
		if (!owner.isPlayingAnimation(rageRunAnim.get())) {
			owner.getNavigation().stop();
			return;
		}
		
		PathNavigator ownerPathNav = owner.getNavigation();
		Path rageRunPath = null;
		Vector3d relevantLookPos = null;
		
		if (owner.getTarget() != null && owner.getTarget().isAlive() && rageRunAnim.get().isPlaying()) {
			if (targetRageRunPos == null || owner.distanceToSqr(Vector3d.atCenterOf(targetRageRunPos)) <= 4.0D || !isPathingRageRun || !owner.isMoving()) {
				this.targetRageRunPos = BlockPosUtil.findHorizontalPositionBeyond(owner, owner.getTarget().blockPosition(), MathHelper.nextInt(owner.getRandom(), 12, 15));
				this.isPathingRageRun = true;
			}
			
			if (targetRageRunPos != null) {
				relevantLookPos = new Vector3d(targetRageRunPos.getX(), targetRageRunPos.getY(), targetRageRunPos.getZ());
			}
		}
		
		if (ObjectUtil.performNullityChecks(false, relevantLookPos)) {
			owner.getMoveControl().setWantedPosition(targetRageRunPos.getX() + 0.5D + (12 * owner.getLookAngle().x), targetRageRunPos.getY(), targetRageRunPos.getZ() + 0.5D + (12 * owner.getLookAngle().z), 0.8F);
			owner.moveRelative(0.02F, new Vector3d(owner.xxa, owner.yya, owner.zza));
			owner.move(MoverType.SELF, owner.getDeltaMovement());
			
			if (owner.isPlayingAnimation(rageRunAnim.get())) owner.getLookControl().setLookAt(relevantLookPos);

			if (targetRageRunPos != null && owner.distanceToSqr(Vector3d.atCenterOf(targetRageRunPos)) <= 4.0D) {
				this.pathTries++;
				this.isPathingRageRun = false;
			}
		}

		if ((owner.getTarget() == null && ++targetInterval >= 10) || owner.getRageRunDuration() <= 0 || (owner.getNavigation().isStuck()) || pathTries >= 125) {
			owner.stopAnimation(rageRunAnim.get());
			owner.playAnimation(rageCooldownAnim.get(), true);
		}
	}
	
	private void affectTargets() {
		LivingEntity target = owner.getTarget();
		
		if (target != null) {
			double reach = owner.getMeleeAttackReach();
			List<LivingEntity> potentialAffectedTargets = EntityUtil.getAllEntitiesAround(owner, reach, reach, reach, reach);
			
			for (LivingEntity potentialAffectedTarget : potentialAffectedTargets) {
				if (owner.isAlliedTo(potentialAffectedTarget) || owner.getClass() == potentialAffectedTarget.getClass()) continue;
				
				double targetAngle = MathUtil.getRelativeAngleBetweenEntities(owner, potentialAffectedTarget);
				double attackAngle = owner.yBodyRot % 360;
				
				if (targetAngle < 0) targetAngle += 360;
				if (attackAngle < 0) attackAngle += 360;
				
				double relativeHitAngle = targetAngle - attackAngle;
				float hitDistanceSqr = (float) (Math.sqrt((target.getZ() - owner.getZ()) * (target.getZ() - owner.getZ()) + (target.getX() - owner.getX()) * (target.getX() - owner.getX())) - owner.getBbWidth() / 2F);
				
				if (hitDistanceSqr <= reach && MathUtil.isWithinAngleRestriction(relativeHitAngle, 460.0D)) owner.doHurtTarget(potentialAffectedTarget);
			}
		}
	}
	
	private void handleRageCrash() {
		Iterable<BlockPos> collisionBlocks = BlockPos.betweenClosed((int) Math.round(owner.getBoundingBox().minX), (int) Math.round(owner.getBoundingBox().minY) + 1, (int) Math.round(owner.getBoundingBox().minZ), (int) Math.round(owner.getBoundingBox().maxX), (int) Math.round(owner.getBoundingBox().maxY), (int) Math.round(owner.getBoundingBox().maxZ));

		if (owner.fluidOnEyes != null || owner.hasEffect(CAEffects.PARALYSIS_EFFECT.get())) this.foundCrashCollision = true;
		else if (owner.horizontalCollision) {
			float maxUpStep = owner.maxUpStep;

			for (BlockPos detectedPos : collisionBlocks) {
				if (detectedPos == null) continue;

				BlockState detectedState = owner.level.getBlockState(detectedPos);

				if (detectedState.isAir(owner.level, detectedPos) || detectedState.getCollisionShape(owner.level, detectedPos).isEmpty()) continue;

				double blockHeight = Math.abs(detectedState.getCollisionShape(owner.level, detectedPos).max(Direction.Axis.Y) - owner.getY());
				
				if (((detectedState.is(CATags.Blocks.POUNDER_IMMUNE) || owner.isInWater() || owner.isInLava()) && blockHeight > maxUpStep) || !owner.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
					this.foundCrashCollision = true;
					break;
				}
			}
		}
		
		if (foundCrashCollision) {
			CAScreenShakeEntity.shakeScreen(owner.level, owner.position(), 20.0F, 0.2F, 20, 60);
			
			owner.level.playSound(null, owner.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundCategory.HOSTILE, 1.0F, MathHelper.nextFloat(owner.getRandom(), 0.7F, 0.8F));
			
			for (BlockPos targetPos : collisionBlocks) {
				if (owner.level instanceof ServerWorld) ((ServerWorld) owner.level).sendParticles(new BlockParticleData(ParticleTypes.BLOCK, owner.level.getBlockState(targetPos)), targetPos.getX() + 0.5D, targetPos.getY() + 0.5D, targetPos.getZ() + 0.5D, owner.getRandom().nextInt(20), 0, 0, 0, 1);
			}
			
			owner.playAnimation(rageCrashAnim.get(), true);

			if (!hasPlayedCrashSound) {
				owner.level.playSound(null, owner.blockPosition(), CASoundEvents.ROBO_POUNDER_RAGE_RUN_CRASH.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
				this.hasPlayedCrashSound = true;
			}

			owner.setDeltaMovement(0, owner.getDeltaMovement().y, 0);
			owner.getNavigation().stop();
			EntityUtil.freezeEntityRotation(owner);
		} else BlockPosUtil.destroyCollidingBlocksWithOffset(owner, false, 0.07D, 0, 0.07D, (targetBlock) -> !targetBlock.is(CATags.Blocks.POUNDER_IMMUNE));
	}
	
	private boolean shouldExitCooldown() { // Inlining this would be a crime to the eyes (Oh The Irony:tm:)
		if (rageCrashAnim.get().isPlaying()) return rageCrashAnim.get().getWrappedAnimProgress() >= MathHelper.nextDouble(owner.getRandom(), rageCrashAnim.get().getWrappedAnimLength() / 1.5D, rageCrashAnim.get().getWrappedAnimLength() / 1.167D);
		else return rageCooldownAnim.get().isPlaying() && rageCooldownAnim.get().getWrappedAnimProgress() >= MathHelper.nextDouble(owner.getRandom(), rageCooldownAnim.get().getWrappedAnimLength() / 1.44D, rageCooldownAnim.get().getWrappedAnimLength() / 1.367D);
	}

	private boolean hasEffectivelyBegunRageRun() {
		return rageBeginAnim.get().isPlaying() && rageBeginAnim.get().getWrappedAnimProgress() >= MathHelper.nextDouble(owner.getRandom(), rageBeginAnim.get().getWrappedAnimLength() / 1.7D, rageBeginAnim.get().getWrappedAnimLength() / 1.2D);
	}
	
	@Override
	public void tick() {
		owner.stopAnimation(owner.getWalkAnim());
		owner.stopAnimation(owner.getIdleAnim());
		
		LivingEntity target = owner.getTarget();
		boolean hasCharged = false;
		boolean hasExitedCooldown = false;
		
		if (rageBeginAnim.get().isPlaying() || rageRestartAnim.get().isPlaying()) {
			owner.getNavigation().stop();
			EntityUtil.freezeEntityRotation(owner);
		}
		
		if (rageCrashAnim.get().isPlaying()) {
			owner.setDeltaMovement(0, owner.getDeltaMovement().y, 0);
			owner.getNavigation().stop();
			EntityUtil.freezeEntityRotation(owner);
		}
		
		if (hasEffectivelyBegunRageRun()) {
			if (target != null) {
				owner.playAnimation(rageRunAnim.get(), false);
				createRageRunPath();
			}
		}
		
		if (rageRunAnim.get().isPlaying()) {			
			createRageRunPath();
			affectTargets();
			handleRageCrash();

			if (owner.tickCount % 8 == 0) CAScreenShakeEntity.shakeScreen(owner.level, owner.position(), 35.0F, 0.08F, 5, 20);
		}

		if (rageCooldownAnim.get().isPlaying()) {
			owner.getNavigation().stop();

			if (!hasCharged) {
			//	EntityUtil.chargeTowards(owner, BlockPosUtil.findHorizontalPositionBeyond(owner, targetRageRunPos, owner.getRageRunFrictionOffset()), 12.0D, 4.5D, 0.035D);

				if (!hasPlayedCooldownSound) {
					owner.level.playSound(null, owner.blockPosition(), CASoundEvents.ROBO_POUNDER_RAGE_RUN_COOLDOWN.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);

					this.hasPlayedCooldownSound = true;
				}

				hasCharged = true;
			}

			double mod = Math.min(owner.getRageRunFrictionOffset() / 250.0D, 0.09D); // Hundredth decimal for scalar/power mod

			base += mod;

			double xMod = Math.min(base, 0) * Math.cos(Math.toRadians(owner.yRot - 90)); // Power * direction (duh, that's what a vector is, basically)
			double zMod = Math.min(base, 0) * Math.sin(Math.toRadians(owner.yRot - 90));

			owner.setDeltaMovement(xMod, owner.getDeltaMovement().y, zMod);
		}
		
		if (shouldExitCooldown() && !hasExitedCooldown) {
			if (owner.isPlayingAnimation(rageCooldownAnim.get())) {
				owner.playAnimation(rageRestartAnim.get(), false);

				if (!hasPlayedRestartSound) {
					owner.level.playSound(null, owner.blockPosition(), CASoundEvents.ROBO_POUNDER_RAGE_RUN_RESTART.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
					this.hasPlayedRestartSound = true;
				}
			} else if (owner.isPlayingAnimation(rageCrashAnim.get())) {
				owner.playAnimation(rageCrashRestartAnim.get(), false);

				if (!hasPlayedCrashRestartSound) {
					owner.level.playSound(null, owner.blockPosition(), CASoundEvents.ROBO_POUNDER_RAGE_RUN_CRASH_RESTART.get(), SoundCategory.HOSTILE, 1.0F, 1.0F);
					this.hasPlayedCrashRestartSound = true;
				}
			}
			hasExitedCooldown = true;
		}
	}
}