package earth.terrarium.overcharged.utils;

import earth.terrarium.overcharged.energy.EnergyItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

import java.util.function.BiFunction;

public interface ToolType {
    BiFunction<EnergyItem, UseOnContext, InteractionResult> getFunction();
}
