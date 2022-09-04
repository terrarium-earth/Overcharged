package earth.terrarium.overcharged.registry;

import earth.terrarium.botarium.api.registry.RegistryHolder;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.item.ConstantanBow;
import earth.terrarium.overcharged.item.ConstantanSwordItem;
import earth.terrarium.overcharged.item.ConstantanTool;
import earth.terrarium.overcharged.utils.ToolType;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class OverchargedItems {
    public static final RegistryHolder<Item> ITEMS = new RegistryHolder<>(Registry.ITEM, Overcharged.MODID);

    public static final Supplier<Item> HAMMER = ITEMS.register("hammer", () -> new Item(props()));
    public static final Supplier<Item> SMASHED_IRON = ITEMS.register("smashed_iron", () -> new Item(props()));
    public static final Supplier<Item> SMASHED_COPPER = ITEMS.register("smashed_copper", () -> new Item(props()));
    public static final Supplier<Item> SMASHED_NICKEL = ITEMS.register("smashed_nickel", () -> new Item(props()));
    public static final Supplier<Item> SMASHED_CONSTANTAN = ITEMS.register("smashed_constantan", () -> new Item(props()));
    public static final Supplier<Item> NICKEL_INGOT = ITEMS.register("nickel_ingot", () -> new Item(props()));
    public static final Supplier<Item> CONSTANTAN_INGOT = ITEMS.register("constantan_ingot", () -> new Item(props()));
    public static final Supplier<Item> CONSTANTAN_SWORD = ITEMS.register("constantan_sword", () -> new ConstantanSwordItem(3, -2.4f, props()));
    public static final Supplier<Item> CONSTANTAN_SHOVEL = ITEMS.register("constantan_shovel", () -> new ConstantanTool(ToolType.SHOVEL, BlockTags.MINEABLE_WITH_SHOVEL, 1.5f, -3.0f, props()));
    public static final Supplier<Item> CONSTANTAN_PICKAXE = ITEMS.register("constantan_pickaxe", () -> new ConstantanTool(ToolType.NONE, BlockTags.MINEABLE_WITH_PICKAXE,1, -2.8f, props()));
    public static final Supplier<Item> CONSTANTAN_AXE = ITEMS.register("constantan_axe", () -> new ConstantanTool(ToolType.AXE, BlockTags.MINEABLE_WITH_AXE,5.0f, -3.0f, props()));
    public static final Supplier<Item> CONSTANTAN_HOE = ITEMS.register("constantan_hoe", () -> new ConstantanTool(ToolType.HOE, BlockTags.MINEABLE_WITH_HOE,-3, 0.0f, props()));
    public static final Supplier<Item> CONSTANTAN_AIOT = ITEMS.register("constantan_aiot", () -> new ConstantanTool(ToolType.NONE, ToolUtils.getAIOTBlockTag(),4.0f, -2.6f, props()));
    public static final Supplier<Item> CONSTANTAN_BOW = ITEMS.register("constantan_bow", () -> new ConstantanBow(props()));

    public static Item.Properties props() {
        return new Item.Properties().tab(Overcharged.TAB);
    }

    public static void init() {}

    public static void registerAll() {
        ITEMS.initialize();
    }
}
