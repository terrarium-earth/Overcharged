package earth.terrarium.overcharged.registry;

import earth.terrarium.botarium.api.registry.RegistryHelpers;
import earth.terrarium.botarium.api.registry.RegistryHolder;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.block.generator.GeneratorMenu;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

import java.util.Map;
import java.util.function.Supplier;

public class OverchargedMenus {
    public static final RegistryHolder<MenuType<?>> MENUS = new RegistryHolder<>(Registry.MENU, Overcharged.MODID);

    public static final Supplier<MenuType<GeneratorMenu>> GENERATOR_MENU = MENUS.register("generator", () -> RegistryHelpers.createMenuType(GeneratorMenu::new));

    public static void init() {}

    public static void registerAll() {
        MENUS.initialize();
    }
}
