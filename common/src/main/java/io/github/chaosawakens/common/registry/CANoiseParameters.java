package io.github.chaosawakens.common.registry;

import com.google.common.collect.ImmutableList;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.platform.CAServices;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.function.Supplier;

@RegistrarEntry
public class CANoiseParameters {
    private static final ObjectArrayList<Supplier<ResourceKey<NormalNoise.NoiseParameters>>> NOISE_PARAMETERS = new ObjectArrayList<>();

    // Mining Paradise
    public static final Supplier<ResourceKey<NormalNoise.NoiseParameters>> MINING_PARADISE_CONTINENTALNESS = registerNoiseParameter("mining_paradise/continentalness", () -> new NormalNoise.NoiseParameters(-10, 1.5D, 1.0D, 1.0D, 2.0D, 2.0D, 2.0D, 1.0D, 1.0D, 1.0D));
    public static final Supplier<ResourceKey<NormalNoise.NoiseParameters>> MINING_PARADISE_EROSION = registerNoiseParameter("mining_paradise/erosion", () -> new NormalNoise.NoiseParameters(-12, 1.75D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 1.0D));

    private static Supplier<ResourceKey<NormalNoise.NoiseParameters>> registerNoiseParameter(ResourceLocation id, Supplier<NormalNoise.NoiseParameters> actualNoiseParamSup) {
        Supplier<ResourceKey<NormalNoise.NoiseParameters>> noiseParamSup = CAServices.REGISTRAR.registerDatapackObject(id, b -> actualNoiseParamSup, Registries.NOISE);
        NOISE_PARAMETERS.add(noiseParamSup);
        return noiseParamSup;
    }

    private static Supplier<ResourceKey<NormalNoise.NoiseParameters>> registerNoiseParameter(String id, Supplier<NormalNoise.NoiseParameters> actualNoiseParamSup) {
        return registerNoiseParameter(CAConstants.prefix(id), actualNoiseParamSup);
    }

    public static ImmutableList<Supplier<ResourceKey<NormalNoise.NoiseParameters>>> getNoiseParameters() {
        return ImmutableList.copyOf(NOISE_PARAMETERS);
    }
}
