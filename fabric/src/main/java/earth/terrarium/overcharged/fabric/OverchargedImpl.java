package earth.terrarium.overcharged.fabric;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class OverchargedImpl {
    public static CreativeModeTab registerItemGroup(ResourceLocation id, ItemStack icon) {
        return FabricItemGroupBuilder.build(id, () -> icon);
    }
}
