package earth.terrarium.overcharged.mixin;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(AxeItem.class)
public interface AxeItemAccessor {

    @Invoker("getStripped")
    static Optional<BlockState> invokeGetStripped(BlockState state) {
        throw new AssertionError();
    }
}
