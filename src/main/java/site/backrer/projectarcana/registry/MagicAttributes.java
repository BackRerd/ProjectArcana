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

        private static final site.backrer.projectarcana.api.MagicConfig BOOTSTRAP_CONFIG = site.backrer.projectarcana.api.MagicConfig
                        .loadDefaultConfig();

        private static RegistryObject<Attribute> registerRanged(String name, double fallbackValue, double fallbackMin,
                        double fallbackMax) {
                double val = fallbackValue;
                double min = fallbackMin;
                double max = fallbackMax;

                if (BOOTSTRAP_CONFIG != null) {
                        site.backrer.projectarcana.api.MagicConfig.AttributeConfig cfg = BOOTSTRAP_CONFIG
                                        .getAttributeConfig(name);
                        if (cfg != null) {
                                val = cfg.getValue();
                                min = cfg.getMin();
                                max = cfg.getMax();
                        }
                }

                final double finalVal = val;
                final double finalMin = min;
                final double finalMax = max;

                return ATTRIBUTES.register(name,
                                () -> new RangedAttribute("attribute." + Projectarcana.MODID + "." + name, finalVal,
                                                finalMin, finalMax).setSyncable(true));
        }

        // Base Attributes
        public static final RegistryObject<Attribute> MAX_MANA = registerRanged("max_mana", 100.0, 0.0, 1000000.0);
        public static final RegistryObject<Attribute> MANA_REGEN = registerRanged("mana_regen", 1.0, 0.0, 1024.0);
        public static final RegistryObject<Attribute> SPELL_POWER = registerRanged("spell_power", 0.0, 0.0, 1000000.0);
        public static final RegistryObject<Attribute> COOLDOWN_REDUCTION = registerRanged("cooldown_reduction", 0.0,
                        0.0, 1.0);
        public static final RegistryObject<Attribute> MAX_STAGGER = registerRanged("max_stagger", 100.0, 0.0, 10000.0);

        // Elemental Damage Attributes
        public static final RegistryObject<Attribute> ELEMENT_METAL_DAMAGE = registerRanged("element_metal_damage", 0.0,
                        0.0, 1000000.0);
        public static final RegistryObject<Attribute> ELEMENT_WOOD_DAMAGE = registerRanged("element_wood_damage", 0.0,
                        0.0, 1000000.0);
        public static final RegistryObject<Attribute> ELEMENT_WATER_DAMAGE = registerRanged("element_water_damage", 0.0,
                        0.0, 1000000.0);
        public static final RegistryObject<Attribute> ELEMENT_FIRE_DAMAGE = registerRanged("element_fire_damage", 0.0,
                        0.0, 1000000.0);
        public static final RegistryObject<Attribute> ELEMENT_LIGHT_DAMAGE = registerRanged("element_light_damage", 0.0,
                        0.0, 1000000.0);
        public static final RegistryObject<Attribute> ELEMENT_DARK_DAMAGE = registerRanged("element_dark_damage", 0.0,
                        0.0, 1000000.0);
        public static final RegistryObject<Attribute> ELEMENT_WIND_DAMAGE = registerRanged("element_wind_damage", 0.0,
                        0.0, 1000000.0);
        public static final RegistryObject<Attribute> ELEMENT_ICE_DAMAGE = registerRanged("element_ice_damage", 0.0,
                        0.0, 1000000.0);

        // Defense Attributes
        public static final RegistryObject<Attribute> MAGIC_RESILIENCE = registerRanged("magic_resilience", 0.0, 0.0,
                        1000000.0);
        public static final RegistryObject<Attribute> MAGIC_RESISTANCE = registerRanged("magic_resistance", 0.0, 0.0,
                        1000000.0);

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
