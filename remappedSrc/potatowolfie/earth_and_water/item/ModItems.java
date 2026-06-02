package potatowolfie.earth_and_water.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.item.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import potatowolfie.earth_and_water.EarthWater;
import potatowolfie.earth_and_water.entity.ModEntities;
import potatowolfie.earth_and_water.item.custom.*;
import potatowolfie.earth_and_water.trim.ModTrimMaterials;

import java.util.List;
import java.util.Optional;

public class ModItems {
    public static final Item BORE_ROD = registerItem("bore_rod",
            new Item(new Item.Properties()
                    .setId(createItemRegistryKey("bore_rod"))));
    public static final Item BORE_SPAWN_EGG = registerItem("bore_spawn_egg",
            new SpawnEggItem(
                    new Item.Properties().spawnEgg(ModEntities.BORE)
                            .setId(createItemRegistryKey("bore_spawn_egg"))));

    public static final Item BRINE_ROD = registerItem("brine_rod",
            new Item(new Item.Properties()
                    .setId(createItemRegistryKey("brine_rod"))));
    public static final Item BRINE_SPAWN_EGG = registerItem("brine_spawn_egg",
            new SpawnEggItem(
                    new Item.Properties().spawnEgg(ModEntities.BRINE)
                            .setId(createItemRegistryKey("brine_spawn_egg"))));

    public static final Item STEEL_INGOT = registerItem("steel_ingot",
            new Item(new Item.Properties()
                    .setId(createItemRegistryKey("steel_ingot"))
                    .trimMaterial(ModTrimMaterials.STEEL)));

    public static final Item STEEL_NUGGET = registerItem("steel_nugget",
            new Item(new Item.Properties()
                    .setId(createItemRegistryKey("steel_nugget"))));
    public static final Item EARTH_CHARGE = registerItem("earth_charge",
            new EarthChargeItem(new Item.Properties()
                    .setId(createItemRegistryKey("earth_charge"))));
    public static final Item WATER_CHARGE = registerItem("water_charge",
            new WaterChargeItem(new Item.Properties()
                    .setId(createItemRegistryKey("water_charge"))));
    public static final Item REINFORCED_KEY = registerItem("reinforced_key",
            new ReinforcedKeyItem(new Item.Properties()
                    .setId(createItemRegistryKey("reinforced_key"))));

    public static final Item STEEL_UPGRADE_SMITHING_TEMPLATE = registerItem("steel_upgrade_smithing_template",
            SmithingTemplateItem.createArmorTrimTemplate(new Item.Properties().rarity(Rarity.UNCOMMON)
                    .setId(createItemRegistryKey("steel_upgrade_smithing_template"))));

    public static final Item BLOCK_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("block_armor_trim_smithing_template",
            SmithingTemplateItem.createArmorTrimTemplate(new Item.Properties().rarity(Rarity.UNCOMMON)
                    .setId(createItemRegistryKey("block_armor_trim_smithing_template"))));

    public static final Item GUARD_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("guard_armor_trim_smithing_template",
            SmithingTemplateItem.createArmorTrimTemplate(new Item.Properties().rarity(Rarity.UNCOMMON)
                    .setId(createItemRegistryKey("guard_armor_trim_smithing_template"))));

    public static final Item WHIP = registerItem("whip",
            new WhipItem(ModToolMaterials.PRISMARINE,
                    new Item.Properties().sword(ModToolMaterials.PRISMARINE, 4, -2.8F)
                            .rarity(Rarity.UNCOMMON)
                            .setId(createItemRegistryKey("whip"))
            ));
    public static final Item BATTLE_AXE = registerItem("battle_axe",
            new BattleAxeItem(ModToolMaterials.STEEL, 5.0F, -3.2F,
                    new Item.Properties()
                            .rarity(Rarity.UNCOMMON)
                            .setId(createItemRegistryKey("battle_axe"))
    ));

    public static final Item SPIKED_SHIELD = Registry.register(BuiltInRegistries.ITEM,
            createItemRegistryKey("spiked_shield"),
            new SpikedShieldItem(new Item.Properties()
                    .durability(556)
                    .component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
                    .repairable(ItemTags.WOODEN_TOOL_MATERIALS)
                    .equippableUnswappable(EquipmentSlot.OFFHAND)
                    .component(DataComponents.BLOCKS_ATTACKS, new BlocksAttacks(0.25F, 1.0F,
                            List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                            new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F), Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                            Optional.of(SoundEvents.SHIELD_BLOCK), Optional.of(SoundEvents.SHIELD_BREAK)))
                    .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
                    .setId(createItemRegistryKey("spiked_shield"))
            ));

    private static ResourceKey<Item> createItemRegistryKey(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, name));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, createItemRegistryKey(name), item);
    }

    private static void customIngredients(FabricItemGroupEntries entries) {
        entries.addBefore(Items.BLAZE_ROD, BORE_ROD);
        entries.addAfter(Items.BLAZE_ROD, BRINE_ROD);
        entries.addAfter(Items.IRON_INGOT, STEEL_INGOT);
        entries.addAfter(Items.IRON_NUGGET, STEEL_NUGGET);
        entries.addAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, STEEL_UPGRADE_SMITHING_TEMPLATE);
        entries.addAfter(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, BLOCK_ARMOR_TRIM_SMITHING_TEMPLATE);
        entries.addAfter(BLOCK_ARMOR_TRIM_SMITHING_TEMPLATE, GUARD_ARMOR_TRIM_SMITHING_TEMPLATE);
        entries.addAfter(Items.OMINOUS_TRIAL_KEY, REINFORCED_KEY);
    }

    private static void customCombat(FabricItemGroupEntries entries) {
        entries.addAfter(Items.TRIDENT, WHIP);
        entries.addAfter(WHIP, BATTLE_AXE);
        entries.addAfter(Items.SHIELD, SPIKED_SHIELD);
        entries.addAfter(Items.WIND_CHARGE, WATER_CHARGE);
        entries.addAfter(WATER_CHARGE, EARTH_CHARGE);
    }

    private static void customSpawnEggs(FabricItemGroupEntries entries) {
        entries.addAfter(Items.BREEZE_SPAWN_EGG, BORE_SPAWN_EGG);
        entries.addAfter(BORE_SPAWN_EGG, BRINE_SPAWN_EGG);
    }

    public static void registerModItems() {
        EarthWater.LOGGER.info("Registering Mod Items for " + EarthWater.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(ModItems::customIngredients);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(ModItems::customCombat);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(ModItems::customSpawnEggs);
    }
}