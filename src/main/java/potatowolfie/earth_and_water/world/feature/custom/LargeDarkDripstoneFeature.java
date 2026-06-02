package potatowolfie.earth_and_water.world.feature.custom;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;
import potatowolfie.earth_and_water.block.ModBlocks;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.phys.Vec3;

public class LargeDarkDripstoneFeature extends Feature<LargeDarkDripstoneFeatureConfig> {
    public LargeDarkDripstoneFeature(Codec<LargeDarkDripstoneFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<LargeDarkDripstoneFeatureConfig> context) {
        WorldGenLevel structureWorldAccess = context.level();
        BlockPos blockPos = context.origin();
        LargeDarkDripstoneFeatureConfig largeDarkDripstoneFeatureConfig = (LargeDarkDripstoneFeatureConfig)context.config();
        RandomSource random = context.random();
        if (!DarkDripstoneHelper.canGenerate(structureWorldAccess, blockPos)) {
            return false;
        } else {
            Optional<Column> optional = Column.scan(structureWorldAccess, blockPos, largeDarkDripstoneFeatureConfig.floorToCeilingSearchRange, DarkDripstoneHelper::canGenerate, DarkDripstoneHelper::canReplaceOrLava);
            if (!optional.isEmpty() && optional.get() instanceof Column.Range) {
                Column.Range bounded = (Column.Range)optional.get();
                if (bounded.height() < 4) {
                    return false;
                } else {
                    int i = (int)((float)bounded.height() * largeDarkDripstoneFeatureConfig.maxColumnRadiusToCaveHeightRatio);
                    int j = Mth.clamp(i, largeDarkDripstoneFeatureConfig.columnRadius.minInclusive(), largeDarkDripstoneFeatureConfig.columnRadius.maxInclusive());
                    int k = Mth.randomBetweenInclusive(random, largeDarkDripstoneFeatureConfig.columnRadius.minInclusive(), j);
                    DarkDripstoneGenerator DarkdripstoneGenerator = createGenerator(blockPos.atY(bounded.ceiling() - 1), false, random, k, largeDarkDripstoneFeatureConfig.stalactiteBluntness, largeDarkDripstoneFeatureConfig.heightScale);
                    DarkDripstoneGenerator DarkdripstoneGenerator2 = createGenerator(blockPos.atY(bounded.floor() + 1), true, random, k, largeDarkDripstoneFeatureConfig.stalagmiteBluntness, largeDarkDripstoneFeatureConfig.heightScale);
                    WindModifier windModifier;
                    if (DarkdripstoneGenerator.generateWind(largeDarkDripstoneFeatureConfig) && DarkdripstoneGenerator2.generateWind(largeDarkDripstoneFeatureConfig)) {
                        windModifier = new WindModifier(blockPos.getY(), random, largeDarkDripstoneFeatureConfig.windSpeed);
                    } else {
                        windModifier = WindModifier.create();
                    }

                    boolean bl = DarkdripstoneGenerator.canGenerate(structureWorldAccess, windModifier);
                    boolean bl2 = DarkdripstoneGenerator2.canGenerate(structureWorldAccess, windModifier);
                    if (bl) {
                        DarkdripstoneGenerator.generate(structureWorldAccess, random, windModifier);
                    }

                    if (bl2) {
                        DarkdripstoneGenerator2.generate(structureWorldAccess, random, windModifier);
                    }

                    return true;
                }
            } else {
                return false;
            }
        }
    }

    private static DarkDripstoneGenerator createGenerator(BlockPos pos, boolean isStalagmite, RandomSource random, int scale, FloatProvider bluntness, FloatProvider heightScale) {
        return new DarkDripstoneGenerator(pos, isStalagmite, scale, (double)bluntness.sample(random), (double)heightScale.sample(random));
    }

    private void testGeneration(WorldGenLevel world, BlockPos pos, Column.Range surface, WindModifier wind) {
        world.setBlock(wind.modify(pos.atY(surface.ceiling() - 1)), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
        world.setBlock(wind.modify(pos.atY(surface.floor() + 1)), Blocks.GOLD_BLOCK.defaultBlockState(), 2);

        for(BlockPos.MutableBlockPos mutable = pos.atY(surface.floor() + 2).mutable(); mutable.getY() < surface.ceiling() - 1; mutable.move(Direction.UP)) {
            BlockPos blockPos = wind.modify(mutable);
            if (DarkDripstoneHelper.canGenerate(world, blockPos) || world.getBlockState(blockPos).is(Blocks.DRIPSTONE_BLOCK)) {
                world.setBlock(blockPos, Blocks.CREEPER_HEAD.defaultBlockState(), 2);
            }
        }

    }

    static final class DarkDripstoneGenerator {
        private BlockPos pos;
        private final boolean isStalagmite;
        private int scale;
        private final double bluntness;
        private final double heightScale;

        DarkDripstoneGenerator(BlockPos pos, boolean isStalagmite, int scale, double bluntness, double heightScale) {
            this.pos = pos;
            this.isStalagmite = isStalagmite;
            this.scale = scale;
            this.bluntness = bluntness;
            this.heightScale = heightScale;
        }

        private int getBaseScale() {
            return this.scale(0.0F);
        }

        private int getBottomY() {
            return this.isStalagmite ? this.pos.getY() : this.pos.getY() - this.getBaseScale();
        }

        private int getTopY() {
            return !this.isStalagmite ? this.pos.getY() : this.pos.getY() + this.getBaseScale();
        }

        boolean canGenerate(WorldGenLevel world, WindModifier wind) {
            while(this.scale > 1) {
                BlockPos.MutableBlockPos mutable = this.pos.mutable();
                int i = Math.min(10, this.getBaseScale());

                for(int j = 0; j < i; ++j) {
                    if (world.getBlockState(mutable).is(Blocks.LAVA)) {
                        return false;
                    }

                    if (DarkDripstoneHelper.canGenerateBase(world, wind.modify(mutable), this.scale)) {
                        this.pos = mutable;
                        return true;
                    }

                    mutable.move(this.isStalagmite ? Direction.DOWN : Direction.UP);
                }

                this.scale /= 2;
            }

            return false;
        }

        private int scale(float height) {
            return (int)DarkDripstoneHelper.scaleHeightFromRadius((double)height, (double)this.scale, this.heightScale, this.bluntness);
        }

        void generate(WorldGenLevel world, RandomSource random, WindModifier wind) {
            for(int i = -this.scale; i <= this.scale; ++i) {
                for(int j = -this.scale; j <= this.scale; ++j) {
                    float f = Mth.sqrt((float)(i * i + j * j));
                    if (!(f > (float)this.scale)) {
                        int k = this.scale(f);
                        if (k > 0) {
                            if ((double)random.nextFloat() < 0.2) {
                                k = (int)((float)k * Mth.randomBetween(random, 0.8F, 1.0F));
                            }

                            BlockPos.MutableBlockPos mutable = this.pos.offset(i, 0, j).mutable();
                            boolean bl = false;
                            int l = this.isStalagmite ? world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, mutable.getX(), mutable.getZ()) : Integer.MAX_VALUE;

                            for(int m = 0; m < k && mutable.getY() < l; ++m) {
                                BlockPos blockPos = wind.modify(mutable);
                                if (DarkDripstoneHelper.canGenerateOrLava(world, blockPos)) {
                                    bl = true;
                                    Block block = ModBlocks.DARK_DRIPSTONE_BLOCK;
                                    world.setBlock(blockPos, block.defaultBlockState(), 2);
                                } else if (bl && world.getBlockState(blockPos).is(BlockTags.BASE_STONE_OVERWORLD)) {
                                    break;
                                }

                                mutable.move(this.isStalagmite ? Direction.UP : Direction.DOWN);
                            }
                        }
                    }
                }
            }

        }

        boolean generateWind(LargeDarkDripstoneFeatureConfig config) {
            return this.scale >= config.minRadiusForWind && this.bluntness >= (double)config.minBluntnessForWind;
        }
    }

    private static final class WindModifier {
        private final int y;
        @Nullable
        private final Vec3 wind;

        WindModifier(int y, RandomSource random, FloatProvider wind) {
            this.y = y;
            float f = wind.sample(random);
            float g = Mth.randomBetween(random, 0.0F, 3.1415927F);
            this.wind = new Vec3((double)(Mth.cos(g) * f), 0.0, (double)(Mth.sin(g) * f));
        }

        private WindModifier() {
            this.y = 0;
            this.wind = null;
        }

        static WindModifier create() {
            return new WindModifier();
        }

        BlockPos modify(BlockPos pos) {
            if (this.wind == null) {
                return pos;
            } else {
                int i = this.y - pos.getY();
                Vec3 vec3d = this.wind.scale((double)i);
                return pos.offset(Mth.floor(vec3d.x), 0, Mth.floor(vec3d.z));
            }
        }
    }
}
