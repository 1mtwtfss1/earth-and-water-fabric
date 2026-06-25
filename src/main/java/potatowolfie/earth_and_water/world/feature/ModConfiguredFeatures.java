package potatowolfie.earth_and_water.world.feature;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ClampedNormalFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.CompositeFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpeleothemClusterConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpeleothemConfiguration;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import potatowolfie.earth_and_water.EarthWater;
import potatowolfie.earth_and_water.block.ModBlocks;
import potatowolfie.earth_and_water.world.feature.custom.OxygenFeatureConfig;
import potatowolfie.earth_and_water.world.feature.custom.limestone_rock.LimestoneRockFeatureConfig;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_DRIPSTONE_CLUSTER = registerKey("dark_dripstone_cluster");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_DARK_DRIPSTONE = registerKey("large_dark_dripstone");
    public static final ResourceKey<ConfiguredFeature<?, ?>> POINTED_DARK_DRIPSTONE = registerKey("pointed_dark_dripstone");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OXYGEN_CROSS = registerKey("oxygen_cross");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LIMESTONE_ROCK = registerKey("limestone_rock");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);

        HolderSet.Named<Block> replaceableTag = blocks.getOrThrow(
                BlockTags.DRIPSTONE_REPLACEABLE
        );

        register(context, DARK_DRIPSTONE_CLUSTER, Feature.SPELEOTHEM_CLUSTER,
                new SpeleothemClusterConfiguration(
                        ModBlocks.DARK_DRIPSTONE_BLOCK.defaultBlockState(),
                        ModBlocks.POINTED_DARK_DRIPSTONE.defaultBlockState(),
                        replaceableTag,
                        30,
                        UniformInt.of(3, 19),
                        UniformInt.of(2, 8),
                        8,
                        16,
                        UniformInt.of(0, 2),
                        UniformFloat.of(0.3F, 0.7F),
                        ClampedNormalFloat.of(0.2F, 0.7F, 0.0F, 1.0F),
                        0.02F,
                        13,
                        16
                ));

        register(context, LARGE_DARK_DRIPSTONE, Feature.LARGE_DRIPSTONE,
                new LargeDripstoneConfiguration(
                        replaceableTag,
                        30,
                        UniformInt.of(3, 15),
                        UniformFloat.of(0.4F, 2.0F),
                        0.33F,
                        UniformFloat.of(0.3F, 0.9F),
                        UniformFloat.of(0.4F, 1.0F),
                        UniformFloat.of(0.0F, 0.3F),
                        4,
                        0.6F
                ));

        register(context, POINTED_DARK_DRIPSTONE, Feature.SIMPLE_RANDOM_SELECTOR,
                new CompositeFeatureConfiguration(HolderSet.direct(new Holder[]{
                        PlacementUtils.inlinePlaced(
                                Feature.SPELEOTHEM,
                                new SpeleothemConfiguration(
                                        ModBlocks.DARK_DRIPSTONE_BLOCK.defaultBlockState(),
                                        ModBlocks.POINTED_DARK_DRIPSTONE.defaultBlockState(),
                                        replaceableTag,
                                        0.2F,
                                        0.7F,
                                        0.5F,
                                        0.5F
                                ),
                                new PlacementModifier[]{
                                        EnvironmentScanPlacement.scanningFor(
                                                Direction.DOWN,
                                                BlockPredicate.solid(),
                                                BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE,
                                                12),
                                        RandomOffsetPlacement.vertical(ConstantInt.of(1))
                                }),
                        PlacementUtils.inlinePlaced(
                                Feature.SPELEOTHEM,
                                new SpeleothemConfiguration(
                                        ModBlocks.DARK_DRIPSTONE_BLOCK.defaultBlockState(),
                                        ModBlocks.POINTED_DARK_DRIPSTONE.defaultBlockState(),
                                        replaceableTag,
                                        0.2F,
                                        0.7F,
                                        0.5F,
                                        0.5F
                                ),
                                new PlacementModifier[]{
                                        EnvironmentScanPlacement.scanningFor(
                                                Direction.UP,
                                                BlockPredicate.solid(),
                                                BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE,
                                                12),
                                        RandomOffsetPlacement.vertical(ConstantInt.of(-1))
                                })
                })));

        register(context, OXYGEN_CROSS, ModFeatures.OXYGEN_CROSS,
                new OxygenFeatureConfig());

        register(context, LIMESTONE_ROCK, ModFeatures.LIMESTONE_ROCK,
                new LimestoneRockFeatureConfig());
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            F feature,
            FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}