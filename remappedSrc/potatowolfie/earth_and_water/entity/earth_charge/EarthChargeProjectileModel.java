package potatowolfie.earth_and_water.entity.earth_charge;

// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17+ for Yarn

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

@Environment(EnvType.CLIENT)
public class EarthChargeProjectileModel extends EntityModel<EarthChargeProjectileRenderState> {
	private final ModelPart earth_charge;

	public EarthChargeProjectileModel(ModelPart modelPart) {
		super(modelPart);
		this.earth_charge = modelPart.getChild("earth_charge");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition earth_charge = modelPartData.addOrReplaceChild("earth_charge", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = earth_charge.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(2.0F, -2.9142F, -1.4142F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 8).addBox(-5.0F, -2.9142F, -1.4142F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

		PartDefinition cube_r2 = earth_charge.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(2.0F, -2.9142F, 1.4142F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 8).addBox(-5.0F, -2.9142F, 1.4142F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, -0.7854F));

		PartDefinition cube_r3 = earth_charge.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(2.0F, -2.9142F, 1.4142F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 8).addBox(-5.0F, -2.9142F, 1.4142F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r4 = earth_charge.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 8).mirror().addBox(2.0F, -2.9142F, -1.4142F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 8).addBox(-5.0F, -2.9142F, -1.4142F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(modelData, 16, 16);
	}

	public void setAngles(EarthChargeProjectileRenderState earthChargeProjectileRenderState) {
		super.setupAnim(earthChargeProjectileRenderState);
	}

	public ModelPart getEarthCharge() {
		return this.earth_charge;
	}
}