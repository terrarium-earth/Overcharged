package earth.terrarium.overcharged.fabric;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class OverchargedClientImpl {
    public static void registerItemProperty(Item pItem, ResourceLocation pName, ClampedItemPropertyFunction pProperty) {
        ItemProperties.register(pItem, pName, pProperty);
    }

    public static <T extends BlockEntity> void registerBlockEntityRenderers(BlockEntityType<T> type, BlockEntityRendererProvider<T> provider) {
        BlockEntityRendererRegistry.register(type, provider);
    }

    public static void setRenderLayer(Block block, RenderType type) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, type);
    }
}
