package io.github.chaosawakens.common.registry;

import com.google.common.collect.ImmutableList;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.common.worldgen.config.mining_paradise.MiningParadiseDimensionConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.function.Function;
import java.util.function.Supplier;

@RegistrarEntry
public class CANoiseGeneratorSettings {
    private static final ObjectArrayList<Supplier<ResourceKey<NoiseGeneratorSettings>>> NOISE_GENERATOR_SETTINGS = new ObjectArrayList<>();

    // Mining Paradise
    public static final Supplier<ResourceKey<NoiseGeneratorSettings>> MINING_PARADISE = registerNoiseGeneratorSetting("mining_paradise", MiningParadiseDimensionConfig::createMiningParadiseNoiseGenSettings);

    private static Supplier<ResourceKey<NoiseGeneratorSettings>> registerNoiseGeneratorSetting(ResourceLocation id, Function<BootstapContext<NoiseGeneratorSettings>, Supplier<NoiseGeneratorSettings>> ngsConfigFunc) {
        Supplier<ResourceKey<NoiseGeneratorSettings>> ngsSup = CAServices.REGISTRAR.registerDatapackObject(id, ngsConfigFunc, Registries.NOISE_SETTINGS);
        NOISE_GENERATOR_SETTINGS.add(ngsSup);
        return ngsSup;
    }

    private static Supplier<ResourceKey<NoiseGeneratorSettings>> registerNoiseGeneratorSetting(String id, Function<BootstapContext<NoiseGeneratorSettings>, Supplier<NoiseGeneratorSettings>> ngsConfigFunc) {
        return registerNoiseGeneratorSetting(CAConstants.prefix(id), ngsConfigFunc);
    }

    public static ImmutableList<Supplier<ResourceKey<NoiseGeneratorSettings>>> getNoiseGeneratorSettings() {
        return ImmutableList.copyOf(NOISE_GENERATOR_SETTINGS);
    }
}
