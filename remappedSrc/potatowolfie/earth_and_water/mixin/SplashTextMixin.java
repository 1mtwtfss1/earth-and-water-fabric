package potatowolfie.earth_and_water.mixin;

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
    private List<String> splashes;

    @Inject(method = "apply*",
            at = @At("TAIL"))
    private void addEANDWSplashes(CallbackInfo ci) {
        splashes = new ArrayList<>(splashes);

        splashes.add(Component.translatable("splash.earth-and-water.sticks_stone").getString());
        splashes.add(Component.translatable("splash.earth-and-water.herobrine").getString());
        splashes.add(Component.translatable("splash.earth-and-water.september").getString());
        splashes.add(Component.translatable("splash.earth-and-water.w_e_f_a").getString());
        splashes.add(Component.translatable("splash.earth-and-water.w_e_f_a").getString());
        splashes.add(Component.translatable("splash.earth-and-water.drowning").getString());
        splashes.add(Component.translatable("splash.earth-and-water.splash").getString());
        splashes.add(Component.translatable("splash.earth-and-water.hardlyknower").getString());
        splashes.add(Component.translatable("splash.earth-and-water.removed_the_brine").getString());
        splashes.add(Component.translatable("splash.earth-and-water.bore_you").getString());
        splashes.add(Component.translatable("splash.earth-and-water.sweet_carobrine").getString());
        splashes.add(Component.translatable("splash.earth-and-water.sun_tzu").getString());
        splashes.add(Component.translatable("splash.earth-and-water.bore_d").getString());
    }
}