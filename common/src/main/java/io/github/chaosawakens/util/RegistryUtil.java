package io.github.chaosawakens.util;

import com.google.common.base.Suppliers;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.common.registry.CABlocks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class providing shortcut methods for commonly used registry operations (primarily arbitrary).
 */
public final class RegistryUtil {
    private static final ObjectArrayList<ResourceLocation> CACHED_PNG_TEXTURES = new ObjectArrayList<>(); // Cache datagen found textures instead of creating a new list for each iteration/method call

    private RegistryUtil() {
        throw new IllegalAccessError("Attempted to construct Utility Class!");
    }

    /**
     * Retrieves the registry name of the provided {@link ItemLike} via {@link DefaultedRegistry#getKey(Object)} (from the {@link BuiltInRegistries#ITEM} registry).
     *
     * @param targetItemLike The {@link ItemLike} to retrieve the registry name for.
     *
     * @return The registry name of the provided {@link ItemLike}.
     *
     * @see #getItemModId(ItemLike)
     * @see #getItemKey(ItemLike)
     */
    public static String getItemName(ItemLike targetItemLike) {
        return BuiltInRegistries.ITEM.getKey(targetItemLike.asItem()).getPath();
    }

    /**
     * Retrieves the modid of the provided {@link ItemLike} via {@link DefaultedRegistry#getKey(Object)} (from the {@link BuiltInRegistries#ITEM} registry).
     *
     * @param targetItemLike The {@link ItemLike} to retrieve the registry name for.
     *
     * @return The origin modid of the provided {@link ItemLike}.
     *
     * @see #getItemName(ItemLike)
     * @see #getItemKey(ItemLike)
     */
    public static String getItemModId(ItemLike targetItemLike) {
        return BuiltInRegistries.ITEM.getKey(targetItemLike.asItem()).getNamespace();
    }

    /**
     * Retrieves and returns a {@link ResourceLocation} representing the full registry key of the given {@link ItemLike}.
     *
     * @param targetItemLike The {@link ItemLike} to retrieve the registry key for.
     *
     * @return A {@link ResourceLocation} representing the full key of the target {@link ItemLike} (may be an air delegate ({"minecraft:air"}) if no such key exists in accordance to the methods this one overloads).
     *
     * @see #getItemModId(ItemLike)
     * @see #getItemName(ItemLike)
     */
    public static ResourceLocation getItemKey(ItemLike targetItemLike) {
        return new ResourceLocation(getItemModId(targetItemLike), getItemName(targetItemLike));
    }

    /**
     * Attempts to populate {@link #CACHED_PNG_TEXTURES} using the {@code java.class.path} system property.
     *
     * @param baseModId The modid under which actual file-system PNGs (I.E. Basically the textures in your workspace's {@code "/resources"} src directory) should be converted and added.
     */
    public static void populateCachedPNGTextures(String baseModId) {
        if (!CACHED_PNG_TEXTURES.isEmpty()) return;

        String classpath = System.getProperty("java.class.path");

        for (String curClassPath : classpath.split(File.pathSeparator)) {
            File curFile = new File(curClassPath);

            if (curFile.isDirectory()) {
                try (Stream<Path> allExistingPaths = Files.walk(curFile.toPath(), FileVisitOption.FOLLOW_LINKS)) { // Literal files
                    allExistingPaths.filter(Files::isRegularFile)
                            .filter(curPath -> curPath.toString().endsWith(".png"))
                            .forEach(curVerifiedPath -> {
                                File curVerifiedFile = curVerifiedPath.toFile();

                                if (curVerifiedFile.getPath().contains("textures\\item")) {
                                    String name = curVerifiedFile.getPath().replace("\\", "/");
                                    name = name.substring(name.indexOf("item") + "item/".length(), name.indexOf(".png"));

                                    CACHED_PNG_TEXTURES.add(new ResourceLocation(baseModId, name));
                                } else if (curVerifiedFile.getPath().contains("textures\\block")) {
                                    String name = curVerifiedFile.getPath().replace("\\", "/");
                                    name = name.substring(name.indexOf("block") + "block/".length(), name.indexOf(".png"));

                                    CACHED_PNG_TEXTURES.add(new ResourceLocation(baseModId, name));
                                }
                            });
                } catch (IOException e) {
                    CAConstants.LOGGER.error("Failed to walk system classpath. Ensure that you're in your IDE and calling this method in datagen-related code, as it is otherwise not designed to work with non-literal/non-regular directories/files.", e);
                }
            } else {
                try (JarFile curJarFile = new JarFile(curFile)) { // Files nested within JARs
                    curJarFile.stream()
                            .filter(curJarEntry -> curJarEntry.getName().endsWith(".png"))
                            .forEach(curVerifiedJarEntry -> {
                                String entryPath = curVerifiedJarEntry.getName();

                                if (entryPath.contains("/textures/block/") || entryPath.contains("/textures/item/")) {
                                    String[] pathParts = entryPath.split("/");

                                    if (pathParts.length >= 4) {
                                        String modId = pathParts[1];
                                        String relativePath = String.join("/", Arrays.copyOfRange(pathParts, 3, pathParts.length));

                                        CACHED_PNG_TEXTURES.add(new ResourceLocation(modId, relativePath.replace(".png", "")));
                                    }
                                }
                            });
                } catch (IOException e) {
                    CAConstants.LOGGER.error("Failed to walk system classpath. Ensure that you're in your IDE and calling this method in datagen-related code, as it is otherwise not designed to work with non-literal/non-regular directories/files.", e);
                }
            }
        }
    }

    /**
     * Attempts to retrieve the texture for the provided filename. <b>IDE/DEV-ENV ONLY!!!</b> This will not work outside of datagen in the dev environment, as it's only meant to be used to generate data
     * based on located textures within the {@code "build/resources"} directory from the Java Classpath.
     * <p></p>
     * Mind that the registry name of the object attempting to utilise this method must EXACTLY match the name of the target texture to locate. This method (and its equivalents) is meant
     * to substitute for a lack of {@link Minecraft} in datagen, which by extension means a lack of texture management and whatnot.
     *
     * @param modid The modid under which the texture file should be validated against and located.
     * @param targetBlockFileName The {@link String} for which a PNG file with a matching name should be located.
     *
     * @return A {@link ResourceLocation}, formatted and leading to the location of the target object's texture path, or {@code null} if none could be found.
     *
     * @see #getBlockTexture(Supplier)
     * @see #getItemTexture(Supplier)
     */
    @Nullable
    public static ResourceLocation getTexture(String modid, String targetBlockFileName) {
        populateCachedPNGTextures(modid);

        return CACHED_PNG_TEXTURES.stream().filter(curRL -> !modid.isBlank() && curRL.getPath().endsWith(targetBlockFileName) && curRL.getNamespace().equals(modid)).findFirst().orElse(null);
    }

    /**
     * Overloaded variant of {@link #getTexture(String, String)}. Attempts to retrieve the texture for the supplied {@link Item}. <b>IDE/DEV-ENV ONLY!!!</b> This will not work outside of datagen
     * in the dev environment, as it's only meant to be used to generate data based on located textures within the {@code "build/resources"} directory from the Java Classpath.
     * <p></p>
     * Mind that the registry name of the object attempting to utilise this method must EXACTLY match
     * the name of the target texture to locate. This method (and its equivalents) is meant to substitute for a lack of {@link Minecraft} in datagen, which by extension means a lack of texture management
     * and whatnot.
     *
     * @param targetItem The {@link Item} for which a PNG file with a matching name should be located.
     *
     * @return A {@link ResourceLocation}, formatted and leading to the location of the target {@linkplain Item Item's} texture path, or {@code null} if none could be found.
     *
     * @see #getTexture(String, String)
     * @see #getBlockTexture(Supplier)
     */
    @Nullable
    public static ResourceLocation getItemTexture(Supplier<Item> targetItem) {
        return getTexture(BuiltInRegistries.ITEM.getKey(targetItem.get()).getNamespace(), BuiltInRegistries.ITEM.getKey(targetItem.get()).getPath());
    }

    /**
     * Overloaded variant of {@link #getTexture(String, String)}}. Attempts to retrieve the texture for the supplied {@link Block}. <b>IDE/DEV-ENV ONLY!!!</b> This will not work outside of datagen
     * in the dev environment, as it's only meant to be used to generate data based on located textures within the {@code "build/resources"} directory from the Java Classpath.
     * <p></p>
     * Mind that the registry name of the object attempting to utilise this method must EXACTLY match
     * the name of the target texture to locate. This method (and its equivalents) is meant to substitute for a lack of {@link Minecraft} in datagen, which by extension means a lack of texture management
     * and whatnot.
     *
     * @param targetBlock The {@link Block} for which a PNG file with a matching name should be located.
     *
     * @return A {@link ResourceLocation}, formatted and leading to the location of the target {@linkplain Block Block's} texture path, or {@code null} if none could be found.
     *
     * @see #getTexture(String, String)
     * @see #getItemTexture(Supplier)
     */
    @Nullable
    public static ResourceLocation getBlockTexture(Supplier<Block> targetBlock) {
        return getTexture(BuiltInRegistries.BLOCK.getKey(targetBlock.get()).getNamespace(), BuiltInRegistries.BLOCK.getKey(targetBlock.get()).getPath());
    }

    /**
     * Modifies the {@link ResourceLocation} passed in by prepending the provided {@code prefix} to its path if it isn't already... prefixed with said {@code prefix} (duh).
     *
     * @param baseLoc The {@link ResourceLocation} to pick the provided {@code prefix} for.
     * @param prefix The path prefix to search for/prepend the provided {@code baseLoc} with.
     *
     * @return A modified variant of the provided {@code baseLoc} with the provided {@code prefix} picked/appropriately and safely prepended.
     *
     * @see #pickBlockPrefix(ResourceLocation)
     * @see #pickItemPrefix(ResourceLocation)
     */
    public static ResourceLocation pickPrefix(ResourceLocation baseLoc, String prefix) {
        return baseLoc.getPath().startsWith(prefix) ? baseLoc : baseLoc.withPrefix(prefix);
    }

    /**
     * Overloaded variant of {@link #pickPrefix(ResourceLocation, String)}. Modifies the {@link ResourceLocation} passed in by prepending the {@code "block/"} prefix to its path if it isn't already prefixed
     * with said prefix (duh).
     *
     * @param baseBlockLoc The {@link ResourceLocation} to pick the {@code "block/"} prefix for.
     *
     * @return A modified variant of the provided {@code baseBlockLoc} with the {@code "block/"} prefix picked/appropriately and safely prepended.
     *
     * @see #pickPrefix(ResourceLocation, String)
     * @see #pickItemPrefix(ResourceLocation)
     */
    public static ResourceLocation pickBlockPrefix(ResourceLocation baseBlockLoc) {
        return pickPrefix(baseBlockLoc, "block/");
    }

    /**
     * Overloaded variant of {@link #pickPrefix(ResourceLocation, String)}. Modifies the {@link ResourceLocation} passed in by prepending the {@code "item/"} prefix to its path if it isn't already prefixed
     * with said prefix (duh).
     *
     * @param baseItemLoc The {@link ResourceLocation} to pick the {@code "item/"} prefix for.
     *
     * @return A modified variant of the provided {@code baseItemLoc} with the {@code "item/"} prefix picked/appropriately and safely prepended.
     *
     * @see #pickPrefix(ResourceLocation, String)
     * @see #pickBlockPrefix(ResourceLocation)
     */
    public static ResourceLocation pickItemPrefix(ResourceLocation baseItemLoc) {
        return pickPrefix(baseItemLoc, "item/");
    }

    @Nullable
    public static Supplier<Block> getFromWood(Supplier<Block> targetBlock, String regNameSuffix) {
        ResourceLocation targetBlockKey = getItemKey(targetBlock.get());
        String copiedPath = getItemName(targetBlock.get());

        return targetBlockKey.getPath().startsWith("stripped")
                ? targetBlockKey.getPath().endsWith("wood") && targetBlockKey.getPath().charAt(targetBlockKey.getPath().indexOf("wood") - 1) != '_'
                ? () -> BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(copiedPath.concat(regNameSuffix)))
                : () -> BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(StringUtils.substring(copiedPath, 0, copiedPath.lastIndexOf("_")).concat(regNameSuffix)))
                : targetBlockKey.getPath().endsWith("_wood")
                ? () -> BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(StringUtils.substring(copiedPath, 0, copiedPath.lastIndexOf("_")).concat(regNameSuffix)))
                : targetBlockKey.getPath().endsWith("wood") && targetBlockKey.getPath().charAt(targetBlockKey.getPath().indexOf("wood") - 1) != '_'
                ? () -> BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(copiedPath.concat(regNameSuffix)))
                : targetBlockKey.getPath().contains("_")
                ? () -> BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(StringUtils.substringBefore(copiedPath, "_").concat(regNameSuffix)))
                : null; // Force nullity rather than air delegate
    }

    @Nullable
    public static Supplier<Item> getBlockAsItemSup(Supplier<Block> targetBlock) {
        return CABlocks.getBlockItems().get(CABlocks.getBlocks().indexOf(targetBlock)); // TOP 10 UNSAFE PRACTICES: NUMBER 10: WHY
    }

    public static List<Supplier<Item>> getBlocksAsItemSups(List<Supplier<Block>> targetBlocks) {
        return targetBlocks.stream()
                .filter(curBlockSup -> CABlocks.getBlocks().contains(curBlockSup) && CABlocks.getBlockItems().get(CABlocks.getBlocks().indexOf(curBlockSup)) != null)
                .map(RegistryUtil::getBlockAsItemSup)
                .collect(Collectors.toCollection(ObjectArrayList::new));
    }

    @SafeVarargs
    public static List<Supplier<Item>> getBlocksAsItemSups(Supplier<Block>... targetBlocks) {
        return targetBlocks == null || targetBlocks.length == 0 ? ObjectArrayList.of() : getBlocksAsItemSups(ObjectArrayList.of(targetBlocks));
    }

    @Nullable
    public static Supplier<Block> getPlanksFrom(Supplier<Block> targetBlock) {
        return getFromWood(targetBlock, "_planks");
    }

    @Nullable
    public static Supplier<Block> getLogFrom(Supplier<Block> targetBlock) {
        return getFromWood(targetBlock, "_log");
    }

    @Nullable
    public static Supplier<Block> getLeavesFrom(Supplier<Block> targetBlock) {
        ResourceLocation targetBlockKey = getItemKey(targetBlock.get());
        String copiedPath = getItemName(targetBlock.get());

        return targetBlockKey.getPath().endsWith("leaf_carpet")
                && !BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(copiedPath.replace("leaf_carpet", "leaves"))).getDescriptionId().equals("block.minecraft.air")
                ? () -> BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(copiedPath.replace("leaf_carpet", "leaves")))
                : !BuiltInRegistries.BLOCK.get(new ResourceLocation(copiedPath.replace("leaf_carpet", "leaves"))).getDescriptionId().equals("block.minecraft.air")
                ? () -> BuiltInRegistries.BLOCK.get(new ResourceLocation(copiedPath.replace("leaf_carpet", "leaves")))
                : null;
    }

    @Nullable
    public static Supplier<Block> getFromLeaves(Supplier<Block> targetBlock, String regNameSuffix) {
        ResourceLocation targetBlockKey = getItemKey(targetBlock.get());
        String copiedPath = getItemName(targetBlock.get());

        return targetBlockKey.getPath().endsWith("leaves")
                ? () -> BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(copiedPath.replace("leaves", regNameSuffix)))
                : null;
    }

    @Nullable
    public static Supplier<Block> getLeafCarpetFrom(Supplier<Block> targetBlock) {
        return getFromLeaves(targetBlock, "leaf_carpet");
    }

    @Nullable
    public static Supplier<Block> getSaplingFrom(Supplier<Block> targetBlock) {
        return getFromLeaves(targetBlock, "sapling");
    }

    @Nullable
    public static Supplier<Item> getCookedFoodFrom(Supplier<Item> targetItem) {
        ResourceLocation targetItemKey = getItemKey(targetItem.get());
        String copiedPath = getItemName(targetItem.get());

        return !targetItemKey.getPath().startsWith("raw")
                ? () -> BuiltInRegistries.ITEM.get(targetItemKey.withPrefix("cooked_"))
                : copiedPath.startsWith("raw")
                ? () -> BuiltInRegistries.ITEM.get(targetItemKey.withPath(copiedPath.replace("raw_", "cooked_")))
                : null;
    }

    @Nullable
    public static Supplier<Item> getRawFoodFromCookedFood(Supplier<Item> targetItem) {
        ResourceLocation targetItemKey = getItemKey(targetItem.get());
        String copiedPath = getItemName(targetItem.get());
        Supplier<Item> assumedCookedFood = () -> BuiltInRegistries.ITEM.get(targetItemKey.withPath(StringUtils.substringAfter(copiedPath, "cooked_")));

        return targetItemKey.getPath().startsWith("cooked_")
                ? !BuiltInRegistries.ITEM.get(targetItemKey.withPath(copiedPath.replace("cooked_", "raw_"))).getDescriptionId().equals("item.minecraft.air")
                ? () -> BuiltInRegistries.ITEM.get(targetItemKey.withPath(copiedPath.replace("cooked_", "raw_")))
                : !assumedCookedFood.get().getDescriptionId().equals("item.minecraft.air")
                ? assumedCookedFood
                : null
                : null;
    }

    @Nullable
    public static Supplier<Block> getFromSolidBlock(Supplier<Block> targetBlock, String regNameSuffix) {
        ResourceLocation targetBlockKey = getItemKey(targetBlock.get());
        String copiedPath = getItemName(targetBlock.get());

        if (copiedPath.contains("bricks")) copiedPath = copiedPath.replace("bricks", "brick");

        String finalCopiedPath = copiedPath; // Needed cuz WE LOVE TEROP!!!

        return targetBlockKey.getPath().endsWith("_block")
                ? () -> BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(finalCopiedPath.replace("_block", regNameSuffix)))
                : !BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(finalCopiedPath.concat(regNameSuffix))).getDescriptionId().equals("block.minecraft.air")
                ? () -> BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(finalCopiedPath.concat(regNameSuffix)))
                : null;
    }

    @Nullable
    public static Supplier<Block> getSlabFromSolidBlock(Supplier<Block> targetBlock) {
        return getFromSolidBlock(targetBlock, "_slab");
    }

    @Nullable
    public static Supplier<Block> getStairsFromSolidBlock(Supplier<Block> targetBlock) {
        return getFromSolidBlock(targetBlock, "_stairs");
    }

    @Nullable
    public static Supplier<Block> getPillarFromSolidBlock(Supplier<Block> targetBlock) {
        return getFromSolidBlock(targetBlock, "_pillar");
    }

    @Nullable
    public static Supplier<Block> getWallFromSolidBlock(Supplier<Block> targetBlock) {
        return getFromSolidBlock(targetBlock, "_wall");
    }

    @Nullable
    public static Supplier<Block> getSolidBlockFrom(Supplier<Block> targetBlock, String targetRegNameSuffix) {
        ResourceLocation targetBlockKey = getItemKey(targetBlock.get());
        String copiedPath = getItemName(targetBlock.get());

        if (copiedPath.contains("brick")) copiedPath = copiedPath.replace("brick", "bricks");

        String finalCopiedPath = copiedPath; // Needed cuz WE LOVE TEROP!!!

        return targetBlockKey.getPath().endsWith(targetRegNameSuffix)
                ? !BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(finalCopiedPath.replace(targetRegNameSuffix, "_block"))).getDescriptionId().equals("block.minecraft.air")
                ? () -> BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(finalCopiedPath.replace(targetRegNameSuffix, "_block")))
                : !BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(finalCopiedPath.replace(targetRegNameSuffix, ""))).getDescriptionId().equals("block.minecraft.air")
                ? () -> BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(finalCopiedPath.replace(targetRegNameSuffix, "")))
                : null
                : null;
    }

    @Nullable
    public static Supplier<Block> getSolidBlockFromSlab(Supplier<Block> targetBlock) {
        return getSolidBlockFrom(targetBlock, "_slab");
    }

    @Nullable
    public static Supplier<Block> getSolidBlockFromStairs(Supplier<Block> targetBlock) {
        return getSolidBlockFrom(targetBlock, "_stairs");
    }

    @Nullable
    public static Supplier<Block> getSolidBlockFromPillar(Supplier<Block> targetBlock) {
        return getSolidBlockFrom(targetBlock, "_pillar");
    }

    @Nullable
    public static Supplier<Block> getSolidBlockFromWall(Supplier<Block> targetBlock) {
        return getSolidBlockFrom(targetBlock, "_wall");
    }

    @Nullable
    public static Supplier<Block> getBlockBasedOnSuffix(Supplier<Block> targetBlock, String targetRegNameSuffix, String suffixReplacement) {
        ResourceLocation targetBlockKey = getItemKey(targetBlock.get());
        String copiedBlockPath = getItemName(targetBlock.get());

        return targetBlockKey.getPath().endsWith(targetRegNameSuffix)
                && !BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(copiedBlockPath.replace(targetRegNameSuffix, suffixReplacement))).getDescriptionId().equals("block.minecraft.air")
                ? () -> BuiltInRegistries.BLOCK.get(targetBlockKey.withPath(copiedBlockPath.replace(targetRegNameSuffix, suffixReplacement)))
                : null;
    }

    @Nullable
    public static Supplier<Item> getItemBasedOnSuffix(Supplier<Item> targetItem, String targetRegNameSuffix, String suffixReplacement) {
        ResourceLocation targetItemKey = getItemKey(targetItem.get());
        String copiedItemPath = getItemName(targetItem.get());
        String targetPath = targetRegNameSuffix.isBlank() ? copiedItemPath.concat(suffixReplacement) : copiedItemPath.replace(targetRegNameSuffix, suffixReplacement);

        return targetItemKey.getPath().endsWith(targetRegNameSuffix)
                && !BuiltInRegistries.ITEM.get(targetItemKey.withPath(targetPath)).getDescriptionId().equals("item.minecraft.air")
                && !BuiltInRegistries.ITEM.get(targetItemKey.withPath(targetPath)).getDescriptionId().equals("block.minecraft.air")
                ? () -> BuiltInRegistries.ITEM.get(targetItemKey.withPath(targetPath))
                : null;
    }

    @Nullable
    public static Supplier<Item> getItemBasedOnPrefixBySuffix(Supplier<Item> targetItem, String targetRegNameSuffix, String addedPrefix, String suffixReplacement) {
        ResourceLocation targetItemKey = getItemKey(targetItem.get());
        String copiedItemPath = getItemName(targetItem.get());
        String targetPath = targetRegNameSuffix.isBlank() ? copiedItemPath.concat(suffixReplacement) : copiedItemPath.replace(targetRegNameSuffix, suffixReplacement);

        return targetItemKey.getPath().endsWith(targetRegNameSuffix)
                && !BuiltInRegistries.ITEM.get(targetItemKey.withPath(targetPath).withPrefix(addedPrefix)).getDescriptionId().equals("item.minecraft.air")
                && !BuiltInRegistries.ITEM.get(targetItemKey.withPath(targetPath)).getDescriptionId().equals("block.minecraft.air")
                ? () -> BuiltInRegistries.ITEM.get(targetItemKey.withPath(targetPath).withPrefix(addedPrefix))
                : null;
    }

    @Nullable
    public static Supplier<Item> getIngotFrom(Supplier<Item> targetItem, String targetRegNameSuffix) {
        return getItemBasedOnSuffix(targetItem, targetRegNameSuffix, "_ingot");
    }

    @Nullable
    public static Supplier<Item> getLumpFrom(Supplier<Item> targetItem, String targetRegNameSuffix) {
        return getItemBasedOnSuffix(targetItem, targetRegNameSuffix, "_lump");
    }

    @Nullable
    public static Supplier<Item> getNuggetFrom(Supplier<Item> targetItem, String targetRegNameSuffix) {
        return getItemBasedOnSuffix(targetItem, targetRegNameSuffix, "_nugget");
    }

    @Nullable
    public static Supplier<Item> getMaterialBlockFrom(Supplier<Item> targetItem, String targetRegNameSuffix) {
        return getItemBasedOnSuffix(targetItem, targetRegNameSuffix, "_block");
    }

    @Nullable
    public static Supplier<Item> getOreFrom(Supplier<Item> targetItem, String targetRegNameSuffix) {
        return getItemBasedOnSuffix(targetItem, targetRegNameSuffix, "_ore");
    }

    @Nullable
    public static Supplier<Item> getDeepslateOreFrom(Supplier<Item> targetItem, String targetRegNameSuffix) {
        return getItemBasedOnPrefixBySuffix(targetItem, targetRegNameSuffix, "deepslate_", "_ore");
    }

    @Nullable
    public static Supplier<Item> getDeepslateOreFromIngot(Supplier<Item> targetItem) {
        return getItemBasedOnPrefixBySuffix(targetItem, "_ingot", "deepslate_", "_ore");
    }

    @Nullable
    public static Supplier<Item> getOreFromIngot(Supplier<Item> targetItem) {
        return getOreFrom(targetItem, "_ingot");
    }

    public static ObjectArrayList<Supplier<Block>> getBlocksFrom(Supplier<Item> targetItem, String targetBlockRegNameSuffix, String targetItemAbrogatedRegNameSuffix) {
        return BuiltInRegistries.BLOCK.stream()
                .filter(curBlock -> getItemName(curBlock).endsWith(targetBlockRegNameSuffix) && getItemName(curBlock).contains(getItemName(targetItem.get()).replace(targetItemAbrogatedRegNameSuffix, "")))
                .map(Suppliers::ofInstance)
                .collect(Collectors.toCollection(ObjectArrayList::new));
    }

    public static ObjectArrayList<Supplier<Block>> getOresFrom(Supplier<Item> targetItem, String targetAbrogatedRegNameSuffix) {
        return getBlocksFrom(targetItem, "_ore", targetAbrogatedRegNameSuffix);
    }

    public static ObjectArrayList<Supplier<Block>> getOresFromIngot(Supplier<Item> targetItem) {
        return getOresFrom(targetItem, "_ingot");
    }

    public static ObjectArrayList<Supplier<Block>> getOresFromLump(Supplier<Item> targetItem) {
        return getOresFrom(targetItem, "_lump");
    }

    @Nullable
    public static Supplier<Item> getOreFromNugget(Supplier<Item> targetItem) {
        return getOreFrom(targetItem, "_nugget");
    }

    @Nullable
    public static Supplier<Item> getIngotFromNugget(Supplier<Item> targetItem) {
        return getIngotFrom(targetItem, "_nugget");
    }

    @Nullable
    public static Supplier<Item> getIngotFromOre(Supplier<Item> targetItem) {
        return getIngotFrom(targetItem, "_ore");
    }

    @Nullable
    public static Supplier<Item> getIngotFromMaterialBlock(Supplier<Item> targetItem) {
        return getIngotFrom(targetItem, "_block");
    }

    @Nullable
    public static Supplier<Item> getLumpFromMaterialBlock(Supplier<Item> targetItem) {
        return getLumpFrom(targetItem, "_block");
    }

    @Nullable
    public static Supplier<Item> getMaterialFromMaterialBlock(Supplier<Item> targetItem) {
        return getIngotFromMaterialBlock(targetItem) == null
                ? getItemBasedOnSuffix(targetItem, "_block", "") != null
                ? getItemBasedOnSuffix(targetItem, "_block", "")
                : getLumpFromMaterialBlock(targetItem)
                : getIngotFromMaterialBlock(targetItem);
    }

    @Nullable
    public static Supplier<Item> getNuggetFromIngot(Supplier<Item> targetItem) {
        return getNuggetFrom(targetItem, "_ingot");
    }

    @Nullable
    public static Supplier<Item> getNuggetFromOre(Supplier<Item> targetItem) {
        return getNuggetFrom(targetItem, "_ore");
    }

    @Nullable
    public static Supplier<Item> getNuggetFromMaterialBlock(Supplier<Item> targetItem) {
        return getNuggetFrom(targetItem, "_block");
    }

    @Nullable
    public static Supplier<Item> getMaterialBlockFromIngot(Supplier<Item> targetItem) {
        return getMaterialBlockFrom(targetItem, "_ingot");
    }

    @Nullable
    public static Supplier<Item> getMaterialBlockFromOre(Supplier<Item> targetItem) {
        return getMaterialBlockFrom(targetItem, "_ore");
    }

    @Nullable
    public static Supplier<Item> getMaterialBlockFromNugget(Supplier<Item> targetItem) {
        return getMaterialBlockFrom(targetItem, "_nugget");
    }

    @Nullable
    public static BlockColor getVanillaLeafColorFor(Supplier<Block> targetBlock) {
        return getItemModId(getLeavesFrom(targetBlock).get()).equals("minecraft") && !getItemName(targetBlock.get()).startsWith("cherry") ? (targetState, tintGetter, targetPos, tint) -> {
            String targetBlockItemName = getItemName(getLeavesFrom(targetBlock).get());

            boolean isBirch = targetBlockItemName.startsWith("birch");
            boolean isMangrove = targetBlockItemName.startsWith("mangrove");
            boolean isSpruce = targetBlockItemName.startsWith("spruce");

            int birchColor = FoliageColor.getBirchColor();
            int defaultFoliageColor = tintGetter != null && targetPos != null ? BiomeColors.getAverageFoliageColor(tintGetter, targetPos) : FoliageColor.getDefaultColor();
            int mangroveColor = FoliageColor.getMangroveColor();
            int spruceColor = FoliageColor.getEvergreenColor();

            return isBirch ? birchColor
                    : isMangrove ? mangroveColor
                    : isSpruce ? spruceColor
                    : defaultFoliageColor;
        } : null;
    }
}
