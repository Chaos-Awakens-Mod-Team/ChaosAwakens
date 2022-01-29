package io.github.chaosawakens.common.entity;

import io.github.chaosawakens.common.config.CAConfig;
import io.github.chaosawakens.common.registry.CAEntityTypes;
import io.github.chaosawakens.common.registry.CAItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class EnchantedGoldenCarrotPigEntity extends AnimalEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CARROT, Items.POTATO, Items.BEETROOT);

    public EnchantedGoldenCarrotPigEntity(EntityType<? extends EnchantedGoldenCarrotPigEntity> type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 10);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.carrot_pig.walking_animation", true));
            return PlayState.CONTINUE;
        }
        if (!event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.carrot_pig.idle_animation", true));
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        if (CAConfig.COMMON.enableEnchantedAnimalBreeding.get()) {
            this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
            this.goalSelector.addGoal(3, new TemptGoal(this, 1.2D, Ingredient.of(Items.CARROT_ON_A_STICK), false));
            this.goalSelector.addGoal(3, new TemptGoal(this, 1.2D, false, FOOD_ITEMS));
            this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        }
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    public boolean isFood(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIG_AMBIENT;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.PIG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIG_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.PIG_STEP, 0.15F, 1.0F);
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    public void aiStep() {
        if (this.level.isClientSide) {
            this.level.addParticle(ParticleTypes.INSTANT_EFFECT, false, this.getRandomX(0.5D), this.getY(0.85D), this.getRandomZ(0.5D), -1000, -1000, -1000);
        }
        super.aiStep();
    }

    @Override
    public EnchantedGoldenCarrotPigEntity getBreedOffspring(ServerWorld world, AgeableEntity mate) {
        return CAConfig.COMMON.enableEnchantedAnimalBreeding.get() ? CAEntityTypes.ENCHANTED_GOLDEN_CARROT_PIG.get().create(world) : null;
    }

    @Override
    public boolean canFallInLove() {
        return CAConfig.COMMON.enableEnchantedAnimalBreeding.get();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "enchantedgoldencarrtotpigcontroller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public ActionResultType mobInteract(PlayerEntity playerEntity, Hand hand) {
        ItemStack itemstack = playerEntity.getItemInHand(hand);
        if (itemstack.getItem() == Items.SHEARS && this.readyForShearing()) {
            this.shear(SoundCategory.PLAYERS);
            if (!this.level.isClientSide) {
                itemstack.hurtAndBreak(1, playerEntity, (p_213442_1_) -> p_213442_1_.broadcastBreakEvent(hand));
            }

            return ActionResultType.sidedSuccess(this.level.isClientSide);
        } else {
            return super.mobInteract(playerEntity, hand);
        }
    }

    public void shear(SoundCategory soundCategory) {
        this.level.playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, soundCategory, 1.0F, 1.0F);
        if (!this.level.isClientSide()) {
            ((ServerWorld)this.level).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5D), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            this.remove();
            PigEntity pigEntity = EntityType.PIG.create(this.level);
            assert pigEntity != null;
            pigEntity.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
            pigEntity.setHealth(this.getHealth());
            pigEntity.yBodyRot = this.yBodyRot;
            if (this.hasCustomName()) {
                pigEntity.setCustomName(this.getCustomName());
                pigEntity.setCustomNameVisible(this.isCustomNameVisible());
            }

            if (this.isPersistenceRequired()) {
                pigEntity.setPersistenceRequired();
            }

            pigEntity.setInvulnerable(this.isInvulnerable());
            this.level.addFreshEntity(pigEntity);

            this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0D), this.getZ(), new ItemStack(CAItems.ENCHANTED_GOLDEN_CARROT.get())));
        }
    }

    public boolean readyForShearing() {
        return this.isAlive() && !this.isBaby();
    }
}
