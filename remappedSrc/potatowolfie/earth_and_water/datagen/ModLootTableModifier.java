package potatowolfie.earth_and_water.datagen;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import potatowolfie.earth_and_water.item.ModItems;

public class ModLootTableModifier {

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, wrapperLookup) -> {
            if (key.identifier().equals(Identifier.fromNamespaceAndPath("minecraft", "chests/pillager_outpost"))) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(UniformGenerator.between(0.0f, 1.0f))
                        .add(LootItem.lootTableItem(ModItems.STEEL_NUGGET)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 8.0f))));
                tableBuilder.withPool(poolBuilder);
            }
        });

        LootTableEvents.MODIFY.register((key, tableBuilder, source, wrapperLookup) -> {
            if (key.identifier().equals(Identifier.parse("minecraft/datapacks/trade_rebalance/data/minecraft/loot_table/chests/pillager_outpost"))) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .setRolls(UniformGenerator.between(0.0f, 1.0f))
                        .add(LootItem.lootTableItem(ModItems.STEEL_NUGGET)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))));
                tableBuilder.withPool(poolBuilder);
            }
        });
    }
}