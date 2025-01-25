package io.github.chaosawakens.common.registry;

import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.common.block.base.general.MultiLayerPlantBlock;
import io.github.chaosawakens.util.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;

public final class CABlockPropertyWrappers {
    // Basic
    public static final BlockPropertyWrapper BASIC_BLOCK = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withCustomModelDefinitions(parentBlock -> ObjectArrayList.of(ModelUtil.cubeAll(RegistryUtil.getBlockTexture(parentBlock))))
            .withBlockStateDefinition(ModelUtil::simpleBlock)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();

    public static final BlockPropertyWrapper BASIC_BLOCK_SWORD = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.SWORD_EFFICIENT))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_SWORD_STONE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SWORD)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_STONE_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_SWORD_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SWORD)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_IRON_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_SWORD_DIAMOND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SWORD)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_DIAMOND_TOOL))
            .build();

    public static final BlockPropertyWrapper BASIC_BLOCK_PICKAXE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.MINEABLE_WITH_PICKAXE))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_PICKAXE_STONE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_STONE_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_PICKAXE_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_IRON_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_PICKAXE_DIAMOND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_DIAMOND_TOOL))
            .build();

    public static final BlockPropertyWrapper BASIC_BLOCK_AXE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.MINEABLE_WITH_AXE))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_AXE_STONE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_AXE)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_STONE_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_AXE_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_AXE)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_IRON_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_AXE_DIAMOND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_AXE)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_DIAMOND_TOOL))
            .build();

    public static final BlockPropertyWrapper BASIC_BLOCK_SHOVEL = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.MINEABLE_WITH_SHOVEL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_SHOVEL_STONE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_STONE_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_SHOVEL_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_IRON_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_SHOVEL_DIAMOND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_DIAMOND_TOOL))
            .build();

    public static final BlockPropertyWrapper BASIC_BLOCK_HOE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.MINEABLE_WITH_HOE))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_HOE_STONE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_STONE_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_HOE_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_IRON_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_HOE_DIAMOND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.NEEDS_DIAMOND_TOOL))
            .build();

    public static final BlockPropertyWrapper BASIC_ROTATED_PILLAR_BLOCK = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withCustomModelDefinitions(parentBlock -> ModelUtil.rotatedPillarBlock(RegistryUtil.getBlockTexture(parentBlock).withSuffix("_side"), RegistryUtil.getBlockTexture(parentBlock).withSuffix("_end")))
            .withBlockStateDefinition(ModelUtil::rotatedPillarBlock)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();
    public static final BlockPropertyWrapper BASIC_AXIS_ALIGNED_BLOCK = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withCustomModelDefinitions(parentBlock -> ObjectArrayList.of(ModelUtil.cubeColumn(RegistryUtil.getBlockTexture(parentBlock))))
            .withBlockStateDefinition(ModelUtil::axisAlignedBlock)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();

    // Wooden Stuff
    public static final BlockPropertyWrapper WOODEN_PLANKS = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_AXE)
            .cachedBuilder()
            .withRecipe(RecipeUtil::logToPlanks)
            .withTags(ObjectArrayList.of(() -> BlockTags.PLANKS, () -> ItemTags.PLANKS))
            .withFlammability(RegistryUtil::standardWoodFlammability)
            .build();
    public static final BlockPropertyWrapper WOODEN_SLAB = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSlab)
            .withRecipe(RecipeUtil::slabsFromPlanks)
            .withTags(ObjectArrayList.of(() -> BlockTags.WOODEN_SLABS, () -> BlockTags.MINEABLE_WITH_AXE, () -> ItemTags.WOODEN_SLABS))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.slab(RegistryUtil.getBlockTexture(RegistryUtil.getPlanksFrom(parentBlock))))
            .withBlockStateDefinition(ModelUtil::woodenSlab)
            .withFlammability(RegistryUtil::standardWoodFlammability)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();

    public static final BlockPropertyWrapper WOODEN_DOOR = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropDoor)
            .withRecipe(RecipeUtil::doorsFromPlanks)
            .withTags(ObjectArrayList.of(() -> BlockTags.WOODEN_DOORS, () -> BlockTags.MINEABLE_WITH_AXE, () -> ItemTags.WOODEN_DOORS))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.door(RegistryUtil.getTexture(RegistryUtil.getItemModId(parentBlock.get()), RegistryUtil.getItemName(parentBlock.get()).concat("_top")), RegistryUtil.getTexture(RegistryUtil.getItemModId(parentBlock.get()), RegistryUtil.getItemName(parentBlock.get()).concat("_bottom"))))
            .withBlockStateDefinition(ModelUtil::door)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();
    public static final BlockPropertyWrapper WOODEN_TRAPDOOR = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::trapdoorsFromPlanks)
            .withTags(ObjectArrayList.of(() -> BlockTags.WOODEN_TRAPDOORS, () -> BlockTags.MINEABLE_WITH_AXE, () -> ItemTags.WOODEN_TRAPDOORS))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.trapdoor(RegistryUtil.getBlockTexture(parentBlock), RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_bottom").withPrefix("block/")))
            .withBlockStateDefinition(ModelUtil::trapdoor)
            .withFlammability(RegistryUtil::standardWoodFlammability)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();

    public static final BlockPropertyWrapper WOODEN_FENCE = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::fencesFromPlanks)
            .withTags(ObjectArrayList.of(() -> BlockTags.WOODEN_FENCES, () -> BlockTags.MINEABLE_WITH_AXE, () -> ItemTags.WOODEN_FENCES))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.fence(RegistryUtil.getBlockTexture(RegistryUtil.getPlanksFrom(parentBlock)), RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_inventory").withPrefix("block/")))
            .withBlockStateDefinition(ModelUtil::fence)
            .withFlammability(RegistryUtil::standardWoodFlammability)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();
    public static final BlockPropertyWrapper WOODEN_FENCE_GATE = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::fenceGateFromPlanks)
            .withTags(ObjectArrayList.of(() -> BlockTags.FENCE_GATES, () -> BlockTags.MINEABLE_WITH_AXE, () -> ItemTags.FENCE_GATES))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.fenceGate(RegistryUtil.getBlockTexture(RegistryUtil.getPlanksFrom(parentBlock))))
            .withBlockStateDefinition(ModelUtil::fenceGate)
            .withFlammability(RegistryUtil::standardWoodFlammability)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();

    public static final BlockPropertyWrapper WOODEN_STAIRS = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::stairsFromPlanks)
            .withTags(ObjectArrayList.of(() -> BlockTags.WOODEN_STAIRS, () -> BlockTags.MINEABLE_WITH_AXE, () -> ItemTags.WOODEN_STAIRS))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.stairs(RegistryUtil.getBlockTexture(RegistryUtil.getPlanksFrom(parentBlock))))
            .withBlockStateDefinition(ModelUtil::stairs)
            .withFlammability(RegistryUtil::standardWoodFlammability)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();

    public static final BlockPropertyWrapper WOODEN_BUTTON = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::buttonFromPlanks)
            .withTags(ObjectArrayList.of(() -> BlockTags.WOODEN_BUTTONS, () -> BlockTags.MINEABLE_WITH_AXE, () -> ItemTags.WOODEN_BUTTONS))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.button(RegistryUtil.getBlockTexture(RegistryUtil.getPlanksFrom(parentBlock)), RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_inventory").withPrefix("block/")))
            .withBlockStateDefinition(ModelUtil::button)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();
    public static final BlockPropertyWrapper WOODEN_PRESSURE_PLATE = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::pressurePlateFromPlanks)
            .withTags(ObjectArrayList.of(() -> BlockTags.WOODEN_PRESSURE_PLATES, () -> BlockTags.MINEABLE_WITH_AXE, () -> ItemTags.WOODEN_PRESSURE_PLATES))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.pressurePlate(RegistryUtil.getBlockTexture(RegistryUtil.getPlanksFrom(parentBlock))))
            .withBlockStateDefinition(ModelUtil::pressurePlate)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();

    public static final BlockPropertyWrapper LOG = BlockPropertyWrapper.ofTemplate(BASIC_ROTATED_PILLAR_BLOCK)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.MINEABLE_WITH_AXE, () -> BlockTags.LOGS_THAT_BURN, () -> ItemTags.LOGS_THAT_BURN))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.rotatedPillarBlock(RegistryUtil.getBlockTexture(parentBlock), RegistryUtil.getBlockTexture(parentBlock).withSuffix("_top")))
            .withAxeStripping(RegistryUtil::getStrippedWoodFrom)
            .withFlammability(RegistryUtil::standardWoodFlammability)
            .build();
    public static final BlockPropertyWrapper WOOD = BlockPropertyWrapper.ofTemplate(BASIC_AXIS_ALIGNED_BLOCK)
            .cachedBuilder()
            .withRecipe(RecipeUtil::logToWood)
            .withTags(ObjectArrayList.of(() -> BlockTags.MINEABLE_WITH_AXE, () -> BlockTags.LOGS_THAT_BURN, () -> ItemTags.LOGS_THAT_BURN))
            .withCustomModelDefinitions(parentBlock -> ObjectArrayList.of(ModelUtil.cubeColumn(RegistryUtil.getBlockTexture(RegistryUtil.getLogFrom(parentBlock)))))
            .withAxeStripping(RegistryUtil::getStrippedWoodFrom)
            .withFlammability(RegistryUtil::standardWoodFlammability)
            .build();

    public static final BlockPropertyWrapper GATE_BLOCK = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .literalTranslation()
            .withCustomModelDefinitions(parentBlock -> ObjectArrayList.of(ModelUtil.cubeBottomTop(RegistryUtil.getBlockTexture(parentBlock), RegistryUtil.getBlockTexture(parentBlock).withSuffix("_top"), RegistryUtil.getBlockTexture(parentBlock).withSuffix("_top"))))
            .build();

    // Material
    public static final BlockPropertyWrapper MATERIAL_BLOCK_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE_IRON)
            .cachedBuilder()
            .withRecipe(RecipeUtil::materialToBlock)
            .build();
    public static final BlockPropertyWrapper MATERIAL_BLOCK_DIAMOND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE_DIAMOND)
            .cachedBuilder()
            .withRecipe(RecipeUtil::materialToBlock)
            .build();

    // Vegetation
    public static final BlockPropertyWrapper LEAVES = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropLeaves)
            .withTags(ObjectArrayList.of(() -> BlockTags.LEAVES, () -> ItemTags.LEAVES))
            .withCustomModelDefinitions(parentBlock -> ObjectArrayList.of(ModelUtil.leaves(RegistryUtil.getBlockTexture(parentBlock))))
            .withBlockStateDefinition(ModelUtil::simpleBlock)
            .withFlammability(RegistryUtil::standardWoodFlammability)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();
    public static final BlockPropertyWrapper FRUITABLE_LEAVES = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropLeavesRipe)
            .withTags(ObjectArrayList.of(() -> BlockTags.LEAVES, () -> ItemTags.LEAVES))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.fruitableLeaves(RegistryUtil.getBlockTexture(parentBlock)))
            .withBlockStateDefinition(ModelUtil::fruitableLeaves)
            .withFlammability(RegistryUtil::standardWoodFlammability)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();

    public static final BlockPropertyWrapper LEAF_CARPET = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropMultiFace)
            .withCustomModelDefinitions(parentBlock -> ModelUtil.leafCarpet(RegistryUtil.getBlockTexture(RegistryUtil.getLeavesFrom(parentBlock)), ModelLocationUtils.getModelLocation(parentBlock.get()).withSuffix("_inventory")))
            .withBlockStateDefinition(ModelUtil::leafCarpet)
            .withBlockColor(RegistryUtil::getVanillaLeafColorFor)
            .withFlammability(RegistryUtil::standardWoodFlammability)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();

    public static final BlockPropertyWrapper DIRT = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.DIRT, () -> ItemTags.DIRT))
            .withHoeTilling(RegistryUtil::defaultHoeTilling)
            .build();

    public static final BlockPropertyWrapper GRASS_BLOCK = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withCustomModelDefinitions(parentBlock -> ObjectArrayList.of(ModelUtil.grassBlock(RegistryUtil.getBlockTexture(parentBlock))))
            .withBlockColor(RegistryUtil::getVanillaGrassColorFor)
            .withLootTable(LootUtil::dropGrassBlock)
            .withHoeTilling(RegistryUtil::defaultHoeTilling)
            .withTags(ObjectArrayList.of(() -> BlockTags.DIRT, () -> ItemTags.DIRT))
            .build();
    public static final BlockPropertyWrapper CRYSTAL_GRASS_BLOCK = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE)
            .cachedBuilder()
            .withCustomModelDefinitions(parentBlock -> ObjectArrayList.of(ModelUtil.crystalGrassBlock(RegistryUtil.getBlockTexture(parentBlock))))
            .build();

    public static final BlockPropertyWrapper FARMLAND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withCustomModelDefinitions(parentBlock -> ModelUtil.farmland(RegistryUtil.getBlockTexture(parentBlock)))
            .withBlockTag(CATags.CABlockTags.FARMLAND_BLOCKS)
            .withBlockStateDefinition(ModelUtil::farmland)
            .withLootTable(LootUtil::dropFarmland)
            .build();
    public static final BlockPropertyWrapper DEFAULTED_FARMLAND = BlockPropertyWrapper.ofTemplate(FARMLAND)
            .cachedBuilder()
            .withCustomModelDefinitions(parentBlock -> ModelUtil.defaultedFarmland(RegistryUtil.getBlockTexture(parentBlock)))
            .build();

    public static final BlockPropertyWrapper BASIC_PLANT = BlockPropertyWrapper.createTemplate()
            .builder()
            .withCustomModelDefinitions(parentBlock -> ObjectArrayList.of(ModelUtil.tintedCrossCutout(RegistryUtil.getBlockTexture(parentBlock))))
            .withBlockStateDefinition(ModelUtil::simpleBlock)
            .withBlockColor(RegistryUtil::getVanillaGrassColorFor)
            .withLootTable(LootUtil::dropSilkTouchOrShears)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();
    public static final BlockPropertyWrapper BASIC_FLOWER = BlockPropertyWrapper.ofTemplate(BASIC_PLANT)
            .cachedBuilder()
            .withCustomModelDefinitions(parentBlock -> ObjectArrayList.of(ModelUtil.crossCutout(RegistryUtil.getBlockTexture(parentBlock))))
            .withBlockColor(null)
            .withTags(ObjectArrayList.of(() -> BlockTags.FLOWERS, () -> ItemTags.FLOWERS))
            .withLootTable(LootUtil::dropSelf)
            .build();
    public static final BlockPropertyWrapper SMALL_FLOWER = BlockPropertyWrapper.ofTemplate(BASIC_FLOWER)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(() -> BlockTags.SMALL_FLOWERS, () -> ItemTags.SMALL_FLOWERS))
            .build();

    public static final BlockPropertyWrapper TALL_PLANT = BlockPropertyWrapper.ofTemplate(BASIC_PLANT)
            .cachedBuilder()
            .withCustomModelDefinitions(parentBlock -> ModelUtil.tintedDoublePlant(RegistryUtil.getTexture(RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_top")), RegistryUtil.getTexture(RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_bottom"))))
            .withBlockStateDefinition(ModelUtil::doublePlant)
            .withLootTable(LootUtil::dropDoublePlantShearsOrSilkTouch)
            .build();
    public static final BlockPropertyWrapper TALL_FLOWER = BlockPropertyWrapper.ofTemplate(TALL_PLANT)
            .cachedBuilder()
            .withCustomModelDefinitions(parentBlock -> ModelUtil.doublePlant(RegistryUtil.getTexture(RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_top")), RegistryUtil.getTexture(RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_bottom"))))
            .withBlockColor(null)
            .withTags(ObjectArrayList.of(() -> BlockTags.TALL_FLOWERS, () -> ItemTags.TALL_FLOWERS))
            .withLootTable(LootUtil::dropDoublePlant)
            .build();

    public static final BlockPropertyWrapper MULTI_LAYER_PLANT = BlockPropertyWrapper.ofTemplate(BASIC_PLANT)
            .cachedBuilder()
            .withCustomModelDefinitions(parentBlock -> ModelUtil.tintedMultiLayerPlant(RegistryUtil.getTexture(RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_bottom")), RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_middle"), RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_top"), Math.min(((MultiLayerPlantBlock) parentBlock.get()).getPossibleLevels().asList().get(((MultiLayerPlantBlock) parentBlock.get()).getPossibleLevels().size() - 1) - 2, 1), true))
            .withBlockStateDefinition(ModelUtil::multiLayerPlantBlock)
            .build();

    public static final BlockPropertyWrapper NO_TINT_PLANT = BlockPropertyWrapper.ofTemplate(BASIC_PLANT)
            .cachedBuilder()
            .withBlockColor(null)
            .build();

    public static final BlockPropertyWrapper NO_TINT_TALL_PLANT = BlockPropertyWrapper.ofTemplate(TALL_PLANT)
            .cachedBuilder()
            .withBlockColor(null)
            .build();

    public static final BlockPropertyWrapper NO_TINT_MULTI_LAYER_PLANT = BlockPropertyWrapper.ofTemplate(MULTI_LAYER_PLANT)
            .cachedBuilder()
            .withCustomModelDefinitions(parentBlock -> ModelUtil.multiLayerPlant(RegistryUtil.getTexture(RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_bottom")), RegistryUtil.getTexture(RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_middle")), RegistryUtil.getTexture(RegistryUtil.getItemKey(parentBlock.get()).withSuffix("_top")), Math.min(((MultiLayerPlantBlock) parentBlock.get()).getPossibleLevels().asList().get(((MultiLayerPlantBlock) parentBlock.get()).getPossibleLevels().size() - 1) - 2, 1), true))
            .withBlockColor(null)
            .build();

    public static final BlockPropertyWrapper CRYSTAL_PLANT = BlockPropertyWrapper.ofTemplate(NO_TINT_PLANT)
            .cachedBuilder()
            .withSetTags(ObjectArrayList.of())
            .withBlockTag(CATags.CABlockTags.CRYSTAL_VEGETATION)
            .withItemTag(CATags.CAItemTags.CRYSTAL_VEGETATION)
            .build();
    public static final BlockPropertyWrapper CRYSTAL_FLOWER = BlockPropertyWrapper.ofTemplate(BASIC_FLOWER)
            .cachedBuilder()
            .withSetTags(ObjectArrayList.of())
            .withBlockTag(CATags.CABlockTags.CRYSTAL_FLOWER_BLOCKS)
            .withItemTag(CATags.CAItemTags.CRYSTAL_FLOWER_BLOCKS)
            .build();
    public static final BlockPropertyWrapper SMALL_CRYSTAL_FLOWER = BlockPropertyWrapper.ofTemplate(SMALL_FLOWER)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.CRYSTAL_FLOWER_BLOCKS)
            .withItemTag(CATags.CAItemTags.CRYSTAL_FLOWER_BLOCKS)
            .build();

    public static final BlockPropertyWrapper TALL_CRYSTAL_PLANT = BlockPropertyWrapper.ofTemplate(NO_TINT_TALL_PLANT)
            .cachedBuilder()
            .withSetTags(ObjectArrayList.of())
            .withBlockTag(CATags.CABlockTags.CRYSTAL_VEGETATION)
            .withItemTag(CATags.CAItemTags.CRYSTAL_VEGETATION)
            .build();
    public static final BlockPropertyWrapper TALL_CRYSTAL_FLOWER = BlockPropertyWrapper.ofTemplate(TALL_FLOWER)
            .cachedBuilder()
            .withSetTags(ObjectArrayList.of())
            .withBlockTag(CATags.CABlockTags.CRYSTAL_FLOWER_BLOCKS)
            .withItemTag(CATags.CAItemTags.CRYSTAL_FLOWER_BLOCKS)
            .build();

    public static final BlockPropertyWrapper DENSE_PLANT = BlockPropertyWrapper.ofTemplate(NO_TINT_PLANT)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.DENSE_VEGETATION)
            .withItemTag(CATags.CAItemTags.DENSE_VEGETATION)
            .build();
    public static final BlockPropertyWrapper DENSE_FLOWER = BlockPropertyWrapper.ofTemplate(BASIC_FLOWER)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.DENSE_FLOWER_BLOCKS)
            .withItemTag(CATags.CAItemTags.DENSE_FLOWER_BLOCKS)
            .build();
    public static final BlockPropertyWrapper SMALL_DENSE_FLOWER = BlockPropertyWrapper.ofTemplate(SMALL_FLOWER)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.DENSE_FLOWER_BLOCKS)
            .withItemTag(CATags.CAItemTags.DENSE_FLOWER_BLOCKS)
            .build();

    public static final BlockPropertyWrapper TALL_DENSE_PLANT = BlockPropertyWrapper.ofTemplate(NO_TINT_TALL_PLANT)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.DENSE_VEGETATION)
            .withItemTag(CATags.CAItemTags.DENSE_VEGETATION)
            .build();
    public static final BlockPropertyWrapper TALL_DENSE_FLOWER = BlockPropertyWrapper.ofTemplate(TALL_FLOWER)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.DENSE_FLOWER_BLOCKS)
            .withItemTag(CATags.CAItemTags.DENSE_FLOWER_BLOCKS)
            .build();

    // Solid
    public static final BlockPropertyWrapper SOLID_SLAB = BlockPropertyWrapper.ofTemplate(WOODEN_SLAB)
            .cachedBuilder()
            .withRecipe(RecipeUtil::solidSlabRecipe)
            .withCustomModelDefinitions(parentBlock -> ModelUtil.slab(RegistryUtil.getBlockTexture(RegistryUtil.getSolidBlockFromSlab(parentBlock))))
            .withSetTags(ObjectArrayList.of(() -> BlockTags.SLABS, () -> BlockTags.MINEABLE_WITH_PICKAXE, () -> ItemTags.SLABS))
            .build();

    public static final BlockPropertyWrapper SOLID_STAIRS = BlockPropertyWrapper.ofTemplate(WOODEN_STAIRS)
            .cachedBuilder()
            .withRecipe(RecipeUtil::solidStairsRecipe)
            .withCustomModelDefinitions(parentBlock -> ModelUtil.stairs(RegistryUtil.getBlockTexture(RegistryUtil.getSolidBlockFromStairs(parentBlock))))
            .withSetTags(ObjectArrayList.of(() -> BlockTags.STAIRS, () -> BlockTags.MINEABLE_WITH_PICKAXE, () -> ItemTags.STAIRS))
            .build();

    public static final BlockPropertyWrapper SOLID_WALL = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::solidWallRecipe)
            .withTags(ObjectArrayList.of(() -> BlockTags.WALLS, () -> ItemTags.WALLS, () -> BlockTags.MINEABLE_WITH_PICKAXE))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.wall(RegistryUtil.getBlockTexture(RegistryUtil.getSolidBlockFromWall(parentBlock)), ModelLocationUtils.getModelLocation(parentBlock.get()).withSuffix("_inventory")))
            .withBlockStateDefinition(ModelUtil::wall)
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_BLOCKS)
            .build();

    public static final BlockPropertyWrapper SOLID_ROTATED_PILLAR_BLOCK = BlockPropertyWrapper.ofTemplate(BASIC_ROTATED_PILLAR_BLOCK)
            .cachedBuilder()
            .withRecipe(RecipeUtil::solidPillarRecipe)
            .withTag(() -> BlockTags.MINEABLE_WITH_PICKAXE)
            .build();

    // Component Material
    public static final BlockPropertyWrapper COMPONENT_BLOCK = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withRecipe(RecipeUtil::componentMaterialBlock)
            .literalTranslation()
            .build();

    public static final BlockPropertyWrapper COMPONENT_BLOCK_STONE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE_STONE)
            .cachedBuilder()
            .withRecipe(RecipeUtil::componentMaterialBlock)
            .literalTranslation()
            .build();
    public static final BlockPropertyWrapper COMPONENT_BLOCK_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE_IRON)
            .cachedBuilder()
            .withRecipe(RecipeUtil::componentMaterialBlock)
            .literalTranslation()
            .build();
    public static final BlockPropertyWrapper COMPONENT_BLOCK_DIAMOND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE_DIAMOND)
            .cachedBuilder()
            .withRecipe(RecipeUtil::componentMaterialBlock)
            .literalTranslation()
            .build();

    public static final BlockPropertyWrapper COMPONENT_BLOCK_CONSTRUCTED = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withRecipe(RecipeUtil::componentMaterialBlock)
            .withLootTable(LootUtil::dropComponents)
            .literalTranslation()
            .build();

    // Fossil Blocks
    public static final BlockPropertyWrapper FOSSIL_BLOCK = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE)
            .cachedBuilder()
            .withLocalization(RegistryUtil::formatFossilName)
            .withSetParentCreativeModeTabs(ObjectArrayList.of(CACreativeModeTabs.CHAOS_AWAKENS_FOSSILS))
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE_IRON)
            .cachedBuilder()
            .withLocalization(RegistryUtil::formatFossilName)
            .withSetParentCreativeModeTabs(ObjectArrayList.of(CACreativeModeTabs.CHAOS_AWAKENS_FOSSILS))
            .build();
    public static final BlockPropertyWrapper FALLING_FOSSIL_BLOCK = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withLocalization(RegistryUtil::formatFossilName)
            .withSetParentCreativeModeTabs(ObjectArrayList.of(CACreativeModeTabs.CHAOS_AWAKENS_FOSSILS))
            .build();

    // Fossil Blocks (Mod-Agnostic)
    public static final BlockPropertyWrapper FOSSIL_BLOCK_MOD_AGNOSTIC_STONE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_STONE)
            .withItemTag(CATags.CAItemTags.FOSSILS_STONE)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_MOD_AGNOSTIC_DEEPSLATE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK_IRON)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_DEEPSLATE)
            .withItemTag(CATags.CAItemTags.FOSSILS_DEEPSLATE)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_MOD_AGNOSTIC_SANDSTONE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_SANDSTONE)
            .withItemTag(CATags.CAItemTags.FOSSILS_SANDSTONE)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_MOD_AGNOSTIC_ICE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_ICE)
            .withItemTag(CATags.CAItemTags.FOSSILS_ICE)
            .build();

    public static final BlockPropertyWrapper FOSSIL_BLOCK_MOD_AGNOSTIC_GRAVEL = BlockPropertyWrapper.ofTemplate(FALLING_FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_GRAVEL)
            .withItemTag(CATags.CAItemTags.FOSSILS_GRAVEL)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_MOD_AGNOSTIC_SAND = BlockPropertyWrapper.ofTemplate(FALLING_FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_SAND)
            .withItemTag(CATags.CAItemTags.FOSSILS_SAND)
            .build();

    public static final BlockPropertyWrapper FOSSIL_BLOCK_MOD_AGNOSTIC_BLACKSTONE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_BLACKSTONE)
            .withItemTag(CATags.CAItemTags.FOSSILS_BLACKSTONE)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_MOD_AGNOSTIC_NETHERRACK = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_NETHERRACK)
            .withItemTag(CATags.CAItemTags.FOSSILS_NETHERRACK)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_MOD_AGNOSTIC_SOUL_SOIL = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_SOUL_SOIL)
            .withItemTag(CATags.CAItemTags.FOSSILS_SOUL_SOIL)
            .build();

    public static final BlockPropertyWrapper FOSSIL_BLOCK_MOD_AGNOSTIC_END_STONE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_END_STONE)
            .withItemTag(CATags.CAItemTags.FOSSILS_END_STONE)
            .build();

    public static final BlockPropertyWrapper FOSSIL_BLOCK_MOD_AGNOSTIC_KYANITE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_KYANITE)
            .withItemTag(CATags.CAItemTags.FOSSILS_KYANITE)
            .build();

    // Fossil Blocks (Vanilla)
    public static final BlockPropertyWrapper FOSSIL_BLOCK_VANILLA_STONE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_STONE_VANILLA)
            .withItemTag(CATags.CAItemTags.FOSSILS_STONE_VANILLA)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_VANILLA_DEEPSLATE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK_IRON)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_DEEPSLATE)
            .withItemTag(CATags.CAItemTags.FOSSILS_DEEPSLATE)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_VANILLA_SANDSTONE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_SANDSTONE_VANILLA)
            .withItemTag(CATags.CAItemTags.FOSSILS_SANDSTONE_VANILLA)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_VANILLA_ICE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_ICE_VANILLA)
            .withItemTag(CATags.CAItemTags.FOSSILS_ICE_VANILLA)
            .build();

    public static final BlockPropertyWrapper FOSSIL_BLOCK_VANILLA_GRAVEL = BlockPropertyWrapper.ofTemplate(FALLING_FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_GRAVEL_VANILLA)
            .withItemTag(CATags.CAItemTags.FOSSILS_GRAVEL_VANILLA)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_VANILLA_SAND = BlockPropertyWrapper.ofTemplate(FALLING_FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_SAND_VANILLA)
            .withItemTag(CATags.CAItemTags.FOSSILS_SAND_VANILLA)
            .build();

    public static final BlockPropertyWrapper FOSSIL_BLOCK_VANILLA_BLACKSTONE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_BLACKSTONE_VANILLA)
            .withItemTag(CATags.CAItemTags.FOSSILS_BLACKSTONE_VANILLA)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_VANILLA_NETHERRACK = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_NETHERRACK_VANILLA)
            .withItemTag(CATags.CAItemTags.FOSSILS_NETHERRACK_VANILLA)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_VANILLA_SOUL_SOIL = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_SOUL_SOIL_VANILLA)
            .withItemTag(CATags.CAItemTags.FOSSILS_SOUL_SOIL_VANILLA)
            .build();

    public static final BlockPropertyWrapper FOSSIL_BLOCK_VANILLA_END_STONE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_END_STONE_VANILLA)
            .withItemTag(CATags.CAItemTags.FOSSILS_END_STONE_VANILLA)
            .build();

    public static final BlockPropertyWrapper FOSSIL_BLOCK_VANILLA_KYANITE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_KYANITE_VANILLA)
            .withItemTag(CATags.CAItemTags.FOSSILS_KYANITE_VANILLA)
            .build();

    // Fossil Blocks (Chaos Awakens)
    public static final BlockPropertyWrapper FOSSIL_BLOCK_CHAOSAWAKENS_STONE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_STONE_CHAOSAWAKENS)
            .withItemTag(CATags.CAItemTags.FOSSILS_STONE_CHAOSAWAKENS)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_CHAOSAWAKENS_DEEPSLATE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK_IRON)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_DEEPSLATE)
            .withItemTag(CATags.CAItemTags.FOSSILS_DEEPSLATE)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_CHAOSAWAKENS_SANDSTONE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_SANDSTONE_CHAOSAWAKENS)
            .withItemTag(CATags.CAItemTags.FOSSILS_SANDSTONE_CHAOSAWAKENS)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_CHAOSAWAKENS_ICE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_ICE_CHAOSAWAKENS)
            .withItemTag(CATags.CAItemTags.FOSSILS_ICE_CHAOSAWAKENS)
            .build();

    public static final BlockPropertyWrapper FOSSIL_BLOCK_CHAOSAWAKENS_GRAVEL = BlockPropertyWrapper.ofTemplate(FALLING_FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_GRAVEL_CHAOSAWAKENS)
            .withItemTag(CATags.CAItemTags.FOSSILS_GRAVEL_CHAOSAWAKENS)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_CHAOSAWAKENS_SAND = BlockPropertyWrapper.ofTemplate(FALLING_FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_SAND_CHAOSAWAKENS)
            .withItemTag(CATags.CAItemTags.FOSSILS_SAND_CHAOSAWAKENS)
            .build();

    public static final BlockPropertyWrapper FOSSIL_BLOCK_CHAOSAWAKENS_BLACKSTONE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_BLACKSTONE_CHAOSAWAKENS)
            .withItemTag(CATags.CAItemTags.FOSSILS_BLACKSTONE_CHAOSAWAKENS)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_CHAOSAWAKENS_NETHERRACK = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_NETHERRACK_CHAOSAWAKENS)
            .withItemTag(CATags.CAItemTags.FOSSILS_NETHERRACK_CHAOSAWAKENS)
            .build();
    public static final BlockPropertyWrapper FOSSIL_BLOCK_CHAOSAWAKENS_SOUL_SOIL = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_SOUL_SOIL_CHAOSAWAKENS)
            .withItemTag(CATags.CAItemTags.FOSSILS_SOUL_SOIL_CHAOSAWAKENS)
            .build();

    public static final BlockPropertyWrapper FOSSIL_BLOCK_CHAOSAWAKENS_END_STONE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_END_STONE_CHAOSAWAKENS)
            .withItemTag(CATags.CAItemTags.FOSSILS_END_STONE_CHAOSAWAKENS)
            .build();

    public static final BlockPropertyWrapper FOSSIL_BLOCK_CHAOSAWAKENS_KYANITE = BlockPropertyWrapper.ofTemplate(FOSSIL_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FOSSILS_KYANITE_CHAOSAWAKENS)
            .withItemTag(CATags.CAItemTags.FOSSILS_KYANITE_CHAOSAWAKENS)
            .build();

    // Flower
    public static final BlockPropertyWrapper STEM_BLOCK = BlockPropertyWrapper.ofTemplate(BASIC_ROTATED_PILLAR_BLOCK)
            .cachedBuilder()
            .withCustomModelDefinitions(parentBlock -> ModelUtil.rotatedPillarBlock(RegistryUtil.getBlockTexture(parentBlock), RegistryUtil.getBlockTexture(parentBlock).withSuffix("_top")))
            .withBlockTag(CATags.CABlockTags.FLOWER_BLOCKS)
            .withItemTag(CATags.CAItemTags.FLOWER_BLOCKS)
            .withTags(ObjectArrayList.of(() -> BlockTags.MINEABLE_WITH_HOE, () -> BlockTags.MINEABLE_WITH_AXE))
            .build();
    public static final BlockPropertyWrapper PETAL_BLOCK = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withBlockTag(CATags.CABlockTags.FLOWER_BLOCKS)
            .withItemTag(CATags.CAItemTags.FLOWER_BLOCKS)
            .withTags(ObjectArrayList.of(() -> BlockTags.MINEABLE_WITH_HOE, () -> BlockTags.MINEABLE_WITH_AXE))
            .build();
}
