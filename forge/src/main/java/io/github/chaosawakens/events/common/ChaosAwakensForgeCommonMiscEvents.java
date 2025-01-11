package io.github.chaosawakens.events.common;

import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.api.item.ItemPropertyWrapper;
import io.github.chaosawakens.api.tag.TagWrapper;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectObjectMutablePair;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ChaosAwakensForgeCommonMiscEvents {
    public static final Object2ObjectOpenHashMap<Block, Object2ObjectOpenHashMap<ToolAction, Function<Supplier<Block>, BlockState>>> CACHED_BLOCK_TOOL_ACTIONS = new Object2ObjectOpenHashMap<>();
    public static final Object2ObjectOpenHashMap<Block, Function<Supplier<Block>, ObjectObjectMutablePair<Predicate<UseOnContext>, Consumer<UseOnContext>>>> CACHED_BLOCK_TILLING_BEHAVIOURS = new Object2ObjectOpenHashMap<>(); // Separate cause hoe tilling is handled differently
    public static final Object2IntOpenHashMap<Item> CACHED_FUEL_TIME = new Object2IntOpenHashMap<>();

    static { // Handle action-mapping once on static initialization (Thanks for not doing it as good as Fabric does it in this case Forge :skull:)
        BlockPropertyWrapper.getMappedBpws().forEach((parentBlockSup, curBpw) -> {
            Function<Supplier<Block>, Supplier<Block>> strippedBlockVariant = curBpw.getBlockStrippingMappingFunc();
            Function<Supplier<Block>, BlockState> flattenedBlockVariant = curBpw.getBlockFlatteningMappingFunc();
            Function<Supplier<Block>, ObjectObjectMutablePair<Predicate<UseOnContext>, Consumer<UseOnContext>>> parentBlockTillingBehaviourPair = curBpw.getBlockTillingMappingFunc();
            Integer cookTime = curBpw.getBlockFuelMappingFunc() == null ? null : Math.abs(curBpw.getBlockFuelMappingFunc().apply(parentBlockSup));

            if (strippedBlockVariant != null) {
                CACHED_BLOCK_TOOL_ACTIONS
                        .computeIfAbsent(parentBlockSup.get(), parentBlock -> new Object2ObjectOpenHashMap<>())
                        .put(ToolActions.AXE_STRIP, parentBlock -> strippedBlockVariant.apply(parentBlock).get().defaultBlockState());
            }

            if (flattenedBlockVariant != null) {
                CACHED_BLOCK_TOOL_ACTIONS
                        .computeIfAbsent(parentBlockSup.get(), parentBlock -> new Object2ObjectOpenHashMap<>())
                        .put(ToolActions.SHOVEL_FLATTEN, flattenedBlockVariant);
            }

            // Gonna hack HoeItem using a quick mixin to totally override Forge's imposed/defaulted behaviour pair lmao
            if (parentBlockTillingBehaviourPair != null) CACHED_BLOCK_TILLING_BEHAVIOURS.put(parentBlockSup.get(), parentBlockTillingBehaviourPair); // Instead of #putIfAbsent cuz we don't mind updates

            if (cookTime != null && cookTime != 0) CACHED_FUEL_TIME.put(parentBlockSup.get().asItem(), (int) cookTime);
        });

        ItemPropertyWrapper.getMappedIpws().forEach((parentItemSup, curIpw) -> {
            Integer cookTime = curIpw.getItemFuelMappingFunc() == null ? null : Math.abs(curIpw.getItemFuelMappingFunc().apply(parentItemSup));

            if (cookTime != null && cookTime != 0) CACHED_FUEL_TIME.put(parentItemSup.get(), (int) cookTime);
        });

        TagWrapper.getCachedTWEntries().stream().filter(curTw -> curTw.getParentTag().get().isFor(Registries.ITEM) && curTw.getCookTime() != 0).forEach(curTw -> {
            for (Holder<Item> itemEntryHolder : BuiltInRegistries.ITEM.getTagOrEmpty((TagKey<Item>) curTw.getParentTag().get())) {
                CACHED_FUEL_TIME.put(itemEntryHolder.value(), Math.abs(curTw.getCookTime()));
            }
        });
    }

    @SubscribeEvent
    public static void onBlockToolModificationEvent(BlockEvent.BlockToolModificationEvent event) { // Handling tool actions (stripping, tilling, flattening) with as little performance overhead and intrusion as possible
        Block targetBlock = event.getState().getBlock(); // Initial state btw
        Object2ObjectOpenHashMap<ToolAction, Function<Supplier<Block>, BlockState>> toolActionMap = CACHED_BLOCK_TOOL_ACTIONS.get(targetBlock);

        if (toolActionMap != null) {
            Function<Supplier<Block>, BlockState> stateTransformationAction = toolActionMap.get(event.getToolAction());

            if (stateTransformationAction != null) {
                BlockState transformedState = stateTransformationAction.apply(() -> targetBlock); // Not using Suppliers#ofInstance since this could probably change (we want some mutability leeway) (whar)

                if (transformedState != null) event.setFinalState(transformedState);
            }
        }
    }

    @SubscribeEvent
    public static void onFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
        Item targetItem = event.getItemStack().getItem();
        int cookTime = CACHED_FUEL_TIME.getOrDefault(targetItem, 0);

        if (cookTime != 0) event.setBurnTime(cookTime);
    }
}