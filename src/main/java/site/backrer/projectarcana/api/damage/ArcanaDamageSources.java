package site.backrer.projectarcana.api.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import site.backrer.projectarcana.Projectarcana;

/**
 * Utility to create custom magic damage sources.
 */
public class ArcanaDamageSources {
    public static final ResourceKey<DamageType> MAGIC_SPELL = ResourceKey.create(Registries.DAMAGE_TYPE,
            new ResourceLocation(Projectarcana.MODID, "magic_spell"));

    public static DamageSource magicSpell(Level level, Entity attacker) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(MAGIC_SPELL), attacker);
    }
}
