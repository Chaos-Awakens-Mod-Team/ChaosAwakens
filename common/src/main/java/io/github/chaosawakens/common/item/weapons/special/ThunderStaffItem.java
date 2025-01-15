package io.github.chaosawakens.common.item.weapons.special;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ThunderStaffItem extends Item {

    public ThunderStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level curLevel, Player ownerPlayer, InteractionHand curHand) {
        ItemStack itemStack = ownerPlayer.getItemInHand(curHand);

        return super.use(curLevel, ownerPlayer, curHand);
    }
}
