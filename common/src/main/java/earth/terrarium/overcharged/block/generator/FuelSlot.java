package earth.terrarium.overcharged.block.generator;

import earth.terrarium.overcharged.utils.PlatformUtils;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FuelSlot extends Slot {
    public FuelSlot(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return PlatformUtils.getBurnTime(stack) > 0;
    }
}
