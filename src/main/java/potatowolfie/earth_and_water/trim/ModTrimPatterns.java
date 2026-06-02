package potatowolfie.earth_and_water.trim;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import potatowolfie.earth_and_water.EarthWater;
import potatowolfie.earth_and_water.item.ModItems;

public class ModTrimPatterns {
    public static final ResourceKey<TrimPattern> BLOCK = ResourceKey.create(Registries.TRIM_PATTERN, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "block"));
    public static final ResourceKey<TrimPattern> GUARD = ResourceKey.create(Registries.TRIM_PATTERN, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "guard"));

    public static void bootstrap(BootstrapContext<TrimPattern> context) {
        register(context, ModItems.BLOCK_ARMOR_TRIM_SMITHING_TEMPLATE, BLOCK);
        register(context, ModItems.GUARD_ARMOR_TRIM_SMITHING_TEMPLATE, GUARD);
    }

    private static void register(BootstrapContext<TrimPattern> context, Item item, ResourceKey<TrimPattern> key) {
        TrimPattern trimPattern = new TrimPattern(key.identifier(),
                Component.translatable(Util.makeDescriptionId("trim_pattern", key.identifier())), false);

        context.register(key, trimPattern);
    }
}