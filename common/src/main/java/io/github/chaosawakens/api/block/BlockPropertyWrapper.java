package io.github.chaosawakens.api.block;

import com.google.common.collect.ImmutableSortedMap;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.common.registry.CABlocks;
import io.github.chaosawakens.util.LootUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A wrapper class used to store information referenced in datagen to simplify creating data entries for blocks.
 */
public class BlockPropertyWrapper {
    private static final Object2ObjectLinkedOpenHashMap<Supplier<Block>, BlockPropertyWrapper> MAPPED_BWPS = new Object2ObjectLinkedOpenHashMap<>();
    @Nullable
    private final String blockRegName;
    private final Supplier<Block> parentBlock;
    @Nullable
    private BPWBuilder builder;

    private BlockPropertyWrapper(String blockRegName, Supplier<Block> parentBlock) {
        this.blockRegName = blockRegName;
        this.parentBlock = parentBlock;
    }

    private BlockPropertyWrapper(Supplier<Block> parentBlock) {
        this.blockRegName = null;
        this.parentBlock = parentBlock;
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
     * @see #of(Supplier, Supplier)
     * @see #of(String, Supplier)
     */
    public static BlockPropertyWrapper create(Supplier<Block> parentBlock) {
        return new BlockPropertyWrapper(parentBlock);
    }

    /**
     * Creates a new {@link BlockPropertyWrapper} instance from an existing {@link BlockPropertyWrapper} instance based on the provided
     * {@link Supplier<Block>}. If no such existing BPW instance exists, returns {@link #create(Supplier)}.
     *
     * @param parentBlock The parent {@link Supplier<Block>} stored in {@link #MAPPED_BWPS}. Copies its BPW instance's {@link BPWBuilder}
     *                    properties if it exists, or creates a clean new BPW instance if it doesn't.
     * @param newBlock The new registry entry to use for the newly constructed BPW instance.
     *
     * @return A new {@link BlockPropertyWrapper} instance with copied properties based on the provided {@link Supplier<Block>}, or an entirely new/clean instance if no such BPW exists.
     *
     * @see #create(Supplier)
     * @see #of(String, Supplier)
     */
    public static BlockPropertyWrapper of(Supplier<Block> parentBlock, Supplier<Block> newBlock) {
        if (MAPPED_BWPS.containsKey(parentBlock)) {
            BlockPropertyWrapper originalWrapper = MAPPED_BWPS.get(parentBlock);
            BlockPropertyWrapper newWrapper = new BlockPropertyWrapper(newBlock);

            newWrapper.builder()
                    .withCustomName(originalWrapper.builder.manuallyUnlocalizedBlockName)
                    .withCustomSeparatorWords(originalWrapper.builder.definedSeparatorWords)
                    .withSetTags(originalWrapper.builder.parentTags)
                    .withLootTable(originalWrapper.builder.blockLootTableBuilder)
                    .withSetCustomModelDefinitions(originalWrapper.builder.blockModelDefinitions)
                    .withBlockStateDefinition(originalWrapper.builder.blockStateDefinition)
                    .build(); // Direct setting of the builder would copy the entire object itself, which would in-turn overwrite it if any calls are made to the copied BPW afterward

            MAPPED_BWPS.put(newBlock, newWrapper);

            return newWrapper;
        } else return create(newBlock);
    }

    /**
     * Overloaded variant of {@link #of(Supplier, Supplier)} that copies the parent block's {@link BlockBehaviour.Properties}.
     *
     * @param newBlockRegName The new registry name by which the newly constructed {@link Supplier<Block>} instance will be stored.
     * @param parentBlock The parent {@link Supplier<Block>} stored in {@link #MAPPED_BWPS}.
     *
     * @return A new {@link BlockPropertyWrapper} instance with copied properties (including {@link BlockBehaviour.Properties}) based on the provided {@link Supplier<Block>},
     * or an entirely new/clean instance if no such BPW exists.
     *
     * @see #of(Supplier, Supplier)
     * @see #create(Supplier)
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
     * Gets the cached {@link BPWBuilder} instance from the {@link #builder()} if the builder exists. May be {@code null}. Useful for
     * overriding specific properties after having copied another BPW instance.
     *
     * @return The cached {@link BPWBuilder} instance, or {@code null} if the {@link #builder()} is {@code null}.
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
     * Gets the manually unlocalized block name from the {@link #builder()} if the builder exists.
     *
     * @return The manually unlocalized block name, or an empty {@code String} if the {@link #builder()} is {@code null}.
     */
    public String getManuallyUnlocalizedBlockName() {
        return builder == null ? "" : builder.manuallyUnlocalizedBlockName;
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
     * Gets the defined parent {@linkplain TagKey<Block> Block Tags} from the {@link #builder()} if the builder exists.
     *
     * @return The defined parent {@linkplain TagKey<Block> Block Tags}, or an empty {@link ObjectArrayList} if the {@link #builder()} is {@code null}.
     */
    public List<TagKey<Block>> getParentBlockTags() {
        return builder == null ? ObjectArrayList.of() : builder.parentTags;
    }

    /**
     * Gets the {@link List} of {@linkplain BlockModelDefinition BlockModelDefinitions} from the {@link #builder()} if the builder exists, and it is defined within said builder.
     * May be {@code null}.
     *
     * @return The {@link List} of {@linkplain BlockModelDefinition BlockModelDefinitions}, or an empty {@link ObjectArrayList} if the {@link #builder()} is {@code null}.
     */
    @Nullable
    public List<BlockModelDefinition> getModelDefinitions() {
        return builder == null ? ObjectArrayList.of() : builder.blockModelDefinitions;
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
     * Gets an immutable view (via {@link ImmutableSortedMap}) of {@link #MAPPED_BWPS}.
     *
     * @return An immutable view (via {@link ImmutableSortedMap}) of {@link #MAPPED_BWPS}.
     */
    public static ImmutableSortedMap<Supplier<Block>, BlockPropertyWrapper> getMappedBwps() {
        return ImmutableSortedMap.copyOf(MAPPED_BWPS);
    }

    /**
     * A builder class used to construct certain data for datagen.
     */
    public static class BPWBuilder {
        private final BlockPropertyWrapper ownerWrapper;
        private final Supplier<Block> parentBlock;
        private String manuallyUnlocalizedBlockName = "";
        private List<String> definedSeparatorWords = ObjectArrayList.of();
        @Nullable
        private Function<Supplier<Block>, LootTable.Builder> blockLootTableBuilder;
        private List<TagKey<Block>> parentTags = ObjectArrayList.of();
        private List<BlockModelDefinition> blockModelDefinitions = ObjectArrayList.of();
        @Nullable
        private Function<Supplier<Block>, BlockStateDefinition> blockStateDefinition;

        private BPWBuilder(BlockPropertyWrapper ownerWrapper, Supplier<Block> parentBlock) {
            this.ownerWrapper = ownerWrapper;
            this.parentBlock = parentBlock;
        }

        /**
         * Assigns a custom translation key for datagen. By default, a basic regex algorithm is used to automatically de-localize
         * the block name into something more legible (I.E. The names you see in-game). This property is simply an override
         * mechanic which aims to give the end-developer more control over the resulting name instead of being forced to rely on
         * the aforementioned algorithm.
         * <p></p>
         * The algorithm in question, in a nutshell, works as follows (the code block below is purely demonstrative of the
         * de-localization process and has nothing to do with how the algorithm is actually written):
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
         * <b>NOTE:</b> Block registry names ending with "_block" (e.g. "block.chaosawakens.royal_guardian_scale_block") have the "_block" part removed automatically and the result string is prepended with "Block of"
         * during the translation process (the registry name stays the same, of course). You may use this method to bypass that step if needed.
         *
         * @param manuallyUnlocalizedBlockName The name override used to de-localize the parent {@linkplain Block Block's} registry name.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomSeparatorWords(List)
         */
        public BPWBuilder withCustomName(String manuallyUnlocalizedBlockName) {
            this.manuallyUnlocalizedBlockName = manuallyUnlocalizedBlockName;
            return this;
        }

        /**
         * Assigns a {@link List} of custom separator words which are lowercased during the algorithm's de-localization process. This is ignored if {@link #manuallyUnlocalizedBlockName} is defined.
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
         * Tags this BPWBuilder's parent block with the provided {@link TagKey<Block>}.
         *
         * @param parentBlockTag The {@link TagKey<Block>} with which this BPW's parent block will be tagged.
         *
         * @return {@code this} (builder method).
         */
        public BPWBuilder withTag(TagKey<Block> parentBlockTag) {
            this.parentTags.add(parentBlockTag);
            return this;
        }

        /**
         * Tags this BPWBuilder's parent block with the provided {@linkplain TagKey<Block> Block Tags}. Appends to the existing list.
         *
         * @param parentBlockTags The {@linkplain TagKey<Block> TagKeys} with which this BPW's parent block will be tagged.
         *
         * @return {@code this} (builder method).
         *
         * @see #withSetTags(List)
         */
        public BPWBuilder withTags(List<TagKey<Block>> parentBlockTags) {
            this.parentTags.addAll(parentBlockTags);
            return this;
        }

        /**
         * Tags this BPWBuilder's parent block with the provided {@linkplain TagKey<Block> Block Tags}. Overwrites the existing list.
         *
         * @param parentBlockTags The {@linkplain TagKey<Block> TagKeys} with which this BPW's parent block will be tagged.
         *
         * @return {@code this} (builder method).
         *
         * @see #withTags(List)
         */
        public BPWBuilder withSetTags(List<TagKey<Block>> parentBlockTags) {
            this.parentTags = parentBlockTags;
            return this;
        }

        /**
         * Appends a custom {@link BlockModelDefinition} to this builder. By default, model datagen is handled based on a series of
         * type checks (E.G. Doors, walls, fences, rotatable blocks, etc.). You can use this method if your custom block requires a
         * different model definition that isn't natively handled.
         *
         * @param blockStateDefinition The {@link BlockModelDefinition} used to build this BPWBuilder's parent block's model(s) in datagen.
         *
         * @return {@code this} (builder method).
         *
         * @see #withCustomModelDefinitions(List)
         */
        public BPWBuilder withCustomModelDefinition(BlockModelDefinition blockStateDefinition) {
            this.blockModelDefinitions.add(blockStateDefinition);
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
            this.blockModelDefinitions = blockModelDefinitions;
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
        public BPWBuilder withBlockStateDefinition(Function<Supplier<Block>, BlockStateDefinition> bsdMappingFunction) { //TODO Function-ify
            this.blockStateDefinition = bsdMappingFunction;
            return this;
        }

        /**
         * Builds a new {@link BlockPropertyWrapper} using this builder's data. Also maps the owner
         * {@link BlockPropertyWrapper} to the parent {@linkplain Block}.
         *
         * @return The newly data-populated {@link BlockPropertyWrapper}.
         */
        public BlockPropertyWrapper build() {
            MAPPED_BWPS.putIfAbsent(ownerWrapper.blockRegName == null ? ownerWrapper.parentBlock : () -> BuiltInRegistries.BLOCK.get(CAConstants.prefix(ownerWrapper.blockRegName)), ownerWrapper);
            return ownerWrapper;
        }
    }
}
