package site.backrer.projectarcana.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import site.backrer.projectarcana.Projectarcana;

@Mod.EventBusSubscriber(modid = Projectarcana.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MagicAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES,
            Projectarcana.MODID);

    // Base Default Values (Fallback)
    // These values are overridden by
    // data/projectarcana/magic_config/global_attributes.json
    public static final double BASE_MAX_MANA = 100.0;
    public static final double BASE_MANA_REGEN = 1.0;
    public static final double BASE_SPELL_POWER = 0.0;
    public static final double BASE_COOLDOWN_REDUCTION = 0.0;
    public static final double BASE_MAX_STAGGER = 100.0;

    // Existing Attributes
    public static final RegistryObject<Attribute> MAX_MANA = ATTRIBUTES.register("max_mana",
            () -> new RangedAttribute("attribute.projectarcana.max_mana", BASE_MAX_MANA, 0.0D,
                    1000000.0D).setSyncable(true));
    public static final RegistryObject<Attribute> MANA_REGEN = ATTRIBUTES.register("mana_regen",
            () -> new RangedAttribute("attribute.projectarcana.mana_regen", BASE_MANA_REGEN, 0.0D,
                    1024.0D).setSyncable(true));
    public static final RegistryObject<Attribute> SPELL_POWER = ATTRIBUTES.register("spell_power",
            () -> new RangedAttribute("attribute.projectarcana.spell_power", BASE_SPELL_POWER, 0.0D,
                    1000000.0D).setSyncable(true));
    public static final RegistryObject<Attribute> COOLDOWN_REDUCTION = ATTRIBUTES.register("cooldown_reduction",
            () -> new RangedAttribute("attribute.projectarcana.cooldown_reduction",
                    BASE_COOLDOWN_REDUCTION, 0.0D, 1.0D).setSyncable(true));
    public static final RegistryObject<Attribute> MAX_STAGGER = ATTRIBUTES.register("max_stagger",
            () -> new RangedAttribute("attribute.projectarcana.max_stagger", BASE_MAX_STAGGER, 0.0D,
                    10000.0D).setSyncable(true));

    // Elemental Damage Attributes
    public static final RegistryObject<Attribute> ELEMENT_METAL_DAMAGE = ATTRIBUTES.register("element_metal_damage",
            () -> new RangedAttribute("attribute.projectarcana.element_metal_damage",
                    0.0D, 0.0D, 1000000.0D).setSyncable(true));
    public static final RegistryObject<Attribute> ELEMENT_WOOD_DAMAGE = ATTRIBUTES.register("element_wood_damage",
            () -> new RangedAttribute("attribute.projectarcana.element_wood_damage",
                    0.0D, 0.0D, 1000000.0D).setSyncable(true));
    public static final RegistryObject<Attribute> ELEMENT_WATER_DAMAGE = ATTRIBUTES.register("element_water_damage",
            () -> new RangedAttribute("attribute.projectarcana.element_water_damage",
                    0.0D, 0.0D, 1000000.0D).setSyncable(true));
    public static final RegistryObject<Attribute> ELEMENT_FIRE_DAMAGE = ATTRIBUTES.register("element_fire_damage",
            () -> new RangedAttribute("attribute.projectarcana.element_fire_damage",
                    0.0D, 0.0D, 1000000.0D).setSyncable(true));
    public static final RegistryObject<Attribute> ELEMENT_LIGHT_DAMAGE = ATTRIBUTES.register("element_light_damage",
            () -> new RangedAttribute("attribute.projectarcana.element_light_damage",
                    0.0D, 0.0D, 1000000.0D).setSyncable(true));
    public static final RegistryObject<Attribute> ELEMENT_DARK_DAMAGE = ATTRIBUTES.register("element_dark_damage",
            () -> new RangedAttribute("attribute.projectarcana.element_dark_damage",
                    0.0D, 0.0D, 1000000.0D).setSyncable(true));
    public static final RegistryObject<Attribute> ELEMENT_WIND_DAMAGE = ATTRIBUTES.register("element_wind_damage",
            () -> new RangedAttribute("attribute.projectarcana.element_wind_damage",
                    0.0D, 0.0D, 1000000.0D).setSyncable(true));
    public static final RegistryObject<Attribute> ELEMENT_ICE_DAMAGE = ATTRIBUTES.register("element_ice_damage",
            () -> new RangedAttribute("attribute.projectarcana.element_ice_damage",
                    0.0D, 0.0D, 1000000.0D).setSyncable(true));

    // Defense Attributes
    public static final RegistryObject<Attribute> MAGIC_RESILIENCE = ATTRIBUTES.register("magic_resilience",
            () -> new RangedAttribute("attribute.projectarcana.magic_resilience", 0.0D,
                    0.0D, 1000000.0D).setSyncable(true));
    public static final RegistryObject<Attribute> MAGIC_RESISTANCE = ATTRIBUTES.register("magic_resistance",
            () -> new RangedAttribute("attribute.projectarcana.magic_resistance", 0.0D,
                    0.0D, 1000000.0D).setSyncable(true));

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, MAX_MANA.get());
        event.add(EntityType.PLAYER, MANA_REGEN.get());
        event.add(EntityType.PLAYER, SPELL_POWER.get());
        event.add(EntityType.PLAYER, COOLDOWN_REDUCTION.get());
        event.add(EntityType.PLAYER, MAX_STAGGER.get());

        // Elemental
        event.add(EntityType.PLAYER, ELEMENT_METAL_DAMAGE.get());
        event.add(EntityType.PLAYER, ELEMENT_WOOD_DAMAGE.get());
        event.add(EntityType.PLAYER, ELEMENT_WATER_DAMAGE.get());
        event.add(EntityType.PLAYER, ELEMENT_FIRE_DAMAGE.get());
        event.add(EntityType.PLAYER, ELEMENT_LIGHT_DAMAGE.get());
        event.add(EntityType.PLAYER, ELEMENT_DARK_DAMAGE.get());
        event.add(EntityType.PLAYER, ELEMENT_WIND_DAMAGE.get());
        event.add(EntityType.PLAYER, ELEMENT_ICE_DAMAGE.get());

        // Defense
        event.add(EntityType.PLAYER, MAGIC_RESILIENCE.get());
        event.add(EntityType.PLAYER, MAGIC_RESISTANCE.get());
    }
}
