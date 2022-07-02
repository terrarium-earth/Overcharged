package earth.terrarium.overcharged.energy;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Consumer;

public interface ToolMode {
    Component getName();
    void onTick(ItemStack stack);
    void useTool(UseOnContext context, Consumer<UseOnContext> consumer);
    void onMineBlock(ItemStack stack, Level level, BlockHitResult hit, Player player);
    void onHitEntity(ItemStack stack, LivingEntity target, Player player);

}
