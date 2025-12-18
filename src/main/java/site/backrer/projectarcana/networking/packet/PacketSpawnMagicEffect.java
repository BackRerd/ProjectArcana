package site.backrer.projectarcana.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Packet sent from server to client to spawn particles for magic effects.
 */
public class PacketSpawnMagicEffect {
    private final double x, y, z;
    private final int particleId; // 0: Fire, 1: Ice, etc.
    private final int count;

    public PacketSpawnMagicEffect(double x, double y, double z, int particleId, int count) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.particleId = particleId;
        this.count = count;
    }

    public PacketSpawnMagicEffect(FriendlyByteBuf buffer) {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.particleId = buffer.readInt();
        this.count = buffer.readInt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeInt(particleId);
        buffer.writeInt(count);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Client-side logic
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                for (int i = 0; i < count; i++) {
                    double rx = x + (mc.level.random.nextFloat() - 0.5) * 0.5;
                    double ry = y + (mc.level.random.nextFloat() - 0.5) * 0.5;
                    double rz = z + (mc.level.random.nextFloat() - 0.5) * 0.5;

                    if (particleId == 0) { // FIRE
                        mc.level.addParticle(ParticleTypes.FLAME, rx, ry, rz, 0, 0.05, 0);
                    } else if (particleId == 1) { // ICE/WATER
                        mc.level.addParticle(ParticleTypes.SNOWFLAKE, rx, ry, rz, 0, 0.05, 0);
                    } else { // DEFAULT
                        mc.level.addParticle(ParticleTypes.ENCHANTED_HIT, rx, ry, rz, 0, 0.05, 0);
                    }
                }
            }
        });
        return true;
    }
}
