package potatowolfie.earth_and_water.entity.client.spiked_shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;
import potatowolfie.earth_and_water.EarthWaterClient;

import java.util.Objects;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class SpikedShieldModelRenderer implements SpecialModelRenderer<DataComponentMap> {
    private final MaterialSet spriteHolder;
    private final SpikedShieldEntityModel model;

    public SpikedShieldModelRenderer(MaterialSet spriteHolder, SpikedShieldEntityModel model) {
        this.spriteHolder = spriteHolder;
        this.model = model;
    }

    @Nullable
    public DataComponentMap extractArgument(ItemStack itemStack) {
        return itemStack.immutableComponents();
    }

    public void render(@Nullable DataComponentMap componentMap, ItemDisplayContext itemDisplayContext, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, int i, int j, boolean bl, int k) {
        BannerPatternLayers bannerPatternsComponent = componentMap != null ?
                (BannerPatternLayers)componentMap.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY) :
                BannerPatternLayers.EMPTY;
        DyeColor dyeColor = componentMap != null ? (DyeColor)componentMap.get(DataComponents.BASE_COLOR) : null;
        boolean bl2 = !bannerPatternsComponent.layers().isEmpty() || dyeColor != null;

        matrixStack.pushPose();
        matrixStack.scale(1.0F, -1.0F, -1.0F);

        Material spriteIdentifier = bl2 ?
                EarthWaterClient.SPIKED_SHIELD_BASE :
                EarthWaterClient.SPIKED_SHIELD_BASE_NO_PATTERN;

        orderedRenderCommandQueue.submitModelPart(
                this.model.handle(),
                matrixStack,
                RenderTypes.entityCutoutNoCull(Sheets.SHIELD_SHEET),
                i,
                j,
                this.spriteHolder.get(spriteIdentifier),
                false,
                false,
                -1,
                null,
                k
        );

        orderedRenderCommandQueue.submitModelPart(
                this.model.plate(),
                matrixStack,
                RenderTypes.entityCutoutNoCull(Sheets.SHIELD_SHEET),
                i,
                j,
                this.spriteHolder.get(spriteIdentifier),
                false,
                bl,
                -1,
                null,
                k
        );

        if (bl2) {
            Material baseLayerSprite = Sheets.SHIELD_BASE;
            DyeColor baseColor = Objects.requireNonNullElse(dyeColor, DyeColor.WHITE);

            orderedRenderCommandQueue.submitModelPart(
                    this.model.plate(),
                    matrixStack,
                    RenderTypes.entityCutoutNoCull(Sheets.SHIELD_SHEET),
                    i,
                    j,
                    this.spriteHolder.get(baseLayerSprite),
                    false,
                    false,
                    baseColor.getTextureDiffuseColor(),
                    null,
                    0
            );

            for (int layerIndex = 0; layerIndex < 16 && layerIndex < bannerPatternsComponent.layers().size(); ++layerIndex) {
                BannerPatternLayers.Layer layer = bannerPatternsComponent.layers().get(layerIndex);
                Material patternSprite = Sheets.getShieldMaterial(layer.pattern());

                orderedRenderCommandQueue.submitModelPart(
                        this.model.plate(),
                        matrixStack,
                        RenderTypes.entityCutoutNoCull(Sheets.SHIELD_SHEET),
                        i,
                        j,
                        this.spriteHolder.get(patternSprite),
                        false,
                        false,
                        layer.color().getTextureDiffuseColor(),
                        null,
                        0
                );
            }

            if (bl) {
                orderedRenderCommandQueue.submitModelPart(
                        this.model.plate(),
                        matrixStack,
                        RenderTypes.entityGlint(),
                        i,
                        j,
                        this.spriteHolder.get(spriteIdentifier),
                        false,
                        false,
                        -1,
                        null,
                        0
                );
            }
        }

        orderedRenderCommandQueue.submitModelPart(
                this.model.getSpikes(),
                matrixStack,
                RenderTypes.entityCutoutNoCull(Sheets.SHIELD_SHEET),
                i,
                j,
                this.spriteHolder.get(spriteIdentifier),
                false,
                false,
                -1,
                null,
                k
        );

        matrixStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        PoseStack matrixStack = new PoseStack();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        this.model.root().getExtentsForGui(matrixStack, consumer);
    }

    @Environment(EnvType.CLIENT)
    public static record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final SpikedShieldModelRenderer.Unbaked INSTANCE = new SpikedShieldModelRenderer.Unbaked();
        public static final MapCodec<SpikedShieldModelRenderer.Unbaked> CODEC;

        public Unbaked() {
        }

        public MapCodec<SpikedShieldModelRenderer.Unbaked> type() {
            return CODEC;
        }

        public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext context) {
            return new SpikedShieldModelRenderer(
                    context.materials(),
                    new SpikedShieldEntityModel(
                            context.entityModelSet().bakeLayer(EarthWaterClient.SPIKED_SHIELD_MODEL_LAYER)
                    )
            );
        }

        static {
            CODEC = MapCodec.unit(INSTANCE);
        }
    }
}