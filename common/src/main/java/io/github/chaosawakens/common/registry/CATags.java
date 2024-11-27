package io.github.chaosawakens.common.registry;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.LoadEarly;
import io.github.chaosawakens.api.tag.TagWrapper;
import io.github.chaosawakens.util.RegistryUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CATags {

    @LoadEarly
    public static class CABlockTags {
        public static final TagKey<Block> BASE_STONE_CRYSTAL = createWrappedBlockTag("base_stone_crystal").getParentTag();
        public static final TagKey<Block> BASE_STONE_MINING = createWrappedBlockTag("base_stone_mining").getParentTag();
        public static final TagKey<Block> BASE_STONE_VILLAGE = createWrappedBlockTag("base_stone_village").getParentTag();

        public static final TagKey<Block> MINERS_DREAM_MINEABLE = createWrappedBlockTag("miners_dream_mineable")
                .withTagEntries(ObjectArrayList.of(BASE_STONE_CRYSTAL, BASE_STONE_MINING, BASE_STONE_VILLAGE, BlockTags.BASE_STONE_OVERWORLD, BlockTags.BASE_STONE_NETHER))
                .getParentTag();

        public static final TagKey<Block> FARMABLE = createBlockTag("farmable");

        public static final TagKey<Block> DENSE_DIRT = createBlockTag("dense_dirt");
        public static final TagKey<Block> TERRA_PRETA = createBlockTag("terra_preta");

        public static final TagKey<Block> APPLE_LOGS = createWrappedBlockTag("wood/logs/apple_logs")
                .withParentTagEntry(BlockTags.LOGS_THAT_BURN)
                .withEntries(ObjectArrayList.of(CABlocks.APPLE_LOG, CABlocks.APPLE_WOOD, CABlocks.STRIPPED_APPLE_LOG, CABlocks.STRIPPED_APPLE_WOOD))
                .getParentTag();
        public static final TagKey<Block> DEAD_DUPLICATOR_LOGS = createWrappedBlockTag("wood/logs/dead_duplicator_logs")
                .withParentTagEntry(BlockTags.LOGS_THAT_BURN)
                .withEntries(ObjectArrayList.of(CABlocks.DEAD_DUPLICATOR_LOG, CABlocks.DEAD_DUPLICATOR_WOOD, CABlocks.STRIPPED_DEAD_DUPLICATOR_LOG, CABlocks.STRIPPED_DEAD_DUPLICATOR_WOOD))
                .getParentTag();
        public static final TagKey<Block> DUPLICATOR_LOGS = createWrappedBlockTag("wood/logs/duplicator_logs")
                .withParentTagEntry(BlockTags.LOGS_THAT_BURN)
                .withTagEntry(DEAD_DUPLICATOR_LOGS)
                .withEntries(ObjectArrayList.of(CABlocks.DUPLICATOR_LOG, CABlocks.DUPLICATOR_WOOD, CABlocks.STRIPPED_DUPLICATOR_LOG, CABlocks.STRIPPED_DUPLICATOR_WOOD))
                .getParentTag();
        public static final TagKey<Block> PEACH_LOGS = createWrappedBlockTag("wood/logs/peach_logs")
                .withParentTagEntry(BlockTags.LOGS_THAT_BURN)
                .withEntries(ObjectArrayList.of(CABlocks.PEACH_LOG, CABlocks.PEACH_WOOD, CABlocks.STRIPPED_PEACH_LOG, CABlocks.STRIPPED_PEACH_WOOD))
                .getParentTag();
        public static final TagKey<Block> SKYWOOD_LOGS = createWrappedBlockTag("wood/logs/skywood_logs")
                .withParentTagEntry(BlockTags.LOGS_THAT_BURN)
                .withEntries(ObjectArrayList.of(CABlocks.SKYWOOD_LOG, CABlocks.SKYWOOD, CABlocks.STRIPPED_SKYWOOD_LOG, CABlocks.STRIPPED_SKYWOOD))
                .getParentTag();
        public static final TagKey<Block> GINKGO_LOGS = createWrappedBlockTag("wood/logs/ginkgo_logs")
                .withParentTagEntry(BlockTags.LOGS_THAT_BURN)
                .withEntries(ObjectArrayList.of(CABlocks.GINKGO_LOG, CABlocks.GINKGO_WOOD, CABlocks.STRIPPED_GINKGO_LOG, CABlocks.STRIPPED_GINKGO_WOOD))
                .getParentTag();
        public static final TagKey<Block> MESOZOIC_LOGS = createWrappedBlockTag("wood/logs/mesozoic_logs")
                .withParentTagEntry(BlockTags.LOGS_THAT_BURN)
                .withEntries(ObjectArrayList.of(CABlocks.MESOZOIC_LOG, CABlocks.MESOZOIC_WOOD, CABlocks.STRIPPED_MESOZOIC_LOG, CABlocks.STRIPPED_MESOZOIC_WOOD))
                .getParentTag();
        public static final TagKey<Block> DENSEWOOD_LOGS = createWrappedBlockTag("wood/logs/densewood_logs")
                .withParentTagEntry(BlockTags.LOGS_THAT_BURN)
                .withEntries(ObjectArrayList.of(CABlocks.DENSEWOOD_LOG, CABlocks.DENSEWOOD, CABlocks.STRIPPED_DENSEWOOD_LOG, CABlocks.STRIPPED_DENSEWOOD))
                .getParentTag();

        public static final TagKey<Block> CRYSTALWOOD_LOGS = createWrappedBlockTag("wood/logs/crystalwood_logs")
                .withEntries(ObjectArrayList.of(CABlocks.CRYSTALWOOD_LOG, CABlocks.CRYSTALWOOD, CABlocks.STRIPPED_CRYSTALWOOD_LOG, CABlocks.STRIPPED_CRYSTALWOOD))
                .getParentTag();
        public static final TagKey<Block> CRYSTALWOOD_LEAVES = createBlockTag("wood/leaves/crystalwood_leaves");
        public static final TagKey<Block> CRYSTALWOOD_SAPLINGS = createBlockTag("wood/saplings/crystalwood_saplings");

        public static final TagKey<Block> RUBY_ORES = createBlockTag("ruby_ores");

        public static final TagKey<Block> STALAGMITE_ORE_COMMON = createBlockTag("stalagmite_ore/common");
        public static final TagKey<Block> STALAGMITE_ORE_RARE = createBlockTag("stalagmite_ore/rare");
        public static final TagKey<Block> STALAGMITE_ORE = createWrappedBlockTag("stalagmite_ore")
                .withTagEntries(ObjectArrayList.of(STALAGMITE_ORE_COMMON, STALAGMITE_ORE_RARE))
                .getParentTag();

        public static final TagKey<Block> POUNDER_IMMUNE = createBlockTag("robo_immune/pounder");
        public static final TagKey<Block> JEFFERY_IMMUNE = createBlockTag("robo_immune/jeffery");
        public static final TagKey<Block> ROBO_IMMUNE = createWrappedBlockTag("robo_immune")
                .withTagEntries(ObjectArrayList.of(POUNDER_IMMUNE, JEFFERY_IMMUNE))
                .getParentTag();

        public static final TagKey<Block> FOSSILS_STONE_VANILLA = createBlockTag("fossils/stone/vanilla");
        public static final TagKey<Block> FOSSILS_STONE_CHAOSAWAKENS = createBlockTag("fossils/stone/chaosawakens");
        public static final TagKey<Block> FOSSILS_STONE = createWrappedBlockTag("fossils/stone")
                .withTagEntries(ObjectArrayList.of(FOSSILS_STONE_VANILLA, FOSSILS_STONE_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Block> FOSSILS_GRAVEL_VANILLA = createBlockTag("fossils/gravel/vanilla");
        public static final TagKey<Block> FOSSILS_GRAVEL_CHAOSAWAKENS = createBlockTag("fossils/gravel/chaosawakens");
        public static final TagKey<Block> FOSSILS_GRAVEL = createWrappedBlockTag("fossils/gravel")
                .withTagEntries(ObjectArrayList.of(FOSSILS_GRAVEL_VANILLA, FOSSILS_GRAVEL_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Block> FOSSILS_SAND_VANILLA = createBlockTag("fossils/sand/vanilla");
        public static final TagKey<Block> FOSSILS_SAND_CHAOSAWAKENS = createBlockTag("fossils/sand/chaosawakens");
        public static final TagKey<Block> FOSSILS_SAND = createWrappedBlockTag("fossils/sand")
                .withTagEntries(ObjectArrayList.of(FOSSILS_SAND_VANILLA, FOSSILS_SAND_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Block> FOSSILS_SANDSTONE_VANILLA = createBlockTag("fossils/sandstone/vanilla");
        public static final TagKey<Block> FOSSILS_SANDSTONE_CHAOSAWAKENS = createBlockTag("fossils/sandstone/chaosawakens");
        public static final TagKey<Block> FOSSILS_SANDSTONE = createWrappedBlockTag("fossils/sandstone")
                .withTagEntries(ObjectArrayList.of(FOSSILS_SANDSTONE_VANILLA, FOSSILS_SANDSTONE_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Block> FOSSILS_ICE_VANILLA = createBlockTag("fossils/ice/vanilla");
        public static final TagKey<Block> FOSSILS_ICE_CHAOSAWAKENS = createBlockTag("fossils/ice/chaosawakens");
        public static final TagKey<Block> FOSSILS_ICE = createWrappedBlockTag("fossils/ice")
                .withTagEntries(ObjectArrayList.of(FOSSILS_ICE_VANILLA, FOSSILS_ICE_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Block> FOSSILS_NETHERRACK_VANILLA = createBlockTag("fossils/netherrack/vanilla");
        public static final TagKey<Block> FOSSILS_NETHERRACK_CHAOSAWAKENS = createBlockTag("fossils/netherrack/chaosawakens");
        public static final TagKey<Block> FOSSILS_NETHERRACK = createWrappedBlockTag("fossils/netherrack")
                .withTagEntries(ObjectArrayList.of(FOSSILS_NETHERRACK_VANILLA, FOSSILS_NETHERRACK_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Block> FOSSILS_BLACKSTONE_VANILLA = createBlockTag("fossils/blackstone/vanilla");
        public static final TagKey<Block> FOSSILS_BLACKSTONE_CHAOSAWAKENS = createBlockTag("fossils/blackstone/chaosawakens");
        public static final TagKey<Block> FOSSILS_BLACKSTONE = createWrappedBlockTag("fossils/blackstone")
                .withTagEntries(ObjectArrayList.of(FOSSILS_BLACKSTONE_VANILLA, FOSSILS_BLACKSTONE_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Block> FOSSILS_SOUL_SOIL_VANILLA = createBlockTag("fossils/soul_soil/vanilla");
        public static final TagKey<Block> FOSSILS_SOUL_SOIL_CHAOSAWAKENS = createBlockTag("fossils/soul_soil/chaosawakens");
        public static final TagKey<Block> FOSSILS_SOUL_SOIL = createWrappedBlockTag("fossils/soul_soil")
                .withTagEntries(ObjectArrayList.of(FOSSILS_SOUL_SOIL_VANILLA, FOSSILS_SOUL_SOIL_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Block> FOSSILS_END_STONE_VANILLA = createBlockTag("fossils/end_stone/vanilla");
        public static final TagKey<Block> FOSSILS_END_STONE_CHAOSAWAKENS = createBlockTag("fossils/end_stone/chaosawakens");
        public static final TagKey<Block> FOSSILS_END_STONE = createWrappedBlockTag("fossils/end_stone")
                .withTagEntries(ObjectArrayList.of(FOSSILS_END_STONE_VANILLA, FOSSILS_END_STONE_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Block> FOSSILS_KYANITE_VANILLA = createBlockTag("fossils/kyanite/vanilla");
        public static final TagKey<Block> FOSSILS_KYANITE_CHAOSAWAKENS = createBlockTag("fossils/kyanite/chaosawakens");
        public static final TagKey<Block> FOSSILS_KYANITE = createWrappedBlockTag("fossils/kyanite")
                .withTagEntries(ObjectArrayList.of(FOSSILS_KYANITE_VANILLA, FOSSILS_KYANITE_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Block> FOSSILS = createWrappedBlockTag("fossils")
                .withTagEntries(ObjectArrayList.of(FOSSILS_STONE, FOSSILS_GRAVEL, FOSSILS_SAND, FOSSILS_SANDSTONE, FOSSILS_ICE, FOSSILS_NETHERRACK, FOSSILS_BLACKSTONE, FOSSILS_SOUL_SOIL, FOSSILS_END_STONE, FOSSILS_KYANITE))
                .getParentTag();

        public static final TagKey<Block> LEAF_CARPETS = createBlockTag("leaf_carpets");

        private static TagWrapper<Block, TagKey<Block>> createWrappedBlockTag(String name) {
            return TagWrapper.create(createBlockTag(name));
        }

        private static TagKey<Block> createBlockTag(String name) {
            return TagKey.create(Registries.BLOCK, CAConstants.prefix(name));
        }
    }

    @LoadEarly
    public static class CAItemTags {
        public static final TagKey<Item> APPLE_LOGS = createWrappedItemTag("wood/logs/apple_logs")
                .withParentTagEntry(ItemTags.LOGS_THAT_BURN)
                .withEntries(RegistryUtil.getBlocksAsItemSups(CABlocks.APPLE_LOG, CABlocks.APPLE_WOOD, CABlocks.STRIPPED_APPLE_LOG, CABlocks.STRIPPED_APPLE_WOOD))
                .getParentTag();
        public static final TagKey<Item> DEAD_DUPLICATOR_LOGS = createWrappedItemTag("wood/logs/dead_duplicator_logs")
                .withParentTagEntry(ItemTags.LOGS_THAT_BURN)
                .withEntries(RegistryUtil.getBlocksAsItemSups(CABlocks.DEAD_DUPLICATOR_LOG, CABlocks.DEAD_DUPLICATOR_WOOD, CABlocks.STRIPPED_DEAD_DUPLICATOR_LOG, CABlocks.STRIPPED_DEAD_DUPLICATOR_WOOD))
                .getParentTag();
        public static final TagKey<Item> DUPLICATOR_LOGS = createWrappedItemTag("wood/logs/duplicator_logs")
                .withParentTagEntry(ItemTags.LOGS_THAT_BURN)
                .withTagEntry(DEAD_DUPLICATOR_LOGS)
                .withEntries(RegistryUtil.getBlocksAsItemSups(CABlocks.DUPLICATOR_LOG, CABlocks.DUPLICATOR_WOOD, CABlocks.STRIPPED_DUPLICATOR_LOG, CABlocks.STRIPPED_DUPLICATOR_WOOD))
                .getParentTag();
        public static final TagKey<Item> PEACH_LOGS = createWrappedItemTag("wood/logs/peach_logs")
                .withParentTagEntry(ItemTags.LOGS_THAT_BURN)
                .withEntries(RegistryUtil.getBlocksAsItemSups(CABlocks.PEACH_LOG, CABlocks.PEACH_WOOD, CABlocks.STRIPPED_PEACH_LOG, CABlocks.STRIPPED_PEACH_WOOD))
                .getParentTag();
        public static final TagKey<Item> SKYWOOD_LOGS = createWrappedItemTag("wood/logs/skywood_logs")
                .withParentTagEntry(ItemTags.LOGS_THAT_BURN)
                .withEntries(RegistryUtil.getBlocksAsItemSups(CABlocks.SKYWOOD_LOG, CABlocks.SKYWOOD, CABlocks.STRIPPED_SKYWOOD_LOG, CABlocks.STRIPPED_SKYWOOD))
                .getParentTag();
        public static final TagKey<Item> GINKGO_LOGS = createWrappedItemTag("wood/logs/ginkgo_logs")
                .withParentTagEntry(ItemTags.LOGS_THAT_BURN)
                .withEntries(RegistryUtil.getBlocksAsItemSups(CABlocks.GINKGO_LOG, CABlocks.GINKGO_WOOD, CABlocks.STRIPPED_GINKGO_LOG, CABlocks.STRIPPED_GINKGO_WOOD))
                .getParentTag();
        public static final TagKey<Item> MESOZOIC_LOGS = createWrappedItemTag("wood/logs/mesozoic_logs")
                .withParentTagEntry(ItemTags.LOGS_THAT_BURN)
                .withEntries(RegistryUtil.getBlocksAsItemSups(CABlocks.MESOZOIC_LOG, CABlocks.MESOZOIC_WOOD, CABlocks.STRIPPED_MESOZOIC_LOG, CABlocks.STRIPPED_MESOZOIC_WOOD))
                .getParentTag();
        public static final TagKey<Item> DENSEWOOD_LOGS = createWrappedItemTag("wood/logs/densewood_logs")
                .withParentTagEntry(ItemTags.LOGS_THAT_BURN)
                .withEntries(RegistryUtil.getBlocksAsItemSups(CABlocks.DENSEWOOD_LOG, CABlocks.DENSEWOOD, CABlocks.STRIPPED_DENSEWOOD_LOG, CABlocks.STRIPPED_DENSEWOOD))
                .getParentTag();

        public static final TagKey<Item> CRYSTALWOOD_LOGS = createWrappedItemTag("crystalwood_logs")
                .withEntries(RegistryUtil.getBlocksAsItemSups(CABlocks.CRYSTALWOOD_LOG, CABlocks.CRYSTALWOOD, CABlocks.STRIPPED_CRYSTALWOOD_LOG, CABlocks.STRIPPED_CRYSTALWOOD))
                .getParentTag();
        public static final TagKey<Item> CRYSTALWOOD_LEAVES = createItemTag("crystalwood_leaves");
        public static final TagKey<Item> CRYSTALWOOD_SAPLING = createItemTag("crystalwood_saplings");

        public static final TagKey<Item> ULTIMATE_GEAR_HANDLES = createItemTag("ultimate_gear_handles");

        public static final TagKey<Item> FISH = createItemTag("fish");

        public static final TagKey<Item> GEMSTONES = createItemTag("gemstones");
        public static final TagKey<Item> NUGGETS = createItemTag("nuggets");
        public static final TagKey<Item> INGOTS = createItemTag("ingots");
        public static final TagKey<Item> MINERAL_LUMPS = createItemTag("mineral_lumps");

        public static final TagKey<Item> PLANTS = createItemTag("plants");

        public static final TagKey<Item> RUBY_ORES = createItemTag("ruby_ores");

        public static final TagKey<Item> SEEDS = createItemTag("seeds");
        public static final TagKey<Item> LEAF_CARPETS = createItemTag("leaf_carpets");

        public static final TagKey<Item> FOSSILS_STONE_VANILLA = createItemTag("fossils/stone/vanilla");
        public static final TagKey<Item> FOSSILS_STONE_CHAOSAWAKENS = createItemTag("fossils/stone/chaosawakens");
        public static final TagKey<Item> FOSSILS_STONE = createWrappedItemTag("fossils/stone")
                .withTagEntries(ObjectArrayList.of(FOSSILS_STONE_VANILLA, FOSSILS_STONE_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Item> FOSSILS_GRAVEL_VANILLA = createItemTag("fossils/gravel/vanilla");
        public static final TagKey<Item> FOSSILS_GRAVEL_CHAOSAWAKENS = createItemTag("fossils/gravel/chaosawakens");
        public static final TagKey<Item> FOSSILS_GRAVEL = createWrappedItemTag("fossils/gravel")
                .withTagEntries(ObjectArrayList.of(FOSSILS_GRAVEL_VANILLA, FOSSILS_GRAVEL_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Item> FOSSILS_SAND_VANILLA = createItemTag("fossils/sand/vanilla");
        public static final TagKey<Item> FOSSILS_SAND_CHAOSAWAKENS = createItemTag("fossils/sand/chaosawakens");
        public static final TagKey<Item> FOSSILS_SAND = createWrappedItemTag("fossils/sand")
                .withTagEntries(ObjectArrayList.of(FOSSILS_SAND_VANILLA, FOSSILS_SAND_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Item> FOSSILS_SANDSTONE_VANILLA = createItemTag("fossils/sandstone/vanilla");
        public static final TagKey<Item> FOSSILS_SANDSTONE_CHAOSAWAKENS = createItemTag("fossils/sandstone/chaosawakens");
        public static final TagKey<Item> FOSSILS_SANDSTONE = createWrappedItemTag("fossils/sandstone")
                .withTagEntries(ObjectArrayList.of(FOSSILS_SANDSTONE_VANILLA, FOSSILS_SANDSTONE_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Item> FOSSILS_ICE_VANILLA = createItemTag("fossils/ice/vanilla");
        public static final TagKey<Item> FOSSILS_ICE_CHAOSAWAKENS = createItemTag("fossils/ice/chaosawakens");
        public static final TagKey<Item> FOSSILS_ICE = createWrappedItemTag("fossils/ice")
                .withTagEntries(ObjectArrayList.of(FOSSILS_ICE_VANILLA, FOSSILS_ICE_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Item> FOSSILS_NETHERRACK_VANILLA = createItemTag("fossils/netherrack/vanilla");
        public static final TagKey<Item> FOSSILS_NETHERRACK_CHAOSAWAKENS = createItemTag("fossils/netherrack/chaosawakens");
        public static final TagKey<Item> FOSSILS_NETHERRACK = createWrappedItemTag("fossils/netherrack")
                .withTagEntries(ObjectArrayList.of(FOSSILS_NETHERRACK_VANILLA, FOSSILS_NETHERRACK_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Item> FOSSILS_BLACKSTONE_VANILLA = createItemTag("fossils/blackstone/vanilla");
        public static final TagKey<Item> FOSSILS_BLACKSTONE_CHAOSAWAKENS = createItemTag("fossils/blackstone/chaosawakens");
        public static final TagKey<Item> FOSSILS_BLACKSTONE = createWrappedItemTag("fossils/blackstone")
                .withTagEntries(ObjectArrayList.of(FOSSILS_BLACKSTONE_VANILLA, FOSSILS_BLACKSTONE_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Item> FOSSILS_SOUL_SOIL_VANILLA = createItemTag("fossils/soul_soil/vanilla");
        public static final TagKey<Item> FOSSILS_SOUL_SOIL_CHAOSAWAKENS = createItemTag("fossils/soul_soil/chaosawakens");
        public static final TagKey<Item> FOSSILS_SOUL_SOIL = createWrappedItemTag("fossils/soul_soil")
                .withTagEntries(ObjectArrayList.of(FOSSILS_SOUL_SOIL_VANILLA, FOSSILS_SOUL_SOIL_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Item> FOSSILS_END_STONE_VANILLA = createItemTag("fossils/end_stone/vanilla");
        public static final TagKey<Item> FOSSILS_END_STONE_CHAOSAWAKENS = createItemTag("fossils/end_stone/chaosawakens");
        public static final TagKey<Item> FOSSILS_END_STONE = createWrappedItemTag("fossils/end_stone")
                .withTagEntries(ObjectArrayList.of(FOSSILS_END_STONE_VANILLA, FOSSILS_END_STONE_CHAOSAWAKENS))
                .getParentTag();


        public static final TagKey<Item> FOSSILS_KYANITE_VANIILLA = createItemTag("fossils/kyanite/vanilla");
        public static final TagKey<Item> FOSSILS_KYANITE_CHAOSAWAKENS = createItemTag("fossils/kyanite/chaosawakens");
        public static final TagKey<Item> FOSSILS_KYANITE = createWrappedItemTag("fossils/kyanite")
                .withTagEntries(ObjectArrayList.of(FOSSILS_KYANITE_VANIILLA, FOSSILS_KYANITE_CHAOSAWAKENS))
                .getParentTag();

        public static final TagKey<Item> FOSSILS = createWrappedItemTag("fossils")
                .withTagEntries(ObjectArrayList.of(FOSSILS_STONE, FOSSILS_GRAVEL, FOSSILS_SAND, FOSSILS_SANDSTONE, FOSSILS_ICE, FOSSILS_NETHERRACK, FOSSILS_BLACKSTONE, FOSSILS_SOUL_SOIL, FOSSILS_END_STONE, FOSSILS_KYANITE))
                .getParentTag();;

        public static final TagKey<Item> ARCHAEOLOGIST_FOSSILS_COMMON = createItemTag("villager_trades/archaeologist/fossils/common");
        public static final TagKey<Item> ARCHAEOLOGIST_FOSSILS_UNCOMMON = createItemTag("villager_trades/archaeologist/fossils/uncommon");
        public static final TagKey<Item> ARCHAEOLOGIST_FOSSILS_RARE = createItemTag("villager_trades/archaeologist/fossils/rare");
        public static final TagKey<Item> ARCHAEOLOGIST_FOSSILS_EPIC = createItemTag("villager_trades/archaeologist/fossils/epic");
        public static final TagKey<Item> ARCHAEOLOGIST_FOSSILS = createWrappedItemTag("villager_trades/archaeologist/fossils")
                .withTagEntries(ObjectArrayList.of(ARCHAEOLOGIST_FOSSILS_COMMON, ARCHAEOLOGIST_FOSSILS_UNCOMMON, ARCHAEOLOGIST_FOSSILS_RARE, ARCHAEOLOGIST_FOSSILS_EPIC))
                .getParentTag();

        public static final TagKey<Item> ARCHAEOLOGIST_SPAWN_EGGS_COMMON = createItemTag("villager_trades/archaeologist/spawn_eggs/common");
        public static final TagKey<Item> ARCHAEOLOGIST_SPAWN_EGGS_UNCOMMON = createItemTag("villager_trades/archaeologist/spawn_eggs/uncommon");
        public static final TagKey<Item> ARCHAEOLOGIST_SPAWN_EGGS_RARE = createItemTag("villager_trades/archaeologist/spawn_eggs/rare");
        public static final TagKey<Item> ARCHAEOLOGIST_SPAWN_EGGS_EPIC = createItemTag("villager_trades/archaeologist/spawn_eggs/epic");
        public static final TagKey<Item> ARCHAEOLOGIST_SPAWN_EGGS = createWrappedItemTag("villager_trades/archaeologist/spawn_eggs")
                .withTagEntries(ObjectArrayList.of(ARCHAEOLOGIST_SPAWN_EGGS_COMMON, ARCHAEOLOGIST_SPAWN_EGGS_UNCOMMON, ARCHAEOLOGIST_SPAWN_EGGS_RARE, ARCHAEOLOGIST_SPAWN_EGGS_EPIC))
                .getParentTag();

        public static final TagKey<Item> SPAWNER_SPAWN_EGGS = createItemTag("spawner_spawn_eggs");
        
        public static final TagKey<Item> CRYSTAL_FURNACE_FUEL = createItemTag("crystal_furnace_fuel");

        private static TagWrapper<Item, TagKey<Item>> createWrappedItemTag(String name) {
            return TagWrapper.create(createItemTag(name));
        }

        private static TagKey<Item> createItemTag(String name) {
            return TagKey.create(Registries.ITEM, CAConstants.prefix(name));
        }
    }
}
