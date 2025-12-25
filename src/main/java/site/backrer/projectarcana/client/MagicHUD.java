package site.backrer.projectarcana.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import site.backrer.projectarcana.api.MagicAPI;
import site.backrer.projectarcana.api.MagicElement;

import java.util.List;
import java.util.Locale;

public class MagicHUD {
    public static final IGuiOverlay HUD_OVERLAY = MagicHUD::render;

    private static void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth,
            int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui)
            return;

        // Ultra-Compact Position: Top-Left
        int x = 8;
        int y = 8;

        // --- Data Retrieval ---
        float mana = MagicAPI.getCurrentMana(mc.player);
        float maxMana = MagicAPI.getMaxMana(mc.player);
        float stagger = MagicAPI.getCurrentStagger(mc.player);
        float maxStagger = MagicAPI.getMaxStagger(mc.player);
        float shield = MagicAPI.getCurrentShield(mc.player);
        String archetypeId = MagicAPI.getCurrentArchetype(mc.player);
        List<MagicElement> elements = MagicAPI.getAssignedElements(mc.player);

        float spellPower = MagicAPI.getSpellPower(mc.player);
        float cdr = MagicAPI.getCooldownReduction(mc.player) * 100f;

        // --- Localization ---
        String archText;
        if (archetypeId == null || archetypeId.isEmpty() || archetypeId.equals("无")) {
            archText = Component.translatable("hud.projectarcana.unawakened").getString();
        } else {
            String archKey = archetypeId;
            if (archKey.contains(":"))
                archKey = archKey.substring(archKey.indexOf(':') + 1);
            archText = Component.translatable("archetype.projectarcana." + archKey).getString();
        }

        String manaLabel = Component.translatable("hud.projectarcana.mana").getString();
        String staggerLabel = Component.translatable("hud.projectarcana.stagger").getString();
        String shieldLabel = Component.translatable("hud.projectarcana.shield").getString();
        String apLabel = Component.translatable("hud.projectarcana.ap").getString();
        String cdrLabel = Component.translatable("hud.projectarcana.cdr").getString();

        // --- Visual Config (Ultra-Compact) ---
        int barWidth = 70;
        int barHeight = 2;
        int spacing = 10;

        // --- Archetype & Elements (Merged Row) ---
        StringBuilder title = new StringBuilder("§6" + archText + " §r");
        for (MagicElement element : elements) {
            String elementChar = Component
                    .translatable("element.projectarcana." + element.name().toLowerCase(Locale.ROOT)).getString();
            title.append(getElementColor(element)).append(elementChar).append(" ");
        }
        renderShadowedString(guiGraphics, mc, title.toString(), x, y);
        y += spacing;

        // --- Mana Bar ---
        int manaColor = (mana / maxMana < 0.2f) ? 0xFFFFCC00 : 0xFF00AAFF;
        renderBar(guiGraphics, mc, x, y, barWidth, barHeight, mana / maxMana, manaColor,
                "§b" + manaLabel + ": " + (int) mana);
        y += spacing - 1;

        // --- Shield Bar ---
        if (shield > 0) {
            renderBar(guiGraphics, mc, x, y, barWidth, barHeight, 1.0f, 0xFFFFAA00,
                    "§e" + shieldLabel + ": " + (int) shield);
            y += spacing - 1;
        }

        // --- Stagger Bar ---
        if (maxStagger > 0 && stagger > 0) {
            renderBar(guiGraphics, mc, x, y, barWidth, barHeight, stagger / maxStagger, 0xFFFF5555,
                    "§c" + staggerLabel);
            y += spacing - 1;
        }

        // --- Attributes (Compact Row) ---
        String statsText = String.format("§f%s:§b%.0f §f%s:§3%.0f%%", apLabel, spellPower, cdrLabel, cdr);
        renderShadowedString(guiGraphics, mc, statsText, x, y);
    }

    private static void renderShadowedString(GuiGraphics graphics, Minecraft mc, String text, int x, int y) {
        if (mc.font == null)
            return;
        graphics.drawString(mc.font, text, x, y, 0xFFFFFF, true);
    }

    private static void renderBar(GuiGraphics graphics, Minecraft mc, int x, int y, int width, int height, float fill,
            int color, String label) {
        if (mc.font == null)
            return;
        graphics.fill(x, y, x + width, y + height, 0x44FFFFFF);
        int fillWidth = (int) (width * Math.min(1.0f, Math.max(0.0f, fill)));
        if (fillWidth > 0) {
            graphics.fill(x, y, x + fillWidth, y + height, color);
        }
        graphics.drawString(mc.font, label, x + width + 4, y - 3, 0xFFFFFF, true);
    }

    private static String getElementColor(MagicElement element) {
        switch (element) {
            case METAL:
                return "§7";
            case WOOD:
                return "§2";
            case WATER:
                return "§1";
            case FIRE:
                return "§c";
            case LIGHT:
                return "§e";
            case DARK:
                return "§8";
            case WIND:
                return "§f";
            case ICE:
                return "§b";
            default:
                return "§r";
        }
    }
}
