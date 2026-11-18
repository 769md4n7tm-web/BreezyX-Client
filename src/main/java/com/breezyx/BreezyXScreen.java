
package com.breezyx;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class BreezyXScreen extends Screen {

    private static final int CYAN = 0xFF00DFFF;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int MUTED = 0xFF9BAFC3;
    private static final int PANEL = 0xE9101D2B;
    private static final int PANEL_HEADER = 0xF51A2B3D;
    private static final int OFF = 0xFF526477;
    private static final int ON = 0xFF08BDEB;

    private float scale;
    private int baseX;
    private int baseY;

    private boolean combatOpen = true;
    private boolean crystalOpen = true;
    private boolean renderOpen = true;
    private boolean worldOpen = true;
    private boolean playerOpen = true;

    private boolean searchFocused = false;
    private String searchText = "";

    private int renderX, renderY, renderW;
    private int renderRowHeight;

    public BreezyXScreen() {
        super(Component.literal("Breezy X"));
    }

    private int s(float value) {
        return Math.round(value * scale);
    }

    private void panel(GuiGraphics g, int x, int y, int w, int h) {
        g.fill(x - 2, y - 2, x + w + 2, y + h + 2,
                0x5000CFFF);
        g.fill(x, y, x + w, y + h, PANEL);
        g.fill(x, y, x + w, y + s(2), CYAN);
        g.fill(x, y, x + s(2), y + h, 0xFF087B9D);
        g.fill(x + w - s(2), y, x + w, y + h,
                0xFF087B9D);
        g.fill(x, y + h - s(2), x + w, y + h,
                0xFF087B9D);
    }

    private void header(GuiGraphics g, String name,
                        int x, int y, int w) {
        g.fill(x, y, x + w, y + s(34), PANEL_HEADER);
        g.drawString(font, name, x + s(10), y + s(11), WHITE);
        g.drawString(font, "⌄",
                x + w - s(18), y + s(10), CYAN);
    }

    private void row(GuiGraphics g, String name,
                     int x, int y, int w, boolean enabled) {
        g.drawString(font, name, x + s(9), y + s(7), WHITE);

        int toggleX = x + w - s(30);
        int toggleY = y + s(5);

        g.fill(toggleX, toggleY,
                toggleX + s(23), toggleY + s(13),
                enabled ? ON : OFF);

        int knobX = enabled ? toggleX + s(12) : toggleX + s(2);

        g.fill(knobX, toggleY + s(2),
                knobX + s(9), toggleY + s(9), WHITE);
    }

    private void section(GuiGraphics g, String name,
                         int x, int y, int w) {
        g.fill(x, y, x + w, y + s(25), 0xB51B3041);
        g.drawString(font, name, x + s(9), y + s(7), CYAN);
        g.drawString(font, "⌄", x + w - s(18), y + s(7), CYAN);
    }

    @Override
    public void render(GuiGraphics g, int mouseX,
                       int mouseY, float partialTick) {

        scale = Math.min(width / 900.0F, height / 500.0F);
        scale = Math.max(0.48F, Math.min(scale, 1.0F));

        baseX = (width - s(900)) / 2;
        baseY = Math.max(s(8), (height - s(500)) / 2);

        // Keep the Minecraft world visible.
        g.fill(0, 0, width, height, 0x28000000);

        // Logo
        g.drawString(font, "⚡", baseX + s(16),
                baseY + s(17), CYAN);
        g.drawString(font, "Breezy X", baseX + s(43),
                baseY + s(17), WHITE);

        // Search bar
        int searchX = baseX + s(690);
        int searchY = baseY + s(10);
        int searchW = s(195);
        int searchH = s(35);

        panel(g, searchX, searchY, searchW, searchH);
        g.drawString(font, "⌕", searchX + s(10),
                searchY + s(11), CYAN);

        g.drawString(font,
                searchText.isEmpty() ? "Search..." : searchText,
                searchX + s(35), searchY + s(12),
                searchFocused ? WHITE : MUTED);

        g.drawString(font, "—",
                searchX + searchW - s(38),
                searchY + s(10), WHITE);

        g.drawString(font, "X",
                searchX + searchW - s(19),
                searchY + s(10), WHITE);

        // COMBAT PANEL
        int cx = baseX + s(15);
        int cy = baseY + s(55);
        int cw = s(150);
        int ch = s(300);

        panel(g, cx, cy, cw, ch);
        header(g, "⚔ Combat", cx, cy, cw);

        int ry = cy + s(35);
        section(g, "Sword PvP", cx, ry, cw);
        ry += s(27);

        String[] sword = {
            "Auto Crit", "Range", "Shield Break",
            "Kill Aura", "Hover Totem"
        };

        for (String item : sword) {
            row(g, item, cx, ry, cw, false);
            ry += s(26);
        }

        g.fill(cx, ry, cx + cw, ry + s(1), 0xFF30475A);
        ry += s(5);

        section(g, "◆ Crystal PvP", cx, ry, cw);
        ry += s(27);

        String[] crystal = {
            "Auto Crystal", "Auto Anchor",
            "Safe Anchor", "Auto Totem", "Surround"
        };

        for (String item : crystal) {
            row(g, item, cx, ry, cw, false);
            ry += s(26);
        }

        // RENDER PANEL
        renderX = baseX + s(180);
        renderY = baseY + s(55);
        renderW = s(120);

        int rh = s(180);
        panel(g, renderX, renderY, renderW, rh);
        header(g, "▣ Render", renderX, renderY, renderW);

        renderRowHeight = s(27);
        int renderRowY = renderY + s(36);

        row(g, "FPS Counter", renderX, renderRowY,
                renderW, BreezyXModules.fpsCounter);
        renderRowY += renderRowHeight;

        row(g, "Coordinates", renderX, renderRowY,
                renderW, BreezyXModules.coordinatesHud);
        renderRowY += renderRowHeight;

        row(g, "Armor Display", renderX, renderRowY,
                renderW, BreezyXModules.armorDisplay);

        // WORLD PANEL
        int wx = baseX + s(310);
        int wy = baseY + s(55);
        int ww = s(125);
        int wh = s(120);

        panel(g, wx, wy, ww, wh);
        header(g, "◉ World", wx, wy, ww);

        row(g, "Sus Chunk Finder", wx, wy + s(40),
                ww, false);
        row(g, "Block Notifier", wx, wy + s(67),
                ww, false);
        row(g, "Spawner Notifier", wx, wy + s(94),
                ww, false);

        // PLAYER PANEL
        int px = baseX + s(445);
        int py = baseY + s(55);
        int pw = s(115);
        int ph = s(88);

        panel(g, px, py, pw, ph);
        header(g, "♟ Player", px, py, pw);
        g.drawString(font, "Freelook", px + s(10),
                py + s(43), WHITE);
        g.drawString(font, "Freecam", px + s(10),
                py + s(65), MUTED);

        // ACTIVE PANEL
        int ax = baseX + s(570);
        int ay = baseY + s(55);
        int aw = s(185);
        int ah = s(80);

        panel(g, ax, ay, aw, ah);
        header(g, "⚡ Active", ax, ay, aw);

        int activeCount = 0;
        if (BreezyXModules.fpsCounter) activeCount++;
        if (BreezyXModules.coordinatesHud) activeCount++;
        if (BreezyXModules.armorDisplay) activeCount++;

        String activeText = activeCount == 0
                ? "Nothing is switched on"
                : activeCount + " modules enabled";

        g.drawCenteredString(font, activeText,
                ax + aw / 2, ay + s(48), WHITE);

        // CLIENT PANEL
        int clx = baseX + s(765);
        int cly = baseY + s(55);
        int clw = s(125);
        int clh = s(160);

        panel(g, clx, cly, clw, clh);
        header(g, "⚙ Client", clx, cly, clw);

        g.drawString(font, "Interface",
                clx + s(12), cly + s(47), WHITE);
        g.drawString(font, "Configs",
                clx + s(12), cly + s(72), WHITE);
        g.drawString(font, "Reset Layout",
                clx + s(12), cly + s(97), WHITE);

        // Color choices
        int colorY = cly + s(130);
        int[] colors = {
            0xFFAA00FF, 0xFF008CFF, CYAN,
            0xFF35E89A, 0xFFFFC928, 0xFFFF4665
        };

        for (int i = 0; i < colors.length; i++) {
            int colorX = clx + s(12 + i * 17);
            g.fill(colorX, colorY,
                    colorX + s(11), colorY + s(11), colors[i]);
        }

        // Bottom-left profile
        int profileX = baseX + s(15);
        int profileY = baseY + s(420);

        panel(g, profileX, profileY, s(125), s(40));
        g.drawString(font, "blood",
                profileX + s(42), profileY + s(14), WHITE);
        g.drawString(font, "⌄",
                profileX + s(103), profileY + s(14), CYAN);

        panel(g, profileX, profileY + s(45), s(125), s(35));
        g.drawString(font, "● Seller",
                profileX + s(15), profileY + s(57), WHITE);

        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event,
                                boolean doubleClick) {

        if (event.button() == 0) {
            double mx = event.x();
            double my = event.y();

            int rowX = renderX;
            int rowY = renderY + s(36);

            if (mx >= rowX && mx <= rowX + renderW) {
                if (my >= rowY && my <= rowY + renderRowHeight) {
                    BreezyXModules.setEnabled(
                            "FPS Counter",
                            !BreezyXModules.fpsCounter);
                    return true;
                }

                rowY += renderRowHeight;

                if (my >= rowY && my <= rowY + renderRowHeight) {
                    BreezyXModules.setEnabled(
                            "Coordinates HUD",
                            !BreezyXModules.coordinatesHud);
                    return true;
                }

                rowY += renderRowHeight;

                if (my >= rowY && my <= rowY + renderRowHeight) {
                    BreezyXModules.setEnabled(
                            "Armor Display",
                            !BreezyXModules.armorDisplay);
                    return true;
                }
            }

            int searchX = baseX + s(690);
            int searchY = baseY + s(10);

            if (mx >= searchX && mx <= searchX + s(195)
                    && my >= searchY && my <= searchY + s(35)) {
                searchFocused = true;
                return true;
            }

            searchFocused = false;
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean charTyped(net.minecraft.client.input.CharacterEvent event) {
        if (searchFocused && searchText.length() < 24) {
            searchText += event.codepoint();
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
        if (searchFocused && event.key() == 259) {
            if (!searchText.isEmpty()) {
                searchText = searchText.substring(
                        0, searchText.length() - 1);
            }
            return true;
        }

        if (event.key() == 256) {
            onClose();
            return true;
        }

        return super.keyPressed(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
