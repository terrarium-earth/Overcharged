package earth.terrarium.overcharged.block.generator;

import earth.terrarium.botarium.api.energy.EnergyHooks;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnergySlot extends Slot {
    public EnergySlot(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack itemStack) {
        return EnergyHooks.isEnergyItem(itemStack);
    }
}
