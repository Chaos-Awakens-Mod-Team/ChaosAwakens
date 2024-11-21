package io.github.chaosawakens.common.registry;

import com.google.common.collect.ImmutableList;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.util.PredicateUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;
import java.util.stream.Collectors;

@RegistrarEntry
public final class CACreativeModeTabs {
    private static final ObjectArrayList<Supplier<CreativeModeTab>> CREATIVE_MODE_TABS = new ObjectArrayList<>();

    public static final Supplier<CreativeModeTab> CHAOS_AWAKENS_BLOCKS = registerCreativeModeTab("chaos_awakens_blocks", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("creativemodetab.chaosawakens.chaos_awakens_blocks"))
            .icon(() -> CABlocks.TITANIUM_BLOCK.get().asItem().getDefaultInstance())
            .displayItems((displayParams, curOutput) -> curOutput.acceptAll(CABlocks.getBlocks()
                    .stream()
                    .map(Supplier::get)
                    .map(Block::asItem)
                    .map(Item::getDefaultInstance)
                    .filter(PredicateUtil::isRegularBlock)
                    .collect(Collectors.toCollection(ObjectArrayList::new))))
            .build());
    public static final Supplier<CreativeModeTab> CHAOS_AWAKENS_ITEMS = registerCreativeModeTab("chaos_awakens_items", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("creativemodetab.chaosawakens.chaos_awakens_items"))
            .icon(() -> CAItems.URANIUM_INGOT.get().asItem().getDefaultInstance())
            .displayItems((displayParams, curOutput) -> curOutput.acceptAll(CAItems.getItems()
                    .stream()
                    .map(Supplier::get)
                    .map(Item::getDefaultInstance)
                    .filter(PredicateUtil::isRegularItem)
                    .collect(Collectors.toCollection(ObjectArrayList::new))))
            .build());
    public static final Supplier<CreativeModeTab> CHAOS_AWAKENS_FOOD = registerCreativeModeTab("chaos_awakens_food", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("creativemodetab.chaosawakens.chaos_awakens_food"))
            .icon(() -> CAItems.CHEESE.get().asItem().getDefaultInstance())
            .displayItems((displayParams, curOutput) -> curOutput.acceptAll(CAItems.getItems()
                    .stream()
                    .map(Supplier::get)
                    .map(Item::getDefaultInstance)
                    .filter(PredicateUtil::isFood)
                    .collect(Collectors.toCollection(ObjectArrayList::new))))
            .build());


    private static Supplier<CreativeModeTab> registerCreativeModeTab(String id, Supplier<CreativeModeTab> creativeModeTabSup) {
        Supplier<CreativeModeTab> registeredCreativeModeTabSup = CAServices.REGISTRAR.registerObject(CAConstants.prefix(id), creativeModeTabSup, BuiltInRegistries.CREATIVE_MODE_TAB); // Otherwise reference to the creative mode tab sup is null cuz it needs to be registered b4hand
        CREATIVE_MODE_TABS.add(registeredCreativeModeTabSup);
        return registeredCreativeModeTabSup;
    }

    public static ImmutableList<Supplier<CreativeModeTab>> getCreativeModeTabs() {
        return ImmutableList.copyOf(CREATIVE_MODE_TABS);
    }
}
