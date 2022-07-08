package earth.terrarium.overcharged;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.overcharged.registry.OverchargedItems;
import earth.terrarium.overcharged.utils.ToolUtils;
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
        registerItemProperty(OverchargedItems.CONSTANTAN_AIOT.get(), new ResourceLocation(Overcharged.MODID, "energy_state"), ToolUtils::itemProperty);
        registerItemProperty(OverchargedItems.CONSTANTAN_AXE.get(), new ResourceLocation(Overcharged.MODID, "energy_state"), ToolUtils::itemProperty);
        registerItemProperty(OverchargedItems.CONSTANTAN_SHOVEL.get(), new ResourceLocation(Overcharged.MODID, "energy_state"), ToolUtils::itemProperty);
        registerItemProperty(OverchargedItems.CONSTANTAN_HOE.get(), new ResourceLocation(Overcharged.MODID, "energy_state"), ToolUtils::itemProperty);
        registerItemProperty(OverchargedItems.CONSTANTAN_PICKAXE.get(), new ResourceLocation(Overcharged.MODID, "energy_state"), ToolUtils::itemProperty);
        registerItemProperty(OverchargedItems.CONSTANTAN_SWORD.get(), new ResourceLocation(Overcharged.MODID, "energy_state"), ToolUtils::itemProperty);
        registerItemProperty(OverchargedItems.CONSTANTAN_BOW.get(), new ResourceLocation(Overcharged.MODID, "energy_state"), ToolUtils::itemProperty);
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
