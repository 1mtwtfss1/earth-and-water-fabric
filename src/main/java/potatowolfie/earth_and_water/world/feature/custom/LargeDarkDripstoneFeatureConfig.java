package potatowolfie.earth_and_water.world.feature.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;

public class LargeDarkDripstoneFeatureConfig implements FeatureConfiguration {
    public static final Codec<LargeDarkDripstoneFeatureConfig> CODEC = RecordCodecBuilder.create((i) -> {
        return i.group(Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").orElse(30).forGetter((c) -> {
            return c.floorToCeilingSearchRange;
        }), IntProviders.codec(1, 60).fieldOf("column_radius").forGetter((c) -> {
            return c.columnRadius;
        }), FloatProviders.codec(0.0F, 20.0F).fieldOf("height_scale").forGetter((c) -> {
            return c.heightScale;
        }), Codec.floatRange(0.1F, 1.0F).fieldOf("max_column_radius_to_cave_height_ratio").forGetter((c) -> {
            return c.maxColumnRadiusToCaveHeightRatio;
        }), FloatProviders.codec(0.1F, 10.0F).fieldOf("stalactite_bluntness").forGetter((c) -> {
            return c.stalactiteBluntness;
        }), FloatProviders.codec(0.1F, 10.0F).fieldOf("stalagmite_bluntness").forGetter((c) -> {
            return c.stalagmiteBluntness;
        }), FloatProviders.codec(0.0F, 2.0F).fieldOf("wind_speed").forGetter((c) -> {
            return c.windSpeed;
        }), Codec.intRange(0, 100).fieldOf("min_radius_for_wind").forGetter((c) -> {
            return c.minRadiusForWind;
        }), Codec.floatRange(0.0F, 5.0F).fieldOf("min_bluntness_for_wind").forGetter((c) -> {
            return c.minBluntnessForWind;
        })).apply(i, LargeDarkDripstoneFeatureConfig::new);
    });
    public final int floorToCeilingSearchRange;
    public final IntProvider columnRadius;
    public final FloatProvider heightScale;
    public final float maxColumnRadiusToCaveHeightRatio;
    public final FloatProvider stalactiteBluntness;
    public final FloatProvider stalagmiteBluntness;
    public final FloatProvider windSpeed;
    public final int minRadiusForWind;
    public final float minBluntnessForWind;

    public LargeDarkDripstoneFeatureConfig(int floorToCeilingSearchRange, IntProvider columnRadius, FloatProvider heightScale, float maxColumnRadiusToCaveHeightRatio, FloatProvider stalactiteBluntness, FloatProvider stalagmiteBluntness, FloatProvider windSpeed, int minRadiusForWind, float minBluntnessForWind) {
        this.floorToCeilingSearchRange = floorToCeilingSearchRange;
        this.columnRadius = columnRadius;
        this.heightScale = heightScale;
        this.maxColumnRadiusToCaveHeightRatio = maxColumnRadiusToCaveHeightRatio;
        this.stalactiteBluntness = stalactiteBluntness;
        this.stalagmiteBluntness = stalagmiteBluntness;
        this.windSpeed = windSpeed;
        this.minRadiusForWind = minRadiusForWind;
        this.minBluntnessForWind = minBluntnessForWind;
    }
}
