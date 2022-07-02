package earth.terrarium.overcharged.fabric;

import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.registry.OverchargedBlocks;
import earth.terrarium.overcharged.registry.OverchargedItems;
import earth.terrarium.overcharged.registry.OverchargedRecipes;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;

public class OverchargedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Overcharged.init();
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) ->  {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof EnergyItem energyItem) {
                if (!energyItem.hasEnoughEnergy(stack, 200)) return false;
                if (EnergyItem.isEmpowered(stack)) {
                    energyItem.getCurrentToolMode(stack).onMineBlock(stack, level, ToolUtils.getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY), player);
                }
            }
            return true;
        });
    }
}