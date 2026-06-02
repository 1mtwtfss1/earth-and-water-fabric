package potatowolfie.earth_and_water.entity.bore;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import potatowolfie.earth_and_water.animation.BoreAnimations;

// Made with Blockbench 4.12.4

@Environment(EnvType.CLIENT)
public class BoreEntityModel extends EntityModel<BoreEntityRenderState> {
	private final ModelPart head;
	private final ModelPart eyes;
	private final ModelPart rods_top;
	private final ModelPart rods_bottom;
	private final KeyframeAnimation idleAnimation;
	private final KeyframeAnimation shootingAnimation;
	private final KeyframeAnimation burrowingAnimation;
	private final KeyframeAnimation unburrowingAnimation;
	private final KeyframeAnimation whileburrowingAnimation;

	public BoreEntityModel(ModelPart modelPart) {
		super(modelPart);
		this.head = modelPart.getChild("head");
		this.eyes = this.head.getChild("eyes");
		this.rods_top = modelPart.getChild("rods_top");
		this.rods_bottom = modelPart.getChild("rods_bottom");
		this.idleAnimation = BoreAnimations.BORE_IDLE.bake(modelPart);
		this.shootingAnimation = BoreAnimations.BORE_SHOOTING.bake(modelPart);
		this.burrowingAnimation = BoreAnimations.BORE_BURROWING.bake(modelPart);
		this.unburrowingAnimation = BoreAnimations.BORE_UNBURROWING.bake(modelPart);
		this.whileburrowingAnimation = BoreAnimations.BURROWING.bake(modelPart);
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

		PartDefinition head = modelPartData.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 4.0F, 0.0F));

		head.addOrReplaceChild("eyes",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(0, 9).addBox(0.0F, -6.0F, -3.5F, 0.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -2.3562F, 0.0F));

		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(0, 9).addBox(0.0F, -6.0F, -3.5F, 0.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition rods_top = modelPartData.addOrReplaceChild("rods_top",
				CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		rods_top.addOrReplaceChild("rod1",
				CubeListBuilder.create().texOffs(14, 16)
						.addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-5.0F, 0.0F, -5.0F));

		rods_top.addOrReplaceChild("rod2",
				CubeListBuilder.create().texOffs(14, 16)
						.addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(5.0F, 0.0F, -5.0F));

		rods_top.addOrReplaceChild("rod3",
				CubeListBuilder.create().texOffs(14, 16)
						.addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(5.0F, 0.0F, 5.0F));

		rods_top.addOrReplaceChild("rod4",
				CubeListBuilder.create().texOffs(14, 16)
						.addBox(-1.0F, -4.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-5.0F, 0.0F, 5.0F));

		PartDefinition rods_bottom = modelPartData.addOrReplaceChild("rods_bottom",
				CubeListBuilder.create(), PartPose.offset(0.0F, 11.0F, 0.0F));

		rods_bottom.addOrReplaceChild("rod_bottom1",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 22).mirror()
						.addBox(-2.0F, 6.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(2.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

		rods_bottom.addOrReplaceChild("rod_bottom2",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 22)
						.addBox(-1.0F, 6.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, -0.0078F, -0.0231F, -0.1285F));

		return LayerDefinition.create(modelData, 32, 32);
	}

	public void setAngles(BoreEntityRenderState boreEntityRenderState) {
		super.setupAnim(boreEntityRenderState);

		this.idleAnimation.apply(boreEntityRenderState.idleAnimationState, boreEntityRenderState.ageInTicks);
		this.shootingAnimation.apply(boreEntityRenderState.shootingAnimationState, boreEntityRenderState.ageInTicks);
		this.burrowingAnimation.apply(boreEntityRenderState.burrowingAnimationState, boreEntityRenderState.ageInTicks);
		this.unburrowingAnimation.apply(boreEntityRenderState.unburrowingAnimationState, boreEntityRenderState.ageInTicks);
		this.whileburrowingAnimation.apply(boreEntityRenderState.whileburrowingAnimationState, boreEntityRenderState.ageInTicks);
	}

	public ModelPart getHead() {
		return this.head;
	}

	public ModelPart getEyes() {
		return this.eyes;
	}

	public ModelPart getRodsTop() {
		return this.rods_top;
	}

	public ModelPart getRodsBottom() {
		return this.rods_bottom;
	}
}