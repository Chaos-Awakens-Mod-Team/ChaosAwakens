package io.github.chaosawakens.common.worldgen.config.mining_paradise;

import io.github.chaosawakens.common.registry.*;
import io.github.chaosawakens.common.worldgen.config.base.DimensionLevelStemConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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

    public MiningParadiseDimensionConfig() {
    }

    @Override
    public @NotNull Supplier<ResourceKey<DimensionType>> getParentDimensionType() {
        return CADimensions.MINING_PARADISE_DIMENSION_TYPE;
    }

    @Override
    public @NotNull ChunkGenerator createLevelChunkGen(BootstapContext<LevelStem> regCtx) {
        HolderGetter<Biome> biomeLookup = regCtx.lookup(Registries.BIOME);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettingsLookup = regCtx.lookup(Registries.NOISE_SETTINGS);

        BiomeSource src = new FixedBiomeSource(biomeLookup.getOrThrow(CABiomes.DENSE_PLAINS.get()));
        Holder.Reference<NoiseGeneratorSettings> settings = noiseGenSettingsLookup.getOrThrow(CANoiseGeneratorSettings.MINING_PARADISE.get());

        return new NoiseBasedChunkGenerator(src, settings);
    }

    public static DimensionType createDimensionType() {
        return new DimensionType(OptionalLong.empty(), true, false, false, false, 1.0D, true, false, -256, 736, 480, BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS, 0.0F, new DimensionType.MonsterSettings(false, false, ConstantInt.of(4), 0));
    }

    public static Supplier<NoiseGeneratorSettings> createMiningParadiseNoiseGenSettings(BootstapContext<NoiseGeneratorSettings> regCtx) {
        return () -> new NoiseGeneratorSettings(BASE_MP_NOISE_SETTINGS, Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(), createMiningParadiseNoiseRouter(regCtx), createMiningParadiseSurfaceRules(), ObjectArrayList.of(), 121, false, true, true, false);
    }

    protected static SurfaceRules.RuleSource createMiningParadiseSurfaceRules() {
        SurfaceRules.RuleSource defaultSurfaceRuleSource = SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                        SurfaceRules.abovePreliminarySurface(),
                        SurfaceRules.ifTrue(
                                SurfaceRules.ON_FLOOR,
                                SurfaceRules.sequence(
                                        SurfaceRules.ifTrue(
                                                CASurfaceRules.CAConditionSources.AT_ABOVE_WATER_LEVEL,
                                                CASurfaceRules.CAStateRules.DENSE_GRASS_BLOCK
                                        ),
                                        CASurfaceRules.CAStateRules.DENSE_DIRT
                                )
                        )
                ),
                SurfaceRules.ifTrue(
                        SurfaceRules.UNDER_FLOOR,
                        SurfaceRules.ifTrue(
                                SurfaceRules.not(SurfaceRules.hole()),
                                CASurfaceRules.CAStateRules.DENSE_DIRT
                        )
                )
        );
        SurfaceRules.RuleSource densePlainsRuleSource = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(CABiomes.DENSE_PLAINS.get()), defaultSurfaceRuleSource));
        SurfaceRules.RuleSource bedrockFloorRuleSource = SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), CASurfaceRules.CAStateRules.BEDROCK);

        return SurfaceRules.sequence(densePlainsRuleSource, bedrockFloorRuleSource);
    }

    protected static NoiseRouter createMiningParadiseNoiseRouter(BootstapContext<NoiseGeneratorSettings> regCtx) {
        DensityFunction zero = DensityFunctions.zero();
        DensityFunction shiftX = CADensityFunctions.getWrappedDensityFunctionHolder(regCtx, CADensityFunctions.SHIFT_X);
        DensityFunction shiftZ = CADensityFunctions.getWrappedDensityFunctionHolder(regCtx, CADensityFunctions.SHIFT_Z);
        DensityFunction shiftedTemperature = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, regCtx.lookup(Registries.NOISE).getOrThrow(Noises.TEMPERATURE));
        DensityFunction shiftedVegetation = DensityFunctions.shiftedNoise2d(shiftX, shiftZ, 0.25D, regCtx.lookup(Registries.NOISE).getOrThrow(Noises.VEGETATION));
        DensityFunction landContinents = CADensityFunctions.getWrappedDensityFunctionHolder(regCtx, CADensityFunctions.MINING_PARADISE_CONTINENTS);
        DensityFunction landErosion = CADensityFunctions.getWrappedDensityFunctionHolder(regCtx, CADensityFunctions.MINING_PARADISE_EROSION);
        DensityFunction terrainJaggedness = CADensityFunctions.getWrappedDensityFunctionHolder(regCtx, CADensityFunctions.MINING_PARADISE_JAGGEDNESS);
        DensityFunction terrainFactor = CADensityFunctions.getWrappedDensityFunctionHolder(regCtx, CADensityFunctions.MINING_PARADISE_FACTOR);
        DensityFunction terrainDepth = CADensityFunctions.getWrappedDensityFunctionHolder(regCtx, CADensityFunctions.MINING_PARADISE_DEPTH);
        DensityFunction continentRidges = CADensityFunctions.getWrappedDensityFunctionHolder(regCtx, CADensityFunctions.RIDGES);
        DensityFunction initialLandDensity = NoiseRouterData.slide(DensityFunctions.add(NoiseRouterData.noiseGradientDensity(DensityFunctions.cache2d(terrainFactor), terrainDepth), DensityFunctions.constant(-0.803125D)).clamp(-128.0D, 128.0D), -256, 480, 121, 84, -0.198425D, 32, 66, 0.5271875D);
        DensityFunction finalLandDensity = DensityFunctions.mul(DensityFunctions.interpolated(DensityFunctions.blendDensity(initialLandDensity)), DensityFunctions.constant(0.96D)).squeeze();

        return new NoiseRouter(
                zero,
                zero,
                zero,
                zero,
                shiftedTemperature,
                shiftedVegetation,
                landContinents,
                landErosion,
                terrainDepth,
                continentRidges,
                initialLandDensity,
                finalLandDensity,
                zero,
                zero,
                zero);
    }
}