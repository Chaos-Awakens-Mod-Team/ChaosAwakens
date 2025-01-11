package io.github.chaosawakens.datagen;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.block.standard.BlockPropertyWrapper;
import io.github.chaosawakens.api.item.ItemPropertyWrapper;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CARecipeProvider extends RecipeProvider {

    public CARecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        if (!BlockPropertyWrapper.getMappedBpws().isEmpty()) {
            BlockPropertyWrapper.getMappedBpws().forEach((blockSupEntry, bpwEntry) -> {
                Function<Consumer<FinishedRecipe>, Consumer<Supplier<Block>>> mappedRecipe = bpwEntry.getRecipeMappingFunction();

                if (mappedRecipe != null) {
                    CAConstants.LOGGER.debug("[Generating Block Recipe]: {}", blockSupEntry.get().getDescriptionId());

                    mappedRecipe.apply(pWriter).accept(blockSupEntry);
                }
            });
        }

        if (!ItemPropertyWrapper.getMappedIpws().isEmpty()) {
            ItemPropertyWrapper.getMappedIpws().forEach((itemSupEntry, ipwEntry) -> {
                Function<Consumer<FinishedRecipe>, Consumer<Supplier<Item>>> mappedRecipe = ipwEntry.getRecipeMappingFunction();

                if (mappedRecipe != null) {
                    CAConstants.LOGGER.debug("[Generating Item Recipe]: {}", itemSupEntry.get().getDescriptionId());

                    mappedRecipe.apply(pWriter).accept(itemSupEntry);
                }
            });
        }
    }
}
