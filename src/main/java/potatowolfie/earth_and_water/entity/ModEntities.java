package potatowolfie.earth_and_water.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import potatowolfie.earth_and_water.EarthWater;
import potatowolfie.earth_and_water.entity.brine.BrineEntity;
import potatowolfie.earth_and_water.entity.bore.BoreEntity;
import potatowolfie.earth_and_water.entity.earth_charge.EarthChargeProjectileEntity;
import potatowolfie.earth_and_water.entity.water_charge.WaterChargeProjectileEntity;

public class ModEntities {

    public static final EntityType<EarthChargeProjectileEntity> EARTH_CHARGE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "earth_charge"),
            EntityType.Builder.<EarthChargeProjectileEntity>of(EarthChargeProjectileEntity::new, MobCategory.MISC)
                    .sized(0.3125F, 0.3125F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "earth_charge"))));

    public static final EntityType<WaterChargeProjectileEntity> WATER_CHARGE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "water_charge"),
            EntityType.Builder.<WaterChargeProjectileEntity>of(WaterChargeProjectileEntity::new, MobCategory.MISC)
                    .sized(0.3125F, 0.3125F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "water_charge"))));

    public static final EntityType<BoreEntity> BORE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "bore"),
            EntityType.Builder.of(BoreEntity::new, MobCategory.MONSTER)
                    .clientTrackingRange(84).sized(0.6F, 1.8F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "bore"))));

    public static final EntityType<BrineEntity> BRINE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "brine"),
            EntityType.Builder.of(BrineEntity::new, MobCategory.MONSTER)
                    .clientTrackingRange(64).sized(0.8F, 1.8F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "brine"))));

    public static void registerModEntities() {
        EarthWater.LOGGER.info("Registering Mod Entities for " + EarthWater.MOD_ID);
    }
}