
package com.breezyx;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class BreezyXScreen extends Screen {

    private static final int CYAN = 0xFF35D9FF;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int MUTED = 0xFF8493A5;

    private static final int WIDTH = 380;
    private static final int HEIGHT = 240;

    private String selectedTab = "Render";
    private long openedAt;

    private final String[] tabs = {
            "Combat", "Render", "World",
            "Player", "Client", "Settings"
    };

    private final List<Module> modules = new ArrayList<>();

    public BreezyXScreen() {
        super(Component.literal("Breezy X"));

        modules.add(new Module("Coordinates HUD", "Render"));
        modules.add(new Module("FPS Counter", "Render"));
        modules.add(new Module("Light Debug", "Render"));
        modules.add(new Module("Status Panel", "Render"));
        modules.add(new Module("Armor Display", "Render"));
        modules.add(new Module("Clock HUD", "Render"));

        modules.add(new Module("World Information", "World"));
        modules.add(new Module("Time Display", "World"));

        modules.add(new Module("Movement HUD", "Player"));
        modules.add(new Module("Player Information", "Player"));

        modules.add(new Module("Client Sounds", "Client"));
        modules.add(new Module("Watermark", "Client"));

        modules.add(new Module("Menu Animation", "Settings"));
        modules.add(new Module("Interface Effects", "Settings"));

        for (Module module : modules) {
            module.enabled = BreezyXModules.isEnabled(module.name);
        }
    }

    @Override
    protected void init() {
        super.init();
        openedAt = System.currentTimeMillis();
    }

    private int left() {
        return width / 2 - WIDTH / 2;
    }

    private int top() {
        return height / 2 - HEIGHT / 2;
    }

    private float animation() {
        return Math.min(
                1.0F,
                (System.currentTimeMillis() - openedAt) / 220.0F
        );
    }

    private int alpha(int color, float amount) {
        int a = (int) (((color >>> 24) & 255) * amount);
        return (color & 0x00FFFFFF) | (a << 24);
    }

    private int mix(int first, int second, float amount) {
        amount = Math.max(0, Math.min(1, amount));

        int a = (int) (((first >> 24) & 255) *
                (1 - amount) + ((second >> 24) & 255) * amount);
        int r = (int) (((first >> 16) & 255) *
                (1 - amount) + ((second >> 16) & 255) * amount);
        int g = (int) (((first >> 8) & 255) *
                (1 - amount) + ((second >> 8) & 255) * amount);
        int b = (int) ((first & 255) *
                (1 - amount) + (second & 255) * amount);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        int x = left();
        int y = top();

        float open = animation();

        graphics.fill(0, 0, width, height,
                alpha(0x99000000, open));

        roundRect(graphics, x, y, WIDTH, HEIGHT,
                alpha(0xF0101722, open));

        graphics.fill(x + 12, y, x + WIDTH - 12, y + 2,
                alpha(CYAN, open));

        graphics.fill(x + 1, y + 1, x + WIDTH - 1, y + 39,
                alpha(0xFF131D29, open));

        graphics.drawString(font, "BREEZY", x + 13, y + 12,
                alpha(WHITE, open));
        graphics.drawString(font, "X", x + 56, y + 12,
                alpha(CYAN, open));
        graphics.drawString(font, "CLIENT", x + 75, y + 14,
                alpha(MUTED, open));
        graphics.drawString(font, "1.0", x + WIDTH - 35, y + 14,
                alpha(CYAN, open));

        graphics.fill(x + 1, y + 39, x + 103, y + HEIGHT - 1,
                alpha(0xFF121B27, open));

        graphics.drawString(font, "MENU", x + 13, y + 49,
                alpha(MUTED, open));

        for (int i = 0; i < tabs.length; i++) {
            int tabY = y + 67 + i * 25;
            boolean selected = selectedTab.equals(tabs[i]);

            boolean hovered =
                    mouseX >= x + 7 && mouseX <= x + 96 &&
                    mouseY >= tabY - 3 && mouseY <= tabY + 17;

            int background = selected
                    ? 0xFF203747
                    : hovered ? 0xFF1C2937 : 0xFF121B27;

            roundRect(graphics, x + 7, tabY - 3, 89, 21,
                    alpha(background, open));

            if (selected) {
                graphics.fill(x + 7, tabY - 2, x + 9, tabY + 17,
                        alpha(CYAN, open));
            }

            graphics.drawString(font, tabs[i], x + 17, tabY + 3,
                    alpha(selected ? CYAN : WHITE, open));
        }

        int contentX = x + 116;

        graphics.drawString(font, selectedTab.toUpperCase(),
                contentX, y + 49, alpha(WHITE, open));

        graphics.drawString(font, "Breezy X Modules",
                contentX, y + 63, alpha(MUTED, open));

        graphics.fill(contentX, y + 81, x + WIDTH - 13, y + 82,
                alpha(0xFF293746, open));

        if (selectedTab.equals("Combat")) {
            graphics.drawString(font, "No modules configured.",
                    contentX, y + 103, alpha(MUTED, open));
        } else {
            drawModules(graphics, mouseX, mouseY, x, y, open);
        }

        graphics.fill(x + 103, y + HEIGHT - 23,
                x + WIDTH - 1, y + HEIGHT - 22,
                alpha(0xFF293746, open));

        graphics.drawString(font, "BREEZY X",
                contentX, y + HEIGHT - 15, alpha(CYAN, open));

        graphics.drawString(font, "1.0.0",
                x + WIDTH - 42, y + HEIGHT - 15,
                alpha(MUTED, open));

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawModules(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            int x,
            int y,
            float open
    ) {
        int index = 0;

        for (Module module : modules) {
            if (!module.category.equals(selectedTab)) continue;

            int column = index % 2;
            int row = index / 2;

            int cardX = x + 116 + column * 122;
            int cardY = y + 94 + row * 39;

            boolean hovered =
                    mouseX >= cardX && mouseX <= cardX + 112 &&
                    mouseY >= cardY && mouseY <= cardY + 33;

            float target = hovered ? 1.0F : 0.0F;
            module.hover += (target - module.hover) * 0.22F;

            float toggleTarget = module.enabled ? 1.0F : 0.0F;
            module.toggle += (toggleTarget - module.toggle) * 0.22F;

            int background = mix(0xFF192533, 0xFF294354,
                    module.hover);

            roundRect(graphics, cardX, cardY, 112, 33,
                    alpha(background, open));

            graphics.drawString(font, module.name,
                    cardX + 7, cardY + 5, alpha(WHITE, open));

            graphics.drawString(font,
                    module.enabled ? "ON" : "OFF",
                    cardX + 7, cardY + 19,
                    alpha(module.enabled ? CYAN : MUTED, open));

            int toggleX = cardX + 89;
            int toggleY = cardY + 12;

            graphics.fill(toggleX, toggleY, toggleX + 16,
                    toggleY + 9,
                    alpha(mix(0xFF465363, CYAN, module.toggle), open));

            int knobX = toggleX + 2 +
                    Math.round(module.toggle * 7);

            graphics.fill(knobX, toggleY + 2,
                    knobX + 5, toggleY + 7, alpha(WHITE, open));

            index++;
        }

        if (index == 0) {
            graphics.drawString(font, "Nothing here yet.",
                    x + 116, y + 104, alpha(MUTED, open));
        }
    }

    private void roundRect(
            GuiGraphics graphics,
            int x,
            int y,
            int w,
            int h,
            int color
    ) {
        graphics.fill(x + 3, y, x + w - 3, y + h, color);
        graphics.fill(x, y + 3, x + w, y + h - 3, color);
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

        for (int i = 0; i < tabs.length; i++) {
            int tabY = y + 67 + i * 25;

            if (mouseX >= x + 7 && mouseX <= x + 96 &&
                    mouseY >= tabY - 3 && mouseY <= tabY + 17) {
                selectedTab = tabs[i];
                return true;
            }
        }

        int index = 0;

        for (Module module : modules) {
            if (!module.category.equals(selectedTab)) continue;

            int column = index % 2;
            int row = index / 2;

            int cardX = x + 116 + column * 122;
            int cardY = y + 94 + row * 39;

            if (mouseX >= cardX && mouseX <= cardX + 112 &&
                    mouseY >= cardY && mouseY <= cardY + 33) {

                module.enabled = !module.enabled;

                BreezyXModules.setEnabled(
                        module.name,
                        module.enabled
                );

                return true;
            }

            index++;
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
        float hover;
        float toggle;

        Module(String name, String category) {
            this.name = name;
            this.category = category;
        }
    }
}
