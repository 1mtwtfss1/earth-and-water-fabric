package potatowolfie.earth_and_water.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.item.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import potatowolfie.earth_and_water.damage.ModDamageTypes;

import java.util.List;
import java.util.function.Consumer;

public class SpikedShieldItem extends ShieldItem {

    public SpikedShieldItem(Item.Properties settings) {
        super(settings);
    }

    public Component getName(ItemStack stack) {
        DyeColor dyeColor = (DyeColor)stack.get(DataComponents.BASE_COLOR);
        if (dyeColor != null) {
            String var10000 = this.descriptionId;
            return Component.translatable(var10000 + "." + dyeColor.getName());
        } else {
            return super.getName(stack);
        }
    }

    public boolean handleExplosiveDamage(LivingEntity user, DamageSource damageSource, float amount) {
        if (!user.isBlocking()) {
            return false;
        }

        if (isExplosionDamage(damageSource)) {
            LivingEntity attacker = getActualAttacker(damageSource);

            if (attacker != null && attacker != user) {
                if (user.level() instanceof ServerLevel serverWorld) {
                    DamageSource spikedShieldDamage = new DamageSource(
                            serverWorld.registryAccess()
                                    .lookupOrThrow(Registries.DAMAGE_TYPE)
                                    .get(ModDamageTypes.SPIKED_SHIELD.identifier()).get(),
                            user
                    );
                    attacker.hurtServer(serverWorld, spikedShieldDamage, amount);
                }

                return true;
            }

            return true;
        }

        return false;
    }

    private boolean isExplosionDamage(DamageSource damageSource) {
        if (damageSource.is(net.minecraft.world.damagesource.DamageTypes.EXPLOSION) ||
                damageSource.is(net.minecraft.world.damagesource.DamageTypes.PLAYER_EXPLOSION)) {
            return true;
        }

        if (damageSource.getDirectEntity() != null || damageSource.getEntity() != null) {
            String sourceName = damageSource.getMsgId();
            if (sourceName != null && sourceName.toLowerCase().contains("explosion")) {
                return true;
            }
        }

        return false;
    }

    private LivingEntity getActualAttacker(DamageSource damageSource) {
        if (damageSource.getEntity() instanceof LivingEntity livingAttacker) {
            return livingAttacker;
        }

        if (damageSource.getDirectEntity() instanceof LivingEntity livingSource) {
            return livingSource;
        }

        if (damageSource.getDirectEntity() != null) {
            var source = damageSource.getDirectEntity();
            if (source instanceof net.minecraft.world.entity.projectile.Projectile projectile) {
                if (projectile.getOwner() instanceof LivingEntity owner) {
                    return owner;
                }
            }
        }

        return null;
    }
}