package earth.terrarium.overcharged.block.generator;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.botarium.api.energy.EnergyManager;
import earth.terrarium.overcharged.utils.PlatformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeneratorBlockEntity extends BaseEnergyBlockEntity implements WorldlyContainer, MenuProvider {

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(9, ItemStack.EMPTY);
    private int work;
    public static final int MAX_WORK = 100;

    public GeneratorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState, 1000000);
    }

    @Override
    public int[] getSlotsForFace(@NotNull Direction direction) {
        return direction == Direction.UP ? new int[]{0, 1, 2, 3, 4, 5, 6, 7} : new int[]{8};
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        if(direction == Direction.UP) {
            return isFuelItem(itemStack) && (i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6 || i == 7);
        } else {
            return EnergyManager.isEnergyItem(itemStack) && i == 8;
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int i, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        if(direction == Direction.UP) {
            return i == 0 || i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6 || i == 7;
        } else {
            return i == 8;
        }
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return inventory.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(inventory, i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(inventory, i);
    }

    @Override
    public void setItem(int i, @NotNull ItemStack itemStack) {
        inventory.set(i, itemStack);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    public Component getDisplayName() {
        return this.getBlockState().getBlock().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return null;
    }

    @ExpectPlatform
    public static boolean isEnergyItem(ItemStack stack) {
        return false;
    }

    public static boolean isFuelItem(ItemStack stack) {
        return PlatformUtils.getBurnTime(stack) > 0;
    }

    public int getWork() {
        return work;
    }

    public int getGenerateAmount() {
        return 0;
    }
}
