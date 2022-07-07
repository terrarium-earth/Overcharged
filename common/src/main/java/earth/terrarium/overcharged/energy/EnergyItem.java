package earth.terrarium.overcharged.energy;

import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiFunction;

public interface EnergyItem {
    //GitHub copilot is too good.

    default int getEnergy(ItemStack stack) {
        return 0;
    }

    default void setEnergy(ItemStack stack, int energy) {}
    default int getMaxEnergy() {
        return 800000;
    }

    default void drainEnergy(ItemStack stack, int energy) {
        this.setEnergy(stack, this.getEnergy(stack) - energy);
    }

    default boolean hasEnoughEnergy(ItemStack stack, int energy) {
        return getEnergy(stack) >= energy;
    }

    static boolean isEmpowered(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Empowered");
    }

    static void toggleEmpowered(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("Empowered", !isEmpowered(stack));
    }

    default List<ToolMode> getEmpoweredToolModes() {
        return List.of(AOEMode.THREE_BY_THREE_AOE, AOEMode.FIVE_BY_FIVE_AOE, VeinMineMode.VEIN_MINING);
    }

    @Nullable
    default ToolMode getCurrentToolMode(ItemStack stack) {
        if(isEmpowered(stack)) {
            return getEmpoweredToolModes().get(stack.getOrCreateTag().getInt("ToolMode") % getEmpoweredToolModes().size());
        } else return null;
    }

    default void changeToolMode(Player player, ItemStack stack, int index) {
        if(isEmpowered(stack)) {
            stack.getOrCreateTag().putInt("ToolMode", index % this.getEmpoweredToolModes().size());
            ToolMode currentToolMode = getCurrentToolMode(stack);
            if(currentToolMode != null) {
                player.displayClientMessage(Component.translatable("messsages.overcharged.aiot_tool_type", currentToolMode.getName()), true);
            }
        }
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
        BlockState blockState = level.getBlockState(blockPos);
        BlockState optional = ToolUtils.getToolModifiedState(blockState, useOnContext, "axe_strip");
        BlockState optional2 = ToolUtils.getToolModifiedState(blockState, useOnContext, "axe_scrape");
        BlockState optional3 = ToolUtils.getToolModifiedState(blockState, useOnContext, "axe_wax_off");
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

    default InteractionResult shovelAction(UseOnContext useOnContext) {
        if (!this.hasEnoughEnergy(useOnContext.getItemInHand(), 200)) return InteractionResult.PASS;
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (useOnContext.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        } else {
            Player player = useOnContext.getPlayer();
            BlockState blockState2 = ToolUtils.getToolModifiedState(level.getBlockState(blockPos), useOnContext, "shovel_flatten");
            BlockState blockState3 = null;
            if (blockState2 != null && level.getBlockState(blockPos.above()).isAir()) {
                level.playSound(player, blockPos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                blockState3 = blockState2;
            } else if (blockState.getBlock() instanceof CampfireBlock && blockState.getValue(CampfireBlock.LIT)) {
                if (!level.isClientSide()) {
                    level.levelEvent(null, 1009, blockPos, 0);
                }

                CampfireBlock.dowse(useOnContext.getPlayer(), level, blockPos, blockState);
                blockState3 = blockState.setValue(CampfireBlock.LIT, false);
            }

            if (blockState3 != null) {
                if (!level.isClientSide) {
                    level.setBlock(blockPos, blockState3, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, blockState3));
                    if (player != null) {
                        this.drainEnergy(useOnContext.getItemInHand(), 200);
                    }
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    enum ToolType {
        SHOVEL((energyItem, useOnContext) -> {
            var action = energyItem.shovelAction(useOnContext);
            ItemStack itemInHand = useOnContext.getItemInHand();
            ToolMode currentToolMode = energyItem.getCurrentToolMode(itemInHand);
            if(currentToolMode != null) {
                currentToolMode.useTool(useOnContext, energyItem::shovelAction);
            }
            return action;
        }),
        HOE((energyItem, useOnContext) -> {
            var action = energyItem.hoeAction(useOnContext);
            ItemStack itemInHand = useOnContext.getItemInHand();
            ToolMode currentToolMode = energyItem.getCurrentToolMode(itemInHand);
            if(currentToolMode != null) {
                currentToolMode.useTool(useOnContext, energyItem::hoeAction);
            }
            return action;
        }),
        AXE((energyItem, useOnContext) -> {
            var action = energyItem.axeAction(useOnContext);
            ItemStack itemInHand = useOnContext.getItemInHand();
            ToolMode currentToolMode = energyItem.getCurrentToolMode(itemInHand);
            if(currentToolMode != null) {
                currentToolMode.useTool(useOnContext, energyItem::axeAction);
            }
            return action;
        });

        private final BiFunction<EnergyItem, UseOnContext, InteractionResult> function;
        ToolType(BiFunction<EnergyItem, UseOnContext, InteractionResult> function) {
            this.function = function;
        }

        public BiFunction<EnergyItem, UseOnContext, InteractionResult> getFunction() {
            return function;
        }
    }
}
