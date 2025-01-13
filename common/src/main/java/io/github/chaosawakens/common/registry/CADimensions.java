package io.github.chaosawakens.common.registry;

import com.google.common.collect.ImmutableList;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.common.worldgen.config.base.DimensionLevelStemConfig;
import io.github.chaosawakens.common.worldgen.config.mining_paradise.MiningParadiseDimensionConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.function.Supplier;

@RegistrarEntry
public class CADimensions {
    private static final ObjectArrayList<Supplier<ResourceKey<DimensionType>>> DIMENSION_TYPES = new ObjectArrayList<>();
    private static final ObjectArrayList<Supplier<ResourceKey<LevelStem>>> LEVEL_STEMS = new ObjectArrayList<>();
    private static final ObjectArrayList<Supplier<ResourceKey<Level>>> LEVEL_KEYS = new ObjectArrayList<>();

    // Mining Paradise
    public static final Supplier<ResourceKey<DimensionType>> MINING_PARADISE_DIMENSION_TYPE = registerDimensionType("mining_paradise", MiningParadiseDimensionConfig::createDimensionType);
    public static final Supplier<ResourceKey<LevelStem>> MINING_PARADISE_LEVEL_STEM = registerLevelStem("mining_paradise", MiningParadiseDimensionConfig::new);
    public static final Supplier<ResourceKey<Level>> MINING_PARADISE_LEVEL_KEY = registerLevelKey("mining_paradise");

    private static Supplier<ResourceKey<DimensionType>> registerDimensionType(ResourceLocation id, Supplier<DimensionType> actualDimTypeSup) {
        Supplier<ResourceKey<DimensionType>> dimensionTypeSup = CAServices.REGISTRAR.registerDatapackObject(id, b -> actualDimTypeSup, Registries.DIMENSION_TYPE);
        DIMENSION_TYPES.add(dimensionTypeSup);
        return dimensionTypeSup;
    }

    private static Supplier<ResourceKey<LevelStem>> registerLevelStem(ResourceLocation id, Supplier<DimensionLevelStemConfig> dimensionConfigSup) {
        Supplier<ResourceKey<LevelStem>> levelStemSup = CAServices.REGISTRAR.registerDatapackObject(id, b -> () -> new LevelStem(b.lookup(Registries.DIMENSION_TYPE).getOrThrow(dimensionConfigSup.get().getParentDimensionType().get()), dimensionConfigSup.get().createLevelChunkGen(b)), Registries.LEVEL_STEM);
        LEVEL_STEMS.add(levelStemSup);
        return levelStemSup;
    }

    private static Supplier<ResourceKey<Level>> registerLevelKey(ResourceLocation id) {
        Supplier<ResourceKey<Level>> levelKeySup = CAServices.REGISTRAR.registerDatapackObject(id, null, Registries.DIMENSION);
        LEVEL_KEYS.add(levelKeySup);
        return levelKeySup;
    }

    private static Supplier<ResourceKey<DimensionType>> registerDimensionType(String id, Supplier<DimensionType> actualDimTypeSup) {
        return registerDimensionType(CAConstants.prefix(id), actualDimTypeSup);
    }

    private static Supplier<ResourceKey<LevelStem>> registerLevelStem(String id, Supplier<DimensionLevelStemConfig> dimensionConfigSup) {
        return registerLevelStem(CAConstants.prefix(id), dimensionConfigSup);
    }

    private static Supplier<ResourceKey<Level>> registerLevelKey(String id) {
        return registerLevelKey(CAConstants.prefix(id));
    }

    public static ImmutableList<Supplier<ResourceKey<DimensionType>>> getDimensionTypes() {
        return ImmutableList.copyOf(DIMENSION_TYPES);
    }

    public static ImmutableList<Supplier<ResourceKey<LevelStem>>> getLevelStems() {
        return ImmutableList.copyOf(LEVEL_STEMS);
    }

    public static ImmutableList<Supplier<ResourceKey<Level>>> getLevelKeys() {
        return ImmutableList.copyOf(LEVEL_KEYS);
    }
}
