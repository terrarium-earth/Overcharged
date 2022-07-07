package earth.terrarium.overcharged.forge;

import com.mojang.blaze3d.platform.InputConstants;
import earth.terrarium.overcharged.network.NetworkHandler;
import earth.terrarium.overcharged.network.messages.EmpoweredPacket;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

public class OverchargedForgeClient {
    private static final KeyMapping EMPOWER_KEYBIND = new KeyMapping(
            "key.overcharged.toggle_empowered", // The translation key of the keybinding's name
            InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_G, // The keycode of the key
            "category.overcharged.tools" // The translation key of the keybinding's category.
    );

    private static final KeyMapping TOOL_TYPE_KEYBIND = new KeyMapping(
            "key.overcharged.change_tool_type", // The translation key of the keybinding's name
            InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_G, // The keycode of the key
            "category.overcharged.tools" // The translation key of the keybinding's category.
    );

    private static final KeyMapping TOOL_MODE_KEYBIND = new KeyMapping(
            "key.overcharged.change_tool_type", // The translation key of the keybinding's name
            InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_G, // The keycode of the key
            "category.overcharged.tools" // The translation key of the keybinding's category.
    );

    public void keybindAction(InputEvent.KeyInputEvent event) {
        while (event.getKey() == EMPOWER_KEYBIND.getKey().getValue()) {
            NetworkHandler.CHANNEL.sendToServer(new EmpoweredPacket());
        }
    }
}
