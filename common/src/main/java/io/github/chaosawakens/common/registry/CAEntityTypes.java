package io.github.chaosawakens.common.registry;

import com.google.common.collect.ImmutableList;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.entity.EntityTypePropertyWrapper;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.common.entity.hostile.robo.RoboPounder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

@RegistrarEntry
public class CAEntityTypes {
    private static final ObjectArrayList<Supplier<? extends EntityType<?>>> ENTITY_TYPES = new ObjectArrayList<>();

    // Robo
    public static final Supplier<? extends EntityType<?>> ROBO_POUNDER = EntityTypePropertyWrapper.createGeneric(
            registerEntityType("robo_pounder", () -> EntityType.Builder.of(RoboPounder::new, MobCategory.MONSTER)
                    .sized(4.1F, 6.0F)
                    .build(CAConstants.prefix("robo_pounder").toString())))
            .builder()
            .withAttributes(RoboPounder::createAttributes)
            .withClientDataEntry(CAClientDataEntries.ROBO_POUNDER)
            .build()
            .getParentEntityType();

    private static Supplier<? extends EntityType<?>> registerEntityType(ResourceLocation id, Supplier<? extends EntityType<?>> entityTypeSup) {
        Supplier<? extends EntityType<?>> registeredEntityTypeSup = CAServices.REGISTRAR.registerObject(id, entityTypeSup, BuiltInRegistries.ENTITY_TYPE); // Otherwise reference to the entity type sup is null cuz it needs to be registered b4hand
        ENTITY_TYPES.add(registeredEntityTypeSup);
        return registeredEntityTypeSup;
    }

    private static Supplier<? extends EntityType<?>> registerEntityType(String id, Supplier<? extends EntityType<?>> entityTypeSup) {
        return registerEntityType(CAConstants.prefix(id), entityTypeSup);
    }

    public static ImmutableList<Supplier<? extends EntityType<?>>> getEntityTypes() {
        return ImmutableList.copyOf(ENTITY_TYPES);
    }
}
