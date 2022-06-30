package earth.terrarium.overcharged.mixin;

import earth.terrarium.overcharged.energy.EnergyItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import team.reborn.energy.api.base.SimpleBatteryItem;

@Mixin(EnergyItem.class)
public abstract class EnergyItemMixin implements EnergyItem, SimpleBatteryItem {

    @Override
    public int getEnergy(ItemStack stack) {
        return (int) this.getStoredEnergy(stack);
    }

    @Override
    public void setEnergy(ItemStack stack, int energy) {
        this.setStoredEnergy(stack, energy);
    }

    @Override
    public long getEnergyCapacity() {
        return this.getMaxEnergy();
    }

    @Override
    public long getEnergyMaxInput() {
        return this.getMaxEnergy();
    }

    @Override
    public long getEnergyMaxOutput() {
        return this.getMaxEnergy();
    }
}
