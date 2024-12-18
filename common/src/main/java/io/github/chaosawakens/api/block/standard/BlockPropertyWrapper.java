package io.github.chaosawakens.api.block.standard;

import com.google.common.collect.ImmutableSortedMap;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.datagen.block.BlockModelDefinition;
import io.github.chaosawakens.api.datagen.block.BlockStateDefinition;
import io.github.chaosawakens.common.registry.CABlocks;
import io.github.chaosawakens.util.LootUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A wrapper class used to store information referenced in datagen to simplify creating data entries for blocks.
 */
public class BlockPropertyWrapper { //TODO Maybe type param this for blocks
    private static final Object2ObjectLinkedOpenHashMap<Supplier<Block>, BlockPropertyWrapper> MAPPED_BPWS = new Object2ObjectLinkedOpenHashMap<>();
    @Nullable
    private final String blockRegName;
    private final Supplier<Block> parentBlock;
    private final boolean isTemplate;
    @Nullable
    private BPWBuilder builder;

    private BlockPropertyWrapper(@Nullable String blockRegName, Supplier<Block> parentBlock) {
        this.blockRegName = blockRegName;
        this.parentBlock = parentBlock;
        this.isTemplate = false;
    }

    private BlockPropertyWrapper(Supplier<Block> parentBlock) {
        this(null, parentBlock);
    }

    private BlockPropertyWrapper() {
        this.blockRegName = null;
        this.parentBlock = null;
        this.isTemplate = true; // Otherwise can't set with constructor overloading
    }

    /**
     * Creates a new {@link BlockPropertyWrapper} instance. This is usually where you'll begin chaining {@link #builder()} method calls if needed. Use this variant if you want to directly create a BPW during a registration call
     * rather than after it/without storing it.
     *
     * @param blockRegName The registry name of the {@code parentBlock}. Used when access to the {@code parentBlock} returns an air/{@code null} delegate (I.E. It's too early to access
     *                       the parent block).
     * @param parentBlock The parent {@link Supplier<Block>} stored in the newly-initialized BPW instance.
     *
     * @return A new {@link BlockPropertyWrapper} instance.
     *
     * @see #create(Supplier)
     * @see #of(Supplier, Supplier)
     * @see #of(String, Supplier)
     * @see #of(BlockPropertyWrapper, Supplier)
     */
    public static BlockPropertyWrapper create(String blockRegName, Supplier<Block> parentBlock) {
        return new BlockPropertyWrapper(blockRegName, parentBlock);
    }

    /**
     * Creates a new {@link BlockPropertyWrapper} instance. This is usually where you'll begin chaining {@link #builder()} method calls if needed. Use this variant if you want to create a BPW instance with a stored registration call,
     * such that its parent {@link Supplier<Block>} is not an air delegate/{@code null}.
     *
     * @param parentBlock The parent {@link Supplier<Block>} stored in the newly-initialized BPW instance.
     *
     * @return A new {@link BlockPropertyWrapper} instance.
     *
     * @see #create(String, Supplier)
     * @see #of(Supplier, Supplier)
     * @see #of(String, Supplier)
     * @see #of(BlockPropertyWrapper, Supplier)
     */
    public static BlockPropertyWrapper create(Supplier<Block> parentBlock) {
        return new BlockPropertyWrapper(parentBlock);
    }

    /**
     * Creates a new {@link BlockPropertyWrapper} instance as a template. Template BPWs are not stored in {@link #MAPPED_BPWS} and do not store a parent {@link Block}.
     * They're particularly useful for re-using across multiple {@linkplain Block Blocks}.
     *
     * @return A new {@link BlockPropertyWrapper} instance, set as a template.
     *
     * @see #of(BlockPropertyWrapper, Supplier)
     * @see #isTemplate()
     * @see #ofTemplate(BlockPropertyWrapper)
     */
    public static BlockPropertyWrapper createTemplate() {
        return new BlockPropertyWrapper();
    }

    /**
     * Creates a new {@link BlockPropertyWrapper} instance as a template, inheriting data from the provided BPW template. Template BPWs are not stored in {@link #MAPPED_BPWS} and do not store a parent {@link Block}.
     * They're particularly useful for re-using across multiple {@linkplain Block Blocks}.
     *
     * @param parentTemplateWrapper The parent {@link BlockPropertyWrapper} template from which {{@link #builder()}} data should be copied.
     *
     * @return A new {@link BlockPropertyWrapper} instance, set as a template, inheriting from the provided BPW template. If the provided BPW template is {@code null}, returns {@link #createTemplate()}.
     *
     * @see #createTemplate()
     * @see #isTemplate()
     */
    public static BlockPropertyWrapper ofTemplate(BlockPropertyWrapper parentTemplateWrapper) {
        if (parentTemplateWrapper != null) {
            BlockPropertyWrapper newTemplateWrapper = new BlockPropertyWrapper();

            newTemplateWrapper.builder()
                    .withCustomName(parentTemplateWrapper.builder.manuallyLocalizedBlockName)
                    .withCustomSeparatorWords(parentTemplateWrapper.builder.definedSeparatorWords)
                    .withSetTags(parentTemplateWrapper.builder.parentTags)
                    .withLootTable(parentTemplateWrapper.builder.blockLootTableBuilder)
                    .withSetCustomModelDefinitions(parentTemplateWrapper.builder.blockModelDefinitions)
                    .withCustomModelDefinitions(parentTemplateWrapper.builder.bmdMappingFunc)
                    .withBlockStateDefinition(parentTemplateWrapper.builder.blockStateDefinition)
                    .withRecipe(parentTemplateWrapper.builder.recipeBuilderFunction)
                    .withSetParentCreativeModeTabs(List.copyOf(parentTemplateWrapper.builder.parentTabs))
                    .withBlockColor(parentTemplateWrapper.builder.blockColorMappingFunc)
                    .build(); // Direct setting of the builder would copy the entire object itself, which would in-turn overwrite it if any calls are made to the copied BPW afterward

            return newTemplateWrapper;
        } else return createTemplate();
    }

    /**
     * Creates a new {@link BlockPropertyWrapper} instance based on the provided {@link BlockPropertyWrapper}. If the provided BPW instance is {@code null},
     * returns {@link #create(Supplier)}. You'd typically use this if you have a BPW template you want multiple registered {@linkplain Block Blocks} to inherit from.
     *
     * @param parentWrapper The parent {@link BlockPropertyWrapper} instance from which {@link #builder()} should be copied.
     * @param newBlock The new registry entry to use for the newly constructed BPW instance.
     *
     * @return A new {@link BlockPropertyWrapper} instance with copied properties based on the provided BPW, or an entirely new/clean instance if the provided BPW is {@code null}.
     *
     * @see #of(Supplier, Supplier)
     * @see #of(String, Supplier)
     * @see #create(Supplier)
     * @see #create(String, Supplier)
     * @see #createTemplate()
     */
    public static BlockPropertyWrapper of(BlockPropertyWrapper parentWrapper, Supplier<Block> newBlock) {
        if (parentWrapper != null) {
            BlockPropertyWrapper newWrapper = new BlockPropertyWrapper(newBlock);

            newWrapper.builder()
                    .withCustomName(parentWrapper.builder.manuallyLocalizedBlockName)
                    .withCustomSeparatorWords(parentWrapper.builder.definedSeparatorWords)
                    .withSetTags(List.copyOf(parentWrapper.builder.parentTags))
                    .withLootTable(parentWrapper.builder.blockLootTableBuilder)
                    .withSetCustomModelDefinitions(List.copyOf(parentWrapper.builder.blockModelDefinitions))
                    .withCustomModelDefinitions(parentWrapper.builder.bmdMappingFunc)
                    .withBlockStateDefinition(parentWrapper.builder.blockStateDefinition)
                    .withRecipe(parentWrapper.builder.recipeBuilderFunction)
                    .withSetParentCreativeModeTabs(List.copyOf(parentWrapper.builder.parentTabs))
                    .withBlockColor(parentWrapper.builder.blockColorMappingFunc)
                    .build(); // Direct setting of the builder would copy the entire object itself, which would in-turn overwrite it if any calls are made to the copied BPW afterward

            return newWrapper;
        } else return create(newBlock);
    }

    /**
     * Creates a new {@link BlockPropertyWrapper} instance from an existing {@link BlockPropertyWrapper} instance based on the provided
     * {@link Supplier<Block>}. If no such existing BPW instance exists, returns {@link #create(Supplier)}.
     *
     * @param parentBlock The parent {@link Supplier<Block>} stored in {@link #MAPPED_BPWS}. Copies its BPW instance's {@link BPWBuilder}
     *                    properties if it exists, or creates a clean new BPW instance if it doesn't.
     * @param newBlock The new registry entry to use for the newly constructed BPW instance.
     *
     * @return A new {@link BlockPropertyWrapper} instance with copied properties based on the provided {@link Supplier<Block>}, or an entirely new/clean instance if no such BPW exists.
     *
     * @see #create(Supplier)
     * @see #create(String, Supplier)
     * @see #of(String, Supplier)
     * @see #of(BlockPropertyWrapper, Supplier)
     */
    public static BlockPropertyWrapper of(Supplier<Block> parentBlock, Supplier<Block> newBlock) {
        if (MAPPED_BPWS.containsKey(parentBlock)) {
            BlockPropertyWrapper originalWrapper = MAPPED_BPWS.get(parentBlock);
            BlockPropertyWrapper newWrapper = new BlockPropertyWrapper(newBlock);

            newWrapper.builder()
                    .withCustomName(originalWrapper.builder.manuallyLocalizedBlockName)
                    .withCustomSeparatorWords(originalWrapper.builder.definedSeparatorWords)
                    .withSetTags(List.copyOf(originalWrapper.builder.parentTags))
                    .withLootTable(originalWrapper.builder.blockLootTableBuilder)
                    .withSetCustomModelDefinitions(List.copyOf(originalWrapper.builder.blockModelDefinitions))
                    .withCustomModelDefinitions(originalWrapper.builder.bmdMappingFunc)
                    .withBlockStateDefinition(originalWrapper.builder.blockStateDefinition)
                    .withRecipe(originalWrapper.builder.recipeBuilderFunction)
                    .withSetParentCreativeModeTabs(List.copyOf(originalWrapper.builder.parentTabs))
                    .withBlockColor(originalWrapper.builder.blockColorMappingFunc)
                    .build(); // Direct setting of the builder would copy the entire object itself, which would in-turn overwrite it if any calls are made to the copied BPW afterward

            return newWrapper;
        } else return create(newBlock);
    }

    /**
     * Overloaded variant of {@link #of(Supplier, Supplier)} that copies the parent block's {@link BlockBehaviour.Properties}.
     *
     * @param newBlockRegName The new registry name by which the newly constructed {@link Supplier<Block>} instance will be stored.
     * @param parentBlock The parent {@link Supplier<Block>} stored in {@link #MAPPED_BPWS}.
     *
     * @return A new {@link BlockPropertyWrapper} instance with copied properties (including {@link BlockBehaviour.Properties}) based on the provided {@link Supplier<Block>},
     * or an entirely new/clean instance if no such BPW exists.
     *
     * @see #of(BlockPropertyWrapper, Supplier)
     * @see #of(Supplier, Supplier)
     * @see #create(Supplier)
     * @see #create(String, Supplier)
     */
    public static BlockPropertyWrapper of(String newBlockRegName, Supplier<Block> parentBlock) {
        return of(parentBlock, CABlocks.registerExternalBlock(newBlockRegName, () -> new Block(BlockBehaviour.Properties.copy(parentBlock.get()))));
    }

    /**
     * Constructs a builder chain in which certain datagen properties can be assigned and re-built with in this BlockPropertyWrapper instance. Also sets
     * this BPW instance's {@link #builder} to the newly-constructed {@link BPWBuilder} instance.
     *
     * @return A new {@link BPWBuilder} instance from the {@link #builder} field.
     */
    public BPWBuilder builder() {
        return this.builder = new BPWBuilder(this, parentBlock);
    }

    /**
     * Gets the cached {@link BPWBuilder} instance from the {@link #builder} if it exists. May be {@code null}. Useful for
     * overriding specific properties after having copied another BPW instance/already set a BPWBuilder.
     *
     * @return The cached {@link BPWBuilder} instance, or {@code null} if the {@link #builder} is {@code null}.
     *
     * @see #of(String, Supplier)
     * @see #of(Supplier, Supplier)
     */
    @Nullable
    public BPWBuilder cachedBuilder() {
        return builder;
    }

    /**
     * Gets the parent {@link Supplier<Block>} of this BPW instance.
     *
     * @return The parent {@link Supplier<Block>} stored in this BPW instance.
     */
    public Supplier<Block> getParentBlock() {
        return parentBlock;
    }

    /**
     * Gets the manually localized block name from the {@link #builder()} if the builder exists.
     *
     * @return The manually localized block name, or an empty {@code String} if the {@link #builder()} is {@code null}.
     */
    public String getManuallyLocalizedBlockName() {
        return builder == null ? "" : builder.manuallyLocalizedBlockName;
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
     * Gets the {@code Function<Supplier<Block>, LootTable.Builder>} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be {@code null}.
     *
     * @return The {@code Function<Supplier<Block>, LootTable.Builder>}, or {@code null} if the {@link #builder()} is {@code null} || it isn't defined within said builder.
     */
    @Nullable
    public Function<Supplier<Block>, LootTable.Builder> getBlockLootTableMappingFunction() {
        return builder == null ? null : builder.blockLootTableBuilder;
    }

    /**
     * Gets the defined parent {@linkplain TagKey<?> Tags} from the {@link #builder()} if the builder exists.
     *
     * @return The defined parent {@linkplain TagKey<?> Tags}, or an empty {@link ObjectArrayList} if the {@link #builder()} is {@code null}.
     */
    public List<TagKey<?>> getParentTags() {
        return builder == null ? ObjectArrayList.of() : builder.parentTags;
    }

    /**
     * Gets the {@link List} of {@linkplain BlockModelDefinition BlockModelDefinitions} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be {@code null}. Usually needs to be populated before use (see references below).
     *
     * @return The {@link List} of {@linkplain BlockModelDefinition BlockModelDefinitions}, or an empty {@link ObjectArrayList} if the {@link #builder()} is {@code null}.
     *
     * @see #getBMDMappingFunction()
     */
    public List<BlockModelDefinition> getBlockModelDefinitions() {
        return builder == null ? ObjectArrayList.of() : builder.blockModelDefinitions;
    }

    /**
     * Gets the {@code Function<Supplier<Block>, List<BlockModelDefinition>>} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be {@code null}.
     *
     * @return The {@code Function<Supplier<Block>, List<BlockModelDefinition>>}, or {@code null} if the {@link #builder()} is {@code null} || it isn't defined within said builder.
     */
    @Nullable
    public Function<Supplier<Block>, List<BlockModelDefinition>> getBMDMappingFunction() {
        return builder == null ? null : builder.bmdMappingFunc;
    }


    /**
     * Gets the {@code Function<Supplier<Block>, BlockStateDefinition>} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be {@code null}.
     *
     * @return The {@code Function<Supplier<Block>, BlockStateDefinition>}, or {@code null} if the {@link #builder()} is {@code null} || it isn't defined within said builder.
     */
    @Nullable
    public Function<Supplier<Block>, BlockStateDefinition> getBlockStateDefinitionMappingFunction() {
        return builder == null ? null : builder.blockStateDefinition;
    }

    /**
     * Gets the {@code Function<Consumer<FinishedRecipe>, Consumer<Supplier<Block>>>>} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be {@code null}.
     *
     * @return The {@code Function<Consumer<FinishedRecipe>, Consumer<Supplier<Block>>>>}, or {@code null} if the {@link #builder()} is {@code null} || it isn't defined within said builder.
     */
    @Nullable
    public Function<Consumer<FinishedRecipe>, Consumer<Supplier<Block>>> getRecipeMappingFunction() {
        return builder == null ? null : builder.recipeBuilderFunction;
    }

    /**
     * Gets the {@link List} of parent {@linkplain CreativeModeTab CreativeModeTabs} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be empty.
     *
     * @return The {@link List} of parent {@linkplain CreativeModeTab CreativeModeTabs}, or an empty {@link ObjectArrayList} if the {@link #builder()} is {@code null}.
     */
    public List<Supplier<CreativeModeTab>> getParentCreativeModeTabs() {
        return builder == null ? ObjectArrayList.of() : builder.parentTabs;
    }

    /**
     * Gets the {@code Function<Supplier<Block>, BlockColor>} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be {@code null}.
     *
     * @return The {@code Function<Supplier<Block>, BlockColor>}, or {@code null} if the {@link #builder()} is {@code null} || it isn't defined within said builder.
     */
    @Nullable
    public Function<Supplier<Block>, BlockColor> getBlockColorMappingFunc() {
        return builder == null ? null : builder.blockColorMappingFunc;
    }

    /**
     * Whether this BPW instance is a template. Templates are not stored in {@link #getMappedBpws()} and have no parent {@link Block}.
     *
     * @return Whether this BPW instance is a template.
     *
     * @see #of(BlockPropertyWrapper, Supplier)
     * @see #createTemplate()
     */
    public boolean isTemplate() {
        return isTemplate;
    }

    /**
     * Gets an immutable view (via {@link ImmutableSortedMap}) of {@link #MAPPED_BPWS}.
     *
     * @return An immutable view (via {@link ImmutableSortedMap}) of {@link #MAPPED_BPWS}.
     */
    public static ImmutableSortedMap<Supplier<Block>, BlockPropertyWrapper> getMappedBpws() {
        return ImmutableSortedMap.copyOf(MAPPED_BPWS);
    }

    /**
     * A builder class used to construct certain block-related data for datagen.
     */
    public static class BPWBuilder {
        private final BlockPropertyWrapper ownerWrapper;
        private final Supplier<Block> parentBlock;
        private String manuallyLocalizedBlockName = "";
        private List<String> definedSeparatorWords = ObjectArrayList.of();
        @Nullable
        private Function<Supplier<Block>, LootTable.Builder> blockLootTableBuilder;
        private final List<TagKey<?>> parentTags = ObjectArrayList.of();
        private final List<BlockModelDefinition> blockModelDefinitions = ObjectArrayList.of();
        @Nullable
        private Function<Supplier<Block>, BlockStateDefinition> blockStateDefinition;
        @Nullable
        private Function<Consumer<FinishedRecipe>, Consumer<Supplier<Block>>> recipeBuilderFunction;
        @Nullable
        private Function<Supplier<Block>, List<BlockModelDefinition>> bmdMappingFunc;
        private List<Supplier<CreativeModeTab>> parentTabs = new ObjectArrayList<>(); // Not datagen-related but whatever
        @Nullable
        private Function<Supplier<Block>, BlockColor> blockColorMappingFunc;

        private BPWBuilder(BlockPropertyWrapper ownerWrapper, Supplier<Block> parentBlock) {
            this.ownerWrapper = ownerWrapper;
            this.parentBlock = parentBlock;
        }

        /**
         * Assigns a custom translation key for datagen. By default, a basic regex algorithm is used to automatically localize
         * the block name into something more legible (I.E. The names you see in-game). This property is simply an override
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
         *              String unlocalizedName = "block.chaosawakens.block_of_ruby"; // The registry name/initial unlocalized name
         *
         *              // Steps
         *              AlgorithmLanguageProvider.validateNullity(unlocalizedName); // Checks whether the provided 'unlocalizedName' is empty/all whitespaces/you get the point
         *              AlgorithmLanguageProvider.validateRegex(unlocalizedName); // Checks whether the provided 'unlocalizedName' has the signature registry name separator character "."
         *              AlgorithmLanguageProvider.formatCaps(unlocalizedName); // Output: "Block.Chaosawakens.Block_Of_Ruby" <-- Capitalizes the first letter of each word based on regex-checks for special separators ("." and "_") (First character all the way to the left is always capitalized (duh), not that it matters)
         *              AlgorithmLanguageProvider.formatSeparators(unlocalizedName); // Output: "Block.Chaosawakens.Block_of_Ruby" <-- Any defined "separator" Strings are lowercased, see #withCustomSeparatorWords(List)
         *              AlgorithmLanguageProvider.formatSpecialSeparators(unlocalizedName); // Output: "Block of Ruby" <-- All characters preceding the last "." are substringed/removed, and then any "_" characters are replaced with whitespaces
         *
         *              // End result
         *              System.out.println(unlocalizedName); // Output: "Block of Ruby"
         *          }
         *      }
         *     }
         * </pre>
         * <b>NOTE:</b> Block registry names ending with "_block" (e.g. "block.chaosawakens.royal_guardian_scale_block") have the "_block" part pruned and the result string is prepended with "Block of"
         * during the translation process (the registry name stays the same, of course). You may use this method to bypass that step if needed.
         *
         * @param manuallyLocalizedBlockName The name override used to localize the parent {@linkplain Block Block's} registry name.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomSeparatorWords(List)
         */
        public BPWBuilder withCustomName(String manuallyLocalizedBlockName) {
            this.manuallyLocalizedBlockName = manuallyLocalizedBlockName;
            return this;
        }

        /**
         * Assigns a {@link List} of custom separator words which are lowercased during the algorithm's de-localization process. This is ignored if {@link #manuallyLocalizedBlockName} is defined.
         * The default entries for this are {"Of", "And"}. This {@link List} is appended to the default separator definitions rather than replacing them.
         *
         * @param definedSeparatorWords The {@link List} of custom separator words to lowercase while the algorithm is running.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomName(String)
         */
        public BPWBuilder withCustomSeparatorWords(List<String> definedSeparatorWords) {
            this.definedSeparatorWords = definedSeparatorWords;
            return this;
        }

        /**
         * Assigns a given {@link LootTable.Builder} to this builder via the input function. Can be {@code null}.
         *
         * @param blockLootTableBuilder The mapping {@code Function<Supplier<Block>, LootTable.Builder>}
         *                              used to build this BPWBuilder's parent block's loot table in datagen.
         *
         * @return {@code this} (builder method).
         *
         * @see LootUtil
         */
        public BPWBuilder withLootTable(Function<Supplier<Block>, LootTable.Builder> blockLootTableBuilder) {
            this.blockLootTableBuilder = blockLootTableBuilder;
            return this;
        }

        /**
         * Tags this BPWBuilder's parent {@link Block} with the provided {@link TagKey<?>}.
         *
         * @param parentBlockTag The {@link TagKey<?>} with which this BPW's parent {@link Block} will be tagged. May generally be of types {@link Item} or {@link Block}.
         *
         * @return {@code this} (builder method).
         */
        public BPWBuilder withTag(TagKey<?> parentBlockTag) {
            this.parentTags.add(parentBlockTag);
            return this;
        }

        /**
         * Tags this BPWBuilder's parent {@link Block} with the provided {@linkplain TagKey<?> Tags}. Appends to the existing list.
         *
         * @param parentBlockTags The {@linkplain TagKey<?> TagKeys} with which this BPW's parent {@link Block} will be tagged. May generally be of types {@link Item} or {@link Block}.
         *
         * @return {@code this} (builder method).
         *
         * @see #withSetTags(List)
         */
        public BPWBuilder withTags(List<TagKey<?>> parentBlockTags) {
            this.parentTags.addAll(parentBlockTags);
            return this;
        }

        /**
         * Tags this BPWBuilder's parent {@link Block} with the provided {@linkplain TagKey<?> Tags}. Overwrites the existing list.
         *
         * @param parentBlockTags The {@linkplain TagKey<?> TagKeys} with which this BPW's parent {@link Block} will be tagged. May generally be of types {@link Item} or {@link Block}.
         *
         * @return {@code this} (builder method).
         *
         * @see #withTags(List)
         */
        public BPWBuilder withSetTags(List<TagKey<?>> parentBlockTags) {
            this.parentTags.clear();
            this.parentTags.addAll(parentBlockTags);
            return this;
        }

        /**
         * Appends a custom {@link BlockModelDefinition} to this builder. By default, model datagen is handled based on a series of
         * type checks (E.G. Doors, walls, fences, rotatable blocks, etc.). You can use this method if your custom block requires a
         * different model definition that isn't natively handled.
         *
         * @param blockModelDefinition The {@link BlockModelDefinition} used to build this BPWBuilder's parent block's model(s) in datagen.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomModelDefinitions(List)
         * @see #withCustomModelDefinitions(Function)
         */
        public BPWBuilder withCustomModelDefinition(BlockModelDefinition blockModelDefinition) {
            this.blockModelDefinitions.add(blockModelDefinition);
            return this;
        }

        /**
         * Appends a custom list of {@linkplain BlockModelDefinition BlockModelDefinitions} to this builder. By default, model datagen is handled based on a series of
         * type checks (E.G. Doors, walls, fences, rotatable blocks, etc.). You can use this method if your custom block requires a
         * different model definition that isn't natively handled.
         *
         * @param blockModelDefinitions The {@link BlockModelDefinition} used to build this BPWBuilder's parent block's model in datagen.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomModelDefinition(BlockModelDefinition)
         */
        public BPWBuilder withCustomModelDefinitions(List<BlockModelDefinition> blockModelDefinitions) {
            this.blockModelDefinitions.addAll(blockModelDefinitions);
            return this;
        }

        /**
         * Sets the {@link #bmdMappingFunc} of this BPWBuilder. This is used in conjunction with {@link #blockModelDefinitions} in order to generate models for the parent BPW's {@link Block}.
         *
         * @param bmdMappingFunc The mapping {@link Function} used to build this BPWBuilder's parent block's model in datagen.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomModelDefinition(BlockModelDefinition)
         */
        public BPWBuilder withCustomModelDefinitions(Function<Supplier<Block>, List<BlockModelDefinition>> bmdMappingFunc) {
            this.bmdMappingFunc = bmdMappingFunc;
            return this;
        }

        /**
         * Sets a new custom list of {@linkplain BlockModelDefinition BlockModelDefinitions} to this builder. By default, model datagen is handled based on a series of
         * type checks (E.G. Doors, walls, fences, rotatable blocks, etc.). You can use this method if your custom block requires a
         * different model definition that isn't natively handled.
         *
         * @param blockModelDefinitions The {@link BlockModelDefinition} used to build this BPWBuilder's parent block's model in datagen.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomModelDefinition(BlockModelDefinition)
         * @see #withCustomModelDefinitions(List)
         */
        public BPWBuilder withSetCustomModelDefinitions(List<BlockModelDefinition> blockModelDefinitions) {
            this.blockModelDefinitions.clear();
            this.blockModelDefinitions.addAll(blockModelDefinitions);
            return this;
        }

        /**
         * Defines a custom {@link BlockStateDefinition} mapping function to this builder. By default, blockstate datagen is handled based on a series of
         * type checks (E.G. Doors, walls, fences, rotatable blocks, etc.). You can use this method if your custom block requires a
         * different blockstate definition that isn't natively handled.
         *
         * @param bsdMappingFunction The {@link BlockStateDefinition} mapping function used to build this BPWBuilder's parent block's blockstate in datagen.
         *
         * @return {@code this} (builder method).
         */
        public BPWBuilder withBlockStateDefinition(Function<Supplier<Block>, BlockStateDefinition> bsdMappingFunction) {
            this.blockStateDefinition = bsdMappingFunction;
            return this;
        }

        /**
         * Defines a custom mapping function representing the parent {@linkplain Block Block's} recipe. BPWBuilders accepting more than 1 recipe function assume that each recipe has a unique recipe ID,
         * and thus recipes are generated under that constraint.
         *
         * @param recipeBuilderFunction The mapping function accepting a representation of the parent {@linkplain Block Block's} recipe.
         *
         * @return {@code this} (builder method).
         */
        public BPWBuilder withRecipe(Function<Consumer<FinishedRecipe>, Consumer<Supplier<Block>>> recipeBuilderFunction) {
            this.recipeBuilderFunction = recipeBuilderFunction;
            return this;
        }

        /**
         * Appends a parent {@link CreativeModeTab} for the parent BPW's {@link Block} to show up in.
         *
         * @param parentTab The {@link CreativeModeTab} under which the parent BPW's {@link Block} will be listed/show up.
         *
         * @return {@code this} (builder method).
         */
        public BPWBuilder withParentCreativeModeTab(Supplier<CreativeModeTab> parentTab) {
            this.parentTabs.add(parentTab);
            return this;
        }

        /**
         * Appends a {@link List} of parent {@linkplain CreativeModeTab CreativeModeTabs} for the parent BPW's {@link Block} to show up in.
         *
         * @param parentTabs A {@link List} of {@linkplain CreativeModeTab CreativeModeTabs} under which the parent BPW's {@link Block} will be listed/show up.
         *
         * @return {@code this} (builder method).
         */
        public BPWBuilder withParentCreativeModeTabs(List<Supplier<CreativeModeTab>> parentTabs) {
            this.parentTabs.addAll(parentTabs);
            return this;
        }

        /**
         * Sets (does NOT append) a {@link List} of parent {@linkplain CreativeModeTab CreativeModeTabs} for the parent BPW's {@link Block} to show up in.
         *
         * @param parentTabs A {@link List} of {@linkplain CreativeModeTab CreativeModeTabs} under which the parent BPW's {@link Block} will be listed/show up.
         *
         * @return {@code this} (builder method).
         */
        public BPWBuilder withSetParentCreativeModeTabs(List<Supplier<CreativeModeTab>> parentTabs) {
            this.parentTabs.clear();
            this.parentTabs.addAll(parentTabs);
            return this;
        }

        /**
         * Defines a custom mapping function representing the parent {@linkplain Block Block's} optional {@link BlockColor}.
         *
         * @param blockColorMappingFunc The mapping function accepting a representation of the parent {@linkplain Block Block's} optional {@link BlockColor}.
         *
         * @return {@code this} (builder method).
         */
        public BPWBuilder withBlockColor(Function<Supplier<Block>, BlockColor> blockColorMappingFunc) {
            this.blockColorMappingFunc = blockColorMappingFunc;
            return this;
        }

        /**
         * Builds a new {@link BlockPropertyWrapper} using this builder's data. Also maps the owner
         * {@link BlockPropertyWrapper} to the parent {@linkplain Block} if the owner is not a template.
         *
         * @return The newly data-populated {@link BlockPropertyWrapper}.
         *
         * @see BlockPropertyWrapper#isTemplate()
         */
        public BlockPropertyWrapper build() {
            if (!ownerWrapper.isTemplate) MAPPED_BPWS.putIfAbsent(ownerWrapper.blockRegName == null ? ownerWrapper.parentBlock : () -> BuiltInRegistries.BLOCK.get(CAConstants.prefix(ownerWrapper.blockRegName)), ownerWrapper);
            return ownerWrapper;
        }
    }
}
