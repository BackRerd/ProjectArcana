package site.backrer.projectarcana.api.spell;

import net.minecraft.world.entity.LivingEntity;
import site.backrer.projectarcana.api.MagicElement;

import java.util.Optional;

/**
 * Special strategy for handling elemental reactions.
 */
public abstract class ComboSpell extends AbstractMagicSpell {
    private final MagicElement elementA;
    private final MagicElement elementB;

    public ComboSpell(MagicElement elementA, MagicElement elementB) {
        this.elementA = elementA;
        this.elementB = elementB;
    }

    @Override
    public Optional<MagicElement> getElement() {
        // Combos might represent a new state, or just the combination
        return Optional.empty();
    }

    @Override
    protected void onCast(LivingEntity caster) {
        triggerReaction(caster, elementA, elementB);
    }

    /**
     * Defines what happens when these two elements react.
     */
    protected abstract void triggerReaction(LivingEntity caster, MagicElement a, MagicElement b);
}
