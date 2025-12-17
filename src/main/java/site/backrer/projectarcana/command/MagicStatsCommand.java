package site.backrer.projectarcana.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import site.backrer.projectarcana.api.MagicAPI;
import site.backrer.projectarcana.capability.MagicStatsProvider;

public class MagicStatsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("magicinfo")
                .executes(MagicStatsCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();

            player.getCapability(MagicStatsProvider.MAGIC_STATS).ifPresent(cap -> {
                String archetype = cap.getArchetype();
                String elements = cap.getElements().toString(); // List default toString
                if (archetype.isEmpty())
                    archetype = "None";

                double currentMana = MagicAPI.getCurrentMana(player);
                double maxMana = MagicAPI.getMaxMana(player);
                double manaRegen = MagicAPI.getManaRegen(player);
                double spellPower = MagicAPI.getSpellPower(player);
                double currentStagger = MagicAPI.getCurrentStagger(player);
                double maxStagger = MagicAPI.getMaxStagger(player);

                player.sendSystemMessage(Component.literal("§6--- Magic Stats Info ---"));
                player.sendSystemMessage(Component.literal("§eArchetype: §f" + archetype));
                player.sendSystemMessage(Component.literal("§eElements: §f" + elements));
                player.sendSystemMessage(Component.literal("§bMana: §f" + String.format("%.1f", currentMana) + " / "
                        + String.format("%.1f", maxMana) + " (Regen: " + String.format("%.1f", manaRegen) + ")"));
                player.sendSystemMessage(Component.literal("§dSpell Power: §f" + String.format("%.1f", spellPower)));
                player.sendSystemMessage(Component.literal("§cStagger: §f" + String.format("%.1f", currentStagger)
                        + " / " + String.format("%.1f", maxStagger)));
            });

            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("Error retrieving magic stats: " + e.getMessage()));
            return 0;
        }
    }
}
