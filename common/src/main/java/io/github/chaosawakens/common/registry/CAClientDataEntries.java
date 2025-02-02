package io.github.chaosawakens.common.registry;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import io.github.chaosawakens.api.asm.annotations.RegistrarEntry;
import io.github.chaosawakens.api.entity.ClientDataEntry;
import io.github.chaosawakens.client.model.hostile.robo.RoboPounderModel;
import io.github.chaosawakens.client.renderer.hostile.robo.RoboPounderRenderer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;

import java.util.function.Supplier;

@RegistrarEntry
public class CAClientDataEntries {
    private static final ObjectArrayList<Supplier<ClientDataEntry>> CLIENT_DATA_ENTRIES = new ObjectArrayList<>();

    // Robo
    public static final Supplier<ClientDataEntry> ROBO_POUNDER = registerClientDataEntry(Suppliers.ofInstance(new ClientDataEntry(CAEntityTypes.ROBO_POUNDER, (ctx) -> () -> new RoboPounderRenderer<>(ctx.get()), ObjectObjectImmutablePair.of(() -> RoboPounderModel.BASE_LAYER, RoboPounderModel::createBodyLayer))));

    private static Supplier<ClientDataEntry> registerClientDataEntry(Supplier<ClientDataEntry> clientDataEntrySup) {
        if (!CLIENT_DATA_ENTRIES.contains(clientDataEntrySup)) CLIENT_DATA_ENTRIES.add(clientDataEntrySup);
        return clientDataEntrySup;
    }

    public static ImmutableList<Supplier<ClientDataEntry>> getClientDataEntries() {
        return ImmutableList.copyOf(CLIENT_DATA_ENTRIES);
    }
}
