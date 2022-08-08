package earth.terrarium.overcharged.blocks.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseGeneratorBlockEntity extends BlockEntity implements MenuProvider {

    protected static final Direction[] DIRECTIONS = Direction.values();

    private final BlockEnergyStorage energy;
    private final LazyOptional<BlockEnergyStorage> energyHandler;

    private ItemStackHandler inventory;
    private LazyOptional<IItemHandler> inventoryHandler;

    private FluidTank tank;
    private LazyOptional<FluidTank> tankHandler;

    private final int maxTransfer;
    private int energyGen;

    public BaseGeneratorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int energyCapacity, int energyGen, int maxTransfer) {
        super(type, pos, state);
        this.energy = new BlockEnergyStorage(energyCapacity, 0, Integer.MAX_VALUE);
        this.energyHandler = LazyOptional.of(() -> energy);
        this.energyGen = energyGen;
        this.maxTransfer = maxTransfer;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (energy != null)
            tag.put("energy", energy.serializeNBT());

        if (inventory != null)
            tag.put("inventory", inventory.serializeNBT());

        if (tank != null)
            tag.put("fluid", tank.writeToNBT(new CompoundTag()));

        tag.putInt("energyGen", energyGen);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("energy"))
            energy.deserializeNBT(tag.getCompound("energy"));

        if (tag.contains("inventory"))
            inventory.deserializeNBT(tag.getCompound("inventory"));

        if (tag.contains("fluid"))
            tank.readFromNBT(tag.getCompound("fluid"));

        energyGen = tag.getInt("energyGen");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BaseGeneratorBlockEntity blockEntity) {
        if (level == null) return;
        blockEntity.tick();
    }

    //Simulate the server actions on the client to reduce amount of sync packets needed
    public static void tickClient(Level level, BlockPos pos, BlockState state, BaseGeneratorBlockEntity blockEntity) {
        tick(level, pos, state, blockEntity);
    }

    protected void tick() {
        transferEnergy();
    }

    public boolean canGenerate() {
        return level != null &&
                !level.hasNeighborSignal(getBlockPos()) &&
                getEnergy().getMaxEnergyStored() - getEnergy().getEnergyStored() >= energyGen;
    }

    public void generateEnergy() {
        this.generateEnergy(getEnergyGen());
    }

    public void generateEnergy(int energyAmount) {
        if (getEnergy() == null) return;
        getEnergy().receiveInternal(energyAmount, false);
    }

    public void transferEnergy() {
        if (level == null || getEnergy() == null) return;

        for (Direction direction : DIRECTIONS) {
            BlockPos offset = getBlockPos().relative(direction);
            BlockEntity blockEntity = level.getBlockEntity(offset);
            if (blockEntity == null) continue;

            blockEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(energyStorage -> {
                int maxExtract = getEnergy().extractEnergy(maxTransfer, true);
                int extracted = energyStorage.receiveEnergy(maxExtract, false);
                getEnergy().extractEnergy(extracted, false);
            });
        }
    }

    public void setEnergyGen(int energyGen) {
        this.energyGen = energyGen;
        this.setChanged();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        //Energy can never be null
        if (cap == CapabilityEnergy.ENERGY)
            return energyHandler.cast();

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && inventory != null)
            return inventoryHandler.cast();

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && tank != null)
            return tankHandler.cast();

        return super.getCapability(cap, side);
    }

    public void createInventory(int slots) {
        createInventory(new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                BaseGeneratorBlockEntity.this.setChanged();
            }
        });
    }

    public void createInventory(ItemStackHandler itemStackHandler) {
        this.inventory = itemStackHandler;
        this.inventoryHandler = LazyOptional.of(() -> inventory);
    }

    public void createFluid(int capacity, FluidStack filter) {
        tank = new FluidTank(capacity, filter::isFluidEqual);
        tankHandler = LazyOptional.of(() -> tank);
        this.setChanged();
    }

    @NotNull
    @Override
    public abstract Component getDisplayName();

    @Nullable
    @Override
    public abstract AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player);

    public boolean hasMenu() {
        return true;
    }

    public abstract Component getDisplayText();

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public BlockEnergyStorage getEnergy() {
        return energy;
    }

    public FluidTank getTank() {
        return tank;
    }

    public int getEnergyGen() {
        return energyGen;
    }

    public int getMaxEnergyTransfer() {
        return maxTransfer;
    }
}