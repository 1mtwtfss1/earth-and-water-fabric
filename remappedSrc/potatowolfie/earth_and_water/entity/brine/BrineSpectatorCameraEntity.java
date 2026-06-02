package potatowolfie.earth_and_water.entity.brine;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class BrineSpectatorCameraEntity extends Entity {
    private final BrineEntity parentBrine;
    private int activeQuarter = 0;

    public BrineSpectatorCameraEntity(EntityType<?> type, Level world, BrineEntity parent) {
        super(type, world);
        this.parentBrine = parent;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void tick() {
        super.tick();

        if (parentBrine != null && !parentBrine.isRemoved()) {
            this.setPos(parentBrine.getX(), parentBrine.getEyeY(), parentBrine.getZ());

            switch (activeQuarter) {
                case 0:
                    this.setYRot(parentBrine.getYRot() - 90.0F);
                    break;
                case 1, 2:
                    this.setYRot(parentBrine.getYRot());
                    break;
                case 3:
                    this.setYRot(parentBrine.getYRot());
                    break;
            }
        }
    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput view) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput view) {

    }

    public void setActiveQuarter(int quarter) {
        this.activeQuarter = Math.max(0, Math.min(3, quarter));
    }

    public int getActiveQuarter() {
        return activeQuarter;
    }
}