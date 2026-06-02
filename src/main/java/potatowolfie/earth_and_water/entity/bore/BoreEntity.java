package potatowolfie.earth_and_water.entity.bore;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import potatowolfie.earth_and_water.entity.earth_charge.EarthChargeProjectileEntity;
import potatowolfie.earth_and_water.sound.ModSounds;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class BoreEntity extends Monster {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState shootingAnimationState = new AnimationState();
    public final AnimationState burrowingAnimationState = new AnimationState();
    public final AnimationState unburrowingAnimationState = new AnimationState();
    public final AnimationState whileburrowingAnimationState = new AnimationState();

    private int idleAnimationTimeout = 0;
    private boolean isIdleAnimationRunning = false;

    private BlockPos burrowDestination = null;
    private boolean isWalkingWhileBurrowed = false;
    private int burrowCooldown = 0;
    private int nextBurrowTime = 0;
    private int burrowCooldownTimer = 0;
    private static final int BURROW_COOLDOWN_TICKS = 80;
    private boolean burrowAnimPlayed = false;
    private boolean animationStartedThisTick = false;
    private boolean whileburrowAnimPlayed = false;

    private int shootingDelay = 0;
    private static final double PROJECTILE_DANGER_RADIUS = 6.0;
    private static final double FRIENDLY_PROJECTILE_AVOIDANCE_RADIUS = 8.0;

    private boolean isInCombat = false;
    private int combatStartTime = 0;
    private LivingEntity lastTarget = null;
    private Vec3 circlingCenter = null;
    private double circlingAngle = 0;
    private int circlingDirection = 1;

    private Vec3 stuckCheckPosition = null;
    private int stuckTimer = 0;
    private static final int STUCK_TIME_THRESHOLD = 60;
    private static final double STUCK_AREA_SIZE = 2.5;

    protected int getXpToDrop() {
        return 8 + this.random.nextInt(5);
    }

    public AnimationState getAnimationState(String name) {
        return switch (name) {
            case "BORE_SHOOTING" -> shootingAnimationState;
            case "BORE_BURROWING" -> burrowingAnimationState;
            case "BORE_WHILE_BURROWING" -> whileburrowingAnimationState;
            case "BORE_UNBURROWING" -> unburrowingAnimationState;
            default -> idleAnimationState;
        };
    }

    public enum BoreState {
        IDLE,
        SHOOTING,
        BURROWING,
        UNBURROWING
    }

    public enum BoreVariant {
        NORMAL(0),
        DARK(1);

        private final int id;

        BoreVariant(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static BoreVariant byId(int id) {
            for (BoreVariant variant : values()) {
                if (variant.getId() == id) {
                    return variant;
                }
            }
            return NORMAL;
        }
    }

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(BoreEntity.class, EntityDataSerializers.INT);

    private BoreState boreState = BoreState.IDLE;
    private BoreState previousState = BoreState.IDLE;
    private int animationTick = 0;
    private int stateTimer = 0;
    private int shootCooldown = 0;
    private BlockPos relocateTarget = null;
    private Vec3 lastShootPosition = null;
    private boolean hasMovedEnoughToShoot = true;

    public BoreEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 8;
    }

    public static AttributeSupplier.Builder createBoreAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.JUMP_STRENGTH, 0.42);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BoreAvoidProjectileGoal(this));
        this.goalSelector.addGoal(1, new BoreBurrowingMovementGoal(this));
        this.goalSelector.addGoal(2, new BoreShootGoal(this));
        this.goalSelector.addGoal(3, new BoreCircleGoal(this));
        this.goalSelector.addGoal(4, new BoreRelocateGoal(this));
        this.goalSelector.addGoal(5, new BoreFleeGoal(this));
        this.goalSelector.addGoal(6, new BoreSmartPositioningGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this, BoreEntity.class));
    }

    @Override
    public void tick() {
        if (this.isRemoved() || this.level() == null) {
            return;
        }

        animationStartedThisTick = false;

        super.tick();
        animationTick++;
        stateTimer++;

        if (shootCooldown > 0) {
            shootCooldown--;
        }

        if (shootingDelay > 0) {
            shootingDelay--;
        }

        if (burrowCooldownTimer > 0) {
            burrowCooldownTimer--;
        }

        try {
            updateCombatState();
            updateMovementTracking();
            updateMovementSpeed();
            handleStateTransitions();
            updateAnimations();

            if (this.level().isClientSide()) {
                switch (this.getBoreState()) {
                    case BURROWING -> {
                        if (this.stateTimer < 20) {
                            this.addBurrowParticles(this.burrowingAnimationState);
                        }
                    }
                    case UNBURROWING -> {
                        if (this.stateTimer < 10) {
                            this.addBurrowParticles(this.unburrowingAnimationState);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.BORE_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.BORE_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.BORE_AMBIENT;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(DATA_ID_STATE, BoreState.IDLE.ordinal());
    }

    private void updateCombatState() {
        LivingEntity target = this.getTarget();
        boolean wasInCombat = isInCombat;

        if (target != null && target.isAlive() && !target.isRemoved() && target != lastTarget) {
            isInCombat = true;
            combatStartTime = this.tickCount;
            lastTarget = target;
            circlingCenter = target.position();
            circlingAngle = this.random.nextDouble() * Math.PI * 2;
            circlingDirection = this.random.nextBoolean() ? 1 : -1;

        } else if (target == null || !target.isAlive() || target.isRemoved()) {
            isInCombat = false;
            lastTarget = null;
            circlingCenter = null;
            shootingDelay = 0;
            burrowCooldown = 0;
            nextBurrowTime = 0;
        }

        if (isInCombat && target != null && target.isAlive() && !target.isRemoved()) {
            circlingCenter = target.position();

            handleCombatBurrowing();
        }
    }

    private boolean isStuckInSmallArea() {
        if (boreState != BoreState.BURROWING || !isWalkingWhileBurrowed) {
            stuckCheckPosition = null;
            stuckTimer = 0;
            return false;
        }

        Vec3 currentPos = this.position();

        if (stuckCheckPosition == null) {
            stuckCheckPosition = currentPos;
            stuckTimer = 0;
            return false;
        }

        double distance = currentPos.distanceTo(stuckCheckPosition);

        if (distance <= STUCK_AREA_SIZE) {
            stuckTimer++;
            return stuckTimer >= STUCK_TIME_THRESHOLD;
        } else {
            stuckCheckPosition = currentPos;
            stuckTimer = 0;
            return false;
        }
    }

    private void handleCombatBurrowing() {
        if (burrowCooldown > 0) {
            burrowCooldown--;
        }

        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive() && !target.isRemoved()) {
            double distanceToTarget = this.distanceTo(target);

            List<BoreEntity> nearbyBurrowers = this.level().getEntitiesOfClass(
                    BoreEntity.class,
                    this.getBoundingBox().inflate(16.0),
                    bore -> bore != this && bore.getBoreState() == BoreState.BURROWING
            );

            boolean withinLimit = nearbyBurrowers.size() < 4;

            if (distanceToTarget <= 4.0 &&
                    burrowCooldown <= 0 &&
                    burrowCooldownTimer <= 0 &&
                    boreState == BoreState.IDLE &&
                    !isNearbyProjectileDangerous() &&
                    withinLimit) {

                startBurrowing();
                burrowCooldown = 200 + this.random.nextInt(100);

                for (BoreEntity bore : nearbyBurrowers) {
                    Vec3 away = bore.position().subtract(target.position()).normalize();
                    Vec3 retreatPos = bore.position().add(away.scale(6.0));
                    bore.getNavigation().moveTo(retreatPos.x, retreatPos.y, retreatPos.z, 1.3);
                }
            }
        }
    }

    private void updateAnimations() {
        if (this.level().isClientSide()) {
            if (this.boreState == BoreState.IDLE) {
                if (!isIdleAnimationRunning) {
                    --this.idleAnimationTimeout;
                    if (this.idleAnimationTimeout <= 0) {
                        this.idleAnimationTimeout = this.random.nextInt(40) + 80;
                        this.idleAnimationState.start(this.tickCount);
                        this.isIdleAnimationRunning = true;
                    }
                }
            } else {
                this.idleAnimationTimeout = 0;
                this.isIdleAnimationRunning = false;
                this.idleAnimationState.stop();
            }
        }
    }

    private void updateMovementTracking() {
        if (lastShootPosition != null) {
            Vec3 currentPos = this.position();
            if (currentPos != null) {
                double distanceMoved = currentPos.distanceTo(lastShootPosition);

                if (distanceMoved >= 4.0) {
                    hasMovedEnoughToShoot = true;
                }
            }
        }
    }

    private void updateMovementSpeed() {
        double baseSpeed = 0.23;

        if (isInCombat && this.getTarget() != null) {
            int timeSinceCombatStart = this.tickCount - combatStartTime;
            double progressionFactor = Math.min(timeSinceCombatStart / 120.0, 1.0);

            progressionFactor = easeInOutQuad(progressionFactor);

            double targetSpeed = 0.35;
            baseSpeed = baseSpeed + (targetSpeed - baseSpeed) * progressionFactor;
        }

        switch (boreState) {
            case BURROWING:
                if (isWalkingWhileBurrowed) {
                    if (isInCombat && this.getTarget() != null) {
                        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.35);
                    } else {
                        Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.6);
                    }
                } else {
                    Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(baseSpeed * 0.1);
                }
                break;
            case UNBURROWING:
                Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(baseSpeed * 0.1);
                break;
            default:
                Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(baseSpeed);
                break;
        }
    }

    private double easeInOutQuad(double t) {
        return t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2;
    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource damageSource, float amount) {
        if (damageSource.is(DamageTypes.FALL) && boreState == BoreState.BURROWING) {
            return false;
        }

        if (this.getBoreState() == BoreState.BURROWING) {
            if (damageSource.is(DamageTypes.FELL_OUT_OF_WORLD) ||
                    damageSource.is(DamageTypes.GENERIC_KILL)) {
                return super.hurtServer(world, damageSource, amount);
            }

            Entity attacker = damageSource.getEntity();
            if (attacker instanceof LivingEntity living) {
                ItemStack weapon = living.getMainHandItem();
                if (weapon.getItem().toString().contains("pickaxe")) {
                    this.forceUnburrow();
                    this.burrowCooldownTimer = 80;
                    return super.hurtServer(world, damageSource, amount);
                }
            }
            return false;
        }
        return super.hurtServer(world, damageSource, amount);
    }


    private void handleStateTransitions() {
        switch (boreState) {
            case SHOOTING:
                if (stateTimer == 20 && !this.level().isClientSide()) {
                    fireEarthCharge();
                }
                if (stateTimer >= 40) {
                    setBoreState(BoreState.IDLE);
                    setRelocationTarget();
                }
                break;
            case BURROWING:
                if (stateTimer == 20) {
                    if (!isWalkingWhileBurrowed) {
                        setBurrowDestination();
                        isWalkingWhileBurrowed = true;

                        if (this.level().isClientSide() && burrowDestination != null && !whileburrowAnimPlayed) {
                            burrowingAnimationState.stop();
                            whileburrowingAnimationState.start(this.tickCount);
                            whileburrowAnimPlayed = true;
                        } else if (this.level().isClientSide() && burrowDestination == null) {
                            forceUnburrow();
                            return;
                        }
                    }
                }

                if (isWalkingWhileBurrowed && burrowDestination != null) {
                    if (this.getNavigation().isDone()) {
                        this.getNavigation().moveTo(
                                burrowDestination.getX(),
                                this.getY(),
                                burrowDestination.getZ(),
                                isInCombat && this.getTarget() != null ? 1.2 : 1.4
                        );
                    }

                    double distanceToDestination = Math.sqrt(this.distanceToSqr(
                            burrowDestination.getX(), burrowDestination.getY(), burrowDestination.getZ()));

                    if (distanceToDestination < 1.5 || stateTimer >= 300) {
                        forceUnburrow();
                    }
                } else {
                    if (stateTimer >= 70) {
                        forceUnburrow();
                    }
                }
                break;
            case UNBURROWING:
                if (stateTimer >= 40) {
                    setBoreState(BoreState.IDLE);
                    burrowCooldownTimer = BURROW_COOLDOWN_TICKS;
                }
                break;
        }
    }

    private void forceUnburrow() {
        if (this.level().isClientSide()) {
            whileburrowingAnimationState.stop();
            burrowingAnimationState.stop();
        }
        setBoreState(BoreState.UNBURROWING);
        burrowDestination = null;
        isWalkingWhileBurrowed = false;
        stuckCheckPosition = null;
        stuckTimer = 0;
    }

    private void setBurrowDestination() {
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive() || target.isRemoved()) {
            setBurrowDestinationRandom();
            return;
        }

        double currentY = this.getY();
        Vec3 targetPos = target.position();
        Vec3 currentPos = this.position();

        Vec3 awayDirection = currentPos.subtract(targetPos).normalize();

        BlockPos bestDestination = null;
        double bestScore = Double.MAX_VALUE;

        for (int attempts = 0; attempts < 24; attempts++) {
            double baseAngle = Math.atan2(awayDirection.z, awayDirection.x);
            double variance = (this.random.nextDouble() - 0.5) * Math.PI * 0.6;
            double angle = baseAngle + variance;

            double distance = 8 + this.random.nextDouble() * 6;

            double offsetX = Math.cos(angle) * distance;
            double offsetZ = Math.sin(angle) * distance;

            double targetX = this.getX() + offsetX;
            double targetZ = this.getZ() + offsetZ;

            double groundY = findGroundLevel(targetX, targetZ);

            if (groundY != -1) {
                if (groundY < currentY) {
                    continue;
                }

                BlockPos targetPosBlock = new BlockPos((int) targetX, (int) groundY, (int) targetZ);

                if (isPositionSafeForBurrowing(targetPosBlock)) {
                    double distanceFromTarget = Math.sqrt(Math.pow(targetX - targetPos.x, 2) + Math.pow(targetZ - targetPos.z, 2));
                    double score = Math.abs(groundY - currentY) + (distance * 0.1) - (distanceFromTarget * 0.2);

                    if (!isPathBlocked(new Vec3(targetPosBlock.getX(), targetPosBlock.getY(), targetPosBlock.getZ()))) {
                        score -= 5.0;
                    }

                    if (score < bestScore) {
                        bestScore = score;
                        bestDestination = targetPosBlock;
                    }
                }
            }
        }

        if (bestDestination != null) {
            this.burrowDestination = bestDestination;
            return;
        }

        for (int attempts = 0; attempts < 16; attempts++) {
            double baseAngle = Math.atan2(awayDirection.z, awayDirection.x);
            double variance = (this.random.nextDouble() - 0.5) * Math.PI * 0.8;
            double angle = baseAngle + variance;
            double distance = 4 + this.random.nextDouble() * 4;

            double offsetX = Math.cos(angle) * distance;
            double offsetZ = Math.sin(angle) * distance;

            double targetX = this.getX() + offsetX;
            double targetZ = this.getZ() + offsetZ;

            BlockPos targetPosBlock = new BlockPos((int) targetX, (int) currentY, (int) targetZ);

            if (targetPosBlock.getY() < this.getBlockY()) {
                continue;
            }

            if (isPositionSafe(targetPosBlock)) {
                this.burrowDestination = targetPosBlock;
                return;
            }
        }

        this.burrowDestination = null;
    }

    private void setBurrowDestinationRandom() {
        double currentY = this.getY();
        BlockPos bestDestination = null;
        double bestScore = Double.MAX_VALUE;

        for (int attempts = 0; attempts < 24; attempts++) {
            double angle = this.random.nextDouble() * 2 * Math.PI;
            double distance = 8 + this.random.nextDouble() * 6;

            double offsetX = Math.cos(angle) * distance;
            double offsetZ = Math.sin(angle) * distance;

            double targetX = this.getX() + offsetX;
            double targetZ = this.getZ() + offsetZ;

            double groundY = findGroundLevel(targetX, targetZ);

            if (groundY != -1) {
                BlockPos targetPos = new BlockPos((int)targetX, (int)groundY, (int)targetZ);

                if (isPositionSafeForBurrowing(targetPos)) {
                    double score = Math.abs(groundY - currentY) + (distance * 0.1);

                    if (!isPathBlocked(new Vec3(targetPos.getX(), targetPos.getY(), targetPos.getZ()))) {
                        score -= 5.0;
                    }

                    if (score < bestScore) {
                        bestScore = score;
                        bestDestination = targetPos;
                    }
                }
            }
        }

        if (bestDestination != null) {
            this.burrowDestination = bestDestination;
            return;
        }

        for (int attempts = 0; attempts < 16; attempts++) {
            double angle = this.random.nextDouble() * 2 * Math.PI;
            double distance = 4 + this.random.nextDouble() * 4;

            double offsetX = Math.cos(angle) * distance;
            double offsetZ = Math.sin(angle) * distance;

            double targetX = this.getX() + offsetX;
            double targetZ = this.getZ() + offsetZ;

            BlockPos targetPos = new BlockPos((int)targetX, (int)currentY, (int)targetZ);

            if (isPositionSafe(targetPos)) {
                this.burrowDestination = targetPos;
                return;
            }
        }

        this.burrowDestination = null;
    }

    private boolean isPositionSafeForBurrowing(BlockPos pos) {
        if (this.level() == null || pos == null) return false;

        if (this.level().getBlockState(pos).getBlock().toString().contains("lava") ||
                this.level().getBlockState(pos).getBlock().toString().contains("water")) {
            return false;
        }

        int topY = this.level().getMinY() + this.level().getHeight() - 1;
        if (pos.getY() <= this.level().getMinY() || pos.getY() >= topY - 2) {
            return false;
        }

        BlockPos belowPos = pos.below();
        if (!this.level().getBlockState(belowPos).isRedstoneConductor(this.level(), belowPos)) {
            return false;
        }

        if (this.level().getBlockState(pos).isRedstoneConductor(this.level(), pos) ||
                this.level().getBlockState(pos.above()).isRedstoneConductor(this.level(), pos.above())) {
            return false;
        }

        return true;
    }

    private static class BoreBurrowingMovementGoal extends Goal {
        private final BoreEntity bore;
        private int stuckCounter = 0;
        private Vec3 lastPosition = null;
        private int forceMovementTimer = 0;

        public BoreBurrowingMovementGoal(BoreEntity bore) {
            this.bore = bore;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return bore.getBoreState() == BoreState.BURROWING &&
                    bore.isWalkingWhileBurrowed &&
                    bore.burrowDestination != null;
        }

        @Override
        public void start() {
            stuckCounter = 0;
            forceMovementTimer = 0;
            lastPosition = bore.position();
        }

        @Override
        public void tick() {
            if (bore.burrowDestination != null) {
                double currentY = bore.getY();
                double moveSpeed = (bore.isInCombat() && bore.getTarget() != null) ? 1.2 : 1.4;

                Vec3 currentPos = bore.position();

                if (lastPosition != null && currentPos.distanceTo(lastPosition) < 0.1) {
                    stuckCounter++;
                    forceMovementTimer++;

                    if (stuckCounter > 10) {
                        BlockPos alternative = bore.findAlternativeBurrowDestination();
                        if (alternative != null) {
                            if (alternative.getY() < bore.getBlockY()) {
                                bore.forceUnburrow();
                                return;
                            }

                            bore.burrowDestination = alternative;
                            bore.getNavigation().stop();
                            bore.getNavigation().moveTo(
                                    alternative.getX(), currentY, alternative.getZ(), moveSpeed
                            );
                            stuckCounter = 0;
                            forceMovementTimer = 0;
                        }
                    }

                    if (forceMovementTimer > 5 && bore.burrowDestination != null) {
                        Vec3 direction = new Vec3(
                                bore.burrowDestination.getX(), currentY, bore.burrowDestination.getZ()
                        ).subtract(currentPos).normalize();

                        Vec3 forceMovement = direction.scale(0.15);
                        bore.setDeltaMovement(bore.getDeltaMovement().add(forceMovement));
                        forceMovementTimer = 0;
                    }
                } else {
                    stuckCounter = 0;
                    forceMovementTimer = 0;
                }

                lastPosition = currentPos;

                if (bore.getNavigation().isDone()) {
                    if (bore.burrowDestination.getY() < bore.getBlockY()) {
                        bore.forceUnburrow();
                        return;
                    }

                    bore.getNavigation().moveTo(
                            bore.burrowDestination.getX(),
                            currentY,
                            bore.burrowDestination.getZ(),
                            moveSpeed
                    );
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return bore.getBoreState() == BoreState.BURROWING &&
                    bore.isWalkingWhileBurrowed &&
                    bore.burrowDestination != null;
        }
    }

    private boolean isNavigationStuck() {
        return this.getNavigation().isDone() &&
                boreState == BoreState.BURROWING &&
                isWalkingWhileBurrowed &&
                burrowDestination != null;
    }

    private boolean wouldFallOffEdge(Vec3 targetPos) {
        if (this.level() == null || targetPos == null) return true;

        BlockPos blockPos = new BlockPos((int)targetPos.x, (int)targetPos.y, (int)targetPos.z);
        BlockPos belowPos = blockPos.below();

        for (int i = 1; i <= 3; i++) {
            BlockPos checkPos = belowPos.below(i);
            if (this.level().getBlockState(checkPos).isRedstoneConductor(this.level(), checkPos)) {
                return false;
            }
        }

        return true;
    }

    private void setRelocationTarget() {
        for (int attempts = 0; attempts < 10; attempts++) {
            double angle = this.random.nextDouble() * 2 * Math.PI;
            double distance = 10 + this.random.nextDouble() * 2;

            double offsetX = Math.cos(angle) * distance;
            double offsetZ = Math.sin(angle) * distance;

            BlockPos targetPos = new BlockPos((int)(this.getX() + offsetX), (int)this.getY(), (int)(this.getZ() + offsetZ));

            if (isPositionSafe(targetPos)) {
                this.relocateTarget = targetPos;
                break;
            }
        }
    }

    private boolean isPositionSafe(BlockPos pos) {
        if (this.level() == null || pos == null) return false;
        return this.level().isEmptyBlock(pos) &&
                this.level().isEmptyBlock(pos.above()) &&
                this.level().getBlockState(pos.below()).isRedstoneConductor(this.level(), pos.below());
    }

    public Vec3 getCirclingPosition() {
        if (circlingCenter == null) return null;

        double radius = 8.0;
        double x = circlingCenter.x + Math.cos(circlingAngle) * radius;
        double z = circlingCenter.z + Math.sin(circlingAngle) * radius;

        double y = circlingCenter.y;
        BlockPos testPos = new BlockPos((int)x, (int)y, (int)z);

        if (!isPositionSafe(testPos)) {
            for (int yOffset = -2; yOffset <= 3; yOffset++) {
                BlockPos adjustedPos = testPos.offset(0, yOffset, 0);
                if (isPositionSafe(adjustedPos)) {
                    y = adjustedPos.getY();
                    break;
                }
            }
        }

        return new Vec3(x, y, z);
    }

    @Override
    public void travel(Vec3 movementInput) {
        if (getBoreState() == BoreState.BURROWING && isWalkingWhileBurrowed) {
            Vec3 currentPos = this.position();
            Vec3 targetPos = currentPos.add(movementInput);

            BlockPos currentBlock = BlockPos.containing(currentPos);
            BlockPos targetBlock = BlockPos.containing(targetPos);

            if (targetBlock.getY() < currentBlock.getY()) {
                this.setDeltaMovement(0, 0, 0);
                return;
            }

            Vec3 flatMovement = new Vec3(movementInput.x, 0, movementInput.z);
            super.travel(flatMovement);
            return;
        }

        super.travel(movementInput);
    }

    private double findGroundLevel(double x, double z) {
        if (this.level() == null) return -1;

        int blockX = (int) Math.floor(x);
        int blockZ = (int) Math.floor(z);
        int startY = (int) this.getY();
        int topY = this.level().getMinY() + this.level().getHeight() - 1;

        for (int y = startY; y >= this.level().getMinY(); y--) {
            BlockPos checkPos = new BlockPos(blockX, y, blockZ);
            BlockPos abovePos = checkPos.above();

            if (this.level().getBlockState(checkPos).isRedstoneConductor(this.level(), checkPos) &&
                    (!this.level().getBlockState(abovePos).isRedstoneConductor(this.level(), abovePos) ||
                            this.level().isEmptyBlock(abovePos))) {

                BlockPos aboveAbove = abovePos.above();
                if (!this.level().getBlockState(aboveAbove).isRedstoneConductor(this.level(), aboveAbove) ||
                        this.level().isEmptyBlock(aboveAbove)) {
                    return y + 1.0;
                }
            }
        }

        for (int y = startY + 1; y <= topY - 2; y++) {
            BlockPos checkPos = new BlockPos(blockX, y, blockZ);
            BlockPos abovePos = checkPos.above();

            if (this.level().getBlockState(checkPos).isRedstoneConductor(this.level(), checkPos) &&
                    (!this.level().getBlockState(abovePos).isRedstoneConductor(this.level(), abovePos) ||
                            this.level().isEmptyBlock(abovePos))) {

                BlockPos aboveAbove = abovePos.above();
                if (!this.level().getBlockState(aboveAbove).isRedstoneConductor(this.level(), aboveAbove) ||
                        this.level().isEmptyBlock(aboveAbove)) {
                    return y + 1.0;
                }
            }
        }

        return -1;
    }

    @Override
    protected float getJumpPower() {
        if (boreState == BoreState.BURROWING) {
            return 0.0f;
        }
        return super.getJumpPower();
    }

    @Override
    public boolean onClimbable() {
        return super.onClimbable() && boreState != BoreState.BURROWING;
    }

    public void updateCirclingAngle() {
        double angularSpeed = 0.05;
        circlingAngle += circlingDirection * angularSpeed;

        while (circlingAngle > Math.PI * 2) circlingAngle -= Math.PI * 2;
        while (circlingAngle < 0) circlingAngle += Math.PI * 2;
    }

    public boolean isInCombat() {
        return isInCombat;
    }

    public int getCombatDuration() {
        return isInCombat ? this.tickCount - combatStartTime : 0;
    }

    private boolean isNearbyProjectileDangerous() {
        if (this.level() == null) return false;

        try {
            List<EarthChargeProjectileEntity> projectiles = this.level().getEntitiesOfClass(
                    EarthChargeProjectileEntity.class,
                    this.getBoundingBox().inflate(PROJECTILE_DANGER_RADIUS),
                    projectile -> projectile != null && projectile.getOwner() != this
            );

            return !projectiles.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isNearFriendlyProjectile() {
        if (this.level() == null) return false;

        try {
            List<EarthChargeProjectileEntity> friendlyProjectiles = this.level().getEntitiesOfClass(
                    EarthChargeProjectileEntity.class,
                    this.getBoundingBox().inflate(FRIENDLY_PROJECTILE_AVOIDANCE_RADIUS),
                    projectile -> projectile != null && projectile.getOwner() instanceof BoreEntity && projectile.getOwner() != this
            );

            return !friendlyProjectiles.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private Vec3 getProjectileAvoidanceDirection() {
        if (this.level() == null) return null;

        try {
            List<EarthChargeProjectileEntity> projectiles = this.level().getEntitiesOfClass(
                    EarthChargeProjectileEntity.class,
                    this.getBoundingBox().inflate(Math.max(PROJECTILE_DANGER_RADIUS, FRIENDLY_PROJECTILE_AVOIDANCE_RADIUS)),
                    projectile -> projectile != null && projectile.getOwner() != this
            );

            if (projectiles.isEmpty()) return null;

            Vec3 avoidanceDirection = Vec3.ZERO;
            for (EarthChargeProjectileEntity projectile : projectiles) {
                if (projectile != null && projectile.position() != null) {
                    Vec3 directionAway = this.position().subtract(projectile.position()).normalize();
                    double weight = (projectile.getOwner() instanceof BoreEntity) ? 1.5 : 1.0;
                    avoidanceDirection = avoidanceDirection.add(directionAway.scale(weight));
                }
            }

            return avoidanceDirection.normalize();
        } catch (Exception e) {
            return null;
        }
    }

    private int countNearbyShootingAllies() {
        if (this.level() == null) return 0;

        try {
            List<BoreEntity> nearbyAllies = this.level().getEntitiesOfClass(
                    BoreEntity.class,
                    this.getBoundingBox().inflate(16.0),
                    bore -> bore != null && bore != this && bore.isAlive() && !bore.isRemoved()
            );

            int shootingCount = 0;
            for (BoreEntity ally : nearbyAllies) {
                if (this.distanceTo(ally) <= 16.0 &&
                        (ally.getBoreState() == BoreState.SHOOTING || ally.shootCooldown > 45)) {
                    shootingCount++;
                }
            }

            return shootingCount;
        } catch (Exception e) {
            return 0;
        }
    }

    public BlockPos getRelocationTarget() {
        return relocateTarget;
    }

    public void clearRelocationTarget() {
        this.relocateTarget = null;
    }

    public int getAnimationTick() {
        return animationTick;
    }

    public int getStateTimer() {
        return stateTimer;
    }

    public BoreState getBoreState() {
        return boreState;
    }

    public BoreState getPreviousState() {
        return previousState;
    }

    private boolean isChangingState = false;

    public void setBoreState(BoreState newState) {
        if (this.boreState != newState && !isChangingState) {
            if (!isValidStateTransition(this.boreState, newState)) {
                return;
            }

            isChangingState = true;

            this.previousState = this.boreState;
            this.boreState = newState;
            this.animationTick = 0;
            this.stateTimer = 0;

            if (!this.level().isClientSide()) {
                this.entityData.set(DATA_ID_STATE, newState.ordinal());
            } else {
                startStateAnimation(newState);
            }

            if (newState == BoreState.IDLE && previousState == BoreState.UNBURROWING) {
                burrowAnimPlayed = false;
                whileburrowAnimPlayed = false;
            }

            isChangingState = false;
        }
    }

    private void startStateAnimation(BoreState state) {
        if (!this.level().isClientSide() || animationStartedThisTick) return;

        animationStartedThisTick = true;

        switch (state) {
            case IDLE -> {
                if (previousState == BoreState.UNBURROWING) {
                    burrowingAnimationState.stop();
                    whileburrowingAnimationState.stop();
                    unburrowingAnimationState.stop();

                    burrowAnimPlayed = false;
                    whileburrowAnimPlayed = false;
                }

                if (previousState == BoreState.SHOOTING) {
                    shootingAnimationState.stop();
                }

                this.idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idleAnimationState.start(this.tickCount);
                this.isIdleAnimationRunning = true;
            }
            case SHOOTING -> {
                stopAllAnimations();
                this.shootingAnimationState.start(this.tickCount);
                this.isIdleAnimationRunning = false;
            }
            case BURROWING -> {
                idleAnimationState.stop();
                shootingAnimationState.stop();
                unburrowingAnimationState.stop();

                if (!burrowAnimPlayed) {
                    this.burrowingAnimationState.start(this.tickCount);
                    burrowAnimPlayed = true;
                }
                this.isIdleAnimationRunning = false;
            }
            case UNBURROWING -> {
                whileburrowingAnimationState.stop();
                burrowingAnimationState.stop();
                idleAnimationState.stop();
                shootingAnimationState.stop();

                this.unburrowingAnimationState.start(this.tickCount);
                this.isIdleAnimationRunning = false;
            }
        }
    }

    private boolean isValidStateTransition(BoreState from, BoreState to) {
        switch (from) {
            case IDLE:
                return to == BoreState.SHOOTING || to == BoreState.BURROWING;
            case SHOOTING:
                return to == BoreState.IDLE;
            case BURROWING:
                return to == BoreState.UNBURROWING;
            case UNBURROWING:
                return to == BoreState.IDLE;
            default:
                return false;
        }
    }

    private boolean isPathBlocked(Vec3 targetPos) {
        if (this.level() == null || targetPos == null) return true;

        Vec3 currentPos = this.position();
        Vec3 direction = targetPos.subtract(currentPos).normalize();

        for (double step = 1.0; step <= 3.0; step += 0.5) {
            Vec3 checkPos = currentPos.add(direction.scale(step));
            BlockPos blockPos = new BlockPos((int)checkPos.x, (int)checkPos.y, (int)checkPos.z);

            if (this.level().getBlockState(blockPos).isRedstoneConductor(this.level(), blockPos) ||
                    this.level().getBlockState(blockPos.above()).isRedstoneConductor(this.level(), blockPos.above())) {
                return true;
            }
        }

        return false;
    }

    private BlockPos findAlternativeBurrowDestination() {
        double currentY = this.getY();

        for (int attempts = 0; attempts < 24; attempts++) {
            double angle = (Math.PI * 2 * attempts) / 24.0;
            double distance = 8 + this.random.nextDouble() * 4;

            double offsetX = Math.cos(angle) * distance;
            double offsetZ = Math.sin(angle) * distance;

            BlockPos targetPos = new BlockPos(
                    (int)(this.getX() + offsetX),
                    (int)currentY,
                    (int)(this.getZ() + offsetZ)
            );

            if (isPositionSafeForBurrowing(targetPos) && !isPathBlocked(new Vec3(targetPos.getX(), targetPos.getY(), targetPos.getZ()))) {
                return targetPos;
            }
        }

        return null;
    }

    private void stopAllAnimations() {
        if (this.level().isClientSide()) {
            idleAnimationState.stop();
            shootingAnimationState.stop();
            burrowingAnimationState.stop();
            whileburrowingAnimationState.stop();
            unburrowingAnimationState.stop();
        }
    }

    public boolean canShoot() {
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive() || target.isRemoved()) return false;
        if (boreState == BoreState.BURROWING || boreState == BoreState.UNBURROWING) return false;

        try {
            double distance = this.distanceTo(target);
            if (distance < 4.0) return false;
            if (!hasMovedEnoughToShoot || isNearbyProjectileDangerous()) return false;
            if (isNearFriendlyProjectile()) return false;

            if (isStuckInSmallArea() && distance < 6.0) return false;

            return shootingDelay <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    public void tryShootAtPlayer() {
        if (shootCooldown > 0 || !canShoot()) return;

        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive() || target.isRemoved()) return;

        int nearbyShooters = (int) this.level().getEntitiesOfClass(
                BoreEntity.class,
                this.getBoundingBox().inflate(16.0),
                bore -> bore != this && bore.getBoreState() == BoreState.SHOOTING
        ).size();

        if (nearbyShooters >= 2) return;

        this.level().getEntitiesOfClass(
                BoreEntity.class,
                this.getBoundingBox().inflate(8.0),
                bore -> bore != this && bore.isInCombat()
        ).forEach(ally -> {
            Vec3 away = ally.position().subtract(this.position()).normalize();
            ally.getNavigation().moveTo(
                    ally.getX() + away.x * 6,
                    ally.getY(),
                    ally.getZ() + away.z * 6,
                    1.4
            );
        });

        shootCooldown = 40 + this.random.nextInt(20);
        this.setBoreState(BoreState.SHOOTING);
        lastShootPosition = this.position();
        hasMovedEnoughToShoot = false;
        shootingDelay = 5 + this.random.nextInt(10);
    }



    public void startBurrowing() {
        if (boreState == BoreState.IDLE && !isWalkingWhileBurrowed) {
            boolean canBurrow = false;

            for (int quickCheck = 0; quickCheck < 8; quickCheck++) {
                double angle = (Math.PI * 2 * quickCheck) / 8.0;
                double distance = 6 + this.random.nextDouble() * 4;

                double offsetX = Math.cos(angle) * distance;
                double offsetZ = Math.sin(angle) * distance;

                BlockPos testPos = new BlockPos(
                        (int)(this.getX() + offsetX),
                        (int)this.getY(),
                        (int)(this.getZ() + offsetZ)
                );

                if (isPositionSafeForBurrowing(testPos)) {
                    canBurrow = true;
                    break;
                }
            }

            if (canBurrow) {
                setBoreState(BoreState.BURROWING);
            }
        }
    }

    private void fireEarthCharge() {
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive() || target.isRemoved()) return;

        Vec3 targetPos = predictTargetPosition(target);
        if (targetPos == null) return;

        Vec3 direction = targetPos.subtract(this.position()).normalize();

        try {
            EarthChargeProjectileEntity charge = new EarthChargeProjectileEntity(this.level(), this);
            charge.setPos(this.getX(), this.getEyeY(), this.getZ());
            charge.shoot(direction.x, direction.y, direction.z, 1.2f, 0.05f);
            this.level().addFreshEntity(charge);
        } catch (Exception e) {
        }
    }

    private Vec3 predictTargetPosition(LivingEntity target) {
        if (target == null || !target.isAlive() || target.isRemoved()) {
            return this.position();
        }

        try {
            Vec3 targetVelocity = target.getDeltaMovement();
            if (targetVelocity == null) {
                targetVelocity = Vec3.ZERO;
            }

            double projectileSpeed = 1.2;
            double distance = this.distanceTo(target);
            double timeToHit = distance / projectileSpeed;

            Vec3 predictedPos = target.position().add(targetVelocity.scale(timeToHit));
            return predictedPos.add(0, target.getEyeHeight() - 1.0, 0);
        } catch (Exception e) {
            return target.position();
        }
    }

    public String getCurrentAnimation() {
        switch (boreState) {
            case IDLE -> {
                return "BORE_IDLE";
            }
            case SHOOTING -> {
                return "BORE_SHOOTING";
            }
            case BURROWING -> {
                return "BORE_BURROWING";
            }
            case UNBURROWING -> {
                return "BORE_UNBURROWING";
            }
            default -> {
                return "BORE_IDLE";
            }
        }
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public BoreVariant getVariant() {
        return BoreVariant.byId(this.getTypeVariant() & 255);
    }

    public void setVariant(BoreVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean isDarkVariant() {
        return getVariant() == BoreVariant.DARK;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
        BoreVariant variant = world.getRandom().nextFloat() < 0.005f ? BoreVariant.DARK : BoreVariant.NORMAL;
        setVariant(variant);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("BoreState", boreState.name());
        nbt.putInt("StateTimer", stateTimer);
        nbt.putInt("Variant", this.getTypeVariant());
        nbt.putBoolean("HasMovedEnoughToShoot", hasMovedEnoughToShoot);
        nbt.putBoolean("IsInCombat", isInCombat);
        nbt.putInt("CombatStartTime", combatStartTime);
        nbt.putDouble("CirclingAngle", circlingAngle);
        nbt.putInt("CirclingDirection", circlingDirection);
        nbt.putInt("ShootingDelay", shootingDelay);
        nbt.putInt("StuckTimer", stuckTimer);
        nbt.putInt("BurrowCooldownTimer", burrowCooldownTimer);

        if (stuckCheckPosition != null) {
            nbt.putDouble("StuckCheckX", stuckCheckPosition.x);
            nbt.putDouble("StuckCheckY", stuckCheckPosition.y);
            nbt.putDouble("StuckCheckZ", stuckCheckPosition.z);
        }

        if (relocateTarget != null) {
            nbt.putLong("RelocateTarget", relocateTarget.asLong());
        }
        if (lastShootPosition != null) {
            nbt.putDouble("LastShootX", lastShootPosition.x);
            nbt.putDouble("LastShootY", lastShootPosition.y);
            nbt.putDouble("LastShootZ", lastShootPosition.z);
        }
        if (circlingCenter != null) {
            nbt.putDouble("CirclingCenterX", circlingCenter.x);
            nbt.putDouble("CirclingCenterY", circlingCenter.y);
            nbt.putDouble("CirclingCenterZ", circlingCenter.z);
        }
    }

    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);
        String stateString = nbt.getStringOr("BoreState", "IDLE");
        if (!stateString.equals("IDLE")) {
            try {
                BoreState loadedState = BoreState.valueOf(stateString);
                this.boreState = loadedState;
                if (!this.level().isClientSide()) {
                    this.entityData.set(DATA_ID_STATE, loadedState.ordinal());
                }
            } catch (IllegalArgumentException e) {
                this.boreState = BoreState.IDLE;
            }
        }

        this.stateTimer = nbt.getIntOr("StateTimer", 0);
        this.entityData.set(DATA_ID_TYPE_VARIANT, nbt.getIntOr("Variant", 0));
        this.hasMovedEnoughToShoot = nbt.getBooleanOr("HasMovedEnoughToShoot", false);
        this.isInCombat = nbt.getBooleanOr("IsInCombat", false);
        this.combatStartTime = nbt.getIntOr("CombatStartTime", 0);
        this.circlingAngle = nbt.getDoubleOr("CirclingAngle", 0.0);
        this.circlingDirection = nbt.getIntOr("CirclingDirection", 1);
        this.shootingDelay = nbt.getIntOr("ShootingDelay", 0);
        this.stuckTimer = nbt.getIntOr("StuckTimer", 0);
        this.burrowCooldownTimer = nbt.getIntOr("BurrowCooldownTimer", 0);

        double stuckCheckX = nbt.getDoubleOr("StuckCheckX", Double.NaN);
        if (!Double.isNaN(stuckCheckX)) {
            this.stuckCheckPosition = new Vec3(
                    stuckCheckX,
                    nbt.getDoubleOr("StuckCheckY", 0.0),
                    nbt.getDoubleOr("StuckCheckZ", 0.0)
            );
        }

        long relocateTargetLong = nbt.getLongOr("RelocateTarget", Long.MIN_VALUE);
        if (relocateTargetLong != Long.MIN_VALUE) {
            this.relocateTarget = BlockPos.of(relocateTargetLong);
        }

        double lastShootX = nbt.getDoubleOr("LastShootX", Double.NaN);
        if (!Double.isNaN(lastShootX)) {
            this.lastShootPosition = new Vec3(
                    lastShootX,
                    nbt.getDoubleOr("LastShootY", 0.0),
                    nbt.getDoubleOr("LastShootZ", 0.0)
            );
        }

        double circlingCenterX = nbt.getDoubleOr("CirclingCenterX", Double.NaN);
        if (!Double.isNaN(circlingCenterX)) {
            this.circlingCenter = new Vec3(
                    circlingCenterX,
                    nbt.getDoubleOr("CirclingCenterY", 0.0),
                    nbt.getDoubleOr("CirclingCenterZ", 0.0)
            );
        }
    }

    private static class BoreCircleGoal extends Goal {
        private final BoreEntity bore;
        private Vec3 targetPosition;
        private int repositionTimer = 0;

        public BoreCircleGoal(BoreEntity bore) {
            this.bore = bore;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = bore.getTarget();
            return bore.isInCombat() &&
                    target != null &&
                    target.isAlive() &&
                    !target.isRemoved() &&
                    bore.getBoreState() == BoreState.IDLE &&
                    bore.getCombatDuration() < 120;
        }

        @Override
        public void start() {
            targetPosition = bore.getCirclingPosition();
            repositionTimer = 0;
        }

        @Override
        public void tick() {
            repositionTimer++;

            bore.updateCirclingAngle();

            if (repositionTimer >= 10 ||
                    (targetPosition != null && bore.distanceToSqr(targetPosition) < 2.0)) {
                targetPosition = bore.getCirclingPosition();
                repositionTimer = 0;
            }

            if (targetPosition != null) {
                bore.getNavigation().moveTo(targetPosition.x, targetPosition.y, targetPosition.z, 1.2);
            }

            LivingEntity target = bore.getTarget();
            if (target != null && target.isAlive() && !target.isRemoved()) {
                try {
                    bore.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
                } catch (Exception e) {
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = bore.getTarget();
            return bore.isInCombat() &&
                    target != null &&
                    target.isAlive() &&
                    !target.isRemoved() &&
                    bore.getBoreState() == BoreState.IDLE &&
                    bore.getCombatDuration() < 120;
        }
    }

    private static class BoreAvoidProjectileGoal extends Goal {
        private final BoreEntity bore;
        private Vec3 avoidanceDirection;

        public BoreAvoidProjectileGoal(BoreEntity bore) {
            this.bore = bore;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (bore.getBoreState() != BoreState.IDLE) return false;

            avoidanceDirection = bore.getProjectileAvoidanceDirection();
            return avoidanceDirection != null;
        }

        @Override
        public void tick() {
            if (avoidanceDirection != null) {
                Vec3 targetPos = bore.position().add(avoidanceDirection.scale(6.0));
                bore.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 1.5);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return (bore.isNearbyProjectileDangerous() || bore.isNearFriendlyProjectile()) &&
                    bore.getBoreState() == BoreState.IDLE;
        }
    }

    private static class BoreSmartPositioningGoal extends Goal {
        private final BoreEntity bore;
        private Vec3 optimalPosition;

        public BoreSmartPositioningGoal(BoreEntity bore) {
            this.bore = bore;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = bore.getTarget();
            if (target == null || target.isRemoved() || !target.isAlive() || bore.getBoreState() != BoreState.IDLE) return false;

            try {
                double distance = bore.distanceTo(target);

                if (distance < 6.0 || distance > 12.0) {
                    optimalPosition = findOptimalPosition(target);
                    return optimalPosition != null;
                }
            } catch (Exception e) {
                return false;
            }

            return false;
        }

        private Vec3 findOptimalPosition(LivingEntity target) {
            if (target == null || target.isRemoved() || !target.isAlive()) return null;

            try {
                Vec3 targetPos = target.position();
                if (targetPos == null) return null;

                double optimalDistance = 8.0;

                for (int attempts = 0; attempts < 8; attempts++) {
                    double angle = (Math.PI * 2 * attempts) / 8.0;
                    double x = targetPos.x + Math.cos(angle) * optimalDistance;
                    double z = targetPos.z + Math.sin(angle) * optimalDistance;

                    BlockPos testPos = new BlockPos((int)x, (int)targetPos.y, (int)z);

                    if (bore.isPositionSafe(testPos)) {
                        return new Vec3(x, targetPos.y, z);
                    }
                }
            } catch (Exception e) {
            }

            return null;
        }

        @Override
        public void tick() {
            if (optimalPosition != null) {
                bore.getNavigation().moveTo(optimalPosition.x, optimalPosition.y, optimalPosition.z, 1.0);
            }
        }

        @Override
        public boolean canContinueToUse() {
            return optimalPosition != null &&
                    bore.distanceToSqr(optimalPosition) > 4.0 &&
                    bore.getBoreState() == BoreState.IDLE;
        }
    }

    private static class BoreRelocateGoal extends Goal {
        private final BoreEntity bore;

        public BoreRelocateGoal(BoreEntity bore) {
            this.bore = bore;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return bore.getRelocationTarget() != null &&
                    bore.getBoreState() == BoreState.IDLE;
        }

        @Override
        public boolean canContinueToUse() {
            BlockPos target = bore.getRelocationTarget();
            return target != null && bore.distanceToSqr(target.getX(), target.getY(), target.getZ()) > 4.0;
        }

        @Override
        public void tick() {
            BlockPos target = bore.getRelocationTarget();
            if (target != null) {
                bore.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 1.0);
            }
        }

        @Override
        public void stop() {
            bore.clearRelocationTarget();
        }
    }

    private static class BoreFleeGoal extends AvoidEntityGoal<Player> {
        private final BoreEntity bore;

        public BoreFleeGoal(BoreEntity bore) {
            super(bore, Player.class, 6.0F, 1.2, 1.5);
            this.bore = bore;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = bore.getTarget();
            return super.canUse() &&
                    bore.getBoreState() != BoreState.SHOOTING &&
                    bore.getBoreState() != BoreState.BURROWING &&
                    bore.getBoreState() != BoreState.UNBURROWING &&
                    target != null &&
                    target.isAlive() &&
                    !target.isRemoved() &&
                    bore.distanceTo(target) < 4.0 &&
                    (bore.burrowCooldown > 0 || bore.burrowCooldownTimer > 0);
        }
    }

    private static class BoreShootGoal extends Goal {
        private final BoreEntity bore;
        private int aimTimer = 0;

        public BoreShootGoal(BoreEntity bore) {
            this.bore = bore;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = bore.getTarget();
            return target != null &&
                    bore.shootCooldown <= 0 &&
                    bore.canShoot() &&
                    bore.distanceTo(target) >= 4.0 &&
                    bore.distanceTo(target) <= 16.0f &&
                    bore.getBoreState() == BoreState.IDLE &&
                    bore.getCombatDuration() >= 40;
        }

        @Override
        public void start() {
            aimTimer = 15;
        }

        @Override
        public void tick() {
            LivingEntity target = bore.getTarget();
            if (target != null && target.isAlive() && !target.isRemoved()) {
                try {
                    bore.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());

                    if (--aimTimer <= 0) {
                        bore.tryShootAtPlayer();
                        aimTimer = 60;
                    }
                } catch (Exception e) {
                    aimTimer = 60;
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = bore.getTarget();
            return target != null &&
                    bore.getBoreState() != BoreState.SHOOTING &&
                    bore.distanceTo(target) >= 4.0 &&
                    bore.distanceTo(target) <= 16.0f;
        }

        @Override
        public void stop() {
            aimTimer = 0;
        }
    }

    private static final EntityDataAccessor<Integer> DATA_ID_STATE =
            SynchedEntityData.defineId(BoreEntity.class, EntityDataSerializers.INT);

    private void syncStateToClients() {
        if (!this.level().isClientSide()) {
            this.entityData.set(DATA_ID_STATE, this.boreState.ordinal());
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (DATA_ID_STATE.equals(data) && this.level().isClientSide()) {
            BoreState newState = BoreState.values()[this.entityData.get(DATA_ID_STATE)];
            if (this.boreState != newState && !isChangingState) {
                isChangingState = true;

                this.previousState = this.boreState;
                this.boreState = newState;
                this.animationTick = 0;
                this.stateTimer = 0;

                startStateAnimation(newState);

                isChangingState = false;
            }
        }
        super.onSyncedDataUpdated(data);
    }

    private void addBurrowParticles(AnimationState animationState) {
        if (this.level().isClientSide() && animationState.isStarted()) {
            BlockState blockState = this.getBlockStateOn();
            if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
                RandomSource random = this.getRandom();
                for (int i = 0; i < 7; ++i) {
                    double d = this.getX() + (double)Mth.randomBetween(random, -0.3F, 0.3F);
                    double e = this.getY();
                    double f = this.getZ() + (double)Mth.randomBetween(random, -0.3F, 0.3F);
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}