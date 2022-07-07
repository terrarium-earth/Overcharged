package earth.terrarium.overcharged.item;

import earth.terrarium.overcharged.energy.AOEMode;
import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.energy.ToolMode;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConstantanSword extends SwordItem implements EnergyItem {

    public ConstantanSword(int i, float f, Properties properties) {
        super(ConstantanTier.INSTANCE, i, f, properties);
    }

    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        float modifier = EnergyItem.isEmpowered(itemStack) ? 1.0F : 1.2F;
        if (this.hasEnoughEnergy(itemStack, 200)) {
            return 0;
        } else if (blockState.is(Blocks.COBWEB)) {
            return 15.0F * modifier;
        } else {
            Material material = blockState.getMaterial();
            return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && !blockState.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F * modifier;
        }

    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity, @NotNull LivingEntity livingEntity2) {
        return ToolUtils.hurtEnemy(this, itemStack, livingEntity, livingEntity2, 200);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull BlockState blockState, @NotNull BlockPos blockPos, @NotNull LivingEntity livingEntity) {
        return ToolUtils.mineBlock(this, itemStack, level, blockState, blockPos, 400);
    }

    @Override
    public List<ToolMode> getEmpoweredToolModes() {
        return List.of(AOEMode.THREE_BY_THREE_AOE, AOEMode.FIVE_BY_FIVE_AOE);
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
