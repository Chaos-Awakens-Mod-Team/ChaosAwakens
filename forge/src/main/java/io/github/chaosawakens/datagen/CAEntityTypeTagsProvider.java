package io.github.chaosawakens.datagen;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.entity.EntityTypePropertyWrapper;
import io.github.chaosawakens.api.tag.TagWrapper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CAEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public CAEntityTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, CAConstants.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return CAConstants.MOD_NAME.concat(": EntityType Tags");
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        if (!EntityTypePropertyWrapper.getMappedEtpws().isEmpty()) {
            EntityTypePropertyWrapper.getMappedEtpws().forEach((entityTypeSupEntry, curEtpw) -> {
                List<TagKey<?>> parentTags = curEtpw.getParentTags().stream().map(Supplier::get).collect(Collectors.toCollection(ObjectArrayList::new));
                ObjectArrayList<TagKey<? extends EntityType<?>>> parentEntityTypesTags = curEtpw.getParentTags().stream().map(Supplier::get).collect(Collectors.toCollection(ObjectArrayList::new));

                if (!parentTags.isEmpty()) {
                    parentTags.forEach(curEntityTypeTag -> {
                        if (curEntityTypeTag != null && curEntityTypeTag.isFor(Registries.ENTITY_TYPE)) {
                            CAConstants.LOGGER.debug("[Tagging EntityType]: {} -> {}", entityTypeSupEntry.get().getDescriptionId(), curEntityTypeTag);

                            tag((TagKey<EntityType<?>>) curEntityTypeTag).add(entityTypeSupEntry.get());
                        }
                    });
                }

                if (!parentEntityTypesTags.isEmpty()) {
                    parentEntityTypesTags.forEach(curEntityTypeTag -> {
                        if (curEntityTypeTag != null) {
                            CAConstants.LOGGER.debug("[Tagging EntityType]: {} -> {}", entityTypeSupEntry.get().getDescriptionId(), curEntityTypeTag);

                            tag((TagKey<EntityType<?>>) curEntityTypeTag).add(entityTypeSupEntry.get());
                        }
                    });
                }
            });
        }

        if (!TagWrapper.getCachedTWEntries().isEmpty()) {
            TagWrapper.getCachedTWEntries().forEach(twEntry -> {
                TagKey<?> parentTagKey = twEntry.getParentTag().get();

                if (parentTagKey.isFor(Registries.ENTITY_TYPE)) {
                    twEntry.getPredefinedTagEntries().forEach(tagEntry -> {
                        EntityType<?> entityTypeTagEntry = (EntityType<?>) tagEntry.get();

                        if (entityTypeTagEntry != null) {
                            CAConstants.LOGGER.debug("[Tagging EntityType]: {} -> {}", entityTypeTagEntry.getDescriptionId(), parentTagKey);

                            tag((TagKey<EntityType<?>>) parentTagKey).add(entityTypeTagEntry);
                        }
                    });

                    twEntry.getStoredTags().forEach(tagKeyEntry -> {
                        TagKey<?> storedTagKeyEntry = tagKeyEntry.get();

                        if (storedTagKeyEntry != null) {
                            CAConstants.LOGGER.debug("[Tagging EntityType Tag]: {} -> {}", storedTagKeyEntry, parentTagKey);

                            tag((TagKey<EntityType<?>>) storedTagKeyEntry); // Force the existingFileHelper to track the tag to be added (otherwise throws exception). Goofy ahh patch.
                            tag((TagKey<EntityType<?>>) parentTagKey).addTag((TagKey<EntityType<?>>) storedTagKeyEntry);
                        }
                    });

                    twEntry.getParentTags().forEach(parentTagKeyEntry -> {
                        TagKey<?> storedParentTagKeyEntry = parentTagKeyEntry.get();

                        if (storedParentTagKeyEntry != null) {
                            CAConstants.LOGGER.debug("[Tagging EntityType Tag]: {} -> {}", parentTagKey, storedParentTagKeyEntry);

                            tag((TagKey<EntityType<?>>) parentTagKey); // Force the existingFileHelper to track the tag to be added (otherwise throws exception). Goofy ahh patch.
                            tag((TagKey<EntityType<?>>) storedParentTagKeyEntry).addTag((TagKey<EntityType<?>>) parentTagKey);
                        }
                    });
                }
            });
        }
    }
}
