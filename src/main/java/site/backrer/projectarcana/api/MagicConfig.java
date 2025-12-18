package site.backrer.projectarcana.api;

import com.google.gson.Gson;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import site.backrer.projectarcana.Projectarcana;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.List;
import java.util.UUID;

public class MagicConfig {
    private static final Gson GSON = new Gson();
    private final List<AttributeConfig> default_attributes;

    public MagicConfig(List<AttributeConfig> default_attributes) {
        this.default_attributes = default_attributes;
    }

    public static MagicConfig loadDefaultConfig() {
        try {
            String path = "/data/" + Projectarcana.MODID + "/magic_config/global_attributes.json";
            InputStream stream = MagicConfig.class.getResourceAsStream(path);
            if (stream != null) {
                return GSON.fromJson(new InputStreamReader(stream), MagicConfig.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AttributeConfig getAttributeConfig(String attributeName) {
        if (default_attributes == null)
            return null;
        String search = attributeName.contains(":") ? attributeName : Projectarcana.MODID + ":" + attributeName;
        return default_attributes.stream()
                .filter(a -> a.getAttribute().equals(search))
                .findFirst()
                .orElse(null);
    }

    public List<AttributeConfig> getDefaultAttributes() {
        return default_attributes;
    }

    public void apply() {
        if (default_attributes == null)
            return;

        for (AttributeConfig attrConfig : default_attributes) {
            net.minecraft.resources.ResourceLocation rl = new net.minecraft.resources.ResourceLocation(
                    attrConfig.getAttribute());
            net.minecraft.world.entity.ai.attributes.Attribute attribute = net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES
                    .getValue(rl);

            if (attribute instanceof net.minecraft.world.entity.ai.attributes.RangedAttribute rangedAttribute) {
                attrConfig.apply(rangedAttribute);
            }
        }

        // Apply to all online players
        if (net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer() != null) {
            for (net.minecraft.server.level.ServerPlayer player : net.minecraftforge.server.ServerLifecycleHooks
                    .getCurrentServer().getPlayerList().getPlayers()) {
                applyToPlayer(player);
            }
        }
    }

    public void applyToPlayer(net.minecraft.world.entity.player.Player player) {
        if (default_attributes == null)
            return;
        for (AttributeConfig attrConfig : default_attributes) {
            net.minecraft.resources.ResourceLocation rl = new net.minecraft.resources.ResourceLocation(
                    attrConfig.getAttribute());
            net.minecraft.world.entity.ai.attributes.Attribute attribute = net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES
                    .getValue(rl);
            if (attribute != null) {
                net.minecraft.world.entity.ai.attributes.AttributeInstance instance = player.getAttribute(attribute);
                if (instance != null) {
                    instance.setBaseValue(attrConfig.getValue());
                }
            }
        }
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

        public void apply(net.minecraft.world.entity.ai.attributes.RangedAttribute attribute) {
            try {
                // Update Default Value
                java.lang.reflect.Field defaultField = net.minecraft.world.entity.ai.attributes.Attribute.class
                        .getDeclaredField("f_22295_"); // defaultValue
                defaultField.setAccessible(true);
                defaultField.setDouble(attribute, this.value);

                // Update Min Value
                java.lang.reflect.Field minField = net.minecraft.world.entity.ai.attributes.RangedAttribute.class
                        .getDeclaredField("f_22308_"); // minValue
                minField.setAccessible(true);
                minField.setDouble(attribute, this.min);

                // Update Max Value
                java.lang.reflect.Field maxField = net.minecraft.world.entity.ai.attributes.RangedAttribute.class
                        .getDeclaredField("f_22309_"); // maxValue
                maxField.setAccessible(true);
                maxField.setDouble(attribute, this.max);
            } catch (Exception e) {
                // MCP Fallback
                try {
                    java.lang.reflect.Field defaultField = net.minecraft.world.entity.ai.attributes.Attribute.class
                            .getDeclaredField("defaultValue");
                    defaultField.setAccessible(true);
                    defaultField.setDouble(attribute, this.value);

                    java.lang.reflect.Field minField = net.minecraft.world.entity.ai.attributes.RangedAttribute.class
                            .getDeclaredField("minValue");
                    minField.setAccessible(true);
                    minField.setDouble(attribute, this.min);

                    java.lang.reflect.Field maxField = net.minecraft.world.entity.ai.attributes.RangedAttribute.class
                            .getDeclaredField("maxValue");
                    maxField.setAccessible(true);
                    maxField.setDouble(attribute, this.max);
                } catch (Exception ex) {
                    System.err.println("Failed to synchronize attribute: " + this.attribute);
                    ex.printStackTrace();
                }
            }
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
