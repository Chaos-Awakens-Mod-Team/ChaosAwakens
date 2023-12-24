package io.github.chaosawakens.common.worldgen.trunkplacer;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.chaosawakens.common.registry.CATrunkPlacerTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer.Foliage;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class DirtlessGiantTrunkPlacer extends AbstractTrunkPlacer {
	public static final Codec<DirtlessGiantTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) -> {
		return trunkPlacerParts(instance)
				.and(BlockState.CODEC.fieldOf("base").forGetter((placer) -> placer.base))
				.apply(instance, DirtlessGiantTrunkPlacer::new);
	});
	protected final BlockState base;
	
	public DirtlessGiantTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, BlockState base) {
		super(baseHeight, heightRandA, heightRandB);
		this.base = base;
	}
	
	@Override
	protected TrunkPlacerType<?> type() {
		return CATrunkPlacerTypes.DIRTLESS_GIANT_TRUNK_PLACER;
	}
	
	@Override
	public List<FoliagePlacer.Foliage> placeTrunk(IWorldGenerationReader reader, Random rand, int height, BlockPos pos, Set<BlockPos> set, MutableBoundingBox bb, BaseTreeFeatureConfig config) {
		BlockPos belowPos = pos.below();
		
		baseAt(reader, belowPos);
		baseAt(reader, belowPos.east());
		baseAt(reader, belowPos.south());
		baseAt(reader, belowPos.south().east());
		
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		
		for (int i = 0; i < height; ++i) {
			placeLogIfFreeWithOffset(reader, rand, mutablePos, set, bb, config, pos, 0, i, 0);
			placeLogIfFreeWithOffset(reader, rand, mutablePos, set, bb, config, pos, 1, i, 0);
			placeLogIfFreeWithOffset(reader, rand, mutablePos, set, bb, config, pos, 1, i, 1);
			placeLogIfFreeWithOffset(reader, rand, mutablePos, set, bb, config, pos, 0, i, 1);
		}
		
		return ImmutableList.of(new Foliage(pos.above(height), 0, true));
	}
	
	protected static void placeLogIfFreeWithOffset(IWorldGenerationReader reader, Random rand, BlockPos.Mutable mutable, Set<BlockPos> set, MutableBoundingBox bb, BaseTreeFeatureConfig config, BlockPos pos, int xOffset, int yOffset, int zOffset) {
		mutable.setWithOffset(pos, xOffset, yOffset, zOffset);
		placeLogIfFree(reader, rand, mutable, set, bb, config);
	}
	
	protected void baseAt(IWorldGenerationReader reader, BlockPos pos) {
		TreeFeature.setBlockKnownShape(reader, pos, this.base);
	}
}
