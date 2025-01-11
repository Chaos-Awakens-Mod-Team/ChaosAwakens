package io.github.chaosawakens;

import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.api.item.ItemPropertyWrapper;
import io.github.chaosawakens.api.tag.TagWrapper;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.*;

import java.util.function.Supplier;

public final class ForgeVanillaIntegration {

      static void handleVanillaIntegration() { // Tool actions (stripping, tilling, flattening) and fuel are handled separately in their corresponding events
        BlockPropertyWrapper.getMappedBpws().forEach((parentBlockSup, curBpw) -> {
            IntIntMutablePair flammabilityPair = curBpw.getFlammabilityMappingFunc() == null ? null : curBpw.getFlammabilityMappingFunc().apply(parentBlockSup);
            Supplier<Block> oxidizedBlockVariant = curBpw.getBlockOxidizationMappingFunc() == null ? null : curBpw.getBlockOxidizationMappingFunc().apply(parentBlockSup);
            Supplier<Block> waxedBlockVariant = curBpw.getBlockWaxingMappingFunc() == null ? null : curBpw.getBlockWaxingMappingFunc().apply(parentBlockSup);
            Float blockCompostChance = curBpw.getBlockCompostingMappingFunc() == null ? null : Math.abs(curBpw.getBlockCompostingMappingFunc().apply(parentBlockSup));

            if (flammabilityPair != null) ((FireBlock) Blocks.FIRE).setFlammable(parentBlockSup.get(), Math.abs(flammabilityPair.leftInt()), Math.abs(flammabilityPair.rightInt()));
            if (oxidizedBlockVariant != null && oxidizedBlockVariant.get() != null) WeatheringCopper.NEXT_BY_BLOCK.get().put(parentBlockSup.get(), oxidizedBlockVariant.get());
            if (waxedBlockVariant != null && waxedBlockVariant.get() != null) HoneycombItem.WAXABLES.get().put(parentBlockSup.get(), waxedBlockVariant.get());
            if (blockCompostChance != null && blockCompostChance != 0) ComposterBlock.COMPOSTABLES.put(parentBlockSup.get(), (float) blockCompostChance);
        });

        ItemPropertyWrapper.getMappedIpws().forEach((parentItemSup, curIpw) -> {
            Float itemCompostChance = curIpw.getCompostingMappingFunc() == null ? null : Math.abs(curIpw.getCompostingMappingFunc().apply(parentItemSup));

            if (itemCompostChance != null && itemCompostChance != 0) ComposterBlock.COMPOSTABLES.put(parentItemSup.get(), (float) itemCompostChance);
        });

        TagWrapper.getCachedTWEntries().stream().filter(curTw -> curTw.getParentTag().get().isFor(Registries.BLOCK) && curTw.getFlammabilitySettings() != null).forEach(curTw -> {
            IntIntMutablePair tagFlammabilitySettings = curTw.getFlammabilitySettings();

            for (Holder<Block> blockEntryHolder : BuiltInRegistries.BLOCK.getTagOrEmpty((TagKey<Block>) curTw.getParentTag().get())) { // Idk Forge doesn't provide an alt (Native for() loops FTW)
                ((FireBlock) Blocks.FIRE).setFlammable(blockEntryHolder.get(), Math.abs(tagFlammabilitySettings.leftInt()), Math.abs(tagFlammabilitySettings.rightInt()));
            }
        });
    }
}
