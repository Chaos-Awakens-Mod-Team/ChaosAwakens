package io.github.chaosawakens.common.registry;

import io.github.chaosawakens.CAConstants;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class CAModelTemplates {
    // Blocks
    public static final ModelTemplate LEAF_CARPET = createTemplate("leaf_carpet", TextureSlot.TEXTURE);
    public static final ModelTemplate LEAF_CARPET_INVENTORY = createTemplate("leaf_carpet_inventory", "_inventory", TextureSlot.TEXTURE);

    private static ModelTemplate createTemplate(String templateModelFileName, TextureSlot... textureSlots) {
        return createTemplate(templateModelFileName, "", textureSlots);
    }

    private static ModelTemplate createTemplate(String templateModelFileName, String modelFileSuffix, TextureSlot... textureSlots) {
        return createTemplate(CAConstants.prefix("block/" + templateModelFileName), modelFileSuffix, textureSlots);
    }

    private static ModelTemplate createTemplate(ResourceLocation templateModelFileLoc, String modelFileSuffix, TextureSlot... textureSlots) {
        return new ModelTemplate(Optional.of(templateModelFileLoc), Optional.of(modelFileSuffix), textureSlots);
    }

    public static ModelTemplate of(ResourceLocation modelLoc) {
        return new ModelTemplate(Optional.of(modelLoc), Optional.of(""));
    }
}
