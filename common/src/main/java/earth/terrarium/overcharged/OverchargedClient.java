package earth.terrarium.overcharged;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class OverchargedClient {

    public static void initClient() {
    }

    @ExpectPlatform
    public static void registerItemProperty(Item pItem, ResourceLocation pName, ClampedItemPropertyFunction pProperty) {
    }

    @ExpectPlatform
    public static <T extends BlockEntity> void registerBlockEntityRenderers(BlockEntityType<T> type, BlockEntityRendererProvider<T> provider) {
    }

    @ExpectPlatform
    public static void setRenderLayer(Block block, RenderType type) {
    }
}
