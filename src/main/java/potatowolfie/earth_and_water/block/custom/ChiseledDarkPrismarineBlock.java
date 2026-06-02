package potatowolfie.earth_and_water.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ChiseledDarkPrismarineBlock extends Block {
    public static final IntegerProperty Y_LEVEL_STATE = IntegerProperty.create("y_level", 0, 3);

    public ChiseledDarkPrismarineBlock(Properties settings) {
        super(settings.lightLevel(state -> getLuminanceForState(state)));
        this.registerDefaultState(this.getStateDefinition().any().setValue(Y_LEVEL_STATE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(Y_LEVEL_STATE);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.is(state.getBlock())) {
            int newState = getStateForYLevel(pos.getY());
            if (newState != state.getValue(Y_LEVEL_STATE)) {
                world.setBlock(pos, state.setValue(Y_LEVEL_STATE, newState), Block.UPDATE_ALL);
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        int newState = getStateForYLevel(ctx.getClickedPos().getY());
        return this.defaultBlockState().setValue(Y_LEVEL_STATE, newState);
    }

    @Override
    public void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        super.affectNeighborsAfterRemoval(state, world, pos, moved);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        int newState = getStateForYLevel(pos.getY());
        if (newState != state.getValue(Y_LEVEL_STATE)) {
            world.setBlock(pos, state.setValue(Y_LEVEL_STATE, newState), Block.UPDATE_ALL);
        }
    }

    private int getStateForYLevel(int yLevel) {
        if (yLevel >= 32 && yLevel <= 320000000) {
            return 3;
        } else if (yLevel >= 0 && yLevel <= 31) {
            return 2;
        } else if (yLevel >= -32 && yLevel <= -1) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int getLuminanceForState(BlockState state) {
        if (state.hasProperty(Y_LEVEL_STATE)) {
            int yLevelState = state.getValue(Y_LEVEL_STATE);
            switch (yLevelState) {
                case 1: return 4;
                case 2: return 8;
                case 3: return 12;
                default: return 0;
            }
        }
        return 0;
    }
}