package earth.terrarium.overcharged.block.generator;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.botarium.api.energy.*;
import earth.terrarium.botarium.api.menu.ExtraDataMenuProvider;
import earth.terrarium.overcharged.registry.OverchargedBlocks;
import earth.terrarium.overcharged.utils.PlatformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Random;

public class GeneratorBlockEntity extends BlockEntity implements WorldlyContainer, ExtraDataMenuProvider, EnergyBlock {

    public static final int MAX_WORK = 100;
    public static final int MAX_ENERGY = 1000000;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(12, ItemStack.EMPTY);
    private final GeneratorData data = new GeneratorData(this);
    private GeneratorEnergy energy;
    private int work;
    private int generating;
    private float efficiency;

    public GeneratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(OverchargedBlocks.COAL_GENERATOR_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public void tick() {
        if(this.getLevel() == null || this.getLevel().isClientSide()) return;
        if(generating == 0) {
            int slotsFilled = 0;
            int totalFuelCost = 0;
            for (int i = 0; i < 8; i++) {
                ItemStack item = this.getItem(i);
                ItemStack itemCopy = item.copy();
                if (!item.isEmpty()) {
                    slotsFilled++;
                    totalFuelCost += Math.max(1, Math.floorDiv(PlatformUtils.getBurnTime(item), 20));
                    item.shrink(1);
                    if(itemCopy.getItem().hasCraftingRemainingItem() && item.isEmpty()) {
                        inventory.set(i, itemCopy.getItem().getCraftingRemainingItem().getDefaultInstance());
                    }
                }
            }
            if (slotsFilled > 0) {
                for (int i = 8; i < 12; i++) {
                    ItemStack item = this.getItem(i);
                    if(!item.isEmpty()) slotsFilled++;
                }
                this.efficiency = .5f + (slotsFilled / 8.0f) * (slotsFilled / 8.0f) * .5f;
                this.generating = Math.max(1, (int) (efficiency * totalFuelCost));
            }
        }
        if (work < MAX_WORK && generating > 0 && this.getEnergyStorage().getStoredEnergy() < MAX_ENERGY) {
            this.getEnergyStorage().setEnergy(Math.min(this.getEnergyStorage().getStoredEnergy() + generating, MAX_ENERGY));
            work++;
        } else if (work >= MAX_WORK) {
            this.generating = 0;
            this.work = 0;
            this.efficiency = 0;
        }

        for (Direction dir : Direction.values()) {
            BlockEntity potentialEnergy = this.getLevel().getBlockEntity(this.getBlockPos().relative(dir).immutable());
            if(potentialEnergy == null) continue;
            if(EnergyHooks.isEnergyContainer(potentialEnergy, dir.getOpposite())) {
                PlatformEnergyManager blockEnergyManager = EnergyHooks.getBlockEnergyManager(potentialEnergy, dir.getOpposite());
                long inserted = blockEnergyManager.insert((int) this.getEnergyStorage().getStoredEnergy(), false);
                this.getEnergyStorage().extractEnergy(inserted, false);
            }
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.work = compoundTag.getInt("WorkTime");
        this.generating = compoundTag.getInt("EnergyGen");
        this.efficiency = compoundTag.getFloat("Efficiency");
        ContainerHelper.loadAllItems(compoundTag, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("WorkTime", work);
        compoundTag.putInt("EnergyGen", generating);
        compoundTag.putFloat("Efficiency", efficiency);
        ContainerHelper.saveAllItems(compoundTag, inventory);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public int[] getSlotsForFace(@NotNull Direction direction) {
        return new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return isFuelItem(itemStack) && i < 8;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return i < 8;
    }

    @Override
    public int getContainerSize() {
        return 12;
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
        return player.distanceToSqr(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()) <= 64;
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    public Component getDisplayName() {
        return this.getBlockState().getBlock().getName();
    }

    public static boolean isFuelItem(ItemStack stack) {
        return PlatformUtils.getBurnTime(stack) > 0;
    }

    public int getWork() {
        return work;
    }

    public int getGenerateAmount() {
        return generating;
    }

    public float getEfficiency() {
        return efficiency;
    }

    @Override
    public void writeExtraData(ServerPlayer player, FriendlyByteBuf buffer) {
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new GeneratorMenu(this, data, i, inventory);
    }

    @Override
    public UpdatingEnergyContainer getEnergyStorage() {
        if(this.energy == null) {
            this.energy = new GeneratorEnergy(this, 1000000);
        }
        return this.energy;
    }
}
