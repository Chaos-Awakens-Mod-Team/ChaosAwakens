package io.github.chaosawakens.datagen;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.services.ForgeRegistrar;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class CADatapackRegistryProvider extends DatapackBuiltinEntriesProvider {

    public CADatapackRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ForgeRegistrar.getDatapackRegistrySetBuilder(), Collections.singleton(CAConstants.MODID));
    }

    @Override
    public @NotNull String getName() {
        return CAConstants.MOD_NAME.concat(" Datapack Registries");
    }
}
