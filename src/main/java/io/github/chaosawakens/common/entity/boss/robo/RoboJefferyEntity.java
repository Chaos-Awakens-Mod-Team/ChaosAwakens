package io.github.chaosawakens.common.entity.boss.robo;

import io.github.chaosawakens.api.animation.IAnimatableEntity;
import io.github.chaosawakens.api.animation.IAnimationBuilder;
import io.github.chaosawakens.api.animation.SingletonAnimationBuilder;
import io.github.chaosawakens.api.animation.WrappedAnimationController;
import io.github.chaosawakens.common.entity.ai.AnimatableMoveToTargetGoal;
import io.github.chaosawakens.common.entity.ai.goals.boss.robo.robojeffery.RoboJefferyShockwaveGoal;
import io.github.chaosawakens.common.entity.ai.goals.hostile.AnimatableLeapGoal;
import io.github.chaosawakens.common.entity.ai.goals.hostile.AnimatableMeleeGoal;
import io.github.chaosawakens.common.entity.base.AnimatableBossEntity;
import io.github.chaosawakens.common.entity.misc.AOEHitboxEntity;
import io.github.chaosawakens.common.entity.misc.CAScreenShakeEntity;
import io.github.chaosawakens.common.registry.CASoundEvents;
import io.github.chaosawakens.common.registry.CATags;
import io.github.chaosawakens.common.registry.CATeams;
import io.github.chaosawakens.common.util.EntityUtil;
import io.github.chaosawakens.common.util.MathUtil;
import io.github.chaosawakens.common.util.SoundUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.common.ForgeHooks;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Collection;

public class RoboJefferyEntity extends AnimatableBossEntity {
	private final AnimationFactory factory = new AnimationFactory(this);
	private final ObjectArrayList<WrappedAnimationController<RoboJefferyEntity>> roboJefferyControllers = new ObjectArrayList<WrappedAnimationController<RoboJefferyEntity>>(3);
	private final ObjectArrayList<IAnimationBuilder> roboJefferyAnimations = new ObjectArrayList<IAnimationBuilder>(1);
	private final ServerBossInfo bossInfo = (ServerBossInfo) new ServerBossInfo(getType().getDescription().copy().append(getName().copy().withStyle(TextFormatting.DARK_PURPLE).withStyle(TextFormatting.BOLD)), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS).setDarkenScreen(true).setCreateWorldFog(true);
	private static final DataParameter<Boolean> HAS_CORE = EntityDataManager.defineId(RoboJefferyEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> DROPPED_DEATH_LOOT = EntityDataManager.defineId(RoboJefferyEntity.class, DataSerializers.BOOLEAN); // Server lag failsafe
	private final WrappedAnimationController<RoboJefferyEntity> mainController = createMainMappedController("robojefferymaincontroller");
	private final WrappedAnimationController<RoboJefferyEntity> ambienceController = createMappedController("robojefferyambiencecontroller", this::ambiencePredicate);
	private final WrappedAnimationController<RoboJefferyEntity> secondaryAmbienceController = createMappedController("robojefferysecondaryambiencecontroller", this::secondaryAmbiencePredicate);
	private final WrappedAnimationController<RoboJefferyEntity> attackController = createMappedController("robojefferyattackcontroller", this::attackPredicate);
	private final SingletonAnimationBuilder idleAnim = new SingletonAnimationBuilder(this, "Idle", EDefaultLoopTypes.LOOP);
	private final SingletonAnimationBuilder idleExtrasAnim = new SingletonAnimationBuilder(this, "Idle Extras", EDefaultLoopTypes.LOOP).setWrappedController(ambienceController);
	private final SingletonAnimationBuilder lowHealthAnim = new SingletonAnimationBuilder(this, "Low Health", EDefaultLoopTypes.LOOP).setWrappedController(secondaryAmbienceController);
	private final SingletonAnimationBuilder highHealthAnim = new SingletonAnimationBuilder(this, "Healthy", EDefaultLoopTypes.LOOP).setWrappedController(secondaryAmbienceController);
	private final SingletonAnimationBuilder walkAnim = new SingletonAnimationBuilder(this, "Walk", EDefaultLoopTypes.LOOP).setAnimSpeed(0.98D);
	private final SingletonAnimationBuilder deathAnim = new SingletonAnimationBuilder(this, "Death", EDefaultLoopTypes.PLAY_ONCE).setWrappedController(attackController);
	private final SingletonAnimationBuilder leftPunchAnim = new SingletonAnimationBuilder(this, "Left Punch Attack", EDefaultLoopTypes.PLAY_ONCE).setWrappedController(attackController);
	private final SingletonAnimationBuilder rightPunchAnim = new SingletonAnimationBuilder(this, "Right Punch Attack", EDefaultLoopTypes.PLAY_ONCE).setWrappedController(attackController);
	private final SingletonAnimationBuilder smashAnim = new SingletonAnimationBuilder(this, "Smash Attack", EDefaultLoopTypes.PLAY_ONCE).setWrappedController(attackController);
	private final SingletonAnimationBuilder leapBeginAnim = new SingletonAnimationBuilder(this, "Leap Attack: Leap", EDefaultLoopTypes.PLAY_ONCE).setWrappedController(attackController);
	private final SingletonAnimationBuilder leapMidairAnim = new SingletonAnimationBuilder(this, "Leap Attack: Midair", EDefaultLoopTypes.LOOP).setWrappedController(attackController);
	private final SingletonAnimationBuilder leapLandAnim = new SingletonAnimationBuilder(this, "Leap Attack: Land", EDefaultLoopTypes.PLAY_ONCE).setWrappedController(attackController);
	private static final byte PUNCH_ATTACK_ID = 1;
	private static final byte SMASH_ATTACK_ID = 2;
	private static final byte LEAP_ATTACK_ID = 3;
	public static final String ROBO_JEFFERY_MDF_NAME = "robo_jeffery";

	public RoboJefferyEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
		return MobEntity.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, 600)
				.add(Attributes.ARMOR, 50)
				.add(Attributes.ARMOR_TOUGHNESS, 20)
				.add(Attributes.MOVEMENT_SPEED, 0.33D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 720.0D)
				.add(Attributes.ATTACK_DAMAGE, 45)
				.add(Attributes.ATTACK_KNOCKBACK, 25)
				.add(Attributes.FOLLOW_RANGE, 95);
	}

	@Override
	public ServerBossInfo getBossInfo() {
		return bossInfo;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public WrappedAnimationController<RoboJefferyEntity> getMainWrappedController() {
		return mainController;
	}

	@Override
	public <E extends IAnimatableEntity> PlayState mainPredicate(AnimationEvent<E> event) {
		if (isAttacking()) {
			playAnimation(idleAnim, true);
			return PlayState.CONTINUE;
		} else return PlayState.CONTINUE;
	}

	public <E extends IAnimatableEntity> PlayState ambiencePredicate(AnimationEvent<E> event) {
		return isDeadOrDying() ? PlayState.STOP : PlayState.CONTINUE;
	}
	
	public <E extends IAnimatableEntity> PlayState secondaryAmbiencePredicate(AnimationEvent<E> event) {
		return isDeadOrDying() ? PlayState.STOP : PlayState.CONTINUE;
	}

	public <E extends IAnimatableEntity> PlayState attackPredicate(AnimationEvent<E> event) {
		return PlayState.CONTINUE;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new WaterAvoidingRandomWalkingGoal(this, 1.2D) {
			@Override
			public boolean canUse() {
				return super.canUse() && !isAttacking() && !isOnAttackCooldown() && getTarget() == null;
			}

			@Override
			public boolean canContinueToUse() {
				return super.canContinueToUse() && !isAttacking() && !isOnAttackCooldown() && getTarget() == null;
			}
		});
		this.goalSelector.addGoal(0, new AnimatableMoveToTargetGoal(this, 1, 3));
		this.targetSelector.addGoal(0, new AnimatableMeleeGoal(this, null, PUNCH_ATTACK_ID, 140D, 15.6D, 18.1D, 20).pickBetweenAnimations(() -> leftPunchAnim, () -> rightPunchAnim).soundOnStart(CASoundEvents.ROBO_JEFFERY_JEFFERY_PUNCH::get, 1.0F));
		this.targetSelector.addGoal(0, new RoboJefferyShockwaveGoal(this, () -> smashAnim, SMASH_ATTACK_ID, 20D, 25D, 18D, 4, 3, 40).expSpeed(3.1D).soundOnStart(CASoundEvents.ROBO_JEFFERY_SEISMIC_SLAM::get, 1.0F));
		this.targetSelector.addGoal(0, new RoboJefferyShockwaveGoal(this, () -> smashAnim, SMASH_ATTACK_ID, 20D, 25D, 18D, 130, (owner) -> getRandom().nextInt(23) == 0 && MathUtil.isBetween(distanceTo(getTarget()), 0.1D, getMeleeAttackReach() * 5)).expSpeed(3.1D).soundOnStart(CASoundEvents.ROBO_JEFFERY_SEISMIC_SLAM::get, 1.0F));
		this.targetSelector.addGoal(0, new RoboJefferyShockwaveGoal(this, () -> smashAnim, SMASH_ATTACK_ID, 20D, 25D, 18D, 30, (owner) -> getRandom().nextInt(140) == 0 && MathUtil.isBetween(distanceTo(getTarget()), 0, getMeleeAttackReach() * 5) && getTarget().getMaxHealth() >= 150.0F).expSpeed(3.1D).soundOnStart(CASoundEvents.ROBO_JEFFERY_SEISMIC_SLAM::get, 1.0F));
		this.targetSelector.addGoal(0, new AnimatableLeapGoal(this, () -> leapBeginAnim, () -> leapMidairAnim, () -> leapLandAnim, LEAP_ATTACK_ID, 2.37345D, 25.0D, (owner) -> getRandom().nextInt(17) == 0).setLandAction((affectedTarget) -> {
			if (!(affectedTarget instanceof RoboJefferyEntity) && !affectedTarget.isAlliedTo(this)) {
				affectedTarget.hurt(DamageSource.mobAttack(this), 50.0F / distanceTo(affectedTarget));

				double targetAngle = (MathUtil.getAngleBetweenEntities(this, affectedTarget) + 90) * Math.PI / 180; //TODO Dist calc
				double kbMultiplier = affectedTarget instanceof PlayerEntity ? -4.7D : -2.7D;

				affectedTarget.setDeltaMovement(kbMultiplier * Math.cos(targetAngle), affectedTarget.getDeltaMovement().normalize().y + 0.47115D, kbMultiplier * Math.sin(targetAngle));
			}
		}).setBlockBreakPredicate((targetBlock) -> !targetBlock.is(CATags.Blocks.JEFFERY_IMMUNE)).setSound(CASoundEvents.ROBO_JEFFERY_PISTON_LEAP_LAUNCH, CASoundEvents.ROBO_JEFFERY_PISTON_LEAP_LAND, 1.0F));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, false));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<VillagerEntity>(this, VillagerEntity.class, false));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<IronGolemEntity>(this, IronGolemEntity.class, false));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<AnimalEntity>(this, AnimalEntity.class, false));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(HAS_CORE, true);
		this.entityData.define(DROPPED_DEATH_LOOT, false);
	}

	public boolean hasCore() {
		return this.entityData.get(HAS_CORE);
	}

	public void setHasCore(boolean hasCore) {
		this.entityData.set(HAS_CORE, hasCore);
	}

	public boolean hasDroppedDeathLoot() {
		return this.entityData.get(DROPPED_DEATH_LOOT);
	}

	public void setDroppedDeathLoot(boolean droppedDeathLoot) {
		this.entityData.set(DROPPED_DEATH_LOOT, droppedDeathLoot);
	}

	@Override
	public int animationInterval() {
		return 2;
	}

	@Override
	public void tick() {
		super.tick();

	/*	if (leapBeginAnim.isPlaying() && leapBeginAnim.hasAnimationFinished()) {
			for (int angle = 0; angle < 360; angle++) {
				level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, getX() + 0.5D, getY() + 1.0D, getZ() + 0.5D, Math.cos(angle) * 0.75D, 0, Math.sin(angle) * 0.75D);
			}
		} */
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (MathUtil.isBetween(leftPunchAnim.getWrappedAnimProgress(), 15.6D, 18.1D) || MathUtil.isBetween(rightPunchAnim.getWrappedAnimProgress(), 15.6D, 18.1D)) CAScreenShakeEntity.shakeScreen(level, position(), 15.0F, 0.1F, 5, 20); // Cause it's part of the AI n stuff :p
	}

	@Override
	protected void onSpawn(boolean hasAlreadyDied) {
		if (!hasAlreadyDied && level.isClientSide) {
			SoundUtil.playIdleSoundAsTickable(CASoundEvents.ROBO_JEFFERY_IDLE.get(), this);
			SoundUtil.playWalkingSoundAsTickable(CASoundEvents.ROBO_JEFFERY_WALK.get(), this);
		}
	}

	@Override
	protected void tickDeath() {
		EntityUtil.freezeEntityRotation(this);
		setAttackID((byte) 0);
		setAttackCooldown(0);
		setMoving(false);

		if (!isOnGround() && getAttackID() == LEAP_ATTACK_ID) return;
		else {
			DamageSource lastValidDamageSource = getLastDamageSource() == null ? DamageSource.GENERIC : getLastDamageSource();

			playAnimation(getDeathAnim(), false);

			if (getDeathAnim().hasAnimationFinished()) {
				remove();

				for (int i = 0; i < 20; ++i) {
					double xOffset = this.random.nextGaussian() * 0.02D;
					double yOffset = this.random.nextGaussian() * 0.02D;
					double zOffset = this.random.nextGaussian() * 0.02D;
					this.level.addParticle(ParticleTypes.POOF, getRandomX(1.0D), getRandomY(), getRandomZ(1.0D), xOffset, yOffset, zOffset);
				}
			}

			if (MathUtil.isBetween(deathAnim.getWrappedAnimProgress(), 29, 77)) CAScreenShakeEntity.shakeScreen(level, position(), 560F, (float) (deathAnim.getWrappedAnimProgress() / 100F) / 6, 2, 410);
			
			if (deathAnim.getWrappedAnimProgress() == 75) {
				CAScreenShakeEntity.shakeScreen(level, position(), 960F, 0.45F, 5, 480);
				
				AOEHitboxEntity deathHitBox = new AOEHitboxEntity(level, blockPosition(), 20.0F, 2.7F, 15, 3, null);
				
				deathHitBox.setActionOnIntersection((target) -> {
					if (!(target instanceof RoboJefferyEntity) && !target.isAlliedTo(this)) {
						target.hurt(DamageSource.mobAttack(this), 12.0F);

						double targetAngle = (MathUtil.getAngleBetweenEntities(deathHitBox, target) + 90) * Math.PI / 180; //TODO Dist calc
						double kbMultiplier = target instanceof PlayerEntity ? -5.7D : -2.7D;

						target.setDeltaMovement(kbMultiplier * Math.cos(targetAngle), target.getDeltaMovement().normalize().y + 0.7D, kbMultiplier * Math.sin(targetAngle));
					}
				});
				
				if (!level.isClientSide) level.addFreshEntity(deathHitBox);

				if (!hasDroppedDeathLoot()) {
					EntityUtil.handleAnimatableDeath(this, lastValidDamageSource, (owner) -> true, (owner) -> dropAllDeathLoot(lastValidDamageSource));
					setDroppedDeathLoot(true);
					setHasCore(false);
				}
			}

			if (deathAnim.getWrappedAnimProgress() < 75) EntityUtil.attractEntities(this, 20.0D, 20.0D, 0.08D + (deathAnim.getWrappedAnimProgress() / 1000), false);
		}
	}

	@Override
	protected void dropAllDeathLoot(DamageSource pDamageSource) {
		Entity dmgSrcEntity = pDamageSource.getEntity();
		int dmgSrcEntityLootingLevel = ForgeHooks.getLootingLevel(this, dmgSrcEntity, pDamageSource);

		captureDrops(new ObjectArrayList<>());

		if (shouldDropLoot() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
			dropFromLootTable(pDamageSource, true);
			dropCustomDeathLoot(pDamageSource, dmgSrcEntityLootingLevel, true);
		}

		dropEquipment();

		Collection<ItemEntity> allDrops = captureDrops(null);

		if (!ForgeHooks.onLivingDrops(this, pDamageSource, allDrops, dmgSrcEntityLootingLevel, lastHurtByPlayerTime > 0)) allDrops.forEach(targetItemEntity -> EntityUtil.spawnItemWithMotion(this, targetItemEntity.getItem(), new Vector3d(0, 1, 0))); //TODO recentlyHit moment
	}

	@Override
	public void manageAttack(LivingEntity target) {
		switch (getAttackID()) {
		case SMASH_ATTACK_ID:
			if (target instanceof PlayerEntity) EntityUtil.disableShield((PlayerEntity) target, 400);
			break;
		}
	}
	
	@Override
	public float getMeleeAttackReach() {
		return super.getMeleeAttackReach() * 0.88F;
	}

	@Override
	protected float getStandingEyeHeight(Pose pPose, EntitySize pSize) {
		return super.getStandingEyeHeight(pPose, pSize) + 0.34F;
	}

	@Nullable
	@Override
	public Team getTeam() {
		return CATeams.ROBO_TEAM;
	}

	@Override
	public boolean ignoreExplosion() {
		return true;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		SoundEvent standardDamageSound = CASoundEvents.ROBO_JEFFERY_DAMAGE_V1.get();
		SoundEvent criticalHitDamageSound = CASoundEvents.ROBO_JEFFERY_DAMAGE_V4.get();
		SoundEvent smashAttackingDamageSound = CASoundEvents.ROBO_JEFFERY_DAMAGE_V3.get();
		SoundEvent leapAttackingDamageSound = CASoundEvents.ROBO_JEFFERY_DAMAGE_V2.get();
		float lastDamageAmount = getLastDamageAmount();

		return isDeadOrDying() ? CASoundEvents.ROBO_JEFFERY_DEATH.get() : lastDamageAmount >= 50.0F ? criticalHitDamageSound : getAttackID() == SMASH_ATTACK_ID ? smashAttackingDamageSound : getAttackID() == LEAP_ATTACK_ID ? leapAttackingDamageSound : standardDamageSound;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return CASoundEvents.ROBO_JEFFERY_DEATH.get();
	}

	@Override
	protected float getSoundVolume() {
		return super.getSoundVolume() + 1.25F;
	}

	@Override
	protected float getVoicePitch() {
		return isDeadOrDying() ? 1.015F : super.getVoicePitch();
	}

	@Override
	public SingletonAnimationBuilder getIdleAnim() {
		return idleAnim;
	}

	@Override
	public SingletonAnimationBuilder getWalkAnim() {
		return walkAnim;
	}

	@Override
	public SingletonAnimationBuilder getDeathAnim() {
		return deathAnim;
	}

	@Override
	public String getOwnerMDFileName() {
		return ROBO_JEFFERY_MDF_NAME;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectArrayList<WrappedAnimationController<RoboJefferyEntity>> getWrappedControllers() {
		return roboJefferyControllers;
	}
	
	@Override
	public ObjectArrayList<IAnimationBuilder> getCachedAnimations() {
		return roboJefferyAnimations;
	}
	
	@Override
	protected void handleBaseAnimations() {
		super.handleBaseAnimations();

		if (!isAttacking() && !isDeadOrDying() && getHealth() > 50.0F) playAnimation(idleExtrasAnim, false);
		if (getHealth() <= 50.0F && !isDeadOrDying()) playAnimation(lowHealthAnim, false);
		else stopAnimation(lowHealthAnim);

		if (isDeadOrDying()) {
			stopAnimation(idleExtrasAnim);
			stopAnimation(highHealthAnim);
			stopAnimation(lowHealthAnim);
		}
	}
}