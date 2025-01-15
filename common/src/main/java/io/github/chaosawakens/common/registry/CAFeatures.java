package io.github.chaosawakens.common.registry;

import com.google.common.collect.ImmutableList;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.platform.CAServices;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;
import java.util.function.Supplier;

public class CAFeatures {

    @RegistrarEntry
    public static class CAConfiguredFeatures {
        private static final ObjectArrayList<Supplier<ResourceKey<ConfiguredFeature<?, ?>>>> CONFIGURED_FEATURES = new ObjectArrayList<>();

        // Bonemeal
        public static final Supplier<ResourceKey<ConfiguredFeature<?, ?>>> SINGLE_DENSE_GRASS = registerConfiguredFeature("single_dense_grass", () -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(CABlocks.DENSE_GRASS.get()))));
        public static final Supplier<ResourceKey<ConfiguredFeature<?, ?>>> SINGLE_CRYSTAL_GRASS = registerConfiguredFeature("single_crystal_grass", () -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(CABlocks.CRYSTAL_GRASS.get()))));

        private static Supplier<ResourceKey<ConfiguredFeature<?, ?>>> registerConfiguredFeature(ResourceLocation id, Supplier<ConfiguredFeature<?, ?>> actualPlacedFeatureSup) {
            Supplier<ResourceKey<ConfiguredFeature<?, ?>>> placedFeatureSup = CAServices.REGISTRAR.registerDatapackObject(id, b -> actualPlacedFeatureSup, Registries.CONFIGURED_FEATURE);
            CONFIGURED_FEATURES.add(placedFeatureSup);
            return placedFeatureSup;
        }

        private static Supplier<ResourceKey<ConfiguredFeature<?, ?>>> registerConfiguredFeature(String id, Supplier<ConfiguredFeature<?, ?>> actualPlacedFeatureSup) {
            return registerConfiguredFeature(CAConstants.prefix(id), actualPlacedFeatureSup);
        }

        public static ImmutableList<Supplier<ResourceKey<ConfiguredFeature<?, ?>>>> getConfiguredFeatures() {
            return ImmutableList.copyOf(CONFIGURED_FEATURES);
        }
    }

    @RegistrarEntry
    public static class CAPlacedFeatures {
        private static final ObjectArrayList<Supplier<ResourceKey<PlacedFeature>>> PLACED_FEATURES = new ObjectArrayList<>();

        // Bonemeal
        public static final Supplier<ResourceKey<PlacedFeature>> DENSE_GRASS_BONEMEAL = registerPlacedFeature("dense_grass_bonemeal", CAConfiguredFeatures.SINGLE_DENSE_GRASS, ObjectArrayList.of(PlacementUtils.isEmpty()));
        public static final Supplier<ResourceKey<PlacedFeature>> CRYSTAL_GRASS_BONEMEAL = registerPlacedFeature("crystal_grass_bonemeal", CAConfiguredFeatures.SINGLE_CRYSTAL_GRASS, ObjectArrayList.of(PlacementUtils.isEmpty()));

        private static Supplier<ResourceKey<PlacedFeature>> registerPlacedFeature(ResourceLocation id, Supplier<ResourceKey<ConfiguredFeature<?, ?>>> configuredFeatureHolder, List<PlacementModifier> placementModifiers) {
            Supplier<ResourceKey<PlacedFeature>> placedFeatureSup = CAServices.REGISTRAR.registerDatapackObject(id, b -> () -> new PlacedFeature(b.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuredFeatureHolder.get()), List.copyOf(placementModifiers)), Registries.PLACED_FEATURE);
            PLACED_FEATURES.add(placedFeatureSup);
            return placedFeatureSup;
        }

        private static Supplier<ResourceKey<PlacedFeature>> registerPlacedFeature(String id, Supplier<ResourceKey<ConfiguredFeature<?, ?>>> configuredFeatureHolder, List<PlacementModifier> placementModifiers) {
            return registerPlacedFeature(CAConstants.prefix(id), configuredFeatureHolder, placementModifiers);
        }

        public static ImmutableList<Supplier<ResourceKey<PlacedFeature>>> getPlacedFeatures() {
            return ImmutableList.copyOf(PLACED_FEATURES);
        }
    }
}
