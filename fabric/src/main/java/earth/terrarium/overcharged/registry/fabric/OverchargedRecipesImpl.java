package earth.terrarium.overcharged.registry.fabric;

import earth.terrarium.overcharged.Overcharged;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class OverchargedRecipesImpl {
    public static <R extends Recipe<?>, T extends RecipeType<R>> Supplier<T> registerRecipeType(String name, Supplier<T> recipe) {
        var registry = Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(Overcharged.MODID, name), recipe.get());
        return () -> registry;
    }

    public static <R extends Recipe<?>, T extends RecipeSerializer<R>> Supplier<T> registerRecipeSerializer(String name, Supplier<T> recipe) {
        var registry = Registry.register(Registry.RECIPE_SERIALIZER, new ResourceLocation(Overcharged.MODID, name), recipe.get());
        return () -> registry;
    }
}
