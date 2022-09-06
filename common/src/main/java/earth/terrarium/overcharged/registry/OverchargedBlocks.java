package earth.terrarium.overcharged.registry;

import earth.terrarium.botarium.api.registry.RegistryHelpers;
import earth.terrarium.botarium.api.registry.RegistryHolder;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.block.SmashingAnvilBlock;
import earth.terrarium.overcharged.block.SmashingAnvilBlockEntity;
import earth.terrarium.overcharged.block.generator.GeneratorBlock;
import earth.terrarium.overcharged.block.generator.GeneratorBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class OverchargedBlocks {
    public static final RegistryHolder<Block> BLOCKS = new RegistryHolder<>(Registry.BLOCK, Overcharged.MODID);
    public static final RegistryHolder<BlockEntityType<?>> BLOCK_ENTITIES = new RegistryHolder<>(Registry.BLOCK_ENTITY_TYPE, Overcharged.MODID);

    public static Supplier<SmashingAnvilBlock> ANVIL_BLOCK = registerBlockWithItem("smashing_anvil", () -> new SmashingAnvilBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static Supplier<BlockEntityType<SmashingAnvilBlockEntity>> ANVIL_BLOCK_ENTITY = BLOCK_ENTITIES.register("smashing_anvil", () -> RegistryHelpers.createBlockEntityType(SmashingAnvilBlockEntity::new, ANVIL_BLOCK.get()));
    //register coal generator block
    public static Supplier<Block> COAL_GENERATOR_BLOCK = registerBlockWithItem("coal_generator", () -> new GeneratorBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3.0f, 3.0f).noOcclusion()));
    public static Supplier<BlockEntityType<GeneratorBlockEntity>> COAL_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("coal_generator", () -> RegistryHelpers.createBlockEntityType(GeneratorBlockEntity::new, COAL_GENERATOR_BLOCK.get()));

    public static <T extends Block> Supplier<T> registerBlockWithItem(String name, Supplier<T> block) {
        var registeredBlock = BLOCKS.register(name, block);
        OverchargedItems.ITEMS.register(name, () -> new BlockItem(registeredBlock.get(), OverchargedItems.props()));
        return registeredBlock;
    }

    public static void init() {}

    public static void registerAll() {
        BLOCKS.initialize();
        BLOCK_ENTITIES.initialize();
    }
}
