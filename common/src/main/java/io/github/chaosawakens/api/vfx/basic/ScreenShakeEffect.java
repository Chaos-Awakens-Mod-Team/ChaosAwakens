package io.github.chaosawakens.api.vfx.basic;

import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.common.networking.packets.s2c.ScreenShakePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Oneshot VFX effect for basic screenshake. Will be part of the larger/more comprehensive VFXL system later on.
 */
public class ScreenShakeEffect {
    private static final ConcurrentLinkedQueue<ScreenShakeEffect> SHAKES = new ConcurrentLinkedQueue<>();
    private final ShakeData data;

    public ScreenShakeEffect(ShakeData data) {
        this.data = data;
    }

    public ScreenShakeEffect(BlockPos originPos, double range, float magnitude, float duration, float fadeOut) {
        this(new ShakeData(originPos, range, magnitude, duration, fadeOut));
    }

    public ShakeData getData() {
        return data;
    }

    public void enqueue(Level curLevel) {
        if (curLevel == null || curLevel.isClientSide) SHAKES.add(this);
        if (curLevel != null && !curLevel.isClientSide) CAServices.NETWORK_MANAGER.sendToAllClients(new ScreenShakePacket(data.getOriginPos(), data.getRange(), data.getMagnitude(), data.getDuration(), data.getFadeOut()));
    }

    public static ConcurrentLinkedQueue<ScreenShakeEffect> getEnqueuedShakes() {
        return SHAKES;
    }

    public static class ShakeData {
        private final BlockPos originPos;
        private final double range;
        private final float magnitude;
        private float duration;
        private final float fadeOut;

        public ShakeData(BlockPos originPos, double range, float magnitude, float duration, float fadeOut) {
            this.originPos = originPos;
            this.range = range;
            this.magnitude = magnitude;
            this.duration = duration;
            this.fadeOut = fadeOut;
        }

        public BlockPos getOriginPos() {
            return originPos;
        }

        public double getRange() {
            return range;
        }

        public float getMagnitude() {
            return magnitude;
        }

        public float getDuration() {
            return duration;
        }

        public float getFadeOut() {
            return fadeOut;
        }

        public void setDuration(float duration) {
            this.duration = duration;
        }
    }
}
