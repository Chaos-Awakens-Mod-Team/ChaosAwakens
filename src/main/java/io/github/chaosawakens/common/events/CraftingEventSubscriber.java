package io.github.chaosawakens.common.events;

import io.github.chaosawakens.ChaosAwakens;
import io.github.chaosawakens.api.IAutoEnchantable;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemCraftedEvent;

public class CraftingEventSubscriber {
	public static void onItemCraftedEvent(final ItemCraftedEvent event) {
		ChaosAwakens.LOGGER.debug("[CRAFTING]: " + event.getCrafting());
		Item enchantedItem = event.getCrafting().getItem();
		if (event.getCrafting().getItem() instanceof IAutoEnchantable) {
			for (EnchantmentData enchant : ((IAutoEnchantable) enchantedItem).enchantments()) event.getCrafting().enchant(enchant.enchantment, enchant.level);
		}
	}
}
