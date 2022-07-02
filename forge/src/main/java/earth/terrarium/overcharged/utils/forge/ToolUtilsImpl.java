package earth.terrarium.overcharged.utils.forge;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

public class ToolUtilsImpl {
    public static BlockState getToolModifiedState(BlockState state, UseOnContext context, String toolAction) {
        return state.getToolModifiedState(context, ToolAction.get(toolAction), false);
    }
}
