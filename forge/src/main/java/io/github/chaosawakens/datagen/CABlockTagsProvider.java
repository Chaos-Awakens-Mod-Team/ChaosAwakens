package io.github.chaosawakens.datagen;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.api.tag.TagWrapper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CABlockTagsProvider extends BlockTagsProvider {

    public CABlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CAConstants.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return CAConstants.MOD_NAME.concat(": Block Tags");
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        if (!BlockPropertyWrapper.getMappedBpws().isEmpty()) {
            BlockPropertyWrapper.getMappedBpws().forEach((blockSupEntry, curBpw) -> {
                List<TagKey<?>> parentTags = curBpw.getParentTags().stream().map(Supplier::get).collect(Collectors.toCollection(ObjectArrayList::new));
                List<TagKey<Block>> parentBlockTags = curBpw.getParentBlockTags().stream().map(Supplier::get).collect(Collectors.toCollection(ObjectArrayList::new));

                if (!parentTags.isEmpty()) {
                    parentTags.forEach(curBlockTag -> {
                        if (curBlockTag != null && curBlockTag.isFor(Registries.BLOCK)) {
                            CAConstants.LOGGER.debug("[Tagging Block]: {} -> {}", blockSupEntry.get().getDescriptionId(), curBlockTag);

                            tag((TagKey<Block>) curBlockTag).add(blockSupEntry.get());
                        }
                    });
                }

                if (!parentBlockTags.isEmpty()) {
                    parentBlockTags.forEach(curBlockTag -> {
                       if (curBlockTag != null) {
                           CAConstants.LOGGER.debug("[Tagging Block]: {} -> {}", blockSupEntry.get().getDescriptionId(), curBlockTag);

                           tag(curBlockTag).add(blockSupEntry.get());
                       }
                    });
                }
            });
        }

        if (!TagWrapper.getCachedTWEntries().isEmpty()) {
            TagWrapper.getCachedTWEntries().forEach(twEntry -> {
                TagKey<?> parentTagKey = twEntry.getParentTag().get();

                if (parentTagKey.isFor(Registries.BLOCK)) {
                    twEntry.getPredefinedTagEntries().forEach(tagEntry -> {
                        Block blockTagEntry = (Block) tagEntry.get();

                        if (blockTagEntry != null) {
                            CAConstants.LOGGER.debug("[Tagging Block]: {} -> {}", blockTagEntry.getDescriptionId(), parentTagKey);

                            tag((TagKey<Block>) parentTagKey).add(blockTagEntry);
                        }
                    });

                    twEntry.getStoredTags().forEach(tagKeyEntry -> {
                        TagKey<?> storedTagKeyEntry = tagKeyEntry.get();

                        if (storedTagKeyEntry != null) {
                            CAConstants.LOGGER.debug("[Tagging Block Tag]: {} -> {}", storedTagKeyEntry, parentTagKey);

                            tag((TagKey<Block>) storedTagKeyEntry); // Force the existingFileHelper to track the tag to be added (otherwise throws exception). Goofy ahh patch.
                            tag((TagKey<Block>) parentTagKey).addTag((TagKey<Block>) storedTagKeyEntry);
                        }
                    });

                    twEntry.getParentTags().forEach(parentTagKeyEntry -> {
                        TagKey<?> storedParentTagKeyEntry = parentTagKeyEntry.get();

                        if (storedParentTagKeyEntry != null) {
                            CAConstants.LOGGER.debug("[Tagging Block Tag]: {} -> {}", parentTagKey, storedParentTagKeyEntry);

                            tag((TagKey<Block>) parentTagKey); // Force the existingFileHelper to track the tag to be added (otherwise throws exception). Goofy ahh patch.
                            tag((TagKey<Block>) storedParentTagKeyEntry).addTag((TagKey<Block>) parentTagKey);
                        }
                    });
                }
            });
        }
    }
}
