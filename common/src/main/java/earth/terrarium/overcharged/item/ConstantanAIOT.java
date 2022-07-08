package earth.terrarium.overcharged.item;

import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConstantanAIOT extends DiggerItem implements EnergyItem {

    public ConstantanAIOT(float f, float g, Properties properties) {
        super(f, g, ConstantanTier.INSTANCE, ToolUtils.getAIOTBlockTag(), properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        return getToolType(useOnContext.getItemInHand()).getFunction().apply(this, useOnContext);
    }

    public ToolType getToolType(ItemStack stack) {
        String toolType = stack.getOrCreateTag().getString("ToolType");
        if(toolType.isEmpty()) return ToolType.HOE;
        ToolType type;
        try {
            type = ToolType.valueOf(toolType);
        } catch (IllegalArgumentException exception) {
            Overcharged.LOGGER.warn("Constantan AIOT item has invalid tooltype NBT");
            type = ToolType.HOE;
        }
        return type;
    }

    public ToolType changeToolType(Player player, ItemStack stack) {
        String toolType = stack.getOrCreateTag().getString("ToolType");
        if(toolType.isEmpty()) toolType = ToolType.HOE.toString();
        ToolType type;
        try {
            type = ToolType.valueOf(toolType);
            type = ToolType.values()[(type.ordinal() + 1) % ToolType.values().length];
        } catch (Exception exception) {
            type = ToolType.HOE;
            Overcharged.LOGGER.warn("Constantan AIOT item had an exception: ", exception);
        }
        player.displayClientMessage(Component.translatable("messages.overcharged.aiot_tool_type", type.toString()), true);
        return type;
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack itemStack, @NotNull BlockState blockState) {
        float speed = super.getDestroySpeed(itemStack, blockState);
        return this.hasEnoughEnergy(itemStack, 200) ? EnergyItem.isEmpowered(itemStack) ? speed * 1.2F : speed : 0;
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull BlockState blockState) {
        return true;
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
