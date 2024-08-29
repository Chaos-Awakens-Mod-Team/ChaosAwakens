package io.github.chaosawakens.api.item;

import com.google.common.collect.ImmutableSortedMap;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.api.datagen.item.ItemModelDefinition;
import io.github.chaosawakens.common.registry.CAItems;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A wrapper class used to store information referenced in datagen to simplify creating data entries for items.
 */
public class ItemPropertyWrapper {
    private static final Object2ObjectLinkedOpenHashMap<Supplier<Item>, ItemPropertyWrapper> MAPPED_IPWS = new Object2ObjectLinkedOpenHashMap<>();
    @Nullable
    private final String itemRegName;
    private final Supplier<Item> parentItem;
    private final boolean isTemplate;
    @Nullable
    private IPWBuilder builder;

    private ItemPropertyWrapper(@Nullable String itemRegName, Supplier<Item> parentItem) {
        this.itemRegName = itemRegName;
        this.parentItem = parentItem;
        this.isTemplate = false;
    }

    private ItemPropertyWrapper(Supplier<Item> parentItem) {
        this(null, parentItem);
    }

    private ItemPropertyWrapper() {
        this.itemRegName = null;
        this.parentItem = null;
        this.isTemplate = true; // Otherwise can't set with constructor overloading (Laziness:tm:)
    }

    /**
     * Creates a new {@link ItemPropertyWrapper} instance. This is usually where you'll begin chaining {@link #builder()} method calls if needed. Use this variant if you want to directly create an IPW during a registration call
     * rather than after it/without storing it.
     *
     * @param itemRegName The registry name of the {@code parentItem}. Used when access to the {@code parentItem} returns an air/{@code null} delegate (I.E. It's too early to access
     *                       the parent item).
     * @param parentItem The parent {@link Supplier<Item>} stored in the newly-initialized IPW instance.
     *
     * @return A new {@link ItemPropertyWrapper} instance.
     */
    public static ItemPropertyWrapper create(String itemRegName, Supplier<Item> parentItem) {
        return new ItemPropertyWrapper(itemRegName, parentItem);
    }

    /**
     * Creates a new {@link ItemPropertyWrapper} instance. This is usually where you'll begin chaining {@link #builder()} method calls if needed. Use this variant if you want to create an IPW instance with a stored registration call,
     * such that its parent {@link Supplier<Item>} is not an air delegate/{@code null}.
     *
     * @param parentItem The parent {@link Supplier<Item>} stored in the newly-initialized IPW instance.
     *
     * @return A new {@link ItemPropertyWrapper} instance.
     *
     * @see #of(Supplier, Supplier)
     * @see #of(String, Supplier)
     * @see #of(ItemPropertyWrapper, Supplier)
     */
    public static ItemPropertyWrapper create(Supplier<Item> parentItem) {
        return new ItemPropertyWrapper(parentItem);
    }

    /**
     * Creates a new {@link ItemPropertyWrapper} instance as a template. Template BPWs are not stored in {@link #MAPPED_IPWS} and do not store a parent {@link Item}.
     * They're particularly useful for re-using across multiple {@linkplain Item Items}.
     *
     * @return A new {@link ItemPropertyWrapper} instance, set as a template.
     *
     * @see #of(ItemPropertyWrapper, Supplier)
     * @see #isTemplate()
     * @see #ofTemplate(ItemPropertyWrapper)
     */
    public static ItemPropertyWrapper createTemplate() {
        return new ItemPropertyWrapper();
    }

    /**
     * Creates a new {@link ItemPropertyWrapper} instance as a template, inheriting data from the provided BPW template. Template IPWs are not stored in {@link #MAPPED_IPWS} and do not store a parent {@link Item}.
     * They're particularly useful for re-using across multiple {@linkplain Item Items}.
     *
     * @param parentTemplateWrapper The parent {@link ItemPropertyWrapper} template from which {{@link #builder()}} data should be copied.
     *
     * @return A new {@link ItemPropertyWrapper} instance, set as a template, inheriting from the provided IPW template. If the provided IPW template is {@code null}, returns {@link #createTemplate()}.
     *
     * @see #createTemplate()
     * @see #isTemplate()
     */
    public static ItemPropertyWrapper ofTemplate(ItemPropertyWrapper parentTemplateWrapper) {
        if (parentTemplateWrapper != null) {
            ItemPropertyWrapper newTemplateWrapper = new ItemPropertyWrapper();

            newTemplateWrapper.builder()
                    .withCustomName(parentTemplateWrapper.builder.manuallyLocalizedItemName)
                    .withCustomSeparatorWords(parentTemplateWrapper.builder.definedSeparatorWords)
                    .withSetTags(List.copyOf(parentTemplateWrapper.builder.parentTags))
                    .withSetCustomModelDefinitions(List.copyOf(parentTemplateWrapper.builder.itemModelDefinitions))
                    .withRecipe(parentTemplateWrapper.builder.recipeBuilderFunction)
                    .build(); // Direct setting of the builder would copy the entire object itself, which would in-turn overwrite it if any calls are made to the copied IPW afterward

            return newTemplateWrapper;
        } else return createTemplate();
    }

    /**
     * Creates a new {@link ItemPropertyWrapper} instance based on the provided {@link ItemPropertyWrapper}. If the provided IPW instance is {@code null},
     * returns {@link #create(Supplier)}. You'd typically use this if you have an IPW template you want multiple registered {@linkplain Item Items} to inherit from.
     *
     * @param parentWrapper The parent {@link ItemPropertyWrapper} instance from which {@link #builder()} should be copied.
     * @param newItem The new registry entry to use for the newly constructed IPW instance.
     *
     * @return A new {@link ItemPropertyWrapper} instance with copied properties based on the provided IPW, or an entirely new/clean instance if the provided IPW is {@code null}.
     *
     * @see #of(Supplier, Supplier)
     * @see #of(String, Supplier)
     * @see #create(Supplier)
     * @see #create(String, Supplier)
     * @see #createTemplate()
     */
    public static ItemPropertyWrapper of(ItemPropertyWrapper parentWrapper, Supplier<Item> newItem) {
        if (parentWrapper != null) {
            ItemPropertyWrapper newWrapper = new ItemPropertyWrapper(newItem);

            newWrapper.builder()
                    .withCustomName(parentWrapper.builder.manuallyLocalizedItemName)
                    .withCustomSeparatorWords(parentWrapper.builder.definedSeparatorWords)
                    .withSetTags(List.copyOf(parentWrapper.builder.parentTags))
                    .withSetCustomModelDefinitions(List.copyOf(parentWrapper.builder.itemModelDefinitions))
                    .withRecipe(parentWrapper.builder.recipeBuilderFunction)
                    .build(); // Direct setting of the builder would copy the entire object itself, which would in-turn overwrite it if any calls are made to the copied IPW afterward

            return newWrapper;
        } else return create(newItem);
    }

    /**
     * Creates a new {@link ItemPropertyWrapper} instance from an existing {@link BlockPropertyWrapper} instance based on the provided
     * {@link Supplier<Item>}. If no such existing IPW instance exists, returns {@link #create(Supplier)}.
     *
     * @param parentItem The parent {@link Supplier<Item>} stored in {@link #MAPPED_IPWS}. Copies its IPW instance's {@link IPWBuilder}
     *                    properties if it exists, or creates a clean new IPW instance if it doesn't.
     * @param newItem The new registry entry to use for the newly constructed IPW instance.
     *
     * @return A new {@link ItemPropertyWrapper} instance with copied properties based on the provided {@link Supplier<Item>}, or an entirely new/clean instance if no such IPW exists.
     *
     * @see #create(Supplier)
     * @see #create(String, Supplier)
     * @see #of(String, Supplier)
     * @see #of(ItemPropertyWrapper, Supplier)
     */
    public static ItemPropertyWrapper of(Supplier<Item> parentItem, Supplier<Item> newItem) {
        if (MAPPED_IPWS.containsKey(parentItem)) {
            ItemPropertyWrapper originalWrapper = MAPPED_IPWS.get(parentItem);
            ItemPropertyWrapper newWrapper = new ItemPropertyWrapper(newItem);

            newWrapper.builder()
                    .withCustomName(originalWrapper.builder.manuallyLocalizedItemName)
                    .withCustomSeparatorWords(originalWrapper.builder.definedSeparatorWords)
                    .withSetTags(List.copyOf(originalWrapper.builder.parentTags))
                    .withSetCustomModelDefinitions(List.copyOf(originalWrapper.builder.itemModelDefinitions))
                    .withRecipe(originalWrapper.builder.recipeBuilderFunction)
                    .build(); // Direct setting of the builder would copy the entire object itself, which would in-turn overwrite it if any calls are made to the copied IPW afterward

            return newWrapper;
        } else return create(newItem);
    }

    /**
     * Overloaded variant of {@link #of(Supplier, Supplier)} that copies the parent item's {@link Item.Properties}.
     *
     * @param newItemRegName The new registry name by which the newly constructed {@link Supplier<Item>} instance will be stored.
     * @param parentItem The parent {@link Supplier<Item>} stored in {@link #MAPPED_IPWS}.
     *
     * @return A new {@link ItemPropertyWrapper} instance with copied properties (including {@link Item.Properties}) based on the provided {@link Supplier<Item>},
     * or an entirely new/clean instance if no such IPW exists.
     *
     * @see #of(ItemPropertyWrapper, Supplier)
     * @see #of(Supplier, Supplier)
     * @see #create(Supplier)
     * @see #create(String, Supplier)
     */
    public static ItemPropertyWrapper of(String newItemRegName, Supplier<Item> parentItem) {
        Item parentAsItem = parentItem.get();
        Item.Properties copiedProperties = new Item.Properties()
                .rarity(parentAsItem.getRarity(parentAsItem.getDefaultInstance()))
                .stacksTo(parentAsItem.getMaxStackSize());

        if (parentAsItem.getMaxDamage() > 0) copiedProperties.durability(parentAsItem.getMaxDamage()); // Needed to avoid conflict between stack size and durability :moyai:
        if (parentAsItem.hasCraftingRemainingItem()) copiedProperties.craftRemainder(parentAsItem.getCraftingRemainingItem());
        if (parentAsItem.getFoodProperties() != null) copiedProperties.food(parentAsItem.getFoodProperties());
        if (parentAsItem.isFireResistant()) copiedProperties.fireResistant();
        if (parentAsItem.requiredFeatures() != FeatureFlags.VANILLA_SET) copiedProperties.requiredFeatures = parentAsItem.requiredFeatures(); // Direct setting cuz the builder method is about as useful as FeatureFlags themselves in MC

        return of(parentItem, CAItems.registerExternalItem(newItemRegName, () -> new Item(copiedProperties)));
    }

    /**
     * Constructs a builder chain in which certain datagen properties can be assigned and re-built with in this ItemPropertyWrapper instance. Also sets
     * this IPW instance's {@link #builder} to the newly-constructed {@link IPWBuilder} instance.
     *
     * @return A new {@link IPWBuilder} instance from the {@link #builder} field.
     */
    public IPWBuilder builder() {
        return this.builder = new IPWBuilder(this, parentItem);
    }

    /**
     * Gets the cached {@link IPWBuilder} instance from the {@link #builder} if it exists. May be {@code null}. Useful for
     * overriding specific properties after having copied another IPW instance/already set a IPWBuilder.
     *
     * @return The cached {@link IPWBuilder} instance, or {@code null} if the {@link #builder} is {@code null}.
     *
     * @see #of(String, Supplier)
     * @see #of(Supplier, Supplier)
     */
    @Nullable
    public IPWBuilder cachedBuilder() {
        return builder;
    }

    /**
     * Gets the parent {@link Supplier<Item>} of this IPW instance.
     *
     * @return The parent {@link Supplier<Item>} stored in this IPW instance.
     */
    public Supplier<Item> getParentItem() {
        return parentItem;
    }

    /**
     * Gets the manually localized item name from the {@link #builder()} if the builder exists.
     *
     * @return The manually localized item name, or an empty {@code String} if the {@link #builder()} is {@code null}.
     */
    public String getManuallyLocalizedItemName() {
        return builder == null ? "" : builder.manuallyLocalizedItemName;
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
     * Gets the defined parent {@linkplain TagKey<Item> Tags} from the {@link #builder()} if the builder exists.
     *
     * @return The defined parent {@linkplain TagKey<Item> Tags}, or an empty {@link ObjectArrayList} if the {@link #builder()} is {@code null}.
     */
    public List<TagKey<Item>> getParentTags() {
        return builder == null ? ObjectArrayList.of() : builder.parentTags;
    }

    /**
     * Gets the {@link List} of {@linkplain ItemModelDefinition ItemModelDefinitions} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be {@code null}.
     *
     * @return The {@link List} of {@linkplain ItemModelDefinition ItemModelDefinitions}, or an empty {@link ObjectArrayList} if the {@link #builder()} is {@code null}.
     */
    public List<ItemModelDefinition> getItemModelDefinitions() {
        return builder == null ? ObjectArrayList.of() : builder.itemModelDefinitions;
    }

    /**
     * Gets the {@code Function<Consumer<FinishedRecipe>, Consumer<Supplier<Item>>>} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be {@code null}.
     *
     * @return The {@code Function<Consumer<FinishedRecipe>, Consumer<Supplier<Item>>>}, or {@code null} if the {@link #builder()} is {@code null} || it isn't defined within said builder.
     */
    @Nullable
    public Function<Consumer<FinishedRecipe>, Consumer<Supplier<Item>>> getRecipeMappingFunction() {
        return builder == null ? null : builder.recipeBuilderFunction;
    }

    /**
     * Whether this IPW instance is a template. Templates are not stored in {@link #getMappedIpws()} and have no parent {@link Item}.
     *
     * @return Whether this IPW instance is a template.
     *
     * @see #of(ItemPropertyWrapper, Supplier)
     * @see #createTemplate()
     */
    public boolean isTemplate() {
        return isTemplate;
    }

    /**
     * Gets an immutable view (via {@link ImmutableSortedMap}) of {@link #MAPPED_IPWS}.
     *
     * @return An immutable view (via {@link ImmutableSortedMap}) of {@link #MAPPED_IPWS}.
     */
    public static ImmutableSortedMap<Supplier<Item>, ItemPropertyWrapper> getMappedIpws() {
        return ImmutableSortedMap.copyOf(MAPPED_IPWS);
    }

    /**
     * A builder class used to construct certain item-related data for datagen.
     */
    public static class IPWBuilder {
        private final ItemPropertyWrapper ownerWrapper;
        private final Supplier<Item> itemParent;
        private String manuallyLocalizedItemName = "";
        private List<String> definedSeparatorWords = ObjectArrayList.of();
        private final List<TagKey<Item>> parentTags = ObjectArrayList.of();
        private final List<ItemModelDefinition> itemModelDefinitions = ObjectArrayList.of();
        @Nullable
        private Function<Consumer<FinishedRecipe>, Consumer<Supplier<Item>>> recipeBuilderFunction;

        private IPWBuilder(ItemPropertyWrapper ownerWrapper, Supplier<Item> itemParent) {
            this.ownerWrapper = ownerWrapper;
            this.itemParent = itemParent;
        }

        /**
         * Assigns a custom translation key for datagen. By default, a basic regex algorithm is used to automatically localize
         * the item name into something more legible (I.E. The names you see in-game). This property is simply an override
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
         *              String unlocalizedName = "item.chaosawakens.royal_guardian_sword"; // The registry name/initial un-localized name
         *
         *              // Steps
         *              AlgorithmLanguageProvider.validateNullity(unlocalizedName); // Checks whether the provided 'unlocalizedName' is empty/all whitespaces/you get the point
         *              AlgorithmLanguageProvider.validateRegex(unlocalizedName); // Checks whether the provided 'unlocalizedName' has the signature registry name separator character "."
         *              AlgorithmLanguageProvider.formatCaps(unlocalizedName); // Output: "Item.Chaosawakens.Royal_Guardian_Sword" <-- Capitalizes the first letter of each word based on regex-checks for special separators ("." and "_") (First character all the way to the left is always capitalized (duh), not that it matters)
         *              AlgorithmLanguageProvider.formatSeparators(unlocalizedName); // Output: "Block.Chaosawakens.Royal_Guardian_Sword" <-- Any defined "separator" Strings are lowercased, see #withCustomSeparatorWords(List). In this case, there aren't any, so this step does nothing
         *              AlgorithmLanguageProvider.formatSpecialSeparators(unlocalizedName); // Output: "Royal Guardian Sword" <-- All characters preceding the last "." are substringed/removed, and then any "_" characters are replaced with whitespaces
         *
         *              // End result
         *              System.out.println(unlocalizedName); // Output: "Royal Guardian Sword"
         *          }
         *      }
         *     }
         * </pre>
         *
         * @param manuallyLocalizedItemName The name override used to localize the parent {@linkplain Item Item's} registry name.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomSeparatorWords(List)
         */
        public IPWBuilder withCustomName(String manuallyLocalizedItemName) {
            this.manuallyLocalizedItemName = manuallyLocalizedItemName;
            return this;
        }

        /**
         * Assigns a {@link List} of custom separator words which are lowercased during the algorithm's de-localization process. This is ignored if {@link #manuallyLocalizedItemName} is defined.
         * The default entries for this are {"Of", "And"}. This {@link List} is appended to the default separator definitions rather than replacing them.
         *
         * @param definedSeparatorWords The {@link List} of custom separator words to lowercase while the algorithm is running.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomName(String)
         */
        public IPWBuilder withCustomSeparatorWords(List<String> definedSeparatorWords) {
            this.definedSeparatorWords = definedSeparatorWords;
            return this;
        }

        /**
         * Tags this IPWBuilder's parent {@link Item} with the provided {@link TagKey<Item>}.
         *
         * @param parentItemTag The {@link TagKey<Item>} with which this IPW's parent {@link Item} will be tagged. May only be of type {@link Item}.
         *
         * @return {@code this} (builder method).
         */
        public IPWBuilder withTag(TagKey<Item> parentItemTag) {
            this.parentTags.add(parentItemTag);
            return this;
        }

        /**
         * Tags this IPWBuilder's parent {@link Item} with the provided {@linkplain TagKey<Item> Tags}. Appends to the existing list.
         *
         * @param parentItemTags The {@linkplain TagKey<Item> TagKeys} with which this IPW's parent {@link Item} will be tagged. May only be of type {@link Item}.
         *
         * @return {@code this} (builder method).
         *
         * @see #withSetTags(List)
         */
        public IPWBuilder withTags(List<TagKey<Item>> parentItemTags) {
            this.parentTags.addAll(parentItemTags);
            return this;
        }

        /**
         * Tags this IPWBuilder's parent block with the provided {@linkplain TagKey<Item> Tags}. Overwrites the existing list.
         *
         * @param parentItemTags The {@linkplain TagKey<Item> TagKeys} with which this IPW's parent {@link Item} will be tagged. May only be of type {@link Item}.
         *
         * @return {@code this} (builder method).
         *
         * @see #withTags(List)
         */
        public IPWBuilder withSetTags(List<TagKey<Item>> parentItemTags) {
            this.parentTags.clear();
            this.parentTags.addAll(parentItemTags);
            return this;
        }

        /**
         * Appends a custom {@link ItemModelDefinition} to this builder.
         *
         * @param itemModelDefinition The {@link ItemModelDefinition} used to build this IPWBuilder's parent item's model(s) in datagen.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomModelDefinitions(List)
         */
        public IPWBuilder withCustomModelDefinition(ItemModelDefinition itemModelDefinition) {
            this.itemModelDefinitions.add(itemModelDefinition);
            return this;
        }

        /**
         * Appends a custom list of {@linkplain ItemModelDefinition ItemModelDefinitions} to this builder.
         *
         * @param itemModelDefinitions The {@link ItemModelDefinition} used to build this IPWBuilder's parent item's model in datagen.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomModelDefinition(ItemModelDefinition)
         */
        public IPWBuilder withCustomModelDefinitions(List<ItemModelDefinition> itemModelDefinitions) {
            this.itemModelDefinitions.addAll(itemModelDefinitions);
            return this;
        }

        /**
         * Sets a new custom list of {@linkplain ItemModelDefinition ItemModelDefinitions} to this builder.
         *
         * @param itemModelDefinitions The {@link ItemModelDefinition} used to build this IPWBuilder's parent item's model in datagen.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomModelDefinition(ItemModelDefinition)
         * @see #withCustomModelDefinitions(List)
         */
        public IPWBuilder withSetCustomModelDefinitions(List<ItemModelDefinition> itemModelDefinitions) {
            this.itemModelDefinitions.clear();
            this.itemModelDefinitions.addAll(itemModelDefinitions);
            return this;
        }

        /**
         * Defines a custom mapping function representing the parent {@linkplain Item Item's} recipe. IPWBuilders accepting more than 1 recipe function assume that each recipe has a unique recipe ID,
         * and thus recipes are generated under that constraint.
         *
         * @param recipeBuilderFunction The mapping function accepting a representation of the parent {@linkplain Item Item's} recipe.
         *
         * @return {@code this} (builder method).
         */
        public IPWBuilder withRecipe(Function<Consumer<FinishedRecipe>, Consumer<Supplier<Item>>> recipeBuilderFunction) {
            this.recipeBuilderFunction = recipeBuilderFunction;
            return this;
        }

        /**
         * Builds a new {@link ItemPropertyWrapper} using this builder's data. Also maps the owner
         * {@link ItemPropertyWrapper} to the parent {@linkplain Item} if the owner is not a template.
         *
         * @return The newly data-populated {@link ItemPropertyWrapper}.
         *
         * @see ItemPropertyWrapper#isTemplate()
         */
        public ItemPropertyWrapper build() {
            if (!ownerWrapper.isTemplate) MAPPED_IPWS.putIfAbsent(ownerWrapper.itemRegName == null ? ownerWrapper.parentItem : () -> BuiltInRegistries.ITEM.get(CAConstants.prefix(ownerWrapper.itemRegName)), ownerWrapper);
            return ownerWrapper;
        }
    }
}
