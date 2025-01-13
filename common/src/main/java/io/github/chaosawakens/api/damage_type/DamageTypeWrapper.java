package io.github.chaosawakens.api.damage_type;

import com.google.common.collect.ImmutableSortedMap;
import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * A wrapper class used to store information referenced in datagen to simplify creating data entries for damage types.
 */
public class DamageTypeWrapper {
    private static final Object2ObjectLinkedOpenHashMap<Supplier<ResourceKey<DamageType>>, DamageTypeWrapper> MAPPED_DTWS = new Object2ObjectLinkedOpenHashMap<>();
    private final Supplier<ResourceKey<DamageType>> ownerDamageTypeHolder;
    @Nullable
    private DTWBuilder builder;

    private DamageTypeWrapper(Supplier<ResourceKey<DamageType>> ownerDamageTypeHolder) {
        this.ownerDamageTypeHolder = ownerDamageTypeHolder;
    }

    /**
     * Creates a new {@link DamageTypeWrapper} instance. This is usually where you'll begin chaining {@link #builder()} method calls if needed. Use this variant if you want to create a DTW instance with a stored registration call,
     * such that its parent {@link Supplier<ResourceKey<DamageType>>} is not an air delegate/{@code null}.
     *
     * @param ownerDamageTypeHolder The parent {@link Supplier<ResourceKey<DamageType>>} stored in the newly-initialized DTW instance, typically obtained through {@link Level#registryAccess()}.
     *
     * @return A new {@link BlockPropertyWrapper} instance.
     */
    public static DamageTypeWrapper create(Supplier<ResourceKey<DamageType>> ownerDamageTypeHolder) {
        return new DamageTypeWrapper(ownerDamageTypeHolder);
    }

    /**
     * Constructs a builder chain in which certain datagen properties can be assigned and re-built with in this DTW instance. Also sets
     * this DTW instance's {@link #builder} to the newly-constructed {@link DTWBuilder} instance.
     *
     * @return A new {@link DTWBuilder} instance from the {@link #builder} field.
     */
    public DTWBuilder builder() {
        return this.builder = new DTWBuilder(this, ownerDamageTypeHolder);
    }

    /**
     * Gets the cached {@link DTWBuilder} instance from the {@link #builder} if it exists. May be {@code null}. Useful for
     * overriding specific properties after having copied another DTW instance/already set a DTWBuilder.
     *
     * @return The cached {@link DTWBuilder} instance, or {@code null} if the {@link #builder} is {@code null}.
     */
    public DTWBuilder cachedBuilder() {
        return builder;
    }

    /**
     * Gets the parent {@code Supplier<ResourceKey<DamageType>>} of this DTW instance.
     *
     * @return The parent {@code Supplier<ResourceKey<DamageType>>} stored in this DTW instance.
     */
    public Supplier<ResourceKey<DamageType>> getOwnerDamageType() {
        return ownerDamageTypeHolder;
    }

    /**
     * Gets the copied message ID for the parent damage type. May be a blank string.
     *
     * @return The copied message ID, or a blank string if the {@link #builder} is {@code null} or the value itself is {@code null}/a blank string.
     */
    public String getCopiedMsgId() {
        return builder != null ? builder.copiedMsgId == null ? "" : builder.copiedMsgId : "";
    }

    /**
     * Gets the localized death message {@link Component} for the parent damage type. This is typically parsed and formatted for value mapping inside the language file. May be {@code null}.
     *
     * @return The localized death message component, or {@code null} if the {@link #builder} is {@code null} or the value itself is {@code null}.
     */
    @Nullable
    public Component getLocalizedDeathMessageComponent() {
        return builder != null ? builder.localizedDeathMessageComponent : null;
    }

    /**
     * Gets an immutable view (via {@link ImmutableSortedMap}) of {@link #MAPPED_DTWS}.
     *
     * @return An immutable view (via {@link ImmutableSortedMap}) of {@link #MAPPED_DTWS}.
     */
    public static ImmutableSortedMap<Supplier<ResourceKey<DamageType>>, DamageTypeWrapper> getMappedDtws() {
        return ImmutableSortedMap.copyOf(MAPPED_DTWS);
    }

    /**
     * A builder class used to construct certain damage type-related data for datagen.
     */
    public static class DTWBuilder {
        private final DamageTypeWrapper ownerWrapper;
        private final Supplier<ResourceKey<DamageType>> ownerDamageType;
        @Nullable
        private Component localizedDeathMessageComponent;
        @Nullable
        private String copiedMsgId;

        private DTWBuilder(DamageTypeWrapper ownerWrapper, Supplier<ResourceKey<DamageType>> ownerDamageType) {
            this.ownerWrapper = ownerWrapper;
            this.ownerDamageType = ownerDamageType;
        }

        /**
         * Sets the copied message ID for the parent damage type. Used in conjunction with {@link #withLocalizedDeathMessageComponent(Component)} during language datagen since datapack registry lookups aren't available
         * on the client.
         *
         * @param copiedMsgId The copied message ID (Essentially the same thing you used to register your damage type).
         *
         * @return {@code this} (builder method)
         *
         * @see #withLocalizedDeathMessageComponent(Component)
         */
        public DTWBuilder withCopiedMsgId(String copiedMsgId) {
            this.copiedMsgId = copiedMsgId;
            return this;
        }

        /**
         * Sets the localized death message {@link Component} for the parent damage source. This is typically parsed and formatted for value mapping inside the language file.
         *
         * @param localizedDeathMessageComponent The localized death message component.
         *
         * @return {@code this} (builder method)
         *
         * @see #withCopiedMsgId(String)
         */
        public DTWBuilder withLocalizedDeathMessageComponent(Component localizedDeathMessageComponent) {
            this.localizedDeathMessageComponent = localizedDeathMessageComponent;
            return this;
        }

        /**
         * Builds a new {@link DamageTypeWrapper} using this builder's data. Also maps the owner
         * {@link DamageTypeWrapper} to the parent {@linkplain DamageType} if isn't already mapped.
         *
         * @return The newly data-populated {@link DamageTypeWrapper}.
         */
        public DamageTypeWrapper build() {
            MAPPED_DTWS.putIfAbsent(ownerDamageType, ownerWrapper); // Whaddya thing a diss (No nullity allowed)
            return ownerWrapper;
        }
    }
}
