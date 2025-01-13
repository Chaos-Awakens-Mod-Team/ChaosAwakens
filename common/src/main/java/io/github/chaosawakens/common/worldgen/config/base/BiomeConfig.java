package io.github.chaosawakens.common.worldgen.config.base;

import net.minecraft.data.worldgen.biome.EndBiomes;
import net.minecraft.data.worldgen.biome.NetherBiomes;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base interface responsible for passing biome configurations into custom-registered {@linkplain Biome Biomes}. This is effectively the config that manages mob spawns and feature placements within biomes. Chunk generation
 * (and all of its settings/mechanics) is handled by dimensions.
 *
 * @see DimensionLevelStemConfig
 */
public interface BiomeConfig {

    /**
     * The primary method responsible for constructing this config's parent {@link Biome} using the rest of the config methods. Override if you want to directly intercept biome initialization/configuration and modify it
     * according to your needs.
     *
     * @return This config's parent {@link Biome}, passed in during registration. May not be {@code null}.
     *
     * @see OverworldBiomes
     * @see NetherBiomes
     * @see EndBiomes
     */
    @NotNull
    default Biome createBiome() {
        Biome.BiomeBuilder biomeBuilder = new Biome.BiomeBuilder();

        biomeBuilder.hasPrecipitation(hasPrecipitation());
        biomeBuilder.temperature(getTemperature());
        biomeBuilder.downfall(getDownfall());

        biomeBuilder.specialEffects(getSpecialEffects() == null ? new BiomeSpecialEffects.Builder()
                        .fogColor(2989342)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .skyColor(103877)
                .build() : getSpecialEffects());
        biomeBuilder.mobSpawnSettings(getMobSpawnSettings() == null ? new MobSpawnSettings.Builder().build() : getMobSpawnSettings());
        biomeBuilder.generationSettings(getGenerationSettings() == null ? new BiomeGenerationSettings.PlainBuilder().build() : getGenerationSettings());

        if (getTemperatureModifier() != null) biomeBuilder.temperatureAdjustment(getTemperatureModifier());

        return biomeBuilder.build();
    }

    /**
     * Whether the parent {@link Biome} should experience precipitation of any type (snow, rain, etc.).
     *
     * @return Whether the parent {@link Biome} should experience precipitation of any type.
     *
     * @see Biome.Precipitation
     */
    boolean hasPrecipitation();

    /**
     * The temperature of the parent {@link Biome} (hot means less precipitation, cold means more precipitation).
     *
     * @return The temperature of the parent {@link Biome}.
     */
    float getTemperature();

    /**
     * The amount of precipitation the parent {@link Biome} should experience (raining, snowing, etc.).
     *
     * @return The amount of precipitation the parent {@link Biome} should experience.
     */
    float getDownfall();

    /**
     * The visual effects pertaining to elements of the parent {@link Biome} (fog color, water color, grass color, etc.). May be {@code null}.
     *
     * @return The visual effects pertaining to elements of the parent {@link Biome}.
     */
    @Nullable
    BiomeSpecialEffects getSpecialEffects();

    /**
     * The spawn configuration for different mob types in the parent {@link Biome}. May be {@code null}.
     *
     * @return The spawn configuration for different mob types in the parent {@link Biome}.
     */
    @Nullable
    MobSpawnSettings getMobSpawnSettings();

    /**
     * The generation settings used to effectively post-process the parent {@link Biome} ({@linkplain CarverConfiguration Cave Carvers}, placed features, etc.). May be {@code null}.
     *
     * @return The generation settings used to effectively post-process the parent {@link Biome}.
     */
    @Nullable
    BiomeGenerationSettings getGenerationSettings();

    /**
     * The temperature modifier used to determine whether the parent {@link Biome} should undergo temperature modifications based on its default temperature configuration or
     * undergo special "frozen" temperature modifications. May be {@code null}.
     *
     * @return The temperature modifier for the parent {@link Biome}.
     */
    @Nullable
    Biome.TemperatureModifier getTemperatureModifier();
}
