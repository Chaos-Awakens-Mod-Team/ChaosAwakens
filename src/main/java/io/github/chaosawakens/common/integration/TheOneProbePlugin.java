package io.github.chaosawakens.common.integration;

import io.github.chaosawakens.ChaosAwakens;
import io.github.chaosawakens.common.blocks.AntInfestedOre;
import io.github.chaosawakens.common.entity.AppleCowEntity;
import io.github.chaosawakens.common.entity.BirdEntity;
import io.github.chaosawakens.common.entity.CrystalGatorEntity;
import io.github.chaosawakens.common.entity.DimetrodonEntity;
import io.github.chaosawakens.common.entity.FrogEntity;
import io.github.chaosawakens.common.entity.GazelleEntity;
import io.github.chaosawakens.common.registry.CABlocks;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.InterModComms;

import java.util.function.Function;

public class TheOneProbePlugin {
	public static void register() {
		InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
	}

	public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {
		@Override
		public Void apply(ITheOneProbe iTheOneProbe) {
			iTheOneProbe.registerBlockDisplayOverride((probeMode, iProbeInfo, playerEntity, world, blockState, iProbeHitData) -> {
				if (blockState.getBlock() instanceof AntInfestedOre) {
					if (blockState.is(CABlocks.RED_ANT_INFESTED_ORE.get())) {
						ItemStack stack = new ItemStack(Items.DIAMOND_ORE);
						iProbeInfo
								.horizontal()
								.item(stack)
								.vertical()
								.itemLabel(stack)
								.text(CompoundText.create()
										.style(TextStyleClass.MODNAME)
										.text(Tools.getModName(stack.getItem())));
					} else if (blockState.is(CABlocks.TERMITE_INFESTED_ORE.get())) {
						ItemStack stack = new ItemStack(Items.EMERALD_ORE);
						iProbeInfo
								.horizontal()
								.item(stack)
								.vertical()
								.itemLabel(stack)
								.text(CompoundText.create()
										.style(TextStyleClass.MODNAME)
										.text(Tools.getModName(stack.getItem())));
					}
					return true;
				}
				return false;
			});

			iTheOneProbe.registerEntityProvider(new IProbeInfoEntityProvider() {
				@Override
				public String getID() {
					return ChaosAwakens.MODID + ":default";
				}

				@Override
				public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, PlayerEntity playerEntity, World world, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
					String s = TextFormatting.stripFormatting(entity.getName().getString());
					if ("Froakie".equalsIgnoreCase(s)) iProbeInfo.text(CompoundText.createLabelInfo("Special Frog Species: ", "Blue"));
					if (entity instanceof AppleCowEntity) {
						int type = ((AppleCowEntity) entity).getAppleCowType();
						switch (type) {
						case 0:
						default:
							iProbeInfo.text(CompoundText.createLabelInfo("Apple Cow Species: ", "Apple Cow (" + type + ")"));
							return;
						case 1:
							iProbeInfo.text(CompoundText.createLabelInfo("Apple Cow Species: ", "Halloween (" + type + ")"));
						}
					}

					if (entity instanceof BirdEntity) {
						int type = ((BirdEntity) entity).getBirdType();
						switch (type) {
						case 0:
						default:
							iProbeInfo.text(CompoundText.createLabelInfo("Bird Species: ", "Black (" + type + ")"));
							return;
						case 1:
							iProbeInfo.text(CompoundText.createLabelInfo("Bird Species: ", "Brown (" + type + ")"));
							return;
						case 2:
							iProbeInfo.text(CompoundText.createLabelInfo("Bird Species: ", "Blue (" + type + ")"));
							return;
						case 3:
							iProbeInfo.text(CompoundText.createLabelInfo("Bird Species: ", "Green (" + type + ")"));
							return;
						case 4:
							iProbeInfo.text(CompoundText.createLabelInfo("Bird Species: ", "Red (" + type + ")"));
						}
					}

					if (entity instanceof FrogEntity) {
						int type = ((FrogEntity) entity).getFrogType();
						switch (type) {
						case 0:
						default:
							iProbeInfo.text(CompoundText.createLabelInfo("Frog Species: ", "Green (" + type + ")"));
							return;
						case 1:
							iProbeInfo.text(CompoundText.createLabelInfo("Frog Species: ", "Brown (" + type + ")"));
							return;
						case 2:
							iProbeInfo.text(CompoundText.createLabelInfo("Frog Species: ", "Pink (" + type + ")"));
							return;
						case 3:
							iProbeInfo.text(CompoundText.createLabelInfo("Frog Species: ", "Dark Green (" + type + ")"));
							return;
						case 4:
							iProbeInfo.text(CompoundText.createLabelInfo("Frog Species: ", "Red (" + type + ")"));
							return;
						case 5:
							iProbeInfo.text(CompoundText.createLabelInfo("Frog Species: ", "Orange (" + type + ")"));
							return;
						case 6:
							iProbeInfo.text(CompoundText.createLabelInfo("Frog Species: ", "Pale (" + type + ")"));
							return;
						case 7:
							iProbeInfo.text(CompoundText.createLabelInfo("Frog Species: ", "Yellow (" + type + ")"));
							return;
						case 99:
							iProbeInfo.text(CompoundText.createLabelInfo("Frog Species: ", "Black (" + type + ")"));
						}
					}

					if (entity instanceof GazelleEntity) {
						int type = ((GazelleEntity) entity).getGazelleType();
						switch (type) {
						case 0:
						default:
							iProbeInfo.text(CompoundText.createLabelInfo("Gazelle Species: ", "Brown (" + type + ")"));
							return;
						case 1:
							iProbeInfo.text(CompoundText.createLabelInfo("Gazelle Species: ", "Red (" + type + ")"));
							return;
						case 2:
							iProbeInfo.text(CompoundText.createLabelInfo("Gazelle Species: ", "Dark Red (" + type + ")"));
							return;
						case 3:
							iProbeInfo.text(CompoundText.createLabelInfo("Gazelle Species: ", "Black (" + type + ")"));
							return;
						case 4:
							iProbeInfo.text(CompoundText.createLabelInfo("Gazelle Species: ", "Furless (" + type + ")"));
						}
					}

					if (entity instanceof CrystalGatorEntity) {
						int type = ((CrystalGatorEntity) entity).getGatorType();
						switch (type) {
						case 0:
						default:
							iProbeInfo.text(CompoundText.createLabelInfo("Crystal Gator Species: ", "Blue (" + type + ")"));
							return;
						case 1:
							iProbeInfo.text(CompoundText.createLabelInfo("Crystal Gator Species: ", "Red (" + type + ")"));
							return;
						case 2:
							iProbeInfo.text(CompoundText.createLabelInfo("Crystal Gator Species: ", "Yellow (" + type + ")"));
							return;
						case 3:
							iProbeInfo.text(CompoundText.createLabelInfo("Crystal Gator Species: ", "Orange (" + type + ")"));
							return;
						case 4:
							iProbeInfo.text(CompoundText.createLabelInfo("Crystal Gator Species: ", "Pink (" + type + ")"));
							return;
						case 5:
							iProbeInfo.text(CompoundText.createLabelInfo("Crystal Gator Species: ", "Green (" + type + ")"));
						}
					}

					if (entity instanceof DimetrodonEntity) {
						int type = ((DimetrodonEntity) entity).getDimetrodonType();
						switch (type) {
						case 0:
						default:
							iProbeInfo.text(CompoundText.createLabelInfo("Dimetrodon Species: ", "Green (" + type + ")"));
							return;
						case 1:
							iProbeInfo.text(CompoundText.createLabelInfo("Dimetrodon Species: ", "Orange (" + type + ")"));
							return;
						case 2:
							iProbeInfo.text(CompoundText.createLabelInfo("Dimetrodon Species: ", "Purple (" + type + ")"));
							return;
						case 3:
							iProbeInfo.text(CompoundText.createLabelInfo("Dimetrodon Species: ", "Throwback (" + type + ")"));
						}
					}
				}
			});
			return null;
		}
	}
}
