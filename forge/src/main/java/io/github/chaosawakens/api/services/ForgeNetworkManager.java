package io.github.chaosawakens.api.services;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.api.asm.annotations.NetworkRegistrarEntry;
import io.github.chaosawakens.api.network.BasePacket;
import io.github.chaosawakens.api.network.NetworkSide;
import io.github.chaosawakens.api.network.PacketContext;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.api.platform.services.INetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeNetworkManager implements INetworkManager {
    private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(CAConstants.prefix("messages"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .simpleChannel();
    private static int PACKET_ID = 0;

    @Override
    public void setupNetworkHandler() {
        CAServices.PLATFORM.discoverAnnotatedClasses(NetworkRegistrarEntry.class);
    }

    @Override
    public <MSGT> BasePacket<MSGT> registerPacket(BasePacket<MSGT> packet) {
        INSTANCE.registerMessage(PACKET_ID++, packet.packetClass(), packet.packetEncoder(), packet.packetDecoder(), handlePacketFromContext(packet.packetHandler()), Optional.of(packet.targetSide().equals(NetworkSide.C2S) ? NetworkDirection.PLAY_TO_SERVER : NetworkDirection.PLAY_TO_CLIENT));
        return packet;
    }

    @Override
    public <MSGT> void sendToServer(MSGT c2sPacket) {
        INSTANCE.sendToServer(c2sPacket);
    }

    @Override
    public <MSGT> void sendToClient(MSGT s2cPacket) {
        INSTANCE.sendTo(s2cPacket, Objects.requireNonNull(Minecraft.getInstance().getConnection(), "Minecraft instance has no connection (Are you sending this packet outside of an active game?)").getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    @Override
    public <MSGT> void sendToAllClients(MSGT s2cPacket) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), s2cPacket);
    }

    @Override
    public <MSGT> void sendToTrackingClients(MSGT s2cPacket, Entity trackedEntity, boolean ignoreSelf) {
        INSTANCE.send(ignoreSelf ? PacketDistributor.TRACKING_ENTITY.with(() -> trackedEntity) : PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> trackedEntity), s2cPacket);
    }

    @Override
    public <MSGT> void sendToTrackingClients(MSGT s2cPacket, LevelChunk trackedChunk) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> trackedChunk), s2cPacket);
    }

    @Override
    public <MSGT> void sendToClientsInDimension(MSGT s2cPacket, ResourceKey<Level> targetDim) {
        INSTANCE.send(PacketDistributor.DIMENSION.with(() -> targetDim), s2cPacket);
    }

    @Override
    public <MSGT> void sendToClientsWithinRange(MSGT s2cPacket, ResourceKey<Level> targetDim, BlockPos originPos, double range) {
        INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(originPos.getX(), originPos.getY(), originPos.getZ(), range, targetDim)), s2cPacket);
    }

    @Override
    public <MSGT> void sendToClient(MSGT s2cPacket, ServerPlayer targetPlayer) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> targetPlayer), s2cPacket);
    }

    private <MSGT> BiConsumer<MSGT, Supplier<NetworkEvent.Context>> handlePacketFromContext(Function<MSGT, PacketContext> handler) {
        return (msg, ctx) -> {
            ctx.get().enqueueWork(() -> {
                LogicalSide curSide = ctx.get().getDirection().getReceptionSide().isServer() ? LogicalSide.SERVER : LogicalSide.CLIENT;
                ServerPlayer playerSender = ctx.get().getSender();

                handler.apply(msg).handlePacket(playerSender == null ? Minecraft.getInstance().player : playerSender, playerSender == null ? LogicalSidedProvider.CLIENTWORLD.get(curSide).filter(ClientLevel.class::isInstance).orElse(Minecraft.getInstance().level) : playerSender.serverLevel(), curSide.isServer() ? NetworkSide.C2S : NetworkSide.S2C);
            });

            ctx.get().setPacketHandled(true);
        };
    }
}
