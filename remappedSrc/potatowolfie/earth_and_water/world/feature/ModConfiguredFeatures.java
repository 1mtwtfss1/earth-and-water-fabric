package potatowolfie.earth_and_water.world.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.TrapezoidFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import potatowolfie.earth_and_water.EarthWater;
import potatowolfie.earth_and_water.world.feature.custom.DarkDripstoneClusterFeatureConfig;
import potatowolfie.earth_and_water.world.feature.custom.LargeDarkDripstoneFeatureConfig;
import potatowolfie.earth_and_water.world.feature.custom.OxygenFeatureConfig;
import potatowolfie.earth_and_water.world.feature.custom.SmallDarkDripstoneFeatureConfig;
import potatowolfie.earth_and_water.world.feature.custom.limestone_rock.LimestoneRockFeatureConfig;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_DRIPSTONE_CLUSTER = registerKey("dark_dripstone_cluster");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_DARK_DRIPSTONE = registerKey("large_dark_dripstone");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_DARK_DRIPSTONE = registerKey("small_dark_dripstone");
    public static final ResourceKey<ConfiguredFeature<?, ?>> POINTED_DARK_DRIPSTONE = registerKey("pointed_dark_dripstone");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OXYGEN_CROSS = registerKey("oxygen_cross");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LIMESTONE_ROCK = registerKey("limestone_rock");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, DARK_DRIPSTONE_CLUSTER, ModFeatures.DARK_DRIPSTONE_CLUSTER,
                new DarkDripstoneClusterFeatureConfig(
                        30,
                        UniformInt.of(3, 19),
                        UniformInt.of(2, 8),
                        8,
                        16,
                        UniformInt.of(0, 2),
                        UniformFloat.of(0.3F, 0.7F),
                        TrapezoidFloat.of(0.2F, 0.7F, 0.5F),
                        0.02F,
                        13,
                        16
                ));

        register(context, LARGE_DARK_DRIPSTONE, ModFeatures.LARGE_DARK_DRIPSTONE,
                new LargeDarkDripstoneFeatureConfig(
                        30,
                        UniformInt.of(3, 19),
                        UniformFloat.of(0.4F, 2.0F),
                        0.33F,
                        UniformFloat.of(0.3F, 0.9F),
                        UniformFloat.of(0.4F, 1.0F),
                        UniformFloat.of(0.0F, 0.3F),
                        4,
                        0.6F));

        register(context, POINTED_DARK_DRIPSTONE, ModFeatures.POINTED_DARK_DRIPSTONE,
                new SmallDarkDripstoneFeatureConfig(0.2F,
                        0.7F,
                        0.5F,
                        0.5F));

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