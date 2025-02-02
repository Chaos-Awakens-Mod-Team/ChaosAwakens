package io.github.chaosawakens.api.entity;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.function.Function;
import java.util.function.Supplier;

public record ClientDataEntry(ResourceLocation entityTypeId, Function<Supplier<EntityRendererProvider.Context>, Supplier<EntityRenderer>> renderFactory, ObjectObjectImmutablePair<Supplier<ModelLayerLocation>, Supplier<LayerDefinition>> modelPair) {

    public ClientDataEntry(Supplier<? extends EntityType<?>> targetEntityType, Function<Supplier<EntityRendererProvider.Context>, Supplier<EntityRenderer>> renderFactory, ObjectObjectImmutablePair<Supplier<ModelLayerLocation>, Supplier<LayerDefinition>> modelPair) {
        this(BuiltInRegistries.ENTITY_TYPE.getKey(targetEntityType.get()), renderFactory, modelPair);
    }
}
