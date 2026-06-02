package potatowolfie.earth_and_water.world.feature;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ClampedNormalInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import potatowolfie.earth_and_water.EarthWater;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> DARK_DRIPSTONE_CLUSTER_PLACED = registerKey("dark_dripstone_cluster");
    public static final ResourceKey<PlacedFeature> LARGE_DARK_DRIPSTONE_PLACED = registerKey("large_dark_dripstone");
    public static final ResourceKey<PlacedFeature> POINTED_DARK_DRIPSTONE_PLACED = registerKey("pointed_dark_dripstone");
    public static final ResourceKey<PlacedFeature> OXYGEN_CROSS_PLACED = registerKey("oxygen_cross");
    public static final ResourceKey<PlacedFeature> LIMESTONE_ROCK_PLACED = registerKey("limestone_rock");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureRegistryEntryLookup =
                context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, DARK_DRIPSTONE_CLUSTER_PLACED,
                configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.DARK_DRIPSTONE_CLUSTER),
                List.of(
                        CountPlacement.of(net.minecraft.util.valueproviders.UniformInt.of(14, 29)),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(256)),
                        BiomeFilter.biome()
                ));

        register(context, LARGE_DARK_DRIPSTONE_PLACED,
                configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.LARGE_DARK_DRIPSTONE),
                List.of(
                        CountPlacement.of(net.minecraft.util.valueproviders.UniformInt.of(3, 14)),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(256)),
                        BiomeFilter.biome()
                ));

        register(context, POINTED_DARK_DRIPSTONE_PLACED,
                configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.POINTED_DARK_DRIPSTONE),
                List.of(
                        new PlacementModifier[]{
                                CountPlacement.of(UniformInt.of(192, 256)),
                                InSquarePlacement.spread(),
                                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                                CountPlacement.of(UniformInt.of(1, 5)),
                                RandomOffsetPlacement.of
                                        (ClampedNormalInt.of(0.0F, 3.0F, -10, 10),
                                                ClampedNormalInt.of(0.0F, 0.6F, -2, 2)),
                                BiomeFilter.biome()}));

        register(context, OXYGEN_CROSS_PLACED,
                configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.OXYGEN_CROSS),
                List.of(
                        CountPlacement.of(1),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(80),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BiomeFilter.biome()
                )
        );

        register(context, LIMESTONE_ROCK_PLACED,
                configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.LIMESTONE_ROCK),
                List.of(
                        CountPlacement.of(1),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(30),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BiomeFilter.biome()
                )
        );
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                                                                   Holder<ConfiguredFeature<?, ?>> configuration,
                                                                                   PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }
}