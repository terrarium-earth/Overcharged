package earth.terrarium.overcharged.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.components.selection.SelectionList;
import earth.terrarium.overcharged.Overcharged;
import earth.terrarium.overcharged.block.generator.GeneratorBlockEntity;
import earth.terrarium.overcharged.block.generator.GeneratorData;
import earth.terrarium.overcharged.block.generator.GeneratorMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GeneratorScreen extends AbstractContainerScreen<GeneratorMenu> {
    private static final ResourceLocation BASE_SCREEN = new ResourceLocation(Overcharged.MODID, "textures/gui/generator.png");

    public GeneratorScreen(GeneratorMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 190;
        this.inventoryLabelY = 95;
        this.titleLabelY = 8;
    }

    @Override
    protected void init() {
        super.init();
        var boxComponents = addRenderableWidget(new SelectionList(leftPos + 49, topPos + 24, 63, 66, 10, entry -> {}));
        boxComponents.updateEntries(List.of(
                new DeferredInfographic(() -> Component.translatable( getGeneratingAmount() > 0 ? "gui.overcharged.active" : "gui.overcharged.inactive" ).withStyle(ChatFormatting.GRAY)),
                new DeferredInfographic(() -> Component.translatable("gui.overcharged.generating", getGeneratingAmount()).withStyle(ChatFormatting.GRAY)),
                new DeferredInfographic(() -> Component.translatable("gui.overcharged.efficiency", getEfficiency()).withStyle(ChatFormatting.GRAY))
        ));
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float f, int i, int j) {
        RenderSystem.setShaderTexture(0, BASE_SCREEN);
        RenderSystem.setShaderColor(1, 1, 1, 1f);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int energyBarHeight = Mth.clamp((int) (66F * (getEnergyLevel() / (float) GeneratorBlockEntity.MAX_ENERGY)), 0, 66);
        blit(matrixStack, leftPos + 134, topPos + 22 + (66 - energyBarHeight), 176, 66 - energyBarHeight, 12, energyBarHeight);
        int tickBarHeight = Mth.clamp((int) (66F * (getTicks() / (float) GeneratorBlockEntity.MAX_WORK)), 0, 66);
        blit(matrixStack, leftPos + 120, topPos + 22 + (66 - tickBarHeight), 188, 0, 6, tickBarHeight);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
        if(mouseX > leftPos + 133 && mouseX < 146 + leftPos && mouseY > 21 + topPos && mouseY < 88 + topPos) {
            this.renderTooltip(poseStack, Component.translatable("gui." + Overcharged.MODID + ".energy_tooltip", Component.literal(String.valueOf(getEnergyLevel())).withStyle(ChatFormatting.GRAY), Component.literal(String.valueOf(GeneratorBlockEntity.MAX_ENERGY)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.GOLD), mouseX, mouseY);
        } else if(mouseX > leftPos + 119 && mouseX < 126 + leftPos && mouseY > 21 + topPos && mouseY < 88 + topPos) {
            this.renderTooltip(poseStack, Component.translatable("gui." + Overcharged.MODID + ".work_tooltip", Component.literal(String.valueOf(getTicks())).withStyle(ChatFormatting.GRAY), Component.literal(String.valueOf(GeneratorBlockEntity.MAX_WORK)).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.GOLD), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(@NotNull PoseStack poseStack, int i, int j) {
        this.font.draw(poseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 0xffffb62e);
        this.font.draw(poseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 0xffffb62e);
    }

    public int getTicks() {
        return this.getMenu().data.get(GeneratorData.WORK);
    }

    public int getEnergyLevel() {
        return this.getMenu().data.get(GeneratorData.ENERGY_LEVEL);
    }

    public int getGeneratingAmount() {
        return this.getMenu().data.get(GeneratorData.GENERATING);
    }

    public int getEfficiency() {
        return this.getMenu().data.get(GeneratorData.EFFICIENCY);
    }
}
