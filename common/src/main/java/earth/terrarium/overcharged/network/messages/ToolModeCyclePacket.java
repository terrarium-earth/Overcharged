package earth.terrarium.overcharged.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.energy.ToolMode;
import earth.terrarium.overcharged.item.ConstantanAIOT;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record ToolModeCyclePacket(int toolModeIndex) implements Packet<ToolModeCyclePacket> {
    public static final Handler HANDLER = new Handler();
    public static final ResourceLocation ID = new ResourceLocation(Overcharged.MODID, "tool_mode_cycle");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ToolModeCyclePacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ToolModeCyclePacket>{

        @Override
        public void encode(ToolModeCyclePacket empoweredPacket, FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeInt(empoweredPacket.toolModeIndex());
        }

        @Override
        public ToolModeCyclePacket decode(FriendlyByteBuf friendlyByteBuf) {
            return new ToolModeCyclePacket(friendlyByteBuf.readInt());
        }

        @Override
        public PacketContext handle(ToolModeCyclePacket empoweredPacket) {
            return (player, level) -> {
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() instanceof EnergyItem energyItem) {
                    energyItem.changeToolMode(player, stack, empoweredPacket.toolModeIndex());
                }
            };
        }
    }
}
