package earth.terrarium.overcharged.block.generator;

import earth.terrarium.botarium.api.energy.BlockEnergyContainer;
import earth.terrarium.botarium.api.energy.EnergyContainer;
import earth.terrarium.botarium.api.energy.EnergyHoldable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BaseEnergyBlockEntity extends BlockEntity implements EnergyHoldable {
    private final int maxEnergyCapacity;
    private BlockEnergyContainer energyContainer;

    public BaseEnergyBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int maxEnergyCapacity) {
        super(blockEntityType, blockPos, blockState);
        this.maxEnergyCapacity = maxEnergyCapacity;
    }

    @Override
    public EnergyContainer getEnergyStorage() {
        if (energyContainer == null) {
            energyContainer = new BlockEnergyContainer(this, maxEnergyCapacity);
        }
        return energyContainer;
    }
}
