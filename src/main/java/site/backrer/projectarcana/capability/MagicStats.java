package site.backrer.projectarcana.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import site.backrer.projectarcana.api.MagicElement;

import java.util.ArrayList;
import java.util.List;

public class MagicStats implements IMagicStats {
    private float mana;
    private float stagger;
    private String archetype = ""; // Default empty
    private List<MagicElement> elements = new ArrayList<>();

    @Override
    public float getMana() {
        return mana;
    }

    @Override
    public void setMana(float mana) {
        this.mana = mana;
        if (this.mana < 0)
            this.mana = 0;
    }

    @Override
    public void addMana(float mana) {
        this.mana += mana;
        if (this.mana < 0)
            this.mana = 0;
    }

    @Override
    public float getStagger() {
        return stagger;
    }

    @Override
    public void setStagger(float stagger) {
        this.stagger = stagger;
        if (this.stagger < 0)
            this.stagger = 0;
    }

    @Override
    public void addStagger(float stagger) {
        this.stagger += stagger;
        if (this.stagger < 0)
            this.stagger = 0;
    }

    @Override
    public String getArchetype() {
        return archetype;
    }

    @Override
    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    @Override
    public List<MagicElement> getElements() {
        return elements;
    }

    @Override
    public void setElements(List<MagicElement> elements) {
        this.elements = elements;
    }

    @Override
    public void copyFrom(IMagicStats source) {
        this.mana = source.getMana();
        this.stagger = source.getStagger();
        this.archetype = source.getArchetype();
        this.elements = new ArrayList<>(source.getElements());
    }

    @Override
    public void saveNBTData(CompoundTag nbt) {
        nbt.putFloat("CurrentMana", mana);
        nbt.putFloat("CurrentStagger", stagger);
        nbt.putString("Archetype", archetype);

        ListTag elementsTag = new ListTag();
        for (MagicElement element : elements) {
            elementsTag.add(StringTag.valueOf(element.name()));
        }
        nbt.put("Elements", elementsTag);
    }

    @Override
    public void loadNBTData(CompoundTag nbt) {
        mana = nbt.getFloat("CurrentMana");
        stagger = nbt.getFloat("CurrentStagger");
        if (nbt.contains("Archetype")) {
            archetype = nbt.getString("Archetype");
        }

        elements.clear();
        if (nbt.contains("Elements")) {
            ListTag elementsTag = nbt.getList("Elements", Tag.TAG_STRING);
            for (int i = 0; i < elementsTag.size(); i++) {
                try {
                    elements.add(MagicElement.valueOf(elementsTag.getString(i)));
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
    }
}
