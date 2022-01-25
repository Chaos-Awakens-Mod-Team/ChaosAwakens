package io.github.chaosawakens.common.worldgen;

import io.github.chaosawakens.common.config.CAConfig;
import io.github.chaosawakens.common.registry.CABiomes;
import io.github.chaosawakens.common.registry.CAConfiguredFeatures;
import io.github.chaosawakens.common.registry.CAConfiguredStructures;
import io.github.chaosawakens.common.registry.CAEntityTypes;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Class with method(s) that subscribe to the BiomeLoadingEvent
 *
 * @author invalid2
 */
public class BiomeLoadEventSubscriber {
	
	public static void onBiomeLoadingEvent(final BiomeLoadingEvent event) {
		StructureHandler.addfeatures(event);
		StructureHandler.addStructureSpawns(event);
		MobSpawnHandler.addMobSpawns(event);
	}

	public static void setEntDungeon(Function<StructureFeature<?, ?>, ?> func, BiomeLoadingEvent event) {
		RegistryKey<Biome> biome = RegistryKey.create(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(event.getName(), "Who registered null name biome, naming criticism!"));

		final String location = biome.location().toString();

		if (location.contains("birch")) {
			func.apply(CAConfiguredStructures.CONFIGURED_BIRCH_ENT_TREE);
			return;
		}
		if (location.contains("dark")) {
			func.apply(CAConfiguredStructures.CONFIGURED_DARK_OAK_ENT_TREE);
			return;
		}
		if (location.contains("crimson")) {
			func.apply(CAConfiguredStructures.CONFIGURED_CRIMSON_ENT_TREE);
			return;
		}
		if (location.contains("warped")) {
			func.apply(CAConfiguredStructures.CONFIGURED_WARPED_ENT_TREE);
			return;
		}
		
		switch (event.getCategory()) {
			case JUNGLE:
				func.apply(CAConfiguredStructures.CONFIGURED_JUNGLE_ENT_TREE);
				break;
			case SAVANNA:
				if (location.contains("village_"))
					return;
				func.apply(CAConfiguredStructures.CONFIGURED_ACACIA_ENT_TREE);
				break;
			case TAIGA:
			case EXTREME_HILLS:
				if (location.contains("village_"))
					return;
				func.apply(CAConfiguredStructures.CONFIGURED_SPRUCE_ENT_TREE);
				break;
			case FOREST:
			case PLAINS:
				// Doing this in one if statement didn't work and would only make it now spawn in biomes named "village_" so I extended it to 4 if statements.
				if (location.contains("village_"))
					return;
				if (location.contains("stalagmite_valley"))
					return;
				if (location.contains("mining_biome"))
					return;
				if (location.contains("crystal_"))
					return;
				func.apply(CAConfiguredStructures.CONFIGURED_OAK_ENT_TREE);
				break;
			default:
				break;
		}
	}
	
	private static class MobSpawnHandler {
		
//		// Mobs that appear in any biome, but only in the overworld
//		private static final Consumer<MobSpawnInfoBuilder> OVERWORLD_MOBS = (builder) -> {
//		};

		private static final Consumer<MobSpawnInfoBuilder> SWAMP_MOBS = (builder) -> {
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.RUBY_BUG.get(), 5, 2, 5));
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.EMERALD_GATOR.get(), 5, 2, 5));
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.FROG.get(), 25, 2, 6));
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.STINK_BUG.get(), 7, 1, 4));
		};
		
		private static final Consumer<MobSpawnInfoBuilder> OCEAN_MOBS = (builder) -> {
			builder.addSpawn(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.WHALE.get(), 1, 1, 1));
			builder.addSpawn(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.ROCK_FISH.get(), 14, 3, 5));
			builder.addSpawn(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.WOOD_FISH.get(), 20, 5, 8));
		};
		
		private static final Consumer<MobSpawnInfoBuilder> FOREST_MOBS = (builder) -> {
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.BEAVER.get(), 11, 1, 2));
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.STINK_BUG.get(), 15, 3, 5));
		};
		
		private static final Consumer<MobSpawnInfoBuilder> PLAINS_MOBS = (builder) -> {
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.APPLE_COW.get(), 7, 4, 4));
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.GOLDEN_APPLE_COW.get(), 4, 3, 3));
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.ENCHANTED_GOLDEN_APPLE_COW.get(), 1, 1, 1));
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.CARROT_PIG.get(), 8, 3, 3));
			builder.addSpawn(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.GOLDEN_CARROT_PIG.get(), 5, 2, 2));
		};

		private static final Consumer<MobSpawnInfoBuilder> NETHER_MOBS = (builder) -> {
			builder.addSpawn(EntityClassification.WATER_CREATURE, new MobSpawnInfo.Spawners(CAEntityTypes.LAVA_EEL.get(), 10, 1, 2));
		};

		private static final Consumer<MobSpawnInfoBuilder> BASALT_DELTA_MOBS = (builder) -> {
			builder.addSpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(CAEntityTypes.FROG.get(), 25, 2, 6));
		};
		
		public static void addMobSpawns(BiomeLoadingEvent event) {
			MobSpawnInfoBuilder spawnInfoBuilder = event.getSpawns();
			RegistryKey<Biome> biome = RegistryKey.create(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(event.getName(), "Who registered null name biome, naming criticism!"));
			final String location = biome.location().toString();
			
			switch (event.getCategory()) {
				case SWAMP:
					if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD))
						SWAMP_MOBS.accept(spawnInfoBuilder);
				case PLAINS:
					if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD))
						PLAINS_MOBS.accept(spawnInfoBuilder);
				case FOREST:
					if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD))
						FOREST_MOBS.accept(spawnInfoBuilder);
				case OCEAN:
					if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN))
						OCEAN_MOBS.accept(spawnInfoBuilder);
				case THEEND:
				case NETHER:
					if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.HOT) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER))
						NETHER_MOBS.accept(spawnInfoBuilder);
					if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER) && location.contains("basalt"))
						BASALT_DELTA_MOBS.accept(spawnInfoBuilder);
					break;
				default:
					if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER))
//						OVERWORLD_MOBS.accept(spawnInfoBuilder);
						break;
			}
		}
	}
	
	private static class StructureHandler {
		public static void addfeatures(BiomeLoadingEvent event) {
			BiomeGenerationSettingsBuilder gen = event.getGeneration();
			
			RegistryKey<Biome> biome = RegistryKey.create(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(event.getName(), "Who registered null name biome, naming criticism!"));
			final String location = biome.location().toString();

			BiomeLoadEventSubscriber.setEntDungeon((struct) -> gen.getStructures().add(() -> struct), event);
			
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD)) {
				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.CORN_PATCH);
				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.TOMATO_PATCH);
				if (CAConfig.COMMON.enableOreGen.get())
					addOverworldOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) &&  BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addMountainBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) &&  BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addPlainsBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) &&  BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addForestBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) && location.contains("birch")) {
				if (CAConfig.COMMON.enableOreGen.get())
					addBirchBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) &&  location.contains("taiga")) {
				if (CAConfig.COMMON.enableOreGen.get())
					addTaigaBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) &&  location.contains("dark")) {
				if (CAConfig.COMMON.enableOreGen.get())
					addDarkForestBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) &&  BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addJungleBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) &&  BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addSavannaBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) &&  BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addSwampBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) &&  BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addSnowyBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) &&  BiomeDictionary.hasType(biome, BiomeDictionary.Type.HOT) &&
					BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY) &&
					BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) &&
					BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addDesertBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) &&  BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) ||
					BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY) ||
					location.contains("taiga") || location.contains("desert") ||
					BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addVillageBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) &&  BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.RIVER)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addRiverOceanBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.MUSHROOM)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addMushroomBiomeOres(gen);
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
				if (CAConfig.COMMON.enableOreGen.get()) {
					addNetherOres(gen);
				}
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.END)) {
				if (CAConfig.COMMON.enableOreGen.get()) {
					addEndOres(gen);
				}
			}
			if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST)) {
				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.TREES_APPLE);
				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.TREES_CHERRY);
				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.TREES_PEACH);
				gen.getStructures().add(() -> CAConfiguredStructures.CONFIGURED_WASP_DUNGEON);
			}
			if (BiomeDictionary.hasType(biome, CABiomes.Type.MINING_DIMENSION)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addMiningDimOres(gen);

				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.TREES_APPLE);
				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.TREES_CHERRY);
				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.TREES_PEACH);
				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.CORN_PATCH);
			}
			if (BiomeDictionary.hasType(biome, CABiomes.Type.VILLAGE_DIMENSION)) {
				if (CAConfig.COMMON.enableOreGen.get())
					addVillageDimOres(gen);

				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.TREES_APPLE);
				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.TREES_CHERRY);
				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.TREES_PEACH);
				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.CORN_PATCH);
			}
			if (BiomeDictionary.hasType(biome, CABiomes.Type.CRYSTAL_DIMENSION)) {
				gen.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CAConfiguredFeatures.TREES_CRYSTAL_PLAINS);
				if (CAConfig.COMMON.enableOreGen.get())
					addCrystalDimOres(gen);
			}
		}
		
		public static void addStructureSpawns(BiomeLoadingEvent event) {
			BiomeGenerationSettingsBuilder builder = event.getGeneration();
			
			BiomeLoadEventSubscriber.setEntDungeon(builder::addStructureStart, event);
			
			switch (event.getCategory()) {
				case FOREST:
					builder.addStructureStart(CAConfiguredStructures.CONFIGURED_WASP_DUNGEON);
				case SWAMP:
					builder.addStructureStart(CAConfiguredStructures.CONFIGURED_WASP_DUNGEON);
				case SAVANNA:
					builder.addStructureStart(CAConfiguredStructures.CONFIGURED_WASP_DUNGEON);
				default:
					break;
			}
		}
		
		private static void addOverworldOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableOreRubyGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.ORE_RUBY_LAVA);
			if (CAConfig.COMMON.enableOreAmethystGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.ORE_AMETHYST);
			if (CAConfig.COMMON.enableOreUraniumGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.ORE_URANIUM);
			if (CAConfig.COMMON.enableOreTitaniumGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.ORE_TITANIUM);
			if (CAConfig.COMMON.enableOreTigersEyeGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.ORE_TIGERS_EYE);
			if (CAConfig.COMMON.enableOreSaltGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.ORE_SALT);
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_HERCULES_BEETLE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_WTF);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_PIRAPORU);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_APPLE_COW);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_GOLDEN_APPLE_COW);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_CARROT_PIG);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_GOLDEN_CARROT_PIG);

				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_BAT);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_BEE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_CAVE_SPIDER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_CHICKEN);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_COW);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_CREEPER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_DONKEY);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_ENDERMAN);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_PHANTOM);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_PIG);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_RABBIT);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_SHEEP);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_SKELETON);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_SKELETON_HORSE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_SPIDER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_WANDERING_TRADER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_WITCH);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_ZOMBIE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_ZOMBIE_HORSE);
			}
			if (CAConfig.COMMON.enableTrollOreGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.RED_ANT_INFESTED);
			}
			if (CAConfig.COMMON.spawnDzOresInOverworld.get() && CAConfig.COMMON.enableDzMineralOreGen.get()) {
				if (CAConfig.COMMON.enableOreCopperGen.get())
					gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.ORE_COPPER);
				if (CAConfig.COMMON.enableOreTinGen.get())
					gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.ORE_TIN);
				if (CAConfig.COMMON.enableOreSilverGen.get())
					gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.ORE_SILVER);
				if (CAConfig.COMMON.enableOrePlatinumGen.get())
					gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.ORE_PLATINUM);
				if (CAConfig.COMMON.enableOreSunstoneGen.get())
					gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.ORE_SUNSTONE);
				if (CAConfig.COMMON.enableOreBloodstoneGen.get())
					gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.ORE_BLOODSTONE);
			}
			if (CAConfig.COMMON.enableNestGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.BROWN_ANT_NEST);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.RAINBOW_ANT_NEST);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.RED_ANT_NEST);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.UNSTABLE_ANT_NEST);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.TERMITE_NEST);
			}
		}
		
		private static void addMountainBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableTrollOreGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.TERMITE_INFESTED);
			}
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_SPRUCE_ENT);

				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_LLAMA);
			}
		}

		private static void addPlainsBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_OAK_ENT);
			}
		}

		private static void addForestBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_OAK_ENT);

				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_WASP);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_WOLF);
			}
		}

		private static void addBirchBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_BIRCH_ENT);
			}
		}

		private static void addTaigaBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_SPRUCE_ENT);

				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_FOX);
			}
		}

		private static void addDarkForestBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_DARK_OAK_ENT);

				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_EVOKER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_ILLUSIONER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_PILLAGER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_VINDICATOR);
			}
		}

		private static void addJungleBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_JUNGLE_ENT);

				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_OCELOT);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_PANDA);
			}
		}

		private static void addSavannaBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_ACACIA_ENT);
			}
		}

		private static void addSwampBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_EMERALD_GATOR);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_RUBY_BUG);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_SLIME);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_WASP);
			}
		}

		private static void addSnowyBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_POLAR_BEAR);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_SNOW_GOLEM);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_STRAY);
			}
		}

		private static void addDesertBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_HUSK);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_HUSK_STONE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_SCORPION);
			}
		}

		private static void addVillageBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_IRON_GOLEM);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_PILLAGER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_RAVAGER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_VILLAGER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_VINDICATOR);
			}
		}

		private static void addRiverOceanBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_COD);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_DROWNED);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_DOLPHIN);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_GUARDIAN);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_PUFFERFISH);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_SALMON);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_SQUID);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_TROPICAL_FISH);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_TURTLE);

				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_GREEN_FISH);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_ROCK_FISH);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_SPARK_FISH);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_WOOD_FISH);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_WHALE);
			}
		}

		private static void addMushroomBiomeOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.FOSSILISED_MOOSHROOM);
			}
		}

		private static void addNetherOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableOreRubyGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHERRACK_ORE_RUBY_LAVA);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHERRACK_ORE_RUBY);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.BLACKSTONE_ORE_RUBY);
			}
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_CRIMSON_ENT);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_WARPED_ENT);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_LAVA_EEL);

				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_BLAZE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_GHAST);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_HOGLIN);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_ENDERMAN_NETHERRACK);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_MAGMA_CUBE_NETHERRACK);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_MAGMA_CUBE_BLACKSTONE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_PIGLIN);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_SKELETON_SOUL_SOIL);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_STRIDER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_WITHER_SKELETON);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.NETHER_FOSSILISED_ZOMBIFIED_PIGLIN);
			}
		}

		private static void addEndOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.END_FOSSILISED_ENDERMAN_END_STONE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.END_FOSSILISED_ENDERMITE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.END_FOSSILISED_SHULKER);
			}
		}

		private static void addMiningDimOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableOreRubyGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_RUBY_LAVA);
			if (CAConfig.COMMON.enableOreRubyGen.get())
			if (CAConfig.COMMON.enableOreAmethystGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_AMETHYST);
			if (CAConfig.COMMON.enableOreUraniumGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_URANIUM);
			if (CAConfig.COMMON.enableOreTitaniumGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_TITANIUM);
			if (CAConfig.COMMON.enableOreTigersEyeGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_TIGERS_EYE);
			if (CAConfig.COMMON.enableOreAluminumGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_ALUMINUM);
			if (CAConfig.COMMON.enableDzMineralOreGen.get()) {
				if (CAConfig.COMMON.enableOreCopperGen.get())
					gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_COPPER);
				if (CAConfig.COMMON.enableOreTinGen.get())
					gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_TIN);
				if (CAConfig.COMMON.enableOreSilverGen.get())
					gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_SILVER);
				if (CAConfig.COMMON.enableOrePlatinumGen.get())
					gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_PLATINUM);
				if (CAConfig.COMMON.enableOreSunstoneGen.get())
					gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_SUNSTONE);
				if (CAConfig.COMMON.enableOreBloodstoneGen.get())
					gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_BLOODSTONE);
			}
			if (CAConfig.COMMON.enableOreSaltGen.get())
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_ORE_SALT);
			if (CAConfig.COMMON.enableFossilGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_HERCULES_BEETLE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_RUBY_BUG);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_EMERALD_GATOR);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_WTF);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_SCORPION);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_WASP);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_PIRAPORU);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_APPLE_COW);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_GOLDEN_APPLE_COW);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_CARROT_PIG);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_GOLDEN_CARROT_PIG);

				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_BAT);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_BEE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_CAVE_SPIDER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_CHICKEN);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_COW);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_CREEPER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_DONKEY);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_ENDERMAN);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_EVOKER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_FOX);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_GIANT);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_ILLUSIONER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_IRON_GOLEM);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_LLAMA);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_MOOSHROOM);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_OCELOT);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_PANDA);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_PIG);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_PILLAGER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_RABBIT);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_RAVAGER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_SHEEP);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_SKELETON);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_SKELETON_HORSE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_SLIME);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_SPIDER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_VILLAGER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_VINDICATOR);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_WANDERING_TRADER);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_WITCH);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_WOLF);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_ZOMBIE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_FOSSILISED_ZOMBIE_HORSE);
			}
			if (CAConfig.COMMON.enableTrollOreGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_RED_ANT_INFESTED);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.MINING_TERMITE_INFESTED);
			}
			if (CAConfig.COMMON.enableNestGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.RED_ANT_NEST);
			}
		}
		
		private static void addVillageDimOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableNestGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.RAINBOW_ANT_NEST);
			}
		}
		
		private static void addCrystalDimOres(BiomeGenerationSettingsBuilder gen) {
			if (CAConfig.COMMON.enableOreGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.CRYSTAL_ORE_ENERGY);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.GEODE_CATS_EYE);
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.GEODE_PINK_TOURMALINE);
			}
			if (CAConfig.COMMON.enableNestGen.get()) {
				gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.CRYSTAL_TERMITE_NEST);
			}
		}
		
//		private static void addDangerDimOres(BiomeGenerationSettingsBuilder gen) {
//			if (CAConfig.COMMON.enableNestGen.get()) {
//				gen.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.UNSTABLE_ANT_NEST);
//			}
//		}
		
//		private static void addUtopiaDimOres(BiomeGenerationSettingsBuilder gen) {
//			if (CAConfig.COMMON.enableNestGen.get()) {
//				gen.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, CAConfiguredFeatures.BROWN_ANT_NEST);
//			}
//		}
	}
}
