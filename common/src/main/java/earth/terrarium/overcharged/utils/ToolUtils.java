package earth.terrarium.overcharged.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.registry.OverchargedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
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
    protected static final Map<Block, Pair<Predicate<UseOnContext>, Consumer<UseOnContext>>> TILLABLES = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Pair.of(HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.DIRT_PATH, Pair.of(HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.DIRT, Pair.of(HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(Blocks.FARMLAND.defaultBlockState())), Blocks.COARSE_DIRT, Pair.of(HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(Blocks.DIRT.defaultBlockState())), Blocks.ROOTED_DIRT, Pair.of(useOnContext -> true, HoeItem.changeIntoStateAndDropItem(Blocks.DIRT.defaultBlockState(), Items.HANGING_ROOTS))));
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
        if(EnergyItem.isEmpowered(context.getItemInHand())) {
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
}
