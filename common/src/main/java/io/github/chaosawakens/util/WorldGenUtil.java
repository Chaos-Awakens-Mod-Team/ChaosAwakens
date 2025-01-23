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

        CubicSpline<C, I> erosionOffsetSplineFromDeepWater = TerrainProvider.buildErosionOffsetSpline(erosionSplineCoord, foldedRidgesSplineCoord, -0.312F, 0.0F, 0.01F, -0.32F, 0.083F, -0.2763F, true, false, noTransform);
        CubicSpline<C, I> erosionOffsetSplineFromShallowWater = TerrainProvider.buildErosionOffsetSpline(erosionSplineCoord, foldedRidgesSplineCoord, -0.223F, 0.03F, 0.1F, -0.124F, 0.1F, -0.0334F, false, false, noTransform);
        CubicSpline<C, I> erosionOffsetSplineFromLand = TerrainProvider.buildErosionOffsetSpline(erosionSplineCoord, foldedRidgesSplineCoord, -0.132F, 0.03F, 0.1F, 0.95F, 0.19F, 0.334F, true, true, noTransform);
        CubicSpline<C, I> erosionOffsetSplineFromExtremeLand = TerrainProvider.buildErosionOffsetSpline(erosionSplineCoord, foldedRidgesSplineCoord, -0.18F, 0.26F, 1.4F, 1.5F, 0.3F, 0.011F, true, true, noTransform);

        return CubicSpline.builder(continentSplineCoord, noTransform) // Depth
                .addPoint(-1.009F, 0.217F)
                .addPoint(-0.888F, -0.254F)
                .addPoint(-0.338F, -0.256F, 0.01F)
                .addPoint(-0.262F, -0.015F, -0.213F)
                .addPoint(-0.054F, -0.005F, -0.519F)
                .addPoint(0.0F, erosionOffsetSplineFromDeepWater)
                .addPoint(0.16F, erosionOffsetSplineFromShallowWater)
                .addPoint(0.24F, erosionOffsetSplineFromLand)
                .addPoint(0.36F, erosionOffsetSplineFromExtremeLand)
                .addPoint(0.38F, erosionOffsetSplineFromLand)
                .addPoint(0.4F, erosionOffsetSplineFromExtremeLand)
                .addPoint(0.78F, erosionOffsetSplineFromShallowWater)
                .addPoint(1.0F, erosionOffsetSplineFromDeepWater)
                .build();
    }

    public static <C, I extends ToFloatFunction<C>> CubicSpline<C, I> miningParadiseFactor(I continentSplineCoord, I erosionSplineCoord, I ridgesSplineCoord, I foldedRidgesSplineCoord) {
        ToFloatFunction<Float> noTransform =  NO_TRANSFORM;

        CubicSpline<C, I> erosionFactorSpline = TerrainProvider.getErosionFactor(continentSplineCoord, ridgesSplineCoord, foldedRidgesSplineCoord, 8.65F, true, noTransform);
        CubicSpline<C, I> amplifiedErosionFactorSpline = TerrainProvider.getErosionFactor(continentSplineCoord, ridgesSplineCoord, foldedRidgesSplineCoord, 5.97F, true, noTransform);
        CubicSpline<C, I> erosionFactorSplineContinued = TerrainProvider.getErosionFactor(continentSplineCoord, ridgesSplineCoord, foldedRidgesSplineCoord, 5.68F, true, noTransform);
        CubicSpline<C, I> erosionFactorSplineFinal = TerrainProvider.getErosionFactor(continentSplineCoord, ridgesSplineCoord, foldedRidgesSplineCoord, 1.19F, false, noTransform);

        return CubicSpline.builder(erosionSplineCoord, NO_TRANSFORM)
                .addPoint(-0.19F, 4.95F)
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
