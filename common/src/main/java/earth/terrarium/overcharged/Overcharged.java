package earth.terrarium.overcharged;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.overcharged.network.NetworkHandler;
import earth.terrarium.overcharged.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class Overcharged {
    public static final String MODID = "overcharged";
    public static final String MOD_NAME = "Overcharged";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final CreativeModeTab TAB = registerItemGroup(new ResourceLocation(MODID, "itemgroup"), () -> new ItemStack(OverchargedItems.HAMMER.get()));

    public static void init() {
        OverchargedBlocks.init();
        OverchargedItems.init();
        OverchargedRecipes.init();
        OverchargedEntities.init();
        OverchargedMenus.init();
    }

    public static void registerAll() {
        OverchargedBlocks.registerAll();
        OverchargedItems.registerAll();
        OverchargedRecipes.registerAll();
        OverchargedEntities.registerAll();
        OverchargedMenus.registerAll();
    }

    @ExpectPlatform
    public static CreativeModeTab registerItemGroup(ResourceLocation id, Supplier<ItemStack> icon) {
        throw new AssertionError();
    }

}