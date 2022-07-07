package earth.terrarium.overcharged.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class OverchargedImpl {
    public static CreativeModeTab registerItemGroup(ResourceLocation id, Supplier<ItemStack> icon) {
        return new CreativeModeTab(id.getNamespace() + "." + id.getPath()) {
            @Override
            public ItemStack makeIcon() {
                return icon.get();
            }
        };
    }
}
