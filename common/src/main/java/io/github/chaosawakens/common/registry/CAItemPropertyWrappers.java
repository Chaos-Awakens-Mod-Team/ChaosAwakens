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
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_ITEMS)
            .withCustomModelDefinitions(parentItem -> ObjectArrayList.of(ModelUtil.generated(RegistryUtil.getItemTexture(parentItem))))
            .build();
    public static final ItemPropertyWrapper BASIC_HANDHELD = ItemPropertyWrapper.createTemplate()
            .builder()
            .withCustomModelDefinitions(parentItem -> ObjectArrayList.of(ModelUtil.handheld(RegistryUtil.getItemTexture(parentItem))))
            .build();

    // Food
    public static final ItemPropertyWrapper RAW_FOOD = ItemPropertyWrapper.createTemplate()
            .builder()
            .withParentCreativeModeTab(CACreativeModeTabs.CHAOS_AWAKENS_FOOD)
            .withCustomModelDefinitions(parentItem -> ObjectArrayList.of(ModelUtil.generated(RegistryUtil.getItemTexture(parentItem))))
            .build();
    public static final ItemPropertyWrapper COOKED_FOOD = ItemPropertyWrapper.ofTemplate(RAW_FOOD)
            .cachedBuilder()
            .withRecipe(RecipeUtil::cookedFood)
            .build();

    // Mineral
    public static final ItemPropertyWrapper STANDARD_INGOT = ItemPropertyWrapper.ofTemplate(BASIC_GENERATED)
            .cachedBuilder()
            .withRecipe(RecipeUtil::standardIngot)
            .build();

    public static final ItemPropertyWrapper STANDARD_NUGGET = ItemPropertyWrapper.ofTemplate(BASIC_GENERATED)
            .cachedBuilder()
            .withRecipe(RecipeUtil::standardNugget)
            .build();

    public static final ItemPropertyWrapper STANDARD_MINERAL = ItemPropertyWrapper.ofTemplate(BASIC_GENERATED)
            .cachedBuilder()
            .withRecipe(RecipeUtil::standardMineral)
            .build();

    // Component
    public static final ItemPropertyWrapper BASIC_COMPONENT = ItemPropertyWrapper.ofTemplate(BASIC_GENERATED)
            .cachedBuilder()
            .withRecipe(RecipeUtil::componentMaterial)
            .build();
}
