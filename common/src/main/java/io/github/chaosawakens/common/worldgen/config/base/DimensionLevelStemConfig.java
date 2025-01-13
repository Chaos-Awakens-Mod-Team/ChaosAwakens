package io.github.chaosawakens.common.worldgen.config.base;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Base interface for centralization of dimension configs in code through rote/simplistic inheritance and modularization. Primarily used to register custom {@linkplain LevelStem LevelStems}. This is usually where
 * higher-level worldgen mechanics, most notably chunk generation, are handled. Lower-level worldgen settings/mechanics (placed features, structures, etc.) are handled by biomes.
 *
 * @see BiomeConfig
 */
public interface DimensionLevelStemConfig {

    /**
     * The {@link DimensionType} this config belongs to. May not be {@code null}.
     *
     * @return The {@link DimensionType} this config belongs to.
     */
    @NotNull
    Supplier<ResourceKey<DimensionType>> getParentDimensionType();

    /**
     * The method responsible for initializing the parent dimension's {@linkplain LevelStem LevelStem's} chunk generation and all of its features.
     *
     * @param regCtx The {@link BootstapContext} used during registration. Useful for miscellaneous registry lookups in order to properly configure chunk generation. Don't mind the generic type.
     *
     * @return The parent dimension's {@linkplain LevelStem LevelStem's} chunk gen. May not be {@code null}.
     *
     * @see ChunkGenerator
     */
    @NotNull
    ChunkGenerator createLevelChunkGen(BootstapContext<LevelStem> regCtx);
}
