package earth.terrarium.overcharged.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import earth.terrarium.overcharged.OverchargedClient;
import earth.terrarium.overcharged.energy.ConstantanItem;
import earth.terrarium.overcharged.network.NetworkHandler;
import earth.terrarium.overcharged.network.messages.EmpoweredPacket;
import earth.terrarium.overcharged.network.messages.ToolModeCyclePacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class OverchargedFabricClient implements ClientModInitializer {

    private static final KeyMapping EMPOWER_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.overcharged.toggle_empowered", // The translation key of the keybinding's name
            InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_B, // The keycode of the key
            "category.overcharged.tools" // The translation key of the keybinding's category.
    ));

    private static final KeyMapping TOOL_TYPE_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.overcharged.change_tool_type", // The translation key of the keybinding's name
            InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_N, // The keycode of the key
            "category.overcharged.tools" // The translation key of the keybinding's category.
    ));

    private static final KeyMapping TOOL_MODE_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.overcharged.change_tool_mode", // The translation key of the keybinding's name
            InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_M, // The keycode of the key
            "category.overcharged.tools" // The translation key of the keybinding's category.
    ));
    @Override
    public void onInitializeClient() {
        OverchargedClient.initClient();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Player player = client.player;
            if (player != null) {
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() instanceof ConstantanItem) {
                    while (EMPOWER_KEYBIND.consumeClick()) {
                        NetworkHandler.CHANNEL.sendToServer(new EmpoweredPacket());
                    }
                    while (TOOL_MODE_KEYBIND.consumeClick()) {
                        NetworkHandler.CHANNEL.sendToServer(new ToolModeCyclePacket(stack.getOrCreateTag().getInt("ToolMode") + 1));
                    }
                }
            }
        });
    }
}
