package io.github.chaosawakens.api.tag;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.IntIntMutablePair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
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
    protected final Supplier<TK> parentTag;
    protected ObjectArrayList<Supplier<T>> storedTaggedObjects = new ObjectArrayList<>();
    protected ObjectArrayList<Supplier<TK>> storedCopiedTags = new ObjectArrayList<>();
    protected ObjectArrayList<Supplier<TK>> storedParentTags = new ObjectArrayList<>();
    protected int cookTime = 0;
    @Nullable
    protected IntIntMutablePair flammabilityPair;

    private TagWrapper(Supplier<TK> parentTag) {
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
    public static <T, TK extends TagKey<T>> TagWrapper<T, TK> create(@NotNull Supplier<TK> parentTag) {
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
     * @see #withTagEntry(Supplier)
     * @see #withTagEntries(List)
     * @see #withParentTagEntry(Supplier)
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
     * @see #withTagEntry(Supplier)
     * @see #withTagEntries(List)
     * @see #withParentTagEntry(Supplier)
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
     * @see #withParentTagEntry(Supplier)
     * @see #withParentTagEntries(List)
     */
    public TagWrapper<T, TK> withTagEntry(Supplier<TK> tagToCopyFrom) {
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
     * @see #withTagEntry(Supplier)
     * @see #withParentTagEntry(Supplier)
     * @see #withParentTagEntries(List)
     */
    public TagWrapper<T, TK> withTagEntries(List<Supplier<TK>> tagEntries) {
        this.storedCopiedTags.addAll(tagEntries);
        return this;
    }

    /**
     * Adds a pre-defined {@link TagKey} entry to this TW instance's {@link ObjectArrayList} of {@linkplain TagKey TagKeys} to be added to.
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
    public TagWrapper<T, TK> withParentTagEntry(Supplier<TK> parentTag) {
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
     * @see #withTagEntry(Supplier<TK>)
     * @see #withParentTagEntry(Supplier<TK>)
     */
    public TagWrapper<T, TK> withParentTagEntries(List<Supplier<TK>> parentTagEntries) {
        this.storedParentTags.addAll(parentTagEntries);
        return this;
    }

    /**
     * Defines flammability options using an optional {@link IntIntMutablePair} representing the burn time (in ticks) and spread. Only applied to {@link Block} {@linkplain TK TagKeys}.
     *
     * @param flammabilityPair The mapping pair representing the parent {@linkplain TK TagKey's} optional flammability settings, with the {@link IntIntMutablePair}
     *                         representing the burn time (in ticks) and spread respectively.
     *
     * @return {@code this} (builder method).
     */
    public TagWrapper<T, TK> withFlammability(IntIntMutablePair flammabilityPair) {
        this.flammabilityPair = flammabilityPair;
        return this;
    }

    /**
     * Defines this TW instance as flammable with the provided {@code cookTime}, in ticks. Only applied to {@link Item} {@linkplain TK TagKeys}.
     *
     * @param cookTime The {@code cookTime} for all objects under the registered tag, in ticks.
     *
     * @return {@code this} (builder method).
     */
    public TagWrapper<T, TK> asFuel(int cookTime) {
        this.cookTime = cookTime;
        return this;
    }

    /**
     * Gets the parent {@link Supplier<TK>} stored in this TW instance.
     *
     * @return The parent {@link Supplier<TK>}.
     */
    @NotNull
    public Supplier<TK> getParentTag() {
        return parentTag;
    }

    /**
     * Gets the pre-defined {@link List} of object entries stored in this TW instance.
     *
     * @return An immutable view of the pre-defined {@link List} of object entries stored in this TW instance.
     */
    public ImmutableList<Supplier<T>> getPredefinedTagEntries() {
        return storedTaggedObjects.stream().anyMatch(Objects::isNull) ? ImmutableList.of() : ImmutableList.copyOf(storedTaggedObjects);
    }

    /**
     * Gets the pre-defined {@link List} of {@link TagKey} entries stored in this TW instance.
     *
     * @return An immutable view of the pre-defined {@link List} of {@link TagKey} entries stored in this TW instance.
     */
    public ImmutableList<Supplier<TK>> getStoredTags() {
        return ImmutableList.copyOf(storedCopiedTags);
    }

    /**
     * Gets the pre-defined {@link List} of parent {@link TagKey} entries stored in this TW instance.
     *
     * @return An immutable view of the pre-defined {@link List} of parent {@link TagKey} entries stored in this TW instance.
     */
    public ImmutableList<Supplier<TK>> getParentTags() {
        return ImmutableList.copyOf(storedParentTags);
    }

    /**
     * Gets the {@code cookTime} for all objects under the registered tag, in ticks. 0 is treated as none, values under 0 are just abs'd. Only applied to {@link Item} {@linkplain TK TagKeys}.
     *
     * @return The {@code cookTime} for all objects under the registered tag, in ticks.
     */
    public int getCookTime() {
        return cookTime;
    }

    /**
     * Gets an {@link IntIntMutablePair} representing the burn time (in ticks) and spread. Only applied to {@link Block} {@linkplain TK TagKeys}. May be {@code null}.
     *
     * @return The {@link IntIntMutablePair} representing the burn time (in ticks) and spread. May be {@code null}.
     */
    @Nullable
    public IntIntMutablePair getFlammabilitySettings() {
        return flammabilityPair;
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
