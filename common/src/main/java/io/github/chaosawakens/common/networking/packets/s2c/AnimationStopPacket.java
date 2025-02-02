package io.github.chaosawakens.common.networking.packets.s2c;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.animation.faal.base.ExtendedAnimationState;
import io.github.chaosawakens.api.animation.faal.base.WrappedAnimatable;
import io.github.chaosawakens.api.network.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class AnimationStopPacket {
    private final int ownerId;
    private final String animationName;

    public AnimationStopPacket(int ownerId, String animationName) {
        this.ownerId = ownerId;
        this.animationName = animationName;
    }

    public static AnimationStopPacket decode(FriendlyByteBuf buf) {
        return new AnimationStopPacket(buf.readInt(), buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(ownerId);
        buf.writeUtf(animationName);
    }

    public static PacketContext handle(AnimationStopPacket packet) {
        return (packetHandler, curLevel, curSide) -> {
            Entity targetEntity = curLevel.getEntity(packet.ownerId);

            if (targetEntity instanceof WrappedAnimatable wrappedAnimatable) {
                Optional<ExtendedAnimationState> targetAnimOptional = wrappedAnimatable.getCachedAnimationStates().stream().filter(curAnimState -> curAnimState.getAnimationName().equals(packet.animationName)).findFirst();

                targetAnimOptional.ifPresentOrElse(
                        targetAnim -> wrappedAnimatable.stopAnimation(packet.animationName),
                        () -> CAConstants.LOGGER.warn("Attempted to play unknown animation: {}", packet.animationName));
            } else CAConstants.LOGGER.warn("Attempted to play animation for unknown entity: {}", packet.ownerId);
        };
    }
}
