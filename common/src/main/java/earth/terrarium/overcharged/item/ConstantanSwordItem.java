package earth.terrarium.overcharged.item;

import earth.terrarium.botarium.api.energy.*;
import earth.terrarium.overcharged.energy.ConstantanItem;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ConstantanSwordItem extends SwordItem implements ConstantanItem, EnergyItem {
    public ConstantanSwordItem(int i, float f, Properties properties) {
        super(ConstantanTier.INSTANCE, i, f, properties);
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack itemStack, @NotNull BlockState blockState) {
        float speed = super.getDestroySpeed(itemStack, blockState);
        PlatformEnergyManager energyStorage = EnergyHooks.getItemHandler(itemStack);
        return energyStorage.getStoredEnergy() >= 400 ? ToolUtils.isEmpowered(itemStack) ? speed * 1.2F : speed : 0;
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity, @NotNull LivingEntity livingEntity2) {
        return ToolUtils.hurtEnemy(itemStack, livingEntity, livingEntity2, 200);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack itemStack, Level level, @NotNull BlockState blockState, @NotNull BlockPos blockPos, @NotNull LivingEntity livingEntity) {
        return ToolUtils.mineBlock(itemStack, level, blockState, blockPos, 400);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack itemStack) {
        return ToolUtils.isBarVisible(itemStack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        return ToolUtils.energyBar(itemStack);
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return 0xFFDB12;
    }

    @Override
    public EnergyContainer getEnergyStorage(ItemStack stack) {
        return new ItemEnergyContainer(stack, 800000);
    }
}
