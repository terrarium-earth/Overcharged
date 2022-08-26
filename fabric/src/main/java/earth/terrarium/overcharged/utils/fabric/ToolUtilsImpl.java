package earth.terrarium.overcharged.utils.fabric;

import earth.terrarium.overcharged.mixin.AxeItemAccessor;
import earth.terrarium.overcharged.mixin.ShovelItemAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class ToolUtilsImpl {
    public static BlockState getToolModifiedState(BlockState state, UseOnContext context, String toolAction) {
        return switch (toolAction) {
            case "axe_strip" -> AxeItemAccessor.invokeGetStripped(state).orElse(null);
            case "axe_scrape" -> WeatheringCopper.getPrevious(state).orElse(null);
            case "axe_wax_off" ->
                    Optional.ofNullable(HoneycombItem.WAX_OFF_BY_BLOCK.get().get(state.getBlock())).map(block -> block.withPropertiesOf(state)).orElse(null);
            case "shovel_flatten" -> ShovelItemAccessor.getFlatennables().get(state.getBlock());
            case "till" -> till(state, context);
            default -> null;
        };
    }

    private static BlockState till(BlockState state, UseOnContext context) {
        Block block2 = state.getBlock();
        if (block2 == Blocks.ROOTED_DIRT) {
            return Blocks.DIRT.defaultBlockState();
        }
        if ((block2 == Blocks.GRASS_BLOCK || block2 == Blocks.DIRT_PATH || block2 == Blocks.DIRT || block2 == Blocks.COARSE_DIRT) && context.getLevel().getBlockState(context.getClickedPos().above()).isAir()) {
            return block2 == Blocks.COARSE_DIRT ? Blocks.DIRT.defaultBlockState() : Blocks.FARMLAND.defaultBlockState();
        }
        return null;
    }
}

