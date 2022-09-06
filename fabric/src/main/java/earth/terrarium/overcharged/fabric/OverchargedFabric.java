package earth.terrarium.overcharged.fabric;

import earth.terrarium.botarium.api.energy.EnergyHooks;
import earth.terrarium.botarium.api.energy.PlatformEnergyManager;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.energy.ConstantanItem;
import earth.terrarium.overcharged.energy.ToolMode;
import earth.terrarium.overcharged.network.NetworkHandler;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;

public class OverchargedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Overcharged.init();
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) ->  {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof ConstantanItem constantanItem) {
                PlatformEnergyManager energy = EnergyHooks.getItemHandler(stack);
                if (energy.getStoredEnergy() < 200) return false;
                if (ToolUtils.isEmpowered(stack)) {
                    ToolMode currentToolMode = constantanItem.getCurrentToolMode(stack);
                    if(currentToolMode != null) {
                        currentToolMode.onMineBlock(stack, level, ToolUtils.getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY), player);
                    }
                }
            }
            return true;
        });
        NetworkHandler.registerPackets();
    }
}