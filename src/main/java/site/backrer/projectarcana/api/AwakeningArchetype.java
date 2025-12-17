package site.backrer.projectarcana.api;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;
import java.util.UUID;

public class AwakeningArchetype {
    private final double weight;
    private final int min_elements;
    private final int max_elements;
    private final boolean force_all_elements;
    private final boolean force_no_elements;
    private final List<ModifierData> modifiers;

    public AwakeningArchetype(double weight, int min_elements, int max_elements, boolean force_all_elements,
            boolean force_no_elements, List<ModifierData> modifiers) {
        this.weight = weight;
        this.min_elements = min_elements;
        this.max_elements = max_elements;
        this.force_all_elements = force_all_elements;
        this.force_no_elements = force_no_elements;
        this.modifiers = modifiers;
    }

    public double getWeight() {
        return weight;
    }

    public int getMinElements() {
        return min_elements;
    }

    public int getMaxElements() {
        return max_elements;
    }

    public boolean isForceAllElements() {
        return force_all_elements;
    }

    public boolean isForceNoElements() {
        return force_no_elements;
    }

    public List<ModifierData> getModifiers() {
        return modifiers;
    }

    public static class ModifierData {
        private final String attribute;
        private final double amount;
        private final String operation;
        private final String uuid;
        private final String name;

        public ModifierData(String attribute, double amount, String operation, String uuid, String name) {
            this.attribute = attribute;
            this.amount = amount;
            this.operation = operation;
            this.uuid = uuid;
            this.name = name;
        }

        public String getAttribute() {
            return attribute;
        }

        public double getAmount() {
            return amount;
        }

        public AttributeModifier.Operation getOperation() {
            return AttributeModifier.Operation.valueOf(operation);
        }

        public UUID getUuid() {
            return UUID.fromString(uuid);
        }

        public String getName() {
            return name;
        }
    }
}
