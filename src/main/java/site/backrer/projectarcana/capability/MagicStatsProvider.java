package site.backrer.projectarcana.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MagicStatsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<IMagicStats> MAGIC_STATS = CapabilityManager.get(new CapabilityToken<IMagicStats>() {
    });

    private IMagicStats magicStats = null;
    private final LazyOptional<IMagicStats> optional = LazyOptional.of(this::createMagicStats);

    private IMagicStats createMagicStats() {
        if (this.magicStats == null) {
            this.magicStats = new MagicStats();
        }
        return this.magicStats;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == MAGIC_STATS) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createMagicStats().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createMagicStats().loadNBTData(nbt);
    }
}
