package io.github.chaosawakens.api.network;

import io.github.chaosawakens.CAConstants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base class holding all relevant packet data used for networking.
 *
 * @param packetId The {@linkplain ResourceLocation identifier} of this packet. Primarily used in Fabric for packet registration.
 * @param packetClass The class representing the rest of this packet's data. Can be any custom object that stores the required packet handler methods (encoding, decoding, handling).
 * @param packetEncoder The encoding method used to encode this packet's data into a {@link FriendlyByteBuf} on the initial side.
 * @param packetDecoder The decoding method used to decode this packet's data from a {@link FriendlyByteBuf} on the target side.
 * @param packetHandler The interface responsible for both representing the target side and handling this packet's data on the target side, represented as a {@link Function} taking an input of the packet itself for access to
 *                      decoded packet data.
 * @param targetSide The side this packet should be sent to.
 *
 * @param <MSGT> The class representing/containing this packet's data.
 */
public record BasePacket<MSGT>(ResourceLocation packetId, Class<MSGT> packetClass, BiConsumer<MSGT, FriendlyByteBuf> packetEncoder, Function<FriendlyByteBuf, MSGT> packetDecoder, Function<MSGT, PacketContext> packetHandler, NetworkSide targetSide) {

    /**
     * Overloaded constructor with the {@code packetId} set to a formatted version of the packet class' name.
     *
     * @param packetClass The class representing the rest of this packet's data. Can be any custom object that stores the required packet handler methods (encoding, decoding, handling).
     * @param packetEncoder The encoding method used to encode this packet's data into a {@link FriendlyByteBuf} on the initial side.
     * @param packetDecoder The decoding method used to decode this packet's data from a {@link FriendlyByteBuf} on the target side.
     * @param packetHandler The interface responsible for both representing the target side and handling this packet's data on the target side, represented as a {@link Function} taking an input of the packet itself for access to
     *                      decoded packet data.
     * @param targetSide The side this packet should be sent to.
     */
    public BasePacket(Class<MSGT> packetClass, BiConsumer<MSGT, FriendlyByteBuf> packetEncoder, Function<FriendlyByteBuf, MSGT> packetDecoder, Function<MSGT, PacketContext> packetHandler, NetworkSide targetSide) {
        this(CAConstants.prefix(packetClass.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT).replace("_packet", "")), packetClass, packetEncoder, packetDecoder, packetHandler, targetSide);
    }
}
