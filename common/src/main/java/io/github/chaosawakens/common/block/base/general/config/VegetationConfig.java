package io.github.chaosawakens.common.block.base.general.config;

import io.github.chaosawakens.common.block.vegetation.general.config.DefaultDirtVegetationConfig;
import io.github.chaosawakens.common.block.vegetation.general.config.DefaultGrassVegetationConfig;
import io.github.chaosawakens.common.block.vegetation.general.config.DefaultPlantVegetationConfig;
import io.github.chaosawakens.common.registry.CABlocks;
import io.github.chaosawakens.common.registry.CAFeatures;
import io.github.chaosawakens.common.registry.CATags;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public enum VegetationConfig implements StandardVegetationConfig { // This could probably be cleaner :skull:
    DEFAULT(DefaultDirtVegetationConfig::new, DefaultGrassVegetationConfig::new, DefaultPlantVegetationConfig::new),
    DENSE(() -> new DefaultDirtVegetationConfig(CABlocks.DENSE_DIRT), () -> new DefaultGrassVegetationConfig(Optional.of(regAccess -> regAccess.lookup(Registries.PLACED_FEATURE).get().getOrThrow(CAFeatures.CAPlacedFeatures.DENSE_GRASS_BONEMEAL.get()))), () -> new DefaultPlantVegetationConfig(CABlocks.DENSE_GRASS).withPlacementPredicate(targetState -> targetState.is(BlockTags.DIRT) || targetState.is(CATags.CABlockTags.FARMLAND_BLOCKS.get()))),
    DENSE_BUSH(() -> new DefaultDirtVegetationConfig(CABlocks.DENSE_DIRT), () -> new DefaultGrassVegetationConfig(Optional.of(regAccess -> regAccess.lookup(Registries.PLACED_FEATURE).get().getOrThrow(CAFeatures.CAPlacedFeatures.DENSE_GRASS_BONEMEAL.get()))), () -> new DefaultPlantVegetationConfig(CABlocks.BUSH).withPlacementPredicate(targetState -> targetState.is(BlockTags.DIRT) || targetState.is(CATags.CABlockTags.FARMLAND_BLOCKS.get()))),
    TERRA_PRETA(() -> new DefaultDirtVegetationConfig(CABlocks.TERRA_PRETA), null, null),
    CRYSTAL(null, () -> new DefaultGrassVegetationConfig(Optional.empty()), null);

    private final Supplier<DirtVegetationConfig> dirtConfig;
    private final Supplier<GrassVegetationConfig> grassConfig;
    private final Supplier<PlantVegetationConfig> plantConfig;

    VegetationConfig(@Nullable Supplier<DirtVegetationConfig> dirtConfig, @Nullable Supplier<GrassVegetationConfig> grassConfig, @Nullable Supplier<PlantVegetationConfig> plantConfig) {
        this.dirtConfig = dirtConfig;
        this.grassConfig = grassConfig;
        this.plantConfig = plantConfig;
    }

    @Override
    public @Nullable Supplier<DirtVegetationConfig> getDirtConfig() {
        return dirtConfig;
    }

    @Override
    public @Nullable Supplier<GrassVegetationConfig> getGrassConfig() {
        return grassConfig;
    }

    @Override
    public @Nullable Supplier<PlantVegetationConfig> getPlantConfig() {
        return plantConfig;
    }
}
