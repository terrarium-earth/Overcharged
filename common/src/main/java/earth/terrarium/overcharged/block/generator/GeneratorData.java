package earth.terrarium.overcharged.block.generator;

import net.minecraft.world.inventory.ContainerData;

public class GeneratorData implements ContainerData {
    GeneratorBlockEntity generator;
    public static final int ENERGY_LEVEL = 0;
    public static final int WORK = 1;
    public static final int GENERATING = 2;

    public GeneratorData(GeneratorBlockEntity energyBlock) {
        this.generator = energyBlock;
    }

    @Override
    public int get(int i) {
        return switch (i) {
            case ENERGY_LEVEL -> (int) generator.getEnergyStorage().getStoredEnergy();
            case WORK -> generator.getWork();
            case GENERATING -> generator.getGenerateAmount();
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }

    @Override
    public void set(int i, int j) {
    }

    @Override
    public int getCount() {
        return 3;
    }
}
