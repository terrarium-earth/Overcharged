package earth.terrarium.overcharged.block.generator;

import earth.terrarium.botarium.api.menu.MenuHooks;
import earth.terrarium.overcharged.registry.OverchargedBlocks;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GeneratorBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public GeneratorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(@NotNull BlockState blockState, Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (level.getBlockEntity(blockPos) instanceof GeneratorBlockEntity blockEntity) {
            MenuHooks.openMenu((ServerPlayer) player, blockEntity);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        return this.defaultBlockState().setValue(FACING, arg.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState arg, Rotation arg2) {
        return arg.setValue(FACING, arg2.rotate(arg.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState arg, Mirror arg2) {
        return arg.rotate(arg2.getRotation(arg.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
        arg.add(FACING);
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        super.animateTick(blockState, level, blockPos, randomSource);
        Optional<GeneratorBlockEntity> blockEntity = level.getBlockEntity(blockPos, OverchargedBlocks.COAL_GENERATOR_BLOCK_ENTITY.get());
        blockEntity.ifPresent(generator -> {
            if(generator.getWork() > 0) {
                level.addParticle(ToolUtils.GLOWSTONE, blockPos.getX() + 0.5, blockPos.getY() + 0.95, blockPos.getZ() + 0.5, 0.5, 0.5, 0.5);
            }
        });
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GeneratorBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, OverchargedBlocks.COAL_GENERATOR_BLOCK_ENTITY.get(), (l, bp, bs, be) -> be.tick());
    }

    @Override
    public RenderShape getRenderShape(@NotNull BlockState arg) {
        return RenderShape.MODEL;
    }
}
