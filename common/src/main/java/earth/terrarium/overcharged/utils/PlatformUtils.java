package earth.terrarium.overcharged.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.ItemStack;

public class PlatformUtils {

    @ExpectPlatform
    public static int getBurnTime(ItemStack stack) {
        return 0;
    }
}
