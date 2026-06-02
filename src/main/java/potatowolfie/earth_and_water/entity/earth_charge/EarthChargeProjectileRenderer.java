package potatowolfie.earth_and_water.entity.earth_charge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
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

    @Override
    public void submit(final EarthChargeProjectileRenderState state,
                       final PoseStack poseStack,
                       final SubmitNodeCollector submitNodeCollector,
                       final CameraRenderState camera) {

        if (state.ageInTicks >= 2 || state.distanceFromCamera >= field_52258) {

            poseStack.pushPose();

            poseStack.translate(0.0, 1.525, 0.0);
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(state.renderingRotation));

            this.model.setAngles(state);

            submitNodeCollector.submitModelPart(
                    this.model.root(),
                    poseStack,
                    this.model.renderType(TEXTURE),
                    state.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    null,
                    false,
                    false,
                    -1,
                    null,
                    0
            );

            poseStack.popPose();

            super.submit(state, poseStack, submitNodeCollector, camera);
        }
    }

    public Identifier getTexture(EarthChargeProjectileRenderState earthChargeProjectileRenderState) {
        return TEXTURE;
    }

    public EarthChargeProjectileRenderState createRenderState() {
        return new EarthChargeProjectileRenderState();
    }

    @Override
    public void extractRenderState(final EarthChargeProjectileEntity entity,
                                   final EarthChargeProjectileRenderState state,
                                   final float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        state.renderingRotation = entity.getRenderingRotation();

        state.distanceFromCamera = (float) entity.distanceToSqr(
                state.x,
                state.y,
                state.z
        );
    }
}