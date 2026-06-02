package potatowolfie.earth_and_water.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import potatowolfie.earth_and_water.entity.brine.BrineSpectatorOverlayRenderer;

@Mixin(Gui.class)
public class BrineSpectatorMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void renderBrineSpectatorVision(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {
        BrineSpectatorOverlayRenderer.renderBrineVision(context, 1.0f);
    }
}