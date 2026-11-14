
package com.breezyx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public class BreezyXModules {

    public static boolean fpsCounter = false;
    public static boolean coordinatesHud = false;
    public static boolean armorDisplay = false;

    public static boolean isEnabled(String name) {
        return switch (name) {
            case "FPS Counter" -> fpsCounter;
            case "Coordinates HUD" -> coordinatesHud;
            case "Armor Display" -> armorDisplay;
            default -> false;
        };
    }

    public static void setEnabled(String name, boolean enabled) {
        switch (name) {
            case "FPS Counter" -> fpsCounter = enabled;
            case "Coordinates HUD" -> coordinatesHud = enabled;
            case "Armor Display" -> armorDisplay = enabled;
        }
    }

    public static void renderHud(GuiGraphics graphics, DeltaTracker tickCounter) {

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null || minecraft.options.hideGui) {
            return;
        }

        int y = 8;

        if (fpsCounter) {
            graphics.drawString(
                    minecraft.font,
                    "FPS: " + minecraft.getFps(),
                    8, y, 0xFF35D9FF
            );
            y += 12;
        }

        if (coordinatesHud) {
            String coords = String.format(
                    Locale.ROOT,
                    "XYZ: %.1f / %.1f / %.1f",
                    player.getX(),
                    player.getY(),
                    player.getZ()
            );

            graphics.drawString(
                    minecraft.font,
                    coords,
                    8, y, 0xFFFFFFFF
            );
        }

        if (armorDisplay) {
            int x = minecraft.getWindow().getGuiScaledWidth() - 80;
            int armorY = 8;

            graphics.drawString(
                    minecraft.font,
                    "ARMOR",
                    x, armorY, 0xFF35D9FF
            );

            armorY += 12;

            EquipmentSlot[] slots = {
                    EquipmentSlot.FEET,
                    EquipmentSlot.LEGS,
                    EquipmentSlot.CHEST,
                    EquipmentSlot.HEAD
            };

            for (EquipmentSlot slot : slots) {
                ItemStack stack = player.getItemBySlot(slot);

                if (!stack.isEmpty()) {
                    graphics.renderItem(stack, x, armorY);
                }

                x += 18;
            }
        }
    }
}
