package potatowolfie.earth_and_water.block.custom;

import potatowolfie.earth_and_water.EarthWater;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.AABB;

public class ChiseledDripstoneBricksBlock extends Block {
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    public ChiseledDripstoneBricksBlock(Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClientSide() && !state.is(oldState.getBlock())) {
            world.scheduleTick(pos, this, 2);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        if (!ctx.getLevel().isClientSide()) {
            ctx.getLevel().scheduleTick(ctx.getClickedPos(), this, 2);
        }
        return this.defaultBlockState().setValue(POWERED, false);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, net.minecraft.util.RandomSource random) {
        if (!world.isClientSide()) {
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();
            AABB box = new AABB(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5);

            List<Entity> entities = world.getEntities(null, box,
                    entity -> !(entity.isSpectator()) && !entity.isInvisible());

            boolean entityNearby = !entities.isEmpty();
            boolean wasPowered = state.getValue(POWERED);

            if (entityNearby != wasPowered) {
                world.setBlockAndUpdate(pos, state.setValue(POWERED, entityNearby));
                world.updateNeighborsAt(pos, this, Orientation.fromIndex(0));

                if (entityNearby && !wasPowered) {
                    spawnParticleBurst(world, pos);
                }
            }

            world.scheduleTick(pos, this, 2);
        }
    }

    private void spawnParticleBurst(ServerLevel world, BlockPos pos) {
        RandomSource random = world.getRandom();
        for (int i = 0; i < 20; i++) {
            double offsetX = random.nextDouble() * 0.6 - 0.3;
            double offsetY = random.nextDouble() * 0.6 - 0.3;
            double offsetZ = random.nextDouble() * 0.6 - 0.3;

            double velocityX = offsetX * 0.3;
            double velocityY = random.nextDouble() * 0.2 + 0.1;
            double velocityZ = offsetZ * 0.3;
            world.sendParticles(
                    EarthWater.LIGHT_UP,
                    pos.getX() + 0.5 + offsetX,
                    pos.getY() + 0.5 + offsetY,
                    pos.getZ() + 0.5 + offsetZ,
                    1,
                    velocityX,
                    velocityY,
                    velocityZ,
                    0.05
            );
        }
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return 0;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }
}