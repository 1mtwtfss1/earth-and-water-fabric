package potatowolfie.earth_and_water.mixin;

import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import potatowolfie.earth_and_water.accessor.AbstractCauldronBlockAccessor;

@Mixin(AbstractCauldronBlock.class)
public interface AbstractCauldronBlockMixin extends AbstractCauldronBlockAccessor {

    @Invoker("canReceiveStalactiteDrip")
    @Override
    boolean earthAndWater$canReceiveStalactiteDrip(Fluid fluid);
}