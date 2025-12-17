package site.backrer.projectarcana.capability;

import net.minecraft.nbt.CompoundTag;

public interface IMagicStats {
    float getMana();

    void setMana(float mana);

    void addMana(float mana);

    float getStagger();

    void setStagger(float stagger);

    void addStagger(float stagger);

    void copyFrom(IMagicStats source);

    void saveNBTData(CompoundTag nbt);

    void loadNBTData(CompoundTag nbt);

    String getArchetype();

    void setArchetype(String archetype);

    java.util.List<site.backrer.projectarcana.api.MagicElement> getElements();

    void setElements(java.util.List<site.backrer.projectarcana.api.MagicElement> elements);
}
