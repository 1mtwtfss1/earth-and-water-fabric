package potatowolfie.earth_and_water.entity.water_charge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import potatowolfie.earth_and_water.damage.ModDamageTypes;
import potatowolfie.earth_and_water.effect.ModEffects;
import potatowolfie.earth_and_water.entity.ModEntities;
import potatowolfie.earth_and_water.item.ModItems;

import java.util.List;
import potatowolfie.earth_and_water.util.ExplosionUtil;

public class WaterChargeProjectileEntity extends AbstractArrow {
    boolean isStuck = false;
    private Entity attachedEntity = null;
    private BlockPos attachedBlock = null;
    private Direction attachedFace = null;
    private Vec3 exactHitPosition = null;

    private int stuckTicks = -1;
    private static final int TICKS_TO_EXPLODE = 40;

    private float initialEntityYaw = 0;

    private Vec3 initialDirection = null;

    private static final float DIRECT_DAMAGE = 4.5F;
    private static final float INDIRECT_KNOCKBACK_RADIUS = 5.0F;
    private static final float KNOCKBACK_STRENGTH = 1.1F;
    private static final int BUBBLE_EFFECT_DURATION = 60;
    private static final float WATER_BREATHING_DURATION = 2.5F;

    private static final float EXPLOSION_DAMAGE_FACTOR = 1.0F;
    private static final float MAX_EXPLOSION_DAMAGE = 6.0F;
    private static final float EXPLOSION_KNOCKBACK_MULTIPLIER = 1.2F;

    private int bubbleEffectTimer = 0;
    private boolean isPerformingBubbleEffect = false;
    private boolean isDirectHit = false;

    public WaterChargeProjectileEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
        this.setNoPhysics(false);
        this.setBaseDamage(0);
        this.setNoGravity(false);
    }

    public WaterChargeProjectileEntity(Level world, Player player) {
        super(ModEntities.WATER_CHARGE, player, world, new ItemStack(ModItems.WATER_CHARGE), null);

        this.setPos(player.getX(), player.getEyeY() - 0.3, player.getZ());

        this.setXRot(player.getXRot());
        this.setYRot(player.getYRot());

        float pitch = player.getXRot() * 0.017453292F;
        float yaw = player.getYRot() * 0.017453292F;
        float x = -Mth.sin(yaw) * Mth.cos(pitch);
        float y = -Mth.sin(pitch);
        float z = Mth.cos(yaw) * Mth.cos(pitch);
        initialDirection = new Vec3(x, y, z).normalize();

        float speed = 1.5F;
        this.setDeltaMovement(x * speed, y * speed, z * speed);

        this.setNoPhysics(false);
        this.setBaseDamage(0);
        this.setNoGravity(false);
    }

    public WaterChargeProjectileEntity(Level world, double x, double y, double z, Vec3 vec3d) {
        super(ModEntities.WATER_CHARGE, world);
        this.setPos(x, y - 0.2, z);
        if (vec3d != null) {
            this.setDeltaMovement(vec3d);
            initialDirection = vec3d.normalize();
        }
        this.setNoPhysics(false);
        this.setBaseDamage(0);
        this.setNoGravity(false);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.WATER_CHARGE);
    }

    public float getRenderingRotation() {
        if (!isStuck && initialDirection != null) {
            return (float) Math.toDegrees(Math.atan2(initialDirection.x, initialDirection.z));
        }
        return 0.0f;
    }

    public boolean isGrounded() {
        return this.isInGround();
    }

    private boolean isReallyInWater() {
        BlockPos pos = this.blockPosition();
        Level world = this.level();

        if (world.isWaterAt(pos)) {
            return true;
        }

        for (Direction dir : Direction.values()) {
            if (world.isWaterAt(pos.relative(dir))) {
                return true;
            }
        }

        return false;
    }

    private void restoreOxygen(LivingEntity entity) {
        if (!level().isClientSide() && entity instanceof Player) {
            ServerLevel serverWorld = (ServerLevel) level();
            serverWorld.sendParticles(
                    ParticleTypes.BUBBLE,
                    entity.getX(),
                    entity.getY() + entity.getBbHeight() * 0.5,
                    entity.getZ(),
                    10,
                    0.3, 0.3, 0.3,
                    0.1
            );
        }
    }

    private void createWaterExplosionEffects(@Nullable Entity excludedEntity) {
        Level world = this.level();
        Vec3 pos = this.position();

        world.playSound(null, pos.x, pos.y, pos.z,
                SoundEvents.POINTED_DRIPSTONE_LAND,
                SoundSource.BLOCKS, 1.0F, 0.8F);

        if (!world.isClientSide() && world instanceof ServerLevel serverWorld) {
            DamageSource waterChargeDamage = new DamageSource(
                    serverWorld.registryAccess()
                            .lookupOrThrow(Registries.DAMAGE_TYPE)
                            .get(ModDamageTypes.WATER_CHARGE.identifier()).get(),
                    this,
                    this.getOwner()
            );

            ExplosionUtil.createSilentExplosion(
                    serverWorld,
                    this.position(),
                    3.0f,
                    this,
                    excludedEntity,
                    waterChargeDamage,
                    6.0f,
                    3.0f
            );

            serverWorld.sendParticles(
                    ParticleTypes.BUBBLE,
                    pos.x, pos.y + 0.05, pos.z,
                    20,
                    0.2, 0.05, 0.2,
                    0.1
            );

            for (int i = 0; i < 6; i++) {
                double angle = i * Math.PI / 3;
                double distance = 0.2;
                double offsetX = Math.cos(angle) * distance;
                double offsetZ = Math.sin(angle) * distance;
                serverWorld.sendParticles(
                        ParticleTypes.BUBBLE,
                        pos.x + offsetX, pos.y + 0.03, pos.z + offsetZ,
                        10,
                        0.05, 0.02, 0.05,
                        0.15
                );
            }

            for (int i = 0; i < 6; i++) {
                double angle = world.getRandom().nextDouble() * Math.PI * 2;
                double distance = 0.1 + world.getRandom().nextDouble() * 0.25;
                double offsetX = Math.cos(angle) * distance;
                double offsetZ = Math.sin(angle) * distance;
                serverWorld.sendParticles(
                        ParticleTypes.BUBBLE,
                        pos.x + offsetX * 0.3, pos.y + 0.05, pos.z + offsetZ * 0.3,
                        7,
                        0.03, 0.02, 0.03,
                        0.3
                );
            }

            if (world.isWaterAt(this.blockPosition())) {
                serverWorld.sendParticles(
                        ParticleTypes.BUBBLE_COLUMN_UP,
                        pos.x, pos.y, pos.z,
                        17, 0.7, 0.7, 0.7, 0.2
                );

                serverWorld.sendParticles(
                        ParticleTypes.BUBBLE_POP,
                        pos.x, pos.y + 0.5, pos.z,
                        10, 1.2, 0.8, 1.2, 0.05
                );

                world.getServer().execute(() -> {
                    serverWorld.sendParticles(
                            ParticleTypes.BUBBLE,
                            pos.x, pos.y, pos.z,
                            15, 2.0, 1.2, 2.0, 0.05
                    );
                });
            }

            float effectRadius = INDIRECT_KNOCKBACK_RADIUS * 1.2f;

            AABB affectBox = new AABB(
                    pos.x - effectRadius - 0.5,
                    pos.y - effectRadius - 0.5,
                    pos.z - effectRadius - 0.5,
                    pos.x + effectRadius + 0.5,
                    pos.y + effectRadius + 0.5,
                    pos.z + effectRadius + 0.5
            );

            List<Entity> nearbyEntities = world.getEntitiesOfClass(Entity.class, affectBox, entity ->
                    entity != this && entity != this.getOwner() && entity != excludedEntity);

            for (Entity entity : nearbyEntities) {
                double distance = entity.position().distanceTo(pos);

                if (distance <= effectRadius) {
                    float distanceFactor = (float)(1.0 - distance / effectRadius);
                    float explosionFactor = (float)Math.pow(distanceFactor, 2.5) * EXPLOSION_KNOCKBACK_MULTIPLIER;

                    Vec3 knockbackDir = entity.position().subtract(pos).normalize();
                    float knockbackStrength = KNOCKBACK_STRENGTH * explosionFactor;

                    double upwardForce = 0.2 + (0.5 * explosionFactor);

                    entity.push(
                            knockbackDir.x * knockbackStrength,
                            knockbackDir.y * knockbackStrength + upwardForce,
                            knockbackDir.z * knockbackStrength
                    );
                    entity.needsSync = true;

                    if (entity instanceof LivingEntity livingEntity) {
                        float damage = distanceFactor * EXPLOSION_DAMAGE_FACTOR * MAX_EXPLOSION_DAMAGE;
                        if (damage > 0.5f && world instanceof ServerLevel) {
                            new DamageSource(
                                    serverWorld.registryAccess()
                                            .lookupOrThrow(Registries.DAMAGE_TYPE)
                                            .get(ModDamageTypes.WATER_CHARGE.identifier()).get(),
                                    this,
                                    this.getOwner()
                            );
                            livingEntity.hurtServer(serverWorld, waterChargeDamage, damage);
                            restoreOxygen(livingEntity);
                        }
                    }
                }
            }
        }

        isPerformingBubbleEffect = true;
        bubbleEffectTimer = BUBBLE_EFFECT_DURATION;
    }

    private void createWaterShockwave() {
        createWaterExplosionEffects(null);
    }

    private void createDirectHitEffect(Entity hitEntity) {
        Level world = this.level();
        Vec3 pos = hitEntity.position();

        world.playSound(null, pos.x, pos.y, pos.z,
                SoundEvents.GILDED_BLACKSTONE_BREAK,
                SoundSource.NEUTRAL, 0.8F, 1.2F);
        world.playSound(null, pos.x, pos.y, pos.z,
                SoundEvents.GENERIC_SPLASH,
                SoundSource.BLOCKS, 1.0F, 1.2F);

        if (hitEntity instanceof LivingEntity livingEntity) {
            float damage = DIRECT_DAMAGE;
            if (world instanceof ServerLevel serverWorld) {
                DamageSource waterChargeDamage = new DamageSource(
                        serverWorld.registryAccess()
                                .lookupOrThrow(Registries.DAMAGE_TYPE)
                                .get(ModDamageTypes.WATER_CHARGE.identifier()).get(),
                        this,
                        this.getOwner()
                );
                livingEntity.hurtServer(serverWorld, waterChargeDamage, damage);
            }

            int durationTicks = (int)(WATER_BREATHING_DURATION * 20);
            MobEffectInstance breathEffect = new MobEffectInstance(ModEffects.BREATH_GIVING, durationTicks, 1);
            livingEntity.addEffect(breathEffect);

            restoreOxygen(livingEntity);
        }

        if (!world.isClientSide() && world instanceof ServerLevel serverWorld) {
            float entityWidth = hitEntity.getBbWidth();
            float entityHeight = hitEntity.getBbHeight();
            float entitySize = (entityWidth + entityHeight) / 2.0f;

            Vec3 explosionPos = exactHitPosition != null ? exactHitPosition : this.position();

            serverWorld.sendParticles(
                    ParticleTypes.BUBBLE,
                    explosionPos.x, explosionPos.y + 0.05, explosionPos.z,
                    (int)(15 * Math.max(1.0f, entitySize)),
                    0.2, 0.05, 0.2,
                    0.1
            );

            for (int i = 0; i < 6; i++) {
                double angle = i * Math.PI / 3;
                double distance = 0.2 * Math.max(1.0f, entitySize);
                double offsetX = Math.cos(angle) * distance;
                double offsetZ = Math.sin(angle) * distance;
                serverWorld.sendParticles(
                        ParticleTypes.BUBBLE,
                        explosionPos.x + offsetX, explosionPos.y + 0.03, explosionPos.z + offsetZ,
                        7,
                        0.05, 0.02, 0.05,
                        0.15
                );
            }
        }

        isPerformingBubbleEffect = true;
        isDirectHit = true;
        bubbleEffectTimer = BUBBLE_EFFECT_DURATION;
        attachedEntity = hitEntity;
    }

    private void spawnBubbleParticles() {
        Level world = this.level();

        if (!world.isClientSide()) {
            return;
        }

        if (isDirectHit && attachedEntity != null) {
            Vec3 entityPos = attachedEntity.position();
            float entityHeight = attachedEntity.getBbHeight();
            float entityWidth = attachedEntity.getBbWidth();

            int spiralLayers = 3;
            float timeMultiplier = 0.1f;

            for (int layer = 0; layer < spiralLayers; layer++) {
                double spiralRadius = entityWidth * (1.2 + layer * 0.4);
                double baseHeight = layer * entityHeight / (spiralLayers + 1);
                float rotationDirection = (layer % 2 == 0) ? 1.0f : -1.0f;
                double baseAngle = this.tickCount * timeMultiplier * rotationDirection;
                int spiralsPerLayer = 2;
                for (int spiral = 0; spiral < spiralsPerLayer; spiral++) {
                    double spiralOffset = spiral * (Math.PI * 2.0 / spiralsPerLayer);

                    int particlesPerSpiral = 10;
                    for (int i = 0; i < particlesPerSpiral; i++) {
                        double progress = (double)i / particlesPerSpiral;
                        double angle = baseAngle + spiralOffset + progress * Math.PI * 2.0;
                        double posX = entityPos.x + Math.sin(angle) * spiralRadius;
                        double posY = entityPos.y + baseHeight + progress * entityHeight * 0.8;
                        double posZ = entityPos.z + Math.cos(angle) * spiralRadius;
                        double jitter = 0.05;
                        posX += (this.random.nextDouble() - 0.5) * jitter;
                        posY += (this.random.nextDouble() - 0.5) * jitter;
                        posZ += (this.random.nextDouble() - 0.5) * jitter;
                        double velX = Math.cos(angle) * 0.1 * rotationDirection;
                        double velY = 0.05 + (layer * 0.02);
                        double velZ = -Math.sin(angle) * 0.1 * rotationDirection;

                        if (i % 2 == 0) {
                            world.addParticle(
                                    ParticleTypes.BUBBLE,
                                    posX, posY, posZ,
                                    velX, velY, velZ);
                        }

                        if (this.random.nextInt(15) == 0) {
                            world.addParticle(
                                    ParticleTypes.BUBBLE_POP,
                                    posX, posY + 0.2, posZ,
                                    velX * 1.5, velY * 1.5, velZ * 1.5);
                        }
                    }
                }
            }
        } else {
            Vec3 pos = exactHitPosition != null ? exactHitPosition : this.position();
            float explosionProgress = 1.0f - ((float)bubbleEffectTimer / BUBBLE_EFFECT_DURATION);

            if (bubbleEffectTimer > BUBBLE_EFFECT_DURATION * 0.3) {
                for (int i = 0; i < 2; i++) {
                    double randX = (this.random.nextDouble() - 0.5) * explosionProgress * 6.0;
                    double randY = (this.random.nextDouble() - 0.5) * explosionProgress * 6.0;
                    double randZ = (this.random.nextDouble() - 0.5) * explosionProgress * 6.0;

                    if (this.random.nextInt(2) == 0) {
                        world.addParticle(
                                ParticleTypes.BUBBLE_POP,
                                pos.x + randX * 0.5,
                                pos.y + randY * 0.5,
                                pos.z + randZ * 0.5,
                                randX * 0.1, 0.3, randZ * 0.1
                        );
                    }
                }
            }

            int spiralLayers = 2;
            double maxRadius = 1.0 + (double)(BUBBLE_EFFECT_DURATION - bubbleEffectTimer)
                    / BUBBLE_EFFECT_DURATION * INDIRECT_KNOCKBACK_RADIUS * 0.4;

            for (int layer = 0; layer < spiralLayers; layer++) {
                double layerRadius = maxRadius * (0.4 + 0.6 * ((double)layer / spiralLayers));
                double baseHeight = -1.0 + layer * 0.7;
                double heightRange = 2.0 + layer * 0.3;

                double baseAngle = this.tickCount * (0.05 + layer * 0.02) * (layer % 2 == 0 ? 1 : -1);

                int spiralsPerLayer = 1 + layer;
                for (int spiral = 0; spiral < spiralsPerLayer; spiral++) {
                    double spiralOffset = spiral * (Math.PI * 2.0 / spiralsPerLayer);

                    int pointsPerSpiral = 7;
                    for (int i = 0; i < pointsPerSpiral; i++) {
                        double progress = (double)i / pointsPerSpiral;

                        double angle = baseAngle + spiralOffset + progress * Math.PI * 4.0;

                        double currentRadius = layerRadius * (0.3 + 0.7 * progress);

                        double offsetX = Math.sin(angle) * currentRadius;
                        double offsetY = baseHeight + progress * heightRange;
                        double offsetZ = Math.cos(angle) * currentRadius;

                        double spiralTightness = 0.12;
                        double upwardSpeed = 0.20;
                        double velX = Math.cos(angle) * spiralTightness * (layer % 2 == 0 ? -1 : 1);
                        double velY = upwardSpeed;
                        double velZ = -Math.sin(angle) * spiralTightness * (layer % 2 == 0 ? -1 : 1);

                        double rand = 0.03 * (layer + 1);
                        double randX = (this.random.nextDouble() - 0.5) * rand;
                        double randY = (this.random.nextDouble() - 0.5) * rand;
                        double randZ = (this.random.nextDouble() - 0.5) * rand;

                        if (i % 2 == 0) {
                            world.addParticle(
                                    ParticleTypes.BUBBLE,
                                    pos.x + offsetX + randX,
                                    pos.y + offsetY + randY,
                                    pos.z + offsetZ + randZ,
                                    velX, velY, velZ);
                        }

                        if (this.random.nextInt(20) == 0) {
                            world.addParticle(
                                    ParticleTypes.BUBBLE_POP,
                                    pos.x + offsetX + randX,
                                    pos.y + offsetY + randY + 0.2,
                                    pos.z + offsetZ + randZ,
                                    velX * 1.5, velY * 1.5, velZ * 1.5);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();

        if (entity instanceof ItemEntity || !entity.isAlive()) {
            return;
        }

        if (!isReallyInWater()) {
            spawnAsItem();
            return;
        }

        this.exactHitPosition = entityHitResult.getLocation();

        createDirectHitEffect(entity);
        createWaterExplosionEffects(entity);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        if (!isReallyInWater()) {
            spawnAsItem();
            return;
        }

        Vec3 hitPos = result.getLocation();
        Direction face = result.getDirection();
        double embedOffset = 0.05;

        Vec3 embeddedPos = hitPos.add(
                face.getStepX() * embedOffset,
                face.getStepY() * embedOffset,
                face.getStepZ() * embedOffset
        );

        this.setPosRaw(embeddedPos.x, embeddedPos.y, embeddedPos.z);
        this.setDeltaMovement(Vec3.ZERO);
        this.setNoGravity(true);

        this.setYRot(0.0F);
        this.setXRot(0.0F);
        this.setYHeadRot(0.0F);;

        this.exactHitPosition = embeddedPos;
        this.attachedBlock = result.getBlockPos();
        this.attachedFace = face;
        this.isStuck = true;
        this.stuckTicks = 0;

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.GILDED_BLACKSTONE_BREAK,
                SoundSource.NEUTRAL, 1.0F, 1.2F);
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public InteractionResult interact(Player player, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public void playerTouch(Player player) {
    }

    public AbstractArrow.Pickup getPickupType() {
        if (isStuck && isReallyInWater()) {
            return AbstractArrow.Pickup.DISALLOWED;
        } else {
            return AbstractArrow.Pickup.ALLOWED;
        }
    }

    @Override
    public void tick() {
        if (isPerformingBubbleEffect) {
            bubbleEffectTimer--;
            spawnBubbleParticles();

            if (bubbleEffectTimer <= 0) {
                isPerformingBubbleEffect = false;
                this.discard();
                return;
            }
        }

        if (isStuck && stuckTicks >= 0 && !isPerformingBubbleEffect) {
            stuckTicks++;

            if (stuckTicks > 20 && this.level().isClientSide()) {
                int particleChance = stuckTicks > 35 ? 1 : (stuckTicks > 30 ? 2 : 3);

                if (this.random.nextInt(particleChance) == 0) {
                    Vec3 pos = this.position();
                    double spreadFactor = 0.1;

                    this.level().addParticle(
                            ParticleTypes.BUBBLE,
                            pos.x + (this.random.nextDouble() - 0.5) * spreadFactor,
                            pos.y + (this.random.nextDouble() - 0.5) * spreadFactor,
                            pos.z + (this.random.nextDouble() - 0.5) * spreadFactor,
                            (this.random.nextDouble() - 0.5) * 0.03,
                            0.1 + (this.random.nextDouble() * 0.05),
                            (this.random.nextDouble() - 0.5) * 0.03
                    );
                }
            }

            if (stuckTicks >= 40) {
                this.setInvisible(true);

                createWaterShockwave();

                if (!level().isClientSide()) {
                    this.discard();
                }

                stuckTicks = -1;
            }
        }

        if (!isStuck) {
            super.tick();

            if (this.isInWater() && this.level().isClientSide()) {
                Vec3 velocity = this.getDeltaMovement();
                double speed = velocity.length();

                if (speed > 0.1 && this.tickCount % 2 == 0) {
                    Vec3 normalized = velocity.normalize();
                    Vec3 bubblePos = this.position().subtract(normalized.scale(0.3));

                    double spreadFactor = 0.05;

                    for (int i = 0; i < 2; i++) {
                        this.level().addParticle(
                                ParticleTypes.BUBBLE,
                                bubblePos.x + (this.random.nextDouble() - 0.5) * spreadFactor,
                                bubblePos.y + (this.random.nextDouble() - 0.5) * spreadFactor,
                                bubblePos.z + (this.random.nextDouble() - 0.5) * spreadFactor,
                                (this.random.nextDouble() - 0.5) * 0.02,
                                0.05,
                                (this.random.nextDouble() - 0.5) * 0.02
                        );
                    }
                }
            }

            if (this.isInWater()) {
                Vec3 currentVelocity = this.getDeltaMovement();
                this.setDeltaMovement(currentVelocity.scale(1.2));
            }

            if (initialDirection == null) {
                Vec3 velocity = this.getDeltaMovement();
                double length = velocity.length();
                if (length > 0.1) {
                    initialDirection = velocity.normalize();
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.putBoolean("IsStuck", isStuck);
        nbt.putInt("StuckTicks", stuckTicks);

        if (attachedBlock != null) {
            nbt.putInt("AttachedBlockX", attachedBlock.getX());
            nbt.putInt("AttachedBlockY", attachedBlock.getY());
            nbt.putInt("AttachedBlockZ", attachedBlock.getZ());
        }

        if (attachedFace != null) {
            nbt.putInt("AttachedFace", attachedFace.ordinal());
        }

        if (exactHitPosition != null) {
            nbt.putDouble("HitPosX", exactHitPosition.x);
            nbt.putDouble("HitPosY", exactHitPosition.y);
            nbt.putDouble("HitPosZ", exactHitPosition.z);
        }

        if (initialDirection != null) {
            nbt.putDouble("InitialDirX", initialDirection.x);
            nbt.putDouble("InitialDirY", initialDirection.y);
            nbt.putDouble("InitialDirZ", initialDirection.z);
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput nbt) {
        super.readAdditionalSaveData(nbt);

        isStuck = nbt.getBooleanOr("IsStuck", false);
        stuckTicks = nbt.getIntOr("StuckTicks", 0);

        int blockX = nbt.getIntOr("AttachedBlockX", Integer.MIN_VALUE);
        if (blockX != Integer.MIN_VALUE) {
            int blockY = nbt.getIntOr("AttachedBlockY", 0);
            int blockZ = nbt.getIntOr("AttachedBlockZ", 0);
            attachedBlock = new BlockPos(blockX, blockY, blockZ);
        }

        int faceId = nbt.getIntOr("AttachedFace", -1);
        if (faceId != -1 && faceId < Direction.values().length) {
            attachedFace = Direction.values()[faceId];
        }

        double hitX = nbt.getDoubleOr("HitPosX", Double.NaN);
        if (!Double.isNaN(hitX)) {
            double hitY = nbt.getDoubleOr("HitPosY", 0.0);
            double hitZ = nbt.getDoubleOr("HitPosZ", 0.0);
            exactHitPosition = new Vec3(hitX, hitY, hitZ);
        }

        double dirX = nbt.getDoubleOr("InitialDirX", Double.NaN);
        if (!Double.isNaN(dirX)) {
            double dirY = nbt.getDoubleOr("InitialDirY", 0.0);
            double dirZ = nbt.getDoubleOr("InitialDirZ", 0.0);
            initialDirection = new Vec3(dirX, dirY, dirZ);
        }

        this.setNoGravity(isStuck);
    }

    private void spawnAsItem() {
        if (!this.level().isClientSide()) {
            ItemEntity itemEntity = new ItemEntity(
                    this.level(),
                    this.getX(), this.getY(), this.getZ(),
                    new ItemStack(ModItems.WATER_CHARGE)
            );
            this.level().addFreshEntity(itemEntity);

            if (attachedEntity instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
                livingEntity.setArrowCount(Math.max(0, livingEntity.getArrowCount() - 1));
            }

            this.discard();
        }
    }

    public boolean isStuck() {
        return isStuck;
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public void setBaseDamage(double damage) {
        super.setBaseDamage(0.0);
    }

    public double getDamage() {
        return 0.0;
    }

    public static void registerDispenserBehavior() {
        DispenseItemBehavior behavior = new DispenseItemBehavior() {
            @Override
            public ItemStack dispense(BlockSource pointer, ItemStack stack) {
                Level world = pointer.level();
                BlockPos pos = pointer.pos();
                Direction direction = pointer.state().getValue(BlockStateProperties.FACING);

                double x = pos.getX() + 0.5 + direction.getStepX() * 0.5;
                double y = pos.getY() + 0.5 + direction.getStepY() * 0.5;
                double z = pos.getZ() + 0.5 + direction.getStepZ() * 0.5;

                float speed = 1.5f;
                Vec3 velocity = new Vec3(
                        direction.getStepX() * speed,
                        direction.getStepY() * speed,
                        direction.getStepZ() * speed
                );

                WaterChargeProjectileEntity projectile = new WaterChargeProjectileEntity(
                        world, x, y, z, velocity
                );

                world.addFreshEntity(projectile);

                stack.shrink(1);
                return stack;
            }
        };

        DispenserBlock.registerBehavior(ModItems.WATER_CHARGE, behavior);
    }

    @Override
    public void kill(ServerLevel serverWorld) {
        if (!isPerformingBubbleEffect && !level().isClientSide()) {
            Vec3 pos = this.position();

            serverWorld.sendParticles(
                    ParticleTypes.BUBBLE_COLUMN_UP,
                    pos.x, pos.y, pos.z,
                    20, 1.0, 1.0, 1.0, 0.2
            );
        }
        super.kill(serverWorld);
    }

    public boolean isStuckToEntity() {
        return isStuck && attachedEntity != null;
    }
}