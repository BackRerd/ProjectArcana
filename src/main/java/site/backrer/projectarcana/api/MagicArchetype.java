package site.backrer.projectarcana.api;

/**
 * Represents the "constitution" or "archetype" of a player's magic potential.
 * Determines the number of elements they can control and their base stats.
 */
public enum MagicArchetype {
    /**
     * Common (75%).
     * Balanced stats. Can control 1-2 elements.
     */
    ADEPT,

    /**
     * Rare (19%).
     * High Mana, Lower Health. Can control 3-5 elements.
     */
    SAGE,

    /**
     * Legendary (1%).
     * Superior stats across the board. Controls all 8 elements.
     */
    AVATAR,

    /**
     * Special (5%).
     * No Magic, but extreme physical resilience. Controls 0 elements.
     */
    NULL
}
