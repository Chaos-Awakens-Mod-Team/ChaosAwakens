package io.github.chaosawakens.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public final class EnchantmentUtil {

	private EnchantmentUtil() {
		throw new IllegalAccessError("Attempted to instantiate a Utility Class!");
	}

	/**
	 * Checks if the specified {@link ItemStack} is enchanted with the specified {@link Enchantment} using {@link EnchantmentHelper#getItemEnchantmentLevel(Enchantment, ItemStack)}.
	 *
	 * @param stackToCheck The {@link ItemStack} to check for the specified {@link Enchantment}.
	 * @param targetEnchantment The target {@link Enchantment} to check the specified {@link ItemStack} for.
	 *
	 * @return {@code true} if the specified {@link ItemStack} is enchanted with the specified {@link Enchantment}, else returns {@code false}.
	 */
	public static boolean isStackEnchantedWith(ItemStack stackToCheck, Enchantment targetEnchantment) {
		return EnchantmentHelper.getItemEnchantmentLevel(targetEnchantment, stackToCheck) > 0;
	}

	/**
	 * Sifts through the provided {@link Collection} of {@linkplain ItemStack ItemStacks} and returns a filtered {@link ObjectArrayList} of {@linkplain ItemStack ItemStacks} enchanted with the specified {@link Enchantment}}.
	 *
	 * @param stacksToCheck A {@link Collection} of {@linkplain ItemStack ItemStacks} to check for the specified enchantment.
	 * @param targetEnchantment The target {@link Enchantment} to look for in each stack.
	 *
	 * @return A pruned {@link ObjectArrayList} of {@linkplain ItemStack ItemStacks} with the specified enchantment. May be empty.
	 */
	public static ObjectArrayList<ItemStack> getItemStacksWithEnchantment(Collection<ItemStack> stacksToCheck, Enchantment targetEnchantment) {
		return stacksToCheck.stream()
				.filter((targetStack) -> !targetStack.isEmpty() && isStackEnchantedWith(targetStack, targetEnchantment))
				.collect(Collectors.toCollection(ObjectArrayList::new));
	}

	/**
	 * Overloaded variant of {@link #getItemStacksWithEnchantment(Collection, Enchantment)}, checks a {@linkplain Player#inventoryMenu Player's inventoryMenu} for any enchanted {@linkplain ItemStack ItemStacks}
	 * matching the specified {@link Enchantment}.
	 *
	 * @param targetPlayer The target {@link Player} whose inventory should be checked for enchanted {@linkplain ItemStack ItemStacks} matching the specified {@link Enchantment}.
	 * @param targetEnchantment The {@link Enchantment} to check each {@link ItemStack} in the target player's inventory against.
	 *
	 * @return {@link #getItemStacksWithEnchantment(Collection, Enchantment)}.
	 */
	public static ObjectArrayList<ItemStack> getItemStacksWithEnchantment(Player targetPlayer, Enchantment targetEnchantment) {
		return getItemStacksWithEnchantment(targetPlayer.inventoryMenu.getItems(), targetEnchantment);
	}

	/**
	 * Gets all enchanted {@linkplain ItemStack ItemStacks} in the specified {@link Collection}.
	 *
	 * @param itemStacksToCheck The {@linkplain ItemStack ItemStacks} to test for enchantments.
	 *
	 * @return A pruned {@link Object2ObjectArrayMap} containing all enchanted {@linkplain ItemStack ItemStacks} (regardless of their enchantments). May be empty.
	 */
	public static Object2ObjectArrayMap<ItemStack, Map<Enchantment, Integer>> getEnchantedItemStacks(Collection<ItemStack> itemStacksToCheck) {
		ObjectArrayList<ItemStack> enchantedItemStacks = itemStacksToCheck.stream()
				.filter((targetStack) -> !targetStack.isEmpty() && targetStack.isEnchanted())
				.collect(Collectors.toCollection(ObjectArrayList::new));

		Object2ObjectArrayMap<ItemStack, Map<Enchantment, Integer>> mappedEnchantedItemStacks = new Object2ObjectArrayMap<>(enchantedItemStacks.size());

		enchantedItemStacks.forEach((targetStack) -> mappedEnchantedItemStacks.put(targetStack, EnchantmentHelper.getEnchantments(targetStack)));

		return mappedEnchantedItemStacks;
	}

	/**
	 * Overloaded variant of {@link #getEnchantedItemStacks(Collection)}, checks a {@linkplain Player#inventoryMenu Player's inventoryMenu} for any enchanted {@linkplain ItemStack ItemStacks}.
	 *
	 * @param targetPlayer The target {@link Player} whose inventory to check for enchanted {@linkplain ItemStack ItemStacks}.
	 *
	 * @return {@link #getEnchantedItemStacks(Collection)}.
	 */
	public static Object2ObjectArrayMap<ItemStack, Map<Enchantment, Integer>> getEnchantedItemStacks(Player targetPlayer) {
		return getEnchantedItemStacks(targetPlayer.inventoryMenu.getItems());
	}
}