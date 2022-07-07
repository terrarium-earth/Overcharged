package earth.terrarium.overcharged.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.item.ConstantanAIOT;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record AIOTToolTypePacket(EnergyItem.ToolType toolType) implements Packet<AIOTToolTypePacket> {
    public static final Handler HANDLER = new Handler();
    public static final ResourceLocation ID = new ResourceLocation(Overcharged.MODID, "aiot_tool_type");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<AIOTToolTypePacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<AIOTToolTypePacket>{

        @Override
        public void encode(AIOTToolTypePacket empoweredPacket, FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeEnum(empoweredPacket.toolType());
        }

        @Override
        public AIOTToolTypePacket decode(FriendlyByteBuf friendlyByteBuf) {
            return new AIOTToolTypePacket(friendlyByteBuf.readEnum(EnergyItem.ToolType.class));
        }

        @Override
        public PacketContext handle(AIOTToolTypePacket empoweredPacket) {
            return (player, level) -> {
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() instanceof ConstantanAIOT) {
                    stack.getOrCreateTag().putString("ToolType", empoweredPacket.toolType().toString());
                }
            };
        }
    }
}
