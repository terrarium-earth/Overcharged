package earth.terrarium.overcharged.item;

import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.energy.ToolMode;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.tools.Tool;
import java.util.List;

public class ConstantanBow extends BowItem implements EnergyItem {
    public ConstantanBow(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack bow, @NotNull Level level, @NotNull LivingEntity livingEntity, int i) {
        if(livingEntity instanceof Player player && bow.getItem() instanceof EnergyItem bowEnergy) {
            ItemStack quiver = ToolUtils.findQuiver(player);
            int energyUsage = EnergyItem.isEmpowered(bow) ? 200 : 400;
            float drawPower = BowItem.getPowerForTime(this.getUseDuration(bow) - i);
            if(!bowEnergy.hasEnoughEnergy(bow, energyUsage) || quiver.isEmpty() || drawPower < 0.1F) return;
            if (!level.isClientSide) {
                AbstractArrow abstractArrow = ConstantanQuiver.createChargedArrow(quiver, bow, player);
                if (abstractArrow == null) {
                    return;
                }
                abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, drawPower * 3.0f, 1.0f);
                if (drawPower == 1.0f) {
                    abstractArrow.setCritArrow(true);
                }
                int power = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bow);
                if (power > 0) {
                    abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() + power * 0.5 + 0.5);
                }
                int knockback = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bow);
                if (knockback > 0) {
                    abstractArrow.setKnockback(knockback);
                }
                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bow) > 0) {
                    abstractArrow.setSecondsOnFire(100);
                }
                level.addFreshEntity(abstractArrow);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + drawPower * 0.5f);
            if (!player.getAbilities().instabuild) {
                bowEnergy.drainEnergy(bow, energyUsage);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        ItemStack quiver = ToolUtils.findQuiver(player);
        if (player.getAbilities().instabuild || !quiver.isEmpty()) {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public List<ToolMode> getToolModes() {
        return List.of();
    }
}
