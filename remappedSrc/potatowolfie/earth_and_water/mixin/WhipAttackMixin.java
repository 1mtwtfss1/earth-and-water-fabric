package potatowolfie.earth_and_water.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import potatowolfie.earth_and_water.item.custom.WhipItem;

@Mixin(Player.class)
public abstract class WhipAttackMixin {

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void onAttack(Entity target, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        ItemStack stack = player.getMainHandItem();

        if (!(stack.getItem() instanceof WhipItem)) {
            return;
        }
        if (!(target instanceof LivingEntity livingTarget)) {
            return;
        }
        if (player.level().isClientSide()) {
            return;
        }
        ci.cancel();
        DamageSource damageSource = player.damageSources().playerAttack(player);
        float damage = player.isEyeInFluid(FluidTags.WATER) ? 7.0f : 3.5f;
        livingTarget.hurtServer((ServerLevel) player.level(), damageSource, damage);
        stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
        stack.getItem().hurtEnemy(stack, livingTarget, player);
        player.getLastHurtByMobTimestamp();
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP,
                player.getSoundSource(), 1.0F, 1.0F);
    }
}