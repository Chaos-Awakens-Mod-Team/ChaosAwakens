package io.github.chaosawakens.common.worldgen.config.mining_paradise.biome;

import io.github.chaosawakens.common.worldgen.config.base.BiomeConfig;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.Nullable;

public class DensePlainsBiomeConfig implements BiomeConfig {

    public DensePlainsBiomeConfig() {}

    @Override
    public boolean hasPrecipitation() {
        return false;
    }

    @Override
    public float getTemperature() {
        return 0;
    }

    @Override
    public float getDownfall() {
        return 0;
    }

    @Override
    public @Nullable BiomeSpecialEffects getSpecialEffects() {
        return null;
    }

    @Override
    public @Nullable MobSpawnSettings getMobSpawnSettings() {
        return null;
    }

    @Override
    public @Nullable BiomeGenerationSettings getGenerationSettings() {
        return null;
    }

    @Override
    public @Nullable Biome.TemperatureModifier getTemperatureModifier() {
        return null;
    }
}
