package earth.terrarium.overcharged.block;

import earth.terrarium.overcharged.data.SmashingRecipe;
import earth.terrarium.overcharged.registry.OverchargedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmashingAnvilBlock extends BaseEntityBlock {

    public SmashingAnvilBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new SmashingAnvilBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {

        if(player.getLevel().getBlockEntity(blockPos) instanceof SmashingAnvilBlockEntity blockEntity) {
            if(player.getItemInHand(interactionHand).is(OverchargedItems.HAMMER.get()) && !blockEntity.isEmpty()) {
                var recipe = SmashingRecipe.getRecipeForStack(blockEntity.getItem(0), level.getRecipeManager());
                if (recipe.isPresent()) {
                    if(blockEntity.hammerHit < recipe.get().hammerHits()) {
                        blockEntity.hammerHit++;
                    } else {
                        blockEntity.hammerHit = 0;
                        blockEntity.getItem(0).shrink(1);
                        blockEntity.update(Block.UPDATE_ALL);
                        double chance = level.random.nextDouble();
                        for (SmashingRecipe.WeightedItem output : recipe.get().outputs()) {
                            if(chance <= output.weight()) {
                                player.getInventory().placeItemBackInInventory(output.stack().copy());
                            }
                        }
                    }
                    player.playSound(SoundEvents.ANVIL_PLACE);
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.CRIT, blockPos.getX() + 0.5, blockPos.getY() + 0.65, blockPos.getZ() + 0.5, 5, 0, 0, 0, 0.01);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else if(blockEntity.isEmpty()) {
                var stack = player.getItemInHand(interactionHand);
                if(SmashingRecipe.getRecipeForStack(stack, level.getRecipeManager()).isPresent()) {
                    blockEntity.setItem(0, stack);
                    player.playSound(SoundEvents.END_PORTAL_FRAME_FILL);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
}
