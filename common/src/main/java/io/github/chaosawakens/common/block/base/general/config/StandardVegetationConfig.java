package io.github.chaosawakens.common.block.base.general.config;

import io.github.chaosawakens.common.block.base.general.CustomizableDoublePlantBlock;
import io.github.chaosawakens.common.block.base.general.CustomizableGrassBlock;
import io.github.chaosawakens.common.block.base.general.CustomizableSpreadingDirtBlock;
import io.github.chaosawakens.common.block.base.general.CustomizableTallGrassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Base {@code interface} for vegetation blocks implementing different VegetationConfig types. Serves as a reference server/accessor. Centralizes vegetation block implementations that would otherwise require the
 * creation of numerous different {@link Block} subclasses with extensive BP code.
 * <br></br>
 * Despite what the name may imply to those more acquainted with MC's naming conventions, neither this {@code interface} nor the objects it stores are serializable types and, as such, store
 * absolutely no CODECs.
 *
 * @see DirtVegetationConfig
 * @see GrassVegetationConfig
 * @see VegetationConfig
 */
public interface StandardVegetationConfig {

    /**
     * Vegetation Config utilised by standard {@link SpreadingSnowyDirtBlock} implementations.
     *
     * @return This instance's {@link DirtVegetationConfig}. May be {@code null}.
     *
     * @see CustomizableSpreadingDirtBlock
     */
    @Nullable
    Supplier<DirtVegetationConfig> getDirtConfig();

    /**
     * Vegetation Config utilised by {@link CustomizableGrassBlock} implementations.
     *
     * @return This instance's {@link GrassVegetationConfig}. May be {@code null}.
     * @see CustomizableGrassBlock
     */
    Supplier<GrassVegetationConfig> getGrassConfig();

    /**
     * Vegetation Config utilised by extended {@link DoublePlantBlock} and {@link TallGrassBlock} implementations.
     *
     * @return This instance's {@link GrassVegetationConfig}. May be {@code null}.
     *
     * @see CustomizableDoublePlantBlock
     * @see CustomizableTallGrassBlock
     */
    @Nullable
    Supplier<PlantVegetationConfig> getPlantConfig();
}
