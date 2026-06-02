package potatowolfie.earth_and_water.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class BreathGivingEffect extends MobEffect {
    private static final int UPDATE_INTERVAL = 1;
    private static final int BASE_AIR_PER_UPDATE = 15;
    private static final float PARTIAL_AIR_THRESHOLD = 0.9f;
    private final Map<UUID, Float> partialAirValues = new HashMap<>();

    public BreathGivingEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x3CB4FF);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % UPDATE_INTERVAL == 0;
    }

    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity == null) {
            return false;
        }

        int currentAir = entity.getAirSupply();
        int maxAir = entity.getMaxAirSupply();
        boolean isCreativeOrSpectator = false;

        if (entity instanceof Player) {
            Player player = (Player) entity;
            isCreativeOrSpectator = player.isSpectator() || player.getAbilities().instabuild;
        }

        if (currentAir < maxAir || isCreativeOrSpectator) {
            UUID entityId = entity.getUUID();
            float partialAir = partialAirValues.getOrDefault(entityId, 0f);
            float airToAddFloat = BASE_AIR_PER_UPDATE * (amplifier + 1);

            partialAir += airToAddFloat;
            int wholeAirToAdd = (int) partialAir;
            float remainder = partialAir - wholeAirToAdd;

            if (remainder > PARTIAL_AIR_THRESHOLD) {
                wholeAirToAdd++;
                remainder = 0;
            }

            partialAirValues.put(entityId, remainder);

            if (wholeAirToAdd > 0) {
                entity.setAirSupply(Math.min(currentAir + wholeAirToAdd, maxAir));
            }

            if (currentAir + wholeAirToAdd >= maxAir && !isCreativeOrSpectator) {
                partialAirValues.remove(entityId);
            }

            return true;
        } else {
            partialAirValues.remove(entity.getUUID());
        }

        return false;
    }
}