package io.github.chaosawakens.common.blocks.tileentities;

import io.github.chaosawakens.ChaosAwakens;
import io.github.chaosawakens.common.registry.CATileEntities;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CrystalFurnaceTileEntity extends AbstractFurnaceTileEntity {
	public CrystalFurnaceTileEntity() {
		super(CATileEntities.CRYSTAL_FURNACE.get(), IRecipeType.SMELTING);
	}

	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container." + ChaosAwakens.MODID + ".crystal_furnace");
	}

	protected Container createMenu(int id, PlayerInventory player) {
		return new CrystalFurnaceContainer(id, player, this, this.dataAccess);
	}
}
