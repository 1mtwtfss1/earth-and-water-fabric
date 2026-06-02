package potatowolfie.earth_and_water.world.feature.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class OxygenFeatureConfig implements FeatureConfiguration {
    public static final Codec<OxygenFeatureConfig> CODEC = MapCodec.unit(new OxygenFeatureConfig()).codec();

    public OxygenFeatureConfig() {
    }
}