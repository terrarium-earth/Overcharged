package earth.terrarium.overcharged.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.item.ConstantanQuiver;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class OverchargedItems {

    public static final Supplier<Item> HAMMER = registerItem("hammer", () -> new Item(props()));
    public static final Supplier<Item> SMASHED_IRON = registerItem("smashed_iron", () -> new Item(props()));
    public static final Supplier<Item> SMASHED_COPPER = registerItem("smashed_copper", () -> new Item(props()));
    public static final Supplier<Item> SMASHED_NICKEL = registerItem("smashed_nickel", () -> new Item(props()));
    public static final Supplier<Item> SMASHED_CONSTANTAN = registerItem("smashed_constantan", () -> new Item(props()));
    public static final Supplier<Item> NICKEL_INGOT = registerItem("nickel_ingot", () -> new Item(props()));
    public static final Supplier<Item> CONSTANTAN_INGOT = registerItem("constantan_ingot", () -> new Item(props()));
    //register constantan quiver
    public static final Supplier<Item> CONSTANTAN_QUIVER = registerItem("constantan_quiver", () -> new ConstantanQuiver(props()));

    public static Item.Properties props() {
        return new Item.Properties().tab(Overcharged.TAB);
    }

    @ExpectPlatform
    public static Supplier<Item> registerItem(String name, Supplier<Item> item) {
        throw new AssertionError();
    }

    public static void registerAll() {

    }
}
