package earth.terrarium.overcharged.block.fabric;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;

public class GeneratorBlockEntityImpl {
    @SuppressWarnings("UnstableApiUsage")
    public static boolean isEnergyItem(ItemStack stack) {
        return EnergyStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack)) != null;
    }
}
