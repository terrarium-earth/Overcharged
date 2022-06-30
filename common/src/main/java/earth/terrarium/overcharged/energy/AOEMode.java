package earth.terrarium.overcharged.energy;

import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.tools.Tool;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class AOEMode implements ToolMode {
    public static final AOEMode THREE_BY_THREE_AOE = new AOEMode(3);
    public static final AOEMode FIVE_BY_FIVE_AOE = new AOEMode(5);

    public final int radius;

    public AOEMode(int radius) {
        this.radius = radius;
    }

    @Override
    public Component getName() {
        return Component.translatable("overcharged.tooltip.area_break", radius, radius);
    }

    @Override
    public void onTick(ItemStack stack) {
    }

    @Override
    public void onUse(UseOnContext context, Consumer<UseOnContext> consumer) {
        if(EnergyItem.isEmpowered(context.getItemInHand())) {
            AABB box = new AABB(context.getClickedPos()).inflate(radius);
            BlockPos.betweenClosedStream(box).map(blockPos -> new BlockHitResult(context.getClickLocation(), context.getClickedFace(), blockPos.immutable(), false)).map(blockHitResult -> new UseOnContext(Objects.requireNonNull(context.getPlayer()), context.getHand(), blockHitResult)).forEach(consumer);
        }
    }

    @Override
    public void onMineBlock(ItemStack stack, Level level, BlockHitResult hit, Player player) {
        if (!level.isClientSide() && stack.getItem() instanceof EnergyItem energyItem && stack.getItem() instanceof DiggerItem diggerItem) {
            BlockHitResult hitResult = ToolUtils.getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                Direction direction = hitResult.getDirection();
                BlockPos anchorPoint = direction == Direction.DOWN || direction == Direction.UP ? hit.getBlockPos() : hit.getBlockPos().offset(0, radius - 1, 0);
                BoundingBox box = switch (direction) {
                    case UP, DOWN -> BoundingBox.fromCorners(anchorPoint.offset(radius, 0, radius), anchorPoint.offset(-radius, 0, -radius));
                    case EAST, WEST -> BoundingBox.fromCorners(anchorPoint.offset(0, radius, radius), anchorPoint.offset(0, -radius, -radius));
                    case NORTH, SOUTH -> BoundingBox.fromCorners(anchorPoint.offset(radius, radius, 0), anchorPoint.offset(-radius, -radius, 0));
                };

                List<BlockPos> positions = BlockPos.betweenClosedStream(box).filter(blockPos1 -> diggerItem.isCorrectToolForDrops(level.getBlockState(blockPos1)) && level.getBlockState(blockPos1).getDestroySpeed(level, blockPos1) > 0).toList();
                for (BlockPos position : positions) {
                    if(energyItem.hasEnoughEnergy(stack, 200)) {
                        energyItem.drainEnergy(stack, 200);
                        ToolUtils.playerBreak(level, player, stack, position);
                    } else break;
                }
            }
        }
    }

    @Override
    public void onHitEntity(ItemStack stack, LivingEntity target, Player player) {
        if (stack.getItem() instanceof EnergyItem energyItem) {
            AABB box = target.getBoundingBox().inflate(radius, Math.min(1, radius * 0.5), radius);
            List<LivingEntity> livingEntityList = player.getLevel().getEntities(ToolUtils.LIVING_ENTITY_TEST, box, livingEntity -> livingEntity != player && livingEntity != target);
            Iterator<LivingEntity> iterator = livingEntityList.iterator();
            while (iterator.hasNext() && energyItem.hasEnoughEnergy(stack, 200)) {
                LivingEntity entity = iterator.next();
                player.attack(entity);
                energyItem.drainEnergy(stack, 200);
            }
        }
    }
}
