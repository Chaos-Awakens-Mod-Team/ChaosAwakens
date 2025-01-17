package io.github.chaosawakens.util;

import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.util.CubicSpline;
import net.minecraft.util.ToFloatFunction;

/**
 * Utility class containing part-general part-arbitrary helper/shortcut methods for world/terrain generation.
 */
public final class WorldGenUtil {
    public static final ToFloatFunction<Float> NO_TRANSFORM = ToFloatFunction.IDENTITY;

    private WorldGenUtil() {
        throw new IllegalAccessError("Attempted to construct Utility Class!");
    }

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> miningParadiseOffset(I continentSplineCoord, I erosionSplineCoord, I foldedRidgesSplineCoord) {
        ToFloatFunction<Float> noTransform = NO_TRANSFORM;

        CubicSpline<C, I> erosionOffsetSplineToDeepWater = TerrainProvider.buildErosionOffsetSpline(erosionSplineCoord, foldedRidgesSplineCoord, -0.112F, 0.0F, 0.01F, 0.0F, 0.0F, -0.04763F, false, false, noTransform);
        CubicSpline<C, I> erosionOffsetSplineToShallowWater = TerrainProvider.buildErosionOffsetSpline(erosionSplineCoord, foldedRidgesSplineCoord, -0.223F, 0.03F, 0.1F, 0.124F, 0.01F, -0.0334F, false, false, noTransform);
        CubicSpline<C, I> erosionOffsetSplineToLand = TerrainProvider.buildErosionOffsetSpline(erosionSplineCoord, foldedRidgesSplineCoord, -0.532F, 0.03F, 0.1F, 0.7F, 0.019F, -0.0334F, true, true, noTransform);
        CubicSpline<C, I> erosionOffsetSplineToExtremeLand = TerrainProvider.buildErosionOffsetSpline(erosionSplineCoord, foldedRidgesSplineCoord, -1.008F, 0.056F, 0.1F, 1.0F, 0.02F, 0.011F, true, true, noTransform);

        return CubicSpline.builder(continentSplineCoord, noTransform)
                .addPoint(-1.1F, 0.0084F)
                .addPoint(-1.02F, -0.04222F)
                .addPoint(-0.51F, -0.4222F)
                .addPoint(-0.44F, -0.24F)
                .addPoint(-0.18F, -0.024F)
                .addPoint(-0.16F, erosionOffsetSplineToDeepWater)
                .addPoint(-0.15F, erosionOffsetSplineToDeepWater)
                .addPoint(-0.1F, erosionOffsetSplineToShallowWater)
                .addPoint(0.25F, erosionOffsetSplineToLand)
                .addPoint(1.0F, erosionOffsetSplineToExtremeLand)
                .build();
    }

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> miningParadiseFactor(I continentSplineCoord, I erosionSplineCoord, I ridgesSplineCoord, I foldedRidgesSplineCoord) {
        ToFloatFunction<Float> noTransform =  NO_TRANSFORM;

        CubicSpline<C, I> erosionFactorSpline = TerrainProvider.getErosionFactor(erosionSplineCoord, ridgesSplineCoord, foldedRidgesSplineCoord, 3.65F, true, noTransform);
        CubicSpline<C, I> amplifiedErosionFactorSpline = TerrainProvider.getErosionFactor(erosionSplineCoord, ridgesSplineCoord, foldedRidgesSplineCoord, 2.97F, true, noTransform);
        CubicSpline<C, I> erosionFactorSplineContinued = TerrainProvider.getErosionFactor(erosionSplineCoord, ridgesSplineCoord, foldedRidgesSplineCoord, 1.68F, true, noTransform);
        CubicSpline<C, I> erosionFactorSplineFinal = TerrainProvider.getErosionFactor(erosionSplineCoord, ridgesSplineCoord, foldedRidgesSplineCoord, 1.19F, false, noTransform);

        return CubicSpline.builder(continentSplineCoord, NO_TRANSFORM)
                .addPoint(-0.19F, 1.95F)
                .addPoint(-0.15F, erosionFactorSpline)
                .addPoint(-0.1F, amplifiedErosionFactorSpline)
                .addPoint(0.03F, erosionFactorSplineContinued)
                .addPoint(0.06F, erosionFactorSplineFinal)
                .build();
    }

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> miningParadiseJaggedness(I continentCoord, I erosionCoord, I ridgesCoord, I foldedRidgesCoord) {
        ToFloatFunction<Float> noTransform = NO_TRANSFORM;
        float jaggednessThreshold = 0.71F;

        return CubicSpline.builder(continentCoord, noTransform)
                .addPoint(-0.11F, 0.0F)
                .addPoint(0.03F, TerrainProvider.buildErosionJaggednessSpline(erosionCoord, ridgesCoord, foldedRidgesCoord, 1.0F, 0.5F, 0.0F, 0.0F, noTransform))
                .addPoint(jaggednessThreshold, TerrainProvider.buildErosionJaggednessSpline(erosionCoord, ridgesCoord, foldedRidgesCoord, 1.0F, 1.0F, 1.0F, 0.0F, noTransform))
                .build();
    }
}
