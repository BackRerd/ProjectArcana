package site.backrer.projectarcana.api;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;
import java.util.UUID;

public class MagicConfig {
    private final List<AttributeConfig> default_attributes;

    public MagicConfig(List<AttributeConfig> default_attributes) {
        this.default_attributes = default_attributes;
    }

    public List<AttributeConfig> getDefaultAttributes() {
        return default_attributes;
    }

    public static class AttributeConfig {
        private final String attribute;
        private final double value;
        private final double min;
        private final double max;

        public AttributeConfig(String attribute, double value, double min, double max) {
            this.attribute = attribute;
            this.value = value;
            this.min = min;
            this.max = max;
        }

        public String getAttribute() {
            return attribute;
        }

        public double getValue() {
            return value;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }
    }
}
