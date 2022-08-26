package earth.terrarium.overcharged.block;

import earth.terrarium.botarium.api.AbstractEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BaseEnergyBlockEntity extends BlockEntity implements AbstractEnergy {
    private final long maxEnergyCapacity;
    private long storedEnergy;

    public BaseEnergyBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, long maxEnergyCapacity) {
        super(blockEntityType, blockPos, blockState);
        this.maxEnergyCapacity = maxEnergyCapacity;
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        this.storedEnergy = compoundTag.getLong("Energy");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        compoundTag.putLong("Energy", this.storedEnergy);
    }

    @Override
    public long insertEnergy(long maxAmount) {
        long moved = Math.min(Math.max(0, maxAmount), this.getMaxCapacity() - this.getEnergyLevel());
        this.storedEnergy += moved;
        return moved;
    }

    @Override
    public long extractEnergy(long maxAmount) {
        long moved = Math.min(Math.max(0, maxAmount), this.getEnergyLevel());
        this.storedEnergy -= moved;
        return moved;
    }

    @Override
    public long getEnergyLevel() {
        return this.storedEnergy;
    }

    @Override
    public long getMaxCapacity() {
        return maxEnergyCapacity;
    }
}
