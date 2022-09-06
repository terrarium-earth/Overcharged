package earth.terrarium.overcharged.item;

import earth.terrarium.botarium.api.energy.EnergyContainer;
import earth.terrarium.botarium.api.energy.EnergyItem;
import earth.terrarium.botarium.api.energy.ItemEnergyContainer;
import earth.terrarium.overcharged.energy.ConstantanItem;
import earth.terrarium.overcharged.energy.ToolMode;
import earth.terrarium.overcharged.entity.PoweredArrow;
import earth.terrarium.overcharged.registry.OverchargedEntities;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConstantanBow extends BowItem implements ConstantanItem, EnergyItem {
    public ConstantanBow(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack bow, @NotNull Level level, @NotNull LivingEntity livingEntity, int i) {
        if(livingEntity instanceof Player player) {
            int energyUsage = ToolUtils.isEmpowered(bow) ? 400 : 200;
            float drawPower = BowItem.getPowerForTime(this.getUseDuration(bow) - i);
            if(this.getEnergyStorage(bow).getStoredEnergy() < energyUsage || drawPower < 0.1F) return;
            if (!level.isClientSide) {
                AbstractArrow abstractArrow = this.createChargedArrow(bow, player);
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
                this.getEnergyStorage(bow).extractEnergy(energyUsage, false);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        EnergyContainer energy = this.getEnergyStorage(itemStack);
        if (player.getAbilities().instabuild || energy.getStoredEnergy() >= (ToolUtils.isEmpowered(itemStack) ? 400 : 200)) {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Nullable
    public PoweredArrow createChargedArrow(ItemStack bow, Player player) {
        PoweredArrow arrow = OverchargedEntities.POWERED_ARROW.get().create(player.getLevel());
        if(arrow != null) {
            arrow.setOwner(player);
            arrow.setPos(player.getX(), player.getEyeY(), player.getZ());
            arrow.setEmpowered(ToolUtils.isEmpowered(bow));
            return arrow;
        }
        return null;
    }

    @Override
    public List<ToolMode> getEmpoweredToolModes() {
        return List.of();
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack itemStack) {
        return ToolUtils.isBarVisible(itemStack);
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        return ToolUtils.energyBar(itemStack);
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return 0xFFDB12;
    }

    @Override
    public EnergyContainer getEnergyStorage(ItemStack object) {
        return new ItemEnergyContainer(object, 800000);
    }
}
