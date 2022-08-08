package earth.terrarium.overcharged.registry.forge;

import earth.terrarium.overcharged.Overcharged;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class OverchargedEntitiesImpl {
    //deferred registration of entities
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Overcharged.MODID);

    public static <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> factory, MobCategory group, float width, float height) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(factory, group)
                .sized(width, height)
                .build(name));
    }
}
