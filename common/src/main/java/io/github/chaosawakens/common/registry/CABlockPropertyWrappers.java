package io.github.chaosawakens.common.registry;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.util.LootUtil;
import io.github.chaosawakens.util.ModelUtil;
import io.github.chaosawakens.util.RecipeUtil;
import io.github.chaosawakens.util.RegistryUtil;
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
            .build();

    public static final BlockPropertyWrapper BASIC_BLOCK_SWORD = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.SWORD_EFFICIENT))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_SWORD_STONE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SWORD)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_STONE_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_SWORD_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SWORD)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_IRON_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_SWORD_DIAMOND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SWORD)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_DIAMOND_TOOL))
            .build();

    public static final BlockPropertyWrapper BASIC_BLOCK_PICKAXE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.MINEABLE_WITH_PICKAXE))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_PICKAXE_STONE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_STONE_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_PICKAXE_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_IRON_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_PICKAXE_DIAMOND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_PICKAXE)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_DIAMOND_TOOL))
            .build();

    public static final BlockPropertyWrapper BASIC_BLOCK_AXE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.MINEABLE_WITH_AXE))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_AXE_STONE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_AXE)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_STONE_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_AXE_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_AXE)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_IRON_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_AXE_DIAMOND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_AXE)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_DIAMOND_TOOL))
            .build();

    public static final BlockPropertyWrapper BASIC_BLOCK_SHOVEL = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.MINEABLE_WITH_SHOVEL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_SHOVEL_STONE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_STONE_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_SHOVEL_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_IRON_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_SHOVEL_DIAMOND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_DIAMOND_TOOL))
            .build();

    public static final BlockPropertyWrapper BASIC_BLOCK_HOE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.MINEABLE_WITH_HOE))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_HOE_STONE = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_STONE_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_HOE_IRON = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_IRON_TOOL))
            .build();
    public static final BlockPropertyWrapper BASIC_BLOCK_HOE_DIAMOND = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_SHOVEL)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.NEEDS_DIAMOND_TOOL))
            .build();

    public static final BlockPropertyWrapper BASIC_ROTATED_PILLAR_BLOCK = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withCustomModelDefinitions(parentBlock -> ModelUtil.rotatedPillarBlock(RegistryUtil.getBlockTexture(parentBlock).withSuffix("_side"), RegistryUtil.getBlockTexture(parentBlock).withSuffix("_end")))
            .withBlockStateDefinition(ModelUtil::rotatedPillarBlock)
            .build();
    public static final BlockPropertyWrapper BASIC_AXIS_ALIGNED_BLOCK = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withCustomModelDefinitions(parentBlock -> ObjectArrayList.of(ModelUtil.cubeColumn(RegistryUtil.getBlockTexture(parentBlock))))
            .withBlockStateDefinition(ModelUtil::axisAlignedBlock)
            .build();

    // Wooden Stuff
    public static final BlockPropertyWrapper WOODEN_PLANKS = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK_AXE)
            .cachedBuilder()
            .withRecipe(RecipeUtil::logToPlanks)
            .withTags(ObjectArrayList.of(BlockTags.PLANKS, ItemTags.PLANKS))
            .build();
    public static final BlockPropertyWrapper WOODEN_SLAB = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSlab)
            .withRecipe(RecipeUtil::slabsFromPlanks)
            .withTags(ObjectArrayList.of(BlockTags.WOODEN_SLABS, BlockTags.MINEABLE_WITH_AXE, ItemTags.WOODEN_SLABS))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.slab(RegistryUtil.getBlockTexture(RegistryUtil.getPlanksFrom(parentBlock))))
            .withBlockStateDefinition(ModelUtil::woodenSlab)
            .build();

    public static final BlockPropertyWrapper WOODEN_DOOR = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropDoor)
            .withRecipe(RecipeUtil::doorsFromPlanks)
            .withTags(ObjectArrayList.of(BlockTags.WOODEN_DOORS, BlockTags.MINEABLE_WITH_AXE, ItemTags.WOODEN_DOORS))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.door(RegistryUtil.getTexture(RegistryUtil.getItemModId(parentBlock.get()), RegistryUtil.getItemName(parentBlock.get()).concat("_top")), RegistryUtil.getTexture(RegistryUtil.getItemModId(parentBlock.get()), RegistryUtil.getItemName(parentBlock.get()).concat("_bottom"))))
            .withBlockStateDefinition(ModelUtil::door)
            .build();
    public static final BlockPropertyWrapper WOODEN_TRAPDOOR = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::trapdoorsFromPlanks)
            .withTags(ObjectArrayList.of(BlockTags.WOODEN_TRAPDOORS, BlockTags.MINEABLE_WITH_AXE, ItemTags.WOODEN_TRAPDOORS))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.trapdoor(RegistryUtil.getBlockTexture(parentBlock), CAConstants.prefix(RegistryUtil.getItemName(parentBlock.get())).withSuffix("_bottom").withPrefix("block/")))
            .withBlockStateDefinition(ModelUtil::trapdoor)
            .build();

    public static final BlockPropertyWrapper WOODEN_FENCE = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::fencesFromPlanks)
            .withTags(ObjectArrayList.of(BlockTags.WOODEN_FENCES, BlockTags.MINEABLE_WITH_AXE, ItemTags.WOODEN_FENCES))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.fence(RegistryUtil.getBlockTexture(RegistryUtil.getPlanksFrom(parentBlock)), CAConstants.prefix(RegistryUtil.getItemName(parentBlock.get()).concat("_inventory")).withPrefix("block/")))
            .withBlockStateDefinition(ModelUtil::fence)
            .build();
    public static final BlockPropertyWrapper WOODEN_FENCE_GATE = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::fenceGateFromPlanks)
            .withTags(ObjectArrayList.of(BlockTags.FENCE_GATES, BlockTags.MINEABLE_WITH_AXE, ItemTags.FENCE_GATES))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.fenceGate(RegistryUtil.getBlockTexture(RegistryUtil.getPlanksFrom(parentBlock))))
            .withBlockStateDefinition(ModelUtil::fenceGate)
            .build();

    public static final BlockPropertyWrapper WOODEN_STAIRS = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::stairsFromPlanks)
            .withTags(ObjectArrayList.of(BlockTags.WOODEN_STAIRS, BlockTags.MINEABLE_WITH_AXE, ItemTags.WOODEN_STAIRS))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.stairs(RegistryUtil.getBlockTexture(RegistryUtil.getPlanksFrom(parentBlock))))
            .withBlockStateDefinition(ModelUtil::stairs)
            .build();

    public static final BlockPropertyWrapper WOODEN_BUTTON = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::buttonFromPlanks)
            .withTags(ObjectArrayList.of(BlockTags.WOODEN_BUTTONS, BlockTags.MINEABLE_WITH_AXE))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.button(RegistryUtil.getBlockTexture(RegistryUtil.getPlanksFrom(parentBlock)), CAConstants.prefix(RegistryUtil.getItemName(parentBlock.get()).concat("_inventory")).withPrefix("block/")))
            .withBlockStateDefinition(ModelUtil::button)
            .build();
    public static final BlockPropertyWrapper WOODEN_PRESSURE_PLATE = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::pressurePlateFromPlanks)
            .withTags(ObjectArrayList.of(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_AXE))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.pressurePlate(RegistryUtil.getBlockTexture(RegistryUtil.getPlanksFrom(parentBlock))))
            .withBlockStateDefinition(ModelUtil::pressurePlate)
            .build();

    public static final BlockPropertyWrapper LOG = BlockPropertyWrapper.ofTemplate(BASIC_ROTATED_PILLAR_BLOCK)
            .cachedBuilder()
            .withTags(ObjectArrayList.of(BlockTags.MINEABLE_WITH_AXE, BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.rotatedPillarBlock(RegistryUtil.getBlockTexture(parentBlock), RegistryUtil.getBlockTexture(parentBlock).withSuffix("_top")))
            .build();
    public static final BlockPropertyWrapper WOOD = BlockPropertyWrapper.ofTemplate(BASIC_AXIS_ALIGNED_BLOCK)
            .cachedBuilder()
            .withRecipe(RecipeUtil::logToWood)
            .withTags(ObjectArrayList.of(BlockTags.MINEABLE_WITH_AXE, BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN))
            .withCustomModelDefinitions(parentBlock -> ObjectArrayList.of(ModelUtil.cubeColumn(RegistryUtil.getBlockTexture(RegistryUtil.getLogFrom(parentBlock)))))
            .build();

    public static final BlockPropertyWrapper GATE_BLOCK = BlockPropertyWrapper.ofTemplate(BASIC_BLOCK)
            .cachedBuilder()
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
            .withTags(ObjectArrayList.of(BlockTags.LEAVES, ItemTags.LEAVES))
            .withCustomModelDefinitions(parentBlock -> ObjectArrayList.of(ModelUtil.leaves(RegistryUtil.getBlockTexture(parentBlock))))
            .withBlockStateDefinition(ModelUtil::simpleBlock)
            .build();
    public static final BlockPropertyWrapper FRUITABLE_LEAVES = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropLeavesRipe)
            .withTags(ObjectArrayList.of(BlockTags.LEAVES, ItemTags.LEAVES))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.fruitableLeaves(RegistryUtil.getBlockTexture(parentBlock)))
            .withBlockStateDefinition(ModelUtil::fruitableLeaves)
            .build();

    public static final BlockPropertyWrapper LEAF_CARPET = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropMultiFace)
            .withCustomModelDefinitions(parentBlock -> ModelUtil.leafCarpet(RegistryUtil.getBlockTexture(RegistryUtil.getLeavesFrom(parentBlock)), ModelLocationUtils.getModelLocation(parentBlock.get()).withSuffix("_inventory")))
            .withBlockStateDefinition(ModelUtil::leafCarpet)
            .withBlockColor(RegistryUtil::getVanillaLeafColorFor)
            .build();

    // Solid
    public static final BlockPropertyWrapper SOLID_SLAB = BlockPropertyWrapper.ofTemplate(WOODEN_SLAB)
            .cachedBuilder()
            .withRecipe(RecipeUtil::solidSlabRecipe)
            .withCustomModelDefinitions(parentBlock -> ModelUtil.slab(RegistryUtil.getBlockTexture(RegistryUtil.getSolidBlockFromSlab(parentBlock))))
            .withSetTags(ObjectArrayList.of(BlockTags.SLABS, BlockTags.MINEABLE_WITH_PICKAXE, ItemTags.SLABS))
            .build();

    public static final BlockPropertyWrapper SOLID_STAIRS = BlockPropertyWrapper.ofTemplate(WOODEN_STAIRS)
            .cachedBuilder()
            .withRecipe(RecipeUtil::solidStairsRecipe)
            .withCustomModelDefinitions(parentBlock -> ModelUtil.stairs(RegistryUtil.getBlockTexture(RegistryUtil.getSolidBlockFromStairs(parentBlock))))
            .withSetTags(ObjectArrayList.of(BlockTags.STAIRS, BlockTags.MINEABLE_WITH_PICKAXE, ItemTags.STAIRS))
            .build();

    public static final BlockPropertyWrapper SOLID_WALL = BlockPropertyWrapper.createTemplate()
            .builder()
            .withLootTable(LootUtil::dropSelf)
            .withRecipe(RecipeUtil::solidWallRecipe)
            .withTags(ObjectArrayList.of(BlockTags.WALLS, ItemTags.WALLS, BlockTags.MINEABLE_WITH_PICKAXE))
            .withCustomModelDefinitions(parentBlock -> ModelUtil.wall(RegistryUtil.getBlockTexture(RegistryUtil.getSolidBlockFromWall(parentBlock)), ModelLocationUtils.getModelLocation(parentBlock.get()).withSuffix("_inventory")))
            .withBlockStateDefinition(ModelUtil::wall)
            .build();

    public static final BlockPropertyWrapper SOLID_ROTATED_PILLAR_BLOCK = BlockPropertyWrapper.ofTemplate(BASIC_ROTATED_PILLAR_BLOCK)
            .cachedBuilder()
            .withRecipe(RecipeUtil::solidPillarRecipe)
            .withTag(BlockTags.MINEABLE_WITH_PICKAXE)
            .build();
}
