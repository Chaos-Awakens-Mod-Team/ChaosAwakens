package io.github.chaosawakens;

import io.github.chaosawakens.datagen.*;
import io.github.chaosawakens.events.common.ChaosAwakensForgeCommonMiscEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.concurrent.CompletableFuture;

@Mod(CAConstants.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChaosAwakensForge {

    public ChaosAwakensForge() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        ChaosAwakens.setup();

        forgeBus.register(ChaosAwakensForgeCommonMiscEvents.class); // Can't use auto-annotation registration since it'd initialize the class before everything else (bruh)
    }

    @SubscribeEvent
    public static void onFMLCommonSetupEvent(FMLCommonSetupEvent event) {
        ChaosAwakens.deferredSetup();

        event.enqueueWork(ForgeVanillaIntegration::handleVanillaIntegration);
    }

    @SubscribeEvent
    public static void onGatherDataEvent(GatherDataEvent event) {
        DataGenerator datagen = event.getGenerator();
        PackOutput datagenPackOutput = datagen.getPackOutput();
        final ExistingFileHelper curFileHelper = event.getExistingFileHelper();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Resources
        datagen.addProvider(event.includeClient(), new CALanguageProvider(datagenPackOutput));

        datagen.addProvider(event.includeClient(), new CAItemModelProvider(datagenPackOutput, curFileHelper));
        datagen.addProvider(event.includeClient(), new CABlockModelProvider(datagenPackOutput, curFileHelper));
        datagen.addProvider(event.includeClient(), new CABlockStateProvider(datagenPackOutput, curFileHelper));

        // Data
        CABlockTagsProvider cachedBlockTagsProvider = new CABlockTagsProvider(datagenPackOutput, lookupProvider, curFileHelper);

        datagen.addProvider(event.includeServer(), new CALootTableProvider(datagenPackOutput));

        datagen.addProvider(event.includeServer(), cachedBlockTagsProvider);
        datagen.addProvider(event.includeServer(), new CAItemTagsProvider(datagenPackOutput, lookupProvider, cachedBlockTagsProvider.contentsGetter(), curFileHelper));

        datagen.addProvider(event.includeServer(), new CARecipeProvider(datagenPackOutput));

        datagen.addProvider(event.includeServer(), new CADatapackRegistryProvider(datagenPackOutput, lookupProvider));
    }
}