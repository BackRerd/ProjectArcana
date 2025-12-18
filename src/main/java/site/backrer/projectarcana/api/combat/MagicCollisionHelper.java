package site.backrer.projectarcana.api.combat;

/**
 * Logic for resolving collisions between two magic spells.
 */
public class MagicCollisionHelper {

    /**
     * Resolves a collision between two magic projectiles.
     * Logic:
     * 1. Compare powers.
     * 2. If power difference is small (within 20%), both destroy.
     * 3. If one is significantly stronger, it penetrates and loses power equivalent
     * to the weaker one.
     */
    public static void resolveCollision(IMagicProjectile p1, IMagicProjectile p2) {
        float power1 = p1.getMagicPower();
        float power2 = p2.getMagicPower();

        float diff = Math.abs(power1 - power2);
        float avg = (power1 + power2) / 2f;

        if (diff <= avg * 0.2f) {
            // Cancellation: both destroy
            p1.onMagicDestroyed();
            p2.onMagicDestroyed();
        } else if (power1 > power2) {
            // p1 penetrates
            p1.setMagicPower(power1 - power2);
            p2.onMagicDestroyed();
        } else {
            // p2 penetrates
            p2.setMagicPower(power2 - power1);
            p1.onMagicDestroyed();
        }
    }
}
