package io.github.chaosawakens.api.entity;

import com.google.common.collect.ImmutableSortedMap;
import io.github.chaosawakens.CAConstants;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A wrapper class used to store information referenced in datagen to simplify creating data entries and other backend data (such as attributes) for entity types. Also used to store additional data such as renderers,
 * models, etc.
 *
 * @param <E> Any {@link Entity} subclass.
 */
public class EntityTypePropertyWrapper<E extends Entity> {
    private static final Object2ObjectLinkedOpenHashMap<Supplier<? extends EntityType<?>>, EntityTypePropertyWrapper<?>> MAPPED_ETPWS = new Object2ObjectLinkedOpenHashMap<>();
    @Nullable
    private final String entityTypeRegName;
    private final Supplier<EntityType<E>> parentEntityType;
    private final boolean isTemplate;
    @Nullable
    private ETPWBuilder<E> builder;

    private EntityTypePropertyWrapper(@Nullable String entityTypeRegName, Supplier<EntityType<E>> parentEntityType) {
        this.entityTypeRegName = entityTypeRegName;
        this.parentEntityType = parentEntityType;
        this.isTemplate = false;
    }

    private EntityTypePropertyWrapper(Supplier<EntityType<E>> parentEntityType) {
        this(null, parentEntityType);
    }

    private EntityTypePropertyWrapper() {
        this.entityTypeRegName = null;
        this.parentEntityType = null;
        this.isTemplate = true; // Otherwise can't set with constructor overloading (Laziness:tm:)
    }

    /**
     * Creates a new {@link EntityTypePropertyWrapper} instance. This is usually where you'll begin chaining {@link #builder()} method calls if needed. Use this variant if you want to directly create an ETPW during a registration call
     * rather than after it/without storing it.
     *
     * @param entityTypeRegName The registry name of the {@code parentEntityType}. Used when access to the {@code parentItem} returns a pig/{@code null} delegate (I.E. It's too early to access
     *                       the parent entity type).
     * @param parentEntityType The parent {@link Supplier<EntityType<E>>} stored in the newly-initialized ETPW instance.
     *
     * @return A new {@link EntityTypePropertyWrapper} instance.
     *
     * @see #create(Supplier)
     *
     * @param <E> Any {@link Entity} subclass.
     */
    public static <E extends Entity> EntityTypePropertyWrapper<E> create(String entityTypeRegName, Supplier<EntityType<E>> parentEntityType) {
        return new EntityTypePropertyWrapper<>(entityTypeRegName, parentEntityType);
    }

    /**
     * Creates a new {@link EntityTypePropertyWrapper} instance. This is usually where you'll begin chaining {@link #builder()} method calls if needed. Use this variant if you want to create an ETPW instance with a stored registration call,
     * such that its parent {@link Supplier<EntityType<?>>} is not an air delegate/{@code null}.
     *
     * @param parentEntityType The parent {@link Supplier<EntityType<?>>} stored in the newly-initialized ETPW instance.
     *
     * @return A new {@link EntityTypePropertyWrapper} instance.
     *
     * @see #create(String, Supplier)
     * @see #of(EntityTypePropertyWrapper, Supplier)
     *
     * @param <E> Any {@link Entity} subclass.
     */
    public static <E extends Entity> EntityTypePropertyWrapper<E> create(Supplier<EntityType<E>> parentEntityType) {
        return new EntityTypePropertyWrapper<>(parentEntityType);
    }

    /**
     * Creates a new {@link EntityTypePropertyWrapper} instance. This is usually where you'll begin chaining {@link #builder()} method calls if needed. Use this variant if you want to create an ETPW instance with a stored registration call,
     * such that its parent {@link Supplier<EntityType<?>>} is not an air delegate/{@code null}.
     *
     * @param parentEntityType The parent {@link Supplier<EntityType<?>>} stored in the newly-initialized ETPW instance.
     *
     * @return A new {@link EntityTypePropertyWrapper} instance.
     *
     * @see #create(String, Supplier)
     * @see #of(EntityTypePropertyWrapper, Supplier)
     */
    public static EntityTypePropertyWrapper<?> createGeneric(Supplier<? extends EntityType<?>> parentEntityType) {
        return new EntityTypePropertyWrapper(parentEntityType);
    }

    /**
     * Creates a new {@link EntityTypePropertyWrapper} instance as a template. Template ETPWs are not stored in {@link #MAPPED_ETPWS} and do not store a parent {@link EntityType}.
     * They're particularly useful for re-using across multiple {@linkplain EntityType EntityTypes}.
     *
     * @return A new {@link EntityTypePropertyWrapper} instance, set as a template.
     *
     * @see #of(EntityTypePropertyWrapper, Supplier)
     * @see #isTemplate()
     * @see #ofTemplate(EntityTypePropertyWrapper)
     *
     * @param <E> Any {@link Entity} subclass.
     */
    public static <E extends Entity> EntityTypePropertyWrapper<E> createTemplate() {
        return new EntityTypePropertyWrapper<>();
    }

    /**
     * Creates a new {@link EntityTypePropertyWrapper} instance as a template, inheriting data from the provided ETPW template. Template ETPWs are not stored in {@link #MAPPED_ETPWS} and do not store a parent {@link EntityType}.
     * They're particularly useful for re-using across multiple {@linkplain EntityType EntityTypes}.
     *
     * @param parentTemplateWrapper The parent {@link EntityTypePropertyWrapper} template from which {{@link #builder()}} data should be copied.
     *
     * @return A new {@link EntityTypePropertyWrapper} instance, set as a template, inheriting from the provided ETPW template. If the provided ETPW template is {@code null}, returns {@link #createTemplate()}.
     *
     * @see #createTemplate()
     * @see #isTemplate()
     *
     * @param <E> Any {@link Entity} subclass.
     */
    public static <E extends Entity> EntityTypePropertyWrapper<E> ofTemplate(EntityTypePropertyWrapper<E> parentTemplateWrapper) {
        if (parentTemplateWrapper != null) {
            EntityTypePropertyWrapper<E> newTemplateWrapper = new EntityTypePropertyWrapper<>();

            return copyProperties(parentTemplateWrapper, newTemplateWrapper);
        } else return createTemplate();
    }

    /**
     * Creates a new {@link EntityTypePropertyWrapper} instance based on the provided {@link EntityTypePropertyWrapper}. If the provided ETPW instance is {@code null},
     * returns {@link #create(Supplier)}. You'd typically use this if you have an ETPW template you want multiple registered {@linkplain EntityType EntityTypes} to inherit from.
     *
     * @param parentWrapper The parent {@link EntityTypePropertyWrapper} instance from which {@link #builder()} should be copied.
     * @param newEntityType The new registry entry to use for the newly constructed ETPW instance.
     *
     * @return A new {@link EntityTypePropertyWrapper} instance with copied properties based on the provided ETPW, or an entirely new/clean instance if the provided ETPW is {@code null}.
     *
     * @see #create(Supplier)
     * @see #create(String, Supplier)
     * @see #createTemplate()
     *
     * @param <E> Any {@link Entity} subclass.
     */
    public static <E extends Entity> EntityTypePropertyWrapper<E> of(EntityTypePropertyWrapper<E> parentWrapper, Supplier<EntityType<E>> newEntityType) {
        if (parentWrapper != null) {
            EntityTypePropertyWrapper<E> newWrapper = new EntityTypePropertyWrapper<>(newEntityType);

            return copyProperties(parentWrapper, newWrapper);
        } else return create(newEntityType);
    }

    /**
     * Shortcut utility method centered around copying builder properties over from one ETPW instance to another.
     *
     * @param from The ETPW instance to copy properties from.
     * @param to The ETPW instance to copy properties to.
     *
     * @return The provided ETPW instance with copied properties.
     *
     * @param <E> Any {@link Entity} subclass.
     */
    public static <E extends Entity> EntityTypePropertyWrapper<E> copyProperties(EntityTypePropertyWrapper<E> from, EntityTypePropertyWrapper<E> to) {
        return to.builder()
                .withCustomName(from.builder.manuallyLocalizedItemName)
                .withCustomSeparatorWords(from.builder.definedSeparatorWords)
                .withLocalization(from.builder.entityTypeTranslationFunc)
                .withSetTags(List.copyOf(from.builder.parentTags))
                .literalTranslation(from.builder.literalTranslation)
                .build(); // Direct setting of the builder would copy the entire object itself, which would in-turn overwrite it if any calls are made to the copied ETPW afterward
    }

    /**
     * Constructs a builder chain in which certain datagen properties can be assigned and re-built with in this EntityTypePropertyWrapper instance. Also sets
     * this ETPW instance's {@link #builder} to the newly-constructed {@link ETPWBuilder<E>} instance.
     *
     * @return A new {@link ETPWBuilder<E>} instance from the {@link #builder} field.
     */
    public ETPWBuilder<E> builder() {
        return this.builder = new ETPWBuilder<>(this, parentEntityType);
    }

    /**
     * Gets the cached {@link ETPWBuilder<E>} instance from the {@link #builder} if it exists. May be {@code null}. Useful for
     * overriding specific properties after having copied another ETPW instance/already set an ETPWBuilder.
     *
     * @return The cached {@link ETPWBuilder<E>} instance, or {@code null} if the {@link #builder} is {@code null}.
     *
     * @see #of(EntityTypePropertyWrapper, Supplier)
     * @see #ofTemplate(EntityTypePropertyWrapper)
     */
    @Nullable
    public ETPWBuilder<E> cachedBuilder() {
        return builder;
    }

    /**
     * Gets the parent {@link Supplier<EntityType<E>>} of this ETPW instance.
     *
     * @return The parent {@link Supplier<EntityType<E>>} stored in this ETPW instance.
     */
    public Supplier<EntityType<E>> getParentEntityType() {
        return parentEntityType;
    }

    /**
     * Gets the manually localized entity type name from the {@link #builder()} if the builder exists.
     *
     * @return The manually localized entity type name, or an empty {@code String} if the {@link #builder()} is {@code null}.
     */
    public String getManuallyLocalizedItemName() {
        return builder == null ? "" : builder.manuallyLocalizedItemName;
    }

    /**
     * Gets whether this ETPW instance bypasses default translation corrections.
     *
     * @return Whether this ETPW instance bypasses default translation corrections.
     */
    public boolean hasLiteralTranslation() {
        return builder != null && builder.literalTranslation;
    }

    /**
     * Gets the defined separator words from the {@link #builder()} if the builder exists.
     *
     * @return The defined separator words, or an empty {@link ObjectArrayList} if the {@link #builder()} is {@code null}.
     */
    public List<String> getDefinedSeparatorWords() {
        return builder == null ? ObjectArrayList.of() : builder.definedSeparatorWords;
    }

    /**
     * Gets the localization {@code Function<String, String>} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be {@code null}.
     *
     * @return The {@code Function<String, String>}, or {@code null} if the {@link #builder()} is {@code null} || it isn't defined within said builder.
     */
    @Nullable
    public Function<String, String> getEntityTypeTranslationFunc() {
        return builder == null ? null : builder.entityTypeTranslationFunc;
    }

    /**
     * Gets the defined parent {@linkplain Supplier<TagKey<EntityType<E>>> Tags} from the {@link #builder()} if the builder exists.
     *
     * @return The defined parent {@linkplain Supplier<TagKey<EntityType<E>>> Tags}, or an empty {@link ObjectArrayList} if the {@link #builder()} is {@code null}.
     */
    public List<Supplier<TagKey<EntityType<E>>>> getParentTags() {
        return builder == null ? ObjectArrayList.of() : builder.parentTags;
    }

    /**
     * Gets the {@code Supplier<AttributeSupplier.Builder>} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be {@code null}.
     *
     * @return The {@code Supplier<AttributeSupplier.Builder>}, or {@code null} if the {@link #builder()} is {@code null} || it isn't defined within said builder.
     */
    @Nullable
    public Supplier<AttributeSupplier.Builder> getAttributeBuilder() {
        return builder == null ? null : builder.attribBuilder;
    }

    /**
     * Gets the {@code Supplier<ClientDataEntry>} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be {@code null}.
     *
     * @return The {@code Supplier<ClientDataEntry>}, or {@code null} if the {@link #builder()} is {@code null} || it isn't defined within said builder.
     */
    @Nullable
    public Supplier<ClientDataEntry> getClientDataEntry() {
        return builder == null ? null : builder.clientDataEntry;
    }

    /**
     * Whether this ETPW instance is a template. Templates are not stored in {@link #getMappedEtpws()} and have no parent {@link EntityType}.
     *
     * @return Whether this ETPW instance is a template.
     *
     * @see #of(EntityTypePropertyWrapper, Supplier)
     * @see #createTemplate()
     */
    public boolean isTemplate() {
        return isTemplate;
    }

    /**
     * Gets an immutable view (via {@link ImmutableSortedMap}) of {@link #MAPPED_ETPWS}.
     *
     * @return An immutable view (via {@link ImmutableSortedMap}) of {@link #MAPPED_ETPWS}.
     */
    public static ImmutableSortedMap<Supplier<? extends EntityType<?>>, EntityTypePropertyWrapper<?>> getMappedEtpws() {
        return ImmutableSortedMap.copyOf(MAPPED_ETPWS);
    }

    /**
     * A builder class used to construct certain entity type-related data for datagen and other backend data, such as attributes.
     */
    public static class ETPWBuilder<E extends Entity> {
        private final EntityTypePropertyWrapper<E> ownerWrapper;
        private final Supplier<EntityType<E>> entityTypeParent;
        private String manuallyLocalizedItemName = "";
        private List<String> definedSeparatorWords = ObjectArrayList.of();
        private final List<Supplier<TagKey<EntityType<E>>>> parentTags = ObjectArrayList.of();
        @Nullable
        private Function<String, String> entityTypeTranslationFunc;
        private boolean literalTranslation = false;
        private Supplier<AttributeSupplier.Builder> attribBuilder;
        private Supplier<ClientDataEntry> clientDataEntry;

        private ETPWBuilder(EntityTypePropertyWrapper<E> ownerWrapper, Supplier<EntityType<E>> entityTypeParent) {
            this.ownerWrapper = ownerWrapper;
            this.entityTypeParent = entityTypeParent;
        }

        /**
         * Assigns a custom translation key for datagen. By default, a basic regex algorithm is used to automatically localize
         * the entity type name into something more legible (I.E. The names you see in-game). This property is simply an override
         * mechanic which aims to give the end-developer more control over the resulting name instead of being forced to rely on
         * the aforementioned algorithm.
         * <p></p>
         * The algorithm in question, in a nutshell, works as follows (the code block below is purely demonstrative of the
         * localization process and has nothing to do with how the algorithm is actually written):
         * <pre>
         *     {@code
         *      public class AlgorithmExampleDescriptor {
         *
         *          public static void main(String[] args) {
         *              // Input
         *              String unlocalizedName = "entity.chaosawakens.robo_pounder"; // The registry name/initial un-localized name
         *
         *              // Steps
         *              AlgorithmLanguageProvider.validateNullity(unlocalizedName); // Checks whether the provided 'unlocalizedName' is empty/all whitespaces/you get the point
         *              AlgorithmLanguageProvider.validateRegex(unlocalizedName); // Checks whether the provided 'unlocalizedName' has the signature registry name separator character "."
         *              AlgorithmLanguageProvider.formatCaps(unlocalizedName); // Output: "Entity.Chaosawakens.Robo_Pounder" <-- Capitalizes the first letter of each word based on regex-checks for special separators ("." and "_") (First character all the way to the left is always capitalized (duh), not that it matters)
         *              AlgorithmLanguageProvider.formatSeparators(unlocalizedName); // Output: "Entity.Chaosawakens.Robo_Pounder" <-- Any defined "separator" Strings are lowercased, see #withCustomSeparatorWords(List). In this case, there aren't any, so this step does nothing
         *              AlgorithmLanguageProvider.formatSpecialSeparators(unlocalizedName); // Output: "Robo Pounder" <-- All characters preceding the last "." are substringed/removed, and then any "_" characters are replaced with whitespaces
         *
         *              // End result
         *              System.out.println(unlocalizedName); // Output: "Robo Pounder"
         *          }
         *      }
         *     }
         * </pre>
         *
         * @param manuallyLocalizedItemName The name override used to localize the parent {@linkplain EntityType EntityType's} registry name.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomSeparatorWords(List)
         */
        public ETPWBuilder<E> withCustomName(String manuallyLocalizedItemName) {
            this.manuallyLocalizedItemName = manuallyLocalizedItemName;
            return this;
        }

        /**
         * Assigns a {@link List} of custom separator words which are lowercased during the algorithm's de-localization process. This is ignored if {@link #manuallyLocalizedItemName} is defined, {@link #literalTranslation}
         * is {@code true}, or if {@link #entityTypeTranslationFunc} is non-null. The default entries for this are {"Of", "And"}. This {@link List} is appended to the default separator definitions rather than replacing them.
         *
         * @param definedSeparatorWords The {@link List} of custom separator words to lowercase while the algorithm is running.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomName(String)
         * @see #withLocalization(Function)
         * @see #literalTranslation(boolean)
         */
        public ETPWBuilder<E> withCustomSeparatorWords(List<String> definedSeparatorWords) {
            this.definedSeparatorWords = definedSeparatorWords;
            return this;
        }

        /**
         * Marks this builder as using literal translations, meaning that corrections (like the one seen in the example provided by {@link #withCustomName(String)}) are not applied. Useless on entity types (for now).
         *
         * @param literalTranslation Whether to use literal translations.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomName(String)
         * @see #withLocalization(Function)
         * @see #literalTranslation()
         */
        public ETPWBuilder<E> literalTranslation(boolean literalTranslation) {
            this.literalTranslation = literalTranslation;
            return this;
        }

        /**
         * A custom {@link Function} to apply miscellaneous modifications to the resulting localized entity type name. This is influenced by {@link #withCustomName(String)} and {@link #literalTranslation(boolean)},
         * where applicable.
         *
         * @param entityTypeTranslationFunc The {@link Function} responsible for directly modifying the resulting localized entity type name.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomName(String)
         * @see #literalTranslation(boolean)
         */
        public ETPWBuilder<E> withLocalization(Function<String, String> entityTypeTranslationFunc) {
            this.entityTypeTranslationFunc = entityTypeTranslationFunc;
            return this;
        }

        /**
         * Overloaded variant of {@link #literalTranslation(boolean)} which marks this builder as using literal translations.
         *
         * @return {@code this} (builder method).
         *
         * @see #literalTranslation(boolean)
         */
        public ETPWBuilder<E> literalTranslation() {
            return literalTranslation(true);
        }

        /**
         * Tags this ETPWBuilder's parent {@link EntityType<E>} with the provided {@link TagKey<EntityType<E>>}.
         *
         * @param parentEntityTypeTags The {@link TagKey<EntityType<E>>} with which this ETPW's parent {@link EntityType<E>} will be tagged. May only be of type {@link EntityType<E>}.
         *
         * @return {@code this} (builder method).
         */
        public ETPWBuilder<E> withTag(Supplier<TagKey<EntityType<E>>> parentEntityTypeTags) {
            this.parentTags.add(parentEntityTypeTags);
            return this;
        }

        /**
         * Tags this ETPWBuilder's parent {@link EntityType<E>} with the provided {@linkplain TagKey<EntityType<E>> Tags}. Appends to the existing list.
         *
         * @param parentEntityTypeTags The {@linkplain TagKey<EntityType<E>> TagKeys} with which this ETPW's parent {@link EntityType<E>} will be tagged. May only be of type {@link EntityType<E>}.
         *
         * @return {@code this} (builder method).
         *
         * @see #withSetTags(List)
         */
        public ETPWBuilder<E> withTags(List<Supplier<TagKey<EntityType<E>>>> parentEntityTypeTags) {
            this.parentTags.addAll(parentEntityTypeTags);
            return this;
        }

        /**
         * Tags this ETPWBuilder's parent block with the provided {@linkplain TagKey<EntityType<E>> Tags}. Overwrites the existing list.
         *
         * @param parentEntityTypeTags The {@linkplain TagKey<EntityType<E>> TagKeys} with which this ETPW's parent {@link EntityType<E>} will be tagged. May only be of type {@link EntityType<E>}.
         *
         * @return {@code this} (builder method).
         *
         * @see #withTags(List)
         */
        public ETPWBuilder<E> withSetTags(List<Supplier<TagKey<EntityType<E>>>> parentEntityTypeTags) {
            this.parentTags.clear();
            this.parentTags.addAll(parentEntityTypeTags);
            return this;
        }

        /**
         * Specifies the attributes for the owner entity type.
         *
         * @param attributeBuilder The attribute builder for the owner entity type.
         *
         * @return {@code this} (builder method).
         */
        public ETPWBuilder<E> withAttributes(Supplier<AttributeSupplier.Builder> attributeBuilder) {
            this.attribBuilder = attributeBuilder;
            return this;
        }

        /**
         * Specifies the {@link ClientDataEntry} for the owner entity type.
         * <br></br>
         * This is typically used as an identifier for animatable renderers and models.
         *
         * @param attributeBuilder The {@link ClientDataEntry} for the owner entity type.
         *
         * @return {@code this} (builder method).
         */
        public ETPWBuilder<E> withClientDataEntry(Supplier<ClientDataEntry> attributeBuilder) {
            this.clientDataEntry = attributeBuilder;
            return this;
        }

        /**
         * Builds a new {@link EntityTypePropertyWrapper<E>} using this builder's data. Also maps the owner
         * {@link EntityTypePropertyWrapper<E>} to the parent {@linkplain EntityType<E>} if the owner is not a template.
         *
         * @return The newly data-populated {@link EntityTypePropertyWrapper<E>}.
         *
         * @see EntityTypePropertyWrapper#isTemplate()
         */
        public EntityTypePropertyWrapper<E> build() {
            if (!ownerWrapper.isTemplate) MAPPED_ETPWS.putIfAbsent(ownerWrapper.entityTypeRegName == null ? ownerWrapper.parentEntityType : () -> BuiltInRegistries.ENTITY_TYPE.get(CAConstants.prefix(ownerWrapper.entityTypeRegName)), ownerWrapper);
            return ownerWrapper;
        }
    }
}
