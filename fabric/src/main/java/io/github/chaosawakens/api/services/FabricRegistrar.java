package io.github.chaosawakens.api.services;

import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.api.platform.services.IRegistrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class FabricRegistrar implements IRegistrar {

    @Override
    public void setupRegistrar() {
        CAServices.PLATFORM.discoverAnnotatedClasses(RegistrarEntry.class);
    }

    @Override
    public <V, T extends V> Supplier<T> registerObject(ResourceLocation objId, Supplier<T> objSup, Registry<V> targetRegistry) {
        T targetObject = Registry.register(targetRegistry, objId, objSup.get()); // Must store in a local field beforehand cuz... for some reason it's null if inlined (:bruhcat:)
        return () -> targetObject;
    }
}
