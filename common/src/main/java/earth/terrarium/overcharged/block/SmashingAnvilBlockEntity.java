package earth.terrarium.overcharged.block;

import earth.terrarium.overcharged.registry.OverchargedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmashingAnvilBlockEntity extends BlockEntity implements WorldlyContainer {
    private ItemStack item = ItemStack.EMPTY;
    public int hammerHit = 0;

    public SmashingAnvilBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(OverchargedBlocks.ANVIL_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public int[] getSlotsForFace(@NotNull Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return item.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return item;
    }

    @Override
    public ItemStack removeItem(int i, int blockUpdate) {
        ItemStack item = removeItemNoUpdate(i);
        update(blockUpdate);
        return item;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack item = getItem(i).copy();
        this.item = ItemStack.EMPTY;
        return item;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.item = itemStack.copy();
        update(Block.UPDATE_ALL);
    }

    public void update(int blockUpdate) {
        if (getLevel() != null) {
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), blockUpdate);
        }
        setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("item")) {
            item = ItemStack.of(compoundTag.getCompound("Item"));
        }
        hammerHit = compoundTag.getInt("HammerHit");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (!item.isEmpty()) {
            compoundTag.put("Item", item.save(new CompoundTag()));
        }
        compoundTag.putInt("HammerHit", hammerHit);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.item = ItemStack.EMPTY;
        update(Block.UPDATE_ALL);
    }
}
