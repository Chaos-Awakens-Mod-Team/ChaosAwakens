package io.github.chaosawakens.common.entity.projectile;

import io.github.chaosawakens.common.config.CAConfig;
import io.github.chaosawakens.common.entity.RoboEntity;
import io.github.chaosawakens.common.registry.CAEntityTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class RoboExplosionLaserEntity extends DamagingProjectileEntity {
    private float damage;

    public RoboExplosionLaserEntity(EntityType<RoboExplosionLaserEntity> entityType, World worldIn) {
        super(entityType, worldIn);
    }

    public RoboExplosionLaserEntity(World worldIn, RoboEntity p_i47273_2_) {
        this(CAEntityTypes.ROBO_EXPLOSION_LASER.get(), worldIn);
        super.setOwner(p_i47273_2_);
        this.setPos(p_i47273_2_.getX() - (double) (p_i47273_2_.getBbWidth() + 1.0F) * 0.5D * (double) MathHelper.sin(p_i47273_2_.yBodyRot * ((float) Math.PI / 180F)), p_i47273_2_.getEyeY() - (double) 0.1F, p_i47273_2_.getZ() + (double) (p_i47273_2_.getBbWidth() + 1.0F) * 0.5D * (double) MathHelper.cos(p_i47273_2_.yBodyRot * ((float) Math.PI / 180F)));
    }

    @OnlyIn(Dist.CLIENT)
    public RoboExplosionLaserEntity(World worldIn, double x, double y, double z, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_) {
        this(CAEntityTypes.ROBO_EXPLOSION_LASER.get(), worldIn);
        this.setPos(x, y, z);

        for (int i = 0; i < 7; ++i) {
            double d0 = 0.4D + 0.1D * (double) i;
            worldIn.addParticle(ParticleTypes.CRIT, x, y, z, p_i47274_8_ * d0, p_i47274_10_, p_i47274_12_ * d0);
        }
        this.setDeltaMovement(p_i47274_8_, p_i47274_10_, p_i47274_12_);
    }

    public RoboExplosionLaserEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(CAEntityTypes.ROBO_LASER.get(), shooter, accelX, accelY, accelZ, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        Vector3d vector3d = this.getDeltaMovement();
        RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
        if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
            this.onHit(raytraceresult);
        }
        this.setSecondsOnFire(0);
        double d0 = this.getX() + vector3d.x;
        double d1 = this.getY() + vector3d.y;
        double d2 = this.getZ() + vector3d.z;
        this.updateRotation();
        if (this.level.getBlockStates(this.getBoundingBox()).noneMatch(AbstractBlock.AbstractBlockState::isAir)) {
            this.remove();
        } else {
            this.setDeltaMovement(vector3d.scale(0.99F));
            this.setPos(d0, d1, d2);
        }
    }

    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());
            if (!(this.getEntity() instanceof RoboEntity) && flag) {
                if (random.nextInt(6) >= 3) {
                    float EXPLOSION_POWER = CAConfig.COMMON.rayGunExplosionSize.get();
                    boolean hasFire = CAConfig.COMMON.rayGunExplosionFire.get();
                    switch (CAConfig.COMMON.rayGunExplosionType.get()) {
                        case 0:
                            this.level.explode(null, this.getX(), this.getY(), this.getZ(), EXPLOSION_POWER, hasFire, Explosion.Mode.NONE);
                            break;
                        case 1:
                            this.level.explode(null, this.getX(), this.getY(), this.getZ(), EXPLOSION_POWER, hasFire, Explosion.Mode.BREAK);
                            break;
                        case 2:
                            this.level.explode(null, this.getX(), this.getY(), this.getZ(), EXPLOSION_POWER, hasFire, Explosion.Mode.DESTROY);
                            break;
                    }
                }
            }
            this.remove();
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        Entity entity = this.getOwner();
        if (entity instanceof RoboEntity) result.getEntity().hurt(DamageSource.indirectMobAttack(this, (RoboEntity) entity).setProjectile(), damage);
        super.onHitEntity(result);
    }

    @Override
    protected boolean shouldBurn() {
        return false; //TODO Make it not fiery
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
