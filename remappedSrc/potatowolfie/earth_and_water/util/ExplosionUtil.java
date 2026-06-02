package potatowolfie.earth_and_water.util;

import net.minecraft.entity.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExplosionUtil {

    public static void createSilentExplosion(ServerLevel world, Vec3 pos, float power, @Nullable Entity sourceEntity) {
        createSilentExplosion(world, pos, power, sourceEntity, null, null, -1, -1);
    }

    public static void createSilentExplosion(ServerLevel world, Vec3 pos, float power, @Nullable Entity sourceEntity, @Nullable Entity directHit) {
        createSilentExplosion(world, pos, power, sourceEntity, directHit, null, -1, -1);
    }

    public static void createSilentExplosion(ServerLevel world, Vec3 pos, float power, @Nullable Entity sourceEntity, @Nullable Entity directHit, float customDamage, float knockbackMultiplier) {
        createSilentExplosion(world, pos, power, sourceEntity, directHit, null, customDamage, knockbackMultiplier);
    }

    public static void createSilentExplosion(ServerLevel world, Vec3 pos, float power, @Nullable Entity sourceEntity, @Nullable Entity directHit, @Nullable DamageSource customDamageSource, float customDamage, float knockbackMultiplier) {
        world.gameEvent(sourceEntity, GameEvent.EXPLODE, pos);

        float radius = power * 2.0F;
        int minX = Mth.floor(pos.x - radius - 1.0);
        int maxX = Mth.floor(pos.x + radius + 1.0);
        int minY = Mth.floor(pos.y - radius - 1.0);
        int maxY = Mth.floor(pos.y + radius + 1.0);
        int minZ = Mth.floor(pos.z - radius - 1.0);
        int maxZ = Mth.floor(pos.z + radius + 1.0);

        List<Entity> entities = world.getEntities(
                sourceEntity,
                new AABB(minX, minY, minZ, maxX, maxY, maxZ)
        );

        Map<Player, Vec3> affectedPlayers = new HashMap<>();
        DamageSource damageSource = customDamageSource != null ? customDamageSource : world.damageSources().explosion(sourceEntity, getCausingEntity(sourceEntity));

        for (Entity entity : entities) {
            if (entity == directHit || shouldSkipEntity(entity)) {
                continue;
            }

            Vec3 entityCenter = getEntityCenter(entity);
            double distance = entityCenter.distanceTo(pos);
            double normalizedDistance = distance / radius;

            if (normalizedDistance <= 1.0) {
                Vec3 direction = entityCenter.subtract(pos);
                double totalDistance = direction.length();

                if (totalDistance > 0.0) {
                    direction = direction.normalize();

                    float damage;
                    if (customDamage > 0) {
                        damage = customDamage * (float)(1.0 - normalizedDistance);
                    } else {
                        damage = calculateImprovedDamage(power, normalizedDistance);
                    }

                    if (damage > 0.1f) {
                        entity.hurtServer(world, damageSource, damage);
                    }

                    double exposure = getExposure(pos, entity);
                    double knockbackStrength = (1.0 - normalizedDistance) * exposure;

                    if (knockbackMultiplier > 0) {
                        knockbackStrength *= knockbackMultiplier;
                    }

                    if (entity instanceof LivingEntity livingEntity) {
                        knockbackStrength *= (1.0 - livingEntity.getAttributeValue(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE));
                    }

                    Vec3 knockback = direction.scale(knockbackStrength);
                    entity.setDeltaMovement(entity.getDeltaMovement().add(knockback));

                    if (entity instanceof Player player) {
                        if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                            affectedPlayers.put(player, knockback);
                        }
                    }

                    entity.onExplosionHit(sourceEntity);
                }
            }
        }

        for (Map.Entry<Player, Vec3> entry : affectedPlayers.entrySet()) {
            Player player = entry.getKey();
            player.needsSync = true;
        }
    }

    private static boolean shouldSkipEntity(Entity entity) {
        if (entity instanceof ItemEntity) {
            return true;
        }
        if (entity instanceof ExperienceOrb) {
            return true;
        }

        return false;
    }

    private static Vec3 getEntityCenter(Entity entity) {
        AABB box = entity.getBoundingBox();
        return new Vec3(
                (box.minX + box.maxX) * 0.5,
                (box.minY + box.maxY) * 0.5,
                (box.minZ + box.maxZ) * 0.5
        );
    }

    private static float calculateImprovedDamage(float power, double normalizedDistance) {
        float maxDamage = power * 7.0F;
        return (float) (maxDamage * (1.0 - normalizedDistance));
    }

    private static float calculateDamage(float power, double distance) {
        float maxDamage = (power * 2.0F + 1.0F) * 8.0F;
        return (float) (maxDamage * (1.0 - distance));
    }

    private static float getExposure(Vec3 source, Entity entity) {
        AABB box = entity.getBoundingBox();
        double stepX = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
        double stepY = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
        double stepZ = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
        double offsetX = (1.0 - Math.floor(1.0 / stepX) * stepX) / 2.0;
        double offsetZ = (1.0 - Math.floor(1.0 / stepZ) * stepZ) / 2.0;

        if (stepX < 0.0 || stepY < 0.0 || stepZ < 0.0) {
            return 0.0F;
        }

        int visiblePoints = 0;
        int totalPoints = 0;

        for (double x = 0.0; x <= 1.0; x += stepX) {
            for (double y = 0.0; y <= 1.0; y += stepY) {
                for (double z = 0.0; z <= 1.0; z += stepZ) {
                    double pointX = Mth.lerp(x, box.minX, box.maxX);
                    double pointY = Mth.lerp(y, box.minY, box.maxY);
                    double pointZ = Mth.lerp(z, box.minZ, box.maxZ);
                    Vec3 point = new Vec3(pointX + offsetX, pointY, pointZ + offsetZ);

                    if (entity.level().clip(new ClipContext(
                            point, source,
                            ClipContext.Block.COLLIDER,
                            ClipContext.Fluid.NONE,
                            entity
                    )).getType() == HitResult.Type.MISS) {
                        visiblePoints++;
                    }
                    totalPoints++;
                }
            }
        }

        return (float) visiblePoints / (float) totalPoints;
    }

    @Nullable
    private static LivingEntity getCausingEntity(@Nullable Entity from) {
        if (from == null) {
            return null;
        } else if (from instanceof PrimedTnt tntEntity) {
            return tntEntity.getOwner();
        } else if (from instanceof LivingEntity livingEntity) {
            return livingEntity;
        } else if (from instanceof Projectile projectileEntity) {
            Entity owner = projectileEntity.getOwner();
            if (owner instanceof LivingEntity livingEntity) {
                return livingEntity;
            }
        }
        return null;
    }

    public static void createSilentExplosion(ServerLevel world, Vec3 pos, float radius, float damage, double areaKnockback, double directHitKnockback, @Nullable Entity directHit) {
        createSilentExplosion(world, pos, radius, null, directHit, null, damage, (float)areaKnockback);
    }
}