package earth.terrarium.overcharged;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.overcharged.registry.OverchargedBlocks;
import earth.terrarium.overcharged.registry.OverchargedItems;
import earth.terrarium.overcharged.registry.OverchargedRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Overcharged {
    public static final String MODID = "overcharged";
    public static final String MOD_NAME = "Overcharged";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final CreativeModeTab TAB = registerItemGroup(new ResourceLocation(MODID, "itemgroup"), new ItemStack(OverchargedItems.HAMMER.get()));

    public static void init() {
        OverchargedBlocks.registerAll();
        OverchargedItems.registerAll();
        OverchargedRecipes.registerAll();
    }

    @ExpectPlatform
    public static CreativeModeTab registerItemGroup(ResourceLocation id, ItemStack icon) {
        throw new AssertionError();
    }

}