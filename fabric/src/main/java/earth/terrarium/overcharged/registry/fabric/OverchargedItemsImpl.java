package earth.terrarium.overcharged.registry.fabric;

import earth.terrarium.overcharged.Overcharged;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class OverchargedItemsImpl {
    public static Supplier<Item> registerItem(String name, Supplier<Item> item) {
        var registry = Registry.register(Registry.ITEM, new ResourceLocation(Overcharged.MODID, name), item.get());
        return () -> registry;
    }
}
