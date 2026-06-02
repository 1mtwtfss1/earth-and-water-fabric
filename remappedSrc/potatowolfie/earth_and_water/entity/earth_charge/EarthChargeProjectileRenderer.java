package potatowolfie.earth_and_water.entity.earth_charge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import potatowolfie.earth_and_water.EarthWater;
import potatowolfie.earth_and_water.entity.client.ModEntityModelLayers;

@Environment(EnvType.CLIENT)
public class EarthChargeProjectileRenderer extends EntityRenderer<EarthChargeProjectileEntity, EarthChargeProjectileRenderState> {
    private static final float field_52258 = Mth.square(3.5F);
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "textures/entity/earth_charge/earth_charge.png");
    protected EarthChargeProjectileModel model;

    public EarthChargeProjectileRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        model = new EarthChargeProjectileModel(ctx.bakeLayer(ModEntityModelLayers.EARTH_CHARGE));
    }

    public void render(EarthChargeProjectileRenderState earthChargeProjectileRenderState, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        if (earthChargeProjectileRenderState.ageInTicks >= 2 || earthChargeProjectileRenderState.distanceFromCamera >= field_52258) {
            matrixStack.pushPose();

            matrixStack.translate(0, 1.525, 0);

            matrixStack.mulPose(Axis.XP.rotationDegrees(180));

            matrixStack.mulPose(Axis.YP.rotationDegrees(earthChargeProjectileRenderState.renderingRotation));

            this.model.setAngles(earthChargeProjectileRenderState);
            orderedRenderCommandQueue.submitModelPart(
                    this.model.root(),
                    matrixStack,
                    this.model.renderType(TEXTURE),
                    earthChargeProjectileRenderState.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    null,
                    false,
                    false,
                    -1,
                    null,
                    0
            );

            matrixStack.popPose();
            super.submit(earthChargeProjectileRenderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
        }
    }

    public Identifier getTexture(EarthChargeProjectileRenderState earthChargeProjectileRenderState) {
        return TEXTURE;
    }

    public EarthChargeProjectileRenderState createRenderState() {
        return new EarthChargeProjectileRenderState();
    }

    public void updateRenderState(EarthChargeProjectileEntity earthChargeProjectileEntity, EarthChargeProjectileRenderState earthChargeProjectileRenderState, float f) {
        super.extractRenderState(earthChargeProjectileEntity, earthChargeProjectileRenderState, f);
        earthChargeProjectileRenderState.renderingRotation = earthChargeProjectileEntity.getRenderingRotation();
        earthChargeProjectileRenderState.distanceFromCamera = (float) earthChargeProjectileEntity.distanceToSqr(
                earthChargeProjectileRenderState.x,
                earthChargeProjectileRenderState.y,
                earthChargeProjectileRenderState.z
        );
    }
}