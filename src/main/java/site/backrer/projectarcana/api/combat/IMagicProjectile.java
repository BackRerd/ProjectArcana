package site.backrer.projectarcana.api.combat;

import site.backrer.projectarcana.api.MagicElement;

import java.util.Optional;

/**
 * Interface for magic projectiles or area effects that can collide with each
 * other.
 */
public interface IMagicProjectile {
    /**
     * @return Current magic power of the projectile.
     */
    float getMagicPower();

    /**
     * Sets the magic power, used when partially cancelled.
     */
    void setMagicPower(float power);

    /**
     * @return The element of this projectile.
     */
    Optional<MagicElement> getMagicElement();

    /**
     * Called when the projectile is destroyed by a collision.
     */
    void onMagicDestroyed();
}
