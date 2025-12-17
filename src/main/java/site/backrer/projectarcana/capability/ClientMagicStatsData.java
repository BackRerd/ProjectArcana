package site.backrer.projectarcana.capability;

import java.util.Collections;
import java.util.List;

public class ClientMagicStatsData {
    private static float mana;
    private static float stagger;
    private static String archetype;
    private static List<String> elements = Collections.emptyList();

    public static void set(float mana, float stagger, String archetype, List<String> elements) {
        ClientMagicStatsData.mana = mana;
        ClientMagicStatsData.stagger = stagger;
        ClientMagicStatsData.archetype = archetype;
        ClientMagicStatsData.elements = elements;
    }

    public static float getMana() {
        return mana;
    }

    public static float getStagger() {
        return stagger;
    }

    public static String getArchetype() {
        return archetype;
    }

    public static List<String> getElements() {
        return elements;
    }
}
