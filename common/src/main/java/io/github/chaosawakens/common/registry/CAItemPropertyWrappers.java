package io.github.chaosawakens.common.registry;

import io.github.chaosawakens.api.item.ItemPropertyWrapper;
import io.github.chaosawakens.util.ModelUtil;
import io.github.chaosawakens.util.RecipeUtil;
import io.github.chaosawakens.util.RegistryUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class CAItemPropertyWrappers {
    // Basic
    public static final ItemPropertyWrapper BASIC_GENERATED = ItemPropertyWrapper.createTemplate()
            .builder()
            .withCustomModelDefinitions(parentItem -> ObjectArrayList.of(ModelUtil.generated(RegistryUtil.getItemTexture(parentItem))))
            .build();
    public static final ItemPropertyWrapper BASIC_HANDHELD = ItemPropertyWrapper.createTemplate()
            .builder()
            .withCustomModelDefinitions(parentItem -> ObjectArrayList.of(ModelUtil.handheld(RegistryUtil.getItemTexture(parentItem))))
            .build();

    // Food
    public static final ItemPropertyWrapper RAW_FOOD = ItemPropertyWrapper.ofTemplate(BASIC_GENERATED)
            .cachedBuilder()
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_FOOD)
            .build();
    public static final ItemPropertyWrapper COOKED_FOOD = ItemPropertyWrapper.ofTemplate(RAW_FOOD)
            .cachedBuilder()
            .withRecipe(RecipeUtil::cookedFood)
            .build();
}
