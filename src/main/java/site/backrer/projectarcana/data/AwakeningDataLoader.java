package site.backrer.projectarcana.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import site.backrer.projectarcana.api.AwakeningArchetype;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AwakeningDataLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final Map<ResourceLocation, AwakeningArchetype> ARCHETYPES = new HashMap<>();

    public AwakeningDataLoader() {
        super(GSON, "awakening_archetypes");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager,
            ProfilerFiller profiler) {
        ARCHETYPES.clear();
        object.forEach((key, json) -> {
            try {
                AwakeningArchetype archetype = GSON.fromJson(json, AwakeningArchetype.class);
                ARCHETYPES.put(key, archetype);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("Loaded " + ARCHETYPES.size() + " awakening archetypes.");
    }

    public static Map<ResourceLocation, AwakeningArchetype> getArchetypes() {
        return ARCHETYPES;
    }

    public static Map.Entry<ResourceLocation, AwakeningArchetype> pickRandomArchetype() {
        double totalWeight = ARCHETYPES.values().stream().mapToDouble(AwakeningArchetype::getWeight).sum();
        double randomValue = new Random().nextDouble() * totalWeight;
        double currentSum = 0;

        for (Map.Entry<ResourceLocation, AwakeningArchetype> entry : ARCHETYPES.entrySet()) {
            currentSum += entry.getValue().getWeight();
            if (currentSum >= randomValue) {
                return entry;
            }
        }
        // Fallback
        return ARCHETYPES.entrySet().iterator().next();
    }
}
