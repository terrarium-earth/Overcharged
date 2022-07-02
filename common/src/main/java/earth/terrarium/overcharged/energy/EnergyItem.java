package earth.terrarium.overcharged.energy;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public interface EnergyItem {
    //Github copilot is too good.

    default int getEnergy(ItemStack stack) {
        return 0;
    }

    default void setEnergy(ItemStack stack, int energy) {}
    default int getMaxEnergy() {
        return 100000;
    }

    default void addEnergy(ItemStack stack, int energy) {
        this.setEnergy(stack, this.getEnergy(stack) + energy);
    }

    default void drainEnergy(ItemStack stack, int energy) {
        this.setEnergy(stack, this.getEnergy(stack) - energy);
    }

    default boolean hasEnergy(ItemStack stack) {
        return getEnergy(stack) >= 0;
    }

    default boolean hasEnoughEnergy(ItemStack stack, int energy) {
        return getEnergy(stack) >= energy;
    }

    default boolean isFull(ItemStack stack) {
        return getEnergy(stack) >= getMaxEnergy();
    }

    static boolean isEmpowered(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Empowered");
    }

    static void toggleEmpowered(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("Empowered", !isEmpowered(stack));
    }

    List<ToolMode> getEmpoweredToolModes();

    ToolMode defaultToolMode();

    default ToolMode getCurrentToolMode(ItemStack stack) {
        if(isEmpowered(stack)) {
            return getEmpoweredToolModes().get(stack.getOrCreateTag().getInt("ToolMode") % getEmpoweredToolModes().size());
        } else return defaultToolMode();
    }

    default InteractionResult hoeAction(UseOnContext useOnContext) {
        if (!this.hasEnoughEnergy(useOnContext.getItemInHand(), 200)) return InteractionResult.PASS;
        BlockPos blockPos;
        Level level = useOnContext.getLevel();
        BlockState till = ToolUtils.getToolModifiedState(level.getBlockState(blockPos = useOnContext.getClickedPos()), useOnContext, "till");
        if (till != null) {
            Player player = useOnContext.getPlayer();
            level.playSound(player, blockPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (!level.isClientSide) {
                level.setBlock(blockPos, till, Block.UPDATE_ALL_IMMEDIATE);
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, till));
                if (player != null) {
                    this.drainEnergy(useOnContext.getItemInHand(), 200);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    default InteractionResult axeAction(UseOnContext useOnContext) {
        if (!this.hasEnoughEnergy(useOnContext.getItemInHand(), 200)) return InteractionResult.PASS;
        BlockPos blockPos = useOnContext.getClickedPos();
        Level level = useOnContext.getLevel();
        Player player2 = useOnContext.getPlayer();
        BlockState optional = ToolUtils.getToolModifiedState(level.getBlockState(blockPos), useOnContext, "axe_strip");
        BlockState optional2 = ToolUtils.getToolModifiedState(level.getBlockState(blockPos), useOnContext, "axe_scrape");
        BlockState optional3 = ToolUtils.getToolModifiedState(level.getBlockState(blockPos), useOnContext, "axe_wax_off");
        ItemStack itemStack = useOnContext.getItemInHand();
        BlockState optional4 = null;
        if (optional != null) {
            level.playSound(player2, blockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.0f);
            optional4 = optional;
        } else if (optional2 != null) {
            level.playSound(player2, blockPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f);
            level.levelEvent(player2, 3005, blockPos, 0);
            optional4 = optional2;
        } else if (optional3 != null) {
            level.playSound(player2, blockPos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f);
            level.levelEvent(player2, 3004, blockPos, 0);
            optional4 = optional3;
        }
        if (optional4 != null) {
            if (player2 instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player2, blockPos, itemStack);
            }
            level.setBlock(blockPos, optional4, 11);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player2, optional4));
            if (player2 != null) {
                this.drainEnergy(itemStack, 200);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
