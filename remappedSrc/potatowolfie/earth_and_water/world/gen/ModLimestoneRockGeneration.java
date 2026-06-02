package potatowolfie.earth_and_water.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import potatowolfie.earth_and_water.world.feature.ModPlacedFeatures;

public class ModLimestoneRockGeneration {
    public static void addFeaturesToBiomes() {
        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(
                        Biomes.LUKEWARM_OCEAN,
                        Biomes.DEEP_LUKEWARM_OCEAN,
                        Biomes.WARM_OCEAN
                ),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.LIMESTONE_ROCK_PLACED
        );
    }
}