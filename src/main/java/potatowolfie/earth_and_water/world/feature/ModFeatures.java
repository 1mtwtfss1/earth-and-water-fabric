package potatowolfie.earth_and_water.world.feature;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;
import potatowolfie.earth_and_water.EarthWater;
import potatowolfie.earth_and_water.world.feature.custom.*;
import potatowolfie.earth_and_water.world.feature.custom.limestone_rock.LimestoneRockFeature;
import potatowolfie.earth_and_water.world.feature.custom.limestone_rock.LimestoneRockFeatureConfig;

public class ModFeatures {
    public static final Feature<DarkDripstoneClusterFeatureConfig> DARK_DRIPSTONE_CLUSTER =
            Registry.register(BuiltInRegistries.FEATURE,
                    Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "dark_dripstone_cluster"),
                    new DarkDripstoneClusterFeature(DarkDripstoneClusterFeatureConfig.CODEC));

    public static final Feature<LargeDarkDripstoneFeatureConfig> LARGE_DARK_DRIPSTONE =
            Registry.register(BuiltInRegistries.FEATURE,
                    Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "large_dark_dripstone"),
                    new LargeDarkDripstoneFeature(LargeDarkDripstoneFeatureConfig.CODEC));

    public static final Feature<SmallDarkDripstoneFeatureConfig> POINTED_DARK_DRIPSTONE =
            Registry.register(BuiltInRegistries.FEATURE,
                    Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "pointed_dark_dripstone"),
                    new SmallDarkDripstoneFeature(SmallDarkDripstoneFeatureConfig.CODEC));

    public static final Feature<OxygenFeatureConfig> OXYGEN_CROSS =
            Registry.register(BuiltInRegistries.FEATURE,
                    Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "oxygen_cross"),
                    new OxygenFeature(OxygenFeatureConfig.CODEC));

    public static final Feature<LimestoneRockFeatureConfig> LIMESTONE_ROCK =
            Registry.register(BuiltInRegistries.FEATURE,
                    Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "limestone_rock"),
                    new LimestoneRockFeature(LimestoneRockFeatureConfig.CODEC));

    public static void registerModFeatures() {
        EarthWater.LOGGER.info("Registering Mod Features for " + EarthWater.MOD_ID);
    }
}