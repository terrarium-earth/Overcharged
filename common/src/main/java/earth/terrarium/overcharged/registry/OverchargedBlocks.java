package earth.terrarium.overcharged.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.overcharged.block.SmashingAnvilBlock;
import earth.terrarium.overcharged.block.SmashingAnvilBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class OverchargedBlocks {


    public static Supplier<SmashingAnvilBlock> ANVIL_BLOCK = registerBlockWithItem("smashing_anvil", () -> new SmashingAnvilBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static Supplier<BlockEntityType<SmashingAnvilBlockEntity>> ANVIL_BLOCK_ENTITY = registerBlockEntity("smashing_anvil", () -> createBlockEntityType(SmashingAnvilBlockEntity::new, ANVIL_BLOCK.get()));
//register coal generator block
    public static Supplier<Block> COAL_GENERATOR_BLOCK = registerBlock("coal_generator", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(3.0f, 3.0f)));


    @ExpectPlatform
    public static <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String name, Supplier<T> blockEntity) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks) {
        throw new AssertionError();
    }

    public static void registerAll() {
    }

    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity> {
        @NotNull T create(BlockPos blockPos, BlockState blockState);
    }

    public static <T extends Block> Supplier<T> registerBlockWithItem(String name, Supplier<T> block) {
        var registeredBlock = registerBlock(name, block);
        OverchargedItems.registerItem(name, () -> new BlockItem(registeredBlock.get(), OverchargedItems.props()));
        return registeredBlock;
    }
}
