package earth.terrarium.overcharged.network;

import com.teamresourceful.resourcefullib.common.networking.NetworkChannel;
import com.teamresourceful.resourcefullib.common.networking.base.NetworkDirection;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.network.messages.AIOTToolTypePacket;
import earth.terrarium.overcharged.network.messages.EmpoweredPacket;

public class NetworkHandler {
    public static final NetworkChannel CHANNEL = new NetworkChannel(Overcharged.MODID, 0, "overcharged");

    public static void registerPackets() {
        CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, EmpoweredPacket.ID, EmpoweredPacket.HANDLER, EmpoweredPacket.class);
        CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, AIOTToolTypePacket.ID, AIOTToolTypePacket.HANDLER, AIOTToolTypePacket.class);
    }
}
