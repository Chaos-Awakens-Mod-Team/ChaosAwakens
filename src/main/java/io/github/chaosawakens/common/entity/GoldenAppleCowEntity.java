package io.github.chaosawakens.common.entity;

import io.github.chaosawakens.common.registry.CABiomes;
import io.github.chaosawakens.common.registry.CAEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.server.ServerWorld;

public class GoldenAppleCowEntity extends AnimalEntity {
    public GoldenAppleCowEntity(EntityType<? extends GoldenAppleCowEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 10);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.COW_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    public void aiStep() {
        if (this.level.isClientSide) {
            this.level.addParticle(ParticleTypes.INSTANT_EFFECT, false, this.getRandomX(0.5D), this.getY(0.85D), this.getRandomZ(0.5D), -1000, -1000, -1000);
        }
        super.aiStep();
    }

    public ActionResultType mobInteract(PlayerEntity playerIn, Hand hand) {
        ItemStack itemstack = playerIn.getItemInHand(hand);
        if (itemstack.getItem() == Items.BUCKET && !this.isBaby()) {
            playerIn.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack itemstack1 = DrinkHelper.createFilledResult(itemstack, playerIn, Items.MILK_BUCKET.getDefaultInstance());
            playerIn.setItemInHand(hand, itemstack1);
            return ActionResultType.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(playerIn, hand);
        }
    }

    public GoldenAppleCowEntity getBreedOffspring(ServerWorld world, AgeableEntity mate) {
        return CAEntityTypes.GOLDEN_APPLE_COW.get().create(world);
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return this.isBaby() ? sizeIn.height * 0.95F : 1.3F;
    }

    public void thunderHit(ServerWorld serverWorld, LightningBoltEntity lightningBoltEntity) {
        if (net.minecraftforge.event.ForgeEventFactory.canLivingConvert(this, CAEntityTypes.GOLDEN_CARROT_PIG.get(), (timer) -> {})) {
            GoldenCarrotPigEntity goldenCarrotPigEntity = CAEntityTypes.GOLDEN_CARROT_PIG.get().create(serverWorld);
            assert goldenCarrotPigEntity != null;
            goldenCarrotPigEntity.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
            goldenCarrotPigEntity.setNoAi(this.isNoAi());
            goldenCarrotPigEntity.setBaby(this.isBaby());
            if (this.hasCustomName()) {
                goldenCarrotPigEntity.setCustomName(this.getCustomName());
                goldenCarrotPigEntity.setCustomNameVisible(this.isCustomNameVisible());
            }

            goldenCarrotPigEntity.setPersistenceRequired();
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, goldenCarrotPigEntity);
            serverWorld.addFreshEntity(goldenCarrotPigEntity);
            this.remove();
        } else {
            super.thunderHit(serverWorld, lightningBoltEntity);
        }
    }
}
