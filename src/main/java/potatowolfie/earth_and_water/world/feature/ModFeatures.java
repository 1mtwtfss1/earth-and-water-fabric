package potatowolfie.earth_and_water.world.feature;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;
import potatowolfie.earth_and_water.EarthWater;
import potatowolfie.earth_and_water.world.feature.custom.OxygenFeature;
import potatowolfie.earth_and_water.world.feature.custom.OxygenFeatureConfig;
import potatowolfie.earth_and_water.world.feature.custom.limestone_rock.LimestoneRockFeature;
import potatowolfie.earth_and_water.world.feature.custom.limestone_rock.LimestoneRockFeatureConfig;

public class ModFeatures {

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