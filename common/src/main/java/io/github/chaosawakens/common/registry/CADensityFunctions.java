package io.github.chaosawakens.common.registry;

import com.google.common.collect.ImmutableList;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.util.WorldGenUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.Noises;

import java.util.function.Function;
import java.util.function.Supplier;

@RegistrarEntry
public class CADensityFunctions { // Needed so that Vanilla datapacks don't directly affect CA dimension terrain gen (plus some other functions or optimizations, probably)
    private static final ObjectArrayList<Supplier<ResourceKey<DensityFunction>>> DENSITY_FUNCTIONS = new ObjectArrayList<>();

    // Vanilla
    public static final Supplier<ResourceKey<DensityFunction>> ZERO = registerDensityFunction("vanilla/zero", b -> DensityFunctions::zero);
    public static final Supplier<ResourceKey<DensityFunction>> Y = registerDensityFunction("vanilla/y", b -> () -> DensityFunctions.yClampedGradient(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2, DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2));
    public static final Supplier<ResourceKey<DensityFunction>> SHIFT_X = registerDensityFunction("vanilla/shift_x", b -> () -> DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftA(b.lookup(Registries.NOISE).getOrThrow(Noises.SHIFT)))));
    public static final Supplier<ResourceKey<DensityFunction>> SHIFT_Z = registerDensityFunction("vanilla/shift_z", b -> () -> DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.shiftB(b.lookup(Registries.NOISE).getOrThrow(Noises.SHIFT)))));

    public static final Supplier<ResourceKey<DensityFunction>> CONTINENTS = registerDensityFunction("vanilla/land/continents", b -> () -> DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(getWrappedDensityFunctionHolder(b, SHIFT_X), getWrappedDensityFunctionHolder(b, SHIFT_Z), 0.25D, b.lookup(Registries.NOISE).getOrThrow(Noises.CONTINENTALNESS))));
    public static final Supplier<ResourceKey<DensityFunction>> EROSION = registerDensityFunction("vanilla/land/erosion", b -> () -> DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(getWrappedDensityFunctionHolder(b, SHIFT_X), getWrappedDensityFunctionHolder(b, SHIFT_Z), 0.25D, b.lookup(Registries.NOISE).getOrThrow(Noises.EROSION))));
    public static final Supplier<ResourceKey<DensityFunction>> RIDGES = registerDensityFunction("vanilla/land/ridges", b -> () -> DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(getWrappedDensityFunctionHolder(b, SHIFT_X), getWrappedDensityFunctionHolder(b, SHIFT_Z), 0.25D, b.lookup(Registries.NOISE).getOrThrow(Noises.RIDGE))));
    public static final Supplier<ResourceKey<DensityFunction>> RIDGES_FOLDED = registerDensityFunction("vanilla/land/ridges_folded", b -> () -> DensityFunctions.mul(DensityFunctions.add(DensityFunctions.add(getWrappedDensityFunctionHolder(b, RIDGES).abs(), DensityFunctions.constant(-2.0D / 3.0D)).abs(), DensityFunctions.constant(-1.0D / 3.0D)), DensityFunctions.constant(-3.0D)));

    // Mining Paradise
    public static final Supplier<ResourceKey<DensityFunction>> MINING_PARADISE_CONTINENTS = registerDensityFunction("mining_paradise/land/continents", b -> () -> DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(getWrappedDensityFunctionHolder(b, SHIFT_X), getWrappedDensityFunctionHolder(b, SHIFT_Z), 0.375D, b.lookup(Registries.NOISE).getOrThrow(CANoiseParameters.MINING_PARADISE_CONTINENTALNESS.get()))));
    public static final Supplier<ResourceKey<DensityFunction>> MINING_PARADISE_EROSION = registerDensityFunction("mining_paradise/land/erosion", b -> () -> DensityFunctions.flatCache(DensityFunctions.shiftedNoise2d(getWrappedDensityFunctionHolder(b, SHIFT_X), getWrappedDensityFunctionHolder(b, SHIFT_Z), 0.375D, b.lookup(Registries.NOISE).getOrThrow(CANoiseParameters.MINING_PARADISE_EROSION.get()))));

    public static final Supplier<ResourceKey<DensityFunction>> MINING_PARADISE_OFFSET = registerDensityFunction("mining_paradise/land/offset", b -> () -> DensityFunctions.flatCache(DensityFunctions.cache2d(DensityFunctions.lerp(DensityFunctions.blendAlpha(), DensityFunctions.add(DensityFunctions.constant(-1.8141592653D), DensityFunctions.spline(WorldGenUtil.miningParadiseOffset(new DensityFunctions.Spline.Coordinate(getWrappedDensityFunction(b, CONTINENTS)), new DensityFunctions.Spline.Coordinate(getWrappedDensityFunction(b, EROSION)), new DensityFunctions.Spline.Coordinate(getWrappedDensityFunction(b, RIDGES_FOLDED))))), DensityFunctions.blendOffset()))));
    public static final Supplier<ResourceKey<DensityFunction>> MINING_PARADISE_JAGGEDNESS = registerDensityFunction("mining_paradise/land/jaggedness", b -> () -> NoiseRouterData.splineWithBlending(DensityFunctions.spline(WorldGenUtil.miningParadiseJaggedness(new DensityFunctions.Spline.Coordinate(getWrappedDensityFunction(b, CONTINENTS)), new DensityFunctions.Spline.Coordinate(getWrappedDensityFunction(b, EROSION)), new DensityFunctions.Spline.Coordinate(getWrappedDensityFunction(b, RIDGES)), new DensityFunctions.Spline.Coordinate(getWrappedDensityFunction(b, RIDGES_FOLDED)))), DensityFunctions.zero()));
    public static final Supplier<ResourceKey<DensityFunction>> MINING_PARADISE_FACTOR = registerDensityFunction("mining_paradise/land/factor", b -> () -> NoiseRouterData.splineWithBlending(DensityFunctions.spline(WorldGenUtil.miningParadiseFactor(new DensityFunctions.Spline.Coordinate(getWrappedDensityFunction(b, CONTINENTS)), new DensityFunctions.Spline.Coordinate(getWrappedDensityFunction(b, EROSION)), new DensityFunctions.Spline.Coordinate(getWrappedDensityFunction(b, RIDGES)), new DensityFunctions.Spline.Coordinate(getWrappedDensityFunction(b, RIDGES_FOLDED)))), DensityFunctions.constant(10.0D)));
    public static final Supplier<ResourceKey<DensityFunction>> MINING_PARADISE_DEPTH = registerDensityFunction("mining_paradise/land/depth", b -> () -> DensityFunctions.add(DensityFunctions.yClampedGradient(-256, 480, 1.745D, -1.5D), getWrappedDensityFunctionHolder(b, MINING_PARADISE_OFFSET)));


    private static Supplier<ResourceKey<DensityFunction>> registerDensityFunction(ResourceLocation id, Function<BootstapContext<DensityFunction>, Supplier<DensityFunction>> dfFunc) {
        Supplier<ResourceKey<DensityFunction>> densityFunctionSup = CAServices.REGISTRAR.registerDatapackObject(id, dfFunc, Registries.DENSITY_FUNCTION);
        DENSITY_FUNCTIONS.add(densityFunctionSup);
        return densityFunctionSup;
    }

    private static Supplier<ResourceKey<DensityFunction>> registerDensityFunction(String id, Function<BootstapContext<DensityFunction>, Supplier<DensityFunction>> dfFunc) {
        return registerDensityFunction(CAConstants.prefix(id), dfFunc);
    }

    public static DensityFunctions.HolderHolder getWrappedDensityFunctionHolder(BootstapContext<?> regCtx, Supplier<ResourceKey<DensityFunction>> targetDensityFunction) {
        return new DensityFunctions.HolderHolder(regCtx.lookup(Registries.DENSITY_FUNCTION).getOrThrow(targetDensityFunction.get()));
    }

    public static Holder<DensityFunction> getWrappedDensityFunction(BootstapContext<?> regCtx, Supplier<ResourceKey<DensityFunction>> targetDensityFunction) {
        return getWrappedDensityFunctionHolder(regCtx, targetDensityFunction).function();
    }

    public static ImmutableList<Supplier<ResourceKey<DensityFunction>>> getDensityFunctions() {
        return ImmutableList.copyOf(DENSITY_FUNCTIONS);
    }
}
