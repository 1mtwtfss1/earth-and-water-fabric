package potatowolfie.earth_and_water.entity.bore;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import potatowolfie.earth_and_water.EarthWater;
import potatowolfie.earth_and_water.entity.client.ModEntityModelLayers;

@Environment(EnvType.CLIENT)
public class BoreEntityRenderer extends MobRenderer<BoreEntity, BoreEntityRenderState, BoreEntityModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "textures/entity/bore/bore.png");
    private static final Identifier DARK_TEXTURE = Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "textures/entity/bore/dark_bore.png");

    public BoreEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new BoreEntityModel(context.bakeLayer(ModEntityModelLayers.BORE)), 0.3f);
        this.addLayer(new BoreEntityEyesFeatureRenderer(this));
    }

    public void render(BoreEntityRenderState boreEntityRenderState, PoseStack matrixStack, SubmitNodeCollector queue, CameraRenderState cameraRenderState) {
        BoreEntityModel boreEntityModel = (BoreEntityModel)this.getModel();
        updatePartVisibility(boreEntityModel, boreEntityModel.getHead(), boreEntityModel.getRodsTop(), boreEntityModel.getRodsBottom());
        super.submit(boreEntityRenderState, matrixStack, queue, cameraRenderState);
    }

    public Identifier getTexture(BoreEntityRenderState boreEntityRenderState) {
        return switch (boreEntityRenderState.variant) {
            case NORMAL -> TEXTURE;
            case DARK -> DARK_TEXTURE;
        };
    }

    public BoreEntityRenderState createRenderState() {
        return new BoreEntityRenderState();
    }

    public void updateRenderState(BoreEntity boreEntity, BoreEntityRenderState boreEntityRenderState, float f) {
        super.extractRenderState(boreEntity, boreEntityRenderState, f);
        boreEntityRenderState.idleAnimationState.copyFrom(boreEntity.idleAnimationState);
        boreEntityRenderState.shootingAnimationState.copyFrom(boreEntity.shootingAnimationState);
        boreEntityRenderState.burrowingAnimationState.copyFrom(boreEntity.burrowingAnimationState);
        boreEntityRenderState.unburrowingAnimationState.copyFrom(boreEntity.unburrowingAnimationState);
        boreEntityRenderState.whileburrowingAnimationState.copyFrom(boreEntity.whileburrowingAnimationState);
        boreEntityRenderState.variant = boreEntity.getVariant();
    }

    public static BoreEntityModel updatePartVisibility(BoreEntityModel model, ModelPart... modelParts) {
        model.getHead().visible = false;
        model.getEyes().visible = false;
        model.getRodsTop().visible = false;
        model.getRodsBottom().visible = false;
        ModelPart[] var2 = modelParts;
        int var3 = modelParts.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ModelPart modelPart = var2[var4];
            modelPart.visible = true;
        }

        return model;
    }
}