package io.github.chaosawakens;

import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.api.datagen.block.BlockModelDefinition;
import io.github.chaosawakens.api.entity.EntityTypePropertyWrapper;
import io.github.chaosawakens.common.registry.CAClientDataEntries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Needed for some loader-specific optimizations/implementations
 */
public class ChaosAwakensClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        handleClientEntityData();
        handleBlockRenderLayers();

        registerBlockColorProviders();
        registerItemColorProviders();
    }

    private static void handleClientEntityData() {
        CAClientDataEntries.getClientDataEntries().forEach(((curEntry) -> EntityModelLayerRegistry.registerModelLayer(curEntry.get().modelPair().left().get(), () -> curEntry.get().modelPair().right().get())));

        EntityTypePropertyWrapper.getMappedEtpws().forEach((parentEntityTypeSup, curEtpw) -> {
            CAClientDataEntries.getClientDataEntries().stream()
                    .filter(curEntry -> BuiltInRegistries.ENTITY_TYPE.getKey(parentEntityTypeSup.get()).equals(curEntry.get().entityTypeId()))
                    .findFirst()
                    .ifPresent(curEntry -> EntityRendererRegistry.register(parentEntityTypeSup.get(), (ctx) -> curEntry.get().renderFactory().apply(() -> ctx).get()));
        });
    }

    private static void handleBlockRenderLayers() {
        BlockPropertyWrapper.getMappedBpws().forEach((blockSupEntry, curBwp) -> {
            List<BlockModelDefinition> allPresentModels = curBwp.getBlockModelDefinitions();
            Function<Supplier<Block>, List<BlockModelDefinition>> bmdMappingFunc = curBwp.getBMDMappingFunction();

            if (bmdMappingFunc != null) allPresentModels.addAll(bmdMappingFunc.apply(blockSupEntry));

            if (!allPresentModels.isEmpty()) {
                allPresentModels.forEach((curBmd) -> {
                    if (curBmd.getBlockModelRenderType() != null) {
                        String renderTypeName = curBmd.getBlockModelRenderType().getPath();

                        // Gotta do hardcoded rendertype checks like I'm yanderedev because they decided not to have ANY FORM OF ACCESSORS FOR THE RENDERLAYERMAPS. FRICK U FABRIC!!!
                        if (renderTypeName.equals(RenderType.cutout().name)) BlockRenderLayerMap.INSTANCE.putBlock(blockSupEntry.get(), RenderType.cutout());
                        if (renderTypeName.equals(RenderType.cutoutMipped().name)) BlockRenderLayerMap.INSTANCE.putBlock(blockSupEntry.get(), RenderType.cutoutMipped());
                        if (renderTypeName.equals(RenderType.translucent().name)) BlockRenderLayerMap.INSTANCE.putBlock(blockSupEntry.get(), RenderType.translucent());
                        if (renderTypeName.equals(RenderType.translucentMovingBlock().name)) BlockRenderLayerMap.INSTANCE.putBlock(blockSupEntry.get(), RenderType.translucentMovingBlock());
                        if (renderTypeName.equals(RenderType.translucentNoCrumbling().name)) BlockRenderLayerMap.INSTANCE.putBlock(blockSupEntry.get(), RenderType.translucentNoCrumbling());
                        if (renderTypeName.equals(RenderType.glintTranslucent().name)) BlockRenderLayerMap.INSTANCE.putBlock(blockSupEntry.get(), RenderType.glintTranslucent());
                        if (renderTypeName.equals(RenderType.glint().name)) BlockRenderLayerMap.INSTANCE.putBlock(blockSupEntry.get(), RenderType.glint());
                        if (renderTypeName.equals(RenderType.glintDirect().name)) BlockRenderLayerMap.INSTANCE.putBlock(blockSupEntry.get(), RenderType.glintDirect());
                        if (renderTypeName.equals(RenderType.waterMask().name)) BlockRenderLayerMap.INSTANCE.putBlock(blockSupEntry.get(), RenderType.waterMask());
                        if (renderTypeName.equals(RenderType.tripwire().name)) BlockRenderLayerMap.INSTANCE.putBlock(blockSupEntry.get(), RenderType.tripwire());
                    }
                });
            }
        });
    }

    private static void registerBlockColorProviders() {
        BlockPropertyWrapper.getMappedBpws().entrySet().stream().filter(curBwpEntry -> curBwpEntry.getValue().getBlockColorMappingFunc() != null).forEach(curBwpEntry -> {
            Supplier<Block> blockSupEntry = curBwpEntry.getKey();
            BlockColor curMappedBlockColor = curBwpEntry.getValue().getBlockColorMappingFunc().apply(blockSupEntry);

            ColorProviderRegistry.BLOCK.register(curMappedBlockColor, blockSupEntry.get());
        });
    }

    private static void registerItemColorProviders() {
        BlockPropertyWrapper.getMappedBpws().entrySet().stream().filter(curBwpEntry -> curBwpEntry.getValue().getBlockColorMappingFunc() != null).forEach(curBwpEntry -> { // Need to isolate both methods (in terms of blocks), cuz otherwise block is null within the same method's scope when retrieved from the ColorProviderRegistry
            Supplier<Block> blockSupEntry = curBwpEntry.getKey();
            BlockColor curMappedBlockColor = curBwpEntry.getValue().getBlockColorMappingFunc().apply(blockSupEntry);

            if (curMappedBlockColor == null) return; // Failsafe for initial nullity (how)

            ColorProviderRegistry.ITEM.register((curStack, tintIdx) -> curMappedBlockColor.getColor(blockSupEntry.get().defaultBlockState(), null, null, tintIdx), blockSupEntry.get());
        });
    }
}
