package site.backrer.projectarcana.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import site.backrer.projectarcana.Projectarcana;

public class ModCreativeModeTabs {
        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
                        .create(Registries.CREATIVE_MODE_TAB, Projectarcana.MODID);

        public static final RegistryObject<CreativeModeTab> PROJECT_ARCANA_TAB = CREATIVE_MODE_TABS.register(
                        "projectarcana_tab",
                        () -> CreativeModeTab.builder()
                                        .icon(() -> new ItemStack(ModItems.FIRE_CRYSTAL.get()))
                                        .title(Component.translatable("creativetab.projectarcana_tab"))
                                        .displayItems((displayParameters, output) -> {
                                                output.accept(ModItems.METAL_CRYSTAL.get());
                                                output.accept(ModItems.WOOD_CRYSTAL.get());
                                                output.accept(ModItems.WATER_CRYSTAL.get());
                                                output.accept(ModItems.FIRE_CRYSTAL.get());
                                                output.accept(ModItems.LIGHT_CRYSTAL.get());
                                                output.accept(ModItems.DARK_CRYSTAL.get());
                                                output.accept(ModItems.WIND_CRYSTAL.get());
                                                output.accept(ModItems.ICE_CRYSTAL.get());
                                        })
                                        .build());
}
