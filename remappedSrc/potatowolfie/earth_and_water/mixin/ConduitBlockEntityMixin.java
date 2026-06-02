package potatowolfie.earth_and_water.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import potatowolfie.earth_and_water.util.ModTags;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

@Mixin(ConduitBlockEntity.class)
public class ConduitBlockEntityMixin {

    @Shadow
    @Final
    private static Block[] VALID_BLOCKS;

    @Inject(
            method = "updateShape",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void addCustomFrameBlocks(Level world, BlockPos pos, List<BlockPos> activatingBlocks, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }

        activatingBlocks.clear();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    BlockPos waterPos = pos.offset(i, j, k);
                    if (!world.isWaterAt(waterPos)) {
                        return;
                    }
                }
            }
        }

        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 2; k++) {
                    int l = Math.abs(i);
                    int m = Math.abs(j);
                    int n = Math.abs(k);

                    if ((l > 1 || m > 1 || n > 1) &&
                            (i == 0 && (m == 2 || n == 2) ||
                                    j == 0 && (l == 2 || n == 2) ||
                                    k == 0 && (l == 2 || m == 2))) {

                        BlockPos framePos = pos.offset(i, j, k);
                        BlockState blockState = world.getBlockState(framePos);

                        for (Block block : VALID_BLOCKS) {
                            if (blockState.is(block)) {
                                activatingBlocks.add(framePos);
                                break;
                            }
                        }

                        if (blockState.is(ModTags.Blocks.E_W_CONDUIT_FRAME_BLOCKS)) {
                            activatingBlocks.add(framePos);
                        }
                    }
                }
            }
        }

        boolean hasEnoughBlocks = activatingBlocks.size() >= 16;
        cir.setReturnValue(hasEnoughBlocks);
    }

    @Redirect(
            method = "selectNewTarget",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;getEntitiesByClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
            )
    )
    private static List<LivingEntity> filterConduitImmuneEntities(
            ServerLevel world,
            Class<LivingEntity> entityClass,
            AABB box,
            Predicate<? super LivingEntity> predicate) {

        Predicate<LivingEntity> combinedPredicate = entity -> {
            if (!predicate.test(entity)) {
                return false;
            }

            boolean isImmune = entity.getType().is(ModTags.Entities.CONDUIT_IMMUNE);
            return !isImmune;
        };

        return world.getEntitiesOfClass(entityClass, box, combinedPredicate);
    }
}