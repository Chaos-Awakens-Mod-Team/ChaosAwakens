package io.github.chaosawakens;

import net.fabricmc.api.ModInitializer;

public class ChaosAwakensFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ChaosAwakens.setup();

        FabricServerHooks.handleServerLifecycleHooks();

        FabricVanillaIntegration.handleMiscellaneousRegistration();
        FabricVanillaIntegration.handleVanillaIntegration();
    }
}
