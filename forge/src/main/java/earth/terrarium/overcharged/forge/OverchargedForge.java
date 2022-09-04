package earth.terrarium.overcharged.forge;

import earth.terrarium.botarium.api.energy.EnergyManager;
import earth.terrarium.botarium.api.energy.PlatformEnergyManager;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.energy.ConstantanItem;
import earth.terrarium.overcharged.energy.ToolMode;
import earth.terrarium.overcharged.network.NetworkHandler;
import earth.terrarium.overcharged.registry.OverchargedBlocks;
import earth.terrarium.overcharged.registry.OverchargedEntities;
import earth.terrarium.overcharged.registry.OverchargedItems;
import earth.terrarium.overcharged.registry.OverchargedRecipes;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Overcharged.MODID)
public class OverchargedForge {
    public OverchargedForge() {
        Overcharged.init();
        //get mod event bus
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        OverchargedBlocks.registerAll();
        OverchargedItems.registerAll();
        OverchargedRecipes.registerAll();
        OverchargedEntities.registerAll();
        eventBus.addListener(this::commonSetup);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> OverchargedForgeClient::init);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        NetworkHandler.registerPackets();
    }

    @SubscribeEvent
    public void playerBreakEvent(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof ConstantanItem constantanItem) {
            PlatformEnergyManager energyItem = EnergyManager.getItemHandler(stack);
            if (energyItem.getStoredEnergy() < 200) return;
            Level level = event.getPlayer().getLevel();
            ToolMode currentToolMode = constantanItem.getCurrentToolMode(stack);
            if(currentToolMode != null) {
                currentToolMode.onMineBlock(stack, level, ToolUtils.getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY), player);
            }
        }
    }
}