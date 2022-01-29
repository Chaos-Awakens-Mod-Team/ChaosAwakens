package io.github.chaosawakens.common.events;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.serialization.Codec;
import io.github.chaosawakens.ChaosAwakens;
import io.github.chaosawakens.api.CAReflectionHelper;
import io.github.chaosawakens.api.FeatureWrapper;
import io.github.chaosawakens.common.config.CAConfig;
import io.github.chaosawakens.common.integration.CAEMCValues;
import io.github.chaosawakens.common.integration.CAJER;
import io.github.chaosawakens.common.items.ExtendedHitWeaponItem;
import io.github.chaosawakens.common.network.PacketHandler;
import io.github.chaosawakens.common.registry.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper.UnableToFindMethodException;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author invalid2
 */
public class CommonSetupEvent {

    /**
     * List for configured features, so they get registered at the correct time
     */
    public static List<FeatureWrapper> configFeatures = new ArrayList<>();

    private static Method codecMethod;

    public static void onFMLCommonSetupEvent(final FMLCommonSetupEvent event) {
        CAEntityTypes.registerSpawnPlacementTypes();
        PacketHandler.init();
        Raid.WaveMember.create("illusioner", EntityType.ILLUSIONER, new int[]{0, 0, 0, 0, 1, 1, 0, 2});
        
        event.enqueueWork(() -> {
            CAStructures.setupStructures();
            CAConfiguredStructures.registerConfiguredStructures();
            CASurfaceBuilders.Configured.registerConfiguredSurfaceBuilders();
            CAVillagers.registerVillagerTypes();
            CAStrippedLogBlocks.registerStrippedLogs();

            CAReflectionHelper.classLoad("io.github.chaosawakens.common.registry.CAConfiguredFeatures");
            configFeatures.forEach((wrapper) -> Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, wrapper.getIdentifier(), wrapper.getFeatureType()));
        });
        
		ModList modList = ModList.get();
		if (modList.isLoaded("projecte")) CAEMCValues.init();
		if (modList.isLoaded("jeresources")) CAJER.init();

        // TODO Make it so we don't have to add stuff here manually
        BiomeDictionary.addTypes(RegistryKey.create(Registry.BIOME_REGISTRY, CABiomes.MINING_BIOME.getId()), CABiomes.Type.MINING_DIMENSION);
        BiomeDictionary.addTypes(RegistryKey.create(Registry.BIOME_REGISTRY, CABiomes.STALAGMITE_VALLEY.getId()), CABiomes.Type.MINING_DIMENSION);
        BiomeDictionary.addTypes(RegistryKey.create(Registry.BIOME_REGISTRY, CABiomes.VILLAGE_PLAINS.getId()), CABiomes.Type.VILLAGE_DIMENSION);
        BiomeDictionary.addTypes(RegistryKey.create(Registry.BIOME_REGISTRY, CABiomes.VILLAGE_SAVANNA.getId()), CABiomes.Type.VILLAGE_DIMENSION);
        BiomeDictionary.addTypes(RegistryKey.create(Registry.BIOME_REGISTRY, CABiomes.VILLAGE_TAIGA.getId()), CABiomes.Type.VILLAGE_DIMENSION);
        BiomeDictionary.addTypes(RegistryKey.create(Registry.BIOME_REGISTRY, CABiomes.VILLAGE_SNOWY.getId()), CABiomes.Type.VILLAGE_DIMENSION);
        BiomeDictionary.addTypes(RegistryKey.create(Registry.BIOME_REGISTRY, CABiomes.VILLAGE_DESERT.getId()), CABiomes.Type.VILLAGE_DIMENSION);
        BiomeDictionary.addTypes(RegistryKey.create(Registry.BIOME_REGISTRY, CABiomes.DANGER_ISLANDS.getId()), CABiomes.Type.DANGER_DIMENSION);
        BiomeDictionary.addTypes(RegistryKey.create(Registry.BIOME_REGISTRY, CABiomes.CRYSTAL_PLAINS.getId()), CABiomes.Type.CRYSTAL_DIMENSION);
    }
    
    public static void registerReachModifiers(final PlayerEvent e) {
    	double reachDistance = 0.0D;
    	ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
    	builder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(ExtendedHitWeaponItem.REACH_MODIFIER, "Weapon modifier", reachDistance, AttributeModifier.Operation.ADDITION));
    	ItemStack bigBertha = new ItemStack(CAItems.BIG_BERTHA.get());
    	ItemStack attitudeAdjuster = new ItemStack(CAItems.ATTITUDE_ADJUSTER.get());
    	ItemStack prismaticReaper = new ItemStack(CAItems.PRISMATIC_REAPER.get());
    	PlayerEntity p = (PlayerEntity) e.getPlayer();
    	
    	if(p.getItemInHand(Hand.MAIN_HAND).equals(bigBertha)) {
    		reachDistance = 25.0D;
    	}
    	
    	if(p.getItemInHand(Hand.MAIN_HAND).equals(attitudeAdjuster)) {
    		reachDistance = 15.0D;
    	}
    	
    	if(p.getItemInHand(Hand.MAIN_HAND).equals(prismaticReaper)) {
    		reachDistance = 13.0D;
    	}
    }

    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (!(event.getWorld() instanceof ServerWorld)) return;

        ServerWorld serverWorld = (ServerWorld) event.getWorld();
        ServerChunkProvider chunkProvider = serverWorld.getChunkSource();

        try {
            if (codecMethod == null)
                codecMethod = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
            // TODO Fix this
            ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) codecMethod.invoke(chunkProvider.generator));
            if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            ChaosAwakens.warn("WORLDGEN", e);
            e.printStackTrace();
        } catch (UnableToFindMethodException e) {
            if (CAConfig.COMMON.terraforgedCheckMsg.get())
                ChaosAwakens.info("WORLDGEN", "Unable to check if " + serverWorld.dimension().location()
                        + " is using Terraforged's ChunkGenerator due to Terraforged not being present or not accessible,"
                        + " if you aren't using Terraforged please ignore this message");
        }

        if (serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator && serverWorld.dimension().equals(World.OVERWORLD))
            return;

        Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(chunkProvider.generator.getSettings().structureConfig());

        tempMap.putIfAbsent(CAStructures.ACACIA_ENT_TREE.get(), DimensionStructuresSettings.DEFAULTS.get(CAStructures.ACACIA_ENT_TREE.get()));
        tempMap.putIfAbsent(CAStructures.BIRCH_ENT_TREE.get(), DimensionStructuresSettings.DEFAULTS.get(CAStructures.BIRCH_ENT_TREE.get()));
        tempMap.putIfAbsent(CAStructures.CRIMSON_ENT_TREE.get(), DimensionStructuresSettings.DEFAULTS.get(CAStructures.CRIMSON_ENT_TREE.get()));
        tempMap.putIfAbsent(CAStructures.DARK_OAK_ENT_TREE.get(), DimensionStructuresSettings.DEFAULTS.get(CAStructures.DARK_OAK_ENT_TREE.get()));
        tempMap.putIfAbsent(CAStructures.JUNGLE_ENT_TREE.get(), DimensionStructuresSettings.DEFAULTS.get(CAStructures.JUNGLE_ENT_TREE.get()));
        tempMap.putIfAbsent(CAStructures.OAK_ENT_TREE.get(), DimensionStructuresSettings.DEFAULTS.get(CAStructures.OAK_ENT_TREE.get()));
        tempMap.putIfAbsent(CAStructures.SPRUCE_ENT_TREE.get(), DimensionStructuresSettings.DEFAULTS.get(CAStructures.SPRUCE_ENT_TREE.get()));
        tempMap.putIfAbsent(CAStructures.WARPED_ENT_TREE.get(), DimensionStructuresSettings.DEFAULTS.get(CAStructures.WARPED_ENT_TREE.get()));
        tempMap.putIfAbsent(CAStructures.WASP_DUNGEON.get(), DimensionStructuresSettings.DEFAULTS.get(CAStructures.WASP_DUNGEON.get()));

        chunkProvider.generator.getSettings().structureConfig = tempMap;
    }
}