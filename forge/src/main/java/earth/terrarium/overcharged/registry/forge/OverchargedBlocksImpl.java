package earth.terrarium.overcharged.registry.forge;

import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.registry.OverchargedBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class OverchargedBlocksImpl {

    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(OverchargedBlocks.BlockEntityFactory<E> factory, Block... blocks) {
        return BlockEntityType.Builder.of(factory::create, blocks).build(null);
    }
}
