package io.github.chaosawakens.common.item.misc;

import io.github.chaosawakens.util.MiscUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.entity.Entity.RemovalReason.DISCARDED;

public class CritterCageItem extends Item {

    public CritterCageItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Player owner = ctx.getPlayer();
        BlockPos targetPos = ctx.getClickedPos();
        Direction facingDirection = ctx.getClickedFace();
        Level curWorld = ctx.getLevel();
        ItemStack curStack = ctx.getItemInHand();

        if (!canReleaseEntity(owner, targetPos, facingDirection, curWorld, curStack)) return InteractionResult.FAIL;
        return InteractionResult.SUCCESS;
    }
    @Override
    public InteractionResult interactLivingEntity(ItemStack targetEmptyStack, Player playerOwner, LivingEntity target, InteractionHand hand) {
        ItemStack filledStack = getDefaultInstance();

        if (shouldCaptureEntity(filledStack, target)) {
            if (targetEmptyStack.getCount() == 1) {
                if (!containsEntity(targetEmptyStack)) playerOwner.setItemInHand(hand, filledStack);
                else return InteractionResult.FAIL;
            } else {
                if (!playerOwner.addItem(filledStack)) playerOwner.drop(filledStack, false, true);
                targetEmptyStack.shrink(1);
            }
            target.remove(DISCARDED);
            return InteractionResult.SUCCESS;
        } else return InteractionResult.FAIL;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (!containsEntity(stack)) return Component.translatable("item.chaosawakens.critter_cage");
        return Component.translatable("item.chaosawakens.critter_cage").append(" (" + getMobName(stack) + ")");
    }

    @Override
    public void appendHoverText(ItemStack targetStack, Level curWorld, List<Component> tooltip, TooltipFlag flag) {
        if (containsEntity(targetStack)) {
            LivingEntity target = (LivingEntity) getEntityFromStack(targetStack, curWorld, true);

            if (target == null) return;

            tooltip.add(Component.literal("Mob: ").withStyle(ChatFormatting.BLUE).append(Component.literal(getMobID(targetStack)).withStyle(ChatFormatting.GRAY)));
            tooltip.add(Component.literal("Mob Name: ").withStyle(ChatFormatting.BLUE).append(Component.literal(getMobName(targetStack)).withStyle(ChatFormatting.GRAY)));
            tooltip.add(Component.literal("Health: ").withStyle(ChatFormatting.BLUE).append(Component.literal(target.getHealth() + "/" + target.getMaxHealth()).withStyle(ChatFormatting.GRAY)));

            Arrays.stream(EquipmentSlot.values()).forEach(equipmentSlot -> {
                ItemStack armor = target.getItemBySlot(equipmentSlot);
                String slotName = switch (equipmentSlot) {
                    case MAINHAND -> "Main Hand";
                    case OFFHAND -> "Offhand";
                    case FEET -> "Boots";
                    case LEGS -> "Leggings";
                    case CHEST -> "Chestplate";
                    case HEAD -> "Helmet";
                };

                if (!armor.isEmpty()) tooltip.add(Component.literal(slotName + " Item: ").withStyle(ChatFormatting.BLUE).append(armor.getDisplayName()));
            });

            if (target.isBaby()) tooltip.add(Component.literal("Baby").withStyle(ChatFormatting.BLUE));

            if (target instanceof Villager villager) {
                tooltip.add(Component.literal("Profession: ").withStyle(ChatFormatting.BLUE).append(Component.literal(MiscUtil.capitalizeFirstLetter(villager.getVillagerData().getProfession().name())).withStyle(ChatFormatting.GRAY)));
                String toAppend = switch (villager.getVillagerData().getLevel()) {
                    case 1 -> "Novice";
                    case 2 -> "Apprentice";
                    case 3 -> "Journeyman";
                    case 4 -> "Expert";
                    case 5 -> "Master";
                    default -> "";
                };
                if (villager.getVillagerData().getProfession().equals(VillagerProfession.NONE)) toAppend = "None";
                if (!toAppend.isEmpty()) tooltip.add(Component.literal("Trading Level: ").withStyle(ChatFormatting.BLUE).append(Component.literal(toAppend).withStyle(ChatFormatting.GRAY)));
            }

            if (target instanceof TamableAnimal targetTamable) {
                tooltip.add(Component.literal("Owner: ").withStyle(ChatFormatting.BLUE).append(Component.literal(targetTamable.getOwner() instanceof Player player ? player.getScoreboardName() : "None").withStyle(ChatFormatting.GRAY)));

                if (target instanceof Wolf targetWolf) {
                    tooltip.add(Component.literal("Collar Color: ").withStyle(ChatFormatting.BLUE).append(Component.literal((targetWolf.isTame() ? MiscUtil.capitalizeFirstLetter(targetWolf.getCollarColor().toString()) : "None")).withStyle(ChatFormatting.GRAY)));
                }

                if (target instanceof Cat targetCat) {
                    String variantName = MiscUtil.reformatFromSnakeCase(BuiltInRegistries.CAT_VARIANT.getKey(targetCat.getVariant()).getPath());

                    tooltip.add(Component.literal("Collar Color: ").withStyle(ChatFormatting.BLUE).append(Component.literal((targetCat.isTame() ? MiscUtil.capitalizeFirstLetter(targetCat.getCollarColor().toString()) : "None")).withStyle(ChatFormatting.GRAY)));
                    tooltip.add(Component.literal("Variant: ").withStyle(ChatFormatting.BLUE).append(Component.literal(variantName).withStyle(ChatFormatting.GRAY)));
                }
            }
        }
    }

    protected boolean shouldCaptureEntity(ItemStack targetStack, LivingEntity target) {
        if (target.level().isClientSide) return false;
        if (target instanceof Player || !target.isAlive()) return false;
        if (isBlacklisted(target.getType())) return false;
        if (target.getBbWidth() >= 3.35F || target.getBbHeight() >= 4.225F) return false; // Ent size or larger

        CompoundTag critterCageData = new CompoundTag();

        CompoundTag entityData = new CompoundTag();

        target.stopRiding();
        target.ejectPassengers();

        target.save(entityData);
        critterCageData.put("storedEntity", entityData);
        critterCageData.putBoolean("isBaby", target.isBaby());
        critterCageData.putString("entityName", target.getName().getString());

        assert targetStack.getTag() != null;
        if (target instanceof Villager targetVillager && !targetVillager.getVillagerData().getProfession().equals(VillagerProfession.NONE)) {
            critterCageData.putString("entityName", targetVillager.getName().getString() + " Villager");
        }

        critterCageData.putBoolean("enchanted", target.getType().toString().contains("enchanted"));

        targetStack.setTag(critterCageData);

        targetStack.setCount(1);
        return true;
    }

    protected boolean canReleaseEntity(Player owner, BlockPos targetPos, Direction facingDir, Level curWorld, ItemStack filledStack) {
        if (owner.getCommandSenderWorld().isClientSide) return false;
        if (!containsEntity(filledStack)) return false;

        Entity curHeldEntity = getEntityFromStack(filledStack, curWorld, true);
        BlockPos relPos = targetPos.relative(facingDir);

        curHeldEntity.absMoveTo(relPos.getX() + 0.5, relPos.getY(), relPos.getZ() + 0.5, 0, 0);
        filledStack.setTag(null);
        curWorld.addFreshEntity(curHeldEntity);
        return true;
    }

    protected boolean isBlacklisted(EntityType<?> type) {
        return false; //TODO Add blacklist logic here
    }

    public Entity getEntityFromStack(ItemStack targetStack, Level world, boolean withInfo) {
        if (targetStack.hasTag()) {
            Entity targetEntity = EntityType.loadEntityRecursive(targetStack.getTag().getCompound("storedEntity"), world, entity -> entity);
            if (!withInfo && !targetEntity.getType().canSummon()) return null;

            return targetEntity;
        }

        return null;
    }

    public boolean containsEntity(ItemStack curStack) {
        return curStack != null && !curStack.isEmpty() && curStack.hasTag() && curStack.getTag().contains("storedEntity");
    }
    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("enchanted") && stack.getTag().getBoolean("enchanted");
    }

    public String getMobName(ItemStack stack) {
        return stack.getTag().getBoolean("isBaby") ? "Baby " +  stack.getTag().getString("entityName") : stack.getTag().getString("entityName");
    }

    public String getMobID(ItemStack stack) {
        return stack.getTag().getCompound("storedEntity").getString("id");
    }
}