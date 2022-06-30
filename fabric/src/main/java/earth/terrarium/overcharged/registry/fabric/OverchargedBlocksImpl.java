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
    public static <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String name, Supplier<T> blockEntity) {
        var register = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Overcharged.MODID, name), blockEntity.get());
        return () -> register;
    }

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        var register = Registry.register(Registry.BLOCK, new ResourceLocation(Overcharged.MODID, name), block.get());
        return () -> register;
    }

    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(OverchargedBlocks.BlockEntityFactory<E> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(factory::create, blocks).build(null);
    }
}
