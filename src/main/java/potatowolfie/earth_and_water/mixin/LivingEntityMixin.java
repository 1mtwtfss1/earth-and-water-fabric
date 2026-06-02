package potatowolfie.earth_and_water.mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import potatowolfie.earth_and_water.damage.ModDamageTypes;
import potatowolfie.earth_and_water.item.ModItems;
import potatowolfie.earth_and_water.item.custom.BattleAxeItem;
import potatowolfie.earth_and_water.item.custom.SpikedShieldItem;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Unique
    private static final int SHIELD_DISABLE_DURATION = 100;

    @ModifyVariable(
            method = "hurtServer",
            at = @At("HEAD"),
            argsOnly = true
    )
    private DamageSource modifyDamageSource(DamageSource source) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (source.getEntity() instanceof Player player &&
                self.level() instanceof ServerLevel serverWorld) {

            if (player.isBlocking()) {
                ItemStack activeItem = player.getUseItem();
                if (activeItem.getItem() instanceof SpikedShieldItem) {
                    return new DamageSource(
                            serverWorld.registryAccess()
                                    .lookupOrThrow(Registries.DAMAGE_TYPE)
                                    .get(ModDamageTypes.SPIKED_SHIELD.identifier()).get(),
                            player
                    );
                }
            }

            if (player.getMainHandItem().is(ModItems.WHIP)) {
                return new DamageSource(
                        serverWorld.registryAccess()
                                .lookupOrThrow(Registries.DAMAGE_TYPE)
                                .get(ModDamageTypes.WHIP.identifier()).get(),
                        player
                );
            }

            if (player.getMainHandItem().is(ModItems.BATTLE_AXE)) {
                return new DamageSource(
                        serverWorld.registryAccess()
                                .lookupOrThrow(Registries.DAMAGE_TYPE)
                                .get(ModDamageTypes.BATTLE_AXE.identifier()).get(),
                        player
                );
            }
        }

        return source;
    }

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void onDamage(ServerLevel world, DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (damageSource.getEntity() instanceof LivingEntity attacker) {
            ItemStack weapon = attacker.getMainHandItem();

            if (weapon.getItem() instanceof BattleAxeItem) {
                if (self.isBlocking()) {
                    ItemStack shieldStack = self.getUseItem();
                    Item shieldItem = shieldStack.getItem();

                    if (shieldItem instanceof ShieldItem || shieldItem instanceof SpikedShieldItem) {
                        self.level().playSound(null,
                                self.getX(), self.getY(), self.getZ(),
                                SoundEvents.SHIELD_BREAK,
                                SoundSource.PLAYERS,
                                0.8F,
                                0.8F + self.level().getRandom().nextFloat() * 0.4F);

                        if (self instanceof Player playerTarget) {
                            playerTarget.getCooldowns().addCooldown(new ItemStack(Items.SHIELD), SHIELD_DISABLE_DURATION);

                            if (ModItems.SPIKED_SHIELD != null) {
                                playerTarget.getCooldowns().addCooldown(new ItemStack(ModItems.SPIKED_SHIELD), SHIELD_DISABLE_DURATION);
                            }

                            playerTarget.stopUsingItem();
                            playerTarget.level().broadcastEntityEvent(playerTarget, (byte)30);
                        } else {
                            self.stopUsingItem();
                            self.level().broadcastEntityEvent(self, (byte)30);
                        }

                        return;
                    }
                }
            }
        }

        if (self.isBlocking()) {
            ItemStack activeItem = self.getUseItem();
            if (activeItem.getItem() instanceof SpikedShieldItem spikedShield) {
                if (spikedShield.handleExplosiveDamage(self, damageSource, amount)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}