package site.backrer.projectarcana.api.damage;

import net.minecraft.world.entity.LivingEntity;
import site.backrer.projectarcana.api.MagicAPI;
import site.backrer.projectarcana.api.MagicElement;
import site.backrer.projectarcana.capability.MagicStatsProvider;

/**
 * Utility class for calculating and applying modular magic damage.
 */
public class MagicDamageHelper {

    public static void applyMagicDamage(LivingEntity attacker, LivingEntity victim, float baseDamage,
            MagicElement element) {
        float finalDamage = calculateFinalDamage(attacker, victim, baseDamage, element);

        // 1. Resolve Shield
        float remainingDamage = resolveShield(victim, finalDamage);

        // 2. Apply remaining damage to health
        if (remainingDamage > 0) {
            victim.hurt(ArcanaDamageSources.magicSpell(victim.level(), attacker), remainingDamage);
        }

        // 3. Apply Stagger (Stagger usually applies even if shielded, but can be
        // adjusted)
        applyStagger(victim, finalDamage);
    }

    private static float resolveShield(LivingEntity victim, float damage) {
        return victim.getCapability(MagicStatsProvider.MAGIC_STATS).map(stats -> {
            float shield = stats.getShield();
            if (shield <= 0)
                return damage;

            if (shield >= damage) {
                stats.addShield(-damage);
                return 0f;
            } else {
                stats.setShield(0);
                return damage - shield;
            }
        }).orElse(damage);
    }

    public static float calculateFinalDamage(LivingEntity attacker, LivingEntity victim, float baseDamage,
            MagicElement element) {
        // 1. Effective Damage (Base + Spell Power)
        float spellPower = MagicAPI.getSpellPower(attacker);
        float elementalBonus = 0;

        if (element != null) {
            elementalBonus = getElementalBonus(attacker, element);
        }

        float effectiveDamage = (baseDamage + spellPower) * (1 + elementalBonus);

        // 2. Damage Reduction (Resistance + Resilience)
        float resistance = MagicAPI.getMagicResistance(victim); // 1 point = 1% reduction
        float resilience = MagicAPI.getMagicResilience(victim); // Fixed reduction

        float resistanceFactor = Math.min(0.8f, resistance / 100f); // Cap at 80%
        float reducedDamage = effectiveDamage * (1 - resistanceFactor) - resilience;

        return Math.max(0.5f, reducedDamage);
    }

    private static float getElementalBonus(LivingEntity entity, MagicElement element) {
        switch (element) {
            case METAL:
                return MagicAPI.getMetalDamage(entity) / 100f;
            case WOOD:
                return MagicAPI.getWoodDamage(entity) / 100f;
            case WATER:
                return MagicAPI.getWaterDamage(entity) / 100f;
            case FIRE:
                return MagicAPI.getFireDamage(entity) / 100f;
            case LIGHT:
                return MagicAPI.getLightDamage(entity) / 100f;
            case DARK:
                return MagicAPI.getDarkDamage(entity) / 100f;
            case WIND:
                return MagicAPI.getWindDamage(entity) / 100f;
            case ICE:
                return MagicAPI.getIceDamage(entity) / 100f;
            default:
                return 0;
        }
    }

    private static void applyStagger(LivingEntity victim, float damage) {
        victim.getCapability(MagicStatsProvider.MAGIC_STATS).ifPresent(stats -> {
            float toughness = stats.getToughness(); // 1 point = 1% reduction
            float toughnessFactor = Math.min(0.9f, toughness / 100f);

            float staggerInflicted = damage * 1.5f * (1 - toughnessFactor);
            stats.addStagger(staggerInflicted);
        });
    }
}
