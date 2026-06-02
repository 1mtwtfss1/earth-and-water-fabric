package potatowolfie.earth_and_water.entity.water_charge;

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
public class WaterChargeProjectileRenderer extends EntityRenderer<WaterChargeProjectileEntity, WaterChargeProjectileRenderState> {
    private static final float field_52258 = Mth.square(3.5F);
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "textures/entity/water_charge/water_charge.png");
    protected WaterChargeProjectileModel model;

    public WaterChargeProjectileRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        model = new WaterChargeProjectileModel(ctx.bakeLayer(ModEntityModelLayers.WATER_CHARGE));
    }

    public void render(WaterChargeProjectileRenderState waterChargeProjectileRenderState, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        if (waterChargeProjectileRenderState.ageInTicks >= 2 || waterChargeProjectileRenderState.distanceFromCamera >= field_52258) {
            matrixStack.pushPose();

            matrixStack.translate(0, 1.525, 0);

            if (waterChargeProjectileRenderState.isStuck) {
                if (waterChargeProjectileRenderState.isStuckToEntity) {
                    matrixStack.mulPose(Axis.YP.rotationDegrees(waterChargeProjectileRenderState.renderingRotation));
                } else {
                    matrixStack.mulPose(Axis.YP.rotationDegrees(waterChargeProjectileRenderState.yaw));
                    matrixStack.mulPose(Axis.XP.rotationDegrees(waterChargeProjectileRenderState.pitch));
                }
            } else if (waterChargeProjectileRenderState.isGrounded) {
                matrixStack.mulPose(Axis.YP.rotationDegrees(waterChargeProjectileRenderState.yaw));
                matrixStack.mulPose(Axis.XP.rotationDegrees(waterChargeProjectileRenderState.pitch));
            }

            matrixStack.mulPose(Axis.XP.rotationDegrees(180));

            if (!waterChargeProjectileRenderState.isStuck) {
                matrixStack.mulPose(Axis.YP.rotationDegrees(waterChargeProjectileRenderState.renderingRotation));
            }

            this.model.setAngles(waterChargeProjectileRenderState);
            orderedRenderCommandQueue.submitModelPart(
                    this.model.root(),
                    matrixStack,
                    this.model.renderType(TEXTURE),
                    waterChargeProjectileRenderState.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    null,
                    false,
                    false,
                    -1,
                    null,
                    0
            );

            matrixStack.popPose();
            super.submit(waterChargeProjectileRenderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
        }
    }

    public Identifier getTexture(WaterChargeProjectileRenderState waterChargeProjectileRenderState) {
        return TEXTURE;
    }

    public WaterChargeProjectileRenderState createRenderState() {
        return new WaterChargeProjectileRenderState();
    }

    public void updateRenderState(WaterChargeProjectileEntity waterChargeProjectileEntity, WaterChargeProjectileRenderState waterChargeProjectileRenderState, float f) {
        super.extractRenderState(waterChargeProjectileEntity, waterChargeProjectileRenderState, f);
        waterChargeProjectileRenderState.isStuck = waterChargeProjectileEntity.isStuck();
        waterChargeProjectileRenderState.isStuckToEntity = waterChargeProjectileEntity.isStuckToEntity();
        waterChargeProjectileRenderState.isGrounded = waterChargeProjectileEntity.isGrounded();
        waterChargeProjectileRenderState.renderingRotation = waterChargeProjectileEntity.getRenderingRotation();
        waterChargeProjectileRenderState.yaw = waterChargeProjectileEntity.getYRot();
        waterChargeProjectileRenderState.pitch = waterChargeProjectileEntity.getXRot();
        waterChargeProjectileRenderState.distanceFromCamera = (float) waterChargeProjectileEntity.distanceToSqr(
                waterChargeProjectileRenderState.x,
                waterChargeProjectileRenderState.y,
                waterChargeProjectileRenderState.z
        );
    }
}