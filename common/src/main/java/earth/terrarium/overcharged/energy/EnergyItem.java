package earth.terrarium.overcharged.energy;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface EnergyItem {
    //Github copilot is too good.

    List<ToolMode> getToolModes();

    default int getEnergy(ItemStack stack) {
        return 0;
    }
    default void setEnergy(ItemStack stack, int energy) {

    }

    default InteractionResult hoeAction(UseOnContext useOnContext) {
        if (!this.hasEnoughEnergy(useOnContext.getItemInHand(), 200)) return InteractionResult.PASS;
        BlockPos blockPos;
        Level level = useOnContext.getLevel();
        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = TILLABLES.get(level.getBlockState(blockPos = useOnContext.getClickedPos()).getBlock());
        if (pair == null) {
            return InteractionResult.PASS;
        }
        Predicate<UseOnContext> predicate = pair.getFirst();
        Consumer<UseOnContext> consumer = pair.getSecond();
        if (predicate.test(useOnContext)) {
            Player player2 = useOnContext.getPlayer();
            level.playSound(player2, blockPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (!level.isClientSide) {
                consumer.accept(useOnContext);
                if (player2 != null) {
                    this.drainEnergy(useOnContext.getItemInHand(), 200);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    default int getMaxEnergy() {
        return 100000;
    }

    default void addEnergy(ItemStack stack, int energy) {
        this.setEnergy(stack, this.getEnergy(stack) + energy);
    }

    default void drainEnergy(ItemStack stack, int energy) {
        this.setEnergy(stack, this.getEnergy(stack) - energy);
    }

    default boolean hasEnergy(ItemStack stack) {
        return getEnergy(stack) >= 0;
    }

    default boolean hasEnoughEnergy(ItemStack stack, int energy) {
        return getEnergy(stack) >= energy;
    }

    default boolean isFull(ItemStack stack) {
        return getEnergy(stack) >= getMaxEnergy();
    }

    static boolean isEmpowered(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Empowered");
    }

    static void toggleEmpowered(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("Empowered", !isEmpowered(stack));
    }
}
