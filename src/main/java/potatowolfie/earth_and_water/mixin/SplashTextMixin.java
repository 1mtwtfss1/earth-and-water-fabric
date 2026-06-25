package potatowolfie.earth_and_water.mixin;

import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.network.chat.Component;

@Mixin(SplashManager.class)
public class SplashTextMixin {

    @Shadow
    private List<Component> splashes;

    @Inject(method = "apply*", at = @At("TAIL"))
    private void addEANDWSplashes(CallbackInfo ci) {
        splashes = new ArrayList<>(splashes);

        Style yellowStyle = Style.EMPTY.withColor(0xFFFF00);

        splashes.add(Component.translatable("splash.earth-and-water.sticks_stone").setStyle(yellowStyle));
        splashes.add(Component.translatable("splash.earth-and-water.herobrine").setStyle(yellowStyle));
        splashes.add(Component.translatable("splash.earth-and-water.september").setStyle(yellowStyle));
        splashes.add(Component.translatable("splash.earth-and-water.w_e_f_a").setStyle(yellowStyle));
        splashes.add(Component.translatable("splash.earth-and-water.drowning").setStyle(yellowStyle));
        splashes.add(Component.translatable("splash.earth-and-water.splash").setStyle(yellowStyle));
        splashes.add(Component.translatable("splash.earth-and-water.hardlyknower").setStyle(yellowStyle));
        splashes.add(Component.translatable("splash.earth-and-water.removed_the_brine").setStyle(yellowStyle));
        splashes.add(Component.translatable("splash.earth-and-water.bore_you").setStyle(yellowStyle));
        splashes.add(Component.translatable("splash.earth-and-water.sweet_carobrine").setStyle(yellowStyle));
        splashes.add(Component.translatable("splash.earth-and-water.sun_tzu").setStyle(yellowStyle));
        splashes.add(Component.translatable("splash.earth-and-water.bore_d").setStyle(yellowStyle));
    }
}