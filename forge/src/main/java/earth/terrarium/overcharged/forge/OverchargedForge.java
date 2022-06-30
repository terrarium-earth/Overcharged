package earth.terrarium.overcharged.forge;

import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.registry.forge.OverchargedBlocksImpl;
import earth.terrarium.overcharged.registry.forge.OverchargedItemsImpl;
import earth.terrarium.overcharged.registry.forge.OverchargedRecipesImpl;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Overcharged.MODID)
public class OverchargedForge {
    public OverchargedForge() {
        Overcharged.init();
        //get mod event bus
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        OverchargedItemsImpl.ITEMS.register(eventBus);
        OverchargedBlocksImpl.BLOCKS.register(eventBus);
        OverchargedRecipesImpl.RECIPE_TYPES.register(eventBus);
        OverchargedRecipesImpl.RECIPE_SERIALIZERS.register(eventBus);
    }
}