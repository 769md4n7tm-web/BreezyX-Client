
package com.breezyx;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class BreezyXScreen extends Screen {

    private static final int CYAN = 0xFF35D9FF;
    private static final int PANEL = 0xE6121924;
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
    protected void init() {
        super.init();
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick
    ) {
        graphics.fill(0, 0, width, height, 0xFF080D16);

        int left = width / 2 - 260;
        int top = height / 2 - 165;

        graphics.fill(left, top, left + 520, top + 330, PANEL);
        graphics.fill(left, top, left + 520, top + 2, CYAN);
        graphics.fill(left, top, left + 125, top + 330, 0xFF101925);

        graphics.drawString(font, "BREEZY", left + 17, top + 20, CYAN);
        graphics.drawString(font, "X CLIENT", left + 17, top + 34, WHITE);

        String[] tabs = {
                "Combat", "Render", "World",
                "Player", "Active", "Client"
        };

        for (int i = 0; i < tabs.length; i++) {
            int y = top + 75 + i * 32;

            if (selectedTab.equals(tabs[i])) {
                graphics.fill(left + 8, y - 5, left + 117, y + 20, 0xFF203746);
                graphics.fill(left + 8, y - 5, left + 11, y + 20, CYAN);
            }

            graphics.drawString(
                    font,
                    tabs[i],
                    left + 22,
                    y + 3,
                    selectedTab.equals(tabs[i]) ? CYAN : MUTED
            );
        }

        graphics.drawString(
                font, selectedTab.toUpperCase(),
                left + 150, top + 25, WHITE
        );

        graphics.drawString(
                font, "Breezy X / Client Interface",
                left + 150, top + 43, MUTED
        );

        if (selectedTab.equals("Render")) {
            drawModules(graphics, left, top);
        } else {
            graphics.drawString(
                    font, "Coming soon...",
                    left + 150, top + 100, MUTED
            );
        }

        graphics.drawString(
                font, "BREEZY X | 1.0.0",
                left + 150, top + 305, CYAN
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

            int x = left + 150 + (i % 2) * 175;
            int y = top + 75 + (i / 2) * 65;

            graphics.fill(x, y, x + 160, y + 52, 0xFF172331);

            graphics.drawString(
                    font, module.name,
                    x + 12, y + 12, WHITE
            );

            int color = module.enabled ? CYAN : 0xFF566273;

            graphics.fill(x + 118, y + 32, x + 148, y + 36, color);

            graphics.fill(
                    module.enabled ? x + 138 : x + 118,
                    y + 27,
                    module.enabled ? x + 148 : x + 128,
                    y + 41,
                    color
            );

            graphics.drawString(
                    font,
                    module.enabled ? "ON" : "OFF",
                    x + 12, y + 34,
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

        int left = width / 2 - 260;
        int top = height / 2 - 165;

        String[] tabs = {
                "Combat", "Render", "World",
                "Player", "Active", "Client"
        };

        for (int i = 0; i < tabs.length; i++) {
            int y = top + 75 + i * 32;

            if (mouseX >= left + 8 && mouseX <= left + 117
                    && mouseY >= y - 5 && mouseY <= y + 20) {
                selectedTab = tabs[i];
                return true;
            }
        }

        if (selectedTab.equals("Render")) {
            for (int i = 0; i < modules.size(); i++) {
                int x = left + 150 + (i % 2) * 175;
                int y = top + 75 + (i / 2) * 65;

                if (mouseX >= x && mouseX <= x + 160
                        && mouseY >= y && mouseY <= y + 52) {
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
