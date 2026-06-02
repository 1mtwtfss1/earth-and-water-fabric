package potatowolfie.earth_and_water.world.feature.custom;

import com.mojang.serialization.Codec;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SmallDarkDripstoneFeature extends Feature<SmallDarkDripstoneFeatureConfig> {
    public SmallDarkDripstoneFeature(Codec<SmallDarkDripstoneFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<SmallDarkDripstoneFeatureConfig> context) {
        LevelAccessor worldAccess = context.level();
        BlockPos blockPos = context.origin();
        RandomSource random = context.random();
        SmallDarkDripstoneFeatureConfig smallDarkDripstoneFeatureConfig = (SmallDarkDripstoneFeatureConfig)context.config();
        Optional<Direction> optional = getDirection(worldAccess, blockPos, random);
        if (optional.isEmpty()) {
            return false;
        } else {
            BlockPos blockPos2 = blockPos.relative(((Direction)optional.get()).getOpposite());
            generateDripstoneBlocks(worldAccess, random, blockPos2, smallDarkDripstoneFeatureConfig);
            int i = random.nextFloat() < smallDarkDripstoneFeatureConfig.chanceOfTallerDripstone && DarkDripstoneHelper.canGenerate(worldAccess.getBlockState(blockPos.relative((Direction)optional.get()))) ? 2 : 1;
            DarkDripstoneHelper.generatePointedDarkDripstone(worldAccess, blockPos, (Direction)optional.get(), i, false);
            return true;
        }
    }

    private static Optional<Direction> getDirection(LevelAccessor world, BlockPos pos, RandomSource random) {
        boolean bl = DarkDripstoneHelper.canReplace(world.getBlockState(pos.above()));
        boolean bl2 = DarkDripstoneHelper.canReplace(world.getBlockState(pos.below()));

        boolean hasFloorAbove = bl && DarkDripstoneHelper.canGenerate(world.getBlockState(pos.above(2)));
        boolean hasCeilingBelow = bl2 && DarkDripstoneHelper.canGenerate(world.getBlockState(pos.below(2)));

        if (hasFloorAbove && hasCeilingBelow) {
            return Optional.of(random.nextBoolean() ? Direction.DOWN : Direction.UP);
        } else if (hasFloorAbove) {
            return Optional.of(Direction.DOWN);
        } else {
            return hasCeilingBelow ? Optional.of(Direction.UP) : Optional.empty();
        }
    }

    private static void generateDripstoneBlocks(LevelAccessor world, RandomSource random, BlockPos pos, SmallDarkDripstoneFeatureConfig config) {
        DarkDripstoneHelper.generateDarkDripstoneBlock(world, pos);
        Iterator var4 = Direction.Plane.HORIZONTAL.iterator();

        while(var4.hasNext()) {
            Direction direction = (Direction)var4.next();
            if (!(random.nextFloat() > config.chanceOfDirectionalSpread)) {
                BlockPos blockPos = pos.relative(direction);
                DarkDripstoneHelper.generateDarkDripstoneBlock(world, blockPos);
                if (!(random.nextFloat() > config.chanceOfSpreadRadius2)) {
                    BlockPos blockPos2 = blockPos.relative(Direction.getRandom(random));
                    DarkDripstoneHelper.generateDarkDripstoneBlock(world, blockPos2);
                    if (!(random.nextFloat() > config.chanceOfSpreadRadius3)) {
                        BlockPos blockPos3 = blockPos2.relative(Direction.getRandom(random));
                        DarkDripstoneHelper.generateDarkDripstoneBlock(world, blockPos3);
                    }
                }
            }
        }

    }
}
