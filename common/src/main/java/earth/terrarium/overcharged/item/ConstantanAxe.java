package earth.terrarium.overcharged.item;

import earth.terrarium.overcharged.energy.AOEMode;
import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.energy.ToolMode;
import earth.terrarium.overcharged.energy.VeinMineMode;
import earth.terrarium.overcharged.utils.ToolUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ConstantanAxe extends AxeItem implements EnergyItem {
    protected ConstantanAxe(float f, float g, Properties properties) {
        super(ConstantanTier.INSTANCE, f, g, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if (!hasEnoughEnergy(useOnContext.getItemInHand(), 200)) return InteractionResult.PASS;
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        Player player2 = useOnContext.getPlayer();
        BlockState blockState = level.getBlockState(blockPos);
        Optional<BlockState> optional = this.getStripped(blockState);
        Optional<BlockState> optional2 = WeatheringCopper.getPrevious(blockState);
        Optional<BlockState> optional3 = Optional.ofNullable(HoneycombItem.WAX_OFF_BY_BLOCK.get().get(blockState.getBlock())).map(block -> block.withPropertiesOf(blockState));
        ItemStack itemStack = useOnContext.getItemInHand();
        Optional<BlockState> optional4 = Optional.empty();
        if (optional.isPresent()) {
            level.playSound(player2, blockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.0f);
            optional4 = optional;
        } else if (optional2.isPresent()) {
            level.playSound(player2, blockPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f);
            level.levelEvent(player2, 3005, blockPos, 0);
            optional4 = optional2;
        } else if (optional3.isPresent()) {
            level.playSound(player2, blockPos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f);
            level.levelEvent(player2, 3004, blockPos, 0);
            optional4 = optional3;
        }
        if (optional4.isPresent()) {
            if (player2 instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player2, blockPos, itemStack);
            }
            level.setBlock(blockPos, optional4.get(), 11);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player2, optional4.get()));
            if (player2 != null) {
                this.drainEnergy(itemStack, 200);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private Optional<BlockState> getStripped(BlockState blockState) {
        return Optional.ofNullable(STRIPPABLES.get(blockState.getBlock())).map(block -> block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState.getValue(RotatedPillarBlock.AXIS)));
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity, @NotNull LivingEntity livingEntity2) {
        return ToolUtils.hurtEnemy(this, itemStack, livingEntity, livingEntity2, 200);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull BlockState blockState, @NotNull BlockPos blockPos, @NotNull LivingEntity livingEntity) {
        return ToolUtils.mineBlock(this, itemStack, level, blockState, blockPos, 200);
    }

    @Override
    public List<ToolMode> getToolModes() {
        return List.of(AOEMode.THREE_BY_THREE_AOE, AOEMode.FIVE_BY_FIVE_AOE, VeinMineMode.VEIN_MINING);
    }
}
