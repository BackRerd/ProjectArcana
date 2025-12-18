package site.backrer.projectarcana.registry;

import site.backrer.projectarcana.api.spell.IMagicSpell;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry to manage magic spell instances.
 */
public class SpellRegistry {
    private static final Map<String, IMagicSpell> SPELLS = new HashMap<>();

    public static void register(IMagicSpell spell) {
        SPELLS.put(spell.getRegistryName(), spell);
    }

    public static IMagicSpell get(String id) {
        return SPELLS.get(id);
    }

    public static Map<String, IMagicSpell> getSpells() {
        return SPELLS;
    }
}
