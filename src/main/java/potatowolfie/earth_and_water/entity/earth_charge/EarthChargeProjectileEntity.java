package potatowolfie.earth_and_water.entity.earth_charge;

import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import potatowolfie.earth_and_water.damage.ModDamageTypes;
import potatowolfie.earth_and_water.entity.ModEntities;
import potatowolfie.earth_and_water.item.ModItems;
import potatowolfie.earth_and_water.util.ExplosionUtil;

public class EarthChargeProjectileEntity extends AbstractArrow {
    private float rotation;
    public float groundedYawOffset;
    public float groundedPitchOffset;

    private static final float DAMAGE_RADIUS = 1.5F;
    private static final float BASE_DAMAGE = 14.0F;
    private static final float KNOCKBACK_MULTIPLIER = 0.2F;

    public EarthChargeProjectileEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
        this.setNoGravity(false);
    }

    public EarthChargeProjectileEntity(Level world, Player player) {
        super(ModEntities.EARTH_CHARGE, player, world, new ItemStack(ModItems.EARTH_CHARGE), null);
        this.setPos(player.getX(), player.getEyeY() - 0.3, player.getZ());
        this.setXRot(0);
        this.setYRot(player.getYRot());

        Vec3 velocity = player.getLookAngle().scale(1.0);
        this.lerpMotion(velocity);

        this.setNoGravity(false);
    }

    public EarthChargeProjectileEntity(Level world, double x, double y, double z, Vec3 vec3d) {
        super(ModEntities.EARTH_CHARGE, world);
        this.setPos(x, y - 0.2, z);
        if (vec3d != null) {
            this.setDeltaMovement(vec3d);
        }
        this.setNoGravity(false);
    }

    public EarthChargeProjectileEntity(Level world, LivingEntity owner) {
        super(ModEntities.EARTH_CHARGE, world);
        this.shootFromRotation(owner, owner.getXRot(), owner.getYRot(), 0.0f, 1.0f, 1.0f);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.EARTH_CHARGE);
    }

    public float getRenderingRotation() {
        return 0.0f;
    }

    public boolean isGrounded() {
        return this.isInGround();
    }

    private void applyDirectHitDamage(Entity hitEntity) {
        if (hitEntity instanceof LivingEntity livingEntity && !this.level().isClientSide()) {
            if (this.level() instanceof ServerLevel serverWorld) {
                DamageSource earthChargeDamage = new DamageSource(
                        serverWorld.registryAccess()
                                .lookupOrThrow(Registries.DAMAGE_TYPE)
                                .get(ModDamageTypes.EARTH_CHARGE.identifier()).get(),
                        this,
                        this.getOwner()
                );
                livingEntity.hurtServer(serverWorld, earthChargeDamage, 12.0f);
            }

            Vec3 knockbackDir = hitEntity.position().subtract(this.position()).normalize();
            if (knockbackDir.lengthSqr() < 0.001) {
                knockbackDir = new Vec3(0, 1, 0);
            }
            double directKnockback = KNOCKBACK_MULTIPLIER * 3.5;
            hitEntity.push(
                    knockbackDir.x * directKnockback,
                    knockbackDir.y * directKnockback + 0.2,
                    knockbackDir.z * directKnockback
            );
            hitEntity.needsSync = true;
        }
    }

    private void applyAreaDamageExcluding(Entity excludedEntity) {
        Level world = this.level();
        Vec3 pos = this.position();

        if (!world.isClientSide()) {
            if (world instanceof ServerLevel serverWorld) {
                DamageSource earthChargeDamage = new DamageSource(
                        serverWorld.registryAccess()
                                .lookupOrThrow(Registries.DAMAGE_TYPE)
                                .get(ModDamageTypes.EARTH_CHARGE.identifier()).get(),
                        this,
                        this.getOwner()
                );

                ExplosionUtil.createSilentExplosion(
                        serverWorld,
                        pos,
                        1.5f,
                        this,
                        excludedEntity,
                        earthChargeDamage,
                        26.0f,
                        0.4f
                );
            }
        }
    }

    private void createExplosionEffects() {
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
                    null,
                    waterChargeDamage,
                    13.0f,
                    2.0f
            );

            for (int i = 0; i < 6; i++) {
                double angle = i * Math.PI / 3;
                double distance = 0.2;
                double offsetX = Math.cos(angle) * distance;
                double offsetZ = Math.sin(angle) * distance;
                serverWorld.sendParticles(
                        ParticleTypes.DUST_PLUME,
                        pos.x + offsetX, pos.y + 0.03, pos.z + offsetZ,
                        5,
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
                        ParticleTypes.DUST_PLUME,
                        pos.x + offsetX * 0.3, pos.y + 0.05, pos.z + offsetZ * 0.3,
                        3,
                        0.03, 0.02, 0.03,
                        0.3
                );
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity hitEntity = entityHitResult.getEntity();
        applyDirectHitDamage(hitEntity);
        applyAreaDamageExcluding(hitEntity);

        if (!this.level().isClientSide()) {
            createExplosionEffects();
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        switch (result.getDirection()) {
            case SOUTH -> {
                groundedYawOffset = 215f;
                groundedPitchOffset = 180f;
            }
            case NORTH -> {
                groundedYawOffset = 215f;
                groundedPitchOffset = 0f;
            }
            case EAST -> {
                groundedYawOffset = 215f;
                groundedPitchOffset = -90f;
            }
            case WEST -> {
                groundedYawOffset = 215f;
                groundedPitchOffset = 90f;
            }
            case DOWN -> {
                groundedYawOffset = 115f;
                groundedPitchOffset = 180f;
            }
            case UP -> {
                groundedYawOffset = 285f;
                groundedPitchOffset = 180f;
            }
        }

        applyAreaDamageExcluding(null);

        if (!this.level().isClientSide()) {
            createExplosionEffects();
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide() && !this.isRemoved()) {
            if (this.isInWater()) {
                Vec3 currentVelocity = this.getDeltaMovement();
                this.setDeltaMovement(currentVelocity.scale(0.9));
            }
        }

        super.tick();

        if (!this.isInGround()) {
            Vec3 velocity = this.getDeltaMovement();
            double length = velocity.length();
            if (length < 0.5 && (Math.abs(velocity.x) > 0.01 || Math.abs(velocity.z) > 0.01)) {
                double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
                if (horizontalSpeed < 0.5) {
                    double factor = 0.5 / horizontalSpeed;
                    this.setDeltaMovement(
                            velocity.x * factor,
                            velocity.y,
                            velocity.z * factor
                    );
                }
            }
        }

        if (this.level().isClientSide() && !this.isInGround()) {
            this.level().addParticle(
                    ParticleTypes.SMOKE,
                    this.getX(), this.getY(), this.getZ(),
                    0, 0, 0);
        }
    }

    @Override
    public boolean isInWater() {
        return this.level().isWaterAt(this.blockPosition());
    }

    @Override
    protected float getWaterInertia() {
        return 0.8F;
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05F;
    }
}