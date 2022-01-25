package io.github.chaosawakens.data;

import io.github.chaosawakens.ChaosAwakens;
import io.github.chaosawakens.common.registry.CABlocks;
import io.github.chaosawakens.common.registry.CATags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;

/**
 * Yea I do understand that Jsons are easy to make, however it's about time we
 * focus on the real stuff, this provider makes it easier for us to focus on the
 * heavy code
 *
 * @author Meme Man
 */

public class CATagProvider extends BlockTagsProvider {
	
	public CATagProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
		super(gen, ChaosAwakens.MODID, existingFileHelper);
	}
	
	protected Path getPath(ResourceLocation resourceLocation) {
		return this.generator.getOutputFolder().resolve("data/" + resourceLocation.getNamespace() + "/tags/items/" + resourceLocation.getPath() + ".json");
	}
	
	public String getName() {
		return "Chaos Awakens Item-Block Tags";
	}
	
	@Override
	protected void addTags() {
		this.tag(CATags.APPLE_LOGS).add(CABlocks.APPLE_LOG.get(), CABlocks.STRIPPED_APPLE_LOG.get());
		this.tag(CATags.CHERRY_LOGS).add(CABlocks.CHERRY_LOG.get(), CABlocks.STRIPPED_CHERRY_LOG.get());
		this.tag(CATags.PEACH_LOGS).add(CABlocks.PEACH_LOG.get(), CABlocks.STRIPPED_PEACH_LOG.get());
		this.tag(CATags.DUPLICATION_LOGS).add(CABlocks.DUPLICATION_LOG.get(), CABlocks.STRIPPED_DUPLICATION_LOG.get(), CABlocks.DEAD_DUPLICATION_LOG.get());
		this.tag(BlockTags.LOGS).addTags(CATags.DUPLICATION_LOGS);
		this.tag(BlockTags.LOGS_THAT_BURN).addTags(CATags.APPLE_LOGS, CATags.CHERRY_LOGS, CATags.PEACH_LOGS);
		this.tag(BlockTags.PLANKS).add(CABlocks.APPLE_PLANKS.get(), CABlocks.CHERRY_PLANKS.get(), CABlocks.PEACH_PLANKS.get(), CABlocks.DUPLICATION_PLANKS.get(), CABlocks.SKYWOOD_PLANKS.get(), CABlocks.MOLDY_PLANKS.get());
		this.tag(BlockTags.FENCE_GATES).add(CABlocks.APPLE_FENCE_GATE.get(), CABlocks.CHERRY_FENCE_GATE.get(), CABlocks.PEACH_FENCE_GATE.get(), CABlocks.DUPLICATION_FENCE_GATE.get(), CABlocks.SKYWOOD_FENCE_GATE.get());
		this.tag(BlockTags.WOODEN_FENCES).add(CABlocks.APPLE_FENCE.get(), CABlocks.CHERRY_FENCE.get(), CABlocks.PEACH_FENCE.get(), CABlocks.DUPLICATION_FENCE.get(), CABlocks.SKYWOOD_FENCE.get(), CABlocks.MOLDY_FENCE.get());
	}

	// TODO make the static class and the main class function as one instead of having to make them separate
	public static class CATagProviderForBlocks extends BlockTagsProvider {

		/**
		 * Static class for the main tag provider to make a separate package for the
		 * block tags to work properly in crafting recipes and such
		 *
		 * @param gen
		 * @param existingFileHelper
		 * @author Meme Man
		 */

		public CATagProviderForBlocks(DataGenerator gen, ExistingFileHelper existingFileHelper) {
			super(gen, ChaosAwakens.MODID, existingFileHelper);
		}

		protected Path getPath(ResourceLocation resourceLocation) {
			return this.generator.getOutputFolder().resolve("data/" + resourceLocation.getNamespace() + "/tags/blocks/" + resourceLocation.getPath() + ".json");
		}

		public String getName() {
			return "Chaos Awakens Block Tags";
		}

		@Override
		protected void addTags() {
			this.tag(CATags.APPLE_LOGS).add(CABlocks.APPLE_LOG.get(), CABlocks.STRIPPED_APPLE_LOG.get());
			this.tag(CATags.CHERRY_LOGS).add(CABlocks.CHERRY_LOG.get(), CABlocks.STRIPPED_CHERRY_LOG.get());
			this.tag(CATags.PEACH_LOGS).add(CABlocks.PEACH_LOG.get(), CABlocks.STRIPPED_PEACH_LOG.get());
			this.tag(CATags.DUPLICATION_LOGS).add(CABlocks.DUPLICATION_LOG.get(), CABlocks.STRIPPED_DUPLICATION_LOG.get(), CABlocks.DEAD_DUPLICATION_LOG.get());
			this.tag(BlockTags.LOGS).addTags(CATags.DUPLICATION_LOGS);
			this.tag(BlockTags.LOGS_THAT_BURN).addTags(CATags.APPLE_LOGS, CATags.CHERRY_LOGS, CATags.PEACH_LOGS);
			this.tag(BlockTags.PLANKS).add(CABlocks.APPLE_PLANKS.get(), CABlocks.CHERRY_PLANKS.get(), CABlocks.PEACH_PLANKS.get(), CABlocks.DUPLICATION_PLANKS.get(), CABlocks.SKYWOOD_PLANKS.get(), CABlocks.MOLDY_PLANKS.get());
			this.tag(BlockTags.FENCE_GATES).add(CABlocks.APPLE_FENCE_GATE.get(), CABlocks.CHERRY_FENCE_GATE.get(), CABlocks.PEACH_FENCE_GATE.get(), CABlocks.DUPLICATION_FENCE_GATE.get(), CABlocks.SKYWOOD_FENCE_GATE.get());
			this.tag(BlockTags.WOODEN_FENCES).add(CABlocks.APPLE_FENCE.get(), CABlocks.CHERRY_FENCE.get(), CABlocks.PEACH_FENCE.get(), CABlocks.DUPLICATION_FENCE.get(), CABlocks.SKYWOOD_FENCE.get(), CABlocks.MOLDY_FENCE.get());
		}
	}

	// TODO make the static class and the main class function as one instead of having to make them separate
	public static class CAItemTagProvider extends ItemTagsProvider {

		/**
		 * Static class for the main tag provider to make a separate package for the
		 * block tags to work properly in crafting recipes and such
		 *
		 * @param gen
		 * @param existingFileHelper
		 * @author Meme Man
		 */

		public CAItemTagProvider(DataGenerator gen, BlockTagsProvider blockTagsProvider, @javax.annotation.Nullable net.minecraftforge.common.data.ExistingFileHelper existingFileHelper) {
			super(gen, new CATagProviderForBlocks(gen, existingFileHelper), ChaosAwakens.MODID, existingFileHelper);
		}

		protected Path getPath(ResourceLocation resourceLocation) {
			return this.generator.getOutputFolder().resolve("data/" + resourceLocation.getNamespace() + "/tags/items/" + resourceLocation.getPath() + ".json");
		}

		public String getName() {
			return "Chaos Awakens Item Tags";
		}

		@Override
		protected void addTags() {
//			this.tag(CATags.BUCKETS).add(Items.WATER_BUCKET, Items.MILK_BUCKET, Items.LAVA_BUCKET);
		}
	}
}
