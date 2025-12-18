package site.backrer.projectarcana.api.spell;

import net.minecraft.world.entity.LivingEntity;
import site.backrer.projectarcana.api.MagicElement;

import java.util.Optional;

/**
 * Strategy for spells tied to a specific element.
 */
public abstract class ElementalSpell extends AbstractMagicSpell {
    private final MagicElement element;

    public ElementalSpell(MagicElement element) {
        this.element = element;
    }

    @Override
    public Optional<MagicElement> getElement() {
        return Optional.of(element);
    }

    @Override
    protected void onCast(LivingEntity caster) {
        applyElementalEffect(caster);
    }

    /**
     * Specific elemental logic (e.g., Fire adds burn, Ice adds slow).
     */
    protected abstract void applyElementalEffect(LivingEntity caster);
}
