package potatowolfie.earth_and_water.block.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import potatowolfie.earth_and_water.block.entity.custom.ReinforcedSpawnerBlockEntity;

@Environment(EnvType.CLIENT)
public class ReinforcedSpawnerBlockEntityRenderer implements BlockEntityRenderer<ReinforcedSpawnerBlockEntity, ReinforcedSpawnerBlockEntityRenderer.SpawnerRenderState> {
    private final EntityRenderDispatcher entityRenderDispatcher;

    public ReinforcedSpawnerBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.entityRenderDispatcher = ctx.entityRenderer();
    }

    @Override
    public SpawnerRenderState createRenderState() {
        return new SpawnerRenderState();
    }

    @Override
    public void updateRenderState(ReinforcedSpawnerBlockEntity blockEntity, SpawnerRenderState state, float tickProgress, Vec3 cameraPos, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay);

        Level world = blockEntity.getLevel();
        if (world != null) {
            Entity entity = blockEntity.getDisplayEntity(world);

            if (entity != null) {
                state.mobSpawnerRenderState.displayEntity = entityRenderDispatcher.extractEntity(entity, tickProgress);
                state.mobSpawnerRenderState.displayEntity.lightCoords = state.lightCoords;

                double lastRotation = blockEntity.getLastRotation();
                double rotation = blockEntity.getRotation();
                state.mobSpawnerRenderState.spin = (float)net.minecraft.util.Mth.lerp(tickProgress, lastRotation, rotation) * 10.0F;

                state.mobSpawnerRenderState.scale = 0.53125F;
                float maxDimension = Math.max(entity.getBbWidth(), entity.getBbHeight());
                if (maxDimension > 1.0) {
                    state.mobSpawnerRenderState.scale /= maxDimension;
                }
            } else {
                state.mobSpawnerRenderState.displayEntity = null;
            }
        }
    }

    @Override
    public void render(SpawnerRenderState state, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        if (state.mobSpawnerRenderState.displayEntity != null) {
            SpawnerRenderer.submitEntityInSpawner(
                    matrices,
                    queue,
                    state.mobSpawnerRenderState.displayEntity,
                    this.entityRenderDispatcher,
                    state.mobSpawnerRenderState.spin,
                    state.mobSpawnerRenderState.scale,
                    cameraState
            );
        }
    }

    public static class SpawnerRenderState extends BlockEntityRenderState {
        public final net.minecraft.client.renderer.blockentity.state.SpawnerRenderState mobSpawnerRenderState = new net.minecraft.client.renderer.blockentity.state.SpawnerRenderState();
    }
}