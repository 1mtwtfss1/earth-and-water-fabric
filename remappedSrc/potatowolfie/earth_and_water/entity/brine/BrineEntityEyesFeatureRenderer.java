package potatowolfie.earth_and_water.entity.brine;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import potatowolfie.earth_and_water.EarthWater;

@Environment(EnvType.CLIENT)
public class BrineEntityEyesFeatureRenderer extends EyesLayer<BrineEntityRenderState, BrineEntityModel> {
    private static final RenderType SKIN = RenderTypes.eyes(Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "textures/entity/brine/brine_eyes.png"));

    public BrineEntityEyesFeatureRenderer(RenderLayerParent<BrineEntityRenderState, BrineEntityModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public RenderType renderType() {
        return SKIN;
    }
}