package io.github.chaosawakens.common.enchantments;

import io.github.chaosawakens.common.registry.CAEnchantments;
import io.github.chaosawakens.util.EnchantmentUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

public class HoplologyEnchantment extends ProtectionEnchantment {
	private static final int MAX_PROTECTION_LEVEL = 50;
	private int protection;
	
	public HoplologyEnchantment(EquipmentSlot... slots) {
		super(Rarity.VERY_RARE, ProtectionEnchantment.Type.ALL, slots);
	}
	
	@Override
	public int getDamageProtection(int pLevel, DamageSource damageSource) {
		return damageSource.is(DamageTypeTags.BYPASSES_ARMOR) || damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) ? 0 : protection;
	}
	
	@Override
	public boolean checkCompatibility(Enchantment other) {
		return !(other instanceof ProtectionEnchantment);
	}
	
	@Override
	public int getMaxLevel() {
		return 1;
	}
	
	public int getProtection() {
		return protection;
	}
	
	public void incrementProtection(int protection) {
		this.protection += protection;
	}
	
	public void decrementProtection(int protection) {
		this.protection -= protection;
	}
	
	public void setProtection(int protection) {
		this.protection = protection;
	}
	
	public static int getMaxHoplologyProtectionLevel() {
		return MAX_PROTECTION_LEVEL;
	}
	
	public static void handleHoplologyProtection(LivingEntity owner, HoplologyEnchantment hoplologyEnchantment) {
		ObjectArrayList<ItemStack> curStacks = EnchantmentUtil.getItemStacksWithEnchantment(hoplologyEnchantment.getSlotItems(owner).values(), hoplologyEnchantment);

		curStacks.forEach((targetStack) -> {
			if (!(owner instanceof Player playerOwner)) return;

			int playerXPLevel = playerOwner.experienceLevel;

			if (playerXPLevel > MAX_PROTECTION_LEVEL) playerXPLevel = MAX_PROTECTION_LEVEL;

			hoplologyEnchantment.setProtection(Math.abs(playerXPLevel / 10));
		});
	}

	public static void updateProtection(LivingEntity owner) {
		if (owner != null) {
			int hoplologyLevel = EnchantmentHelper.getEnchantmentLevel(CAEnchantments.HOPLOLOGY.get(), owner);

			if (hoplologyLevel > 0) handleHoplologyProtection(owner, (HoplologyEnchantment) CAEnchantments.HOPLOLOGY.get());
		}
	}
}