package earth.terrarium.overcharged.blocks.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeneratorBlockEntity extends BaseGeneratorBlockEntity {

    public GeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int energyCapacity, int energyGen, int maxTransfer) {
        super(type, pos, state, energyCapacity, energyGen, maxTransfer);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return null;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return null;
    }

    @Override
    public Component getDisplayText() {
        return null;
    }
}
