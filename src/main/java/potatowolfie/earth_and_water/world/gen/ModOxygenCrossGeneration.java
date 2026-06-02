package potatowolfie.earth_and_water.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import potatowolfie.earth_and_water.world.feature.ModPlacedFeatures;

public class ModOxygenCrossGeneration {
    public static void addFeatureToBiomes() {
        BiomeModifications.addFeature(
                BiomeSelectors.tag(BiomeTags.IS_OCEAN),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                ModPlacedFeatures.OXYGEN_CROSS_PLACED
        );
    }
}