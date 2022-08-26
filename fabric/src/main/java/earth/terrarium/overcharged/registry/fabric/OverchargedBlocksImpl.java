package earth.terrarium.overcharged.registry.fabric;

import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.registry.OverchargedBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class OverchargedBlocksImpl {

    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(OverchargedBlocks.BlockEntityFactory<E> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(factory::create, blocks).build(null);
    }
}
