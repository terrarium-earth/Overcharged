package earth.terrarium.overcharged.utils;

import earth.terrarium.overcharged.energy.ConstantanItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

@FunctionalInterface
public interface AbstractToolType {
    InteractionResult apply(ConstantanItem energyItem, UseOnContext context);
}
