package earth.terrarium.overcharged.registry;

import earth.terrarium.botarium.api.registry.RegistryHolder;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.entity.PoweredArrow;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class OverchargedEntities {
    public static final RegistryHolder<EntityType<?>> ENTITIES = new RegistryHolder<>(Registry.ENTITY_TYPE, Overcharged.MODID);

    public static final Supplier<EntityType<PoweredArrow>> POWERED_ARROW = ENTITIES.register("powered_arrow", () -> EntityType.Builder.of(PoweredArrow::new, MobCategory.MISC).build("powered_arrow"));
    public static void init() {}

    public static void registerAll() {
        ENTITIES.initialize();
    }
}
