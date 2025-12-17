package site.backrer.projectarcana.logic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import site.backrer.projectarcana.api.AwakeningArchetype;
import site.backrer.projectarcana.api.MagicElement;
import site.backrer.projectarcana.data.AwakeningDataLoader;

import java.util.*;

/**
 * Handles the logic for a player's magical awakening.
 * Includes random determination of Archetype, Element assignments, and
 * Attribute modifications.
 * Now data-driven via AwakeningDataLoader.
 */
public class AwakeningHandler {

    /**
     * Awakens the player, assigning an archetype, elements, and applying stat
     * modifiers.
     * Should be called when the player first enters the game or triggers the
     * awakening event.
     *
     * @param player The player to awaken.
     */
    public static void awakenPlayer(Player player) {
        Random random = new Random();

        // 1. Determine Archetype from Data Pack
        Map.Entry<ResourceLocation, AwakeningArchetype> entry = AwakeningDataLoader.pickRandomArchetype();
        if (entry == null) {
            // Safety check
            return;
        }

        ResourceLocation archetypeId = entry.getKey();
        AwakeningArchetype archetypeValue = entry.getValue();

        // 2. Determine Elements based on Archetype Data
        List<MagicElement> elements = new ArrayList<>();
        List<MagicElement> allElements = new ArrayList<>(Arrays.asList(MagicElement.values()));
        Collections.shuffle(allElements, random); // Shuffle to pick randomly

        int elementCount = 0;
        if (archetypeValue.isForceNoElements()) {
            elementCount = 0;
        } else if (archetypeValue.isForceAllElements()) {
            elementCount = 8;
        } else {
            int min = archetypeValue.getMinElements();
            int max = archetypeValue.getMaxElements();
            elementCount = min + random.nextInt(max - min + 1);
        }

        for (int i = 0; i < elementCount && i < allElements.size(); i++) {
            elements.add(allElements.get(i));
        }

        // Save 'archetypeId' and 'elements' to Player Capability
        player.getCapability(site.backrer.projectarcana.capability.MagicStatsProvider.MAGIC_STATS).ifPresent(cap -> {
            cap.setArchetype(archetypeId.toString());
            cap.setElements(new ArrayList<>(elements));
        });

        // System.out.println("Awakened " + player.getName().getString() + " as " +
        // archetypeId + " with elements: " + elements);

        // 3. Apply Attribute Modifiers
        applyArchetypeAttributes(player, archetypeValue);
    }

    /**
     * Applies the permanent attribute modifiers based on the archetype.
     */
    private static void applyArchetypeAttributes(Player player, AwakeningArchetype archetype) {
        if (archetype.getModifiers() == null)
            return;

        for (AwakeningArchetype.ModifierData modData : archetype.getModifiers()) {
            net.minecraft.world.entity.ai.attributes.Attribute attribute = ForgeRegistries.ATTRIBUTES
                    .getValue(new ResourceLocation(modData.getAttribute()));
            if (attribute != null) {
                applyAttribute(player, attribute, modData.getUuid(), modData.getName(), modData.getAmount(),
                        modData.getOperation());
            } else {
                // Log warning: attribute not found
                System.err.println("Attribute not found: " + modData.getAttribute());
            }
        }
    }

    private static void applyAttribute(Player player, net.minecraft.world.entity.ai.attributes.Attribute attribute,
            UUID uuid, String name, double value, AttributeModifier.Operation operation) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            // Remove if exists to avoid duplication
            instance.removeModifier(uuid);
            // Add permanent modifier
            instance.addPermanentModifier(new AttributeModifier(uuid, name, value, operation));
        }
    }
}
