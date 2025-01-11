package io.github.chaosawakens.datagen;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.api.item.ItemPropertyWrapper;
import io.github.chaosawakens.api.tag.TagWrapper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CAItemTagsProvider extends ItemTagsProvider {

    public CAItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockLookupProvider, CAConstants.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return CAConstants.MOD_NAME.concat(": Item Tags");
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        if (!BlockPropertyWrapper.getMappedBpws().isEmpty()) {
            BlockPropertyWrapper.getMappedBpws().forEach((blockSupEntry, curBwp) -> {
                List<TagKey<?>> parentTags = curBwp.getParentTags().stream().map(Supplier::get).collect(Collectors.toCollection(ObjectArrayList::new));
                List<TagKey<Item>> parentItemTags = curBwp.getParentItemTags().stream().map(Supplier::get).collect(Collectors.toCollection(ObjectArrayList::new));

                if (!parentTags.isEmpty()) {
                    parentTags.forEach(curBlockItemTag -> {
                        if (curBlockItemTag != null && curBlockItemTag.isFor(Registries.ITEM)) {
                            CAConstants.LOGGER.debug("[Tagging Block Item]: {} -> {}", blockSupEntry.get().getDescriptionId(), curBlockItemTag);

                            tag((TagKey<Item>) curBlockItemTag).add(blockSupEntry.get().asItem());
                        }
                    });
                }

                if (!parentItemTags.isEmpty()) {
                    parentItemTags.forEach(curItemTag -> {
                        if (curItemTag != null) {
                            CAConstants.LOGGER.debug("[Tagging Block Item]: {} -> {}", blockSupEntry.get().getDescriptionId(), curItemTag);

                            tag(curItemTag).add(blockSupEntry.get().asItem());
                        }
                    });
                }
            });
        }

        if (!ItemPropertyWrapper.getMappedIpws().isEmpty()) {
            ItemPropertyWrapper.getMappedIpws().forEach((itemSupEntry, curIwp) -> {
                List<TagKey<Item>> parentItemTags = curIwp.getParentTags().stream().map(Supplier::get).collect(Collectors.toCollection(ObjectArrayList::new));

                if (!parentItemTags.isEmpty()) {
                    parentItemTags.forEach(curItemTag -> {
                        CAConstants.LOGGER.debug("[Tagging Item]: {} -> {}", itemSupEntry.get().getDescriptionId(), curItemTag);

                        tag(curItemTag).add(itemSupEntry.get());
                    });
                }
            });
        }

        if (!TagWrapper.getCachedTWEntries().isEmpty()) {
            TagWrapper.getCachedTWEntries().forEach(twEntry -> {
                TagKey<?> parentTagKey = twEntry.getParentTag().get();

                if (parentTagKey.isFor(Registries.ITEM)) {
                    if (parentTagKey != null) {
                        twEntry.getPredefinedTagEntries().forEach(tagEntry -> {
                            Item itemTagEntry = tagEntry.get() instanceof Block ? ((Block) tagEntry.get()).asItem() : (Item) tagEntry.get();

                            if (itemTagEntry != null) {
                                CAConstants.LOGGER.debug("{}{} -> {}", tagEntry.get() instanceof Block ? "[Tagging Block Item]: " : "[Tagging Item]: ", itemTagEntry.getDescriptionId(), parentTagKey);

                                tag((TagKey<Item>) parentTagKey).add(itemTagEntry);
                            }
                        });

                        twEntry.getStoredTags().forEach(tagKeyEntry -> {
                            TagKey<?> storedTagKeyEntry = tagKeyEntry.get();

                            if (storedTagKeyEntry != null) {
                                CAConstants.LOGGER.debug("[Tagging Item Tag]: {} -> {}", storedTagKeyEntry, parentTagKey);

                                tag((TagKey<Item>) tagKeyEntry.get()); // Force the existingFileHelper to track the tag to be added (otherwise throws exception). Goofy ahh patch.
                                tag((TagKey<Item>) parentTagKey).addTag((TagKey<Item>) storedTagKeyEntry);
                            }
                        });

                        twEntry.getParentTags().forEach(parentTagKeyEntry -> {
                            TagKey<?> storedParentTagKeyEntry = parentTagKeyEntry.get();

                            if (storedParentTagKeyEntry != null) {
                                CAConstants.LOGGER.debug("[Tagging Item Tag]: {} -> {}", parentTagKey, storedParentTagKeyEntry);

                                tag((TagKey<Item>) parentTagKey); // Force the existingFileHelper to track the tag to be added (otherwise throws exception). Goofy ahh patch.
                                tag((TagKey<Item>) storedParentTagKeyEntry).addTag((TagKey<Item>) parentTagKey);
                            }
                        });
                    }
                }
            });
        }
    }
}
