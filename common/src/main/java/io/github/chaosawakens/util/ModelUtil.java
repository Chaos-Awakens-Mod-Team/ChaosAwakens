package io.github.chaosawakens.util;

import com.google.common.collect.Sets;
import io.github.chaosawakens.api.datagen.block.BlockModelDefinition;
import io.github.chaosawakens.api.datagen.block.BlockStateDefinition;
import io.github.chaosawakens.api.datagen.item.ItemModelDefinition;
import io.github.chaosawakens.common.registry.CABlockStateProperties;
import io.github.chaosawakens.common.registry.CAModelTemplates;
import io.github.chaosawakens.common.registry.CATextureSlots;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Utility class containing helper methods (generally also found in datagen classes) aimed at reducing boilerplate code by natively providing
 * common {@link BlockModelDefinition} and {@link BlockStateDefinition} patterns.
 */
public final class ModelUtil {

    private ModelUtil() {
        throw new IllegalAccessError("Attempted to construct Utility Class!");
    }

    /**
     * Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_ALL} template.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#ALL} -> {@code sixSideBlockTexture}</li>
     * </ul>
     *
     * @param sixSideBlockTexture The {@link ResourceLocation} representing the texture of every side of a standard cube block.
     *                            Points to the {@code "block/"} path by default (applies to all BMD methods in this class. If they need item locations,
     *                            those point to the {@code "item/"} path by default, with some automatically pointing towards {@code "item/block_items/"}. Only applies
     *                            if the path towards those textures is not auto-located by {@link RegistryUtil#getBlockTexture(Supplier)} or equivalent methods).
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_ALL} template.
     *
     * @see #simpleBlock(Supplier)
     */
    public static BlockModelDefinition cubeAll(ResourceLocation sixSideBlockTexture) {
        return BlockModelDefinition.of(ModelTemplates.CUBE_ALL)
                .withTextureMapping(TextureMapping.cube(RegistryUtil.pickBlockPrefix(sixSideBlockTexture)));
    }

    /**
     * Creates a {@link BlockStateDefinition}, using {@link MultiVariantGenerator} with the {@link VariantProperties#MODEL} property set to the supplied {@linkplain Block Block's}
     * default model location.
     * <p>
     * <h3>Variants</h3>
     * <ul>
     *  <li>{@link VariantProperties#MODEL} -> {@link ModelLocationUtils#getModelLocation(Block)}</li>
     * </ul>
     *
     * @param targetBlock The {@linkplain Block Block} to use as the base for the {@link BlockStateDefinition}.
     *
     * @return A new {@link BlockStateDefinition} with a {@code simpleBlock} template.
     *
     * @see #cubeAll(ResourceLocation)
     * @see #cubeBottomTop(ResourceLocation)
     * @see #cubeBottomTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #cubeBottomTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #cross(ResourceLocation)
     * @see #crossCutout(ResourceLocation)
     * @see #sign(ResourceLocation, ResourceLocation)
     */
    public static BlockStateDefinition simpleBlock(Supplier<Block> targetBlock) {
        return BlockStateDefinition.of(targetBlock)
                .withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get(), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(targetBlock.get()))));
    }

    /**
     * Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_BOTTOM_TOP} template.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code sideTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code bottomTexture}</li>
     *  <li>{@link TextureSlot#TOP} -> {@code topTexture}</li>
     * </ul>
     *
     * @param sideTexture The {@link ResourceLocation} representing the texture of the 4 horizontal faces of a standard cube block (N, S, E, W).
     * @param bottomTexture The {@link ResourceLocation} representing the texture of the bottom vertical face of a standard cube block (D).
     * @param topTexture The {@link ResourceLocation} representing the texture of the top vertical face of a standard cube block (U).
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_BOTTOM_TOP} template.
     *
     * @see #simpleBlock(Supplier)
     * @see #cubeBottomTop(ResourceLocation)
     * @see #cubeBottomTop(ResourceLocation, boolean)
     */
    public static BlockModelDefinition cubeBottomTop(ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
        return BlockModelDefinition.of(ModelTemplates.CUBE_BOTTOM_TOP)
                .withTextureMapping(new TextureMapping()
                        .put(TextureSlot.SIDE, RegistryUtil.pickBlockPrefix(sideTexture))
                        .put(TextureSlot.BOTTOM, RegistryUtil.pickBlockPrefix(bottomTexture))
                        .put(TextureSlot.TOP, RegistryUtil.pickBlockPrefix(topTexture)));
    }

    /**
     * Overloaded variant of {@link #cubeBottomTop(ResourceLocation, ResourceLocation, ResourceLocation)}. Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_BOTTOM_TOP} template, where
     * the {@code bottomTexture} and {@code topTexture} are suffixed appropriately based on the {@code baseTexture}.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code baseTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code baseTexture + "_bottom"}</li>
     *  <li>{@link TextureSlot#TOP} -> {@code baseTexture + "_top"}</li>
     * </ul>
     *
     * @param baseTexture The {@link ResourceLocation} representing the texture of the 4 horizontal faces of a standard cube block (N, S, E, W), additionally representing the 2 other vertical
     *                      faces of a standard cube block (D, U) via pre-determining the bottom and top textures with the appropriate suffixes.
     * @param useBaseTexture Whether the {@code baseTexture} should be used for every required {@link TextureSlot}.
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_BOTTOM_TOP} template.
     *
     * @see #cubeBottomTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #cubeBottomTop(ResourceLocation)
     * @see #simpleBlock(Supplier)
     */
    public static BlockModelDefinition cubeBottomTop(ResourceLocation baseTexture, boolean useBaseTexture) {
        return cubeBottomTop(baseTexture, useBaseTexture ? baseTexture : baseTexture.withSuffix("_bottom"), useBaseTexture ? baseTexture : baseTexture.withSuffix("_top"));
    }

    /**
     * Overloaded variant of {@link #cubeBottomTop(ResourceLocation, boolean)}. Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_BOTTOM_TOP} template, where
     * the {@code bottomTexture} and {@code topTexture} are suffixed appropriately based on the {@code baseTexture}. Uses the provided {@code baseTexture} for every required {@link TextureSlot}.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code baseTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code baseTexture}</li>
     *  <li>{@link TextureSlot#TOP} -> {@code baseTexture}</li>
     * </ul>
     *
     * @param baseTexture The {@link ResourceLocation} representing the texture of the 4 horizontal faces of a standard cube block (N, S, E, W), additionally representing the 2 other vertical
     *                      faces of a standard cube block (D, U) via pre-determining the bottom and top textures with the appropriate suffixes.
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_BOTTOM_TOP} template.
     *
     * @see #cubeBottomTop(ResourceLocation, boolean)
     * @see #cubeBottomTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #simpleBlock(Supplier)
     */
    public static BlockModelDefinition cubeBottomTop(ResourceLocation baseTexture) {
        return cubeBottomTop(baseTexture, true);
    }

    /**
     * Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#SLAB_BOTTOM} template.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code sideTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code bottomTexture}</li>
     *  <li>{@link TextureSlot#TOP} -> {@code topTexture}</li>
     * </ul>
     *
     * @param sideTexture The {@link ResourceLocation} representing the texture of the 4 horizontal faces of a standard slab block (N, S, E, W).
     * @param bottomTexture The {@link ResourceLocation} representing the texture of the bottom vertical face of a standard slab block (D).
     * @param topTexture The {@link ResourceLocation} representing the texture of the top vertical face of a standard slab block (U).
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#SLAB_BOTTOM} template.
     *
     * @see #slabBottom(ResourceLocation, boolean)
     * @see #slabBottom(ResourceLocation)
     * @see #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabTop(ResourceLocation, boolean)
     * @see #slabTop(ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation)
     * @see #slab(Supplier, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(Supplier, ResourceLocation)
     * @see #woodenSlab(Supplier)
     */
    public static BlockModelDefinition slabBottom(ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
        return BlockModelDefinition.of(ModelTemplates.SLAB_BOTTOM)
                .withTextureMapping(new TextureMapping()
                        .put(TextureSlot.SIDE, RegistryUtil.pickBlockPrefix(sideTexture))
                        .put(TextureSlot.BOTTOM, RegistryUtil.pickBlockPrefix(bottomTexture))
                        .put(TextureSlot.TOP, RegistryUtil.pickBlockPrefix(topTexture)));
    }

    /**
     * Overloaded variant of {@link #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)}, with the top and bottom textures determined based on {@code useBaseTexture}.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code baseTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code useBaseTexture ? baseTexture : baseTexture + "_bottom"}</li>
     *  <li>{@link TextureSlot#TOP} -> {@code useBaseTexture ? baseTexture : baseTexture + "_top"}</li>
     * </ul>
     *
     * @param baseTexture The {@link ResourceLocation} representing the texture of the 4 horizontal faces of a standard slab block (N, S, E, W), or all sides if {@code useBaseTexture} is {@code false}.
     * @param useBaseTexture Whether the provided {@code baseTexture} should be used for every required {@link TextureSlot}. If set to {@code false}, the top and bottom textures will use the
     *                       provided {@code baseTexture} suffixed with {@code "_top"} and {@code "_bottom"} respectively.
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#SLAB_BOTTOM} template, with the top and bottom textures determined based on provided {@code baseTexture}.
     *
     * @see #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation)
     * @see #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabTop(ResourceLocation, boolean)
     * @see #slabTop(ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation)
     * @see #slab(Supplier, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(Supplier, ResourceLocation)
     * @see #woodenSlab(Supplier)
     */
    public static BlockModelDefinition slabBottom(ResourceLocation baseTexture, boolean useBaseTexture) {
        return slabBottom(baseTexture, useBaseTexture ? baseTexture : baseTexture.withSuffix("_bottom"), useBaseTexture ? baseTexture : baseTexture.withSuffix("_top"));
    }

    /**
     * Overloaded variant of {@link #slabBottom(ResourceLocation, boolean)}, with the top and bottom textures set to use the provided {@code baseTexture}.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code baseTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code baseTexture}</li>
     *  <li>{@link TextureSlot#TOP} -> {@code baseTexture}</li>
     * </ul>
     *
     * @param baseTexture The {@link ResourceLocation} representing the texture of all sides of a standard slab block.
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#SLAB_BOTTOM} template, with the top and bottom textures set to the provided {@code baseTexture}.
     *
     * @see #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation, boolean)
     * @see #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabTop(ResourceLocation, boolean)
     * @see #slabTop(ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation)
     * @see #slab(Supplier, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(Supplier, ResourceLocation)
     * @see #woodenSlab(Supplier)
     */
    public static BlockModelDefinition slabBottom(ResourceLocation baseTexture) {
        return slabBottom(baseTexture, true);
    }

    /**
     * Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#SLAB_TOP} template.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code sideTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code bottomTexture}</li>
     *  <li>{@link TextureSlot#TOP} -> {@code topTexture}</li>
     * </ul>
     *
     * @param sideTexture The {@link ResourceLocation} representing the texture of the 4 horizontal faces of a standard slab block (N, S, E, W).
     * @param bottomTexture The {@link ResourceLocation} representing the texture of the bottom vertical face of a standard slab block (D).
     * @param topTexture The {@link ResourceLocation} representing the texture of the top vertical face of a standard slab block (U).
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#SLAB_TOP} template.
     *
     * @see #slabTop(ResourceLocation, boolean)
     * @see #slabTop(ResourceLocation)
     * @see #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation, boolean)
     * @see #slabBottom(ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation)
     * @see #slab(Supplier, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(Supplier, ResourceLocation)
     * @see #woodenSlab(Supplier)
     */
    public static BlockModelDefinition slabTop(ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
        return BlockModelDefinition.of(ModelTemplates.SLAB_TOP)
                .withTextureMapping(new TextureMapping()
                        .put(TextureSlot.SIDE, RegistryUtil.pickBlockPrefix(sideTexture))
                        .put(TextureSlot.BOTTOM, RegistryUtil.pickBlockPrefix(bottomTexture))
                        .put(TextureSlot.TOP, RegistryUtil.pickBlockPrefix(topTexture)));
    }

    /**
     * Overloaded variant of {@link #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)}, with the top and bottom textures determined based on {@code useBaseTexture}.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code baseTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code useBaseTexture ? baseTexture : baseTexture + "_bottom"}</li>
     *  <li>{@link TextureSlot#TOP} -> {@code useBaseTexture ? baseTexture : baseTexture + "_top"}</li>
     * </ul>
     *
     * @param baseTexture The {@link ResourceLocation} representing the texture of the 4 horizontal faces of a standard slab block (N, S, E, W), or all sides if {@code useBaseTexture} is {@code false}.
     * @param useBaseTexture Whether the provided {@code baseTexture} should be used for every required {@link TextureSlot}. If set to {@code false}, the top and bottom textures will use the
     *                       provided {@code baseTexture} suffixed with {@code "_top"} and {@code "_bottom"} respectively.
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#SLAB_TOP} template, with the top and bottom textures determined based on provided {@code baseTexture}.
     *
     * @see #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabTop(ResourceLocation)
     * @see #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation, boolean)
     * @see #slabBottom(ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation)
     * @see #slab(Supplier, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(Supplier, ResourceLocation)
     * @see #woodenSlab(Supplier)
     */
    public static BlockModelDefinition slabTop(ResourceLocation baseTexture, boolean useBaseTexture) {
        return slabTop(baseTexture, useBaseTexture ? baseTexture : baseTexture.withSuffix("_bottom"), useBaseTexture ? baseTexture : baseTexture.withSuffix("_top"));
    }

    /**
     * Overloaded variant of {@link #slabTop(ResourceLocation, boolean)}, with the top and bottom textures set to use the provided {@code baseTexture}.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code baseTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code baseTexture}</li>
     *  <li>{@link TextureSlot#TOP} -> {@code baseTexture}</li>
     * </ul>
     *
     * @param baseTexture The {@link ResourceLocation} representing the texture of all sides of a standard slab block.
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#SLAB_TOP} template, with the top and bottom textures set to the provided {@code baseTexture}.
     *
     * @see #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabTop(ResourceLocation, boolean)
     * @see #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation, boolean)
     * @see #slabBottom(ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation)
     * @see #slab(Supplier, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(Supplier, ResourceLocation)
     * @see #woodenSlab(Supplier)
     */
    public static BlockModelDefinition slabTop(ResourceLocation baseTexture) {
        return slabTop(baseTexture, true);
    }

    /**
     * Creates an {@link ObjectArrayList} of {@linkplain BlockModelDefinition BlockModelDefinitions} with the {@link ModelTemplates#SLAB_BOTTOM} and {@link ModelTemplates#SLAB_TOP} templates, with all required
     * {@linkplain TextureSlot TextureSlots} mapped out to the provided {@link ResourceLocation} parameters.
     * <p>
     * <h3>Models</h3>
     * <ul>
     *  <li>{@link ModelTemplates#SLAB_BOTTOM} -> <ul>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code bottomBottomTexture}</li>
     *      <li>{@link TextureSlot#TOP} -> {@code bottomTopTexture}</li>
     *      <li>{@link TextureSlot#SIDE} -> {@code bottomSideTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#SLAB_TOP} -> <ul>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code topBottomTexture}</li>
     *      <li>{@link TextureSlot#TOP} -> {@code topTopTexture}</li>
     *      <li>{@link TextureSlot#SIDE} -> {@code topSideTexture}</li>
     *  </ul></li>
     * </ul>
     *
     * @param bottomSideTexture The {@link ResourceLocation} representing the side texture of the bottom slab block model.
     * @param bottomBottomTexture The {@link ResourceLocation} representing the bottom texture of the bottom slab block model.
     * @param bottomTopTexture The {@link ResourceLocation} representing the top texture of the bottom slab block model.
     * @param topSideTexture The {@link ResourceLocation} representing the side texture of the top slab block model.
     * @param topBottomTexture The {@link ResourceLocation} representing the bottom texture of the top slab block model.
     * @param topTopTexture The {@link ResourceLocation} representing the top texture of the top slab block model.
     *
     * @return An {@link ObjectArrayList} of {@linkplain BlockModelDefinition BlockModelDefinitions} with the {@link ModelTemplates#SLAB_BOTTOM} and {@link ModelTemplates#SLAB_TOP} templates.
     *
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation)
     * @see #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation, boolean)
     * @see #slabBottom(ResourceLocation)
     * @see #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabTop(ResourceLocation, boolean)
     * @see #slabTop(ResourceLocation)
     */
    public static ObjectArrayList<BlockModelDefinition> slab(ResourceLocation bottomSideTexture, ResourceLocation bottomBottomTexture, ResourceLocation bottomTopTexture, ResourceLocation topSideTexture, ResourceLocation topBottomTexture, ResourceLocation topTopTexture) {
        return ObjectArrayList.of(slabBottom(bottomSideTexture, bottomBottomTexture, bottomTopTexture), slabTop(topSideTexture, topBottomTexture, topTopTexture));
    }

    /**
     * Overloaded variant of {@link #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)}. Creates an {@link ObjectArrayList} of
     * {@linkplain BlockModelDefinition BlockModelDefinitions} with the {@link ModelTemplates#SLAB_BOTTOM} and {@link ModelTemplates#SLAB_TOP} templates, with all required {@linkplain TextureSlot TextureSlots}
     * mapped out to the provided {@link ResourceLocation} parameters. Maps both returned models with shared {@linkplain TextureSlot TextureSlots}.
     * <p>
     * <h3>Models</h3>
     * <ul>
     *  <li>{@link ModelTemplates#SLAB_BOTTOM} -> <ul>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code bottomTexture}</li>
     *      <li>{@link TextureSlot#TOP} -> {@code topTexture}</li>
     *      <li>{@link TextureSlot#SIDE} -> {@code sideTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#SLAB_TOP} -> <ul>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code bottomTexture}</li>
     *      <li>{@link TextureSlot#TOP} -> {@code topTexture}</li>
     *      <li>{@link TextureSlot#SIDE} -> {@code sideTexture}</li>
     *  </ul></li>
     * </ul>
     *
     * @param sideTexture The {@link ResourceLocation} representing the side texture of the bottom & top slab block models.
     * @param bottomTexture The {@link ResourceLocation} representing the bottom texture of the bottom & top slab block models.
     * @param topTexture The {@link ResourceLocation} representing the top texture of the bottom & top slab block models.
     *
     * @return An {@link ObjectArrayList} of {@linkplain BlockModelDefinition BlockModelDefinitions} with the {@link ModelTemplates#SLAB_BOTTOM} and {@link ModelTemplates#SLAB_TOP} templates, with each template
     * utilising shared {@linkplain TextureSlot TextureSlots}.
     *
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation)
     * @see #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation, boolean)
     * @see #slabBottom(ResourceLocation)
     * @see #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabTop(ResourceLocation, boolean)
     * @see #slabTop(ResourceLocation)
     */
    public static ObjectArrayList<BlockModelDefinition> slab(ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
        return slab(sideTexture, bottomTexture, topTexture, sideTexture, bottomTexture, topTexture);
    }

    /**
     * Overloaded variant of {@link #slab(ResourceLocation, ResourceLocation, ResourceLocation)}. Creates an {@link ObjectArrayList} of {@linkplain BlockModelDefinition BlockModelDefinitions}
     * with the {@link ModelTemplates#SLAB_BOTTOM} and {@link ModelTemplates#SLAB_TOP} templates, with all required {@linkplain TextureSlot TextureSlots} mapped out to the provided {@link ResourceLocation}
     * parameters. Maps both returned models' {@linkplain TextureSlot TextureSlots} with the provided {@code baseTexture}.
     * <p>
     * <h3>Models</h3>
     * <ul>
     *  <li>{@link ModelTemplates#SLAB_BOTTOM} -> <ul>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code baseTexture}</li>
     *      <li>{@link TextureSlot#TOP} -> {@code baseTexture}</li>
     *      <li>{@link TextureSlot#SIDE} -> {@code baseTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#SLAB_TOP} -> <ul>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code baseTexture}</li>
     *      <li>{@link TextureSlot#TOP} -> {@code baseTexture}</li>
     *      <li>{@link TextureSlot#SIDE} -> {@code baseTexture}</li>
     *  </ul></li>
     * </ul>
     *
     * @param baseTexture The {@link ResourceLocation} representing the base texture of every {@link TextureSlot} of the bottom & top slab block models.
     *
     * @return An {@link ObjectArrayList} of {@linkplain BlockModelDefinition BlockModelDefinitions} with the {@link ModelTemplates#SLAB_BOTTOM} and {@link ModelTemplates#SLAB_TOP} templates, with each template's
     * {@linkplain TextureSlot TextureSlots} sharing the same {@code baseTexture}.
     *
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation, boolean)
     * @see #slabBottom(ResourceLocation)
     * @see #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabTop(ResourceLocation, boolean)
     * @see #slabTop(ResourceLocation)
     */
    public static ObjectArrayList<BlockModelDefinition> slab(ResourceLocation baseTexture) {
        return slab(baseTexture, baseTexture, baseTexture);
    }

    /**
     * Creates a {@link BlockStateDefinition}, using {@link MultiVariantGenerator} with the {@link VariantProperties#MODEL} property set to the provided {@linkplain ResourceLocation ResourceLocations}
     * based on the {@link BlockStateProperties#SLAB_TYPE} property.
     * <p>
     * <h3>Variants / Properties</h3>
     * <ul>
     *  <li>{@link BlockStateProperties#SLAB_TYPE} -> <ul>
     *      <li>{@link SlabType#BOTTOM} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code bottomModel}</li>
     *      </ul></li>
     *      <li>{@link SlabType#TOP} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code topModel}</li>
     *      </ul></li>
     *      <li>{@link SlabType#DOUBLE} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code doubleBlockModel}</li>
     *      </ul></li>
     *  </ul></li>
     * </ul>
     *
     * @param targetBlock The {@linkplain Block Block} to use as the base for the {@link BlockStateDefinition}.
     * @param bottomModel The {@link ResourceLocation} representing the model of the bottom half of a given standard slab.
     * @param topModel The {@link ResourceLocation} representing the model of the top half of a given standard slab.
     * @param doubleSlabModel The {@link ResourceLocation} representing the model of 2 slabs stacked on top of each other to create a standard cube block.
     *
     * @return A {@link BlockStateDefinition} with the {@link MultiVariantGenerator} with the {@link VariantProperties#MODEL} property set to the provided {@linkplain ResourceLocation ResourceLocations}
     * based on the {@link BlockStateProperties#SLAB_TYPE} property.
     *
     * @see #slab(Supplier, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation)
     * @see #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation, boolean)
     * @see #slabBottom(ResourceLocation)
     * @see #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabTop(ResourceLocation, boolean)
     * @see #slabTop(ResourceLocation)
     * @see #woodenSlab(Supplier)
     * @see SlabType
     */
    public static BlockStateDefinition slab(Supplier<Block> targetBlock, ResourceLocation bottomModel, ResourceLocation topModel, ResourceLocation doubleSlabModel) {
        return BlockStateDefinition.of(targetBlock)
                .withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get())
                        .with(PropertyDispatch.property(BlockStateProperties.SLAB_TYPE)
                                .select(SlabType.BOTTOM, Variant.variant()
                                        .with(VariantProperties.MODEL, bottomModel))
                                .select(SlabType.TOP, Variant.variant().with(VariantProperties.MODEL, topModel))
                                .select(SlabType.DOUBLE, Variant.variant().with(VariantProperties.MODEL, doubleSlabModel))));
    }

    /**
     * Overloaded variant of {@link #slab(Supplier, ResourceLocation, ResourceLocation, ResourceLocation)}, utilising the supplied {@link Block} as the base for deciding the
     * {@link ResourceLocation} of every required {@link TextureSlot} in a standard slab. The double-slab model variant is based on the provided {@code doubleSlabModel}.
     * <p>
     * <h3>Variants / Properties</h3>
     * <ul>
     *  <li>{@link BlockStateProperties#SLAB_TYPE} -> <ul>
     *      <li>{@link SlabType#BOTTOM} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@link ModelLocationUtils#getModelLocation(Block)}</li>
     *      </ul></li>
     *      <li>{@link SlabType#TOP} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@link ModelLocationUtils#getModelLocation(Block, String)} (Suffixed with {@code "_top"})</li>
     *      </ul></li>
     *      <li>{@link SlabType#DOUBLE} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code doubleBlockModel}</li>
     *      </ul></li>
     *  </ul></li>
     * </ul>
     *
     * @param targetBlock The {@link Block} used as the base for determining the {@link ResourceLocation} of every required {@link TextureSlot} in a standard slab.
     * @param doubleBlockModel The {@link ResourceLocation} representing the model of 2 slabs stacked on top of each other to create a standard cube block.
     *
     * @return A {@link BlockStateDefinition} with the {@link MultiVariantGenerator} with the {@link VariantProperties#MODEL} property set to the required {@linkplain ResourceLocation ResourceLocations}
     * based on the required {@linkplain TextureSlot TextureSlots} in a standard slab, utilising the supplied {@link Block} as the base.
     *
     * @see #slab(Supplier, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation)
     * @see #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation, boolean)
     * @see #slabBottom(ResourceLocation)
     * @see #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabTop(ResourceLocation, boolean)
     * @see #slabTop(ResourceLocation)
     * @see #slab(Supplier)
     * @see #woodenSlab(Supplier)
     * @see SlabType
     */
    public static BlockStateDefinition slab(Supplier<Block> targetBlock, ResourceLocation doubleBlockModel) {
        return slab(targetBlock, ModelLocationUtils.getModelLocation(targetBlock.get()), ModelLocationUtils.getModelLocation(targetBlock.get(), "_top"), doubleBlockModel);
    }

    /**
     * Overloaded variant of {@link #slab(Supplier, ResourceLocation)}, utilising the supplied {@link Block} as the base for deciding the
     * {@link ResourceLocation} of every required {@link TextureSlot} in a standard slab. Defaults the double-slab model to an arbitrarily-guessed reference of the should-be existing
     * block variant of the supplied slab. It first attempts to find the full-block model by only pruning {@code "_slab"} from the supplied {@linkplain Block Block's} registry name,
     * defaulting to pruning {@code "_slab"} and appending {@code "_block"} if it fails to find a match.
     * <p>
     * <h3>Variants / Properties</h3>
     * <ul>
     *  <li>{@link BlockStateProperties#SLAB_TYPE} -> <ul>
     *      <li>{@link SlabType#BOTTOM} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@link ModelLocationUtils#getModelLocation(Block)}</li>
     *      </ul></li>
     *      <li>{@link SlabType#TOP} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@link ModelLocationUtils#getModelLocation(Block, String)} (Suffixed with {@code "_top"})</li>
     *      </ul></li>
     *      <li>{@link SlabType#DOUBLE} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code !findPruned(blockName, "_slab") ? blockName.replace("_slab", "").concat("_block") : prune(blockName, "_slab")} (Does not accurately represent how this method actually does String mutation)</li>
     *      </ul></li>
     *  </ul></li>
     * </ul>
     *
     * @param targetBlock The {@link Block} used as the base for determining the {@link ResourceLocation} of every required {@link TextureSlot} in a standard slab.
     *
     * @return A {@link BlockStateDefinition} with the {@link MultiVariantGenerator} with the {@link VariantProperties#MODEL} property set to the required {@linkplain ResourceLocation ResourceLocations}
     * based on the required {@linkplain TextureSlot TextureSlots} in a standard slab, utilising the supplied {@link Block} as the base.
     *
     * @see #slab(Supplier, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation)
     * @see #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation, boolean)
     * @see #slabBottom(ResourceLocation)
     * @see #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabTop(ResourceLocation, boolean)
     * @see #slabTop(ResourceLocation)
     * @see #woodenSlab(Supplier)
     * @see SlabType
     */
    public static BlockStateDefinition slab(Supplier<Block> targetBlock) {
        String targetDoubleBlockModel = StringUtils.substringBefore(ModelLocationUtils.getModelLocation(targetBlock.get()).getPath(), "_slab");
        String fallbackDoubleBlockModel = StringUtils.substringBefore(ModelLocationUtils.getModelLocation(targetBlock.get()).getPath(), "_slab").concat("_block");
        String chosenDoubleBlockModel = BuiltInRegistries.BLOCK.get(RegistryUtil.getItemKey(targetBlock.get()).withPath(targetDoubleBlockModel)).getDescriptionId().equals("block.minecraft.air") ? fallbackDoubleBlockModel : targetDoubleBlockModel;
        return slab(targetBlock, RegistryUtil.getItemKey(targetBlock.get()).withPath(chosenDoubleBlockModel));
    }

    /**
     * Overloaded variant of {@link #slab(Supplier, ResourceLocation)}, utilising the supplied {@link Block} as the base for deciding the
     * {@link ResourceLocation} of every required {@link TextureSlot} in a standard slab. Also defaults the double-slab model to reference the plank variant of the supplied {@link Block},
     * assuming that it exists (Will likely throw an exception otherwise). Should typically be used for standard wood sets, particularly slabs (duh).
     * <p>
     * <h3>Variants / Properties</h3>
     * <ul>
     *  <li>{@link BlockStateProperties#SLAB_TYPE} -> <ul>
     *      <li>{@link SlabType#BOTTOM} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@link ModelLocationUtils#getModelLocation(Block)}</li>
     *      </ul></li>
     *      <li>{@link SlabType#TOP} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@link ModelLocationUtils#getModelLocation(Block, String)} (Suffixed with {@code "_top"})</li>
     *      </ul></li>
     *      <li>{@link SlabType#DOUBLE} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@link ModelLocationUtils#getModelLocation(Block)} + {@code replaceAll("_slab", "_planks")}</li>
     *      </ul></li>
     *  </ul></li>
     * </ul>
     *
     * @param targetBlock The {@link Block} used as the base for determining the {@link ResourceLocation} of every required {@link TextureSlot} in a standard slab.
     *
     * @return A {@link BlockStateDefinition} with the {@link MultiVariantGenerator} with the {@link VariantProperties#MODEL} property set to the required {@linkplain ResourceLocation ResourceLocations}
     * based on the required {@linkplain TextureSlot TextureSlots} in a standard slab, utilising the supplied {@link Block} as the base.
     *
     * @see #slab(Supplier, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slab(ResourceLocation)
     * @see #slabBottom(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabBottom(ResourceLocation, boolean)
     * @see #slabBottom(ResourceLocation)
     * @see #slabTop(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #slabTop(ResourceLocation, boolean)
     * @see #slabTop(ResourceLocation)
     * @see #slab(Supplier, ResourceLocation)
     * @see #slab(Supplier)
     * @see SlabType
     */
    public static BlockStateDefinition woodenSlab(Supplier<Block> targetBlock) {
        String targetDoubleBlockModel = ModelLocationUtils.getModelLocation(targetBlock.get()).getPath();
        return slab(targetBlock, RegistryUtil.getItemKey(targetBlock.get()).withPath(!targetDoubleBlockModel.contains("_slab") ? targetDoubleBlockModel.concat("_planks") : targetDoubleBlockModel.replaceAll("_slab", "_planks")));
    }

    /**
     * Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#CROSS} template. If you're doing crops and such, you may want to refer to {@link #crossCutout(ResourceLocation)}.
     * Handles item model.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#CROSS} -> {@code baseCrossTexture}</li>
     * </ul>
     *
     * @param baseCrossTexture The {@link ResourceLocation} representing the base texture file of every face of a cross-block.
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#CROSS} template.
     *
     * @see #crossCutout(ResourceLocation)
     * @see #simpleBlock(Supplier)
     */
    public static BlockModelDefinition cross(ResourceLocation baseCrossTexture) {
        return BlockModelDefinition.of(ModelTemplates.CROSS)
                .withTextureMapping(TextureMapping.cross(RegistryUtil.pickBlockPrefix(baseCrossTexture)))
                .withCustomItemModel(generatedBlock(baseCrossTexture));
    }

    /**
     * Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#CROSS} template. Calls {@link #cross(ResourceLocation)} and sets the
     * {@link BlockModelDefinition#withBlockRenderType(ResourceLocation)} to {@link RenderType#cutout()}. Handles item model.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#CROSS} -> {@code baseCrossTexture}</li>
     * </ul>
     *
     * @param baseCrossTexture The {@link ResourceLocation} representing the base texture file of every face of a cross-block.
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#CROSS} template and the {@link RenderType#cutout()} render type.
     *
     * @see #cross(ResourceLocation)
     * @see #simpleBlock(Supplier)
     * @see RenderType
     */
    public static BlockModelDefinition crossCutout(ResourceLocation baseCrossTexture) {
        return cross(baseCrossTexture).withBlockRenderType(new ResourceLocation(RenderType.cutout().name));
    }

    public static BlockModelDefinition tintedCross(ResourceLocation baseCrossTexture) {
        return BlockModelDefinition.of(ModelTemplates.TINTED_CROSS)
                .withTextureMapping(TextureMapping.cross(RegistryUtil.pickBlockPrefix(baseCrossTexture)))
                .withCustomItemModel(generatedBlock(baseCrossTexture));
    }

    public static BlockModelDefinition tintedCrossCutout(ResourceLocation baseCrossTexture) {
        return tintedCross(baseCrossTexture).withBlockRenderType(new ResourceLocation(RenderType.cutout().name));
    }

    /**
     * Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_COLUMN} template.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code sideTexture}</li>
     *  <li>{@link TextureSlot#END} -> {@code endTexture}</li>
     * </ul>
     *
     * @param sideTexture The {@link ResourceLocation} representing the texture file of the side faces of a standard column block (I.E. horizontal faces, N, S, E, W).
     * @param endTexture The {@link ResourceLocation} representing the texture file of the end faces of a standard column block (I.E. vertical faces, U, D).
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_COLUMN} template.
     *
     * @see #cubeColumn(ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation, ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier)
     */
    public static BlockModelDefinition cubeColumn(ResourceLocation sideTexture, ResourceLocation endTexture) {
        return BlockModelDefinition.of(ModelTemplates.CUBE_COLUMN)
                .withTextureMapping(TextureMapping.column(RegistryUtil.pickBlockPrefix(sideTexture), RegistryUtil.pickBlockPrefix(endTexture)));
    }

    /**
     * Overload variant of {@link #cubeColumn(ResourceLocation, ResourceLocation)}. Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_COLUMN} template, with both required
     * {@linkplain TextureSlot TextureSlots} mapped to the provided {@code baseTexture}.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code baseTexture}</li>
     *  <li>{@link TextureSlot#END} -> {@code baseTexture}</li>
     * </ul>
     *
     * @param baseTexture The {@link ResourceLocation} representing the texture file of all faces of a standard column block (I.E. horizontal + vertical faces, N, S, E, W, U, D).
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_COLUMN} template, with both required {@linkplain TextureSlot TextureSlots} set to the provided {@code baseTexture}.
     *
     * @see #cubeColumn(ResourceLocation, ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation, ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier)
     */
    public static BlockModelDefinition cubeColumn(ResourceLocation baseTexture) {
        return cubeColumn(baseTexture, baseTexture);
    }

    /**
     * Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} template.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code sideTexture}</li>
     *  <li>{@link TextureSlot#END} -> {@code endTexture}</li>
     * </ul>
     *
     * @param sideTexture The {@link ResourceLocation} representing the texture file of the side faces of a standard column block (I.E. horizontal faces, N, S, E, W).
     * @param endTexture The {@link ResourceLocation} representing the texture file of the end faces of a standard column block (I.E. vertical faces, U, D).
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} template.
     *
     * @see #cubeColumnHorizontal(ResourceLocation)
     * @see #cubeColumn(ResourceLocation, ResourceLocation)
     * @see #cubeColumn(ResourceLocation)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier)
     */
    public static BlockModelDefinition cubeColumnHorizontal(ResourceLocation sideTexture, ResourceLocation endTexture) {
        return BlockModelDefinition.of(ModelTemplates.CUBE_COLUMN_HORIZONTAL)
                .withTextureMapping(TextureMapping.column(RegistryUtil.pickBlockPrefix(sideTexture), RegistryUtil.pickBlockPrefix(endTexture)));
    }

    /**
     * Overload variant of {@link #cubeColumnHorizontal(ResourceLocation, ResourceLocation)}. Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} template, with both required
     * {@linkplain TextureSlot TextureSlots} mapped to the provided {@code baseTexture}.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <ul>
     *  <li>{@link TextureSlot#SIDE} -> {@code baseTexture}</li>
     *  <li>{@link TextureSlot#END} -> {@code baseTexture}</li>
     * </ul>
     *
     * @param baseTexture The {@link ResourceLocation} representing the texture file of all faces of a standard column block (I.E. horizontal + vertical faces, N, S, E, W, U, D).
     *
     * @return A {@link BlockModelDefinition} with the {@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} template, with both required {@linkplain TextureSlot TextureSlots} set to the provided {@code baseTexture}.
     *
     * @see #cubeColumnHorizontal(ResourceLocation, ResourceLocation)
     * @see #cubeColumn(ResourceLocation, ResourceLocation)
     * @see #cubeColumn(ResourceLocation)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier)
     */
    public static BlockModelDefinition cubeColumnHorizontal(ResourceLocation baseTexture) {
        return cubeColumnHorizontal(baseTexture, baseTexture);
    }

    /**
     * Creates an {@link ObjectArrayList} of {@linkplain BlockModelDefinition BlockModelDefinitions} with the {@link ModelTemplates#CUBE_COLUMN} and {@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} templates, in order
     * to constitute a standard rotated pillar block.
     * <p>
     * <h3>Models</h3>
     * <ul>
     *  <li>{@link ModelTemplates#CUBE_COLUMN} -> <ul>
     *      <li>{@link TextureSlot#SIDE} -> {@code ccSideTexture}</li>
     *      <li>{@link TextureSlot#END} -> {@code ccEndTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} -> <ul>
     *      <li>{@link TextureSlot#SIDE} -> {@code cchSideTexture}</li>
     *      <li>{@link TextureSlot#END} -> {@code cchEndTexture}</li>
     *  </ul></li>
     * </ul>
     *
     * @param ccSideTexture The {@link ResourceLocation} representing the texture file of the side faces (horizontal, I.E. N, S, E, W) of the {@link ModelTemplates#CUBE_COLUMN} model.
     * @param ccEndTexture The {@link ResourceLocation} representing the texture file of the end (vertical, I.E. U, D) faces of the {@link ModelTemplates#CUBE_COLUMN} model.
     * @param cchSideTexture The {@link ResourceLocation} representing the texture file of the side faces (horizontal, I.E. N, S, E, W) of the {@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} model.
     * @param cchEndTexture The {@link ResourceLocation} representing the texture file of the end (vertical, I.E. U, D) faces of the {@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} model.
     *
     * @return An {@link ObjectArrayList} of {@linkplain BlockModelDefinition BlockModelDefinitions} with the {@link ModelTemplates#CUBE_COLUMN} and {@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} templates, in order
     *         to constitute a standard rotated pillar block.
     *
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier)
     * @see #cubeColumn(ResourceLocation, ResourceLocation)
     * @see #cubeColumn(ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation, ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation)
     */
    public static ObjectArrayList<BlockModelDefinition> rotatedPillarBlock(ResourceLocation ccSideTexture, ResourceLocation ccEndTexture, ResourceLocation cchSideTexture, ResourceLocation cchEndTexture) {
        return ObjectArrayList.of(cubeColumn(ccSideTexture, ccEndTexture), cubeColumnHorizontal(cchSideTexture, cchEndTexture));
    }

    /**
     * Overloaded variant of {@link #rotatedPillarBlock(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)}. Creates an {@link ObjectArrayList} of
     * {@linkplain BlockModelDefinition BlockModelDefinitions} with the {@link ModelTemplates#CUBE_COLUMN} and {@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} templates, in order
     * to constitute a standard rotated pillar block. Allows both models to share the same side and end textures.
     * <p>
     * <h3>Models</h3>
     * <ul>
     *  <li>{@link ModelTemplates#CUBE_COLUMN} -> <ul>
     *      <li>{@link TextureSlot#SIDE} -> {@code sideTexture}</li>
     *      <li>{@link TextureSlot#END} -> {@code endTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} -> <ul>
     *      <li>{@link TextureSlot#SIDE} -> {@code sideTexture}</li>
     *      <li>{@link TextureSlot#END} -> {@code endTexture}</li>
     *  </ul></li>
     * </ul>
     *
     * @param sideTexture The {@link ResourceLocation} representing the texture file of the side faces (horizontal, I.E. N, S, E, W) of both {@link ModelTemplates#CUBE_COLUMN}
     *                   and {@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} models.
     * @param endTexture The {@link ResourceLocation} representing the texture file of the end (vertical, I.E. U, D) faces of both {@link ModelTemplates#CUBE_COLUMN}
     *                   and {@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} models.
     *
     * @return An {@link ObjectArrayList} of {@linkplain BlockModelDefinition BlockModelDefinitions} with the {@link ModelTemplates#CUBE_COLUMN} and {@link ModelTemplates#CUBE_COLUMN_HORIZONTAL} templates, in order
     *         to constitute a standard rotated pillar block, both models sharing the same side and end textures.
     *
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier)
     * @see #cubeColumn(ResourceLocation, ResourceLocation)
     * @see #cubeColumn(ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation, ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation)
     */
    public static ObjectArrayList<BlockModelDefinition> rotatedPillarBlock(ResourceLocation sideTexture, ResourceLocation endTexture) {
        return rotatedPillarBlock(sideTexture, endTexture, sideTexture, endTexture);
    }

    /**
     * Creates a {@link BlockStateDefinition}, using {@link MultiVariantGenerator} to update the supplied {@linkplain Block Block's} model based on its rotation across all 3 axis with a horizontal variant.
     * <p>
     * <h3>Variants / Properties</h3>
     * <ul>
     *  <li>{@link BlockStateProperties#AXIS} -> <ul>
     *      <li>{@link Direction.Axis#X} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code horizontalModel}</li>
     *          <li>{@link VariantProperties#X_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *          <li>{@link VariantProperties#Y_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *      </ul></li>
     *      <li>{@link Direction.Axis#Y} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code baseModel}</li>
     *      </ul></li>
     *      <li>{@link Direction.Axis#Z} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code horizontalModel}</li>
     *          <li>{@link VariantProperties#X_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *      </ul></li>
     *  </ul></li>
     * </ul>
     *
     * @param targetBlock The {@linkplain Block Block} to use as the base for the {@link BlockStateDefinition}.
     * @param baseModel The {@link ResourceLocation} representing the block model of the non-horizontal variant of the supplied {@link Block}.
     * @param horizontalModel The {@link ResourceLocation} representing the block model of the horizontal (rotated) variant of the supplied {@link Block}.
     *
     * @return A {@link BlockStateDefinition}, using {@link MultiVariantGenerator} to update the supplied {@linkplain Block Block's} model based on its rotation across all 3 axis.
     *
     * @see #rotatedPillarBlock(Supplier, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation)
     * @see #cubeColumn(ResourceLocation, ResourceLocation)
     * @see #cubeColumn(ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation, ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation)
     */
    public static BlockStateDefinition rotatedPillarBlock(Supplier<Block> targetBlock, ResourceLocation baseModel, ResourceLocation horizontalModel) {
        return BlockStateDefinition.of(targetBlock).withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get())
                .with(PropertyDispatch.property(BlockStateProperties.AXIS)
                        .select(Direction.Axis.Y, Variant.variant()
                                .with(VariantProperties.MODEL, baseModel))
                        .select(Direction.Axis.Z, Variant.variant()
                                .with(VariantProperties.MODEL, horizontalModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.Axis.X, Variant.variant()
                                .with(VariantProperties.MODEL, horizontalModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))));
    }

    /**
     * Overloaded variant of {@link #rotatedPillarBlock(Supplier, ResourceLocation, ResourceLocation)}. Creates a {@link BlockStateDefinition}, using {@link MultiVariantGenerator}
     * to update the supplied {@linkplain Block Block's} model based on its rotation across all 3 axis with a horizontal variant. Defaults the {@code horizontalModel} to {@code baseModel + {@code "_horizontal"}}.
     * <p>
     * <h3>Variants / Properties</h3>
     * <ul>
     *  <li>{@link BlockStateProperties#AXIS} -> <ul>
     *      <li>{@link Direction.Axis#X} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code baseModel + {@code "_horizontal"}}</li>
     *          <li>{@link VariantProperties#X_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *          <li>{@link VariantProperties#Y_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *      </ul></li>
     *      <li>{@link Direction.Axis#Y} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code baseModel}</li>
     *      </ul></li>
     *      <li>{@link Direction.Axis#Z} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code baseModel + {@code "_horizontal"}}</li>
     *          <li>{@link VariantProperties#X_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *      </ul></li>
     *  </ul></li>
     * </ul>
     *
     * @param targetBlock The {@linkplain Block Block} to use as the base for the {@link BlockStateDefinition}.
     * @param baseModel The {@link ResourceLocation} representing the block model of the non-horizontal variant of the supplied {@link Block}.
     *
     * @return A {@link BlockStateDefinition}, using {@link MultiVariantGenerator} to update the supplied {@linkplain Block Block's} model based on its rotation across all 3 axis,
     * and with the {@code horizontalModel} defaulted to {@code baseModel + {@code "_horizontal"}}.
     *
     * @see #rotatedPillarBlock(Supplier, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation)
     * @see #cubeColumn(ResourceLocation, ResourceLocation)
     * @see #cubeColumn(ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation, ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation)
     */
    public static BlockStateDefinition rotatedPillarBlock(Supplier<Block> targetBlock, ResourceLocation baseModel) {
        return rotatedPillarBlock(targetBlock, baseModel, baseModel.withSuffix("_horizontal"));
    }

    /**
     * Overloaded variant of {@link #rotatedPillarBlock(Supplier, ResourceLocation)}. Creates a {@link BlockStateDefinition}, using {@link MultiVariantGenerator}
     * to update the supplied {@linkplain Block Block's} model based on its rotation across all 3 axis with a horizontal variant. Defaults the {@code baseModel} to the default location of the supplied {@linkplain Block Block's}
     * model ({@link ModelLocationUtils#getModelLocation(Block)}).
     * <p>
     * <h3>Variants / Properties</h3>
     * <ul>
     *  <li>{@link BlockStateProperties#AXIS} -> <ul>
     *      <li>{@link Direction.Axis#X} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@link ModelLocationUtils#getModelLocation(Block)} (Suffixed with {@code "_horizontal"})</li>
     *          <li>{@link VariantProperties#X_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *          <li>{@link VariantProperties#Y_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *      </ul></li>
     *      <li>{@link Direction.Axis#Y} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@link ModelLocationUtils#getModelLocation(Block)}</li>
     *      </ul></li>
     *      <li>{@link Direction.Axis#Z} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@link ModelLocationUtils#getModelLocation(Block)} (Suffixed with {@code "_horizontal"})</li>
     *          <li>{@link VariantProperties#X_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *      </ul></li>
     *  </ul></li>
     * </ul>
     *
     * @param targetBlock The {@linkplain Block Block} to use as the base for the {@link BlockStateDefinition} and the {@link ResourceLocation} of its cube-column variant models.
     *
     * @return A {@link BlockStateDefinition}, using {@link MultiVariantGenerator} to update the supplied {@linkplain Block Block's} model based on its rotation across all 3 axis,
     * and with the {@code horizontalModel} defaulted to {@code baseModel + {@code "_horizontal"}}, where {@code baseModel} defaults to {@link ModelLocationUtils#getModelLocation(Block)}.
     *
     * @see #rotatedPillarBlock(Supplier, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(Supplier, ResourceLocation)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #rotatedPillarBlock(ResourceLocation, ResourceLocation)
     * @see #cubeColumn(ResourceLocation, ResourceLocation)
     * @see #cubeColumn(ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation, ResourceLocation)
     * @see #cubeColumnHorizontal(ResourceLocation)
     */
    public static BlockStateDefinition rotatedPillarBlock(Supplier<Block> targetBlock) {
        return rotatedPillarBlock(targetBlock, ModelLocationUtils.getModelLocation(targetBlock.get()));
    }

    /**
     * Creates a {@link BlockStateDefinition}, using {@link MultiVariantGenerator} to update the supplied {@linkplain Block Block's} model based on its rotation across all 3 axis.
     * <p>
     * <h3>Variants / Properties</h3>
     * <ul>
     *  <li>{@link VariantProperties#MODEL} -> {@code baseModel}</li>
     *  <li>{@link BlockStateProperties#AXIS} -> <ul>
     *      <li>{@link Direction.Axis#X} -> <ul>
     *          <li>{@link VariantProperties#X_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *          <li>{@link VariantProperties#Y_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *      </ul></li>
     *      <li>{@link Direction.Axis#Y} -> <ul>
     *      </ul></li>
     *      <li>{@link Direction.Axis#Z} -> <ul>
     *          <li>{@link VariantProperties#X_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *      </ul></li>
     *  </ul></li>
     * </ul>
     *
     * @param targetBlock The {@linkplain Block Block} to use as the base for the {@link BlockStateDefinition}.
     *
     * @return A {@link BlockStateDefinition}, using {@link MultiVariantGenerator} to update the supplied {@linkplain Block Block's} model based on its rotation across all 3 axis.
     */
    public static BlockStateDefinition axisAlignedBlock(Supplier<Block> targetBlock, ResourceLocation baseModel) {
        return BlockStateDefinition.of(targetBlock).withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get(), Variant.variant().with(VariantProperties.MODEL, baseModel))
                .with(PropertyDispatch
                        .property(BlockStateProperties.AXIS)
                        .select(Direction.Axis.Y, Variant.variant())
                        .select(Direction.Axis.Z, Variant.variant()
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.Axis.X, Variant.variant()
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))));
    }

    /**
     * Overloaded variant of {@link #axisAlignedBlock(Supplier, ResourceLocation)}. Creates a {@link BlockStateDefinition}, using {@link MultiVariantGenerator} to update the supplied {@linkplain Block Block's}
     * model based on its rotation across all 3 axis. Defaults to the supplied {@linkplain Block Block's} default model location.
     * <p>
     * <h3>Variants / Properties</h3>
     * <ul>
     *  <li>{@link BlockStateProperties#AXIS} -> <ul>
     *      <li>{@link Direction.Axis#X} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code horizontalModel}</li>
     *          <li>{@link VariantProperties#X_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *          <li>{@link VariantProperties#Y_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *      </ul></li>
     *      <li>{@link Direction.Axis#Y} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code baseModel}</li>
     *      </ul></li>
     *      <li>{@link Direction.Axis#Z} -> <ul>
     *          <li>{@link VariantProperties#MODEL} -> {@code horizontalModel}</li>
     *          <li>{@link VariantProperties#X_ROT} -> {@link VariantProperties.Rotation#R90}</li>
     *      </ul></li>
     *  </ul></li>
     * </ul>
     *
     * @param targetBlock The {@linkplain Block Block} to use as the base for the {@link BlockStateDefinition}.
     *
     * @return A {@link BlockStateDefinition}, using {@link MultiVariantGenerator} to update the supplied {@linkplain Block Block's} model based on its rotation across all 3 axis, with the target model defaulting to
     * {@link ModelLocationUtils#getModelLocation(Block)}.
     */
    public static BlockStateDefinition axisAlignedBlock(Supplier<Block> targetBlock) {
        return axisAlignedBlock(targetBlock, ModelLocationUtils.getModelLocation(targetBlock.get()));
    }

    /**
     * Creates a {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_BOTTOM_LEFT} template. Handles item model.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <b>Block Model</b>
     * <ul>
     *  <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     * </ul>
     * <b>Item Model</b>
     * <ul>
     *  <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     * </ul>
     *
     * @param doorTopTexture The {@link ResourceLocation} representing the top-half texture of the door's bottom left (I.E. When the hinge is on the left side).
     * @param doorBottomTexture The {@link ResourceLocation} representing the bottom-half texture of the door's bottom left (I.E. When the hinge is on the left side).
     * @param doorItemTexture The {@link ResourceLocation} representing the item texture of the door.
     *
     * @return A {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_BOTTOM_LEFT} template.
     *
     * @see #doorBottomLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static BlockModelDefinition doorBottomLeft(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture, ResourceLocation doorItemTexture) {
        return BlockModelDefinition.of(ModelTemplates.DOOR_BOTTOM_LEFT)
                .withTextureMapping(TextureMapping.door(RegistryUtil.pickBlockPrefix(doorTopTexture), RegistryUtil.pickBlockPrefix(doorBottomTexture)))
                .withBlockRenderType(new ResourceLocation(RenderType.cutout().name))
                .withCustomItemModel(ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                        .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(doorItemTexture))));
    }

    /**
     * Creates a {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_BOTTOM_LEFT_OPEN} template. Handles item model.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <b>Block Model</b>
     * <ul>
     *  <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     * </ul>
     * <b>Item Model</b>
     * <ul>
     *  <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     * </ul>
     *
     * @param doorTopTexture The {@link ResourceLocation} representing the top-half texture of the door's bottom left (I.E. When the hinge is on the left side) when it's open.
     * @param doorBottomTexture The {@link ResourceLocation} representing the bottom-half texture of the door's bottom left (I.E. When the hinge is on the left side) when it's open.
     * @param doorItemTexture The {@link ResourceLocation} representing the item texture of the door.
     *
     * @return A {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_BOTTOM_LEFT_OPEN} template.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static BlockModelDefinition doorBottomLeftOpen(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture, ResourceLocation doorItemTexture) {
        return BlockModelDefinition.of(ModelTemplates.DOOR_BOTTOM_LEFT_OPEN)
                .withTextureMapping(TextureMapping.door(RegistryUtil.pickBlockPrefix(doorTopTexture), RegistryUtil.pickBlockPrefix(doorBottomTexture)))
                .withBlockRenderType(new ResourceLocation(RenderType.cutout().name))
                .withCustomItemModel(ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                        .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(doorItemTexture))));
    }

    /**
     * Creates a {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_TOP_LEFT} template. Handles item model.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <b>Block Model</b>
     * <ul>
     *  <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     * </ul>
     * <b>Item Model</b>
     * <ul>
     *  <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     * </ul>
     *
     * @param doorTopTexture The {@link ResourceLocation} representing the top-half texture of the door's top left (I.E. When the hinge is on the left side).
     * @param doorBottomTexture The {@link ResourceLocation} representing the bottom-half texture of the door's top left (I.E. When the hinge is on the left side).
     * @param doorItemTexture The {@link ResourceLocation} representing the item texture of the door.
     *
     * @return A {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_TOP_LEFT} template.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static BlockModelDefinition doorTopLeft(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture, ResourceLocation doorItemTexture) {
        return BlockModelDefinition.of(ModelTemplates.DOOR_TOP_LEFT)
                .withTextureMapping(TextureMapping.door(RegistryUtil.pickBlockPrefix(doorTopTexture), RegistryUtil.pickBlockPrefix(doorBottomTexture)))
                .withBlockRenderType(new ResourceLocation(RenderType.cutout().name))
                .withCustomItemModel(ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                        .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(doorItemTexture))));
    }

    /**
     * Creates a {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_TOP_LEFT_OPEN} template. Handles item model.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <b>Block Model</b>
     * <ul>
     *  <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     * </ul>
     * <b>Item Model</b>
     * <ul>
     *  <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     * </ul>
     *
     * @param doorTopTexture The {@link ResourceLocation} representing the top-half texture of the door's top left (I.E. When the hinge is on the left side) when it's open.
     * @param doorBottomTexture The {@link ResourceLocation} representing the bottom-half texture of the door's top left (I.E. When the hinge is on the left side) when it's open.
     * @param doorItemTexture The {@link ResourceLocation} representing the item texture of the door.
     *
     * @return A {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_TOP_LEFT_OPEN} template.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static BlockModelDefinition doorTopLeftOpen(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture, ResourceLocation doorItemTexture) {
        return BlockModelDefinition.of(ModelTemplates.DOOR_TOP_LEFT_OPEN)
                .withTextureMapping(TextureMapping.door(RegistryUtil.pickBlockPrefix(doorTopTexture), RegistryUtil.pickBlockPrefix(doorBottomTexture)))
                .withBlockRenderType(new ResourceLocation(RenderType.cutout().name))
                .withCustomItemModel(ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                        .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(doorItemTexture))));
    }

    /**
     * Creates a {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_BOTTOM_RIGHT} template. Handles item model.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <b>Block Model</b>
     * <ul>
     *  <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     * </ul>
     * <b>Item Model</b>
     * <ul>
     *  <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     * </ul>
     *
     * @param doorTopTexture The {@link ResourceLocation} representing the top-half texture of the door's bottom right (I.E. When the hinge is on the right side).
     * @param doorBottomTexture The {@link ResourceLocation} representing the bottom-half texture of the door's bottom right (I.E. When the hinge is on the right side).
     * @param doorItemTexture The {@link ResourceLocation} representing the item texture of the door.
     *
     * @return A {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_BOTTOM_RIGHT} template.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static BlockModelDefinition doorBottomRight(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture, ResourceLocation doorItemTexture) {
        return BlockModelDefinition.of(ModelTemplates.DOOR_BOTTOM_RIGHT)
                .withTextureMapping(TextureMapping.door(RegistryUtil.pickBlockPrefix(doorTopTexture), RegistryUtil.pickBlockPrefix(doorBottomTexture)))
                .withBlockRenderType(new ResourceLocation(RenderType.cutout().name))
                .withCustomItemModel(ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                        .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(doorItemTexture))));
    }

    /**
     * Creates a {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_BOTTOM_RIGHT_OPEN} template. Handles item model.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <b>Block Model</b>
     * <ul>
     *  <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     * </ul>
     * <b>Item Model</b>
     * <ul>
     *  <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     * </ul>
     *
     * @param doorTopTexture The {@link ResourceLocation} representing the top-half texture of the door's bottom right (I.E. When the hinge is on the left side) when it's open.
     * @param doorBottomTexture The {@link ResourceLocation} representing the bottom-half texture of the door's bottom right (I.E. When the hinge is on the left side) when it's open.
     * @param doorItemTexture The {@link ResourceLocation} representing the item texture of the door.
     *
     * @return A {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_BOTTOM_RIGHT_OPEN} template.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static BlockModelDefinition doorBottomRightOpen(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture, ResourceLocation doorItemTexture) {
        return BlockModelDefinition.of(ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN)
                .withTextureMapping(TextureMapping.door(RegistryUtil.pickBlockPrefix(doorTopTexture), RegistryUtil.pickBlockPrefix(doorBottomTexture)))
                .withBlockRenderType(new ResourceLocation(RenderType.cutout().name))
                .withCustomItemModel(ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                        .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(doorItemTexture))));
    }

    /**
     * Creates a {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_TOP_RIGHT} template. Handles item model.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <b>Block Model</b>
     * <ul>
     *  <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     * </ul>
     * <b>Item Model</b>
     * <ul>
     *  <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     * </ul>
     *
     * @param doorTopTexture The {@link ResourceLocation} representing the top-half texture of the door's top right (I.E. When the hinge is on the right side).
     * @param doorBottomTexture The {@link ResourceLocation} representing the bottom-half texture of the door's top right (I.E. When the hinge is on the right side).
     * @param doorItemTexture The {@link ResourceLocation} representing the item texture of the door.
     *
     * @return A {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_TOP_RIGHT} template.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static BlockModelDefinition doorTopRight(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture, ResourceLocation doorItemTexture) {
        return BlockModelDefinition.of(ModelTemplates.DOOR_TOP_RIGHT)
                .withTextureMapping(TextureMapping.door(RegistryUtil.pickBlockPrefix(doorTopTexture), RegistryUtil.pickBlockPrefix(doorBottomTexture)))
                .withBlockRenderType(new ResourceLocation(RenderType.cutout().name))
                .withCustomItemModel(ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                        .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(doorItemTexture))));
    }

    /**
     * Creates a {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_TOP_RIGHT_OPEN} template. Handles item model.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <b>Block Model</b>
     * <ul>
     *  <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *  <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     * </ul>
     * <b>Item Model</b>
     * <ul>
     *  <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     * </ul>
     *
     * @param doorTopTexture The {@link ResourceLocation} representing the top-half texture of the door's top right (I.E. When the hinge is on the right side) when it's open.
     * @param doorBottomTexture The {@link ResourceLocation} representing the bottom-half texture of the door's top right (I.E. When the hinge is on the right side) when it's open.
     * @param doorItemTexture The {@link ResourceLocation} representing the item texture of the door.
     *
     * @return A {@link BlockModelDefinition} using the {@link ModelTemplates#DOOR_TOP_RIGHT_OPEN} template.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static BlockModelDefinition doorTopRightOpen(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture, ResourceLocation doorItemTexture) {
        return BlockModelDefinition.of(ModelTemplates.DOOR_TOP_RIGHT_OPEN)
                .withTextureMapping(TextureMapping.door(RegistryUtil.pickBlockPrefix(doorTopTexture), RegistryUtil.pickBlockPrefix(doorBottomTexture)))
                .withBlockRenderType(new ResourceLocation(RenderType.cutout().name))
                .withCustomItemModel(ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                        .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(doorItemTexture))));
    }

    /**
     * Creates an {@link ObjectArrayList} of {@linkplain BlockModelDefinition BlockModelDefinitions} with all requisite left door model templates ({@link ModelTemplates#DOOR_BOTTOM_LEFT}, {@link ModelTemplates#DOOR_BOTTOM_LEFT_OPEN},
     * {@link ModelTemplates#DOOR_TOP_LEFT}, {@link ModelTemplates#DOOR_TOP_LEFT_OPEN}).
     * <p>
     * <h3>Models</h3>
     * <ul>
     *  <li>{@link ModelTemplates#DOOR_BOTTOM_LEFT} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dblTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dblBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_BOTTOM_LEFT_OPEN} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dbloTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dbloBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_TOP_LEFT} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dtlTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dtlBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_TOP_LEFT_OPEN} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dtloTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dtloBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#FLAT_ITEM} (Item Model) -> <ul>
     *      <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     *  </ul></li>
     * </ul>
     *
     * @param dblTopTexture The {@link ResourceLocation} representing the top texture of the door's bottom left model.
     * @param dblBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's bottom left model.
     * @param dbloTopTexture The {@link ResourceLocation} representing the top texture of the door's bottom open left model.
     * @param dbloBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's bottom open left model.
     * @param dtlTopTexture The {@link ResourceLocation} representing the top texture of the door's top left model.
     * @param dtlBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's top left model.
     * @param dtloTopTexture The {@link ResourceLocation} representing the top texture of the door's top open left model.
     * @param dtloBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's top open left model.
     * @param doorItemTexture The {@link ResourceLocation} representing item texture of the door.
     *
     * @return An {@link ObjectArrayList} of all relevant door model definitions, for all of its models depending on its left hinge.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static ObjectArrayList<BlockModelDefinition> doorLeft(ResourceLocation dblTopTexture, ResourceLocation dblBottomTexture, ResourceLocation dbloTopTexture, ResourceLocation dbloBottomTexture, ResourceLocation dtlTopTexture, ResourceLocation dtlBottomTexture, ResourceLocation dtloTopTexture, ResourceLocation dtloBottomTexture, ResourceLocation doorItemTexture) {
        return ObjectArrayList.of(doorBottomLeft(dblTopTexture, dblBottomTexture, doorItemTexture), doorBottomLeftOpen(dbloTopTexture, dbloBottomTexture, doorItemTexture), doorTopLeft(dtlTopTexture, dtlBottomTexture, doorItemTexture), doorTopLeftOpen(dtloTopTexture, dtloBottomTexture, doorItemTexture));
    }

    /**
     * Overloaded variant of {@link #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)}.
     * Creates an {@link ObjectArrayList} of {@linkplain BlockModelDefinition BlockModelDefinitions} with all requisite left door model templates ({@link ModelTemplates#DOOR_BOTTOM_LEFT},
     * {@link ModelTemplates#DOOR_BOTTOM_LEFT_OPEN}, {@link ModelTemplates#DOOR_TOP_LEFT}, {@link ModelTemplates#DOOR_TOP_LEFT_OPEN}). Generalizes top and bottom texture references for both top and bottom
     * door models, for both open and closed left door variants.
     * <p>
     * <h3>Models</h3>
     * <ul>
     *  <li>{@link ModelTemplates#DOOR_BOTTOM_LEFT} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dblTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dblBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_BOTTOM_LEFT_OPEN} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dblTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dblBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_TOP_LEFT} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dtlTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dtlBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_TOP_LEFT_OPEN} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dtlTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dtlBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#FLAT_ITEM} (Item Model) -> <ul>
     *      <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     *  </ul></li>
     * </ul>
     *
     * @param dblTopTexture The {@link ResourceLocation} representing the top texture of the door's bottom left model.
     * @param dblBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's bottom left model.
     * @param dtlTopTexture The {@link ResourceLocation} representing the top texture of the door's top left model.
     * @param dtlBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's top left model.
     * @param doorItemTexture The {@link ResourceLocation} representing item texture of the door.
     *
     * @return An {@link ObjectArrayList} of all relevant door model definitions, for all of its models depending on its left hinge, with top and bottom texture references for both top and bottom door models
     * pre-defined.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static ObjectArrayList<BlockModelDefinition> doorLeft(ResourceLocation dblTopTexture, ResourceLocation dblBottomTexture, ResourceLocation dtlTopTexture, ResourceLocation dtlBottomTexture, ResourceLocation doorItemTexture) {
        return doorLeft(dblTopTexture, dblBottomTexture, dblTopTexture, dblBottomTexture, dtlTopTexture, dtlBottomTexture, dtlTopTexture, dtlBottomTexture, doorItemTexture);
    }

    /**
     * Overloaded variant of {@link #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)}. Creates an {@link ObjectArrayList} of
     * {@linkplain BlockModelDefinition BlockModelDefinitions} with all requisite left door model templates ({@link ModelTemplates#DOOR_BOTTOM_LEFT}, {@link ModelTemplates#DOOR_BOTTOM_LEFT_OPEN},
     * {@link ModelTemplates#DOOR_TOP_LEFT}, {@link ModelTemplates#DOOR_TOP_LEFT_OPEN}). Generalizes top and bottom texture references for all left door models/variants.
     * <p>
     * <h3>Models</h3>
     * <ul>
     *  <li>{@link ModelTemplates#DOOR_BOTTOM_LEFT} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_BOTTOM_LEFT_OPEN} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_TOP_LEFT} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_TOP_LEFT_OPEN} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#FLAT_ITEM} (Item Model) -> <ul>
     *      <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     *  </ul></li>
     * </ul>
     *
     * @param doorTopTexture The {@link ResourceLocation} representing the top texture of the door's left model.
     * @param doorBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's left model.
     * @param doorItemTexture The {@link ResourceLocation} representing item texture of the door.
     *
     * @return An {@link ObjectArrayList} of all relevant door model definitions, for all of its models depending on its left hinge, with top and bottom texture references for both top and bottom door models
     * pre-defined.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static ObjectArrayList<BlockModelDefinition> doorLeft(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture, ResourceLocation doorItemTexture) {
        return doorLeft(doorTopTexture, doorBottomTexture, doorTopTexture, doorBottomTexture, doorItemTexture);
    }

    /**
     * Creates an {@link ObjectArrayList} of {@linkplain BlockModelDefinition BlockModelDefinitions} with all requisite right door model templates ({@link ModelTemplates#DOOR_BOTTOM_RIGHT}, {@link ModelTemplates#DOOR_BOTTOM_RIGHT_OPEN},
     * {@link ModelTemplates#DOOR_TOP_RIGHT}, {@link ModelTemplates#DOOR_TOP_RIGHT_OPEN}).
     * <p>
     * <h3>Models</h3>
     * <ul>
     *  <li>{@link ModelTemplates#DOOR_BOTTOM_RIGHT} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dbrTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dbrBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_BOTTOM_RIGHT_OPEN} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dbroTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dbroBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_TOP_RIGHT} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dtrTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dtrBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_TOP_RIGHT_OPEN} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dtroTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dtroBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#FLAT_ITEM} (Item Model) -> <ul>
     *      <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     *  </ul></li>
     * </ul>
     *
     * @param dbrTopTexture The {@link ResourceLocation} representing the top texture of the door's bottom right model.
     * @param dbrBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's bottom right model.
     * @param dbroTopTexture The {@link ResourceLocation} representing the top texture of the door's bottom open right model.
     * @param dbroBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's bottom open right model.
     * @param dtrTopTexture The {@link ResourceLocation} representing the top texture of the door's top right model.
     * @param dtrBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's top right model.
     * @param dtroTopTexture The {@link ResourceLocation} representing the top texture of the door's top open right model.
     * @param dtroBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's top open right model.
     * @param doorItemTexture The {@link ResourceLocation} representing item texture of the door.
     *
     * @return An {@link ObjectArrayList} of all relevant door model definitions, for all of its models depending on its right hinge.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static ObjectArrayList<BlockModelDefinition> doorRight(ResourceLocation dbrTopTexture, ResourceLocation dbrBottomTexture, ResourceLocation dbroTopTexture, ResourceLocation dbroBottomTexture, ResourceLocation dtrTopTexture, ResourceLocation dtrBottomTexture, ResourceLocation dtroTopTexture, ResourceLocation dtroBottomTexture, ResourceLocation doorItemTexture) {
        return ObjectArrayList.of(doorBottomRight(dbrTopTexture, dbrBottomTexture, doorItemTexture), doorBottomRightOpen(dbroTopTexture, dbroBottomTexture, doorItemTexture), doorTopRight(dtrTopTexture, dtrBottomTexture, doorItemTexture), doorTopRightOpen(dtroTopTexture, dtroBottomTexture, doorItemTexture));
    }

    /**
     * Overloaded variant of {@link #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)}.
     * Creates an {@link ObjectArrayList} of {@linkplain BlockModelDefinition BlockModelDefinitions} with all requisite right door model templates ({@link ModelTemplates#DOOR_BOTTOM_RIGHT},
     * {@link ModelTemplates#DOOR_BOTTOM_RIGHT_OPEN}, {@link ModelTemplates#DOOR_TOP_RIGHT}, {@link ModelTemplates#DOOR_TOP_RIGHT_OPEN}). Generalizes top and bottom texture references for both top and bottom
     * door models, for both open and closed right door variants.
     * <p>
     * <h3>Models</h3>
     * <ul>
     *  <li>{@link ModelTemplates#DOOR_BOTTOM_RIGHT} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dblTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dblBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_BOTTOM_RIGHT_OPEN} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dblTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dblBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_TOP_RIGHT} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dtlTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dtlBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_TOP_RIGHT_OPEN} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code dtlTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code dtlBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#FLAT_ITEM} (Item Model) -> <ul>
     *      <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     *  </ul></li>
     * </ul>
     *
     * @param dbrTopTexture The {@link ResourceLocation} representing the top texture of the door's bottom right model.
     * @param dbrBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's bottom right model.
     * @param dtrTopTexture The {@link ResourceLocation} representing the top texture of the door's top right model.
     * @param dtrBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's top right model.
     * @param doorItemTexture The {@link ResourceLocation} representing item texture of the door.
     *
     * @return An {@link ObjectArrayList} of all relevant door model definitions, for all of its models depending on its left hinge, with top and bottom texture references for both top and bottom door models
     * pre-defined.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static ObjectArrayList<BlockModelDefinition> doorRight(ResourceLocation dbrTopTexture, ResourceLocation dbrBottomTexture, ResourceLocation dtrTopTexture, ResourceLocation dtrBottomTexture, ResourceLocation doorItemTexture) {
        return doorRight(dbrTopTexture, dbrBottomTexture, dbrTopTexture, dbrBottomTexture, dtrTopTexture, dtrBottomTexture, dtrTopTexture, dtrBottomTexture, doorItemTexture);
    }

    /**
     * Overloaded variant of {@link #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)}. Creates an {@link ObjectArrayList} of
     * {@linkplain BlockModelDefinition BlockModelDefinitions} with all requisite right door model templates ({@link ModelTemplates#DOOR_BOTTOM_RIGHT}, {@link ModelTemplates#DOOR_BOTTOM_RIGHT_OPEN},
     * {@link ModelTemplates#DOOR_TOP_RIGHT}, {@link ModelTemplates#DOOR_TOP_RIGHT_OPEN}). Generalizes top and bottom texture references for all right door models/variants.
     * <p>
     * <h3>Models</h3>
     * <ul>
     *  <li>{@link ModelTemplates#DOOR_BOTTOM_RIGHT} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_BOTTOM_RIGHT_OPEN} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_TOP_RIGHT} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#DOOR_TOP_RIGHT_OPEN} -> <ul>
     *      <li>{@link TextureSlot#TOP} -> {@code doorTopTexture}</li>
     *      <li>{@link TextureSlot#BOTTOM} -> {@code doorBottomTexture}</li>
     *  </ul></li>
     *
     *  <li>{@link ModelTemplates#FLAT_ITEM} (Item Model) -> <ul>
     *      <li>{@link TextureSlot#LAYER0} -> {@code doorItemTexture}</li>
     *  </ul></li>
     * </ul>
     *
     * @param doorTopTexture The {@link ResourceLocation} representing the top texture of the door's right model.
     * @param doorBottomTexture The {@link ResourceLocation} representing the bottom texture of the door's right model.
     * @param doorItemTexture The {@link ResourceLocation} representing item texture of the door.
     *
     * @return An {@link ObjectArrayList} of all relevant door model definitions, for all of its models depending on its right hinge, with top and bottom texture references for both top and bottom door models
     * pre-defined.
     *
     * @see #doorBottomLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorBottomRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopLeftOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRight(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorTopRightOpen(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorLeft(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #doorRight(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation, ResourceLocation)
     * @see #door(Supplier, ResourceLocation, ResourceLocation)
     * @see #door(Supplier)
     */
    public static ObjectArrayList<BlockModelDefinition> doorRight(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture, ResourceLocation doorItemTexture) {
        return doorRight(doorTopTexture, doorBottomTexture, doorTopTexture, doorBottomTexture, doorItemTexture);
    }

    public static ObjectArrayList<BlockModelDefinition> door(ResourceLocation dblTopTexture, ResourceLocation dblBottomTexture, ResourceLocation dbloTopTexture, ResourceLocation dbloBottomTexture, ResourceLocation dtlTopTexture, ResourceLocation dtlBottomTexture, ResourceLocation dtloTopTexture, ResourceLocation dtloBottomTexture, ResourceLocation dbrTopTexture, ResourceLocation dbrBottomTexture, ResourceLocation dbroTopTexture, ResourceLocation dbroBottomTexture, ResourceLocation dtrTopTexture, ResourceLocation dtrBottomTexture, ResourceLocation dtroTopTexture, ResourceLocation dtroBottomTexture, ResourceLocation doorItemTexture) {
        return ObjectArrayList.wrap(Sets.union(Set.copyOf(doorLeft(dblTopTexture, dblBottomTexture, dbloTopTexture, dbloBottomTexture, dtlTopTexture, dtlBottomTexture, dtloTopTexture, dtloBottomTexture, doorItemTexture)), Set.copyOf(doorRight(dbrTopTexture, dbrBottomTexture, dbroTopTexture, dbroBottomTexture, dtrTopTexture, dtrBottomTexture, dtroTopTexture, dtroBottomTexture, doorItemTexture))).toArray(BlockModelDefinition[]::new));
    }

    public static ObjectArrayList<BlockModelDefinition> door(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture, ResourceLocation doorOpenTopTexture, ResourceLocation doorOpenBottomTexture, ResourceLocation doorItemTexture) {
        return door(doorTopTexture, doorBottomTexture, doorOpenTopTexture, doorOpenBottomTexture, doorTopTexture, doorBottomTexture, doorOpenTopTexture, doorOpenBottomTexture, doorTopTexture, doorBottomTexture, doorOpenTopTexture, doorOpenBottomTexture, doorTopTexture, doorBottomTexture, doorOpenTopTexture, doorOpenBottomTexture, doorItemTexture);
    }

    public static ObjectArrayList<BlockModelDefinition> door(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture, ResourceLocation doorItemTexture) {
        return door(doorTopTexture, doorBottomTexture, doorTopTexture, doorBottomTexture, doorItemTexture);
    }

    public static ObjectArrayList<BlockModelDefinition> door(ResourceLocation doorTopTexture, ResourceLocation doorBottomTexture) {
        return door(doorTopTexture, doorBottomTexture, doorTopTexture, doorBottomTexture, new ResourceLocation(doorTopTexture.getNamespace(), "block_items/" +
                (doorTopTexture.getPath().contains("_top")
                        ? StringUtils.substringBefore(doorTopTexture.getPath(), "_top")
                        : doorTopTexture.getPath())));
    }

    public static ObjectArrayList<BlockModelDefinition> door(ResourceLocation doorTexture) {
        return door(doorTexture.withSuffix("_top"), doorTexture.withSuffix("_bottom"));
    }

    public static BlockStateDefinition door(Supplier<Block> targetBlock, ResourceLocation topLeftDoorModel, ResourceLocation topLeftDoorOpenModel, ResourceLocation bottomLeftDoorModel, ResourceLocation bottomLeftDoorOpenModel, ResourceLocation topRightDoorModel, ResourceLocation topRightDoorOpenModel, ResourceLocation bottomRightDoorModel, ResourceLocation bottomRightDoorOpenModel) {
        return BlockStateDefinition.of(targetBlock).withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get()).with(
                PropertyDispatch
                        .properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.DOUBLE_BLOCK_HALF, BlockStateProperties.DOOR_HINGE, BlockStateProperties.OPEN)
                        .select(Direction.EAST, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, false, Variant.variant()
                                .with(VariantProperties.MODEL, bottomLeftDoorModel))
                        .select(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, false, Variant.variant()
                                .with(VariantProperties.MODEL, bottomLeftDoorModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.WEST, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, false, Variant.variant()
                                .with(VariantProperties.MODEL, bottomLeftDoorModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, false, Variant.variant()
                                .with(VariantProperties.MODEL, bottomLeftDoorModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.EAST, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, false, Variant.variant()
                                .with(VariantProperties.MODEL, bottomRightDoorModel))
                        .select(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, false, Variant.variant()
                                .with(VariantProperties.MODEL, bottomRightDoorModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.WEST, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, false, Variant.variant()
                                .with(VariantProperties.MODEL, bottomRightDoorModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, false, Variant.variant()
                                .with(VariantProperties.MODEL, bottomRightDoorModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.EAST, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, true, Variant.variant()
                                .with(VariantProperties.MODEL, bottomLeftDoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, true, Variant.variant()
                                .with(VariantProperties.MODEL, bottomLeftDoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.WEST, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, true, Variant.variant()
                                .with(VariantProperties.MODEL, bottomLeftDoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHingeSide.LEFT, true, Variant.variant()
                                .with(VariantProperties.MODEL, bottomLeftDoorOpenModel))
                        .select(Direction.EAST, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, true, Variant.variant()
                                .with(VariantProperties.MODEL, bottomRightDoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.SOUTH, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, true, Variant.variant()
                                .with(VariantProperties.MODEL, bottomRightDoorOpenModel))
                        .select(Direction.WEST, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, true, Variant.variant()
                                .with(VariantProperties.MODEL, bottomRightDoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.NORTH, DoubleBlockHalf.LOWER, DoorHingeSide.RIGHT, true, Variant.variant()
                                .with(VariantProperties.MODEL, bottomRightDoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.EAST, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, false, Variant.variant()
                                .with(VariantProperties.MODEL, topLeftDoorModel))
                        .select(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, false, Variant.variant()
                                .with(VariantProperties.MODEL, topLeftDoorModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.WEST, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, false, Variant.variant()
                                .with(VariantProperties.MODEL, topLeftDoorModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, false, Variant.variant()
                                .with(VariantProperties.MODEL, topLeftDoorModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.EAST, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, false, Variant.variant()
                                .with(VariantProperties.MODEL, topRightDoorModel))
                        .select(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, false, Variant.variant()
                                .with(VariantProperties.MODEL, topRightDoorModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.WEST, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, false, Variant.variant()
                                .with(VariantProperties.MODEL, topRightDoorModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, false, Variant.variant()
                                .with(VariantProperties.MODEL, topRightDoorModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.EAST, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, true, Variant.variant()
                                .with(VariantProperties.MODEL, topLeftDoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, true, Variant.variant()
                                .with(VariantProperties.MODEL, topLeftDoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.WEST, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, true, Variant.variant()
                                .with(VariantProperties.MODEL, topLeftDoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHingeSide.LEFT, true, Variant.variant()
                                .with(VariantProperties.MODEL, topLeftDoorOpenModel))
                        .select(Direction.EAST, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, true, Variant.variant()
                                .with(VariantProperties.MODEL, topRightDoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.SOUTH, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, true, Variant.variant()
                                .with(VariantProperties.MODEL, topRightDoorOpenModel))
                        .select(Direction.WEST, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, true, Variant.variant()
                                .with(VariantProperties.MODEL, topRightDoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.NORTH, DoubleBlockHalf.UPPER, DoorHingeSide.RIGHT, true, Variant.variant()
                                .with(VariantProperties.MODEL, topRightDoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
        ));
    }

    public static BlockStateDefinition door(Supplier<Block> targetBlock, ResourceLocation topDoorModel, ResourceLocation bottomDoorModel) {
        return door(targetBlock, topDoorModel.withSuffix("_left"), topDoorModel.withSuffix("_left_open"), bottomDoorModel.withSuffix("_left"), bottomDoorModel.withSuffix("_left_open"), topDoorModel.withSuffix("_right"), topDoorModel.withSuffix("_right_open"), bottomDoorModel.withSuffix("_right"), bottomDoorModel.withSuffix("_right_open"));
    }

    public static BlockStateDefinition door(Supplier<Block> targetBlock) {
        return door(targetBlock, ModelLocationUtils.getModelLocation(targetBlock.get(), "_top"), ModelLocationUtils.getModelLocation(targetBlock.get(), "_bottom"));
    }

    public static BlockModelDefinition trapdoorOpen(ResourceLocation trapdoorTexture, ResourceLocation trapDoorItemParentBlockModel) {
        return BlockModelDefinition.of(ModelTemplates.TRAPDOOR_OPEN)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(trapdoorTexture)))
                .withBlockRenderType(new ResourceLocation(RenderType.cutout().name))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(trapDoorItemParentBlockModel)));
    }

    public static BlockModelDefinition trapdoorTop(ResourceLocation trapdoorTexture, ResourceLocation trapDoorItemParentBlockModel) {
        return BlockModelDefinition.of(ModelTemplates.TRAPDOOR_TOP)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(trapdoorTexture)))
                .withBlockRenderType(new ResourceLocation(RenderType.cutout().name))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(trapDoorItemParentBlockModel)));
    }

    public static BlockModelDefinition trapdoorBottom(ResourceLocation trapdoorTexture, ResourceLocation trapDoorItemParentBlockModel) {
        return BlockModelDefinition.of(ModelTemplates.TRAPDOOR_BOTTOM)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(trapdoorTexture)))
                .withBlockRenderType(new ResourceLocation(RenderType.cutout().name))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(trapDoorItemParentBlockModel)));
    }

    public static ObjectArrayList<BlockModelDefinition> trapdoor(ResourceLocation trapdoorOpenTexture, ResourceLocation trapDoorTopTexture, ResourceLocation trapdoorBottomTexture, ResourceLocation trapDoorItemParentBlockModel) {
        return ObjectArrayList.of(trapdoorBottom(trapdoorBottomTexture, trapDoorItemParentBlockModel), trapdoorTop(trapDoorTopTexture, trapDoorItemParentBlockModel), trapdoorOpen(trapdoorOpenTexture, trapDoorItemParentBlockModel));
    }

    public static ObjectArrayList<BlockModelDefinition> trapdoor(ResourceLocation trapdoorTexture, ResourceLocation trapDoorItemParentBlockModel) {
        return trapdoor(trapdoorTexture, trapdoorTexture, trapdoorTexture, trapDoorItemParentBlockModel);
    }

    public static BlockStateDefinition trapdoor(Supplier<Block> targetBlock, ResourceLocation trapdoorOpenModel, ResourceLocation trapdoorTopModel, ResourceLocation trapdoorBottomModel) {
        return BlockStateDefinition.of(targetBlock).withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get())
                .with(PropertyDispatch
                        .properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.OPEN)
                        .select(Direction.NORTH, Half.BOTTOM, false, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorBottomModel))
                        .select(Direction.SOUTH, Half.BOTTOM, false, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorBottomModel))
                        .select(Direction.EAST, Half.BOTTOM, false, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorBottomModel))
                        .select(Direction.WEST, Half.BOTTOM, false, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorBottomModel))
                        .select(Direction.NORTH, Half.TOP, false, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorTopModel))
                        .select(Direction.SOUTH, Half.TOP, false, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorTopModel))
                        .select(Direction.EAST, Half.TOP, false, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorTopModel))
                        .select(Direction.WEST, Half.TOP, false, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorTopModel))
                        .select(Direction.NORTH, Half.BOTTOM, true, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorOpenModel))
                        .select(Direction.SOUTH, Half.BOTTOM, true, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.EAST, Half.BOTTOM, true, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.WEST, Half.BOTTOM, true, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(Direction.NORTH, Half.TOP, true, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorOpenModel))
                        .select(Direction.SOUTH, Half.TOP, true, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.EAST, Half.TOP, true, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.WEST, Half.TOP, true, Variant.variant()
                                .with(VariantProperties.MODEL, trapdoorOpenModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
    }

    public static BlockStateDefinition trapdoor(Supplier<Block> targetBlock, ResourceLocation trapdoorModel) {
        return trapdoor(targetBlock, trapdoorModel.withSuffix("_open"), trapdoorModel.withSuffix("_top"), trapdoorModel.withSuffix("_bottom"));
    }

    public static BlockStateDefinition trapdoor(Supplier<Block> targetBlock) {
        return trapdoor(targetBlock, ModelLocationUtils.getModelLocation(targetBlock.get()));
    }

    /**
     * Creates a {@link BlockModelDefinition} with the {@link ModelTemplates#PARTICLE_ONLY} template. Handles item model.
     * <p>
     * <h3>Required Texture Slots</h3>
     * <b>Block Model</b>
     * <ul>
     *  <li>{@link TextureSlot#PARTICLE} -> {@code signParticleTexture}</li>
     * </ul>
     * <b>Item Model</b>
     * <ul>
     *  <li>{@link TextureSlot#LAYER0} -> {@code signItemTexture}</li>
     * </ul>
     *
     * @param signParticleTexture The {@link ResourceLocation} representing the texture of the particle emitted by the {@link BlockEntity} representation of the provided sign {@link Block}.
     * @param signItemTexture The {@link ResourceLocation} representing the texture of the item model of the provided sign {@link Block}.
     *
     * @return A new {@link BlockModelDefinition} with the {@link ModelTemplates#PARTICLE_ONLY} template
     *
     * @see #simpleBlock(Supplier)
     */
    public static BlockModelDefinition sign(ResourceLocation signParticleTexture, ResourceLocation signItemTexture) {
        return BlockModelDefinition.of(ModelTemplates.PARTICLE_ONLY)
                .withTextureMapping(TextureMapping.particle(RegistryUtil.pickBlockPrefix(signParticleTexture)))
                .withCustomItemModel(ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                        .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(signItemTexture))));
    }

    public static ObjectArrayList<BlockModelDefinition> sign(ResourceLocation signParticleTexture, ResourceLocation ceilingHangingSignTexture, ResourceLocation signItemTexture, String ceilingHangingSignModelName) {
        return ObjectArrayList.of(sign(signParticleTexture, signItemTexture), sign(ceilingHangingSignTexture, signItemTexture)
                .withCustomModelName(ceilingHangingSignModelName));
    }

    public static BlockModelDefinition stairsStraight(ResourceLocation straightStairsTopTexture, ResourceLocation straightStairsSideTexture, ResourceLocation straightStairsBottomTexture) {
        return BlockModelDefinition.of(ModelTemplates.STAIRS_STRAIGHT)
                .withTextureMapping(new TextureMapping()
                        .put(TextureSlot.TOP, RegistryUtil.pickBlockPrefix(straightStairsTopTexture))
                        .put(TextureSlot.SIDE, RegistryUtil.pickBlockPrefix(straightStairsSideTexture))
                        .put(TextureSlot.BOTTOM, RegistryUtil.pickBlockPrefix(straightStairsBottomTexture)));
    }

    public static BlockModelDefinition stairsStraight(ResourceLocation straightStairsTexture) {
        return stairsStraight(straightStairsTexture, straightStairsTexture, straightStairsTexture);
    }

    public static BlockModelDefinition stairsInner(ResourceLocation innerStairsTopTexture, ResourceLocation innerStairsSideTexture, ResourceLocation innerStairsBottomTexture) {
        return BlockModelDefinition.of(ModelTemplates.STAIRS_INNER)
                .withTextureMapping(new TextureMapping()
                        .put(TextureSlot.TOP, RegistryUtil.pickBlockPrefix(innerStairsTopTexture))
                        .put(TextureSlot.SIDE, RegistryUtil.pickBlockPrefix(innerStairsSideTexture))
                        .put(TextureSlot.BOTTOM, RegistryUtil.pickBlockPrefix(innerStairsBottomTexture)));
    }

    public static BlockModelDefinition stairsInner(ResourceLocation innerStairsTexture) {
        return stairsInner(innerStairsTexture, innerStairsTexture, innerStairsTexture);
    }

    public static BlockModelDefinition stairsOuter(ResourceLocation outerStairsTopTexture, ResourceLocation outerStairsSideTexture, ResourceLocation outerStairsBottomTexture) {
        return BlockModelDefinition.of(ModelTemplates.STAIRS_OUTER)
                .withTextureMapping(new TextureMapping()
                        .put(TextureSlot.TOP, RegistryUtil.pickBlockPrefix(outerStairsTopTexture))
                        .put(TextureSlot.SIDE, RegistryUtil.pickBlockPrefix(outerStairsSideTexture))
                        .put(TextureSlot.BOTTOM, RegistryUtil.pickBlockPrefix(outerStairsBottomTexture)));
    }

    public static BlockModelDefinition stairsOuter(ResourceLocation outerStairsTexture) {
        return stairsOuter(outerStairsTexture, outerStairsTexture, outerStairsTexture);
    }

    public static ObjectArrayList<BlockModelDefinition> stairs(ResourceLocation straightStairsTopTexture, ResourceLocation straightStairsSideTexture, ResourceLocation straightStairsBottomTexture, ResourceLocation innerStairsTopTexture, ResourceLocation innerStairsSideTexture, ResourceLocation innerStairsBottomTexture, ResourceLocation outerStairsTopTexture, ResourceLocation outerStairsSideTexture, ResourceLocation outerStairsBottomTexture) {
        return ObjectArrayList.of(stairsStraight(straightStairsTopTexture, straightStairsSideTexture, straightStairsBottomTexture), stairsInner(innerStairsTopTexture, innerStairsSideTexture, innerStairsBottomTexture), stairsOuter(outerStairsTopTexture, outerStairsSideTexture, outerStairsBottomTexture));
    }

    public static ObjectArrayList<BlockModelDefinition> stairs(ResourceLocation stairsTopTexture, ResourceLocation stairsSideTexture, ResourceLocation stairsBottomTexture) {
        return stairs(stairsTopTexture, stairsSideTexture, stairsBottomTexture, stairsTopTexture, stairsSideTexture, stairsBottomTexture, stairsTopTexture, stairsSideTexture, stairsBottomTexture);
    }

    public static ObjectArrayList<BlockModelDefinition> stairs(ResourceLocation stairsTexture) {
        return stairs(stairsTexture, stairsTexture, stairsTexture);
    }

    public static BlockStateDefinition stairs(Supplier<Block> targetBlock, ResourceLocation straightStairsModel, ResourceLocation innerStairsModel, ResourceLocation outerStairsModel) {
        return BlockStateDefinition.of(targetBlock).withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get())
                .with(PropertyDispatch
                        .properties(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.HALF, BlockStateProperties.STAIRS_SHAPE)
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, straightStairsModel))
                        .select(Direction.WEST, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, straightStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, straightStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.BOTTOM, StairsShape.STRAIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, straightStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel))
                        .select(Direction.WEST, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel))
                        .select(Direction.NORTH, Half.BOTTOM, StairsShape.OUTER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel))
                        .select(Direction.WEST, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel))
                        .select(Direction.NORTH, Half.BOTTOM, StairsShape.INNER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.TOP, StairsShape.STRAIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, straightStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.TOP, StairsShape.STRAIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, straightStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.TOP, StairsShape.STRAIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, straightStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.TOP, StairsShape.STRAIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, straightStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.TOP, StairsShape.OUTER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.TOP, StairsShape.OUTER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, outerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.TOP, StairsShape.INNER_RIGHT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.EAST, Half.TOP, StairsShape.INNER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.WEST, Half.TOP, StairsShape.INNER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.SOUTH, Half.TOP, StairsShape.INNER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(Direction.NORTH, Half.TOP, StairsShape.INNER_LEFT, Variant.variant()
                                .with(VariantProperties.MODEL, innerStairsModel)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                .with(VariantProperties.UV_LOCK, true))));
    }

    public static BlockStateDefinition stairs(Supplier<Block> targetBlock) {
        return stairs(targetBlock, ModelLocationUtils.getModelLocation(targetBlock.get()), ModelLocationUtils.getModelLocation(targetBlock.get(), "_inner"), ModelLocationUtils.getModelLocation(targetBlock.get(), "_outer"));
    }

    public static BlockModelDefinition buttonDefault(ResourceLocation buttonTexture, ResourceLocation buttonItemParentBlockModel) {
        return BlockModelDefinition.of(ModelTemplates.BUTTON)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(buttonTexture)))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(buttonItemParentBlockModel)));
    }

    public static BlockModelDefinition buttonPressed(ResourceLocation buttonTexture, ResourceLocation buttonItemParentBlockModel) {
        return BlockModelDefinition.of(ModelTemplates.BUTTON_PRESSED)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(buttonTexture)))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(buttonItemParentBlockModel)));
    }

    public static BlockModelDefinition buttonInventory(ResourceLocation buttonTexture, ResourceLocation buttonItemParentBlockModel) {
        return BlockModelDefinition.of(ModelTemplates.BUTTON_INVENTORY)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(buttonTexture)))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(buttonItemParentBlockModel)));
    }

    public static ObjectArrayList<BlockModelDefinition> button(ResourceLocation buttonTexture, ResourceLocation buttonPressedTexture, ResourceLocation buttonInventoryTexture, ResourceLocation buttonItemParentBlockModel) {
        return ObjectArrayList.of(buttonInventory(buttonInventoryTexture, buttonItemParentBlockModel), buttonDefault(buttonTexture, buttonItemParentBlockModel), buttonPressed(buttonPressedTexture, buttonItemParentBlockModel));
    }

    public static ObjectArrayList<BlockModelDefinition> button(ResourceLocation buttonTexture, ResourceLocation buttonItemParentBlockModel) {
        return button(buttonTexture, buttonTexture, buttonTexture, buttonItemParentBlockModel);
    }

    public static BlockStateDefinition button(Supplier<Block> targetBlock, ResourceLocation buttonModel, ResourceLocation buttonPressedModel) {
        return BlockStateDefinition.of(targetBlock).withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get())
                .with(PropertyDispatch
                        .property(BlockStateProperties.POWERED)
                        .select(false, Variant.variant()
                                .with(VariantProperties.MODEL, buttonModel))
                        .select(true, Variant.variant()
                                .with(VariantProperties.MODEL, buttonPressedModel)))
                .with(PropertyDispatch
                        .properties(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
                        .select(AttachFace.FLOOR, Direction.EAST, Variant.variant()
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(AttachFace.FLOOR, Direction.WEST, Variant.variant()
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .select(AttachFace.FLOOR, Direction.SOUTH, Variant.variant()
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(AttachFace.FLOOR, Direction.NORTH, Variant.variant())
                        .select(AttachFace.WALL, Direction.EAST, Variant.variant()
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(AttachFace.WALL, Direction.WEST, Variant.variant()
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(AttachFace.WALL, Direction.SOUTH, Variant.variant()
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(AttachFace.WALL, Direction.NORTH, Variant.variant()
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.UV_LOCK, true))
                        .select(AttachFace.CEILING, Direction.EAST, Variant.variant()
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                        .select(AttachFace.CEILING, Direction.WEST, Variant.variant()
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                        .select(AttachFace.CEILING, Direction.SOUTH, Variant.variant()
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                        .select(AttachFace.CEILING, Direction.NORTH, Variant.variant()
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))));
    }

    public static BlockStateDefinition button(Supplier<Block> targetBlock, ResourceLocation buttonModel) {
        return button(targetBlock, buttonModel, buttonModel.withSuffix("_pressed"));
    }

    public static BlockStateDefinition button(Supplier<Block> targetBlock) {
        return button(targetBlock, ModelLocationUtils.getModelLocation(targetBlock.get()));
    }

    public static BlockModelDefinition pressurePlateDown(ResourceLocation pressurePlateTexture) {
        return BlockModelDefinition.of(ModelTemplates.PRESSURE_PLATE_DOWN)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(pressurePlateTexture)));
    }

    public static BlockModelDefinition pressurePlateUp(ResourceLocation pressurePlateTexture) {
        return BlockModelDefinition.of(ModelTemplates.PRESSURE_PLATE_UP)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(pressurePlateTexture)));
    }

    public static ObjectArrayList<BlockModelDefinition> pressurePlate(ResourceLocation pressurePlateDownTexture, ResourceLocation pressurePlateUpTexture) {
        return ObjectArrayList.of(pressurePlateUp(pressurePlateUpTexture), pressurePlateDown(pressurePlateDownTexture));
    }

    public static ObjectArrayList<BlockModelDefinition> pressurePlate(ResourceLocation pressurePlateTexture) {
        return pressurePlate(pressurePlateTexture, pressurePlateTexture);
    }

    public static BlockStateDefinition pressurePlate(Supplier<Block> targetBlock, ResourceLocation pressurePlateDownModel, ResourceLocation pressurePlateUpModel) {
        return BlockStateDefinition.of(targetBlock).withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get())
                .with(PropertyDispatch
                        .property(BlockStateProperties.POWERED)
                        .select(true, Variant.variant()
                                .with(VariantProperties.MODEL, pressurePlateDownModel))
                        .select(false, Variant.variant()
                                .with(VariantProperties.MODEL, pressurePlateUpModel))));
    }

    public static BlockStateDefinition pressurePlate(Supplier<Block> targetBlock) {
        return pressurePlate(targetBlock, ModelLocationUtils.getModelLocation(targetBlock.get(), "_down"), ModelLocationUtils.getModelLocation(targetBlock.get()));
    }

    public static BlockModelDefinition fencePost(ResourceLocation fenceTexture, ResourceLocation fenceItemParentBlockModel) {
        return BlockModelDefinition.of(ModelTemplates.FENCE_POST)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(fenceTexture)))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(fenceItemParentBlockModel)));
    }

    public static BlockModelDefinition fenceSide(ResourceLocation fenceTexture, ResourceLocation fenceItemParentBlockModel) {
        return BlockModelDefinition.of(ModelTemplates.FENCE_SIDE)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(fenceTexture)))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(fenceItemParentBlockModel)));
    }

    public static BlockModelDefinition fenceInventory(ResourceLocation fenceTexture, ResourceLocation fenceItemParentBlockModel) {
        return BlockModelDefinition.of(ModelTemplates.FENCE_INVENTORY)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(fenceTexture)))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(fenceItemParentBlockModel)));
    }

    public static ObjectArrayList<BlockModelDefinition> fence(ResourceLocation fencePostTexture, ResourceLocation fenceSideTexture, ResourceLocation fenceInventoryTexture, ResourceLocation fenceItemParentBlockModel) {
        return ObjectArrayList.of(fenceInventory(fenceInventoryTexture, fenceItemParentBlockModel), fencePost(fencePostTexture, fenceItemParentBlockModel), fenceSide(fenceSideTexture, fenceItemParentBlockModel));
    }

    public static ObjectArrayList<BlockModelDefinition> fence(ResourceLocation fenceTexture, ResourceLocation fenceItemParentBlockModel) {
        return fence(fenceTexture, fenceTexture, fenceTexture, fenceItemParentBlockModel);
    }

    public static BlockStateDefinition fence(Supplier<Block> targetBlock, ResourceLocation fencePostModel, ResourceLocation fenceSideModel) {
        return BlockStateDefinition.of(targetBlock).withBlockStateSupplier(MultiPartGenerator.multiPart(targetBlock.get())
                .with(Variant.variant()
                        .with(VariantProperties.MODEL, fencePostModel))
                .with(Condition.condition()
                        .term(BlockStateProperties.NORTH, true), Variant.variant()
                        .with(VariantProperties.MODEL, fenceSideModel)
                        .with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition()
                        .term(BlockStateProperties.EAST, true), Variant.variant()
                        .with(VariantProperties.MODEL, fenceSideModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                        .with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition()
                        .term(BlockStateProperties.SOUTH, true), Variant.variant()
                        .with(VariantProperties.MODEL, fenceSideModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                        .with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition()
                        .term(BlockStateProperties.WEST, true), Variant.variant()
                        .with(VariantProperties.MODEL, fenceSideModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                        .with(VariantProperties.UV_LOCK, true)));
    }

    public static BlockStateDefinition fence(Supplier<Block> targetBlock) {
        return fence(targetBlock, ModelLocationUtils.getModelLocation(targetBlock.get(), "_post"), ModelLocationUtils.getModelLocation(targetBlock.get(), "_side"));
    }

    public static BlockModelDefinition fenceGateClosed(ResourceLocation fenceGateTexture) {
        return BlockModelDefinition.of(ModelTemplates.FENCE_GATE_CLOSED)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(fenceGateTexture)));
    }

    public static BlockModelDefinition fenceGateOpen(ResourceLocation fenceGateOpenTexture) {
        return BlockModelDefinition.of(ModelTemplates.FENCE_GATE_OPEN)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(fenceGateOpenTexture)));
    }

    public static BlockModelDefinition fenceGateWall(ResourceLocation fenceGateWallTexture) {
        return BlockModelDefinition.of(ModelTemplates.FENCE_GATE_WALL_CLOSED)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(fenceGateWallTexture)));
    }

    public static BlockModelDefinition fenceGateWallOpen(ResourceLocation fenceGateWallOpenTexture) {
        return BlockModelDefinition.of(ModelTemplates.FENCE_GATE_WALL_OPEN)
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(fenceGateWallOpenTexture)));
    }

    public static ObjectArrayList<BlockModelDefinition> fenceGate(ResourceLocation fenceGateTexture, ResourceLocation fenceGateOpenTexture, ResourceLocation fenceGateWallTexture, ResourceLocation fenceGateWallOpenTexture) {
        return ObjectArrayList.of(fenceGateClosed(fenceGateTexture), fenceGateOpen(fenceGateOpenTexture), fenceGateWall(fenceGateWallTexture), fenceGateWallOpen(fenceGateWallOpenTexture));
    }

    public static ObjectArrayList<BlockModelDefinition> fenceGate(ResourceLocation fenceGateTexture) {
        return fenceGate(fenceGateTexture, fenceGateTexture, fenceGateTexture, fenceGateTexture);
    }

    public static BlockStateDefinition fenceGate(Supplier<Block> targetBlock, ResourceLocation fenceGateModel, ResourceLocation fenceGateOpenModel, ResourceLocation fenceGateWallModel, ResourceLocation fenceGateWallOpenModel, boolean uvLock) {
        return BlockStateDefinition.of(targetBlock).withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get(), Variant.variant().with(VariantProperties.UV_LOCK, uvLock))
                .with(PropertyDispatch
                        .property(BlockStateProperties.HORIZONTAL_FACING)
                        .select(Direction.SOUTH, Variant.variant())
                        .select(Direction.WEST, Variant.variant()
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .select(Direction.NORTH, Variant.variant()
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .select(Direction.EAST, Variant.variant()
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)))
                .with(PropertyDispatch
                        .properties(BlockStateProperties.IN_WALL, BlockStateProperties.OPEN)
                        .select(false, false, Variant.variant()
                                .with(VariantProperties.MODEL, fenceGateModel))
                        .select(true, false, Variant.variant()
                                .with(VariantProperties.MODEL, fenceGateWallModel))
                        .select(false, true, Variant.variant()
                                .with(VariantProperties.MODEL, fenceGateOpenModel))
                        .select(true, true, Variant.variant()
                                .with(VariantProperties.MODEL, fenceGateWallOpenModel))));
    }

    public static BlockStateDefinition fenceGate(Supplier<Block> targetBlock, ResourceLocation fenceGateModel, ResourceLocation fenceGateOpenModel, ResourceLocation fenceGateWallModel, ResourceLocation fenceGateWallOpenModel) {
        return fenceGate(targetBlock, fenceGateModel, fenceGateOpenModel, fenceGateWallModel, fenceGateWallOpenModel, true);
    }

    public static BlockStateDefinition fenceGate(Supplier<Block> targetBlock, ResourceLocation fenceGateModel) {
        return fenceGate(targetBlock, fenceGateModel, fenceGateModel.withSuffix("_open"), fenceGateModel.withSuffix("_wall"), fenceGateModel.withSuffix("_wall_open"));
    }

    public static BlockStateDefinition fenceGate(Supplier<Block> targetBlock) {
        return fenceGate(targetBlock, ModelLocationUtils.getModelLocation(targetBlock.get()));
    }

    public static BlockModelDefinition wallPost(ResourceLocation wallPostTexture, ResourceLocation wallInventoryModel) {
        return BlockModelDefinition.of(ModelTemplates.WALL_POST)
                .withTextureMapping(new TextureMapping().put(TextureSlot.WALL, RegistryUtil.pickBlockPrefix(wallPostTexture)))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(wallInventoryModel)));
    }

    public static BlockModelDefinition wallSide(ResourceLocation wallSideTexture, ResourceLocation wallInventoryModel) {
        return BlockModelDefinition.of(ModelTemplates.WALL_LOW_SIDE)
                .withTextureMapping(new TextureMapping().put(TextureSlot.WALL, RegistryUtil.pickBlockPrefix(wallSideTexture)))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(wallInventoryModel)));
    }

    public static BlockModelDefinition wallSideTall(ResourceLocation wallSideTallTexture, ResourceLocation wallInventoryModel) {
        return BlockModelDefinition.of(ModelTemplates.WALL_TALL_SIDE)
                .withTextureMapping(new TextureMapping().put(TextureSlot.WALL, RegistryUtil.pickBlockPrefix(wallSideTallTexture)))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(wallInventoryModel)));
    }

    public static BlockModelDefinition wallInventory(ResourceLocation wallInventoryTexture, ResourceLocation wallInventoryModel) {
        return BlockModelDefinition.of(ModelTemplates.WALL_INVENTORY)
                .withTextureMapping(new TextureMapping().put(TextureSlot.WALL, RegistryUtil.pickBlockPrefix(wallInventoryTexture)))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(wallInventoryModel)));
    }

    public static ObjectArrayList<BlockModelDefinition> wall(ResourceLocation wallPostTexture, ResourceLocation wallSideTexture, ResourceLocation wallSideTallTexture, ResourceLocation wallInventoryTexture, ResourceLocation wallInventoryModel) {
        return ObjectArrayList.of(wallPost(wallPostTexture, wallInventoryModel), wallSide(wallSideTexture, wallInventoryModel), wallSideTall(wallSideTallTexture, wallInventoryModel), wallInventory(wallInventoryTexture, wallInventoryModel));
    }

    public static ObjectArrayList<BlockModelDefinition> wall(ResourceLocation wallTexture, ResourceLocation wallInventoryModel) {
        return wall(wallTexture, wallTexture, wallTexture, wallTexture, wallInventoryModel);
    }

    public static BlockStateDefinition wall(Supplier<Block> targetBlock, ResourceLocation wallPostModel, ResourceLocation wallSideModel, ResourceLocation wallSideTallModel) {
        return BlockStateDefinition.of(targetBlock).withBlockStateSupplier(MultiPartGenerator.multiPart(targetBlock.get())
                .with(Condition.condition()
                        .term(BlockStateProperties.UP, true), Variant.variant()
                        .with(VariantProperties.MODEL, wallPostModel))
                .with(Condition.condition()
                        .term(BlockStateProperties.NORTH_WALL, WallSide.LOW), Variant.variant()
                        .with(VariantProperties.MODEL, wallSideModel)
                        .with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition()
                        .term(BlockStateProperties.EAST_WALL, WallSide.LOW), Variant.variant()
                        .with(VariantProperties.MODEL, wallSideModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                        .with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition()
                        .term(BlockStateProperties.SOUTH_WALL, WallSide.LOW), Variant.variant()
                        .with(VariantProperties.MODEL, wallSideModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                        .with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition()
                        .term(BlockStateProperties.WEST_WALL, WallSide.LOW), Variant.variant()
                        .with(VariantProperties.MODEL, wallSideModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                        .with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition()
                        .term(BlockStateProperties.NORTH_WALL, WallSide.TALL), Variant.variant()
                        .with(VariantProperties.MODEL, wallSideTallModel)
                        .with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition()
                        .term(BlockStateProperties.EAST_WALL, WallSide.TALL), Variant.variant()
                        .with(VariantProperties.MODEL, wallSideTallModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)
                        .with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition()
                        .term(BlockStateProperties.SOUTH_WALL, WallSide.TALL), Variant.variant()
                        .with(VariantProperties.MODEL, wallSideTallModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
                        .with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition()
                        .term(BlockStateProperties.WEST_WALL, WallSide.TALL), Variant.variant()
                        .with(VariantProperties.MODEL, wallSideTallModel)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)
                        .with(VariantProperties.UV_LOCK, true)));
    }

    public static BlockStateDefinition wall(Supplier<Block> targetBlock) {
        return wall(targetBlock, ModelLocationUtils.getModelLocation(targetBlock.get(), "_post"), ModelLocationUtils.getModelLocation(targetBlock.get(), "_side"), ModelLocationUtils.getModelLocation(targetBlock.get(), "_side_tall"));
    }

    public static BlockModelDefinition standingTorch(ResourceLocation torchTexture, ResourceLocation torchItemTexture) {
        return BlockModelDefinition.of(ModelTemplates.TORCH)
                .withTextureMapping(TextureMapping.torch(RegistryUtil.pickBlockPrefix(torchTexture)))
                .withCustomItemModel(ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                        .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(torchItemTexture))));
    }

    public static BlockModelDefinition wallTorch(ResourceLocation torchTexture, ResourceLocation torchItemTexture) {
        return BlockModelDefinition.of(ModelTemplates.WALL_TORCH)
                .withTextureMapping(TextureMapping.torch(RegistryUtil.pickBlockPrefix(torchTexture)))
                .withCustomItemModel(ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                        .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(torchItemTexture))));
    }

    public static ObjectArrayList<BlockModelDefinition> torch(ResourceLocation torchTexture, ResourceLocation wallTorchTexture, ResourceLocation torchItemTexture) {
        return ObjectArrayList.of(standingTorch(torchTexture, torchItemTexture), wallTorch(wallTorchTexture, torchItemTexture));
    }

    public static ObjectArrayList<BlockModelDefinition> torch(ResourceLocation torchTexture, ResourceLocation torchItemTexture) {
        return torch(torchTexture, torchTexture, torchItemTexture);
    }

    public static ObjectArrayList<BlockModelDefinition> fruitableLeaves(ResourceLocation unripeLeavesTexture, ResourceLocation ripeLeavesTexture) {
        return ObjectArrayList.of(cubeAll(unripeLeavesTexture).withBlockRenderType(new ResourceLocation(RenderType.cutoutMipped().name)), cubeAll(ripeLeavesTexture).withBlockRenderType(new ResourceLocation(RenderType.cutoutMipped().name)).withCustomModelName(ripeLeavesTexture.getPath().substring(ripeLeavesTexture.getPath().lastIndexOf("/") + 1)));
    }

    public static ObjectArrayList<BlockModelDefinition> fruitableLeaves(ResourceLocation leavesTexture) {
        return fruitableLeaves(leavesTexture, leavesTexture.withSuffix("_ripe"));
    }

    public static BlockStateDefinition fruitableLeaves(Supplier<Block> targetBlock, ResourceLocation unripeLeavesModel, ResourceLocation ripeLeavesModel) {
        return BlockStateDefinition.of(targetBlock).withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get())
                .with(PropertyDispatch
                        .property(CABlockStateProperties.RIPE)
                        .select(true, Variant.variant()
                                .with(VariantProperties.MODEL, ripeLeavesModel))
                        .select(false, Variant.variant()
                                .with(VariantProperties.MODEL, unripeLeavesModel))));
    }

    public static BlockStateDefinition fruitableLeaves(Supplier<Block> targetBlock) {
        return fruitableLeaves(targetBlock, ModelLocationUtils.getModelLocation(targetBlock.get()), ModelLocationUtils.getModelLocation(targetBlock.get()).withSuffix("_ripe"));
    }

    public static BlockModelDefinition leafCarpetDefault(ResourceLocation leafCarpetTexture, ResourceLocation leafCarpetItemModelParent) {
        return BlockModelDefinition.of(CAModelTemplates.LEAF_CARPET)
                .withBlockRenderType(new ResourceLocation(RenderType.cutoutMipped().name))
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(leafCarpetTexture)))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(leafCarpetItemModelParent)));
    }

    public static BlockModelDefinition leafCarpetInventory(ResourceLocation leafCarpetTexture, ResourceLocation leafCarpetItemModelParent) {
        return BlockModelDefinition.of(CAModelTemplates.LEAF_CARPET_INVENTORY)
                .withBlockRenderType(new ResourceLocation(RenderType.cutoutMipped().name))
                .withTextureMapping(TextureMapping.defaultTexture(RegistryUtil.pickBlockPrefix(leafCarpetTexture)))
                .withCustomItemModel(ItemModelDefinition.of(CAModelTemplates.of(leafCarpetItemModelParent)));
    }

    public static ObjectArrayList<BlockModelDefinition> leafCarpet(ResourceLocation leafCarpetTexture, ResourceLocation leafCarpetInventoryTexture, ResourceLocation leafCarpetItemModelParent) {
        return ObjectArrayList.of(leafCarpetInventory(leafCarpetInventoryTexture, leafCarpetItemModelParent), leafCarpetDefault(leafCarpetTexture, leafCarpetItemModelParent));
    }

    public static ObjectArrayList<BlockModelDefinition> leafCarpet(ResourceLocation leafCarpetTexture, ResourceLocation leafCarpetItemModelParent) {
        return leafCarpet(leafCarpetTexture, leafCarpetTexture, leafCarpetItemModelParent);
    }

    public static BlockStateDefinition leafCarpet(Supplier<Block> targetBlock) {
        ResourceLocation targetModelLoc = ModelLocationUtils.getModelLocation(targetBlock.get());
        return BlockStateDefinition.of(targetBlock)
                .withBlockStateSupplier(MultiPartGenerator.multiPart(targetBlock.get())
                        .with(Condition.condition()
                                .term(PipeBlock.NORTH, true), Variant.variant() // Gotta use PipeBlock properties or it just won't work :skull:
                                .with(VariantProperties.MODEL, targetModelLoc))
                        .with(Condition.condition()
                                .term(PipeBlock.DOWN, false)
                                .term(PipeBlock.NORTH, false)
                                .term(PipeBlock.EAST, false)
                                .term(PipeBlock.SOUTH, false)
                                .term(PipeBlock.UP, false)
                                .term(PipeBlock.WEST, false), Variant.variant()
                                .with(VariantProperties.MODEL, targetModelLoc))
                        .with(Condition.condition()
                                .term(PipeBlock.EAST, true), Variant.variant()
                                .with(VariantProperties.MODEL, targetModelLoc)
                                .with(VariantProperties.UV_LOCK, true)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .with(Condition.condition()
                                .term(PipeBlock.DOWN, false)
                                .term(PipeBlock.NORTH, false)
                                .term(PipeBlock.EAST, false)
                                .term(PipeBlock.SOUTH, false)
                                .term(PipeBlock.UP, false)
                                .term(PipeBlock.WEST, false), Variant.variant()
                                .with(VariantProperties.MODEL, targetModelLoc)
                                .with(VariantProperties.UV_LOCK, true)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                        .with(Condition.condition()
                                .term(PipeBlock.SOUTH, true), Variant.variant()
                                .with(VariantProperties.MODEL, targetModelLoc)
                                .with(VariantProperties.UV_LOCK, true)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .with(Condition.condition()
                                .term(PipeBlock.DOWN, false)
                                .term(PipeBlock.NORTH, false)
                                .term(PipeBlock.EAST, false)
                                .term(PipeBlock.SOUTH, false)
                                .term(PipeBlock.UP, false)
                                .term(PipeBlock.WEST, false), Variant.variant()
                                .with(VariantProperties.MODEL, targetModelLoc)
                                .with(VariantProperties.UV_LOCK, true)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                        .with(Condition.condition()
                                .term(PipeBlock.WEST, true), Variant.variant()
                                .with(VariantProperties.MODEL, targetModelLoc)
                                .with(VariantProperties.UV_LOCK, true)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .with(Condition.condition()
                                .term(PipeBlock.DOWN, false)
                                .term(PipeBlock.NORTH, false)
                                .term(PipeBlock.EAST, false)
                                .term(PipeBlock.SOUTH, false)
                                .term(PipeBlock.UP, false)
                                .term(PipeBlock.WEST, false), Variant.variant()
                                .with(VariantProperties.MODEL, targetModelLoc)
                                .with(VariantProperties.UV_LOCK, true)
                                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                        .with(Condition.condition()
                                .term(PipeBlock.UP, true), Variant.variant()
                                .with(VariantProperties.MODEL, targetModelLoc)
                                .with(VariantProperties.UV_LOCK, true)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
                        .with(Condition.condition()
                                .term(PipeBlock.DOWN, false)
                                .term(PipeBlock.NORTH, false)
                                .term(PipeBlock.EAST, false)
                                .term(PipeBlock.SOUTH, false)
                                .term(PipeBlock.UP, false)
                                .term(PipeBlock.WEST, false), Variant.variant()
                                .with(VariantProperties.MODEL, targetModelLoc)
                                .with(VariantProperties.UV_LOCK, true)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
                        .with(Condition.condition()
                                .term(PipeBlock.DOWN, true), Variant.variant()
                                .with(VariantProperties.MODEL, targetModelLoc)
                                .with(VariantProperties.UV_LOCK, true)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                        .with(Condition.condition()
                                .term(PipeBlock.DOWN, false)
                                .term(PipeBlock.NORTH, false)
                                .term(PipeBlock.EAST, false)
                                .term(PipeBlock.SOUTH, false)
                                .term(PipeBlock.UP, false)
                                .term(PipeBlock.WEST, false), Variant.variant()
                                .with(VariantProperties.MODEL, targetModelLoc)
                                .with(VariantProperties.UV_LOCK, true)
                                .with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                );
    }

    public static BlockModelDefinition leaves(ResourceLocation leavesTexture) {
        return cubeAll(leavesTexture)
                .withBlockRenderType(new ResourceLocation(RenderType.cutoutMipped().name));
    }

    public static ItemModelDefinition generatedBlock(ResourceLocation itemTexture) {
        return ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickBlockPrefix(itemTexture)));
    }

    public static ItemModelDefinition generated(ResourceLocation itemTexture) {
        return ItemModelDefinition.of(ModelTemplates.FLAT_ITEM)
                .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(itemTexture)));
    }

    public static ItemModelDefinition handheld(ResourceLocation itemTexture) {
        return ItemModelDefinition.of(ModelTemplates.FLAT_HANDHELD_ITEM)
                .withTextureMapping(TextureMapping.layer0(RegistryUtil.pickItemPrefix(itemTexture)));
    }

    public static BlockModelDefinition grassBlock(ResourceLocation particleTexture, ResourceLocation bottomTexture, ResourceLocation topTexture, ResourceLocation sideTexture, ResourceLocation overlayTexture) {
        return BlockModelDefinition.of(CAModelTemplates.GRASS_BLOCK)
                .withTextureMapping(CATextureSlots.grassBlock(RegistryUtil.pickBlockPrefix(particleTexture), RegistryUtil.pickBlockPrefix(bottomTexture), RegistryUtil.pickBlockPrefix(topTexture), RegistryUtil.pickBlockPrefix(sideTexture), RegistryUtil.pickBlockPrefix(overlayTexture)))
                .withBlockRenderType(new ResourceLocation(RenderType.cutoutMipped().name));
    }

    public static BlockModelDefinition grassBlock(ResourceLocation dirtTexture, ResourceLocation topTexture, ResourceLocation sideTexture, ResourceLocation overlayTexture) {
        return grassBlock(dirtTexture, dirtTexture, topTexture, sideTexture, overlayTexture);
    }

    public static BlockModelDefinition grassBlock(ResourceLocation grassBlockReferenceTextureLoc) {
        ResourceLocation dirtLoc = grassBlockReferenceTextureLoc.withPath(grassBlockReferenceTextureLoc.getPath().replace("_grass_block", "_dirt"));
        return grassBlock(dirtLoc, dirtLoc, grassBlockReferenceTextureLoc.withSuffix("_top"), grassBlockReferenceTextureLoc.withSuffix("_side"), grassBlockReferenceTextureLoc.withSuffix("_side_overlay"));
    }

    public static ObjectArrayList<BlockModelDefinition> doublePlant(ResourceLocation topPlantTexture, ResourceLocation bottomPlantTexture) {
        return ObjectArrayList.of(crossCutout(topPlantTexture).withCustomModelName(topPlantTexture.getPath().substring(topPlantTexture.getPath().lastIndexOf("/") + 1)), crossCutout(bottomPlantTexture).withCustomModelName(bottomPlantTexture.getPath().substring(bottomPlantTexture.getPath().lastIndexOf("/") + 1)).withCustomItemModel(generatedBlock(topPlantTexture)));
    }

    public static ObjectArrayList<BlockModelDefinition> doublePlant(ResourceLocation baseTallPlantTexture) {
        return doublePlant(baseTallPlantTexture.withSuffix("_top"), baseTallPlantTexture.withSuffix("_bottom"));
    }

    public static ObjectArrayList<BlockModelDefinition> tintedDoublePlant(ResourceLocation topPlantTexture, ResourceLocation bottomPlantTexture) {
        return ObjectArrayList.of(tintedCrossCutout(topPlantTexture).withCustomModelName(topPlantTexture.getPath().substring(topPlantTexture.getPath().lastIndexOf("/") + 1)), tintedCrossCutout(bottomPlantTexture).withCustomModelName(bottomPlantTexture.getPath().substring(bottomPlantTexture.getPath().lastIndexOf("/") + 1)).withCustomItemModel(generatedBlock(topPlantTexture)));
    }

    public static ObjectArrayList<BlockModelDefinition> tintedDoublePlant(ResourceLocation baseTallPlantTexture) {
        return tintedDoublePlant(baseTallPlantTexture.withSuffix("_top"), baseTallPlantTexture.withSuffix("_bottom"));
    }

    public static BlockStateDefinition doublePlant(Supplier<Block> targetBlock) {
        return BlockStateDefinition.of(targetBlock)
                .withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get())
                        .with(PropertyDispatch.property(BlockStateProperties.DOUBLE_BLOCK_HALF)
                                .select(DoubleBlockHalf.LOWER, Variant.variant()
                                        .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(targetBlock.get()).withSuffix("_bottom")))
                                .select(DoubleBlockHalf.UPPER, Variant.variant()
                                        .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(targetBlock.get()).withSuffix("_top")))));
    }

    public static BlockModelDefinition farmlandDry(ResourceLocation farmlandTexture, ResourceLocation dirtTexture) {
        return BlockModelDefinition.of(ModelTemplates.FARMLAND)
                .withTextureMapping(CATextureSlots.farmland(RegistryUtil.pickBlockPrefix(farmlandTexture), RegistryUtil.pickBlockPrefix(dirtTexture)));
    }

    public static BlockModelDefinition farmlandMoist(ResourceLocation moistFarmlandTexture, ResourceLocation dirtTexture) {
        return BlockModelDefinition.of(ModelTemplates.FARMLAND)
                .withTextureMapping(CATextureSlots.farmland(RegistryUtil.pickBlockPrefix(moistFarmlandTexture), RegistryUtil.pickBlockPrefix(dirtTexture)))
                .withCustomModelName(moistFarmlandTexture.getPath().substring(moistFarmlandTexture.getPath().lastIndexOf("/") + 1));
    }

    public static ObjectArrayList<BlockModelDefinition> farmland(ResourceLocation farmlandTexture, ResourceLocation moistFarmlandTexture, ResourceLocation dirtTexture) {
        return ObjectArrayList.of(farmlandDry(farmlandTexture, dirtTexture), farmlandMoist(moistFarmlandTexture, dirtTexture));
    }

    public static ObjectArrayList<BlockModelDefinition> farmland(ResourceLocation farmlandTexture) {
        return farmland(farmlandTexture, farmlandTexture.withSuffix("_moist"), farmlandTexture.withPath(farmlandTexture.getPath().replace("_farmland", "_dirt")));
    }

    public static ObjectArrayList<BlockModelDefinition> defaultedFarmland(ResourceLocation farmlandTexture) {
        return farmland(farmlandTexture, farmlandTexture.withSuffix("_moist"), farmlandTexture.withPath(farmlandTexture.getPath().replace("_farmland", "")));
    }

    public static BlockStateDefinition farmland(Supplier<Block> targetBlock) {
        ResourceLocation dryFarmlandModelLoc = ModelLocationUtils.getModelLocation(targetBlock.get());
        ResourceLocation moistureFarmlandModelLoc = ModelLocationUtils.getModelLocation(targetBlock.get()).withSuffix("_moist");
        return BlockStateDefinition.of(targetBlock)
                .withBlockStateSupplier(MultiVariantGenerator.multiVariant(targetBlock.get())
                        .with(PropertyDispatch.property(BlockStateProperties.MOISTURE)
                                .generate(moistureLevel -> moistureLevel.compareTo(7) >= 0 ? Variant.variant().with(VariantProperties.MODEL, moistureFarmlandModelLoc) : Variant.variant().with(VariantProperties.MODEL, dryFarmlandModelLoc))));
    }
}