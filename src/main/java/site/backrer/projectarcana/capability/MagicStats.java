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
    private float toughness;
    private float stiffness;
    private float shield;
    private java.util.Map<String, Integer> cooldowns = new java.util.HashMap<>();

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
    public float getToughness() {
        return toughness;
    }

    @Override
    public void setToughness(float toughness) {
        this.toughness = toughness;
    }

    @Override
    public float getStiffness() {
        return stiffness;
    }

    @Override
    public void setStiffness(float stiffness) {
        this.stiffness = stiffness;
    }

    @Override
    public float getShield() {
        return shield;
    }

    @Override
    public void setShield(float shield) {
        this.shield = shield;
        if (this.shield < 0)
            this.shield = 0;
    }

    @Override
    public void addShield(float shield) {
        this.shield += shield;
        if (this.shield < 0)
            this.shield = 0;
    }

    @Override
    public int getSpellCooldown(String spellId) {
        return cooldowns.getOrDefault(spellId, 0);
    }

    @Override
    public void setSpellCooldown(String spellId, int ticks) {
        cooldowns.put(spellId, ticks);
    }

    @Override
    public void tickCooldowns() {
        cooldowns.entrySet().removeIf(entry -> {
            int remaining = entry.getValue() - 1;
            if (remaining <= 0)
                return true;
            entry.setValue(remaining);
            return false;
        });
    }

    @Override
    public void copyFrom(IMagicStats source) {
        this.mana = source.getMana();
        this.stagger = source.getStagger();
        this.archetype = source.getArchetype();
        this.elements = new ArrayList<>(source.getElements());
        this.toughness = source.getToughness();
        this.stiffness = source.getStiffness();
        this.shield = source.getShield();
        // Cooldowns might or might not want to be copied on death/respawn
    }

    @Override
    public void saveNBTData(CompoundTag nbt) {
        nbt.putFloat("CurrentMana", mana);
        nbt.putFloat("CurrentStagger", stagger);
        nbt.putFloat("Toughness", toughness);
        nbt.putFloat("Stiffness", stiffness);
        nbt.putFloat("Shield", shield);
        nbt.putString("Archetype", archetype);

        CompoundTag cooldownsTag = new CompoundTag();
        cooldowns.forEach(cooldownsTag::putInt);
        nbt.put("Cooldowns", cooldownsTag);

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
        toughness = nbt.getFloat("Toughness");
        stiffness = nbt.getFloat("Stiffness");
        shield = nbt.getFloat("Shield");
        if (nbt.contains("Archetype")) {
            archetype = nbt.getString("Archetype");
        }

        cooldowns.clear();
        if (nbt.contains("Cooldowns")) {
            CompoundTag cooldownsTag = nbt.getCompound("Cooldowns");
            for (String key : cooldownsTag.getAllKeys()) {
                cooldowns.put(key, cooldownsTag.getInt(key));
            }
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
