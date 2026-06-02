package potatowolfie.earth_and_water.effect;

import java.util.UUID;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class UnderwaterStunEffect extends MobEffect {
    private static final UUID MOVEMENT_SPEED_MODIFIER_ID = UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890");
    private static final UUID ATTACK_DAMAGE_MODIFIER_ID = UUID.fromString("22653B89-116E-49DC-9B6B-9971489B5BE5");

    protected UnderwaterStunEffect(MobEffectCategory category, int color) {
        super(category, color);

        this.addAttributeModifier(
            Attributes.MOVEMENT_SPEED,
                Identifier.parse(MOVEMENT_SPEED_MODIFIER_ID.toString()),
            -0.8,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        this.addAttributeModifier(
            Attributes.ATTACK_DAMAGE,
                Identifier.parse(ATTACK_DAMAGE_MODIFIER_ID.toString()),
            -0.65,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }

    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
