package io.github.chaosawakens.common.registry;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class CASurfaceRules {

    public static class CAConditionSources {
        // General
        public static final SurfaceRules.ConditionSource AT_ABOVE_WATER_LEVEL = SurfaceRules.waterBlockCheck(0, 0);
    }

    public static class CAStateRules {
        // General
        public static final SurfaceRules.RuleSource BEDROCK = SurfaceRules.state(Blocks.BEDROCK.defaultBlockState());

        // Mining Paradise
        public static final SurfaceRules.RuleSource DENSE_GRASS_BLOCK = SurfaceRules.state(CABlocks.DENSE_GRASS_BLOCK.get().defaultBlockState());
        public static final SurfaceRules.RuleSource DENSE_DIRT = SurfaceRules.state(CABlocks.DENSE_DIRT.get().defaultBlockState());
    }
}
