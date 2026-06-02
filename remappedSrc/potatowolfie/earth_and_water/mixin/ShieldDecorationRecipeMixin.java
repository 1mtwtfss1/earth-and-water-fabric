package potatowolfie.earth_and_water.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import potatowolfie.earth_and_water.util.ModTags;

// Code used from "More Shield Variants" by hypothetiKal and pnku under a MIT License

@Mixin(ShieldDecorationRecipe.class)
public abstract class ShieldDecorationRecipeMixin extends CustomRecipe {

    public ShieldDecorationRecipeMixin(CraftingBookCategory category) {
        super(category);
    }

    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    private void injectedMatches(CraftingInput input, Level world, CallbackInfoReturnable<Boolean> cir) {
        ItemStack shield = ItemStack.EMPTY;
        ItemStack banner = ItemStack.EMPTY;
        boolean hasSpikedShield = false;

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModTags.Item.SPIKED_SHIELD)) {
                hasSpikedShield = true;
                break;
            }
        }

        if (!hasSpikedShield) {
            return;
        }

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof BannerItem) {
                if (!banner.isEmpty()) {
                    cir.setReturnValue(false);
                    return;
                }
                banner = stack;
            } else if (stack.is(ModTags.Item.SPIKED_SHIELD)) {
                if (!shield.isEmpty()) {
                    cir.setReturnValue(false);
                    return;
                }

                BannerPatternLayers patterns = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
                if (!patterns.layers().isEmpty()) {
                    cir.setReturnValue(false);
                    return;
                }

                shield = stack;
            } else {
                cir.setReturnValue(false);
                return;
            }
        }

        cir.setReturnValue(!shield.isEmpty() && !banner.isEmpty());
    }

    @Inject(method = "assemble", at = @At("HEAD"), cancellable = true)
    private void injectedCraft(CraftingInput input, HolderLookup.Provider lookup, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack banner = ItemStack.EMPTY;
        ItemStack shield = ItemStack.EMPTY;
        boolean hasSpikedShield = false;

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(ModTags.Item.SPIKED_SHIELD)) {
                hasSpikedShield = true;
                shield = stack.copy();
            } else if (stack.getItem() instanceof BannerItem) {
                banner = stack;
            }
        }

        if (!hasSpikedShield) {
            return;
        }

        if (shield.isEmpty()) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }

        shield.set(DataComponents.BANNER_PATTERNS, banner.get(DataComponents.BANNER_PATTERNS));
        shield.set(DataComponents.BASE_COLOR, ((BannerItem) banner.getItem()).getColor());
        cir.setReturnValue(shield);
    }
}