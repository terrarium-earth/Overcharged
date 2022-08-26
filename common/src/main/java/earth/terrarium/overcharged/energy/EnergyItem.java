package earth.terrarium.overcharged.energy;

import earth.terrarium.botarium.api.AbstractEnergy;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public interface EnergyItem extends AbstractEnergy {
    //GitHub copilot is too good.

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
