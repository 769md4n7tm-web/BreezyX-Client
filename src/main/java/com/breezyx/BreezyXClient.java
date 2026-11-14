

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
    private static final int MUTED = 0xFF8796A8;

    private String selectedTab = "Render";
    private final List<Module> modules = new ArrayList<>();

    public BreezyXScreen() {
        super(Component.literal("Breezy X"));

        modules.add(new Module("Block ESP"));
        modules.add(new Module("Storage ESP"));
        modules.add(new Module("Spotify HUD"));
        modules.add(new Module("Admin List"));
        modules.add(new Module("HUD"));
        modules.add(new Module("Light Debug"));
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        int left = width / 2 - 175;
        int top = height / 2 - 115;

        graphics.fill(0, 0, width, height, 0x99000000);

        graphics.fill(left, top, left + 350, top + 230, 0xE6121924);
        graphics.fill(left, top, left + 350, top + 2, CYAN);
        graphics.fill(left, top, left + 90, top + 230, 0xFF101925);

        graphics.drawString(font, "BREEZY X", left + 10, top + 15, CYAN);

        String[] tabs = {
                "Combat", "Render", "World",
                "Player", "Active", "Client"
        };

        for (int i = 0; i < tabs.length; i++) {
            int y = top + 43 + i * 27;

            if (selectedTab.equals(tabs[i])) {
                graphics.fill(left + 5, y - 3, left + 85, y + 17, 0xFF203746);
                graphics.fill(left + 5, y - 3, left + 7, y + 17, CYAN);
            }

            graphics.drawString(
                    font, tabs[i], left + 13, y + 3,
                    selectedTab.equals(tabs[i]) ? CYAN : MUTED
            );
        }

        graphics.drawString(
                font, selectedTab.toUpperCase(),
                left + 105, top + 17, WHITE
        );

        graphics.drawString(
                font, "Breezy X Client",
                left + 105, top + 32, MUTED
        );

        if (selectedTab.equals("Render")) {
            drawModules(graphics, left, top);
        } else {
            graphics.drawString(
                    font, "Coming soon...",
                    left + 105, top + 80, MUTED
            );
        }

        graphics.drawString(
                font, "BREEZY X | 1.0.0",
                left + 105, top + 211, CYAN
        );

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void drawModules(
            GuiGraphics graphics,
            int left,
            int top
    ) {
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);

            int x = left + 105 + (i % 2) * 115;
            int y = top + 55 + (i / 2) * 48;

            graphics.fill(x, y, x + 105, y + 39, 0xFF172331);

            graphics.drawString(
                    font, module.name,
                    x + 5, y + 8, WHITE
            );

            graphics.drawString(
                    font,
                    module.enabled ? "ON" : "OFF",
                    x + 5, y + 24,
                    module.enabled ? CYAN : MUTED
            );
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

        int left = width / 2 - 175;
        int top = height / 2 - 115;

        String[] tabs = {
                "Combat", "Render", "World",
                "Player", "Active", "Client"
        };

        for (int i = 0; i < tabs.length; i++) {
            int y = top + 43 + i * 27;

            if (mouseX >= left + 5 && mouseX <= left + 85
                    && mouseY >= y - 3 && mouseY <= y + 17) {
                selectedTab = tabs[i];
                return true;
            }
        }

        if (selectedTab.equals("Render")) {
            for (int i = 0; i < modules.size(); i++) {
                int x = left + 105 + (i % 2) * 115;
                int y = top + 55 + (i / 2) * 48;

                if (mouseX >= x && mouseX <= x + 105
                        && mouseY >= y && mouseY <= y + 39) {
                    modules.get(i).enabled = !modules.get(i).enabled;
                    return true;
                }
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    private static class Module {
        final String name;
        boolean enabled = false;

        Module(String name) {
            this.name = name;
        }
    }
}
