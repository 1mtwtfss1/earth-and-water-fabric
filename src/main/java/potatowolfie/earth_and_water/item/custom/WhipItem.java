package potatowolfie.earth_and_water.item.custom;

import potatowolfie.earth_and_water.damage.ModDamageTypes;
import potatowolfie.earth_and_water.effect.ModEffects;

import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class WhipItem extends Item {

    public WhipItem(ToolMaterial toolMaterial, Item.Properties settings) {
        super(settings);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.isEyeInFluid(FluidTags.WATER)) {
            target.addEffect(new MobEffectInstance(ModEffects.STUN, 40, 1));

            if (attacker.level() instanceof ServerLevel serverWorld) {
                DamageSource whipDamage = new DamageSource(
                        serverWorld.registryAccess()
                                .lookupOrThrow(Registries.DAMAGE_TYPE)
                                .get(ModDamageTypes.WHIP.identifier()).get(),
                        attacker
                );
                target.hurtServer(serverWorld, whipDamage, 1.0f);
            }
        }
        super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.translatable("tooltip.earth-and-water.tooltipempty"));
        textConsumer.accept(Component.translatable("tooltip.earth-and-water.whip.tooltip1"));
        textConsumer.accept(Component.translatable("tooltip.earth-and-water.whip.tooltip2"));
        textConsumer.accept(Component.translatable("tooltip.earth-and-water.tooltipempty"));
        textConsumer.accept(Component.translatable("tooltip.earth-and-water.whip.tooltip3"));
        textConsumer.accept(Component.translatable("tooltip.earth-and-water.whip.tooltip4"));
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
    }
}