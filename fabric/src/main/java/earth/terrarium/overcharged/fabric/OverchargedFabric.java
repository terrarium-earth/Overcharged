package earth.terrarium.overcharged.fabric;

import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.registry.OverchargedBlocks;
import earth.terrarium.overcharged.registry.OverchargedItems;
import earth.terrarium.overcharged.registry.OverchargedRecipes;
import net.fabricmc.api.ModInitializer;

public class OverchargedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Overcharged.init();
    }
}