
package com.breezyx;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class BreezyXScreen extends Screen {

    private static final int CYAN = 0xFF20D9FF;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int MUTED = 0xFF8493A5;
    private static final int PANEL = 0xF014202E;
    private static final int CARD = 0xFF1C2B3B;

    private String selectedTab = "Render";
    private long openedAt;

    private final String[] tabs = {
        "Combat", "Render", "World",
        "Player", "Client"
    };

    private final List<Module> modules = new ArrayList<>();

    public BreezyXScreen() {
        super(Component.literal("Breezy X"));

        modules.add(new Module("FPS Counter", "Render"));
        modules.add(new Module("Coordinates HUD", "Render"));
        modules.add(new Module("Armor Display", "Render"));

        for (Module module : modules) {
            module.enabled = BreezyXModules.isEnabled(module.name);
        }
    }

    @Override
    protected void init() {
        super.init();
        openedAt = System.currentTimeMillis();
    }

    private int panelWidth() {
        return Math.min(760, width - 24);
    }

    private int panelHeight() {
        return Math.min(430, height - 24);
    }

    private int left() {
        return (width - panelWidth()) / 2;
    }

    private int top() {
        return (height - panelHeight()) / 2;
    }

    private int alpha(int color, float amount) {
        int a = (int) (((color >>> 24) & 255) * amount);
        return (color & 0x00FFFFFF) | (a << 24);
    }

    private float animation() {
        return Math.min(1.0F,
            (System.currentTimeMillis() - openedAt) / 220.0F);
    }

    private void panel(GuiGraphics g, int x, int y, int w, int h) {
        g.fill(x, y, x + w, y + h, PANEL);
        g.fill(x, y, x + w, y + 2, CYAN);
        g.fill(x, y + h - 2, x + w, y + h, 0xFF174B63);
        g.fill(x, y, x + 2, y + h, 0xFF174B63);
        g.fill(x + w - 2, y, x + w, y + h, 0xFF174B63);
    }

    @Override
    public void render(
        GuiGraphics g,
        int mouseX,
        int mouseY,
        float partialTick
    ) {
        float fade = animation();

        int x = left();
        int y = top();
        int w = panelWidth();
        int h = panelHeight();

        g.fill(0, 0, width, height, alpha(0x99000000, fade));

        // Main window
        g.fill(x, y, x + w, y + h, alpha(0xFF0D1724, fade));

        // Header
        g.fill(x, y, x + w, y + 58, alpha(0xFF111F2E, fade));
        g.fill(x, y, x + w, y + 2, alpha(CYAN, fade));

        g.drawString(font, "BREEZY", x + 22, y + 19,
            alpha(WHITE, fade));
        g.drawString(font, "X", x + 67, y + 19,
            alpha(CYAN, fade));
        g.drawString(font, "CLIENT", x + 100, y + 20,
            alpha(MUTED, fade));

        g.drawString(font, "1.0.0",
            x + w - 55, y + 20, alpha(CYAN, fade));

        // Sidebar
        g.fill(x, y + 58, x + 145, y + h,
            alpha(0xFF101B29, fade));

        g.drawString(font, "NAVIGATION",
            x + 14, y + 77, alpha(MUTED, fade));

        for (int i = 0; i < tabs.length; i++) {
            int tabY = y + 105 + i * 38;
            boolean selected = selectedTab.equals(tabs[i]);

            if (selected) {
                g.fill(x + 7, tabY - 5, x + 137, tabY + 23,
                    alpha(0xFF203747, fade));
                g.fill(x + 7, tabY - 5, x + 10, tabY + 23,
                    alpha(CYAN, fade));
            }

            g.drawString(font, tabs[i],
                x + 20, tabY + 4,
                alpha(selected ? CYAN : WHITE, fade));
        }

        // Main content
        int contentX = x + 165;
        int contentY = y + 77;

        g.drawString(font, selectedTab.toUpperCase(),
            contentX, contentY, alpha(WHITE, fade));

        g.drawString(font, "Breezy X Client Interface",
            contentX, contentY + 19, alpha(MUTED, fade));

        g.fill(contentX, contentY + 40,
            x + w - 18, contentY + 41,
            alpha(0xFF344456, fade));

        if (selectedTab.equals("Render")) {
            drawRenderCards(g, contentX, contentY + 60, fade);
        } else {
            panel(g, contentX, contentY + 60,
                Math.max(100, w - 185), 90);

            g.drawString(font, "No modules available",
                contentX + 15, contentY + 82,
                alpha(MUTED, fade));
        }

        // Active panel
        int activeX = x + w - 170;
        int activeY = y + 58;

        panel(g, activeX, activeY, 155, 105);

        g.drawString(font, "ACTIVE",
            activeX + 12, activeY + 13, alpha(CYAN, fade));

        int activeCount = 0;
        for (Module module : modules) {
            if (module.enabled) activeCount++;
        }

        g.drawString(font,
            activeCount == 0
                ? "Nothing is switched on"
                : activeCount + " modules enabled",
            activeX + 12, activeY + 38,
            alpha(WHITE, fade));

        int activeYText = activeY + 58;

        for (Module module : modules) {
            if (module.enabled) {
                g.drawString(font, module.name,
                    activeX + 12, activeYText,
                    alpha(CYAN, fade));
                activeYText += 12;
            }
        }

        // Footer
        g.fill(x + 145, y + h - 28,
            x + w, y + h - 27, alpha(0xFF344456, fade));

        g.drawString(font, "BREEZY X",
            x + 165, y + h - 17, alpha(CYAN, fade));

        g.drawString(font, "Fabric Client",
            x + w - 100, y + h - 17, alpha(MUTED, fade));

        super.render(g, mouseX, mouseY, partialTick);
    }

    private void drawRenderCards(
        GuiGraphics g,
        int x,
        int y,
        float fade
    ) {
        int cardWidth = 190;
        int cardHeight = 58;
        int gap = 15;

        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);

            int column = i % 2;
            int row = i / 2;

            int cardX = x + column * (cardWidth + gap);
            int cardY = y + row * (cardHeight + gap);

            g.fill(cardX, cardY,
                cardX + cardWidth, cardY + cardHeight,
                alpha(CARD, fade));

            g.fill(cardX, cardY,
                cardX + 2, cardY + cardHeight,
                alpha(CYAN, fade));

            g.drawString(font, module.name,
                cardX + 12, cardY + 12,
                alpha(WHITE, fade));

            g.drawString(font,
                module.enabled ? "ENABLED" : "DISABLED",
                cardX + 12, cardY + 34,
                alpha(module.enabled ? CYAN : MUTED, fade));

            int toggleColor = module.enabled
                ? CYAN : 0xFF526174;

            g.fill(cardX + cardWidth - 32, cardY + 20,
                cardX + cardWidth - 12, cardY + 38,
                alpha(toggleColor, fade));

            int knobX = module.enabled
                ? cardX + cardWidth - 19
                : cardX + cardWidth - 30;

            g.fill(knobX, cardY + 23,
                knobX + 9, cardY + 35,
                alpha(WHITE, fade));
        }
    }

    @Override
    public boolean mouseClicked(
        MouseButtonEvent event,
        boolean doubleClick
    ) {
        if (event.button() != 0) {
            return super.mouseClicked(event, doubleClick);
        }

        double mouseX = event.x();
        double mouseY = event.y();

        int x = left();
        int y = top();

        // Navigation
        for (int i = 0; i < tabs.length; i++) {
            int tabY = y + 105 + i * 38;

            if (mouseX >= x + 7 && mouseX <= x + 137
                && mouseY >= tabY - 5 && mouseY <= tabY + 23) {
                selectedTab = tabs[i];
                return true;
            }
        }

        // Render toggles
        if (selectedTab.equals("Render")) {
            int contentX = x + 165;
            int contentY = y + 77 + 60;

            int cardWidth = 190;
            int cardHeight = 58;
            int gap = 15;

            for (int i = 0; i < modules.size(); i++) {
                Module module = modules.get(i);

                int column = i % 2;
                int row = i / 2;

                int cardX = contentX + column * (cardWidth + gap);
                int cardY = contentY + row * (cardHeight + gap);

                if (mouseX >= cardX && mouseX <= cardX + cardWidth
                    && mouseY >= cardY && mouseY <= cardY + cardHeight) {

                    module.enabled = !module.enabled;

                    BreezyXModules.setEnabled(
                        module.name,
                        module.enabled
                    );

                    return true;
                }
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static class Module {
        final String name;
        final String category;
        boolean enabled;

        Module(String name, String category) {
            this.name = name;
            this.category = category;
        }
    }
}
