package io.github.chaosawakens.common.worldgen.config.mining_paradise;

import io.github.chaosawakens.common.registry.CABiomes;
import io.github.chaosawakens.common.registry.CADimensions;
import io.github.chaosawakens.common.registry.CASurfaceRules;
import io.github.chaosawakens.common.worldgen.config.base.DimensionLevelStemConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalLong;
import java.util.function.Supplier;

public class MiningParadiseDimensionConfig implements DimensionLevelStemConfig {
    public static final NoiseSettings BASE_MP_NOISE_SETTINGS = NoiseSettings.create(-256, 736, 1, 2);

    public MiningParadiseDimensionConfig() {}

    @Override
    public @NotNull Supplier<ResourceKey<DimensionType>> getParentDimensionType() {
        return CADimensions.MINING_PARADISE_DIMENSION_TYPE;
    }

    @Override
    public @NotNull ChunkGenerator createLevelChunkGen(BootstapContext<LevelStem> regCtx) {
        HolderGetter<Biome> biomeLookup = regCtx.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypeLookup = regCtx.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettingsLookup = regCtx.lookup(Registries.NOISE_SETTINGS);

        BiomeSource src = new FixedBiomeSource(biomeLookup.getOrThrow(CABiomes.DENSE_PLAINS.get()));
        Holder.Reference<NoiseGeneratorSettings> settings = noiseGenSettingsLookup.getOrThrow(NoiseGeneratorSettings.OVERWORLD);

        return new NoiseBasedChunkGenerator(src, settings);
    }

    public static DimensionType createDimensionType() {
        return new DimensionType(OptionalLong.empty(), true, false, false, false, 1.0D, true, false, -256, 736, 480, BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS, 0.0F, new DimensionType.MonsterSettings(false ,false, ConstantInt.of(4), 0));
    }

    public static Supplier<NoiseGeneratorSettings> createMiningParadiseNoiseGenSettings(BootstapContext<NoiseGeneratorSettings> regCtx) {
        return () -> new NoiseGeneratorSettings(BASE_MP_NOISE_SETTINGS, Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), NoiseRouterData.overworld(regCtx.lookup(Registries.DENSITY_FUNCTION), regCtx.lookup(Registries.NOISE), false, false), createMiningParadiseSurfaceRules(), (new OverworldBiomeBuilder()).spawnTarget(), 128, false, true, true, false);
    }

    public static SurfaceRules.RuleSource createMiningParadiseSurfaceRules() {
        SurfaceRules.RuleSource surfaceRuleSource = SurfaceRules.sequence(SurfaceRules.ifTrue(CASurfaceRules.CAConditionSources.AT_ABOVE_WATER_LEVEL, CASurfaceRules.CAStateRules.DENSE_GRASS_BLOCK), CASurfaceRules.CAStateRules.DENSE_DIRT);
        SurfaceRules.RuleSource bedrockFloorRuleSource = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), CASurfaceRules.CAStateRules.BEDROCK));
        SurfaceRules.RuleSource densePlainsRuleSource = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(CABiomes.DENSE_PLAINS.get()), SurfaceRules.sequence(surfaceRuleSource)));

        return SurfaceRules.sequence(densePlainsRuleSource, bedrockFloorRuleSource);
    }
}
