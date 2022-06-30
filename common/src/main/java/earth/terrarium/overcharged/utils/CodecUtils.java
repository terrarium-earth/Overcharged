package earth.terrarium.overcharged.utils;

import com.google.gson.JsonElement;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
/**
 * This class was largely inspired by or taken from the Resourceful Bees repository with
 * the expressed permission from one of their developers.
 *
 * @author Team Resourceful
 */
public class CodecUtils {
    public static final Codec<Ingredient> INGREDIENT_CODEC = Codec.PASSTHROUGH.comapFlatMap(CodecUtils::decodeIngredient, CodecUtils::encodeIngredient);

    private static DataResult<Ingredient> decodeIngredient(Dynamic<?> dynamic) {
        return DataResult.success(Ingredient.fromJson(dynamic.convert(JsonOps.INSTANCE).getValue()));
    }

    private static Dynamic<JsonElement> encodeIngredient(Ingredient ingredient) {
        return new Dynamic<>(JsonOps.INSTANCE, ingredient.toJson()).convert(JsonOps.COMPRESSED);
    }

    public static <T extends Weighted> Codec<RandomCollection<T>> randomCollectionCodec(Codec<T> codec) {
        return createSetCodec(codec).comapFlatMap(CodecUtils::convertToRandomCollection, CodecUtils::convertToSet);
    }

    private static <T extends Weighted> DataResult<RandomCollection<T>> convertToRandomCollection(Set<T> set) {
        return DataResult.success(set.stream().collect(RandomCollection.getCollector(T::getWeight)));
    }

    public static <T> Codec<Set<T>> createSetCodec(Codec<T> codec) {
        return codec.listOf().xmap(HashSet::new, ArrayList::new);
    }

    private static <T extends Weighted> Set<T> convertToSet(RandomCollection<T> randomCollection) {
        return new HashSet<>(randomCollection.getMap().values());
    }

    public interface Weighted {
        double getWeight();
    }
}
