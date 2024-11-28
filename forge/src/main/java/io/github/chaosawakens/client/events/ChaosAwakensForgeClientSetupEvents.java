package io.github.chaosawakens.client.events;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = CAConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChaosAwakensForgeClientSetupEvents {

    @SubscribeEvent
    public static void onRegisterBlockColorHandlersEvent(RegisterColorHandlersEvent.Block event) {
        BlockPropertyWrapper.getMappedBpws().entrySet().stream().filter(curBwpEntry -> curBwpEntry.getValue().getBlockColorMappingFunc() != null).forEach(curBwpEntry -> {
            Supplier<Block> blockSupEntry = curBwpEntry.getKey();
            BlockColor curMappedBlockColor = curBwpEntry.getValue().getBlockColorMappingFunc().apply(blockSupEntry);

            event.register(curMappedBlockColor, blockSupEntry.get());
        });
    }

    @SubscribeEvent
    public static void onRegisterBlockColorHandlersEvent(RegisterColorHandlersEvent.Item event) {
        BlockPropertyWrapper.getMappedBpws().entrySet().stream().filter(curBwpEntry -> curBwpEntry.getValue().getBlockColorMappingFunc() != null).forEach(curBwpEntry -> {
            Supplier<Block> blockSupEntry = curBwpEntry.getKey();

            event.register((curStack, tintIdx) -> event.getBlockColors().getColor(blockSupEntry.get().defaultBlockState(), null, null, tintIdx), blockSupEntry.get());
        });
    }
}
