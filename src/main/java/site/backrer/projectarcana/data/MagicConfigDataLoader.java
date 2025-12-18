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
                        config.apply();
                    }
                    System.out.println("Loaded Global Magic Config.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static MagicConfig getConfig() {
        return config;
    }
}
