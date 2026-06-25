package potatowolfie.earth_and_water.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import potatowolfie.earth_and_water.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(ItemTags.TRIM_MATERIALS)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(ModItems.STEEL_INGOT).unwrapKey().orElseThrow());

        builder(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(ModItems.BATTLE_AXE).unwrapKey().orElseThrow())
                .add(BuiltInRegistries.ITEM.wrapAsHolder(ModItems.WHIP).unwrapKey().orElseThrow());

        builder(ItemTags.DURABILITY_ENCHANTABLE)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(ModItems.BATTLE_AXE).unwrapKey().orElseThrow())
                .add(BuiltInRegistries.ITEM.wrapAsHolder(ModItems.WHIP).unwrapKey().orElseThrow())
                .add(BuiltInRegistries.ITEM.wrapAsHolder(ModItems.SPIKED_SHIELD).unwrapKey().orElseThrow());

        builder(ItemTags.MINING_ENCHANTABLE)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(ModItems.BATTLE_AXE).unwrapKey().orElseThrow());

        builder(ItemTags.WEAPON_ENCHANTABLE)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(ModItems.BATTLE_AXE).unwrapKey().orElseThrow())
                .add(BuiltInRegistries.ITEM.wrapAsHolder(ModItems.WHIP).unwrapKey().orElseThrow());
    }
}
