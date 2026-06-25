package potatowolfie.earth_and_water.block.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
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
    public void submit(SpawnerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.displayEntity != null) {
            SpawnerRenderer.submitEntityInSpawner(
                    poseStack,
                    submitNodeCollector,
                    state.displayEntity,
                    this.entityRenderDispatcher,
                    state.spin,
                    state.scale,
                    camera
            );
        }
    }

    @Override
    public void extractRenderState(ReinforcedSpawnerBlockEntity blockEntity,
                                   SpawnerRenderState state,
                                   float partialTicks,
                                   Vec3 cameraPos,
                                   @Nullable ModelFeatureRenderer.CrumblingOverlay overlay) {

        BlockEntityRenderState.extractBase(blockEntity, state, overlay);

        Level level = blockEntity.getLevel();
        if (level == null) {
            state.displayEntity = null;
            return;
        }

        BaseSpawner spawner = blockEntity.getSpawner();
        Entity displayEntity = spawner.getOrCreateDisplayEntity(level, blockEntity.getBlockPos());

        extractSpawnerData(
                state,
                partialTicks,
                displayEntity,
                this.entityRenderDispatcher,
                blockEntity.getLastRotation(),
                blockEntity.getRotation()
        );
    }

    public static class SpawnerRenderState extends BlockEntityRenderState {
        public @org.jspecify.annotations.Nullable EntityRenderState displayEntity;
        public float spin;
        public float scale;
    }

    public static void extractSpawnerData(final SpawnerRenderState state, final float partialTicks, final @org.jspecify.annotations.Nullable Entity displayEntity, final EntityRenderDispatcher entityRenderer, final double oSpin, final double spin) {
        if (displayEntity != null) {
            state.displayEntity = entityRenderer.extractEntity(displayEntity, partialTicks);
            state.displayEntity.lightCoords = state.lightCoords;
            state.spin = (float) Mth.lerp((double)partialTicks, oSpin, spin) * 10.0F;
            state.scale = 0.53125F;
            float maxLength = Math.max(displayEntity.getBbWidth(), displayEntity.getBbHeight());
            if ((double)maxLength > 1.0) {
                state.scale /= maxLength;
            }
        }
    }
}