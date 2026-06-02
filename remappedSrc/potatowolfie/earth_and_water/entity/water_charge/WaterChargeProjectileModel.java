package potatowolfie.earth_and_water.entity.water_charge;

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
public class WaterChargeProjectileModel extends EntityModel<WaterChargeProjectileRenderState> {
	private final ModelPart water_charge;

	public WaterChargeProjectileModel(ModelPart modelPart) {
		super(modelPart);
		this.water_charge = modelPart.getChild("water_charge");
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition water_charge = modelPartData.addOrReplaceChild("water_charge", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 8).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = water_charge.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(9, 0).addBox(-1.0F, 3.2321F, -1.4019F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, -2.0944F));

		PartDefinition cube_r2 = water_charge.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(9, 0).addBox(-1.0F, 3.2321F, -1.4019F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.0944F, 0.0F, 0.0F));

		PartDefinition cube_r3 = water_charge.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(9, 0).addBox(-1.0F, -3.2321F, -1.4019F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, -1.0472F));

		PartDefinition cube_r4 = water_charge.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(9, 0).addBox(-1.0F, -3.2321F, -1.4019F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0472F, 0.0F, 0.0F));

		return LayerDefinition.create(modelData, 32, 32);
	}

	public void setAngles(WaterChargeProjectileRenderState waterChargeProjectileRenderState) {
		super.setupAnim(waterChargeProjectileRenderState);
	}

	public ModelPart getWaterCharge() {
		return this.water_charge;
	}
}