package potatowolfie.earth_and_water.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import potatowolfie.earth_and_water.EarthWater;

public class ModEffects {
    public static final Holder<MobEffect> STUN = registerStatusEffect("stun",
            new UnderwaterStunEffect(MobEffectCategory.HARMFUL, 0xedc466)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED,
                            Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "underwater_stun"), -0.25f,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> BREATH_GIVING = registerStatusEffect("breath_giving",
            new BreathGivingEffect());


    private static Holder<MobEffect> registerStatusEffect(String name, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, name), statusEffect);
    }

    public static void registerEffects() {
        EarthWater.LOGGER.info("Registering Mod Effects for " + EarthWater.MOD_ID);
    }
}