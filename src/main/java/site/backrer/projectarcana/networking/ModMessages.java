package site.backrer.projectarcana.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import site.backrer.projectarcana.Projectarcana;
import site.backrer.projectarcana.networking.packet.PacketSyncMagicStats;
import site.backrer.projectarcana.networking.packet.PacketSpawnMagicEffect;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Projectarcana.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(PacketSyncMagicStats.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncMagicStats::new)
                .encoder(PacketSyncMagicStats::toBytes)
                .consumerMainThread(PacketSyncMagicStats::handle)
                .add();

        net.messageBuilder(PacketSpawnMagicEffect.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSpawnMagicEffect::new)
                .encoder(PacketSpawnMagicEffect::toBytes)
                .consumerMainThread(PacketSpawnMagicEffect::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
