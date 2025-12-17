package site.backrer.projectarcana.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import site.backrer.projectarcana.api.MagicConfig;

import java.util.Map;

public class MagicConfigDataLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static MagicConfig config;

    public MagicConfigDataLoader() {
        super(GSON, "magic_config");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager,
            ProfilerFiller profiler) {
        object.forEach((key, json) -> {
            if (key.getPath().equals("global_attributes")) {
                try {
                    config = GSON.fromJson(json, MagicConfig.class);
                    if (config != null && config.getDefaultAttributes() != null) {
                        applyAttributes(config);
                    }
                    System.out.println("Loaded Global Magic Config.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void applyAttributes(MagicConfig config) {
        for (MagicConfig.AttributeConfig attrConfig : config.getDefaultAttributes()) {
            net.minecraft.world.entity.ai.attributes.Attribute attribute = net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES
                    .getValue(new ResourceLocation(attrConfig.getAttribute()));
            if (attribute instanceof net.minecraft.world.entity.ai.attributes.RangedAttribute rangedAttribute) {
                // Use reflection to set min and max
                try {
                    java.lang.reflect.Field minField = net.minecraft.world.entity.ai.attributes.RangedAttribute.class
                            .getDeclaredField("f_22308_"); // minValue
                    minField.setAccessible(true);
                    minField.setDouble(rangedAttribute, attrConfig.getMin());

                    java.lang.reflect.Field maxField = net.minecraft.world.entity.ai.attributes.RangedAttribute.class
                            .getDeclaredField("f_22309_"); // maxValue
                    maxField.setAccessible(true);
                    maxField.setDouble(rangedAttribute, attrConfig.getMax());
                } catch (Exception e) {
                    // Try MCP names if obfuscated names fail (dev env fallback)
                    try {
                        java.lang.reflect.Field minField = net.minecraft.world.entity.ai.attributes.RangedAttribute.class
                                .getDeclaredField("minValue");
                        minField.setAccessible(true);
                        minField.setDouble(rangedAttribute, attrConfig.getMin());

                        java.lang.reflect.Field maxField = net.minecraft.world.entity.ai.attributes.RangedAttribute.class
                                .getDeclaredField("maxValue");
                        maxField.setAccessible(true);
                        maxField.setDouble(rangedAttribute, attrConfig.getMax());
                    } catch (Exception ex) {
                        System.err.println("Failed to set min/max for attribute " + attrConfig.getAttribute());
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static MagicConfig getConfig() {
        return config;
    }
}
