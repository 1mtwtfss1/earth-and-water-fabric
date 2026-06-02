package potatowolfie.earth_and_water.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class ChiseledPrismarineBricksBlock extends Block {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public ChiseledPrismarineBricksBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.is(state.getBlock())) {
            boolean shouldBeActive = checkForWater(world, pos);
            if (shouldBeActive != state.getValue(ACTIVE)) {
                world.setBlock(pos, state.setValue(ACTIVE, shouldBeActive), Block.UPDATE_ALL);
            }

            world.scheduleTick(pos, this, 2);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean shouldBeActive = checkForWater(ctx.getLevel(), ctx.getClickedPos());
        return this.defaultBlockState().setValue(ACTIVE, shouldBeActive);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (world instanceof Level) {
            ((Level) world).scheduleTick(pos, this, 2);
        }

        boolean shouldBeActive = checkForWater(world, pos);
        return shouldBeActive != state.getValue(ACTIVE) ? state.setValue(ACTIVE, shouldBeActive) : state;
    }

    public void neighborUpdate(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        boolean shouldBeActive = checkForWater(world, pos);
        if (shouldBeActive != state.getValue(ACTIVE)) {
            world.setBlock(pos, state.setValue(ACTIVE, shouldBeActive), Block.UPDATE_ALL);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        boolean shouldBeActive = checkForWater(world, pos);
        if (shouldBeActive != state.getValue(ACTIVE)) {
            world.setBlock(pos, state.setValue(ACTIVE, shouldBeActive), Block.UPDATE_ALL);
        }

        world.scheduleTick(pos, this, 2);
    }

    private boolean checkForWater(LevelAccessor world, BlockPos pos) {
        FluidState blockFluidState = world.getFluidState(pos);
        if (blockFluidState.getType() == Fluids.WATER || blockFluidState.getType() == Fluids.FLOWING_WATER) {
            return true;
        }

        for (Direction direction : Direction.values()) {
            BlockPos adjacentPos = pos.relative(direction);

            BlockState adjacentState = world.getBlockState(adjacentPos);
            FluidState fluidState = world.getFluidState(adjacentPos);

            if (fluidState.getType() == Fluids.WATER ||
                    fluidState.getType() == Fluids.FLOWING_WATER ||
                    adjacentState.is(Blocks.WATER)) {
                return true;
            }
        }

        BlockPos belowPos = pos.below();
        FluidState belowFluidState = world.getFluidState(belowPos);
        if (belowFluidState.getType() == Fluids.WATER || belowFluidState.getType() == Fluids.FLOWING_WATER) {
            return true;
        }

        return false;
    }
}