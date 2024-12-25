package io.github.chaosawakens.common.item.misc;

import io.github.chaosawakens.util.RegistryUtil;
import io.github.chaosawakens.util.MiscUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
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

        if (shouldCaptureEntity(filledStack, playerOwner, target)) {
            if (targetEmptyStack.getCount() == 1) {
                if (!containsEntity(targetEmptyStack)) {
                    playerOwner.setItemInHand(hand, filledStack);
                    targetEmptyStack.shrink(1);
                    target.remove(DISCARDED);
                    return InteractionResult.SUCCESS;
                } else return InteractionResult.FAIL;
            } else {
                if (!playerOwner.addItem(filledStack)) playerOwner.drop(filledStack, false, true);
                targetEmptyStack.shrink(1);
                target.remove(DISCARDED);
                return InteractionResult.SUCCESS;
            }
        } else return InteractionResult.FAIL;
    }
    private boolean shouldCaptureEntity(ItemStack targetStack, Player playerOwner, LivingEntity target) {
        //if (target.getCommandSenderWorld().isClientSide) return false;
        if (target instanceof Player || !target.isAlive()) return false;
        if (containsEntity(targetStack)) return false;
        if (isBlacklisted(target.getType())) return false;
        if (target.getBbWidth() >= 3.35F || target.getBbHeight() >= 4.225F) return false; // Ent size or larger

        CompoundTag critterCageData = new CompoundTag();

        critterCageData.putString("entity", EntityType.getKey(target.getType()).toString());
        critterCageData.putString("entityName", target.getName().getString());
        critterCageData.putDouble("entityMaxHealth", target.getAttribute(Attributes.MAX_HEALTH).getValue());
        critterCageData.putBoolean("isBaby", target.isBaby());
        critterCageData.putBoolean("isVillager", target instanceof Villager);
        critterCageData.putBoolean("isTamable", target instanceof TamableAnimal);

        assert targetStack.getTag() != null;
        if (critterCageData.getBoolean("isVillager")) {
            Villager targetVillager = (Villager) target;

            critterCageData.putString("entityName", targetVillager.getName().getString() + " Villager");
            critterCageData.putString("villagerProfession", targetVillager.getVillagerData().getProfession() != VillagerProfession.NONE ? MiscUtil.capitalizeFirstLetter(targetVillager.getVillagerData().getProfession().toString()) : "None");
            critterCageData.putInt("villagerTradingLevel", targetVillager.getVillagerData().getLevel());
        }
        if (critterCageData.getBoolean("isTameable")) {
            TamableAnimal targetTamable = (TamableAnimal) target;

            critterCageData.putString("owner", targetTamable.getOwner() instanceof Player ? playerOwner.getScoreboardName() : "None");

            if (targetTamable instanceof Wolf targetWolf) {
                critterCageData.putString("collarColor", targetWolf.isTame() ? MiscUtil.capitalizeFirstLetter(targetWolf.getCollarColor().toString()) : "None");
            }
        }

        critterCageData.putBoolean("enchanted", target.getType().toString().contains("enchanted"));
        critterCageData.putString("entityRegName", RegistryUtil.getEntityKey(target.getType()).toString());

        target.saveWithoutId(critterCageData);
        targetStack.setTag(critterCageData);

        targetStack.setCount(1);
        return true;
    }

    private boolean isBlacklisted(EntityType<?> type) {
        return false; //TODO Add blacklist logic heretype.is(CATags.CRITTER_CAGE_BLACKLISTED);
    }

    private boolean canReleaseEntity(Player owner, BlockPos targetPos, Direction facingDir, Level curWorld, ItemStack filledStack) {
        if (owner.getCommandSenderWorld().isClientSide) return false;
        if (!containsEntity(filledStack)) return false;

        Entity curHeldEntity = getEntityFromStack(filledStack, curWorld, true);
        BlockPos relPos = targetPos.relative(facingDir);

        curHeldEntity.absMoveTo(relPos.getX() + 0.5, relPos.getY(), relPos.getZ() + 0.5, 0, 0);
        filledStack.setTag(null);
        curWorld.addFreshEntity(curHeldEntity);
        return true;
    }

    private Entity getEntityFromStack(ItemStack targetStack, Level world, boolean withInfo) {
        if (targetStack.hasTag()) {
            EntityType<?> type = EntityType.byString(targetStack.getTag().getString("entity")).orElse(null);

            if (type != null) {
                LivingEntity targetEntity = (LivingEntity) type.create(world);

                if (withInfo) targetEntity.load(targetStack.getTag());
                else if (!type.canSummon()) return null;
                return targetEntity;
            }
        }
        return null;
    }

    private boolean containsEntity(ItemStack curStack) {
        return curStack != null && !curStack.isEmpty() && curStack.hasTag() && curStack.getTag().contains("entity");
    }
    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("enchanted") && stack.getTag().getBoolean("enchanted");
    }

    public String getMobID(ItemStack stack) {
        return stack.getTag().getString("entity");
    }

    public String getMobName(ItemStack stack) {
        return stack.getTag().getBoolean("isBaby") ? "Baby " +  stack.getTag().getString("entityName") : stack.getTag().getString("entityName");
    }

    @Nonnull
    @Override
    public Component getName(ItemStack stack) {
        if (!containsEntity(stack)) return Component.translatable("item.chaosawakens.critter_cage");
        return Component.translatable("item.chaosawakens.critter_cage").append(" (" + getMobName(stack) + ")");
    }
    public void appendHoverText(ItemStack targetStack, Level curWorld, List<Component> tooltip, TooltipFlag flag) {
        if (containsEntity(targetStack)) {
            LivingEntity target = (LivingEntity) getEntityFromStack(targetStack, curWorld, true);

            //	tooltip.add(Component.literal("Is Baby: " + stack.getTag().getBoolean("isBaby"))); -Unneeded for now --Meme Man
            if (targetStack.getTag().getBoolean("isVillager")) {
                if (target instanceof Villager) { //TODO What is this abomination of a switch statement :skullcry:
                    switch (targetStack.getTag().getInt("villagerTradingLevel")) {
                        case 1:
                            if (targetStack.getTag().getInt("villagerTradingLevel") == 1 && !targetStack.getTag().getString("villagerProfession").equalsIgnoreCase("none"))
                                tooltip.add(Component.literal("Trading Level: Novice"));
                        case 2:
                            if (targetStack.getTag().getInt("villagerTradingLevel") == 2)
                                tooltip.add(Component.literal("Trading Level: Apprentice"));
                        case 3:
                            if (targetStack.getTag().getInt("villagerTradingLevel") == 3)
                                tooltip.add(Component.literal("Trading Level: Journeyman"));
                        case 4:
                            if (targetStack.getTag().getInt("villagerTradingLevel") == 4)
                                tooltip.add(Component.literal("Trading Level: Expert"));
                        case 5:
                            if (targetStack.getTag().getInt("villagerTradingLevel") == 5)
                                tooltip.add(Component.literal("Trading Level: Master"));
                        default:
                            if (targetStack.getTag().getString("villagerProfession").equalsIgnoreCase("none"))
                                tooltip.add(Component.literal("Trading Level: None"));
                    }
                }
            }
            if (targetStack.getTag().getBoolean("isTameable")) {
                if (target instanceof TamableAnimal) {
                    tooltip.add(Component.literal("Owner: " + targetStack.getTag().getString("owner")));
                    if (target instanceof Wolf) {
                        tooltip.add(Component.literal("Collar Color: " + targetStack.getTag().getString("collarColor")));
                    }
                }
            }
        }
    }
}