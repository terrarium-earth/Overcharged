package earth.terrarium.overcharged.forge;

import com.mojang.blaze3d.platform.InputConstants;
import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.item.ConstantanAIOT;
import earth.terrarium.overcharged.network.NetworkHandler;
import earth.terrarium.overcharged.network.messages.AIOTToolTypePacket;
import earth.terrarium.overcharged.network.messages.EmpoweredPacket;
import earth.terrarium.overcharged.network.messages.ToolModeCyclePacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = "overcharged", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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

    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(EMPOWER_KEYBIND);
        ClientRegistry.registerKeyBinding(TOOL_TYPE_KEYBIND);
        ClientRegistry.registerKeyBinding(TOOL_MODE_KEYBIND);
    }

    @SubscribeEvent
    public void keybindAction(InputEvent.KeyInputEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof EnergyItem) {
                while (EMPOWER_KEYBIND.consumeClick()) {
                    NetworkHandler.CHANNEL.sendToServer(new EmpoweredPacket());
                }
                while (TOOL_MODE_KEYBIND.consumeClick()) {
                    NetworkHandler.CHANNEL.sendToServer(new ToolModeCyclePacket(stack.getOrCreateTag().getInt("ToolMode") + 1));
                }
                while (TOOL_TYPE_KEYBIND.consumeClick()) {
                    if(stack.getItem() instanceof ConstantanAIOT aiot) {
                        NetworkHandler.CHANNEL.sendToServer(new AIOTToolTypePacket(aiot.changeToolType(player, stack)));
                    }
                }
            }
        }
    }
}
