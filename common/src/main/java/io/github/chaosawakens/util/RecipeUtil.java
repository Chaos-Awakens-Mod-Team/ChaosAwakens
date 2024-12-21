package io.github.chaosawakens.util;

import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Utility class containing helper methods (generally also found in datagen classes) aimed at reducing boilerplate code by providing
 * common/redundant {@link RecipeBuilder} patterns. Partially arbitrary.
 */
public final class RecipeUtil {

    private RecipeUtil() {
        throw new IllegalAccessError("Attempted to construct Utility Class!");
    }

    public static Consumer<Supplier<Block>> materialToBlock(Consumer<FinishedRecipe> recipeConsumer, ItemLike materialItem) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get())
                .define('M', materialItem)
                .pattern("MMM")
                .pattern("MMM")
                .pattern("MMM")
                .unlockedBy("has_" + RegistryUtil.getItemName(materialItem), PredicateUtil.has(materialItem))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultBlockSup.get()), RegistryUtil.getItemName(materialItem) + "_to_" + RegistryUtil.getItemName(resultBlockSup.get())));
    }

    public static Consumer<Supplier<Block>> materialToBlock(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> materialToBlock(recipeConsumer, RegistryUtil.getMaterialFromMaterialBlock(resultBlockSup.get()::asItem).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> logToPlanks(Consumer<FinishedRecipe> recipeConsumer, ItemLike logILReference) {
        return (resultBlockSup) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 4)
                .requires(logILReference)
                .unlockedBy("has_" + RegistryUtil.getItemName(logILReference), PredicateUtil.has(logILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> logToPlanks(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> logToPlanks(recipeConsumer, RegistryUtil.getLogFrom(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> logToWood(Consumer<FinishedRecipe> recipeConsumer, ItemLike logILReference) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 3)
                .define('L', logILReference)
                .pattern("LL")
                .pattern("LL")
                .unlockedBy("has_" + RegistryUtil.getItemName(logILReference), PredicateUtil.has(logILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> logToWood(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> logToWood(recipeConsumer, RegistryUtil.getLogFrom(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> stairsFromPlanks(Consumer<FinishedRecipe> recipeConsumer, ItemLike planksILReference) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 4)
                .define('P', planksILReference)
                .pattern("P  ")
                .pattern("PP ")
                .pattern("PPP")
                .unlockedBy("has_" + RegistryUtil.getItemName(planksILReference), PredicateUtil.has(planksILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> stairsFromPlanks(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> stairsFromPlanks(recipeConsumer, RegistryUtil.getPlanksFrom(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> doorsFromPlanks(Consumer<FinishedRecipe> recipeConsumer, ItemLike planksILReference) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 3)
                .define('P', planksILReference)
                .pattern("PP")
                .pattern("PP")
                .pattern("PP")
                .unlockedBy("has_" + RegistryUtil.getItemName(planksILReference), PredicateUtil.has(planksILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> doorsFromPlanks(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> doorsFromPlanks(recipeConsumer, RegistryUtil.getPlanksFrom(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> slabsFromPlanks(Consumer<FinishedRecipe> recipeConsumer, ItemLike planksILReference) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 6)
                .define('P', planksILReference)
                .pattern("PPP")
                .unlockedBy("has_" + RegistryUtil.getItemName(planksILReference), PredicateUtil.has(planksILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> slabsFromPlanks(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> slabsFromPlanks(recipeConsumer, RegistryUtil.getPlanksFrom(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> buttonFromPlanks(Consumer<FinishedRecipe> recipeConsumer, ItemLike planksILReference) {
        return (resultBlockSup) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 1)
                .requires(planksILReference,1)
                .unlockedBy("has_" + RegistryUtil.getItemName(planksILReference), PredicateUtil.has(planksILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> buttonFromPlanks(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> buttonFromPlanks(recipeConsumer, RegistryUtil.getPlanksFrom(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> trapdoorsFromPlanks(Consumer<FinishedRecipe> recipeConsumer, ItemLike planksILReference) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 2)
                .define('P', planksILReference)
                .pattern("PPP")
                .pattern("PPP")
                .unlockedBy("has_" + RegistryUtil.getItemName(planksILReference), PredicateUtil.has(planksILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> trapdoorsFromPlanks(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> trapdoorsFromPlanks(recipeConsumer, RegistryUtil.getPlanksFrom(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> pressurePlateFromPlanks(Consumer<FinishedRecipe> recipeConsumer, ItemLike planksILReference) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 1)
                .define('P', planksILReference)
                .pattern("PP")
                .unlockedBy("has_" + RegistryUtil.getItemName(planksILReference), PredicateUtil.has(planksILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> pressurePlateFromPlanks(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> pressurePlateFromPlanks(recipeConsumer, RegistryUtil.getPlanksFrom(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> fencesFromPlanks(Consumer<FinishedRecipe> recipeConsumer, ItemLike planksILReference) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 3)
                .define('P', planksILReference)
                .define('S', Items.STICK)
                .pattern("PSP")
                .pattern("PSP")
                .unlockedBy("has_" + RegistryUtil.getItemName(planksILReference), PredicateUtil.has(planksILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> fencesFromPlanks(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> fencesFromPlanks(recipeConsumer, RegistryUtil.getPlanksFrom(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> fenceGateFromPlanks(Consumer<FinishedRecipe> recipeConsumer, ItemLike planksILReference) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 1)
                .define('P', planksILReference)
                .define('S', Items.STICK)
                .pattern("SPS")
                .pattern("SPS")
                .unlockedBy("has_" + RegistryUtil.getItemName(planksILReference), PredicateUtil.has(planksILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> fenceGateFromPlanks(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> fenceGateFromPlanks(recipeConsumer, RegistryUtil.getPlanksFrom(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> standardBlockSmelting(Consumer<FinishedRecipe> recipeConsumer, ItemLike inputIngredientILReference) {
        return (resultBlockSup) -> SimpleCookingRecipeBuilder.smelting(Ingredient.of(inputIngredientILReference), RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 0.35F, 200)
                .group(RegistryUtil.getItemModId(inputIngredientILReference))
                .unlockedBy("has_" + RegistryUtil.getItemName(inputIngredientILReference), PredicateUtil.has(inputIngredientILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultBlockSup.get()), RegistryUtil.getItemName(resultBlockSup.get()) + "_from_smelting"));
    }

    public static Consumer<Supplier<Item>> cookedFoodFromSmelting(Consumer<FinishedRecipe> recipeConsumer, ItemLike rawFoodILReference) {
        return (resultItemSup) -> SimpleCookingRecipeBuilder.smelting(Ingredient.of(rawFoodILReference), RecipeCategory.FOOD, resultItemSup.get(), 0.35F, 200)
                .group(RegistryUtil.getItemModId(rawFoodILReference))
                .unlockedBy("has_" + RegistryUtil.getItemName(rawFoodILReference), PredicateUtil.has(rawFoodILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultItemSup.get()), RegistryUtil.getItemName(resultItemSup.get()) + "_from_smelting"));
    }

    public static Consumer<Supplier<Item>> foodSmokingRecipe(Consumer<FinishedRecipe> recipeConsumer, ItemLike rawFoodILReference) {
        return (resultItemSup) -> SimpleCookingRecipeBuilder.smoking(Ingredient.of(rawFoodILReference), RecipeCategory.FOOD, resultItemSup.get(), 0.35F, 100)
                .group(RegistryUtil.getItemModId(rawFoodILReference))
                .unlockedBy("has_" + RegistryUtil.getItemName(rawFoodILReference), PredicateUtil.has(rawFoodILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultItemSup.get()), RegistryUtil.getItemName(resultItemSup.get()) + "_from_smoking"));
    }

    public static Consumer<Supplier<Item>> foodCampfireSmokingRecipe(Consumer<FinishedRecipe> recipeConsumer, ItemLike rawFoodILReference) {
        return (resultItemSup) -> SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(rawFoodILReference), RecipeCategory.FOOD, resultItemSup.get(), 0.35F, 600)
                .group(RegistryUtil.getItemModId(rawFoodILReference))
                .unlockedBy("has_" + RegistryUtil.getItemName(rawFoodILReference), PredicateUtil.has(rawFoodILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultItemSup.get()), RegistryUtil.getItemName(resultItemSup.get()) + "_from_campfire_smoking"));
    }

    public static Consumer<Supplier<Item>> cookedFood(Consumer<FinishedRecipe> recipeConsumer, ItemLike rawFoodILReference) {
        return (resultItemSup) -> {
            cookedFoodFromSmelting(recipeConsumer, rawFoodILReference).accept(resultItemSup);
            foodSmokingRecipe(recipeConsumer, rawFoodILReference).accept(resultItemSup);
            foodCampfireSmokingRecipe(recipeConsumer, rawFoodILReference).accept(resultItemSup);
        };
    }

    public static Consumer<Supplier<Item>> cookedFood(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultItemSup) -> cookedFood(recipeConsumer, RegistryUtil.getRawFoodFromCookedFood(resultItemSup).get()).accept(resultItemSup);
    }

    public static Consumer<Supplier<Item>> threeRowRecipe(Consumer<FinishedRecipe> recipeConsumer, ItemLike topItemILReference, ItemLike middleItemILReference, ItemLike bottomItemILReference, int resultItemCount) {
        return (resultItemSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultItemSup.get(), resultItemCount)
                .define('T', topItemILReference)
                .define('M', middleItemILReference)
                .define('B', bottomItemILReference)
                .pattern("TTT")
                .pattern("MMM")
                .pattern("BBB")
                .unlockedBy("has_" + RegistryUtil.getItemName(topItemILReference), PredicateUtil.has(topItemILReference))
                .unlockedBy("has_" + RegistryUtil.getItemName(middleItemILReference), PredicateUtil.has(middleItemILReference))
                .unlockedBy("has_" + RegistryUtil.getItemName(bottomItemILReference), PredicateUtil.has(bottomItemILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Item>> threeRowRecipe(Consumer<FinishedRecipe> recipeConsumer, ItemLike topItemILReference, ItemLike middleItemILReference, ItemLike bottomItemILReference) {
        return (resultItemSup) -> threeRowRecipe(recipeConsumer, topItemILReference, middleItemILReference, bottomItemILReference, 1).accept(resultItemSup);
    }

    public static Consumer<Supplier<Block>> solidBlockFromSolidBlock(Consumer<FinishedRecipe> recipeConsumer, ItemLike solidBlockILReference, int resultBlockCount) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), resultBlockCount)
                .define('S', solidBlockILReference)
                .pattern("SS")
                .pattern("SS")
                .unlockedBy("has_" + RegistryUtil.getItemName(solidBlockILReference), PredicateUtil.has(solidBlockILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> solidBlockFromSolidBlock(Consumer<FinishedRecipe> recipeConsumer, ItemLike solidBlockILReference) {
        return (resultBlockSup) -> solidBlockFromSolidBlock(recipeConsumer, solidBlockILReference, 3).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> solidPillarFromSolidBlock(Consumer<FinishedRecipe> recipeConsumer, ItemLike solidBlockILReference, int resultBlockCount) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), resultBlockCount)
                .define('S', solidBlockILReference)
                .pattern("S")
                .pattern("S")
                .unlockedBy("has_" + RegistryUtil.getItemName(solidBlockILReference), PredicateUtil.has(solidBlockILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> solidPillarFromSolidBlock(Consumer<FinishedRecipe> recipeConsumer, ItemLike solidBlockILReference) {
        return (resultBlockSup) -> solidPillarFromSolidBlock(recipeConsumer, solidBlockILReference, 1).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> solidBlockFromSlab(Consumer<FinishedRecipe> recipeConsumer, ItemLike solidBlockILReference) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get())
                .define('S', solidBlockILReference)
                .pattern("S")
                .pattern("S")
                .unlockedBy("has_" + RegistryUtil.getItemName(solidBlockILReference), PredicateUtil.has(solidBlockILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> solidBlockFromSlab(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> solidBlockFromSlab(recipeConsumer, RegistryUtil.getSlabFromSolidBlock(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> slabsFromSolidBlock(Consumer<FinishedRecipe> recipeConsumer, ItemLike solidBlockILReference) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 6)
                .define('S', solidBlockILReference)
                .pattern("SSS")
                .unlockedBy("has_" + RegistryUtil.getItemName(solidBlockILReference), PredicateUtil.has(solidBlockILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> slabsFromSolidBlock(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> slabsFromSolidBlock(recipeConsumer, RegistryUtil.getSolidBlockFromSlab(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> stairsFromSolidBlock(Consumer<FinishedRecipe> recipeConsumer, ItemLike solidBlockILReference) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 4)
                .define('S', solidBlockILReference)
                .pattern("S  ")
                .pattern("SS ")
                .pattern("SSS")
                .unlockedBy("has_" + RegistryUtil.getItemName(solidBlockILReference), PredicateUtil.has(solidBlockILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> stairsFromSolidBlock(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> stairsFromSolidBlock(recipeConsumer, RegistryUtil.getSolidBlockFromStairs(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> wallsFromSolidBlock(Consumer<FinishedRecipe> recipeConsumer, ItemLike solidBlockILReference) {
        return (resultBlockSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 6)
                .define('S', solidBlockILReference)
                .pattern("SSS")
                .pattern("SSS")
                .unlockedBy("has_" + RegistryUtil.getItemName(solidBlockILReference), PredicateUtil.has(solidBlockILReference))
                .save(recipeConsumer);
    }

    public static Consumer<Supplier<Block>> wallsFromSolidBlock(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> wallsFromSolidBlock(recipeConsumer, RegistryUtil.getSolidBlockFromWall(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> solidBlockFromStoneCuttingSolidBlock(Consumer<FinishedRecipe> recipeConsumer, ItemLike solidBlockILReference) {
        return (resultBlockSup) -> SingleItemRecipeBuilder.stonecutting(Ingredient.of(solidBlockILReference), RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get())
                .unlockedBy("has_" + RegistryUtil.getItemName(solidBlockILReference), PredicateUtil.has(solidBlockILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultBlockSup.get()), RegistryUtil.getItemName(resultBlockSup.get()) + "_from_stonecutting_" + RegistryUtil.getItemName(solidBlockILReference)));
    }

    public static Consumer<Supplier<Block>> slabsFromStoneCuttingSolidBlock(Consumer<FinishedRecipe> recipeConsumer, ItemLike solidBlockILReference) {
        return (resultBlockSup) -> SingleItemRecipeBuilder.stonecutting(Ingredient.of(solidBlockILReference), RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get(), 2)
                .unlockedBy("has_" + RegistryUtil.getItemName(solidBlockILReference), PredicateUtil.has(solidBlockILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultBlockSup.get()), RegistryUtil.getItemName(resultBlockSup.get()) + "_from_stonecutting_" + RegistryUtil.getItemName(solidBlockILReference)));
    }

    public static Consumer<Supplier<Block>> slabsFromStoneCuttingSolidBlock(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> slabsFromStoneCuttingSolidBlock(recipeConsumer, RegistryUtil.getSolidBlockFromSlab(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> stairsFromStoneCuttingSolidBlock(Consumer<FinishedRecipe> recipeConsumer, ItemLike solidBlockILReference) {
        return (resultBlockSup) -> SingleItemRecipeBuilder.stonecutting(Ingredient.of(solidBlockILReference), RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get())
                .unlockedBy("has_" + RegistryUtil.getItemName(solidBlockILReference), PredicateUtil.has(solidBlockILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultBlockSup.get()), RegistryUtil.getItemName(resultBlockSup.get()) + "_from_stonecutting_" + RegistryUtil.getItemName(solidBlockILReference)));
    }

    public static Consumer<Supplier<Block>> stairsFromStoneCuttingSolidBlock(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> stairsFromStoneCuttingSolidBlock(recipeConsumer, RegistryUtil.getSolidBlockFromStairs(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> wallFromStoneCuttingSolidBlock(Consumer<FinishedRecipe> recipeConsumer, ItemLike solidBlockILReference) {
        return (resultBlockSup) -> SingleItemRecipeBuilder.stonecutting(Ingredient.of(solidBlockILReference), RecipeCategory.BUILDING_BLOCKS, resultBlockSup.get())
                .unlockedBy("has_" + RegistryUtil.getItemName(solidBlockILReference), PredicateUtil.has(solidBlockILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultBlockSup.get()), RegistryUtil.getItemName(resultBlockSup.get()) + "_from_stonecutting_" + RegistryUtil.getItemName(solidBlockILReference)));
    }

    public static Consumer<Supplier<Block>> wallFromStoneCuttingSolidBlock(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> wallFromStoneCuttingSolidBlock(recipeConsumer, RegistryUtil.getSolidBlockFromWall(resultBlockSup).get()).accept(resultBlockSup);
    }

    public static Consumer<Supplier<Block>> solidBlockRecipe(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> {
            // They don't have any by default since they're mined and stuff, just leaving this here in case we need it again or smth idk
        };
    }

    public static Consumer<Supplier<Block>> solidBrickBlockRecipe(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> {

        };
    }

    public static Consumer<Supplier<Block>> solidChiseledBlockRecipe(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> {

        };
    }

    public static Consumer<Supplier<Block>> solidCrackedBlockRecipe(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> {

        };
    }

    public static Consumer<Supplier<Block>> solidSlabRecipe(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> {

        };
    }

    public static Consumer<Supplier<Block>> solidStairsRecipe(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> {

        };
    }

    public static Consumer<Supplier<Block>> solidWallRecipe(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> {

        };
    }

    public static Consumer<Supplier<Block>> solidPillarRecipe(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultBlockSup) -> {

        };
    }

    public static Consumer<Supplier<Item>> nuggetsToIngot(Consumer<FinishedRecipe> recipeConsumer, ItemLike nuggetILReference) {
        return (resultItemSup) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, resultItemSup.get())
                .define('N', nuggetILReference)
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .unlockedBy("has_" + RegistryUtil.getItemName(nuggetILReference), PredicateUtil.has(nuggetILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultItemSup.get()), RegistryUtil.getItemName(resultItemSup.get()) + "_from_" + RegistryUtil.getItemName(nuggetILReference)));
    }

    public static Consumer<Supplier<Item>> nuggetsToIngot(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultItemSup) -> nuggetsToIngot(recipeConsumer, RegistryUtil.getNuggetFromIngot(resultItemSup).get()).accept(resultItemSup);
    }

    public static Consumer<Supplier<Item>> oreToMaterialSmelting(Consumer<FinishedRecipe> recipeConsumer, ItemLike oreILReference) {
        return (resultItemSup) -> SimpleCookingRecipeBuilder.smelting(Ingredient.of(oreILReference), RecipeCategory.MISC, resultItemSup.get(), 0.35F, 200)
                .group(RegistryUtil.getItemModId(oreILReference))
                .unlockedBy("has_" + RegistryUtil.getItemName(oreILReference), PredicateUtil.has(oreILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultItemSup.get()), RegistryUtil.getItemName(resultItemSup.get()) + "_from_smelting_" + RegistryUtil.getItemName(oreILReference)));
    }

    public static Consumer<Supplier<Item>> oreToMaterialBlasting(Consumer<FinishedRecipe> recipeConsumer, ItemLike oreILReference) {
        return (resultItemSup) -> SimpleCookingRecipeBuilder.blasting(Ingredient.of(oreILReference), RecipeCategory.MISC, resultItemSup.get(), 0.35F, 100)
                .group(RegistryUtil.getItemModId(oreILReference))
                .unlockedBy("has_" + RegistryUtil.getItemName(oreILReference), PredicateUtil.has(oreILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultItemSup.get()), RegistryUtil.getItemName(resultItemSup.get()) + "_from_blasting_" + RegistryUtil.getItemName(oreILReference)));
    }

    public static Consumer<Supplier<Item>> materialBlockToMaterial(Consumer<FinishedRecipe> recipeConsumer, ItemLike materialBlockILReference) {
        return (resultItemSup) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, resultItemSup.get(), 9)
                .requires(materialBlockILReference)
                .unlockedBy("has_" + RegistryUtil.getItemName(materialBlockILReference), PredicateUtil.has(materialBlockILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultItemSup.get()), RegistryUtil.getItemName(resultItemSup.get()) + "_from_" + RegistryUtil.getItemName(materialBlockILReference)));
    }

    public static Consumer<Supplier<Item>> materialBlockToIngot(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultItemSup) -> materialBlockToMaterial(recipeConsumer, RegistryUtil.getMaterialBlockFromIngot(resultItemSup).get()).accept(resultItemSup);
    }

    public static Consumer<Supplier<Item>> standardIngot(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultItemSup) -> {
            if (RegistryUtil.getNuggetFromIngot(resultItemSup) != null) nuggetsToIngot(recipeConsumer).accept(resultItemSup);
            if (RegistryUtil.getMaterialBlockFromIngot(resultItemSup) != null) materialBlockToIngot(recipeConsumer).accept(resultItemSup);

            RegistryUtil.getOresFromIngot(resultItemSup).forEach(curOre -> {
                oreToMaterialSmelting(recipeConsumer, curOre.get()).accept(resultItemSup);
                oreToMaterialBlasting(recipeConsumer, curOre.get()).accept(resultItemSup);
            });
        };
    }

    public static Consumer<Supplier<Item>> ingotToNuggets(Consumer<FinishedRecipe> recipeConsumer, ItemLike ingotILReference) {
        return (resultItemSup) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, resultItemSup.get(), 9)
                .requires(ingotILReference)
                .unlockedBy("has_" + RegistryUtil.getItemName(ingotILReference), PredicateUtil.has(ingotILReference))
                .save(recipeConsumer, new ResourceLocation(RegistryUtil.getItemModId(resultItemSup.get()), RegistryUtil.getItemName(resultItemSup.get()) + "_from_" + RegistryUtil.getItemName(ingotILReference)));
    }

    public static Consumer<Supplier<Item>> ingotToNuggets(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultItemSup) -> ingotToNuggets(recipeConsumer, RegistryUtil.getIngotFromNugget(resultItemSup).get()).accept(resultItemSup);
    }

    public static Consumer<Supplier<Item>> standardNugget(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultItemSup) -> {
            if (RegistryUtil.getIngotFromNugget(resultItemSup) != null) ingotToNuggets(recipeConsumer).accept(resultItemSup);
        };
    }

    public static Consumer<Supplier<Item>> standardMineral(Consumer<FinishedRecipe> recipeConsumer) {
        return (resultItemSup) -> {
            if (RegistryUtil.getMaterialBlockFrom(resultItemSup, "") != null) materialBlockToMaterial(recipeConsumer, RegistryUtil.getMaterialBlockFrom(resultItemSup, "").get()).accept(resultItemSup);

            RegistryUtil.getOresFrom(resultItemSup, "").forEach(curOre -> {
                oreToMaterialSmelting(recipeConsumer, curOre.get()).accept(resultItemSup);
                oreToMaterialBlasting(recipeConsumer, curOre.get()).accept(resultItemSup);
            });
        };
    }
}
