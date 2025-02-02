package io.github.chaosawakens.common.networking;

import io.github.chaosawakens.api.asm.annotations.NetworkRegistrarEntry;
import io.github.chaosawakens.api.network.BasePacket;
import io.github.chaosawakens.api.network.NetworkSide;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.common.networking.packets.s2c.AnimationPlayPacket;
import io.github.chaosawakens.common.networking.packets.s2c.AnimationStopPacket;
import io.github.chaosawakens.common.networking.packets.s2c.ScreenShakePacket;

@NetworkRegistrarEntry
public final class CAPackets {

    // Animation
    public static final BasePacket<AnimationPlayPacket> ANIMATION_PLAY_PACKET = registerPacket(new BasePacket<>(AnimationPlayPacket.class, AnimationPlayPacket::encode, AnimationPlayPacket::decode, AnimationPlayPacket::handle, NetworkSide.S2C));
    public static final BasePacket<AnimationStopPacket> ANIMATION_STOP_PACKET = registerPacket(new BasePacket<>(AnimationStopPacket.class, AnimationStopPacket::encode, AnimationStopPacket::decode, AnimationStopPacket::handle, NetworkSide.S2C));

    // VFX
    public static final BasePacket<ScreenShakePacket> SCREEN_SHAKE_PACKET = registerPacket(new BasePacket<>(ScreenShakePacket.class, ScreenShakePacket::encode, ScreenShakePacket::decode, ScreenShakePacket::handle, NetworkSide.S2C));

    private static <MSGT> BasePacket<MSGT> registerPacket(BasePacket<MSGT> packet) {
        return CAServices.NETWORK_MANAGER.registerPacket(packet);
    }
}
