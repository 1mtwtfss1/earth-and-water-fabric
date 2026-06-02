package potatowolfie.earth_and_water.entity.brine;

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
public class BrineEntityRenderer extends MobRenderer<BrineEntity, BrineEntityRenderState, BrineEntityModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "textures/entity/brine/brine.png");

    public BrineEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new BrineEntityModel(context.bakeLayer(ModEntityModelLayers.BRINE)), 0.5F);
        this.addLayer(new BrineEntityEyesFeatureRenderer(this));
    }

    public void render(BrineEntityRenderState brineEntityRenderState, PoseStack matrixStack, SubmitNodeCollector queue, CameraRenderState cameraRenderState) {
        BrineEntityModel brineEntityModel = (BrineEntityModel)this.getModel();
        updatePartVisibility(brineEntityModel, brineEntityModel.getHead(), brineEntityModel.getRodsTop(), brineEntityModel.getRodsBottom());
        super.submit(brineEntityRenderState, matrixStack, queue, cameraRenderState);
    }

    public Identifier getTexture(BrineEntityRenderState brineEntityRenderState) {
        return TEXTURE;
    }

    public BrineEntityRenderState createRenderState() {
        return new BrineEntityRenderState();
    }

    public void updateRenderState(BrineEntity brineEntity, BrineEntityRenderState brineEntityRenderState, float f) {
        super.extractRenderState(brineEntity, brineEntityRenderState, f);
        brineEntityRenderState.idleAnimationState.copyFrom(brineEntity.idleAnimationState);
        brineEntityRenderState.underwaterAnimationState.copyFrom(brineEntity.underwaterAnimationState);
        brineEntityRenderState.attackAnimationState.copyFrom(brineEntity.attackAnimationState);
    }


    public static BrineEntityModel updatePartVisibility(BrineEntityModel model, ModelPart... modelParts) {
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