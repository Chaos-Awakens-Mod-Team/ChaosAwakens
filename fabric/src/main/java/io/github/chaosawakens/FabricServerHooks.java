package io.github.chaosawakens;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

public final class FabricServerHooks {
    private static MinecraftServer CURRENT_SERVER;

    static void handleServerLifecycleHooks() {
        ServerLifecycleEvents.SERVER_STARTING.register((targetServer) -> CURRENT_SERVER = targetServer);
        ServerLifecycleEvents.SERVER_STOPPING.register((targetServer) -> CURRENT_SERVER = null);
    }

    @Nullable
    public static MinecraftServer getCurrentServer() {
        return CURRENT_SERVER;
    }
}
