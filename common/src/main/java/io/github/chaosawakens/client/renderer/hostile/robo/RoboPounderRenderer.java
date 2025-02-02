package io.github.chaosawakens.client.renderer.hostile.robo;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.client.model.hostile.robo.RoboPounderModel;
import io.github.chaosawakens.common.entity.hostile.robo.RoboPounder;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class RoboPounderRenderer<RP extends RoboPounder> extends MobRenderer<RP, RoboPounderModel<RP>> {
    public static final ResourceLocation BASE_TEXTURE = CAConstants.prefix("textures/entity/hostile/robo/robo_pounder/robo_pounder.png");

    public RoboPounderRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new RoboPounderModel<>(ctx.bakeLayer(RoboPounderModel.BASE_LAYER)), 1.2F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(RP rp) {
        return BASE_TEXTURE;
    }
}
