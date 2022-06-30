package earth.terrarium.overcharged.registry.fabric;

import earth.terrarium.overcharged.Overcharged;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class OverchargedEntitiesImpl {

    public static <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> factory, MobCategory group, float width, float height) {
        var register = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Overcharged.MODID, name), FabricEntityTypeBuilder.create(group, factory).dimensions(EntityDimensions.scalable(width, height)).build());
        return () -> register;
    }
}
