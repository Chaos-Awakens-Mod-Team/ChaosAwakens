package io.github.chaosawakens.api.services;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.api.platform.services.IRegistrar;
import it.unimi.dsi.fastutil.objects.ObjectObjectMutablePair;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;
import java.util.function.Supplier;

public class FabricRegistrar implements IRegistrar {
    private static final Multimap<ResourceKey<? extends Registry<?>>, ObjectObjectMutablePair<ResourceKey<?>, Function<? extends BootstapContext<?>, ? extends Supplier<?>>>> CACHED_DATAPACK_OBJECT_ENTRIES = ArrayListMultimap.create();
    private static RegistrySetBuilder DATAPACK_REGISTRY_SET_BUILDER;

    @Override
    public void setupRegistrar() {
        CAServices.PLATFORM.discoverAnnotatedClasses(RegistrarEntry.class);

        CACHED_DATAPACK_OBJECT_ENTRIES.asMap().forEach((registryKey, objSupMappingFuncs) -> {
            getDatapackRegistrySetBuilder().add(tCastRegistryKey(registryKey), b -> objSupMappingFuncs.forEach(mappedObjKey -> {
                b.register(tCastObjectKey(mappedObjKey.left()), tCastObsSupMappingFunc(mappedObjKey.right(), b).get());
            }));
        });
    }

    @Override
    public <V, T extends V> Supplier<T> registerObject(ResourceLocation objId, Supplier<T> objSup, Registry<V> targetRegistry) {
        T targetObject = Registry.register(targetRegistry, objId, objSup.get()); // Must store in a local field beforehand cuz... for some reason it's null if inlined (:bruhcat:)
        return () -> targetObject;
    }

    @Override
    public <T> Supplier<ResourceKey<T>> registerDatapackObject(ResourceLocation objId, Function<BootstapContext<T>, Supplier<T>> objSupMappingFunc, ResourceKey<Registry<T>> targetDatapackRegistry) {
        ResourceKey<T> targetObject = ResourceKey.create(targetDatapackRegistry, objId);

        if (objSupMappingFunc != null) {
            CACHED_DATAPACK_OBJECT_ENTRIES.put(targetDatapackRegistry, ObjectObjectMutablePair.of(targetObject, objSupMappingFunc)); // Need to use this approach since RSBs don't support stacking registration calls and I'm too stubborn to diverge from the otherwise straightforward ML registration approach I've been trying to facilitate :trol:
        }

        return () -> targetObject;
    }

    protected <T> Supplier<T> tCastObsSupMappingFunc(Function<? extends BootstapContext<?>, ? extends Supplier<?>> objSupMappingFunc, BootstapContext<T> bootstapContext) { // I love wildcard casts
        return ((Function<BootstapContext<T>, Supplier<T>>) objSupMappingFunc).apply(bootstapContext);
    }

    protected <T> ResourceKey<T> tCastObjectKey(ResourceKey<?> objectKey) {
        return (ResourceKey<T>) objectKey;
    }

    protected <T> ResourceKey<Registry<T>> tCastRegistryKey(ResourceKey<? extends Registry<?>> registryKey) {
        return (ResourceKey<Registry<T>>) registryKey;
    }

    public static RegistrySetBuilder getDatapackRegistrySetBuilder() {
        return DATAPACK_REGISTRY_SET_BUILDER == null ? DATAPACK_REGISTRY_SET_BUILDER = new RegistrySetBuilder() : DATAPACK_REGISTRY_SET_BUILDER;
    }
}
