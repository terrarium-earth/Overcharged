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
    protected ConstantanHoe(Tier tier, int i, float f, Properties properties) {
        super(tier, i, f, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        ItemStack itemInHand = useOnContext.getItemInHand();
        if(this.hasEnoughEnergy(itemInHand, 200)) {
            var action = this.hoeAction(useOnContext);
            if (EnergyItem.isEmpowered(itemInHand)) {
                getCurrentToolMode(itemInHand).useTool(useOnContext, this::hoeAction);
            }
            return action;
        }
        return InteractionResult.PASS;
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
    public List<ToolMode> getEmpoweredToolModes() {
        return List.of(AOEMode.THREE_BY_THREE_AOE, AOEMode.FIVE_BY_FIVE_AOE, VeinMineMode.VEIN_MINING);
    }

    @Override
    public ToolMode defaultToolMode() {
        return null;
    }
}
