package potatowolfie.earth_and_water.world.feature.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.levelgen.feature.configurations.DripstoneClusterConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class DarkDripstoneClusterFeatureConfig implements FeatureConfiguration {
    public static final Codec<DarkDripstoneClusterFeatureConfig> CODEC = RecordCodecBuilder.create((i) -> {
        return i.group(Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").forGetter((c) -> {
            return c.floorToCeilingSearchRange;
        }), IntProviders.codec(1, 128).fieldOf("height").forGetter((c) -> {
            return c.height;
        }), IntProviders.codec(1, 128).fieldOf("radius").forGetter((c) -> {
            return c.radius;
        }), Codec.intRange(0, 64).fieldOf("max_stalagmite_stalactite_height_diff").forGetter((c) -> {
            return c.maxStalagmiteStalactiteHeightDiff;
        }), Codec.intRange(1, 64).fieldOf("height_deviation").forGetter((c) -> {
            return c.heightDeviation;
        }), IntProviders.codec(0, 128).fieldOf("dripstone_block_layer_thickness").forGetter((c) -> {
            return c.dripstoneBlockLayerThickness;
        }), FloatProviders.codec(0.0F, 2.0F).fieldOf("density").forGetter((c) -> {
            return c.density;
        }), FloatProviders.codec(0.0F, 2.0F).fieldOf("wetness").forGetter((c) -> {
            return c.wetness;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_dripstone_column_at_max_distance_from_center").forGetter((c) -> {
            return c.chanceOfDripstoneColumnAtMaxDistanceFromCenter;
        }), Codec.intRange(1, 64).fieldOf("max_distance_from_edge_affecting_chance_of_dripstone_column").forGetter((c) -> {
            return c.maxDistanceFromEdgeAffectingChanceOfDripstoneColumn;
        }), Codec.intRange(1, 64).fieldOf("max_distance_from_center_affecting_height_bias").forGetter((c) -> {
            return c.maxDistanceFromCenterAffectingHeightBias;
        })).apply(i, DarkDripstoneClusterFeatureConfig::new);
    });

    public final int floorToCeilingSearchRange;
    public final IntProvider height;
    public final IntProvider radius;
    public final int maxStalagmiteStalactiteHeightDiff;
    public final int heightDeviation;
    public final IntProvider dripstoneBlockLayerThickness;
    public final FloatProvider density;
    public final FloatProvider wetness;
    public final float chanceOfDripstoneColumnAtMaxDistanceFromCenter;
    public final int maxDistanceFromEdgeAffectingChanceOfDripstoneColumn;
    public final int maxDistanceFromCenterAffectingHeightBias;

    public DarkDripstoneClusterFeatureConfig(
            int floorToCeilingSearchRange,
            IntProvider height,
            IntProvider radius,
            int maxStalagmiteStalactiteHeightDiff,
            int heightDeviation,
            IntProvider dripstoneBlockLayerThickness,
            FloatProvider density,
            FloatProvider wetness,
            float chanceOfDripstoneColumnAtMaxDistanceFromCenter,
            int maxDistanceFromEdgeAffectingChanceOfDripstoneColumn,
            int maxDistanceFromCenterAffectingHeightBias
    ) {
        this.floorToCeilingSearchRange = floorToCeilingSearchRange;
        this.height = height;
        this.radius = radius;
        this.maxStalagmiteStalactiteHeightDiff = maxStalagmiteStalactiteHeightDiff;
        this.heightDeviation = heightDeviation;
        this.dripstoneBlockLayerThickness = dripstoneBlockLayerThickness;
        this.density = density;
        this.wetness = wetness;
        this.chanceOfDripstoneColumnAtMaxDistanceFromCenter = chanceOfDripstoneColumnAtMaxDistanceFromCenter;
        this.maxDistanceFromEdgeAffectingChanceOfDripstoneColumn = maxDistanceFromEdgeAffectingChanceOfDripstoneColumn;
        this.maxDistanceFromCenterAffectingHeightBias = maxDistanceFromCenterAffectingHeightBias;
    }
}