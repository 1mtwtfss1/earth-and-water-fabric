package potatowolfie.earth_and_water.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import potatowolfie.earth_and_water.item.ModItems;

import java.util.function.Consumer;

@Mixin(SmithingTemplateItem.class)
public class SmithingTemplateItemMixin {

    @Unique
    private static final Component STEEL_UPGRADE_APPLIES_TO_TEXT = Component.translatable(
                    Util.makeDescriptionId("item",
                            Identifier.fromNamespaceAndPath("earth-and-water", "smithing_template.steel_upgrade.applies_to")))
            .withStyle(ChatFormatting.BLUE);

    @Unique
    private static final Component STEEL_UPGRADE_INGREDIENTS_TEXT = Component.translatable(
                    Util.makeDescriptionId("item",
                            Identifier.fromNamespaceAndPath("earth-and-water", "smithing_template.steel_upgrade.ingredients")))
            .withStyle(ChatFormatting.BLUE);

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void injectSteelUpgradeTooltip(ItemStack stack, Item.TooltipContext context,
                                           TooltipDisplay displayComponent,
                                           Consumer<Component> textConsumer, TooltipFlag type,
                                           CallbackInfo ci) {

        if (stack.getItem() == ModItems.STEEL_UPGRADE_SMITHING_TEMPLATE) {
            textConsumer.accept(Component.translatable(
                            Util.makeDescriptionId("item",
                                    Identifier.withDefaultNamespace("smithing_template")))
                    .withStyle(ChatFormatting.GRAY));

            textConsumer.accept(CommonComponents.EMPTY);

            textConsumer.accept(Component.translatable(
                            Util.makeDescriptionId("item",
                                    Identifier.withDefaultNamespace("smithing_template.applies_to")))
                    .withStyle(ChatFormatting.GRAY));

            textConsumer.accept(CommonComponents.space().append(STEEL_UPGRADE_APPLIES_TO_TEXT));

            textConsumer.accept(Component.translatable(
                            Util.makeDescriptionId("item",
                                    Identifier.withDefaultNamespace("smithing_template.ingredients")))
                    .withStyle(ChatFormatting.GRAY));

            textConsumer.accept(CommonComponents.space().append(STEEL_UPGRADE_INGREDIENTS_TEXT));

            ci.cancel();
        }
    }
}