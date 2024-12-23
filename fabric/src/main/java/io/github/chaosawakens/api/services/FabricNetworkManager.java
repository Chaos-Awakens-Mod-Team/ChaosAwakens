package io.github.chaosawakens.api.services;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.FabricServerHooks;
import io.github.chaosawakens.api.asm.annotations.NetworkRegistrarEntry;
import io.github.chaosawakens.api.network.BasePacket;
import io.github.chaosawakens.api.network.NetworkSide;
import io.github.chaosawakens.api.platform.CAServices;
import io.github.chaosawakens.api.platform.services.INetworkManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;

public class FabricNetworkManager implements INetworkManager {
    private static final Object2ObjectLinkedOpenHashMap<Class<?>, BasePacket<?>> MAPPED_PACKETS = new Object2ObjectLinkedOpenHashMap<>();

    @Override
    public void setupNetworkHandler() {
        CAServices.PLATFORM.discoverAnnotatedClasses(NetworkRegistrarEntry.class);
    }

    @Override
    public <MSGT> BasePacket<MSGT> registerPacket(BasePacket<MSGT> packet) {
        if (packet.packetId() == null || !ResourceLocation.isValidResourceLocation(packet.packetId().toString())) {
            throw new IllegalArgumentException("Attempted to register packet with invalid packet id: " + (packet == null ? "null" : packet.packetId()));
        }

        NetworkSide targetSide = packet.targetSide();

        MAPPED_PACKETS.putIfAbsent(packet.packetClass(), packet);

        if (targetSide.equals(NetworkSide.C2S)) {
            ServerPlayNetworking.registerGlobalReceiver(packet.packetId(), ((targetServer, playerReceiver, serverPacketListener, buf, fabricPacketSender) -> {
                buf.readByte(); // Forge discriminator handling (monke see monke do)

                packet.packetHandler().apply(packet.packetDecoder().apply(buf)).handlePacket(playerReceiver, targetServer.getLevel(playerReceiver.level().dimension()), NetworkSide.C2S);
            }));
        } else if (targetSide.equals(NetworkSide.S2C)) {
            ClientPlayNetworking.registerGlobalReceiver(packet.packetId(), ((targetClient, clientPacketListener, buf, fabricPacketSender) -> {
                buf.readByte(); // Forge discriminator handling (monke see monke do)

                packet.packetHandler().apply(packet.packetDecoder().apply(buf)).handlePacket(targetClient.player, targetClient.level, NetworkSide.S2C);
            }));
        }
        return packet;
    }

    @Override
    public <MSGT> void sendToServer(MSGT c2sPacket) {
        BasePacket<MSGT> mappedPacketToSend = (BasePacket<MSGT>) MAPPED_PACKETS.get(c2sPacket.getClass());

        if (mappedPacketToSend != null) {
            if (mappedPacketToSend.targetSide() != NetworkSide.C2S) {
                CAConstants.LOGGER.warn("Attempted to send non-C2S packet ({}) to server!", mappedPacketToSend.packetId() == null ? mappedPacketToSend.packetClass().getTypeName() : mappedPacketToSend.packetId());
                return;
            }

            if (ClientPlayNetworking.canSend(mappedPacketToSend.packetId())) {
                FriendlyByteBuf encodedBuf = PacketByteBufs.create();

                encodedBuf.writeByte(0); // Forge discriminator handling (monke see monke do)
                mappedPacketToSend.packetEncoder().accept(c2sPacket, encodedBuf);

                ClientPlayNetworking.send(mappedPacketToSend.packetId(), encodedBuf);
            }
        } else CAConstants.LOGGER.warn("Attempted to send unknown packet ({}) to server! Ensure that the packet is registered!", c2sPacket.getClass().getTypeName());
    }

    @Override
    public <MSGT> void sendToClient(MSGT s2cPacket) {
        throw new UnsupportedOperationException("Attempted to send ambiguous S2C packet on Fabric! Use sendToClient(MSGT s2cPacket, ServerPlayer targetPlayer) instead.");
    }

    @Override
    public <MSGT> void sendToAllClients(MSGT s2cPacket) {
        for (ServerPlayer targetServerPlayer : PlayerLookup.all(FabricServerHooks.getCurrentServer())) sendToClient(s2cPacket, targetServerPlayer);
    }

    @Override
    public <MSGT> void sendToTrackingClients(MSGT s2cPacket, Entity trackedEntity, boolean ignoreSelf) {
        if (trackedEntity instanceof ServerPlayer trackedServerPlayer && !ignoreSelf) sendToClient(s2cPacket, trackedServerPlayer);

        for (ServerPlayer trackingServerPlayer : PlayerLookup.tracking(trackedEntity)) sendToClient(s2cPacket, trackingServerPlayer);
    }

    @Override
    public <MSGT> void sendToTrackingClients(MSGT s2cPacket, LevelChunk trackedChunk) {
        if (trackedChunk.getLevel() instanceof ServerLevel curServerLevel) {
            for (ServerPlayer trackingServerPlayer : PlayerLookup.tracking(curServerLevel, trackedChunk.getPos())) sendToClient(s2cPacket, trackingServerPlayer);
        }
    }

    @Override
    public <MSGT> void sendToClientsInDimension(MSGT s2cPacket, ResourceKey<Level> targetDim) {
        for (ServerPlayer targetServerPlayer : PlayerLookup.all(FabricServerHooks.getCurrentServer())) {
            if (targetServerPlayer.level().dimension().equals(targetDim)) sendToClient(s2cPacket, targetServerPlayer);
        }
    }

    @Override
    public <MSGT> void sendToClientsWithinRange(MSGT s2cPacket, ResourceKey<Level> targetDim, BlockPos originPos, double range) {
        for (ServerPlayer targetServerPlayer : PlayerLookup.around(FabricServerHooks.getCurrentServer().getLevel(targetDim), new Vec3(originPos.getX(), originPos.getY(), originPos.getZ()), range)) sendToClient(s2cPacket, targetServerPlayer);
    }

    @Override
    public <MSGT> void sendToClient(MSGT s2cPacket, ServerPlayer targetPlayer) {
        BasePacket<MSGT> mappedPacketToSend = (BasePacket<MSGT>) MAPPED_PACKETS.get(s2cPacket.getClass());

        if (mappedPacketToSend != null) {
            if (mappedPacketToSend.targetSide() != NetworkSide.S2C) {
                CAConstants.LOGGER.warn("Attempted to send non-S2C packet ({}) to client!", mappedPacketToSend.packetId() == null ? mappedPacketToSend.packetClass().getTypeName() : mappedPacketToSend.packetId());
                return;
            }

            if (ServerPlayNetworking.canSend(targetPlayer, mappedPacketToSend.packetId())) {
                FriendlyByteBuf encodedBuf = PacketByteBufs.create();

                encodedBuf.writeByte(0); // Forge discriminator handling (monke see monke do)
                mappedPacketToSend.packetEncoder().accept(s2cPacket, encodedBuf);

                ServerPlayNetworking.send(targetPlayer, mappedPacketToSend.packetId(), encodedBuf);
            }
        } else CAConstants.LOGGER.warn("Attempted to send unknown packet ({}) to client! Ensure that the packet is registered!", s2cPacket.getClass().getTypeName());
    }
}
