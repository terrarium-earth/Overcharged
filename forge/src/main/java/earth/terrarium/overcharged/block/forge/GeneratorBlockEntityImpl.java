package earth.terrarium.overcharged.block.forge;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;

public class GeneratorBlockEntityImpl {
    public static boolean isEnergyItem(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY, null).isPresent();
    }
}
