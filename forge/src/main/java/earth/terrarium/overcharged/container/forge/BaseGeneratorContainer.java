package earth.terrarium.overcharged.container.forge;

import earth.terrarium.overcharged.blocks.forge.BaseGeneratorBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseGeneratorContainer<T extends BaseGeneratorBlockEntity> extends AbstractContainerMenu {

    private final T generator;

    public BaseGeneratorContainer(@Nullable MenuType<?> menuType, int id, Inventory inventory, T generator) {
        super(menuType, id);
        this.generator = generator;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        return handleShiftClick(this, player, index);
    }

    public T getGenerator() {
        return generator;
    }

    public static ItemStack handleShiftClick(AbstractContainerMenu container, Player player, int slotIndex) {
        List<Slot> slots = container.slots;
        Slot sourceSlot = slots.get(slotIndex);
        ItemStack inputStack = sourceSlot.getItem();
        if (inputStack == ItemStack.EMPTY) {
            return ItemStack.EMPTY;
        }

        boolean sourceIsPlayer = sourceSlot.container == player.getInventory();
        ItemStack copy = inputStack.copy();

        if (sourceIsPlayer) {
            if (failsMergeStack(player.getInventory(), false, sourceSlot, slots, false)) {
                return ItemStack.EMPTY;
            } else {
                return copy;
            }
        } else {
            boolean isMachineOutput = !sourceSlot.mayPickup(player);
            if (failsMergeStack(player.getInventory(), true, sourceSlot, slots, !isMachineOutput)) {
                return ItemStack.EMPTY;
            } else {
                return copy;
            }
        }
    }

    private static boolean failsMergeStack(Inventory playerInv, boolean mergeIntoPlayer, Slot sourceSlot, List<Slot> slots, boolean reverse) {
        ItemStack sourceStack = sourceSlot.getItem();
        int originalSize = sourceStack.getCount();
        int len = slots.size();
        int idx;

        if (sourceStack.isStackable()) {
            idx = reverse ? len - 1 : 0;

            while (sourceStack.getCount() > 0 && (reverse ? idx >= 0 : idx < len)) {
                Slot targetSlot = slots.get(idx);
                if ((targetSlot.container == playerInv) == mergeIntoPlayer) {
                    ItemStack target = targetSlot.getItem();
                    if (ItemStack.isSame(sourceStack, target)) {
                        int targetMax = Math.min(targetSlot.getMaxStackSize(), target.getMaxStackSize());
                        int toTransfer = Math.min(sourceStack.getCount(), targetMax - target.getCount());
                        if (toTransfer > 0) {
                            target.grow(toTransfer);
                            sourceSlot.remove(toTransfer);
                            targetSlot.setChanged();
                        }
                    }
                }

                if (reverse) {
                    idx--;
                } else {
                    idx++;
                }
            }
            if (sourceStack.getCount() == 0) {
                sourceSlot.set(ItemStack.EMPTY);
                return false;
            }
        }

        idx = reverse ? len - 1 : 0;
        while (reverse ? idx >= 0 : idx < len) {
            Slot targetSlot = slots.get(idx);
            if ((targetSlot.container == playerInv) == mergeIntoPlayer
                    && !targetSlot.hasItem() && targetSlot.mayPlace(sourceStack)) {
                targetSlot.set(sourceStack);
                sourceSlot.set(ItemStack.EMPTY);
                return false;
            }

            if (reverse) {
                idx--;
            } else {
                idx++;
            }
        }

        if (sourceStack.getCount() != originalSize) {
            sourceSlot.setChanged();
            return false;
        }

        return true;
    }
}