package potatowolfie.earth_and_water.item.custom;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;
import potatowolfie.earth_and_water.entity.water_charge.WaterChargeProjectileEntity;

public class WaterChargeItem extends Item implements ProjectileItem {
    private static final int COOLDOWN = 100;

    public WaterChargeItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide()) {
            WaterChargeProjectileEntity waterChargeProjectileEntity = new WaterChargeProjectileEntity(world, user);

            Vec3 lookVec = user.getViewVector(1.0F);

            float speed = 2.0F;
            waterChargeProjectileEntity.setDeltaMovement(
                    lookVec.x * speed,
                    lookVec.y * speed,
                    lookVec.z * speed
            );

            waterChargeProjectileEntity.setNoGravity(false);

            world.addFreshEntity(waterChargeProjectileEntity);

            world.playSound(
                    null,
                    user.getX(),
                    user.getY(),
                    user.getZ(),
                    SoundEvents.WIND_CHARGE_THROW,
                    SoundSource.NEUTRAL,
                    0.5F,
                    0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
            );
        }

        ItemStack itemStack = user.getItemInHand(hand);
        user.getCooldowns().addCooldown(itemStack, COOLDOWN);
        user.awardStat(Stats.ITEM_USED.get(this));
        itemStack.consume(1, user);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Projectile asProjectile(Level world, Position pos, ItemStack stack, Direction direction) {

        Vec3 dirVector = new Vec3(direction.getStepX(), direction.getStepY(), direction.getStepZ());
        WaterChargeProjectileEntity waterChargeProjectileEntity = new WaterChargeProjectileEntity(
                world, pos.x(), pos.y(), pos.z(), dirVector);

        float speed = 1.5F;
        waterChargeProjectileEntity.setDeltaMovement(
                direction.getStepX() * speed,
                direction.getStepY() * speed,
                direction.getStepZ() * speed
        );

        waterChargeProjectileEntity.setNoGravity(false);

        return waterChargeProjectileEntity;
    }

    @Override
    public void shoot(Projectile entity, double x, double y, double z, float power, float uncertainty) {
        entity.shoot(x, y, z, power, 0.1F);

        entity.setNoGravity(false);
    }

    @Override
    public ProjectileItem.DispenseConfig createDispenseConfig() {
        return ProjectileItem.DispenseConfig.builder()
                .positionFunction((pointer, facing) -> DispenserBlock.getDispensePosition(pointer, 1.0, Vec3.ZERO))
                .uncertainty(0.1F)
                .power(1.5F)
                .overrideDispenseEvent(1051)
                .build();
    }
}