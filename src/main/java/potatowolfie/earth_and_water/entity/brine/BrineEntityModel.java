package potatowolfie.earth_and_water.entity.brine;

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
import potatowolfie.earth_and_water.animation.BrineAnimations;

// Made with Blockbench 4.12.4

@Environment(EnvType.CLIENT)
public class BrineEntityModel extends EntityModel<BrineEntityRenderState> {
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart shell1;
	private final ModelPart shell2;
	private final ModelPart rods_top;
	private final ModelPart rods_bottom;
	private final ModelPart eyes;
	private final KeyframeAnimation idlingAnimation;
	private final KeyframeAnimation underwateridlingAnimation;
	private final KeyframeAnimation shootingAnimation;

	public BrineEntityModel(ModelPart modelPart) {
		super(modelPart);
		this.head = modelPart.getChild("head");
		this.body = modelPart.getChild("body");
		this.shell1 = this.body.getChild("shell1");
		this.shell2 = this.body.getChild("shell2");
		this.rods_top = this.body.getChild("rods_top");
		this.rods_bottom = this.body.getChild("rods_bottom");
		this.eyes = this.head.getChild("eyes");
		this.idlingAnimation = BrineAnimations.BRINE_IDLE.bake(modelPart);
		this.underwateridlingAnimation = BrineAnimations.BRINE_UNDERWATER.bake(modelPart);
		this.shootingAnimation = BrineAnimations.BRINE_SHOOTING.bake(modelPart);
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

		head.addOrReplaceChild("eyes", CubeListBuilder.create()
						.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.01F, 8.0F, 8.0F, 0.0F),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 24).addBox(-1.5F, -12.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 0.0F));

		PartDefinition shell1 = body.addOrReplaceChild("shell1", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -11.0F, 0.0F));

		PartDefinition shell2 = body.addOrReplaceChild("shell2", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, -11.0F, 0.0F));

		PartDefinition rods_top = body.addOrReplaceChild("rods_top", CubeListBuilder.create(), PartPose.offset(0.0F, -13.0F, 0.0F));

		PartDefinition rod1 = rods_top.addOrReplaceChild("rod1", CubeListBuilder.create().texOffs(12, 16).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 4.0F, -6.0F));

		PartDefinition rod2 = rods_top.addOrReplaceChild("rod2", CubeListBuilder.create().texOffs(12, 16).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 4.0F, 6.0F));

		PartDefinition rod3 = rods_top.addOrReplaceChild("rod3", CubeListBuilder.create().texOffs(12, 16).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 4.0F, 6.0F));

		PartDefinition rod4 = rods_top.addOrReplaceChild("rod4", CubeListBuilder.create().texOffs(12, 16).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 4.0F, -6.0F));

		PartDefinition rods_bottom = body.addOrReplaceChild("rods_bottom", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition rod5 = rods_bottom.addOrReplaceChild("rod5", CubeListBuilder.create().texOffs(20, 16).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, -5.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition rod6 = rods_bottom.addOrReplaceChild("rod6", CubeListBuilder.create().texOffs(20, 16).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

		PartDefinition rod7 = rods_bottom.addOrReplaceChild("rod7", CubeListBuilder.create().texOffs(20, 16).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 5.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition rod8 = rods_bottom.addOrReplaceChild("rod8", CubeListBuilder.create().texOffs(20, 16).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.1309F));
		return LayerDefinition.create(modelData, 32, 32);
	}

	public void setupAnim(BrineEntityRenderState brineEntityRenderState) {
		super.setupAnim(brineEntityRenderState);

		this.idlingAnimation.apply(brineEntityRenderState.idleAnimationState, brineEntityRenderState.ageInTicks);
		this.shootingAnimation.apply(brineEntityRenderState.attackAnimationState, brineEntityRenderState.ageInTicks);
		this.underwateridlingAnimation.apply(brineEntityRenderState.underwaterAnimationState, brineEntityRenderState.ageInTicks);
	}

	public ModelPart getHead() {
		return this.head;
	}

	public ModelPart getEyes() {
		return this.eyes;
	}

	public ModelPart getShell1() {
		return this.shell1;
	}

	public ModelPart getShell2() {
		return this.shell2;
	}

	public ModelPart getRodsTop() {
		return this.rods_top;
	}

	public ModelPart getRodsBottom() {
		return this.rods_bottom;
	}
}