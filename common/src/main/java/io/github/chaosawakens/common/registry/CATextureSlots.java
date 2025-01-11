package io.github.chaosawakens.common.registry;

import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

public class CATextureSlots {
    public static final TextureSlot OVERLAY = TextureSlot.create("overlay");

    public static TextureMapping cube(ResourceLocation particleLoc, ResourceLocation bottomLoc, ResourceLocation topLoc, ResourceLocation sideLoc) {
        return new TextureMapping().put(TextureSlot.PARTICLE, particleLoc).put(TextureSlot.BOTTOM, bottomLoc).put(TextureSlot.TOP, topLoc).put(TextureSlot.SIDE, sideLoc);
    }

    public static TextureMapping grassBlock(ResourceLocation particleTexture, ResourceLocation bottomTexture, ResourceLocation topTexture, ResourceLocation sideTexture, ResourceLocation overlayTexture) {
        return cube(particleTexture, bottomTexture, topTexture, sideTexture).put(OVERLAY, overlayTexture);
    }

    public static TextureMapping farmland(ResourceLocation farmlandTopTexture, ResourceLocation dirtTexture) {
        return new TextureMapping().put(TextureSlot.TOP, farmlandTopTexture).put(TextureSlot.DIRT, dirtTexture);
    }
}
