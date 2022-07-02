package earth.terrarium.overcharged.item;

import earth.terrarium.overcharged.energy.AOEMode;
import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.energy.ToolMode;
import earth.terrarium.overcharged.energy.VeinMineMode;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ConstantanAxe extends AxeItem implements EnergyItem {
    protected ConstantanAxe(float f, float g, Properties properties) {
        super(ConstantanTier.INSTANCE, f, g, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        var action = this.axeAction(useOnContext);
        ItemStack itemInHand = useOnContext.getItemInHand();
        if(EnergyItem.isEmpowered(itemInHand)) {
            getCurrentToolMode(itemInHand).useTool(useOnContext, this::axeAction);
        }
        return action;
    }

    private Optional<BlockState> getStripped(BlockState blockState) {
        return Optional.ofNullable(STRIPPABLES.get(blockState.getBlock())).map(block -> block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState.getValue(RotatedPillarBlock.AXIS)));
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity, @NotNull LivingEntity livingEntity2) {
        return ToolUtils.hurtEnemy(this, itemStack, livingEntity, livingEntity2, 200);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull BlockState blockState, @NotNull BlockPos blockPos, @NotNull LivingEntity livingEntity) {
        if(ToolUtils.mineBlock(this, itemStack, level, blockState, blockPos, 200)) {
            if(EnergyItem.isEmpowered(itemStack) && livingEntity instanceof Player player) {
                getCurrentToolMode(itemStack).onMineBlock(itemStack, level, ToolUtils.getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY), player);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<ToolMode> getEmpoweredToolModes() {
        return List.of(AOEMode.THREE_BY_THREE_AOE, AOEMode.FIVE_BY_FIVE_AOE, VeinMineMode.VEIN_MINING);
    }

    @Override
    public ToolMode defaultToolMode() {
        return null;
    }
}
