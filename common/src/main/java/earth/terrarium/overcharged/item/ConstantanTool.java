package earth.terrarium.overcharged.item;

import earth.terrarium.botarium.api.energy.*;
import earth.terrarium.overcharged.energy.ConstantanItem;
import earth.terrarium.overcharged.utils.ToolType;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ConstantanTool extends DiggerItem implements ConstantanItem, EnergyItem {
    private final ToolType toolType;
    public ConstantanTool(ToolType toolType, TagKey<Block> tag, float i, float f, Properties properties) {
        super(i, f, ConstantanTier.INSTANCE, tag, properties);
        this.toolType = toolType;
    }

    @Override
    public InteractionResult useOn(@NotNull UseOnContext useOnContext) {
        return toolType.toolAction.apply(this, useOnContext);
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack itemStack, @NotNull BlockState blockState) {
        float speed = super.getDestroySpeed(itemStack, blockState);
        PlatformEnergyManager energyStorage = EnergyHooks.getItemHandler(itemStack);
        return energyStorage.getStoredEnergy() > 0 ? ToolUtils.isEmpowered(itemStack) ? speed * 1.2F : speed : 0;
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity, @NotNull LivingEntity livingEntity2) {
        return ToolUtils.hurtEnemy(itemStack, livingEntity, livingEntity2, 400);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull BlockState blockState, @NotNull BlockPos blockPos, @NotNull LivingEntity livingEntity) {
        return ToolUtils.mineBlock(itemStack, level, blockState, blockPos, 200);
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
    public EnergyContainer getEnergyStorage(ItemStack object) {
        return new ItemEnergyContainer(object, 800000);
    }
}
