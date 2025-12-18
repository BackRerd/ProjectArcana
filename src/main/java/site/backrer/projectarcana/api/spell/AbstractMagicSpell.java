package site.backrer.projectarcana.api.spell;

import net.minecraft.world.entity.LivingEntity;
import site.backrer.projectarcana.api.MagicAPI;
import site.backrer.projectarcana.api.MagicElement;
import site.backrer.projectarcana.api.damage.MagicDamageHelper;
import site.backrer.projectarcana.capability.MagicStatsProvider;

/**
 * Base implementation of IMagicSpell that handles common logic like mana
 * consumption and cooldowns.
 */
public abstract class AbstractMagicSpell implements IMagicSpell {

    @Override
    public void cast(LivingEntity caster) {
        if (canCast(caster)) {
            consumeResources(caster);
            applyCooldown(caster);
            onCast(caster);
        }
    }

    /**
     * Checks if the caster has enough mana and the spell is not on cooldown.
     */
    protected boolean canCast(LivingEntity caster) {
        float currentMana = MagicAPI.getCurrentMana(caster);
        if (currentMana < getManaCost()) {
            return false;
        }

        // TODO: Check cooldown via Capability once implemented
        return true;
    }

    /**
     * Deducts mana from the caster.
     */
    protected void consumeResources(LivingEntity caster) {
        if (!caster.level().isClientSide) {
            caster.getCapability(MagicStatsProvider.MAGIC_STATS).ifPresent(stats -> {
                stats.addMana(-getManaCost());
            });
        }
    }

    /**
     * Starts the spell's cooldown.
     */
    protected void applyCooldown(LivingEntity caster) {
        if (!caster.level().isClientSide) {
            // TODO: Apply cooldown to Capability once implemented
        }
    }

    /**
     * Helper to apply damage using the modular system.
     */
    protected void applyDamage(LivingEntity caster, LivingEntity target, float baseDamage) {
        MagicDamageHelper.applyMagicDamage(caster, target, baseDamage, getElement().orElse(null));
    }

    /**
     * Actual spell effect logic to be implemented by subclasses.
     */
    protected abstract void onCast(LivingEntity caster);
}
