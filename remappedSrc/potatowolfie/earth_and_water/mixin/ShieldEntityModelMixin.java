package potatowolfie.earth_and_water.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;
import net.minecraft.client.model.object.equipment.ShieldModel;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

@Mixin(ShieldModel.class)
public class ShieldEntityModelMixin {

    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/Model;<init>(Lnet/minecraft/client/model/geom/ModelPart;Ljava/util/function/Function;)V"
            ),
            index = 1
    )
    private static Function<Identifier, RenderType> useEntityCutoutForSpikedShield(Function<Identifier, RenderType> layerFactory) {
        return RenderTypes::entityCutoutNoCull;
    }
}