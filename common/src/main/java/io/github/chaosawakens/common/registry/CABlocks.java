package io.github.chaosawakens.common.registry;

import com.google.common.collect.ImmutableList;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.common.block.dungeon.general.DungeonGateBlock;
import io.github.chaosawakens.common.block.vegetation.general.FruitableLeavesBlock;
import io.github.chaosawakens.common.block.vegetation.general.LeafCarpetBlock;
import io.github.chaosawakens.util.PredicateUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

@RegistrarEntry
public final class CABlocks {
    private static final ObjectArrayList<Supplier<Block>> BLOCKS = new ObjectArrayList<>();
    private static final ObjectArrayList<Supplier<Item>> BLOCK_ITEMS = new ObjectArrayList<>();

    // Wood Sets
    public static final Supplier<Block> APPLE_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("apple_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_ORANGE : MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> APPLE_WOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("apple_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> STRIPPED_APPLE_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("stripped_apple_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(APPLE_LOG.get()).mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_ORANGE : MapColor.COLOR_ORANGE)))).getParentBlock();
    public static final Supplier<Block> STRIPPED_APPLE_WOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("stripped_apple_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(APPLE_WOOD.get())))).getParentBlock();
    public static final Supplier<Block> APPLE_PLANKS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PLANKS, registerBlock("apple_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> APPLE_STAIRS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_STAIRS, registerBlock("apple_stairs", () -> new StairBlock(APPLE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(APPLE_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> APPLE_SLAB = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_SLAB, registerBlock("apple_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(APPLE_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> APPLE_FENCE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE, registerBlock("apple_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(APPLE_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> APPLE_FENCE_GATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE_GATE, registerBlock("apple_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(APPLE_PLANKS.get()).forceSolidOn(), CAWoodTypes.APPLE))).getParentBlock();
    public static final Supplier<Block> APPLE_DOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_DOOR, registerBlock("apple_door", () -> new DoorBlock(BlockBehaviour.Properties.of().mapColor(APPLE_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY).forceSolidOn(), CABlockSetTypes.APPLE))).getParentBlock();
    public static final Supplier<Block> APPLE_TRAPDOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_TRAPDOOR, registerBlock("apple_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of().mapColor(APPLE_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().forceSolidOn().isValidSpawn(PredicateUtil::neverSpawnOnBlock).ignitedByLava(), CABlockSetTypes.APPLE))).getParentBlock();
    public static final Supplier<Block> APPLE_PRESSURE_PLATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PRESSURE_PLATE, registerBlock("apple_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of().mapColor(APPLE_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).forceSolidOn().noCollission().pushReaction(PushReaction.DESTROY).strength(0.5F), CABlockSetTypes.APPLE))).getParentBlock();
    public static final Supplier<Block> APPLE_BUTTON = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_BUTTON, registerBlock("apple_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), CABlockSetTypes.APPLE, 30, true))).getParentBlock();

    public static final Supplier<Block> CRYSTALWOOD_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("crystalwood_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_ORANGE : MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> CRYSTALWOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("crystalwood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> STRIPPED_CRYSTALWOOD_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("stripped_crystalwood_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(CRYSTALWOOD_LOG.get()).mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_ORANGE : MapColor.COLOR_ORANGE)))).getParentBlock();
    public static final Supplier<Block> STRIPPED_CRYSTALWOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("stripped_crystalwood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(CRYSTALWOOD.get())))).getParentBlock();
    public static final Supplier<Block> CRYSTALWOOD_PLANKS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PLANKS, registerBlock("crystalwood_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> CRYSTALWOOD_STAIRS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_STAIRS, registerBlock("crystalwood_stairs", () -> new StairBlock(CRYSTALWOOD_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(CRYSTALWOOD_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> CRYSTALWOOD_SLAB = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_SLAB, registerBlock("crystalwood_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(CRYSTALWOOD_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> CRYSTALWOOD_FENCE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE, registerBlock("crystalwood_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(CRYSTALWOOD_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> CRYSTALWOOD_FENCE_GATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE_GATE, registerBlock("crystalwood_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(CRYSTALWOOD_PLANKS.get()).forceSolidOn(), CAWoodTypes.CRYSTALWOOD))).getParentBlock();
    public static final Supplier<Block> CRYSTALWOOD_DOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_DOOR, registerBlock("crystalwood_door", () -> new DoorBlock(BlockBehaviour.Properties.of().mapColor(CRYSTALWOOD_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY).forceSolidOn(), CABlockSetTypes.CRYSTALWOOD))).getParentBlock();
    public static final Supplier<Block> CRYSTALWOOD_TRAPDOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_TRAPDOOR, registerBlock("crystalwood_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of().mapColor(CRYSTALWOOD_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().forceSolidOn().isValidSpawn(PredicateUtil::neverSpawnOnBlock).ignitedByLava(), CABlockSetTypes.CRYSTALWOOD))).getParentBlock();
    public static final Supplier<Block> CRYSTALWOOD_PRESSURE_PLATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PRESSURE_PLATE, registerBlock("crystalwood_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of().mapColor(CRYSTALWOOD_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).forceSolidOn().noCollission().pushReaction(PushReaction.DESTROY).strength(0.5F), CABlockSetTypes.CRYSTALWOOD))).getParentBlock();
    public static final Supplier<Block> CRYSTALWOOD_BUTTON = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_BUTTON, registerBlock("crystalwood_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), CABlockSetTypes.CRYSTALWOOD, 30, true))).getParentBlock();

    public static final Supplier<Block> DENSEWOOD_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("densewood_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_ORANGE : MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> DENSEWOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("densewood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> STRIPPED_DENSEWOOD_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("stripped_densewood_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(DENSEWOOD_LOG.get()).mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_ORANGE : MapColor.COLOR_ORANGE)))).getParentBlock();
    public static final Supplier<Block> STRIPPED_DENSEWOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("stripped_densewood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(DENSEWOOD.get())))).getParentBlock();
    public static final Supplier<Block> DENSEWOOD_PLANKS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PLANKS, registerBlock("densewood_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> DENSEWOOD_STAIRS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_STAIRS, registerBlock("densewood_stairs", () -> new StairBlock(DENSEWOOD_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(DENSEWOOD_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> DENSEWOOD_SLAB = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_SLAB, registerBlock("densewood_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(DENSEWOOD_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> DENSEWOOD_FENCE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE, registerBlock("densewood_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(DENSEWOOD_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> DENSEWOOD_FENCE_GATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE_GATE, registerBlock("densewood_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(DENSEWOOD_PLANKS.get()).forceSolidOn(), CAWoodTypes.DENSEWOOD))).getParentBlock();
    public static final Supplier<Block> DENSEWOOD_DOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_DOOR, registerBlock("densewood_door", () -> new DoorBlock(BlockBehaviour.Properties.of().mapColor(DENSEWOOD_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY).forceSolidOn(), CABlockSetTypes.DENSEWOOD))).getParentBlock();
    public static final Supplier<Block> DENSEWOOD_TRAPDOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_TRAPDOOR, registerBlock("densewood_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of().mapColor(DENSEWOOD_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().forceSolidOn().isValidSpawn(PredicateUtil::neverSpawnOnBlock).ignitedByLava(), CABlockSetTypes.DENSEWOOD))).getParentBlock();
    public static final Supplier<Block> DENSEWOOD_PRESSURE_PLATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PRESSURE_PLATE, registerBlock("densewood_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of().mapColor(DENSEWOOD_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).forceSolidOn().noCollission().pushReaction(PushReaction.DESTROY).strength(0.5F), CABlockSetTypes.DENSEWOOD))).getParentBlock();
    public static final Supplier<Block> DENSEWOOD_BUTTON = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_BUTTON, registerBlock("densewood_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), CABlockSetTypes.DENSEWOOD, 30, true))).getParentBlock();

    public static final Supplier<Block> DUPLICATOR_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("duplicator_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor((duplicatorLogBlock) -> duplicatorLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_ORANGE : MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> DUPLICATOR_WOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("duplicator_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> STRIPPED_DUPLICATOR_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("stripped_duplicator_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(DUPLICATOR_LOG.get()).mapColor((duplicatorLogBlock) -> duplicatorLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_BROWN : MapColor.COLOR_BROWN)))).getParentBlock();
    public static final Supplier<Block> STRIPPED_DUPLICATOR_WOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("stripped_duplicator_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(DUPLICATOR_WOOD.get())))).getParentBlock();
    public static final Supplier<Block> DEAD_DUPLICATOR_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("dead_duplicator_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor((duplicatorLogBlock) -> duplicatorLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_BROWN : MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> DEAD_DUPLICATOR_WOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("dead_duplicator_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> STRIPPED_DEAD_DUPLICATOR_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("stripped_dead_duplicator_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(DEAD_DUPLICATOR_LOG.get()).mapColor((duplicatorLogBlock) -> duplicatorLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_ORANGE : MapColor.COLOR_ORANGE)))).getParentBlock();
    public static final Supplier<Block> STRIPPED_DEAD_DUPLICATOR_WOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("stripped_dead_duplicator_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(DEAD_DUPLICATOR_WOOD.get())))).getParentBlock();
    public static final Supplier<Block> DUPLICATOR_PLANKS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PLANKS, registerBlock("duplicator_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> DUPLICATOR_STAIRS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_STAIRS, registerBlock("duplicator_stairs", () -> new StairBlock(DUPLICATOR_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(DUPLICATOR_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> DUPLICATOR_SLAB = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_SLAB, registerBlock("duplicator_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(DUPLICATOR_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> DUPLICATOR_FENCE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE, registerBlock("duplicator_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(DUPLICATOR_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> DUPLICATOR_FENCE_GATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE_GATE, registerBlock("duplicator_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(DUPLICATOR_PLANKS.get()).forceSolidOn(), CAWoodTypes.DUPLICATOR))).getParentBlock();
    public static final Supplier<Block> DUPLICATOR_DOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_DOOR, registerBlock("duplicator_door", () -> new DoorBlock(BlockBehaviour.Properties.of().mapColor(DUPLICATOR_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY).forceSolidOn(), CABlockSetTypes.DUPLICATOR))).getParentBlock();
    public static final Supplier<Block> DUPLICATOR_TRAPDOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_TRAPDOOR, registerBlock("duplicator_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of().mapColor(DUPLICATOR_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().forceSolidOn().isValidSpawn(PredicateUtil::neverSpawnOnBlock).ignitedByLava(), CABlockSetTypes.DUPLICATOR))).getParentBlock();
    public static final Supplier<Block> DUPLICATOR_PRESSURE_PLATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PRESSURE_PLATE, registerBlock("duplicator_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of().mapColor(DUPLICATOR_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).forceSolidOn().noCollission().pushReaction(PushReaction.DESTROY).strength(0.5F), CABlockSetTypes.DUPLICATOR))).getParentBlock();
    public static final Supplier<Block> DUPLICATOR_BUTTON = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_BUTTON, registerBlock("duplicator_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), CABlockSetTypes.DUPLICATOR, 30, true))).getParentBlock();

    public static final Supplier<Block> GINKGO_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("ginkgo_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_ORANGE : MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> GINKGO_WOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("ginkgo_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> STRIPPED_GINKGO_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("stripped_ginkgo_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(GINKGO_LOG.get()).mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_ORANGE : MapColor.COLOR_ORANGE)))).getParentBlock();
    public static final Supplier<Block> STRIPPED_GINKGO_WOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("stripped_ginkgo_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(GINKGO_WOOD.get())))).getParentBlock();
    public static final Supplier<Block> GINKGO_PLANKS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PLANKS, registerBlock("ginkgo_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> GINKGO_STAIRS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_STAIRS, registerBlock("ginkgo_stairs", () -> new StairBlock(GINKGO_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(GINKGO_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> GINKGO_SLAB = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_SLAB, registerBlock("ginkgo_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(GINKGO_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> GINKGO_FENCE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE, registerBlock("ginkgo_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(GINKGO_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> GINKGO_FENCE_GATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE_GATE, registerBlock("ginkgo_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(GINKGO_PLANKS.get()).forceSolidOn(), CAWoodTypes.GINKGO))).getParentBlock();
    public static final Supplier<Block> GINKGO_DOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_DOOR, registerBlock("ginkgo_door", () -> new DoorBlock(BlockBehaviour.Properties.of().mapColor(GINKGO_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY).forceSolidOn(), CABlockSetTypes.GINKGO))).getParentBlock();
    public static final Supplier<Block> GINKGO_TRAPDOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_TRAPDOOR, registerBlock("ginkgo_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of().mapColor(GINKGO_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().forceSolidOn().isValidSpawn(PredicateUtil::neverSpawnOnBlock).ignitedByLava(), CABlockSetTypes.GINKGO))).getParentBlock();
    public static final Supplier<Block> GINKGO_PRESSURE_PLATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PRESSURE_PLATE, registerBlock("ginkgo_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of().mapColor(GINKGO_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).forceSolidOn().noCollission().pushReaction(PushReaction.DESTROY).strength(0.5F), CABlockSetTypes.GINKGO))).getParentBlock();
    public static final Supplier<Block> GINKGO_BUTTON = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_BUTTON, registerBlock("ginkgo_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), CABlockSetTypes.GINKGO, 30, true))).getParentBlock();

    public static final Supplier<Block> MESOZOIC_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("mesozoic_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_ORANGE : MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> MESOZOIC_WOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("mesozoic_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> STRIPPED_MESOZOIC_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("stripped_mesozoic_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(MESOZOIC_LOG.get()).mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_ORANGE : MapColor.COLOR_ORANGE)))).getParentBlock();
    public static final Supplier<Block> STRIPPED_MESOZOIC_WOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("stripped_mesozoic_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(MESOZOIC_WOOD.get())))).getParentBlock();
    public static final Supplier<Block> MESOZOIC_PLANKS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PLANKS, registerBlock("mesozoic_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> MESOZOIC_STAIRS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_STAIRS, registerBlock("mesozoic_stairs", () -> new StairBlock(MESOZOIC_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(MESOZOIC_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> MESOZOIC_SLAB = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_SLAB, registerBlock("mesozoic_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(MESOZOIC_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> MESOZOIC_FENCE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE, registerBlock("mesozoic_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(MESOZOIC_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> MESOZOIC_FENCE_GATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE_GATE, registerBlock("mesozoic_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(MESOZOIC_PLANKS.get()).forceSolidOn(), CAWoodTypes.MESOZOIC))).getParentBlock();
    public static final Supplier<Block> MESOZOIC_DOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_DOOR, registerBlock("mesozoic_door", () -> new DoorBlock(BlockBehaviour.Properties.of().mapColor(MESOZOIC_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY).forceSolidOn(), CABlockSetTypes.MESOZOIC))).getParentBlock();
    public static final Supplier<Block> MESOZOIC_TRAPDOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_TRAPDOOR, registerBlock("mesozoic_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of().mapColor(MESOZOIC_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().forceSolidOn().isValidSpawn(PredicateUtil::neverSpawnOnBlock).ignitedByLava(), CABlockSetTypes.MESOZOIC))).getParentBlock();
    public static final Supplier<Block> MESOZOIC_PRESSURE_PLATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PRESSURE_PLATE, registerBlock("mesozoic_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of().mapColor(MESOZOIC_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).forceSolidOn().noCollission().pushReaction(PushReaction.DESTROY).strength(0.5F), CABlockSetTypes.MESOZOIC))).getParentBlock();
    public static final Supplier<Block> MESOZOIC_BUTTON = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_BUTTON, registerBlock("mesozoic_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), CABlockSetTypes.MESOZOIC, 30, true))).getParentBlock();

    public static final Supplier<Block> PEACH_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("peach_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_ORANGE : MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> PEACH_WOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("peach_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> STRIPPED_PEACH_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("stripped_peach_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(PEACH_LOG.get()).mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.TERRACOTTA_ORANGE : MapColor.COLOR_ORANGE)))).getParentBlock();
    public static final Supplier<Block> STRIPPED_PEACH_WOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("stripped_peach_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(PEACH_WOOD.get())))).getParentBlock();
    public static final Supplier<Block> PEACH_PLANKS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PLANKS, registerBlock("peach_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> PEACH_STAIRS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_STAIRS, registerBlock("peach_stairs", () -> new StairBlock(PEACH_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(PEACH_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> PEACH_SLAB = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_SLAB, registerBlock("peach_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(PEACH_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> PEACH_FENCE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE, registerBlock("peach_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(PEACH_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> PEACH_FENCE_GATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE_GATE, registerBlock("peach_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(PEACH_PLANKS.get()).forceSolidOn(), CAWoodTypes.PEACH))).getParentBlock();
    public static final Supplier<Block> PEACH_DOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_DOOR, registerBlock("peach_door", () -> new DoorBlock(BlockBehaviour.Properties.of().mapColor(PEACH_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY).forceSolidOn(), CABlockSetTypes.PEACH))).getParentBlock();
    public static final Supplier<Block> PEACH_TRAPDOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_TRAPDOOR, registerBlock("peach_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of().mapColor(PEACH_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().forceSolidOn().isValidSpawn(PredicateUtil::neverSpawnOnBlock).ignitedByLava(), CABlockSetTypes.PEACH))).getParentBlock();
    public static final Supplier<Block> PEACH_PRESSURE_PLATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PRESSURE_PLATE, registerBlock("peach_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of().mapColor(PEACH_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).forceSolidOn().noCollission().pushReaction(PushReaction.DESTROY).strength(0.5F), CABlockSetTypes.PEACH))).getParentBlock();
    public static final Supplier<Block> PEACH_BUTTON = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_BUTTON, registerBlock("peach_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), CABlockSetTypes.PEACH, 30, true))).getParentBlock();

    public static final Supplier<Block> SKYWOOD_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("skywood_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_BLUE : MapColor.COLOR_LIGHT_BLUE).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> SKYWOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("skywood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> STRIPPED_SKYWOOD_LOG = BlockPropertyWrapper.of(CABlockPropertyWrappers.LOG, registerBlock("stripped_skywood_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(SKYWOOD_LOG.get()).mapColor((appleLogBlock) -> appleLogBlock.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.COLOR_BLUE : MapColor.COLOR_LIGHT_BLUE)))).getParentBlock();
    public static final Supplier<Block> STRIPPED_SKYWOOD = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOOD, registerBlock("stripped_skywood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(SKYWOOD.get())))).getParentBlock();
    public static final Supplier<Block> SKYWOOD_PLANKS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PLANKS, registerBlock("skywood_planks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).sound(SoundType.WOOD).ignitedByLava()))).getParentBlock();
    public static final Supplier<Block> SKYWOOD_STAIRS = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_STAIRS, registerBlock("skywood_stairs", () -> new StairBlock(SKYWOOD_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(SKYWOOD_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> SKYWOOD_SLAB = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_SLAB, registerBlock("skywood_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(SKYWOOD_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> SKYWOOD_FENCE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE, registerBlock("skywood_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(SKYWOOD_PLANKS.get())))).getParentBlock();
    public static final Supplier<Block> SKYWOOD_FENCE_GATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_FENCE_GATE, registerBlock("skywood_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(SKYWOOD_PLANKS.get()).forceSolidOn(), CAWoodTypes.SKYWOOD))).getParentBlock();
    public static final Supplier<Block> SKYWOOD_DOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_DOOR, registerBlock("skywood_door", () -> new DoorBlock(BlockBehaviour.Properties.of().mapColor(SKYWOOD_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY).forceSolidOn(), CABlockSetTypes.SKYWOOD))).getParentBlock();
    public static final Supplier<Block> SKYWOOD_TRAPDOOR = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_TRAPDOOR, registerBlock("skywood_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of().mapColor(SKYWOOD_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).strength(3.0F).noOcclusion().forceSolidOn().isValidSpawn(PredicateUtil::neverSpawnOnBlock).ignitedByLava(), CABlockSetTypes.SKYWOOD))).getParentBlock();
    public static final Supplier<Block> SKYWOOD_PRESSURE_PLATE = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_PRESSURE_PLATE, registerBlock("skywood_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of().mapColor(SKYWOOD_PLANKS.get().defaultMapColor()).instrument(NoteBlockInstrument.BASS).forceSolidOn().noCollission().pushReaction(PushReaction.DESTROY).strength(0.5F), CABlockSetTypes.SKYWOOD))).getParentBlock();
    public static final Supplier<Block> SKYWOOD_BUTTON = BlockPropertyWrapper.of(CABlockPropertyWrappers.WOODEN_BUTTON, registerBlock("skywood_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), CABlockSetTypes.SKYWOOD, 30, true))).getParentBlock();

    // Leaves
    public static final Supplier<Block> APPLE_LEAVES = BlockPropertyWrapper.of(CABlockPropertyWrappers.FRUITABLE_LEAVES, registerBlock("apple_leaves", () -> new FruitableLeavesBlock(() -> Items.APPLE, BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)))).getParentBlock();
    public static final Supplier<Block> DENSEWOOD_LEAVES = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAVES, registerBlock("densewood_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)))).getParentBlock();
    public static final Supplier<Block> DUPLICATOR_LEAVES = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAVES, registerBlock("duplicator_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)))).getParentBlock();
    public static final Supplier<Block> GINKGO_LEAVES = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAVES, registerBlock("ginkgo_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)))).getParentBlock();
    public static final Supplier<Block> MESOZOIC_LEAVES = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAVES, registerBlock("mesozoic_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)))).getParentBlock();
    public static final Supplier<Block> PEACH_LEAVES = BlockPropertyWrapper.of(CABlockPropertyWrappers.FRUITABLE_LEAVES, registerBlock("peach_leaves", () -> new FruitableLeavesBlock(CAItems.PEACH, BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)))).getParentBlock();
    public static final Supplier<Block> SKYWOOD_LEAVES = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAVES, registerBlock("skywood_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)))).getParentBlock();

    // Leaf Carpets
    public static final Supplier<Block> APPLE_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("apple_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(APPLE_LEAVES.get()).strength(0.1F)))).getParentBlock();
    public static final Supplier<Block> DENSEWOOD_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("densewood_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(DENSEWOOD_LEAVES.get()).strength(0.1F)))).getParentBlock();
    public static final Supplier<Block> DUPLICATOR_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("duplicator_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(DUPLICATOR_LEAVES.get()).strength(0.1F)))).getParentBlock();
    public static final Supplier<Block> GINKGO_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("ginkgo_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(GINKGO_LEAVES.get()).strength(0.1F)))).getParentBlock();
    public static final Supplier<Block> MESOZOIC_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("mesozoic_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(MESOZOIC_LEAVES.get()).strength(0.1F)))).getParentBlock();
    public static final Supplier<Block> PEACH_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("peach_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(PEACH_LEAVES.get()).strength(0.1F)))).getParentBlock();
    public static final Supplier<Block> SKYWOOD_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("skywood_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(SKYWOOD_LEAVES.get()).strength(0.1F)))).getParentBlock();

    public static final Supplier<Block> ACACIA_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("acacia_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(Blocks.ACACIA_LEAVES).strength(0.1F)))).getParentBlock();
    public static final Supplier<Block> BIRCH_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("birch_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_LEAVES).strength(0.1F)))).getParentBlock();
    public static final Supplier<Block> CHERRY_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("cherry_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_LEAVES).strength(0.1F)))).getParentBlock();
    public static final Supplier<Block> DARK_OAK_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("dark_oak_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_LEAVES).strength(0.1F)))).getParentBlock();
    public static final Supplier<Block> MANGROVE_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("mangrove_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(Blocks.MANGROVE_LEAVES).strength(0.1F)))).getParentBlock();
    public static final Supplier<Block> OAK_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("oak_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(0.1F)))).getParentBlock();
    public static final Supplier<Block> SPRUCE_LEAF_CARPET = BlockPropertyWrapper.of(CABlockPropertyWrappers.LEAF_CARPET, registerBlock("spruce_leaf_carpet", () -> new LeafCarpetBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_LEAVES).strength(0.1F)))).getParentBlock();

    // Dungeon Blocks
    public static final Supplier<Block> APPLE_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("apple_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(APPLE_PLANKS.get()))))
            .cachedBuilder()
            .withCustomName("Apple Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> CRYSTALWOOD_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("crystalwood_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(CRYSTALWOOD_PLANKS.get()))))
            .cachedBuilder()
            .withCustomName("Crystalwood Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> DENSEWOOD_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("densewood_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(DENSEWOOD_PLANKS.get()))))
            .cachedBuilder()
            .withCustomName("Densewood Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> DUPLICATOR_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("duplicator_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(DUPLICATOR_PLANKS.get()))))
            .cachedBuilder()
            .withCustomName("Duplicator Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> GINKGO_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("ginkgo_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(GINKGO_PLANKS.get()))))
            .cachedBuilder()
            .withCustomName("Ginkgo Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> MESOZOIC_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("mesozoic_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(MESOZOIC_PLANKS.get()))))
            .cachedBuilder()
            .withCustomName("Mesozoic Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> PEACH_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("peach_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(PEACH_PLANKS.get()))))
            .cachedBuilder()
            .withCustomName("Peach Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> SKYWOOD_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("skywood_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(SKYWOOD_PLANKS.get()))))
            .cachedBuilder()
            .withCustomName("Skywood Gate Block")
            .build()
            .getParentBlock();

    public static final Supplier<Block> ACACIA_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("acacia_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS))))
            .cachedBuilder()
            .withCustomName("Acacia Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> BIRCH_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("birch_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS))))
            .cachedBuilder()
            .withCustomName("Birch Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> CHERRY_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("cherry_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(Blocks.CHERRY_PLANKS))))
            .cachedBuilder()
            .withCustomName("Cherry Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> CRIMSON_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("crimson_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS))))
            .cachedBuilder()
            .withCustomName("Crimson Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> DARK_OAK_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("dark_oak_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS))))
            .cachedBuilder()
            .withCustomName("Dark Oak Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> MANGROVE_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("mangrove_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(Blocks.MANGROVE_PLANKS))))
            .cachedBuilder()
            .withCustomName("Mangrove Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> OAK_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("oak_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS))))
            .cachedBuilder()
            .withCustomName("Oak Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> SPRUCE_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("spruce_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS))))
            .cachedBuilder()
            .withCustomName("Spruce Gate Block")
            .build()
            .getParentBlock();
    public static final Supplier<Block> WARPED_GATE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.GATE_BLOCK, registerBlock("warped_gate_block", () -> new DungeonGateBlock(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS))))
            .cachedBuilder()
            .withCustomName("Warped Gate Block")
            .build()
            .getParentBlock();

    // Mineral Blocks
    public static final Supplier<Block> KUNZITE_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.MATERIAL_BLOCK_DIAMOND, registerBlock("kunzite_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(75.0F, 2400.0F).sound(SoundType.NETHERITE_BLOCK)), new Item.Properties().fireResistant())).getParentBlock();
    public static final Supplier<Block> RUBY_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.MATERIAL_BLOCK_DIAMOND, registerBlock("ruby_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).strength(75.0F, 2400.0F).sound(SoundType.NETHERITE_BLOCK)), new Item.Properties().fireResistant())).getParentBlock();
    public static final Supplier<Block> TITANIUM_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.MATERIAL_BLOCK_DIAMOND, registerBlock("titanium_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(75.0F, 2400.0F).sound(SoundType.NETHERITE_BLOCK)), new Item.Properties().fireResistant())).getParentBlock();
    public static final Supplier<Block> URANIUM_BLOCK = BlockPropertyWrapper.of(CABlockPropertyWrappers.MATERIAL_BLOCK_DIAMOND, registerBlock("uranium_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).strength(60.0F, 1800.0F).sound(SoundType.NETHERITE_BLOCK)), new Item.Properties().fireResistant())).getParentBlock();


    // Standard Ores

    // Deepslate Ores


    private static Supplier<Block> registerBlock(String id, Supplier<Block> blockSup) {
        return registerBlock(id, blockSup, new Item.Properties());
    }

    private static Supplier<Block> registerBlock(String id, Supplier<Block> blockSup, Item.Properties blockItemProperties) {
        Supplier<Block> registeredBlock = registerItemlessBlock(id, blockSup);
        registerBlockItem(id, () -> new BlockItem(registeredBlock.get(), blockItemProperties));
        return registeredBlock;
    }

    private static Supplier<Block> registerBlock(String id, Supplier<Block> blockSup, Supplier<Item> itemSup) {
        Supplier<Block> registeredBlock = registerItemlessBlock(id, blockSup);
        registerBlockItem(id, itemSup);
        return registeredBlock;
    }

    private static Supplier<Block> registerItemlessBlock(String id, Supplier<Block> blockSup) {
        Supplier<Block> registeredBlockSup = CAServices.REGISTRAR.registerObject(CAConstants.prefix(id), blockSup, BuiltInRegistries.BLOCK); // Otherwise reference to the block sup is null cuz it needs to be registered b4hand
        BLOCKS.add(registeredBlockSup);
        return registeredBlockSup;
    }

    private static Supplier<Item> registerBlockItem(String id, Supplier<Item> itemSup) {
        Supplier<Item> registeredItemSup = CAServices.REGISTRAR.registerObject(CAConstants.prefix(id), itemSup, BuiltInRegistries.ITEM); // Otherwise reference to the item sup is null cuz it needs to be registered b4hand
        BLOCK_ITEMS.add(registeredItemSup);
        return registeredItemSup;
    }

    public static Supplier<Block> registerExternalBlock(String id, Supplier<Block> blockSup) {
        return registerBlock(id, blockSup);
    }

    public static ImmutableList<Supplier<Block>> getBlocks() {
        return ImmutableList.copyOf(BLOCKS);
    }

    public static ImmutableList<Supplier<Item>> getBlockItems() {
        return ImmutableList.copyOf(BLOCK_ITEMS);
    }
}
