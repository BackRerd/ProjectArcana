package site.backrer.projectarcana.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import site.backrer.projectarcana.api.MagicAPI;
import site.backrer.projectarcana.capability.MagicStatsProvider;
import site.backrer.projectarcana.logic.AwakeningHandler;

public class MagicStatsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("magicinfo")
                .executes(MagicStatsCommand::execute));

        dispatcher.register(Commands.literal("magicinit")
                .requires(source -> source.hasPermission(2)) // Require OP level 2
                .executes(MagicStatsCommand::initPlayerAttributes));
    }

    private static int initPlayerAttributes(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            AwakeningHandler.resetPlayer(player);
            context.getSource().sendSuccess(() -> Component.literal("§a魔法属性已成功初始化！"), true);
            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("初始化属性失败: " + e.getMessage()));
            return 0;
        }
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();

            player.getCapability(MagicStatsProvider.MAGIC_STATS).ifPresent(cap -> {
                String archetype = cap.getArchetype();
                String elements = cap.getElements().toString();
                if (archetype.isEmpty())
                    archetype = "无";

                // --- 基础属性 ---
                double currentMana = MagicAPI.getCurrentMana(player);
                double maxMana = MagicAPI.getMaxMana(player);
                double manaRegen = MagicAPI.getManaRegen(player);
                double spellPower = MagicAPI.getSpellPower(player);
                double cdReduction = MagicAPI.getCooldownReduction(player) * 100.0;
                double currentStagger = MagicAPI.getCurrentStagger(player);
                double maxStagger = MagicAPI.getMaxStagger(player);

                player.sendSystemMessage(Component.literal("§6--- 魔法属性信息 (Magic Stats) ---"));
                player.sendSystemMessage(Component.literal("§e职业: §f" + archetype));
                player.sendSystemMessage(Component.literal("§e元素: §f" + elements));
                player.sendSystemMessage(Component.literal("§b魔力: §f" + String.format("%.1f", currentMana) + " / "
                        + String.format("%.1f", maxMana) + " (恢复: " + String.format("%.1f", manaRegen) + "/s)"));
                player.sendSystemMessage(Component.literal("§d法术强度: §f" + String.format("%.1f", spellPower)));
                player.sendSystemMessage(Component.literal("§3冷却缩减: §f" + String.format("%.1f %%", cdReduction)));
                player.sendSystemMessage(Component.literal("§c硬直值: §f" + String.format("%.1f", currentStagger)
                        + " / " + String.format("%.1f", maxStagger)));

                // --- 属性伤害 ---
                player.sendSystemMessage(Component.literal("§6--- 属性伤害 ---"));
                player.sendSystemMessage(
                        Component.literal("§7金: §f" + String.format("%.1f", MagicAPI.getMetalDamage(player)) +
                                "  §2木: §f" + String.format("%.1f", MagicAPI.getWoodDamage(player)) +
                                "  §1水: §f" + String.format("%.1f", MagicAPI.getWaterDamage(player)) +
                                "  §c火: §f" + String.format("%.1f", MagicAPI.getFireDamage(player))));
                player.sendSystemMessage(
                        Component.literal("§e光: §f" + String.format("%.1f", MagicAPI.getLightDamage(player)) +
                                "  §8暗: §f" + String.format("%.1f", MagicAPI.getDarkDamage(player)) +
                                "  §f风: §f" + String.format("%.1f", MagicAPI.getWindDamage(player)) +
                                "  §b冰: §f" + String.format("%.1f", MagicAPI.getIceDamage(player))));

                // --- 防御属性 ---
                player.sendSystemMessage(Component.literal("§6--- 防御属性 ---"));
                player.sendSystemMessage(
                        Component.literal("§9魔法韧性: §f" + String.format("%.1f", MagicAPI.getMagicResilience(player))));
                player.sendSystemMessage(
                        Component.literal("§5魔法抗性: §f" + String.format("%.1f", MagicAPI.getMagicResistance(player))));
            });

            return 1;
        } catch (Exception e) {
            context.getSource().sendFailure(Component.literal("获取魔法属性失败: " + e.getMessage()));
            return 0;
        }
    }
}
