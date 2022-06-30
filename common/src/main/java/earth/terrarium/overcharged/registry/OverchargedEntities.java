package earth.terrarium.overcharged.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.overcharged.entity.PoweredArrow;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class OverchargedEntities {

    public static final Supplier<EntityType<PoweredArrow>> POWERED_ARROW = registerEntity("powered_arrow", PoweredArrow::new, MobCategory.MISC, 0.5F, 0.5F);

    @ExpectPlatform
    public static <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> factory, MobCategory group, float width, float height) {
        throw new AssertionError();
    }

    public static void registerAll() {

    }
}
