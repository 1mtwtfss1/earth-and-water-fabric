package potatowolfie.earth_and_water.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;
import potatowolfie.earth_and_water.EarthWater;
import potatowolfie.earth_and_water.block.ModBlocks;
import potatowolfie.earth_and_water.block.entity.custom.ReinforcedSpawnerBlockEntity;

public class ModBlockEntities {

    public static final BlockEntityType<ReinforcedSpawnerBlockEntity> REINFORCED_SPAWNER_BLOCK_ENTITY =
            Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath("earth-and-water", "reinforced_spawner"),
                    FabricBlockEntityTypeBuilder.create(ReinforcedSpawnerBlockEntity::new, ModBlocks.REINFORCED_SPAWNER).build()
            );

    public static void registerBlockEntities() {
        EarthWater.LOGGER.info("Registering Block Entities for " + EarthWater.MOD_ID);
    }
}