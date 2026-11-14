
package com.breezyx;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class BreezyXClient implements ClientModInitializer {

    private static KeyMapping openMenu;

    @Override
    public void onInitializeClient() {

        openMenu = KeyBindingHelper.registerKeyBinding(
                new KeyMapping(
                        "key.breezyx.open_menu",
                        GLFW.GLFW_KEY_RIGHT_SHIFT,
                        KeyMapping.Category.MISC
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenu.consumeClick()) {
                client.setScreen(new BreezyXScreen());
            }
        });

        System.out.println("Breezy X initialized!");
    }
}
