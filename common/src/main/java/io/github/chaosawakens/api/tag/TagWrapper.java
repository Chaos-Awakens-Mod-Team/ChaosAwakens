package io.github.chaosawakens.api.tag;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

/**
 * A wrapper builder class used to store information about tags and their pre-defined references in datagen to simplify creating data entries for tag entries.
 *
 * @param <T> The object type stored by the parent {@link TagKey}.
 * @param <TK> The {@link TagKey} object itself parenting the objects stored in this wrapper.
 */
public class TagWrapper<T, TK extends TagKey<T>> {
    private static final ObjectArrayList<TagWrapper<?, ? extends TagKey<?>>> CACHED_WRAPPERS = new ObjectArrayList<>();
    @NotNull
    protected final TK parentTag;
    protected ObjectArrayList<Supplier<T>> storedTaggedObjects = new ObjectArrayList<>();
    protected ObjectArrayList<TK> storedCopiedTags = new ObjectArrayList<>();
    protected ObjectArrayList<TK> storedParentTags = new ObjectArrayList<>();

    private TagWrapper(TK parentTag) {
        this.parentTag = parentTag;
    }

    /**
     * Creates a new TW instance.
     *
     * @param parentTag The {@link TagKey} parenting all stored objects in this wrapper instance. Cannot be {@code null}.
     *
     * @return The newly constructed TW instance.
     *
     * @param <T> The object type stored by the parent {@link TagKey}.
     * @param <TK> The {@link TagKey} object itself parenting the objects stored in this wrapper.
     */
    public static <T, TK extends TagKey<T>> TagWrapper<T, TK> create(@NotNull TK parentTag) {
        TagWrapper<T, TK> createdWrapper =  new TagWrapper<>(parentTag);
        CACHED_WRAPPERS.add(createdWrapper);
        return createdWrapper;
    }

    /**
     * Adds a pre-defined object entry to this TW instance's parent {@link TagKey}.
     *
     * @param tagEntry The object entry to add to the collection of pre-defined objects tagged with this TW instance's parent {@link TagKey}.
     *
     * @return {@code this} (builder method).
     *
     * @see #withEntries(List)
     * @see #withTagEntry(TK)
     * @see #withTagEntries(List)
     * @see #withParentTagEntry(TagKey)
     * @see #withParentTagEntries(List)
     */
    public TagWrapper<T, TK> withEntry(Supplier<T> tagEntry) {
        this.storedTaggedObjects.add(tagEntry);
        return this;
    }

    /**
     * Adds a pre-defined {@link List} of object entries to this TW instance's parent {@link TagKey}.
     *
     * @param tagEntries The {@link List} of object entries to add to the collection of pre-defined objects tagged with this TW instance's parent {@link TagKey}.
     *
     * @return {@code this} (builder method).
     *
     * @see #withEntry(Supplier)
     * @see #withTagEntry(TK)
     * @see #withTagEntries(List)
     * @see #withParentTagEntry(TagKey)
     * @see #withParentTagEntries(List)
     */
    public TagWrapper<T, TK> withEntries(List<Supplier<T>> tagEntries) {
        this.storedTaggedObjects.addAll(tagEntries);
        return this;
    }

    /**
     * Adds a pre-defined {@link TagKey} entry to this TW instance's {@link ObjectArrayList} of {@linkplain TagKey TagKeys} to inherit object entries from.
     *
     * @param tagToCopyFrom The {@link TagKey} entry to add to this TW instance's {@link ObjectArrayList} of {@linkplain TagKey TagKeys} to inherit object entries from.
     *
     * @return {@code this} (builder method).
     *
     * @see #withEntry(Supplier)
     * @see #withEntries(List)
     * @see #withTagEntries(List)
     * @see #withParentTagEntry(TagKey)
     * @see #withParentTagEntries(List)
     */
    public TagWrapper<T, TK> withTagEntry(TK tagToCopyFrom) {
        this.storedCopiedTags.add(tagToCopyFrom);
        return this;
    }

    /**
     * Adds a pre-defined {@link List} of {@link TagKey} entries to this TW instance's {@link ObjectArrayList} of {@linkplain TagKey TagKeys} to inherit object entries from.
     *
     * @param tagEntries The {@link List} of {@link TagKey} entries to add to this TW instance's {@link ObjectArrayList} of {@linkplain TagKey TagKeys} to inherit object entries from.
     *
     * @return {@code this} (builder method).
     *
     * @see #withEntry(Supplier)
     * @see #withEntries(List)
     * @see #withTagEntry(TK)
     * @see #withParentTagEntry(TagKey)
     * @see #withParentTagEntries(List)
     */
    public TagWrapper<T, TK> withTagEntries(List<TK> tagEntries) {
        this.storedCopiedTags.addAll(tagEntries);
        return this;
    }

    /**
     * Adds a pre-defined {@link TagKey} entry to this TW instance's {@link ObjectArrayList} of {@linkplain TagKey TagKeys} to to be added to.
     *
     * @param parentTag The {@link TagKey} entry to add to this TW instance's {@link ObjectArrayList} of parent {@linkplain TagKey TagKeys} to be added to.
     *
     * @return {@code this} (builder method).
     *
     * @see #withEntry(Supplier)
     * @see #withEntries(List)
     * @see #withTagEntries(List)
     * @see #withParentTagEntries(List)
     */
    public TagWrapper<T, TK> withParentTagEntry(TK parentTag) {
        this.storedParentTags.add(parentTag);
        return this;
    }

    /**
     * Adds a pre-defined {@link List} of {@link TagKey} entries to this TW instance's {@link ObjectArrayList} of {@linkplain TagKey TagKeys} to be added to.
     *
     * @param parentTagEntries The {@link List} of {@link TagKey} entries to add to this TW instance's {@link ObjectArrayList} of parent {@linkplain TagKey TagKeys} to be added to.
     *
     * @return {@code this} (builder method).
     *
     * @see #withEntry(Supplier)
     * @see #withEntries(List)
     * @see #withTagEntry(TK)
     * @see #withParentTagEntry(TagKey)
     */
    public TagWrapper<T, TK> withParentTagEntries(List<TK> parentTagEntries) {
        this.storedParentTags.addAll(parentTagEntries);
        return this;
    }

    /**
     * Gets the parent {@link TagKey} stored in this TW instance.
     *
     * @return The parent {@link TagKey}.
     */
    @NotNull
    public TK getParentTag() {
        return parentTag;
    }

    /**
     * Gets the pre-defined {@link List} of object entries stored in this TW instance.
     *
     * @return An immutable view of the pre-defined {@link List} of object entries stored in this TW instance.
     */
    public ImmutableList<Supplier<T>> getPredefinedTagEntries() {
        return ImmutableList.copyOf(storedTaggedObjects);
    }

    /**
     * Gets the pre-defined {@link List} of {@link TagKey} entries stored in this TW instance.
     *
     * @return An immutable view of the pre-defined {@link List} of {@link TagKey} entries stored in this TW instance.
     */
    public ImmutableList<TK> getStoredTags() {
        return ImmutableList.copyOf(storedCopiedTags);
    }

    /**
     * Gets the pre-defined {@link List} of parent {@link TagKey} entries stored in this TW instance.
     *
     * @return An immutable view of the pre-defined {@link List} of parent {@link TagKey} entries stored in this TW instance.
     */
    public ImmutableList<TK> getParentTags() {
        return ImmutableList.copyOf(storedParentTags);
    }

    /**
     * Gets an {@link ImmutableList} representing all created TW entries, cached in a {@code static} collection.
     *
     * @return An immutable view of {@link #CACHED_WRAPPERS}.
     */
    public static ImmutableList<TagWrapper<?, ? extends TagKey<?>>> getCachedTWEntries() {
        return ImmutableList.copyOf(CACHED_WRAPPERS);
    }
}
