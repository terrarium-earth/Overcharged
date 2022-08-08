package earth.terrarium.overcharged.utils;

import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.energy.ToolMode;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import java.util.function.BiFunction;

public enum ToolTypes implements ToolType {
    SHOVEL((energyItem, useOnContext) -> {
        var action = energyItem.shovelAction(useOnContext);
        ItemStack itemInHand = useOnContext.getItemInHand();
        ToolMode currentToolMode = energyItem.getCurrentToolMode(itemInHand);
        if(currentToolMode != null) {
            currentToolMode.useTool(useOnContext, energyItem::shovelAction);
        }
        return action;
    }),
    HOE((energyItem, useOnContext) -> {
        var action = energyItem.hoeAction(useOnContext);
        ItemStack itemInHand = useOnContext.getItemInHand();
        ToolMode currentToolMode = energyItem.getCurrentToolMode(itemInHand);
        if(currentToolMode != null) {
            currentToolMode.useTool(useOnContext, energyItem::hoeAction);
        }
        return action;
    }),
    AXE((energyItem, useOnContext) -> {
        var action = energyItem.axeAction(useOnContext);
        ItemStack itemInHand = useOnContext.getItemInHand();
        ToolMode currentToolMode = energyItem.getCurrentToolMode(itemInHand);
        if(currentToolMode != null) {
            currentToolMode.useTool(useOnContext, energyItem::axeAction);
        }
        return action;
    });

    private final BiFunction<EnergyItem, UseOnContext, InteractionResult> function;
    ToolTypes(BiFunction<EnergyItem, UseOnContext, InteractionResult> function) {
        this.function = function;
    }

    @Override
    public BiFunction<EnergyItem, UseOnContext, InteractionResult> getFunction() {
        return function;
    }
}
