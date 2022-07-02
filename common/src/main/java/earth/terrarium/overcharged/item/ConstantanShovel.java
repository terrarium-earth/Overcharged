package earth.terrarium.overcharged.item;

import earth.terrarium.overcharged.energy.AOEMode;
import earth.terrarium.overcharged.energy.EnergyItem;
import earth.terrarium.overcharged.energy.ToolMode;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

import java.util.List;

public class ConstantanShovel extends ShovelItem implements EnergyItem {
    public ConstantanShovel(Tier tier, float f, float g, Properties properties) {
        super(tier, f, g, properties);
    }

    @Override
    public List<ToolMode> getEmpoweredToolModes() {
        return List.of(AOEMode.THREE_BY_THREE_AOE, AOEMode.FIVE_BY_FIVE_AOE);
    }
}
