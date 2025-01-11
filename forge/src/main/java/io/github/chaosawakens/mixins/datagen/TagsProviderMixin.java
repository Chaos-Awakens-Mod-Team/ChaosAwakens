package io.github.chaosawakens.mixins.datagen;

import net.minecraft.data.tags.TagsProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TagsProvider.class)
public abstract class TagsProviderMixin {

    private TagsProviderMixin() {
        throw new IllegalAccessError("Attempted to construct Mixin Class!");
    }

/*    @Inject(method = "lambda$run$6", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/DataProvider;saveStable(Lnet/minecraft/data/CachedOutput;Lcom/google/gson/JsonElement;Ljava/nio/file/Path;)Ljava/util/concurrent/CompletableFuture;", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void chaosawakens$run(Predicate<TagEntry> predicate, Predicate<TagEntry> predicate1, CachedOutput pOutput, Map.Entry<ResourceLocation, TagBuilder> p_255499_, CallbackInfoReturnable<CompletableFuture<?>> cir, ResourceLocation resourcelocation, TagBuilder tagbuilder, List<TagEntry> list, List<TagEntry> list1, List<TagEntry> removed, JsonElement jsonelement, Path path) {
        if (list.isEmpty()) cir.setReturnValue(CompletableFuture.completedFuture(null)); // PATCH: We need the existingFileHelper to track un-tracked tags to avoid throwing missing tag definition exceptions (despite said tags existing). Avoid writing a file if the tag in question has no entries. See CABlockTagsProvider for more info.
    } */ // SIDE NOTE: Only re-enable this if there is too much bloat or something (so never). Turns out existing tags referencing tags that don't exist yet (E.G. Their blocks/items/etc. have yet to be added and thus they just exist) is a BAD idea. I concede, Forge Datagen.
}
