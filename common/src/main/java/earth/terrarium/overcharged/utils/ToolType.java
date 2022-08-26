package earth.terrarium.overcharged.utils;

import earth.terrarium.overcharged.energy.ToolMode;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import java.util.function.Consumer;
import java.util.function.Function;

public enum ToolType {
    SHOVEL(toolAction(ToolUtils::shovelAction)),
    HOE(toolAction(ToolUtils::hoeAction)),
    AXE(toolAction(ToolUtils::axeAction)),
    NONE((item, context) -> InteractionResult.PASS)
    ;

    public final AbstractToolType toolAction;
    ToolType(AbstractToolType toolAction) {
        this.toolAction = toolAction;
    }

    private static AbstractToolType toolAction(Function<UseOnContext, InteractionResult> consumer) {
        return (energyItem, context) -> {
            var action = consumer.apply(context);
            ItemStack itemInHand = context.getItemInHand();
            ToolMode currentToolMode = energyItem.getCurrentToolMode(itemInHand);
            if(currentToolMode != null) {
                currentToolMode.useTool(context, consumer::apply);
            }
            return action;
        };
    }
}
