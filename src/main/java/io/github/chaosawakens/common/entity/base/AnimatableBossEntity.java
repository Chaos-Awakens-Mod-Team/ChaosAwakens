package io.github.chaosawakens.common.entity.base;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class AnimatableBossEntity extends AnimatableMonsterEntity {

	public AnimatableBossEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	public abstract ServerBossInfo getBossInfo();
	
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT pCompound) {
		super.readAdditionalSaveData(pCompound);

		if (hasCustomName()) getBossInfo().setName(getBossInfo().getName());
	}
	
	@Override
	public void setCustomName(ITextComponent pName) {
		super.setCustomName(pName);
		getBossInfo().setName(getBossInfo().getName());
	}

	@Override
	public void startSeenByPlayer(ServerPlayerEntity pServerPlayer) {
		super.startSeenByPlayer(pServerPlayer);
		getBossInfo().addPlayer(pServerPlayer);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayerEntity pServerPlayer) {
		super.stopSeenByPlayer(pServerPlayer);
		getBossInfo().removePlayer(pServerPlayer);
	}
	
	@Override
	public void tick() {
		super.tick();
		manageBossInfoHealthDisplay();
	}
	
	private void manageBossInfoHealthDisplay() {
		float percentageHealth = getHealth() / getMaxHealth();
		if (percentageHealth != getBossInfo().getPercent()) getBossInfo().setPercent(percentageHealth);
	}
	
	@Override
	protected boolean canRide(Entity vehicle) {
		return false;
	}

	@Override
	protected int calculateFallDamage(float f1, float f2) {
		return 0;
	}

	@Override
	public boolean isAffectedByFluids() {
		return false;
	}

	@Override
	public boolean isSuppressingBounce() {
		return false;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	public void push(double pX, double pY, double pZ) {
		if (!canBePushed()) return;
		else super.push(pX, pY, pZ);
	}

	@Override
	public void push(Entity pEntity) {
		if (!canBePushed()) return;
		else super.push(pEntity);
	}

	@Override
	public boolean canCollideWith(Entity pEntity) {
		return false;
	}

	@Override
	public void knockback(float pStrength, double pRatioX, double pRatioZ) {
		if (!canBePushed()) return;
		else super.knockback(pStrength, pRatioX, pRatioZ);
	}

	@Override
	protected void blockedByShield(LivingEntity entity) {
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
		return false;
	}

	@Override
	public boolean isPushable() {
		return !canBePushed() ? false : super.isPushable();
	}

	@Override
	protected float getJumpPower() {
		return 0;
	}
	
	@Override
	public boolean isPersistenceRequired() {
		return true;
	}
	
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
