package earth.terrarium.overcharged.registry.forge;

import earth.terrarium.overcharged.Overcharged;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class OverchargedItemsImpl {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Overcharged.MODID);

    public static Supplier<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }
}
