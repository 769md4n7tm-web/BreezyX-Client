
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
    private static final int PANEL = 0xF0101722;
    private static final int CARD = 0xFF192533;
    private static final int HOVER = 0xFF24394A;

    private static final int GUI_WIDTH = 430;
    private static final int GUI_HEIGHT = 270;

    private String selectedTab = "Render";

    private final String[] tabs = {
            "Combat",
            "Render",
            "World",
            "Player",
            "Client",
            "Settings"
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
    }

    private int left() {
        return width / 2 - GUI_WIDTH / 2;
    }

    private int top() {
        return height / 2 - GUI_HEIGHT / 2;
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

        graphics.fill(0, 0, width, height, 0x99000000);

        // Main window
        roundRect(graphics, x, y, GUI_WIDTH, GUI_HEIGHT, PANEL);

        // Top accent
        graphics.fill(
                x + 12, y,
                x + GUI_WIDTH - 12, y + 2,
                CYAN
        );

        // Header
        graphics.fill(
                x + 1, y + 1,
                x + GUI_WIDTH - 1, y + 43,
                0xFF131D29
        );

        graphics.drawString(
                font,
                "BREEZY",
                x + 16,
                y + 12,
                WHITE
        );

        graphics.drawString(
                font,
                "X",
                x + 59,
                y + 12,
                CYAN
        );

        graphics.drawString(
                font,
                "CLIENT",
                x + 79,
                y + 14,
                MUTED
        );

        graphics.drawString(
                font,
                "1.0.0",
                x + GUI_WIDTH - 48,
                y + 14,
                CYAN
        );

        // Sidebar
        graphics.fill(
                x + 1, y + 43,
                x + 112, y + GUI_HEIGHT - 1,
                0xFF121B27
        );

        graphics.drawString(
                font,
                "NAVIGATION",
                x + 13,
                y + 56,
                MUTED
        );

        // Category buttons
        for (int i = 0; i < tabs.length; i++) {
            int tabY = y + 76 + i * 28;

            boolean selected = selectedTab.equals(tabs[i]);

            boolean hovered =
                    mouseX >= x + 8 &&
                    mouseX <= x + 104 &&
                    mouseY >= tabY - 4 &&
                    mouseY <= tabY + 19;

            if (selected) {
                roundRect(
                        graphics,
                        x + 8,
                        tabY - 4,
                        96,
                        23,
                        0xFF203747
                );

                graphics.fill(
                        x + 8,
                        tabY - 3,
                        x + 10,
                        tabY + 18,
                        CYAN
                );
            } else if (hovered) {
                roundRect(
                        graphics,
                        x + 8,
                        tabY - 4,
                        96,
                        23,
                        0xFF1C2937
                );
            }

            graphics.drawString(
                    font,
                    tabs[i],
                    x + 20,
                    tabY + 4,
                    selected ? CYAN : WHITE
            );
        }

        // Main content
        int contentX = x + 128;

        graphics.drawString(
                font,
                selectedTab.toUpperCase(),
                contentX,
                y + 57,
                WHITE
        );

        graphics.drawString(
                font,
                "Manage your client interface",
                contentX,
                y + 72,
                MUTED
        );

        graphics.fill(
                contentX,
                y + 91,
                x + GUI_WIDTH - 16,
                y + 92,
                0xFF293746
        );

        if (selectedTab.equals("Combat")) {
            graphics.drawString(
                    font,
                    "Combat features are not configured.",
                    contentX,
                    y + 116,
                    MUTED
            );
        } else {
            drawModules(graphics, mouseX, mouseY, x, y);
        }

        // Footer
        graphics.fill(
                x + 112,
                y + GUI_HEIGHT - 27,
                x + GUI_WIDTH - 1,
                y + GUI_HEIGHT - 26,
                0xFF293746
        );

        graphics.drawString(
                font,
                "BREEZY X",
                contentX,
                y + GUI_HEIGHT - 17,
                CYAN
        );

        graphics.drawString(
                font,
                "Fabric Client",
                x + GUI_WIDTH - 95,
                y + GUI_HEIGHT - 17,
                MUTED
        );

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawModules(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            int x,
            int y
    ) {
        int index = 0;

        for (Module module : modules) {
            if (!module.category.equals(selectedTab)) {
                continue;
            }

            int column = index % 2;
            int row = index / 2;

            int cardX = x + 128 + column * 139;
            int cardY = y + 105 + row * 47;

            boolean hovered =
                    mouseX >= cardX &&
                    mouseX <= cardX + 128 &&
                    mouseY >= cardY &&
                    mouseY <= cardY + 38;

            roundRect(
                    graphics,
                    cardX,
                    cardY,
                    128,
                    38,
                    hovered ? HOVER : CARD
            );

            graphics.drawString(
                    font,
                    module.name,
                    cardX + 8,
                    cardY + 7,
                    WHITE
            );

            graphics.drawString(
                    font,
                    module.enabled ? "ENABLED" : "DISABLED",
                    cardX + 8,
                    cardY + 22,
                    module.enabled ? CYAN : MUTED
            );

            // Toggle indicator
            int toggleColor =
                    module.enabled ? CYAN : 0xFF526171;

            graphics.fill(
                    cardX + 105,
                    cardY + 13,
                    cardX + 119,
                    cardY + 25,
                    toggleColor
            );

            index++;
        }

        if (index == 0) {
            graphics.drawString(
                    font,
                    "Nothing here yet.",
                    x + 128,
                    y + 117,
                    MUTED
            );
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
        graphics.fill(x + 1, y + 2, x + 2, y + h - 2, color);
        graphics.fill(x + w - 2, y + 2, x + w - 1, y + h - 2, color);
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

        // Sidebar clicks
        for (int i = 0; i < tabs.length; i++) {
            int tabY = y + 76 + i * 28;

            if (mouseX >= x + 8 &&
                    mouseX <= x + 104 &&
                    mouseY >= tabY - 4 &&
                    mouseY <= tabY + 19) {

                selectedTab = tabs[i];
                return true;
            }
        }

        // Module clicks
        if (!selectedTab.equals("Combat")) {
            int index = 0;

            for (Module module : modules) {
                if (!module.category.equals(selectedTab)) {
                    continue;
                }

                int column = index % 2;
                int row = index / 2;

                int cardX = x + 128 + column * 139;
                int cardY = y + 105 + row * 47;

                if (mouseX >= cardX &&
                        mouseX <= cardX + 128 &&
                        mouseY >= cardY &&
                        mouseY <= cardY + 38) {

                    module.enabled = !module.enabled;
                    return true;
                }

                index++;
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
