package site.backrer.projectarcana.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import site.backrer.projectarcana.capability.ClientMagicStatsData;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PacketSyncMagicStats {
    private final float mana;
    private final float stagger;
    private final float shield;
    private final String archetype;
    private final List<String> elements;

    public PacketSyncMagicStats(float mana, float stagger, float shield, String archetype, List<String> elements) {
        this.mana = mana;
        this.stagger = stagger;
        this.shield = shield;
        this.archetype = archetype;
        this.elements = elements;
    }

    public PacketSyncMagicStats(FriendlyByteBuf buf) {
        this.mana = buf.readFloat();
        this.stagger = buf.readFloat();
        this.shield = buf.readFloat();
        this.archetype = buf.readUtf();
        int size = buf.readInt();
        this.elements = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.elements.add(buf.readUtf());
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeFloat(mana);
        buf.writeFloat(stagger);
        buf.writeFloat(shield);
        buf.writeUtf(archetype != null ? archetype : "");
        buf.writeInt(elements.size());
        for (String element : elements) {
            buf.writeUtf(element);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT
            ClientMagicStatsData.set(mana, stagger, shield, archetype, elements);
        });
        return true;
    }
}
