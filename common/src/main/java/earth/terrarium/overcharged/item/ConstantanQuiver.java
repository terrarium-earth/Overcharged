package earth.terrarium.overcharged.item;

import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.entity.PoweredArrow;
import earth.terrarium.overcharged.registry.OverchargedEntities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ConstantanQuiver extends Item implements EnergyItem {
    public ConstantanQuiver(Properties properties) {
        super(properties);
    }

    //create charged arrow method
    @Nullable
    public static PoweredArrow createChargedArrow(ItemStack quiver, ItemStack bow, Player player) {
        if(quiver.getItem() instanceof EnergyItem quiverEnergy && bow.getItem() instanceof EnergyItem bowEnergy) {
            if(EnergyItem.isEmpowered(quiver) && quiverEnergy.hasEnoughEnergy(quiver, 400)) {
                quiverEnergy.drainEnergy(quiver, 400);
            } else if (quiverEnergy.hasEnoughEnergy(quiver, 200)) {
                quiverEnergy.drainEnergy(quiver, 200);
            } else {
                return null;
            }
            PoweredArrow arrow = OverchargedEntities.POWERED_ARROW.get().create(player.getLevel());
            if(arrow != null) {
                arrow.setOwner(player);
                arrow.setPos(player.getX(), player.getEyeY(), player.getZ());
                arrow.setEmpowered(EnergyItem.isEmpowered(quiver));
                arrow.setNoGravity(EnergyItem.isEmpowered(bow));
                return arrow;
            }
        }
        return null;
    }


}
