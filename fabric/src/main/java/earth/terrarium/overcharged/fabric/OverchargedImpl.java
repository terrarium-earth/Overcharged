package earth.terrarium.overcharged.fabric;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class OverchargedImpl {
    public static CreativeModeTab registerItemGroup(ResourceLocation id, Supplier<ItemStack> icon) {
        return FabricItemGroupBuilder.build(id, icon);
    }
}
