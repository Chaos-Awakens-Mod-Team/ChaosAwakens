package io.github.chaosawakens.common.registry;

import com.google.common.collect.ImmutableList;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.damage_type.DamageTypeWrapper;
import io.github.chaosawakens.api.platform.CAServices;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class CADamageEntries {

    @RegistrarEntry
    public static class CADamageTypes {
        private static final ObjectArrayList<Supplier<ResourceKey<DamageType>>> DAMAGE_TYPES = new ObjectArrayList<>();

        // Block
        public static final Supplier<ResourceKey<DamageType>> THORNY_SUN = DamageTypeWrapper.create(registerDamageType("thorny_sun", () -> new DamageType("thorny_sun", 0.1F)))
                .builder()
                .withCopiedMsgId("thorny_sun")
                .withLocalizedDeathMessageComponent(Component.literal("%1$s was pricked to death by a Thorny Sun"))
                .build()
                .getOwnerDamageType();

        public static final Supplier<ResourceKey<DamageType>> BIG_CARNIVOROUS_PLANT = DamageTypeWrapper.create(registerDamageType("big_carnivorous_plant", () -> new DamageType("big_carnivorous_plant", 0.1F)))
                .builder()
                .withCopiedMsgId("big_carnivorous_plant")
                .withLocalizedDeathMessageComponent(Component.literal("%1$s was bitten to death by a Big Carnivorous Plant"))
                .build()
                .getOwnerDamageType();


        private static Supplier<ResourceKey<DamageType>> registerConfiguredFeature(ResourceLocation id, Supplier<DamageType> actualDamageTypeSup) {
            Supplier<ResourceKey<DamageType>> damageTypeSup = CAServices.REGISTRAR.registerDatapackObject(id, b -> actualDamageTypeSup, Registries.DAMAGE_TYPE);
            DAMAGE_TYPES.add(damageTypeSup);
            return damageTypeSup;
        }

        private static Supplier<ResourceKey<DamageType>> registerDamageType(String id, Supplier<DamageType> actualDamageTypeSup) {
            return registerConfiguredFeature(CAConstants.prefix(id), actualDamageTypeSup);
        }

        public static ImmutableList<Supplier<ResourceKey<DamageType>>> getDamageTypes() {
            return ImmutableList.copyOf(DAMAGE_TYPES);
        }
    }

    @RegistrarEntry // Probably unnecessary idk
    public static class CADamageSources {

        public static DamageSource thornySun(Level targetLevel) {
            return new DamageSource(targetLevel.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(CADamageTypes.THORNY_SUN.get()), null, null);
        }

        public static DamageSource bigCarnivorousPlant(Level targetLevel) {
            return new DamageSource(targetLevel.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(CADamageTypes.BIG_CARNIVOROUS_PLANT.get()), null, null);
        }
    }
}
