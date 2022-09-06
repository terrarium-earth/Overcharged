package earth.terrarium.overcharged.block.generator;

import earth.terrarium.botarium.api.energy.BlockEnergyContainer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class GeneratorEnergy extends BlockEnergyContainer {
    public GeneratorEnergy(BlockEntity entity, int energyCapacity) {
        super(entity, energyCapacity);
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        return 0;
    }

    @Override
    public long maxInsert() {
        return 0;
    }

    @Override
    public boolean allowsInsertion() {
        return false;
    }
}
