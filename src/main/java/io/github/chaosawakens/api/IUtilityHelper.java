package io.github.chaosawakens.api;

import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.chaosawakens.ChaosAwakens;
import io.github.chaosawakens.common.registry.CATags;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.FogRenderer.FogType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

public interface IUtilityHelper {

	///////////////////////////////
	// Variables //
	///////////////////////////////

	List<Block> Queue = new ArrayList<>();

	///////////////////////////////
	// Functions //
	///////////////////////////////

	/**
	 * Set the reach distance of a player to a new value
	 * 
	 * @param player        player to apply modified reach to
	 * @param newReachValue the new reach value applied to the attribute the player
	 *                      has, it doesn't add to the original attribute value, but
	 *                      sets it to a new value
	 */
	static void setReach(PlayerEntity player, int newReachValue) {
		player.getAttribute(ForgeMod.REACH_DISTANCE.get()).setBaseValue(newReachValue);
	}

	/**
	 * Sets the reach distance of a player to a new value, but only in the condition
	 * that the player is holding the stack defined
	 * 
	 * @param player        player to apply modified reach to
	 * @param newReachValue the new reach value applied to the attribute the player
	 *                      has, it doesn't add to the original attribute value, but
	 *                      sets it to a new value
	 * @param stack         the item/itemstack needed (to be held in the player's
	 *                      main hand) to activate the attribute modifier
	 */
	static void setReach(PlayerEntity player, int newReachValue, ItemStack stack) {
		if (player.getItemInHand(Hand.MAIN_HAND) == stack) {
			player.getAttribute(ForgeMod.REACH_DISTANCE.get()).setBaseValue(newReachValue);
		}
	}

	/**
	 * Get the reach distance of the player
	 * 
	 * @param player player to check reach distance attribute value of
	 */
	static void getPlayerReach(PlayerEntity player) {
		player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getBaseValue();
	}

	/**
	 * Set the swim speed of a player to a new value
	 * 
	 * @param player            player to apply modified swim speed to
	 * @param newSwimSpeedValue the new swim speed value applied to the attribute
	 *                          the player has, it doesn't add to the original
	 *                          attribute value, but sets it to a new value
	 */
	static void setSwimSpeed(PlayerEntity player, int newSwimSpeedValue) {
		player.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(newSwimSpeedValue);
	}

	/**
	 * Sets the swim speed of a player to a new value, but only in the condition
	 * that the player is holding the stack defined
	 * 
	 * @param player            player to apply modified reach to
	 * @param newSwimSpeedValue the new swim speed value applied to the attribute
	 *                          the player has, it doesn't add to the original
	 *                          attribute value, but sets it to a new value
	 * @param stack             the item/itemstack needed (to be held in the
	 *                          player's main hand) to activate the attribute
	 *                          modifier
	 */
	static void setSwimSpeed(PlayerEntity player, int newSwimSpeedValue, ItemStack stack) {
		if (player.getItemInHand(Hand.MAIN_HAND) == stack) {
			player.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(newSwimSpeedValue);
		}
	}

	/**
	 * Get the swim speed of the player
	 * 
	 * @param player player to check swim speed attribute value of
	 */
	static void getPlayerSwimSpeed(PlayerEntity player) {
		player.getAttribute(ForgeMod.SWIM_SPEED.get()).getBaseValue();
	}

	/**
	 * Sets the gravity of a player to a new value
	 * 
	 * @param player                player to apply modified gravity to
	 * @param newPlayerGravityValue the new gravity value appled to the attribute
	 *                              the player has, it doesn't add to the original
	 *                              value, but sets it to a new value
	 */
	static void setPlayerGravity(PlayerEntity player, int newPlayerGravityValue) {
		player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(newPlayerGravityValue);
	}

	/**
	 * Sets the gravity of a player to a new value, but only in the condition that
	 * the player is holding the stack defined
	 * 
	 * @param player                player to apply modified gravity to
	 * @param newPlayerGravityValue the new gravity value appled to the attribute
	 *                              the player has, it doesn't add to the original
	 *                              value, but sets it to a new value
	 * @param stack                 the item/itemstack needed (to be held in the
	 *                              player's main hand) to activate the attribute
	 *                              modifier
	 */
	static void setPlayerGravity(PlayerEntity player, int newPlayerGravityValue, ItemStack stack) {
		if (player.getItemInHand(Hand.MAIN_HAND) == stack) {
			player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(newPlayerGravityValue);
		}
	}

	/**
	 * Get the gravity of the player
	 * 
	 * @param player player to check gravity attribute value of
	 */
	static void getPlayerGravity(PlayerEntity player) {
		player.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).getBaseValue();
	}

	/**
	 * Summon particles into the world
	 * 
	 * @param world        world to summon particles in
	 * @param particleData particle data/particle type, just grab whatever particle
	 *                     type you want from the <code>ParticleTypes</code> class
	 * @param x            x position to add particles to
	 * @param y            y position to add particles to
	 * @param z            z position to add particles to
	 * @param randomXValue random value within the x position to add particles
	 * @param randomYValue random value within the y position to add particles
	 * @param randomZValue random value within the z position to add particles
	 * @param amount       amount of particles to add
	 */
	@SuppressWarnings("resource")
	static void addParticles(World world, IParticleData particleData, double x, double y, double z, double randomXValue, double randomYValue, double randomZValue, int amount) {
		if (world.isClientSide) {
			for (int particleCount = 0; particleCount <= amount; particleCount++) {
				Minecraft.getInstance().particleEngine.createParticle(particleData, x + getRandomHorizontalPos(randomXValue, world), y + getRandomVerticalPos(randomYValue, world), z + getRandomHorizontalPos(randomZValue, world), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	/**
	 * Add a certain number of living entities within a bounding box in a certain
	 * position
	 * 
	 * @param world         world to add entities in
	 * @param entitiesToAdd the list<LivingEntity> of entities to add
	 * @param pos           position to add entities in. CANNOT BE NULL!
	 * @param x             x position to add entities
	 * @param y             y position to add entities
	 * @param z             z position to add entities
	 * @param xSize         the x size of the bounding box
	 * @param ySize         the y size of the bounding box
	 * @param zSize         the z size of the bounding box
	 * @param amountToSpawn amount of entities inside the entitiesToAdd list, that
	 *                      amount will be summoned
	 */
	static void addLivingEntities(World world, List<LivingEntity> entitiesToAdd, @Nonnull BlockPos pos, double x, double y, double z, double xSize, double ySize, double zSize, int amountToSpawn) {
		if (!world.isClientSide) {
			amountToSpawn = entitiesToAdd.size();
			entitiesToAdd = world.getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(pos).inflate(xSize, ySize, zSize)));
			for (int entityCount = 0; entityCount <= amountToSpawn; entityCount++) {
				if (pos != null) {
					world.addFreshEntity((Entity) entitiesToAdd);
				}
			}
		}
	}

	/**
	 * Summon a singular entity in a certain position (checks if the world isn't
	 * client side first by static)
	 * 
	 * @param world          world to summon entity in
	 * @param entityToSummon entity to be summoned
	 * @param pos            position to summon entity in
	 */
	static void summonLivingEntity(World world, LivingEntity entityToSummon, BlockPos pos) {
		if (!world.isClientSide) {
			world.addFreshEntity(entityToSummon);
		}
	}

	/**
	 * Adds fog after checking if the world is client side, to save you a little
	 * hassle
	 * 
	 * @param world   world to add fog in
	 * @param density fog density
	 * @param b       boolean
	 * @param pTicks  particle ticks
	 * @param type    fog type
	 * @param info    active render information
	 */
	static void addFog(World world, float density, boolean b, float pTicks, FogType type, ActiveRenderInfo info) {
		if (world.isClientSide) {
			FogRenderer.setupFog(info, type, density, false, pTicks);
		}
	}

	/**
	 * Adds the block specified to the <code>Blacklist</code> tag
	 * 
	 * @param blockToAdd block to add <code>Blacklist</code> tag to
	 */
	static void addBlockToDuplicationsBlackList(Block blockToAdd) {
		blockToAdd.getTags().add((ResourceLocation) CATags.Blocks.tag("blacklist"));
	}

	/**
	 * Adds the block specified to the <code>Whitelist</code> tag
	 * 
	 * @param blockToAdd block to add <code>Whitelist</code> tag to
	 */
	static void addBlockToDuplicationsWhiteList(Block blockToAdd) {
		blockToAdd.getTags().add((ResourceLocation) CATags.Blocks.tag("whitelist"));
	}

	/**
	 * Sets the item texture to something else in-game.
	 * 
	 * @param itemToSet        item to replace texture of
	 * @param pathWithTextures path containing textures
	 * @param textureName      name of texture file
	 * @param pathName         name of path, could simply be some characters in the
	 *                         name (e.g.
	 *                         <code>assets.chaosawakens.textures.item.critter_cage</code>
	 *                         can simply be <code>critter_cage</code>, or you could
	 *                         run the <code>getPath()</code> method off of a
	 *                         certain item in that folder/package you want to grab)
	 */
	static void setItemTexture(ItemStack itemToSet, ResourceLocation pathWithTextures, String textureName, String pathName) {
		boolean hasTextures = pathWithTextures.getPath().contains(pathName);
		if (hasTextures) {
			ItemModelsProperties.register(itemToSet.getItem(), pathWithTextures, (stack, world, entity) -> {
				stack = itemToSet;
				String modid = entity.getType().getRegistryName().getNamespace();
				String regName = entity.getType().getRegistryName().toString().replace(modid, "").replace(":", "");
				boolean nameMatches = entity.getType().getRegistryName().toString().contains(regName);
				return hasTextures && nameMatches ? 1.0F : 0.0F;
			});
		}
	}

	///////////////////////////////
	// Booleans //
	///////////////////////////////

	/**
	 * Gets an entity moving in any direction
	 * 
	 * @param entity entity to check
	 * @return true if entity is moving, else returns false
	 */
	static boolean isMoving(LivingEntity entity) {
		return entity.moveDist > 0 || entity.getSpeed() == 0;
	}

	/**
	 * Check the speed of an entity
	 * 
	 * @param speed         speed entity is moving at
	 * @param entityToCheck entity to check moving speed of
	 * @return true if the speed of the entity is equal to the speed parameter, else
	 *         returns false
	 */
	static boolean isMovingAtVelocity(float speed, LivingEntity entityToCheck) {
		return entityToCheck.getSpeed() == speed;
	}

	/**
	 * Checks the uuid of an entity or player
	 * 
	 * @param entityToCheck entity to check uuid of
	 * @param uuidToCheck   uuid of entity to check
	 * @return true if entity's uuid is equal to uuidToCheck, else returns false
	 */
	static boolean isUserOrEntityUUIDEqualTo(Entity entityToCheck, UUID uuidToCheck) {
		return entityToCheck.getUUID().equals(uuidToCheck);
	}

	/**
	 * Gets the name of an entity or player
	 * 
	 * @param nameToCheck   name of entity to check
	 * @param entityToCheck entity to check name of
	 * @return true if entity name is equal to the name provided, else returns false
	 */
	static boolean isEntityNameEqualTo(Entity entityToCheck, String nameToCheck) {
		return entityToCheck.getName().getString().equals(nameToCheck);
	}

	/**
	 * Check if a block can be duplicated by the duplication tree
	 * 
	 * @param blockToCheck block to check for duplicatability (that's a word now)
	 * @return true if the block to check is duplicatable, else returns false
	 */
	static boolean isDuplicatable(Block blockToCheck) {
		return blockToCheck.is(CATags.Blocks.WHITELIST) || !blockToCheck.is(CATags.Blocks.BLACKLIST) || !blockToCheck.is(CATags.Blocks.BLACKLIST) && !blockToCheck.is(CATags.Blocks.WHITELIST);
	}

	/**
	 * Checks if an entity is moving on the y axis
	 * 
	 * @param entity entity to check
	 * @return true if entity is moving vertically, else returns false
	 */
	static boolean isMovingVertically(LivingEntity entity) {
		return false;
	}

	static boolean isBoss(LivingEntity entityToCheck) {
		return false;
	}

	/**
	 * Gets all of an entity's active effects if it has any
	 * 
	 * @param entity entity to check for effects
	 * @return true if any effects are present, else returns false
	 */
	static boolean hasActiveEffects(LivingEntity entity) {
		return entity.getActiveEffects() != null;
	}

	/**
	 * Gets an entity's bounding box, if it has any
	 * 
	 * @param entity entity to check for bounding box
	 * @return true if entity has a bounding box, else returns false
	 */
	static boolean hasBoundingBox(LivingEntity entity) {
		return entity.getBoundingBox() != null;
	}

	/**
	 * Checks if an item specified is in the player's inventory
	 * 
	 * @param stackToCheck stack to check in inventory
	 * @return true if the player has the specified item in their inventory, else
	 *         returns false
	 */
	static boolean hasItemInInventory(PlayerEntity player, ItemStack stackToCheck) {
		return player.inventory.items.contains(stackToCheck);
	}

	// WIP
	/*
	 * static boolean hasItemInInventoryByAmount(PlayerEntity player, ItemStack
	 * stackToCheck, int amount) { return NonNullList.withSize(amount, stackToCheck)
	 * != null; }
	 */

	/**
	 * Checks if an entity matches another entity stored inside the itemstack (which
	 * will most likely be a critter cage)
	 * 
	 * @param entity    entity to check name of
	 * @param cageStack itemstack to grab data from
	 * @return true if entity name matches the one stored in the itemstack, else
	 *         returns false
	 */
	static boolean matchesNameOfEntityStoredInItemStack(Entity entity, ItemStack cageStack) {
		return entity.getType().getRegistryName().toString().matches(cageStack.getTag().getString(entity.getType().getRegistryName().toString())) && entity != null && cageStack != null;
	}

	/**
	 * Checks if a block is part of the duplication queue list, can be used to
	 * arrange duplication patterns if there's multiple pattern lists
	 * 
	 * @param blockToCheck block to check for queue validity
	 * @return true if the block is inside the <code>Queue</code> list, else returns
	 *         false
	 */
	static boolean isInQueueForDuplication(Block blockToCheck) {
		return Queue.contains(blockToCheck);
	}

	/**
	 * Checks if a certain thing is part of chaos awakens
	 * 
	 * @param name resource location/registry to check
	 * @return true if it's part of chaos awakens, else returns false
	 */
	static boolean isChaosAwakens(ResourceLocation name) {
		return name.getNamespace().equalsIgnoreCase(ChaosAwakens.MODID);
	}

	/**
	 * Checks if an itemstack has an entity/entity data stored inside it
	 * 
	 * @param stackToCheck stack to check for entities
	 * @return true if the itemstack has an entity stored in it, else returns false
	 */
	static boolean stackHasMob(ItemStack stackToCheck) {
		return !(stackToCheck.isEmpty()) && stackToCheck.hasTag() && stackToCheck.getTag().contains("entity");
	}

	/**
	 * Checks if something is from a mod, specified by the modid
	 * 
	 * @param loc   resource location/registry to check
	 * @param modid the modid for the stuff you wanna get, from the loc
	 * @return true if stuff is from specified modid, else returns false
	 */
	static boolean isFromMod(ResourceLocation loc, String modid) {
		return loc.getNamespace().equalsIgnoreCase(modid);
	}

	/**
	 * Checks if an entity in air
	 * 
	 * @param entity entity to check
	 * @return true if entity is standing on air, else returns false
	 */
	static boolean isInAir(Entity entity) {
		return entity.blockPosition().below() == null;
	}

	/**
	 * Checks if an entity is completely surrounded by air from all cardinal
	 * directions
	 * 
	 * @param entity entity to check
	 * @return true if entity is completely surrounded by air from all
	 *         sides/cardinal directions, else returns false
	 */
	static boolean isSurroundedByAir(Entity entity) {
		return entity.blockPosition().below() == null && entity.blockPosition().above() == null && entity.blockPosition().east() == null && entity.blockPosition().west() == null && entity.blockPosition().north() == null && entity.blockPosition().south() == null;
	}

	///////////////////////////////
	// Doubles //
	///////////////////////////////

	/**
	 * Gets the distance between 2 positions on the x and z axis
	 * 
	 * @param a starting blockpos
	 * @param b finishing blockpos
	 * @return the distance between a and b
	 */
	static double getHorizontalDistanceBetween(BlockPos a, BlockPos b) {
		int x = Math.abs(a.getX() - b.getX());
		int z = Math.abs(a.getZ() - b.getZ());

		return Math.sqrt(x * x + z * z);
	}

	/**
	 * Gets the distance between 2 positions on the y axis
	 * 
	 * @param a starting blockpos
	 * @param b finishing blockpos
	 * @return the distance between a and b
	 */
	static double getVerticalDistanceBetween(BlockPos a, BlockPos b) {
		int y = Math.abs(a.getY() - b.getY());

		return Math.sqrt(y * y);
	}

	/**
	 * Gets the distance between 2 positions on all axis
	 * 
	 * @param a starting blockpos
	 * @param b finishing blockpos
	 * @return the distance between a and b
	 */
	static double getDistanceBetween(@Nullable BlockPos a, @Nullable BlockPos b) {
		int x = Math.abs(a.getX() - b.getX());
		int y = Math.abs(a.getY() - b.getY());
		int z = Math.abs(a.getZ() - b.getZ());

		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Gets the horizontal distance (distance on the x and z axis) between an entity
	 * and a block position
	 * 
	 * @param entity point a, where entity is
	 * @param pos    point b, where the destination pos is
	 * @return the distance (on the x and z axis) between the entity and the
	 *         destination pos
	 */
	static double getHorizontalDistanceBetweenEntityAndBlockPos(Entity entity, BlockPos pos) {
		int x = Math.abs((int) entity.getX() - pos.getX());
		int z = Math.abs((int) entity.getZ() - pos.getZ());

		return Math.sqrt(x * x + z * z);
	}

	/**
	 * Gets the vertical distance (distance on the y axis) between an entity and a
	 * block position
	 * 
	 * @param entity point a, where entity is
	 * @param pos    point b, where the destination pos is
	 * @return the distance (on the y axis) between the entity and the destination
	 *         pos
	 */
	static double getVerticalDistanceBetweenEntityAndBlockPos(Entity entity, BlockPos pos) {
		int y = Math.abs((int) entity.getY() - pos.getY());

		return Math.sqrt(y * y);
	}

	/**
	 * Gets distance between entity and a certain point
	 * 
	 * @param entity point a, where entity is
	 * @param pos    point b, where the destination pos is
	 * @return the distance between the entity and the destination pos
	 */
	static double getDistanceBetweenEntityAndBlockPos(Entity entity, BlockPos pos) {
		int x = Math.abs((int) entity.getX() - pos.getX());
		int y = Math.abs((int) entity.getY() - pos.getY());
		int z = Math.abs((int) entity.getZ() - pos.getZ());

		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Gets the horizontal distance (on the x and z axis) between 2 entities
	 * 
	 * @param entityA entity point a, starting pos to check
	 * @param entityB entity point b, finishing pos to check
	 * @return the distance (on the x and z axis) between the 2 entities
	 */
	static double getHorizontalDistanceBetweenEntities(Entity entityA, Entity entityB) {
		double x = Math.abs(entityA.getX() - entityB.getX());
		double z = Math.abs(entityA.getZ() - entityB.getZ());

		return Math.sqrt(x * x + z * z);
	}

	/**
	 * Gets the vertical distance (on the y axis) between 2 entities
	 * 
	 * @param entityA entity point a, starting pos to check
	 * @param entityB entity point b, finishing pos to check
	 * @return the distance (on the y axis) between the 2 entities
	 */
	static double getVerticalDistanceBetweenEntities(Entity entityA, Entity entityB) {
		double y = Math.abs(entityA.getY() - entityB.getY());

		return Math.sqrt(y * y);
	}

	/**
	 * Gets distance between 2 entities
	 * 
	 * @param entityA entity point a, starting pos to check
	 * @param entityB entity point b, finishing pos to check
	 * @return the distance between the 2 entities
	 */
	static double getDistanceBetweenEntities(@Nullable Entity entityA, @Nullable Entity entityB) {
		double x = Math.abs(entityA.getX() - entityB.getX());
		double y = Math.abs(entityA.getY() - entityB.getY());
		double z = Math.abs(entityA.getZ() - entityB.getZ());

		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Gets a random position horizontally. This is mainly used for particle
	 * summoning, but can be used for other purposes (Thanks to cyclic for this
	 * piece of code)
	 * 
	 * @param random random value
	 * @param world  world to get pos in
	 * @return random position to execute an event in
	 */
	static double getRandomHorizontalPos(double random, World world) {
		return (world.random.nextDouble() - 0.5D) * random;
	}

	/**
	 * Gets a random position vertically. This is mainly used for particle
	 * summoning, but can be used for other purposes (Thanks to cyclic for this
	 * piece of code)
	 * 
	 * @param random random value
	 * @param world  world to get pos in
	 * @return random position to execute an event in
	 */
	static double getRandomVerticalPos(double random, World world) {
		return world.random.nextDouble() - 0.1D * random;
	}

	/**
	 * Get the square root of a number/double without the hassle of typing
	 * <code>Math.sqrt()</code>
	 * 
	 * @param numberToGetSquareRootOf number to find square root of
	 * @return square root of the number entered
	 */
	static double squareRoot(double numberToGetSquareRootOf) {
		return Math.sqrt(numberToGetSquareRootOf);
	}

	/**
	 * Get the number squared
	 * 
	 * @param numberSquared number to get squared (number * number) --How could you
	 *                      not know this and code?
	 * @return the number entered squared (number * number)
	 */
	static double squared(double numberSquared) {
		return numberSquared * numberSquared;
	}

	/**
	 * I don't know how this could be used, I genuinely made this out of boredom -
	 * Meme Man
	 * 
	 * @param a          variable a
	 * @param b          variable b
	 * @param c          variable c
	 * @param isAddition whether or not the operation should be addition. If false,
	 *                   it'll be subtraction.
	 * @return <code>(-b + sqrt of bsq - 4ac / 2a)</code> if <code>isAddition</code>
	 *         is true, else <code>(-b - sqrt of bsq - 4ac / 2a)</code>
	 */
	static double quadraticFormula(double a, double b, double c, boolean isAddition) {
		if (isAddition) {
			return -b + squareRoot(squared(b)) - 4 * a * c / 2 * a;
		}
		return -b - squareRoot(squared(b)) - 4 * a * c / 2 * a;
	}

	///////////////////////////////
	// Ints //
	///////////////////////////////

	/**
	 * Sets the duplication tree duplicator cap
	 * 
	 * @return amount of blocks duplication tree can duplicate
	 */
	static int setDuplicationCap() {
		return 36;
	}

	/**
	 * Gets the cap specified in the method <code>setDuplicationCap()</code>
	 * 
	 * @return the duplication cap that was specified in
	 *         <code>setDuplicationCap()</code>
	 */
	static int getDuplicationCap() {
		return setDuplicationCap();
	}

	/**
	 * Sets the duplication tree update rate/speed in duplicating blocks
	 * 
	 * @return amount of ticks duplication tree undergoes before duplicating another
	 *         block
	 */
	static int setDuplicationSpeedAKATickRate() {
		return 2000;
	}

	/**
	 * Gets the duplication tree update rate/speed in duplicating blocks specified
	 * in <code>setDuplicationSpeedAKATickRate()</code>
	 * 
	 * @return amount of ticks duplication tree undergoes before duplicating another
	 *         block specified in <code>setDuplicationSpeedAKATickRate()</code>
	 */
	static int getDuplicationSpeedAKATickRate() {
		return setDuplicationSpeedAKATickRate();
	}

	///////////////////////////////
	// Lists //
	///////////////////////////////

	/**
	 * Adds blocks to a queue list to be grabbed for duplication
	 * 
	 * @param blockToAdd block to add to duplication queue
	 * @return the list <code>Queue</code>
	 */
	static List<Block> addBlockToDuplicationQueue(Block blockToAdd) {
		if (!blockToAdd.is(CATags.Blocks.BLACKLIST) && blockToAdd != null && !(Queue.contains(blockToAdd))) {
			Queue.add(blockToAdd);
		}
		return Queue;
	}

	///////////////////////////////
	// Other Method Types //
	///////////////////////////////

	/**
	 * Reads NBT data. To put it simply, this just reads data input from the
	 * <code>writeToNBT</code> method
	 * 
	 * @param nbtToRead nbt to read data of
	 * @return NBT data read
	 */
	static CompoundNBT readFromNBT(CompoundNBT nbtToRead) {
		return nbtToRead;
	}

	/**
	 * Writes NBT data to a (usually) JSON file
	 * 
	 * @return Written NBT data
	 */
	static CompoundNBT writeToNBT() {
		return new CompoundNBT();
	}

	/**
	 * Gets/Writes NBT data from/to a living entity
	 * 
	 * @param entityToGetNBTDataFrom entity to get NBT data from
	 * @return the entity data/NBT
	 */
	static CompoundNBT getNBTDataFromLivingEntity(LivingEntity entityToGetNBTDataFrom) {
		return new CompoundNBT();
	}

}
