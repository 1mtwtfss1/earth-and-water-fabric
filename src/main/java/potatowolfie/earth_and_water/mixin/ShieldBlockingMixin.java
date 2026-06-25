package potatowolfie.earth_and_water.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import potatowolfie.earth_and_water.item.custom.SpikedShieldItem;

@Mixin(LivingEntity.class)
public class ShieldBlockingMixin {

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void onSpikedShieldBlock(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.isBlocking()) {
            ItemStack activeItem = entity.getUseItem();
            if (activeItem.getItem() instanceof SpikedShieldItem) {
                if (entity instanceof Player player) {
                    if (player.getCooldowns().isOnCooldown(activeItem)) {
                        return;
                    }
                }

                if (source.is(net.minecraft.tags.DamageTypeTags.IS_PROJECTILE)) {
                    return;
                }

                if (source.getEntity() instanceof LivingEntity attacker) {
                    attacker.hurtServer(world, entity.damageSources().thorns(entity), 3.5F);

                    activeItem.hurtAndBreak(Math.max((int)amount, 1), entity, entity.getUsedItemHand() == null ?
                            (entity.getMainHandItem() == activeItem ?
                                    EquipmentSlot.MAINHAND :
                                    EquipmentSlot.OFFHAND) :
                            (entity.getUsedItemHand() == InteractionHand.MAIN_HAND ?
                                    EquipmentSlot.MAINHAND :
                                    EquipmentSlot.OFFHAND));
                }
            }
        }
    }
}