package site.backrer.projectarcana.api.spell;

import net.minecraft.world.entity.LivingEntity;
import site.backrer.projectarcana.api.MagicElement;

import java.util.Optional;

/**
 * Core contract for all magic spells in Project Arcana.
 */
public interface IMagicSpell {
    /**
     * Executes the spell logic.
     * 
     * @param caster The entity casting the spell.
     */
    void cast(LivingEntity caster);

    /**
     * @return Mana cost of the spell.
     */
    float getManaCost();

    /**
     * @return Cooldown in ticks.
     */
    int getCooldownTicks();

    /**
     * @return The element of the spell, if any.
     */
    Optional<MagicElement> getElement();

    /**
     * Unique ID for the spell, used for registry and cooldowns.
     */
    String getRegistryName();
}
