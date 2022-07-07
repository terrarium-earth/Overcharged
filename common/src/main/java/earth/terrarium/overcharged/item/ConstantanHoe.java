package earth.terrarium.overcharged.item;

import earth.terrarium.overcharged.energy.AOEMode;
import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.energy.ToolMode;
import earth.terrarium.overcharged.energy.VeinMineMode;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConstantanHoe extends HoeItem implements EnergyItem {
    public ConstantanHoe(int i, float f, Properties properties) {
        super(ConstantanTier.INSTANCE, i, f, properties);
    }

    @Override
    public InteractionResult useOn(@NotNull UseOnContext useOnContext) {
        return ToolType.HOE.getFunction().apply(this, useOnContext);
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack itemStack, @NotNull BlockState blockState) {
        float speed = super.getDestroySpeed(itemStack, blockState);
        return this.hasEnoughEnergy(itemStack, 200) ? EnergyItem.isEmpowered(itemStack) ? speed * 1.2F : speed : 0;
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity, @NotNull LivingEntity livingEntity2) {
        return ToolUtils.hurtEnemy(this, itemStack, livingEntity, livingEntity2, 400);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull BlockState blockState, @NotNull BlockPos blockPos, @NotNull LivingEntity livingEntity) {
        return ToolUtils.mineBlock(this, itemStack, level, blockState, blockPos, 200);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack itemStack) {
        return hasEnoughEnergy(itemStack, 1);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        return (int)(((double) getEnergy(itemStack) / getMaxEnergy()) * 13);
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return 0xFFDB12;
    }
}
