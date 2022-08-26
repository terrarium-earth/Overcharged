package earth.terrarium.overcharged.registry;

import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeSerializer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.botarium.api.RegistryHolder;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.data.SmashingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class OverchargedRecipes {
    public static final RegistryHolder<RecipeType<?>> RECIPE_TYPES = new RegistryHolder<>(Registry.RECIPE_TYPE, Overcharged.MODID);
    public static final RegistryHolder<RecipeSerializer<?>> RECIPE_SERIALIZERS = new RegistryHolder<>(Registry.RECIPE_SERIALIZER, Overcharged.MODID);

    public static Supplier<RecipeType<SmashingRecipe>> SMASHING_RECIPE = RECIPE_TYPES.register("smashing", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "smashing";
        }
    });

    public static Supplier<RecipeSerializer<SmashingRecipe>> SMASHING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("smashing", () -> new CodecRecipeSerializer<>(SMASHING_RECIPE.get(), SmashingRecipe::codec));

    public static void registerAll() {
        RECIPE_TYPES.initialize();
        RECIPE_SERIALIZERS.initialize();
    }
}
