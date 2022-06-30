package earth.terrarium.overcharged.energy;

import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class VeinMineMode implements ToolMode {
    public static final VeinMineMode VEIN_MINING = new VeinMineMode();
    @Override
    public Component getName() {
        return Component.translatable("overcharged.tooltip.vein_mine");
    }

    @Override
    public void onTick(ItemStack stack) {

    }

    @Override
    public void onUse(UseOnContext context, Consumer<UseOnContext> consumer) {

    }

    @Override
    public void onMineBlock(ItemStack stack, Level level, BlockHitResult hit, Player player) {
        if(stack.getItem() instanceof EnergyItem energyItem) {
            List<BlockPos> cachedPositions = new ArrayList<>();
            BlockState state = level.getBlockState(hit.getBlockPos());
            cachedPositions.add(hit.getBlockPos());
            int index = 0;
            int limit = 32;
            while (index < limit) {
                List<BlockPos> newCachedPositions = new ArrayList<>();
                for (BlockPos logPos : cachedPositions) {
                    AABB box = new AABB(logPos).inflate(1);
                    Set<BlockPos> logList = BlockPos.betweenClosedStream(box)
                            .filter(blockPos -> level.getBlockState(blockPos).is(state.getBlock()))
                            .collect(Collectors.toSet());
                    for (BlockPos blockPos : logList) {
                        if (index < limit && energyItem.hasEnoughEnergy(stack, 200)) {
                            newCachedPositions.add(blockPos);
                            ToolUtils.playerBreak(level, player, stack, blockPos);
                            energyItem.drainEnergy(stack, 200);
                            index++;
                        } else break;
                    }
                    newCachedPositions.addAll(logList);
                }
                if (newCachedPositions.isEmpty()) break;
                cachedPositions = newCachedPositions;
            }
        }
    }

    @Override
    public void onHitEntity(ItemStack stack, LivingEntity target, Player player) {
    }
}
