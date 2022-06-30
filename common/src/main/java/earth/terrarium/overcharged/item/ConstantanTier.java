package earth.terrarium.overcharged.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class ConstantanTier implements Tier {

    public static final ConstantanTier INSTANCE = new ConstantanTier();

    @Override
    public int getUses() {
        return 0;
    }

    @Override
    public float getSpeed() {
        return 15;
    }

    @Override
    public float getAttackDamageBonus() {
        return 3.0F;
    }

    @Override
    public int getLevel() {
        return 4;
    }

    @Override
    public int getEnchantmentValue() {
        return 10;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }
}
