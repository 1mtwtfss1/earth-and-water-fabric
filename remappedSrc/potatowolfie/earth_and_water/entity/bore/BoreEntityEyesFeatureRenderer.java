package potatowolfie.earth_and_water.entity.bore;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import potatowolfie.earth_and_water.EarthWater;

@Environment(EnvType.CLIENT)
public class BoreEntityEyesFeatureRenderer extends EyesLayer<BoreEntityRenderState, BoreEntityModel> {
    private static final RenderType SKIN = RenderTypes.eyes(Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "textures/entity/bore/bore_eyes.png"));

    public BoreEntityEyesFeatureRenderer(RenderLayerParent<BoreEntityRenderState, BoreEntityModel> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public RenderType renderType() {
        return SKIN;
    }
}