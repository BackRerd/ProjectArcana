package site.backrer.projectarcana.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import site.backrer.projectarcana.Projectarcana;

public class ModItems {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
                        Projectarcana.MODID);

        public static final RegistryObject<Item> METAL_CRYSTAL = ITEMS.register("metal_crystal",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> WOOD_CRYSTAL = ITEMS.register("wood_crystal",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> WATER_CRYSTAL = ITEMS.register("water_crystal",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> FIRE_CRYSTAL = ITEMS.register("fire_crystal",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> LIGHT_CRYSTAL = ITEMS.register("light_crystal",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> DARK_CRYSTAL = ITEMS.register("dark_crystal",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> WIND_CRYSTAL = ITEMS.register("wind_crystal",
                        () -> new Item(new Item.Properties()));
        public static final RegistryObject<Item> ICE_CRYSTAL = ITEMS.register("ice_crystal",
                        () -> new Item(new Item.Properties()));
}
