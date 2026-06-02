package potatowolfie.earth_and_water.mixin;

import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractCauldronBlock.class)
public interface AbstractCauldronBlockMixin {
    @Invoker("canReceiveStalactiteDrip")
    boolean invokeCanBeFilledByDripstone(Fluid fluid);
}