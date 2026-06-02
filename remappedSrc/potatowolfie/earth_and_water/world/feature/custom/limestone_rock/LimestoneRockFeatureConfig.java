package potatowolfie.earth_and_water.world.feature.custom.limestone_rock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class LimestoneRockFeatureConfig implements FeatureConfiguration {
    public static final Codec<LimestoneRockFeatureConfig> CODEC = MapCodec.unit(new LimestoneRockFeatureConfig()).codec();

    public LimestoneRockFeatureConfig() {
    }
}