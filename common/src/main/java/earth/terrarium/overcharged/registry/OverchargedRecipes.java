package earth.terrarium.overcharged.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.overcharged.data.CodecRecipeSerializer;
import earth.terrarium.overcharged.data.SmashingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class OverchargedRecipes {

    public static Supplier<RecipeType<SmashingRecipe>> SMASHING_RECIPE = registerRecipeType("smashing", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "smashing";
        }
    });

    public static Supplier<RecipeSerializer<SmashingRecipe>> SMASHING_RECIPE_SERIALIZER = registerRecipeSerializer("smashing", () -> new CodecRecipeSerializer<>(SMASHING_RECIPE.get(), SmashingRecipe::codec));

    @ExpectPlatform
    public static <R extends Recipe<?>, T extends RecipeType<R>> Supplier<T> registerRecipeType(String name, Supplier<T> recipe) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <R extends Recipe<?>, T extends RecipeSerializer<R>> Supplier<T> registerRecipeSerializer(String name, Supplier<T> recipe) {
        throw new AssertionError();
    }

    public static void registerAll() {

    }
}
