package io.github.chaosawakens.common.registry;

import com.google.common.collect.ImmutableList;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.common.worldgen.config.base.BiomeConfig;
import io.github.chaosawakens.common.worldgen.config.mining_paradise.biome.DensePlainsBiomeConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Supplier;

@RegistrarEntry
public class CABiomes {
    private static final ObjectArrayList<Supplier<ResourceKey<Biome>>> BIOMES = new ObjectArrayList<>();

    // Mining Paradise
    public static final Supplier<ResourceKey<Biome>> DENSE_PLAINS = registerBiome("dense_plains", DensePlainsBiomeConfig::new);

    private static Supplier<ResourceKey<Biome>> registerBiome(ResourceLocation id, Supplier<BiomeConfig> biomeConfigSup) {
        Supplier<ResourceKey<Biome>> biomeSup = CAServices.REGISTRAR.registerDatapackObject(id, b -> () -> biomeConfigSup.get().createBiome(b), Registries.BIOME);
        BIOMES.add(biomeSup);
        return biomeSup;
    }

    private static Supplier<ResourceKey<Biome>> registerBiome(String id, Supplier<BiomeConfig> biomeConfigSup) {
        return registerBiome(CAConstants.prefix(id), biomeConfigSup);
    }

    public static ImmutableList<Supplier<ResourceKey<Biome>>> getBiomes() {
        return ImmutableList.copyOf(BIOMES);
    }
}
