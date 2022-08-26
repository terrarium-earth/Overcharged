package earth.terrarium.overcharged.energy;

import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiFunction;

public interface EnergyItem {
    //GitHub copilot is too good.

    default int getEnergy(ItemStack stack) {
        return 0;
    }

    default void setEnergy(ItemStack stack, int energy) {}
    default int getMaxEnergy() {
        return 800000;
    }

    default void drainEnergy(ItemStack stack, int energy) {
        this.setEnergy(stack, this.getEnergy(stack) - energy);
    }

    default boolean hasEnoughEnergy(ItemStack stack, int energy) {
        return getEnergy(stack) >= energy;
    }

    default List<ToolMode> getEmpoweredToolModes() {
        return List.of(AOEMode.THREE_BY_THREE_AOE, AOEMode.FIVE_BY_FIVE_AOE, VeinMineMode.VEIN_MINING);
    }

    @Nullable
    default ToolMode getCurrentToolMode(ItemStack stack) {
        if(ToolUtils.isEmpowered(stack)) {
            return getEmpoweredToolModes().get(stack.getOrCreateTag().getInt("ToolMode") % getEmpoweredToolModes().size());
        } else return null;
    }

    default void changeToolMode(Player player, ItemStack stack, int index) {
        if(ToolUtils.isEmpowered(stack)) {
            stack.getOrCreateTag().putInt("ToolMode", index % this.getEmpoweredToolModes().size());
            ToolMode currentToolMode = getCurrentToolMode(stack);
            if(currentToolMode != null) {
                player.displayClientMessage(Component.translatable("messages.overcharged.constantan_change_mode", currentToolMode.getName()), true);
            }
        }
    }
}
