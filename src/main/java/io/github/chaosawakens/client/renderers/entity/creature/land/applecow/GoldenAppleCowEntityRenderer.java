package io.github.chaosawakens.client.renderers.entity.creature.land.applecow;

import io.github.chaosawakens.client.models.entity.creature.land.applecow.GoldenAppleCowEntityModel;
import io.github.chaosawakens.client.renderers.entity.base.ExtendedGeoEntityRenderer;
import io.github.chaosawakens.common.entity.creature.land.applecow.GoldenAppleCowEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;

public class GoldenAppleCowEntityRenderer extends ExtendedGeoEntityRenderer<GoldenAppleCowEntity> {

	public GoldenAppleCowEntityRenderer(EntityRendererManager renderManager) {
		super(renderManager, new GoldenAppleCowEntityModel());
	}

	@Override
	protected boolean shouldRotateOnDeath() {
		return true;
	}

	@Override
	protected float getShadowRadius() {
		return 0.7F;
	}

	@Override
	protected ObjectArrayList<GeoLayerRenderer<GoldenAppleCowEntity>> getLayers() {
		return null;
	}

}
