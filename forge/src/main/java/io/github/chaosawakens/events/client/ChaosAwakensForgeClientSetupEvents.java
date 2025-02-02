package io.github.chaosawakens.events.client;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.api.entity.EntityTypePropertyWrapper;
import io.github.chaosawakens.common.registry.CAClientDataEntries;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = CAConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChaosAwakensForgeClientSetupEvents {

    @SubscribeEvent
    public static void onRegisterEntityRenderersEvent(EntityRenderersEvent.RegisterRenderers event) {
        EntityTypePropertyWrapper.getMappedEtpws().forEach((parentEntityTypeSup, curEtpw) -> {
            CAClientDataEntries.getClientDataEntries().stream()
                    .filter(curEntry -> ForgeRegistries.ENTITY_TYPES.getKey(parentEntityTypeSup.get()) != null && Objects.equals(ForgeRegistries.ENTITY_TYPES.getKey(parentEntityTypeSup.get()), curEntry.get().entityTypeId()))
                    .findFirst()
                    .ifPresent(curEntry -> event.registerEntityRenderer(parentEntityTypeSup.get(), (ctx) -> curEntry.get().renderFactory().apply(() -> ctx).get()));
        });
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitionsEvent(EntityRenderersEvent.RegisterLayerDefinitions event) {
        CAClientDataEntries.getClientDataEntries().forEach(((curEntry) -> event.registerLayerDefinition(curEntry.get().modelPair().left().get(), () -> curEntry.get().modelPair().right().get())));
    }

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
