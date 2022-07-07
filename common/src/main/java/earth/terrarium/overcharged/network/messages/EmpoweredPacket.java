package earth.terrarium.overcharged.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.energy.EnergyItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record EmpoweredPacket() implements Packet<EmpoweredPacket> {
    public static final Handler HANDLER = new Handler();
    public static final ResourceLocation ID = new ResourceLocation(Overcharged.MODID, "toggle_empowered");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<EmpoweredPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<EmpoweredPacket>{

        @Override
        public void encode(EmpoweredPacket empoweredPacket, FriendlyByteBuf friendlyByteBuf) {

        }

        @Override
        public EmpoweredPacket decode(FriendlyByteBuf friendlyByteBuf) {
            return new EmpoweredPacket();
        }

        @Override
        public PacketContext handle(EmpoweredPacket empoweredPacket) {

            return (player, level) -> {
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() instanceof EnergyItem) {
                    EnergyItem.toggleEmpowered(stack);
                }
            };
        }
    }
}
