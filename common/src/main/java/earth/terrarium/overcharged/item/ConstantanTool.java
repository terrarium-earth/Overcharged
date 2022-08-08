package earth.terrarium.overcharged.item;

import earth.terrarium.overcharged.energy.EnergyItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.level.block.Block;

public class ConstantanTool extends DiggerItem implements EnergyItem {
    public ConstantanTool(TagKey<Block> tag, int i, float f, Properties properties) {
        super(i, f, ConstantanTier.INSTANCE, tag, properties);
    }
}
