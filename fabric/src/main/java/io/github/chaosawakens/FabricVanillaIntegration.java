package io.github.chaosawakens;

import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.api.entity.EntityTypePropertyWrapper;
import io.github.chaosawakens.api.item.ItemPropertyWrapper;
import io.github.chaosawakens.api.tag.TagWrapper;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import it.unimi.dsi.fastutil.objects.ObjectObjectMutablePair;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class FabricVanillaIntegration {

    static void handleVanillaIntegration() {
        BlockPropertyWrapper.getMappedBpws().forEach((parentBlockSup, curBpw) -> {
            IntIntMutablePair flammabilityPair = curBpw.getFlammabilityMappingFunc() == null ? null : curBpw.getFlammabilityMappingFunc().apply(parentBlockSup);
            Supplier<Block> strippedBlockVariant = curBpw.getBlockStrippingMappingFunc() == null ? null : curBpw.getBlockStrippingMappingFunc().apply(parentBlockSup);
            ObjectObjectMutablePair<Predicate<UseOnContext>, Consumer<UseOnContext>> parentBlockTillingBehaviourPair = curBpw.getBlockTillingMappingFunc() == null ? null : curBpw.getBlockTillingMappingFunc().apply(parentBlockSup);
            BlockState flattenedBlockVariant = curBpw.getBlockFlatteningMappingFunc() == null ? null : curBpw.getBlockFlatteningMappingFunc().apply(parentBlockSup);
            Supplier<Block> oxidizedBlockVariant = curBpw.getBlockOxidizationMappingFunc() == null ? null : curBpw.getBlockOxidizationMappingFunc().apply(parentBlockSup);
            Supplier<Block> waxedBlockVariant = curBpw.getBlockWaxingMappingFunc() == null ? null : curBpw.getBlockWaxingMappingFunc().apply(parentBlockSup);
            Float blockCompostChance = curBpw.getBlockCompostingMappingFunc() == null ? null : Math.abs(curBpw.getBlockCompostingMappingFunc().apply(parentBlockSup));
            Integer blockFuelCookTime = curBpw.getBlockFuelMappingFunc() == null ? null : Math.abs(curBpw.getBlockFuelMappingFunc().apply(parentBlockSup));

            if (flammabilityPair != null) FlammableBlockRegistry.getDefaultInstance().add(parentBlockSup.get(), Math.abs(flammabilityPair.leftInt()), Math.abs(flammabilityPair.rightInt()));
            if (strippedBlockVariant != null && strippedBlockVariant.get() != null) StrippableBlockRegistry.register(parentBlockSup.get(), strippedBlockVariant.get());
            if (parentBlockTillingBehaviourPair != null && parentBlockTillingBehaviourPair.left() != null && parentBlockTillingBehaviourPair.right() != null) TillableBlockRegistry.register(parentBlockSup.get(), parentBlockTillingBehaviourPair.left(), parentBlockTillingBehaviourPair.right());
            if (flattenedBlockVariant != null) FlattenableBlockRegistry.register(parentBlockSup.get(), flattenedBlockVariant);
            if (oxidizedBlockVariant != null && oxidizedBlockVariant.get() != null) OxidizableBlocksRegistry.registerOxidizableBlockPair(parentBlockSup.get(), oxidizedBlockVariant.get());
            if (waxedBlockVariant != null && waxedBlockVariant.get() != null) OxidizableBlocksRegistry.registerWaxableBlockPair(parentBlockSup.get(), waxedBlockVariant.get());
            if (blockCompostChance != null && blockCompostChance != 0) CompostingChanceRegistry.INSTANCE.add(parentBlockSup.get(), blockCompostChance);
            if (blockFuelCookTime != null && blockFuelCookTime != 0) FuelRegistry.INSTANCE.add(parentBlockSup.get(), blockFuelCookTime);
        });

        ItemPropertyWrapper.getMappedIpws().forEach((parentItemSup, curIpw) -> {
            Float itemCompostChance = curIpw.getCompostingMappingFunc() == null ? null : Math.abs(curIpw.getCompostingMappingFunc().apply(parentItemSup));
            Integer itemFuelCookTime = curIpw.getItemFuelMappingFunc() == null ? null : Math.abs(curIpw.getItemFuelMappingFunc().apply(parentItemSup));

            if (itemCompostChance != null && itemCompostChance != 0) CompostingChanceRegistry.INSTANCE.add(parentItemSup.get(), itemCompostChance);
            if (itemFuelCookTime != null && itemFuelCookTime != 0) FuelRegistry.INSTANCE.add(parentItemSup.get(), itemFuelCookTime);
        });

        TagWrapper.getCachedTWEntries().forEach(curTw -> {
            int tagFuelCookTime = Math.abs(curTw.getCookTime());
            IntIntMutablePair tagFlammabilitySettings = curTw.getFlammabilitySettings();
            TagKey<?> curTagKey = curTw.getParentTag().get();

            if (tagFuelCookTime != 0 && curTagKey.isFor(Registries.ITEM)) FuelRegistry.INSTANCE.add((TagKey<Item>) curTagKey, tagFuelCookTime);
            if (tagFlammabilitySettings != null && curTagKey.isFor(Registries.BLOCK)) FlammableBlockRegistry.getDefaultInstance().add((TagKey<Block>) curTagKey, Math.abs(tagFlammabilitySettings.leftInt()), Math.abs(tagFlammabilitySettings.rightInt()));
        });
    }

    static void handleMiscellaneousRegistration() {
        EntityTypePropertyWrapper.getMappedEtpws().forEach((parentEntityTypeSup, curEtpw) -> {
            if (curEtpw.getAttributeBuilder() != null && !parentEntityTypeSup.get().getCategory().equals(MobCategory.MISC)) FabricDefaultAttributeRegistry.register((EntityType<? extends LivingEntity>) parentEntityTypeSup.get(), curEtpw.getAttributeBuilder().get().build());
        });
    }
}
