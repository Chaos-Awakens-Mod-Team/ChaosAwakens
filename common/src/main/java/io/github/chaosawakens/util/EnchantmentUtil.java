package io.github.chaosawakens.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public final class EnchantmentUtil {
	
	private EnchantmentUtil() {
		throw new IllegalAccessError("Attempted to instantiate a Utility Class!");
	}
	
	/**
	 * Checks if a stack is enchanted with the specified enchantment using {@link EnchantmentHelper#getItemEnchantmentLevel(Enchantment, ItemStack)}.
	 * @param stackToCheck The stack to check for the specified enchantment.
	 * @param targetEnchantment The target enchantment to check the specified stack for.
	 * @return true if the specified {@link ItemStack} is enchanted with the specified {@link Enchantment}, else returns false.
	 */
	public static boolean isStackEnchantedWith(ItemStack stackToCheck, Enchantment targetEnchantment) {
		return EnchantmentHelper.getItemEnchantmentLevel(targetEnchantment, stackToCheck) > 0;
	}
	
	/**
	 * Gets a filtered list of {@link ItemStack}s enchanted with the specified enchantment.
	 * @param stacksToCheck A {@link Collection} of {@link ItemStack}s to check for the specified enchantment.
	 * @param targetEnchantment The target {@link Enchantment} to look for in each stack.
	 * @return A pruned {@link ObjectArrayList} of {@link ItemStack}s with the specified enchantment, or null if there aren't any.
	 */
	@Nullable
	public static ObjectArrayList<ItemStack> getItemStacksWithEnchantment(Collection<ItemStack> stacksToCheck, Enchantment targetEnchantment) {
		ObjectArrayList<ItemStack> relevantEnchantedStacks = stacksToCheck.stream()
				.filter((targetStack) -> !targetStack.isEmpty() && isStackEnchantedWith(targetStack, targetEnchantment))
				.collect(Collectors.toCollection(ObjectArrayList::new));
		
		return relevantEnchantedStacks != null && !relevantEnchantedStacks.isEmpty() ? relevantEnchantedStacks : null;
	}
	
	/**
	 * Overloaded method for {@link #getItemStacksWithEnchantment(Collection, Enchantment)}, checks a player's {@link Player#inventoryMenu} for any enchanted {@link ItemStack}s
	 * matching the specified {@link Enchantment}.
	 * @param targetPlayer The target player whose inventory to check for enchanted {@link ItemStack}s matching the specified {@link Enchantment}.
	 * @param targetEnchantment The enchantment to check each {@link ItemStack} in a player's inventory against.
	 * @return {@link #getItemStacksWithEnchantment(Collection, Enchantment)}.
	 */
	@Nullable
	public static ObjectArrayList<ItemStack> getItemStacksWithEnchantment(Player targetPlayer, Enchantment targetEnchantment) {
		return getItemStacksWithEnchantment(targetPlayer.inventoryMenu.getItems(), targetEnchantment);
	}
	
	/**
	 * Gets all enchanted {@link ItemStack}s in the specified {@link Collection}.
	 * @param itemStacksToCheck The {@link ItemStack}s to test for being enchanted.
	 * @return A pruned {@link Object2ObjectArrayMap} containing all enchanted {@link ItemStack}s (regardless of their enchantments), or null if there aren't any.
	 */
	@Nullable
	public static Object2ObjectArrayMap<ItemStack, Map<Enchantment, Integer>> getEnchantedItemStacks(Collection<ItemStack> itemStacksToCheck) {
		ObjectArrayList<ItemStack> enchantedItemStacks = itemStacksToCheck.stream()
				.filter((targetStack) -> !targetStack.isEmpty() && targetStack.isEnchanted())
				.collect(Collectors.toCollection(ObjectArrayList::new));
		
		if (enchantedItemStacks == null || enchantedItemStacks.isEmpty()) return null;
		
		Object2ObjectArrayMap<ItemStack, Map<Enchantment, Integer>> mappedEnchantedItemStacks = new Object2ObjectArrayMap<>(enchantedItemStacks.size());
		
		enchantedItemStacks.forEach((targetStack) -> mappedEnchantedItemStacks.put(targetStack, EnchantmentHelper.getEnchantments(targetStack)));
		
		return mappedEnchantedItemStacks;
	}
	
	/**
	 * Overloaded method for {@link #getEnchantedItemStacks(Collection)}, checks a player's {@link Player#inventoryMenu} for any enchanted {@link ItemStack}s.
	 * @param targetPlayer The target player whose inventory to check for enchanted {@link ItemStack}s.
	 * @return {@link #getEnchantedItemStacks(Collection)}.
	 */
	@Nullable
	public static Object2ObjectArrayMap<ItemStack, Map<Enchantment, Integer>> getEnchantedItemStacks(Player targetPlayer) {
		return getEnchantedItemStacks(targetPlayer.inventoryMenu.getItems());
	}
}