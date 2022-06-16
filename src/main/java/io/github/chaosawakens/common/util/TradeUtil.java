package io.github.chaosawakens.common.util;

import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;

public class TradeUtil {
	public static final int NOVICE = 1;
	public static final int APPRENTICE = 2;
	public static final int JOURNEYMAN = 3;
	public static final int EXPERT = 4;
	public static final int MASTER = 5;

	public static void addVillagerTrades(VillagerTradesEvent event, int level, VillagerTrades.ITrade... trades) {
		for (VillagerTrades.ITrade trade : trades) event.getTrades().get(level).add(trade);
	}

	public static void addVillagerTrades(VillagerTradesEvent event, VillagerProfession profession, int level, VillagerTrades.ITrade... trades) {
		if (event.getType() == profession) addVillagerTrades(event, level, trades);
	}

	public static void addWandererTrades(WandererTradesEvent event, VillagerTrades.ITrade... trades) {
		for (VillagerTrades.ITrade trade : trades) event.getGenericTrades().add(trade);
	}

	public static void addRareWandererTrades(WandererTradesEvent event, VillagerTrades.ITrade... trades) {
		for (VillagerTrades.ITrade trade : trades) event.getRareTrades().add(trade);
	}

	public static class CATrade extends BasicTrade {
		public CATrade(ItemStack input, ItemStack input2, ItemStack output, int maxTrades, int xp, float priceMult) {
			super(input, input2, output, maxTrades, xp, priceMult);
		}

		public CATrade(Item input, int inputCount, Item output, int outputCount, int maxTrades, int xp, float priceMult) {
			this(new ItemStack(input, inputCount), ItemStack.EMPTY, new ItemStack(output, outputCount), maxTrades, xp, priceMult);
		}

		public CATrade(Item input, int inputCount, Item output, int outputCount, int maxTrades, int xp) {
			this(input, inputCount, output, outputCount, maxTrades, xp, 0.15F);
		}

		public CATrade(Item input, int inputCount, int emeraldCount, int maxTrades, int xp, float priceMult) {
			this(new ItemStack(input, inputCount), ItemStack.EMPTY, new ItemStack(Items.EMERALD, emeraldCount), maxTrades, xp, priceMult);
		}

		public CATrade(Item input, int inputCount, int emeraldCount, int maxTrades, int xp) {
			this(input, inputCount, emeraldCount, maxTrades, xp, 0.15F);
		}

		public CATrade(int emeraldCount, Item output, int outputCount, int maxTrades, int xp, float priceMult) {
			this(new ItemStack(Items.EMERALD, emeraldCount), ItemStack.EMPTY, new ItemStack(output, outputCount), maxTrades, xp, priceMult);
		}

		public CATrade(int emeraldCount, Item output, int outputCount, int maxTrades, int xp) {
			this(emeraldCount, output, outputCount, maxTrades, xp, 0.15F);
		}

		public CATrade(int emeraldCount, Item output, int maxTrades) {
			this(emeraldCount, output, 1, maxTrades, 0, 0);
		}
	}
}
