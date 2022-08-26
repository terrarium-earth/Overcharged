package earth.terrarium.overcharged.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.registry.OverchargedItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ToolUtils {
    public static final Vector3f GLOWSTONE_COLOR = new Vector3f(Vec3.fromRGB24(0xffd324));
    public static final DustParticleOptions GLOWSTONE = new DustParticleOptions(GLOWSTONE_COLOR, 1.0f);

    public static final EntityTypeTest<Entity, LivingEntity> LIVING_ENTITY_TEST = new EntityTypeTest<>() {
        @Nullable
        @Override
        public LivingEntity tryCast(@NotNull Entity entity) {
            return entity instanceof LivingEntity living ? living : null;
        }

        @NotNull
        @Override
        public Class<? extends Entity> getBaseClass() {
            return LivingEntity.class;
        }
    };

    public static boolean mineBlock(EnergyItem item, @NotNull ItemStack itemStack, Level level, @NotNull BlockState blockState, @NotNull BlockPos blockPos, int damage) {
        if (!level.isClientSide && blockState.getDestroySpeed(level, blockPos) != 0.0f && item.hasEnoughEnergy(itemStack, damage)) {
            item.drainEnergy(itemStack, damage);
            return true;
        }
        return false;
    }

    public static boolean hurtEnemy(EnergyItem item, @NotNull ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity2, int damage) {
        if(item.hasEnoughEnergy(itemStack, damage)) {
            item.drainEnergy(itemStack, damage);
            return true;
        }
        return false;
    }

    public static void playerBreak(Level world, Player player, ItemStack stack, BlockPos pos) {
        if(world instanceof ServerLevel serverLevel && world.mayInteract(player, pos)) {
            BlockState state = world.getBlockState(pos);
            world.removeBlock(pos, false);
            serverLevel.sendParticles(GLOWSTONE, pos.getX(), pos.getY(), pos.getZ(), 10, 1, 1, 1, 3);
            state.getBlock().playerDestroy(world, player, pos, state, world.getBlockEntity(pos), stack);
            state.getBlock().playerWillDestroy(world, pos, state, player);
        }
    }

    public static BlockHitResult getPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid fluid) {
        float pitch = player.getXRot();
        float yaw = player.getYRot();
        Vec3 eyePos = player.getEyePosition();
        double yawOffsetZ = Math.cos(Math.toRadians(-yaw) - Math.PI);
        double yawOffsetX = Math.sin(Math.toRadians(-yaw) - Math.PI);
        double pitchOffsetZ = -Math.cos(Math.toRadians(pitch));
        double pitchOffsetX = Math.sin(Math.toRadians(-pitch));
        double xOffset = yawOffsetX * pitchOffsetZ;
        double zOffset = yawOffsetZ * pitchOffsetZ;
        Vec3 vec3d2 = eyePos.add(xOffset * 5.0D, pitchOffsetX * 5.0D, zOffset * 5.0D);
        return level.clip(new ClipContext(eyePos, vec3d2, ClipContext.Block.OUTLINE, fluid, player));
    }

    public static void areaUseOnBlock(UseOnContext context, Consumer<UseOnContext> consumer) {
        if(isEmpowered(context.getItemInHand())) {
            BlockPos.betweenClosedStream(context.getClickedPos().offset(1, 0, 1), context.getClickedPos().offset(-1, 0, -1))
                    .map(blockPos -> new BlockHitResult(context.getClickLocation(), context.getClickedFace(), blockPos.immutable(), false))
                    .map(blockHitResult -> new UseOnContext(Objects.requireNonNull(context.getPlayer()), context.getHand(), blockHitResult))
                    .forEach(consumer);
        }
    }

    @ExpectPlatform
    public static BlockState getToolModifiedState(BlockState state, UseOnContext context, String toolAction) {
        throw new AssertionError();
    }

    public static TagKey<Block> getAIOTBlockTag() {
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(ArchitecturyTarget.getCurrentTarget().equals("forge") ? "forge" : "c", "mineable/aiot"));
    }

    public static float itemProperty(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
        if (itemStack.getItem() instanceof EnergyItem energyItem) {
            if(isEmpowered(itemStack) && energyItem.hasEnoughEnergy(itemStack, 1)) {
                return 1.0f;
            } else if(energyItem.hasEnoughEnergy(itemStack, 1)){
                return 0.5f;
            }
        }
        return 0;
    }

    public static InteractionResult hoeAction(UseOnContext useOnContext) {
        if (useOnContext.getItemInHand().getItem() instanceof EnergyItem energyItem) {
            if (!energyItem.hasEnoughEnergy(useOnContext.getItemInHand(), 200)) return InteractionResult.PASS;
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
                        energyItem.drainEnergy(useOnContext.getItemInHand(), 200);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult axeAction(UseOnContext useOnContext) {
        if (useOnContext.getItemInHand().getItem() instanceof EnergyItem energyItem) {
            if (!energyItem.hasEnoughEnergy(useOnContext.getItemInHand(), 200)) return InteractionResult.PASS;
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
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player2, blockPos, itemStack);
                }
                level.setBlock(blockPos, optional4, 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player2, optional4));
                if (player2 != null) {
                    energyItem.drainEnergy(itemStack, 200);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult shovelAction(UseOnContext useOnContext) {
        if (useOnContext.getItemInHand().getItem() instanceof EnergyItem energyItem) {
            if (!energyItem.hasEnoughEnergy(useOnContext.getItemInHand(), 200)) return InteractionResult.PASS;
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
                            energyItem.drainEnergy(useOnContext.getItemInHand(), 200);
                        }
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static boolean isEmpowered(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Empowered");
    }

    public static void toggleEmpowered(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("Empowered", !isEmpowered(stack));
    }
}
