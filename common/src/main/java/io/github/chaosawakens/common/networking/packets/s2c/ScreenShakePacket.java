package io.github.chaosawakens.common.networking.packets.s2c;

import io.github.chaosawakens.api.network.PacketContext;
import io.github.chaosawakens.api.vfx.basic.ScreenShakeEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class ScreenShakePacket {
    private final BlockPos originPos;
    private final double range;
    private final float magnitude;
    private final float duration;
    private final float fadeOut;

    public ScreenShakePacket(BlockPos originPos, double range, float magnitude, float duration, float fadeOut) {
        this.originPos = originPos;
        this.range = range;
        this.magnitude = magnitude;
        this.duration = duration;
        this.fadeOut = fadeOut;
    }

    public static ScreenShakePacket decode(FriendlyByteBuf buf) {
        return new ScreenShakePacket(buf.readBlockPos(), buf.readDouble(), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(originPos);
        buf.writeDouble(range);
        buf.writeFloat(magnitude);
        buf.writeFloat(duration);
        buf.writeFloat(fadeOut);
    }

    public static PacketContext handle(ScreenShakePacket dedcodedPacket) {
        return (packetHandler, curLevel, curSide) -> new ScreenShakeEffect(dedcodedPacket.originPos, dedcodedPacket.range, dedcodedPacket.magnitude, dedcodedPacket.duration, dedcodedPacket.fadeOut).enqueue(curLevel);
    }
}
