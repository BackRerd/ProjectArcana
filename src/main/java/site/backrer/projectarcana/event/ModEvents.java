package site.backrer.projectarcana.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import site.backrer.projectarcana.Projectarcana;
import site.backrer.projectarcana.capability.IMagicStats;
import site.backrer.projectarcana.capability.MagicStats;
import site.backrer.projectarcana.capability.MagicStatsProvider;
import site.backrer.projectarcana.networking.ModMessages;
import site.backrer.projectarcana.networking.packet.PacketSyncMagicStats;
import site.backrer.projectarcana.registry.MagicAttributes;

@Mod.EventBusSubscriber(modid = Projectarcana.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(MagicStatsProvider.MAGIC_STATS).isPresent()) {
                event.addCapability(new ResourceLocation(Projectarcana.MODID, "properties"), new MagicStatsProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        site.backrer.projectarcana.api.MagicConfig config = site.backrer.projectarcana.data.MagicConfigDataLoader
                .getConfig();
        if (config != null && config.getDefaultAttributes() != null) {
            for (site.backrer.projectarcana.api.MagicConfig.AttributeConfig attrConfig : config
                    .getDefaultAttributes()) {
                net.minecraft.world.entity.ai.attributes.Attribute attribute = net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES
                        .getValue(new ResourceLocation(attrConfig.getAttribute()));
                if (attribute != null) {
                    net.minecraft.world.entity.ai.attributes.AttributeInstance instance = player
                            .getAttribute(attribute);
                    if (instance != null) {
                        instance.setBaseValue(attrConfig.getValue());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(MagicStatsProvider.MAGIC_STATS).ifPresent(oldStore -> {
                event.getEntity().getCapability(MagicStatsProvider.MAGIC_STATS).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(MagicStats.class);
    }

    @SubscribeEvent
    public static void onRegisterCommands(net.minecraftforge.event.RegisterCommandsEvent event) {
        site.backrer.projectarcana.command.MagicStatsCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            event.player.getCapability(MagicStatsProvider.MAGIC_STATS).ifPresent(magicStats -> {
                // Mana Regen Logic (Simple implementation, can be expanded)
                if (magicStats.getMana() < event.player.getAttributeValue(MagicAttributes.MAX_MANA.get())) {
                    // Regen rate per tick? Or per second?
                    // Attribute is "Regen Rate". Let's assume the attribute is "Mana per Second".
                    // So we add (Regen Rate / 20) per tick.
                    // Or if "Regen Rate" is a multiplier? Let's assume flat value.
                    double regenRate = event.player.getAttributeValue(MagicAttributes.MANA_REGEN.get());
                    magicStats.addMana((float) (regenRate / 20.0));
                }

                // Clamp Mana to Max Mana (if addMana didn't handle max)
                // MagicStats.addMana doesn't check MAX mana because it doesn't know about
                // attributes.
                // So we clamp here.
                float maxMana = (float) event.player.getAttributeValue(MagicAttributes.MAX_MANA.get());
                if (magicStats.getMana() > maxMana) {
                    magicStats.setMana(maxMana);
                }

                // Stagger Decay Logic? (Optional, assumed stagger decreases over time)
                if (magicStats.getStagger() > 0) {
                    magicStats.addStagger(-0.5f); // Example decay
                }

                // Sync to Client
                ModMessages.sendToPlayer(new PacketSyncMagicStats(magicStats.getMana(), magicStats.getStagger(),
                        magicStats.getShield(), magicStats.getArchetype(),
                        magicStats.getElements().stream().map(Enum::name).toList()),
                        (ServerPlayer) event.player);
            });
        }
    }
}
