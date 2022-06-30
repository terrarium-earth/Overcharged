package earth.terrarium.overcharged.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.registry.OverchargedRecipes;
import earth.terrarium.overcharged.utils.CodecUtils;
import earth.terrarium.overcharged.utils.ItemStackCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.Optional;

public record SmashingRecipe(ResourceLocation id, Ingredient input, int hammerHits, List<WeightedItem> outputs) implements SyncedData {
    public static Codec<SmashingRecipe> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                CodecUtils.INGREDIENT_CODEC.fieldOf("input").forGetter(SmashingRecipe::input),
                Codec.INT.fieldOf("hammerHits").forGetter(SmashingRecipe::hammerHits),
                WeightedItem.CODEC.listOf().fieldOf("outputs").forGetter(SmashingRecipe::outputs)
        ).apply(instance, SmashingRecipe::new));
    }

    @Override
    public ResourceLocation getId() {
        return id();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return OverchargedRecipes.SMASHING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return OverchargedRecipes.SMASHING_RECIPE.get();
    }

    public static Optional<SmashingRecipe> getRecipeForStack(ItemStack stack, RecipeManager manager) {
        return manager.getAllRecipesFor(OverchargedRecipes.SMASHING_RECIPE.get()).stream().filter(recipe -> recipe.input.test(stack)).findFirst();
    }

    public record WeightedItem(ItemStack stack, double weight) {
        public static final Codec<WeightedItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStackCodec.CODEC.fieldOf("item").forGetter(WeightedItem::stack),
                Codec.DOUBLE.fieldOf("dropChance").orElse(1.0).forGetter(WeightedItem::weight)
        ).apply(instance, WeightedItem::new));
    }
}
