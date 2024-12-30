package io.github.chaosawakens.common.registry;

import com.google.common.collect.ImmutableList;
import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.common.enchantments.HoplologyEnchantment;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Supplier;

@RegistrarEntry
public class CAEnchantments {
    private static final ObjectArrayList<Supplier<Enchantment>> ENCHANTMENTS = new ObjectArrayList<>();

    private static final EquipmentSlot[] ARMOR_SLOTS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    // Enchantments
    public static final Supplier<Enchantment> HOPLOLOGY = registerEnchantment("hoplology", () -> new HoplologyEnchantment(ARMOR_SLOTS));

    private static Supplier<Enchantment> registerEnchantment(String id, Supplier<Enchantment> enchantmentSup) {
        Supplier<Enchantment> registeredEnchantmentSup = CAServices.REGISTRAR.registerObject(CAConstants.prefix(id), enchantmentSup, BuiltInRegistries.ENCHANTMENT); // Otherwise reference to the enchantment sup is null cuz it needs to be registered b4hand
        ENCHANTMENTS.add(registeredEnchantmentSup);
        return registeredEnchantmentSup;
    }

    public static ImmutableList<Supplier<Enchantment>> getEnchantments() {
        return ImmutableList.copyOf(ENCHANTMENTS);
    }
}
