package earth.terrarium.overcharged.entity;

import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class PoweredArrow extends AbstractArrow {
    private boolean empowered;
    public PoweredArrow(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public void setEmpowered(boolean empowered) {
        this.empowered = empowered;
    }

    public boolean isEmpowered() {
        return empowered;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (isEmpowered()) {
            this.getLevel().explode(entityHitResult.getEntity(), this.getX(), this.getY(), this.getZ(), 3.0f, false, Explosion.BlockInteraction.NONE);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (isEmpowered()) {
            this.getLevel().explode(this, this.getX(), this.getY(), this.getZ(), 3.0f, false, Explosion.BlockInteraction.NONE);
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.makePowerParticles(1);
                }
            } else {
                this.makePowerParticles(2);
            }
        }
    }

    public void makePowerParticles(int i) {
        for (int k = 0; k < i; ++k) {
            this.level.addParticle(ToolUtils.GLOWSTONE, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0, 0, 0);
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public double getBaseDamage() {
        return isEmpowered() ? 5.5 : 3.0;
    }

    @Override
    public void setEnchantmentEffectsFromEntity(@NotNull LivingEntity arg, float f) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, arg);
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, arg);
        //Overridden to literally fix this one line. piss off mojang -_-.
        this.setBaseDamage((f * this.getBaseDamage()) + this.random.triangle((double) this.level.getDifficulty().getId() * 0.11, 0.57425));
        if (i > 0) {
            this.setBaseDamage(this.getBaseDamage() + (double) i * 0.5 + 0.5);
        }
        if (j > 0) {
            this.setKnockback(j);
        }
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAMING_ARROWS, arg) > 0) {
            this.setSecondsOnFire(100);
        }
    }
}
