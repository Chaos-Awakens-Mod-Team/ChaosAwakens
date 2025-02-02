package io.github.chaosawakens.common.networking.packets.s2c;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.animation.faal.base.ExtendedAnimationState;
import io.github.chaosawakens.api.animation.faal.base.WrappedAnimatable;
import io.github.chaosawakens.api.network.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class AnimationPlayPacket {
    private final int ownerId;
    private final String animationName;
    private final boolean forcePose;

    public AnimationPlayPacket(int ownerId, String animationName, boolean forcePose) {
        this.ownerId = ownerId;
        this.animationName = animationName;
        this.forcePose = forcePose;
    }

    public static AnimationPlayPacket decode(FriendlyByteBuf buf) {
        return new AnimationPlayPacket(buf.readInt(), buf.readUtf(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(ownerId);
        buf.writeUtf(animationName);
        buf.writeBoolean(forcePose);
    }

    public static PacketContext handle(AnimationPlayPacket packet) {
        return (packetHandler, curLevel, curSide) -> {
            Entity targetEntity = curLevel.getEntity(packet.ownerId);

            if (targetEntity instanceof WrappedAnimatable wrappedAnimatable) {
                Optional<ExtendedAnimationState> targetAnimOptional = wrappedAnimatable.getCachedAnimationStates().stream().filter(curAnimState -> curAnimState.getAnimationName().equals(packet.animationName)).findFirst();

                targetAnimOptional.ifPresentOrElse(
                        targetAnim -> wrappedAnimatable.playAnimation(packet.animationName, packet.forcePose),
                        () -> CAConstants.LOGGER.warn("Attempted to play unknown animation: {}", packet.animationName));
            } else CAConstants.LOGGER.warn("Attempted to play animation for unknown entity: {}", packet.ownerId);
        };
    }
}
