package earth.terrarium.overcharged.block.generator;

import earth.terrarium.overcharged.registry.OverchargedMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GeneratorMenu extends AbstractContainerMenu {
    private final Container container;
    public final ContainerData data;

    protected GeneratorMenu(Container container, ContainerData data, int i, Inventory inventory) {
        super(OverchargedMenus.GENERATOR_MENU.get(), i);
        this.container = container;
        this.data = data;

        int slot = 0;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 2; x++) {
                this.addSlot(new FuelSlot(container, slot, 8 + x * 18, 20 + y * 18));
                slot++;
            }
        }

        for (int y = 0; y < 4; y++) {
            this.addSlot(new Slot(container, y + 8, 152, 20 + y * 18));
        }
        addPlayerInvSlots(inventory);
        addDataSlots(data);
    }

    public GeneratorMenu(int i, Inventory inventory, FriendlyByteBuf byteBuf) {
        this(new SimpleContainer(12), new SimpleContainerData(4), i, inventory);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemStack = slotItem.copy();

            if (index < 12) {
                if (!this.moveItemStackTo(slotItem, 12, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotItem, 0, 12, false)) {
                return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.container.stillValid(player);
    }

    protected void addPlayerInvSlots(Inventory inventory) {
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 108 + y * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 108 + 58));
        }
    }
}
