package io.github.chaosawakens.common.entity;

import java.util.Random;

import io.github.chaosawakens.common.registry.CAEntityTypes;
import io.github.chaosawakens.common.registry.CAItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class SparkFishEntity extends AbstractFishEntity implements IAnimatable{
	private final AnimationFactory factory = new AnimationFactory(this);
	private static final DataParameter<Boolean> FROM_BUCKET = EntityDataManager.defineId(SparkFishEntity.class, DataSerializers.BOOLEAN);

	public SparkFishEntity(EntityType<? extends AbstractFishEntity> p_i48855_1_, World p_i48855_2_) {
		super(p_i48855_1_, p_i48855_2_);
		this.setPathfindingMalus(PathNodeType.WATER, 1.0F);
		this.moveControl = new SparkFishEntity.MoveHelperController(this);
		this.noCulling = true;
	}
	
    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED,1.3D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 4.0D);
    }
    
    @Override
    public int getMaxAirSupply() {
    	return 1500;
    }

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}
	
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "sparkfishcontroller", 0, this::predicate));
	}
	
	@Override
	protected void registerGoals() {
	      this.goalSelector.addGoal(0, new PanicGoal(this, 1.05D));
	      this.goalSelector.addGoal(0, new LookAtGoal(this, PlayerEntity.class, 3.0F, 3.0F));
	      this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 4.0F, 0.8D, 0.7D, EntityPredicates.NO_SPECTATORS::test));
	      this.goalSelector.addGoal(4, new SparkFishEntity.SwimGoal(this));
	}
	
	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.spark_fish.swim", true));
            return PlayState.CONTINUE;
        }
        if (!event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.spark_fish.swim", true));
            this.animationSpeed = 0.5F;
            return PlayState.CONTINUE;
        }
        if(this.isSwimming()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.spark_fish.swim", true));
        }
        return PlayState.CONTINUE;
    }
	
	static class MoveHelperController extends MovementController {
	      private final SparkFishEntity fish;

	      MoveHelperController(SparkFishEntity sparkfish) {
	         super(sparkfish);
	         this.fish = sparkfish;
	      }

	      public void tick() {
	         if (this.fish.isEyeInFluid(FluidTags.WATER)) {
	            this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
	         }

	         if (this.operation == MovementController.Action.MOVE_TO && !this.fish.getNavigation().isDone()) {
	            float f = (float)(this.speedModifier * this.fish.getAttributeValue(Attributes.MOVEMENT_SPEED));
	            this.fish.setSpeed(MathHelper.lerp(0.125F, this.fish.getSpeed(), f));
	            double d0 = this.wantedX - this.fish.getX();
	            double d1 = this.wantedY - this.fish.getY();
	            double d2 = this.wantedZ - this.fish.getZ();
	            if (d1 != 0.0D) {
	               double d3 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
	               this.fish.setDeltaMovement(this.fish.getDeltaMovement().add(0.0D, (double)this.fish.getSpeed() * (d1 / d3) * 0.1D, 0.0D));
	            }

	            if (d0 != 0.0D || d2 != 0.0D) {
	               float f1 = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
	               this.fish.yRot = this.rotlerp(this.fish.yRot, f1, 90.0F);
	               this.fish.yBodyRot = this.fish.yRot;
	            }

	         } else {
	            this.fish.setSpeed(0.0F);
	         }
	      }
	   }
	
	@Override
	public boolean doHurtTarget(Entity e) {
		return super.doHurtTarget(e);
	}

	   static class SwimGoal extends RandomSwimmingGoal {
	      private static SparkFishEntity fish;

	      public SwimGoal(SparkFishEntity sparkfish) {
	         super(sparkfish, 1.0D, 40);
	         SwimGoal.fish = sparkfish;
	      }

	      public boolean canUse() {
	         return SwimGoal.fish.canRandomSwim() && super.canUse();
	      }
	   }
	   
	  @Override
	  public void aiStep() {
		   if (!this.isInWater() && this.onGround && this.verticalCollision) {
		      this.setDeltaMovement(this.getDeltaMovement().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), (double)0.4F, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
		      this.onGround = false;
		      this.hasImpulse = true;
		      this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
		   }

		   super.aiStep();
     }
	  
	  @Override
	public EntityType<?> getType() {
		return CAEntityTypes.SPARK_FISH.get();
	}
	  
	  @Override
	  protected ActionResultType mobInteract(PlayerEntity player, Hand hand) {
	      ItemStack itemstack = player.getItemInHand(hand);
	      if (itemstack.getItem() == Items.WATER_BUCKET && this.isAlive()) {
	         this.playSound(SoundEvents.BUCKET_FILL_FISH, 1.0F, 1.0F);
	         itemstack.shrink(1);
	         ItemStack itemstack1 = this.getBucketItemStack();
	         this.saveToBucketTag(itemstack1);
	         if (!this.level.isClientSide) {
	            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)player, itemstack1);
	         }

	         if (itemstack.isEmpty()) {
	            player.setItemInHand(hand, itemstack1);
	         } else if (!player.inventory.add(itemstack1)) {
	            player.drop(itemstack1, false);
	         }

	         this.remove();
	         return ActionResultType.sidedSuccess(this.level.isClientSide);
	      } else {
	         return super.mobInteract(player, hand);
	      }
	   }

	  @Override
	   protected void saveToBucketTag(ItemStack stack) {
	      if (this.hasCustomName()) {
	         stack.setHoverName(this.getCustomName());
	      }
	   }
	  
	  public static boolean checkFishSpawnRules(EntityType<? extends AbstractFishEntity> woodfish, IWorld world, SpawnReason reason, BlockPos pos, Random rand) {
	      return world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER);
	   }
	  
	  @Override
	public int getMaxSpawnClusterSize() {
		return 6;
	}
	  
	  @Override
	   public boolean removeWhenFarAway(double distance) {
	      return !this.fromBucket() && !this.hasCustomName();
	   }
	  
	  @Override
	   protected void defineSynchedData() {
	      super.defineSynchedData();
	      this.entityData.define(FROM_BUCKET, false);
	   }

	   public boolean fromBucket() {
	      return this.entityData.get(FROM_BUCKET);
	   }

	   @Override
	   public void setFromBucket(boolean frombucket) {
	      this.entityData.set(FROM_BUCKET, frombucket);
	   }

	   @Override
	   public void addAdditionalSaveData(CompoundNBT bucketnbt) {
	      super.addAdditionalSaveData(bucketnbt);
	      bucketnbt.putBoolean("FromBucket", this.fromBucket());
	   }

	   @Override
	   public void readAdditionalSaveData(CompoundNBT bucketnbt) {
	      super.readAdditionalSaveData(bucketnbt);
	      this.setFromBucket(bucketnbt.getBoolean("FromBucket"));
	   }
	  
	  @Override
	  public boolean requiresCustomPersistence() {
		  return super.requiresCustomPersistence() || this.fromBucket();
	  }
	  
	  @Override
	   protected PathNavigator createNavigation(World world) {
		      return new SwimmerPathNavigator(this, world);
		   }

	     @Override
		   public void travel(Vector3d vector) {
		      if (this.isEffectiveAi() && this.isInWater()) {
		         this.moveRelative(0.01F, vector);
		         this.move(MoverType.SELF, this.getDeltaMovement());
		         this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
		         if (this.getTarget() == null) {
		            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
		         }
		      } else {
		         super.travel(vector);
		      }

		   }
		   
			public CreatureAttribute getMobType() {
				return CreatureAttribute.WATER;
			}	

	@Override
	protected ItemStack getBucketItemStack() {
		return new ItemStack(CAItems.SPARK_FISH_BUCKET.get());
	}

	@Override
	protected SoundEvent getFlopSound() {
		return SoundEvents.COD_FLOP;
	}
	
	@Override
	protected SoundEvent getSwimSound() {
		return SoundEvents.FISH_SWIM;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.COD_AMBIENT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.COD_DEATH;
	}
	
	@Override
	protected float getStandingEyeHeight(Pose pose, EntitySize size) {
		 return this.isBaby() ? size.height * 0.45F : 0.6F;
	}

}
