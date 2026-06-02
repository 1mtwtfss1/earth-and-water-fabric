package potatowolfie.earth_and_water;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import potatowolfie.earth_and_water.block.ModBlocks;
import potatowolfie.earth_and_water.block.entity.ModBlockEntities;
import potatowolfie.earth_and_water.datagen.ModLootTableModifier;
import potatowolfie.earth_and_water.effect.ModEffects;
import potatowolfie.earth_and_water.entity.ModEntities;
import potatowolfie.earth_and_water.entity.brine.BrineEntity;
import potatowolfie.earth_and_water.entity.bore.BoreEntity;
import potatowolfie.earth_and_water.item.ModItems;
import potatowolfie.earth_and_water.sound.ModSounds;
import potatowolfie.earth_and_water.structure.ModStructurePieceTypes;
import potatowolfie.earth_and_water.structure.ModStructureTypes;
import potatowolfie.earth_and_water.world.feature.ModFeatures;
import potatowolfie.earth_and_water.world.gen.ModWorldGeneration;

public class EarthWater implements ModInitializer {
	public static final String MOD_ID = "earth-and-water";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final SimpleParticleType LIGHT_UP = FabricParticleTypes.simple();
	public static final SimpleParticleType REINFORCED_SPAWNER_DETECTION = FabricParticleTypes.simple();
	public static final SimpleParticleType REINFORCED_SPAWNER_DETECTION_OUTWARD = FabricParticleTypes.simple();
	public static final SimpleParticleType REINFORCED_SPAWNER_DETECTION_INNER = FabricParticleTypes.simple();

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModEffects.registerEffects();
		ModEntities.registerModEntities();
		ModBlockEntities.registerBlockEntities();
		ModSounds.registerSounds();
		ModFeatures.registerModFeatures();
		ModWorldGeneration.init();
		ModStructureTypes.registerStructureTypes();
		ModStructurePieceTypes.registerStructurePieceTypes();
		ModLootTableModifier.modifyLootTables();
		registerChunkLoadEvent();

		Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "light_up"), LIGHT_UP);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "reinforced_spawner_detection"), REINFORCED_SPAWNER_DETECTION);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "reinforced_spawner_detection_outward"), REINFORCED_SPAWNER_DETECTION_OUTWARD);
		Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "reinforced_spawner_detection_inner"), REINFORCED_SPAWNER_DETECTION_INNER);

		registerDispenserBehaviors();

		FabricDefaultAttributeRegistry.register(ModEntities.BORE, BoreEntity.createBoreAttributes());
		FabricDefaultAttributeRegistry.register(ModEntities.BRINE, BrineEntity.createBrineAttributes());

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			server.overworld().scheduleTick(BlockPos.ZERO, Blocks.CAULDRON, 1);
			server.overworld().scheduleTick(BlockPos.ZERO, Blocks.LAVA_CAULDRON, 1);
			server.overworld().scheduleTick(BlockPos.ZERO, Blocks.WATER_CAULDRON, 1);
			server.overworld().scheduleTick(BlockPos.ZERO, Blocks.POWDER_SNOW_CAULDRON, 1);
		});

		LOGGER.info("Earth and Water mod initialized!");
	}

	private void registerDispenserBehaviors() {
		registerProjectileDispenserBehavior(ModItems.WATER_CHARGE);
		registerProjectileDispenserBehavior(ModItems.EARTH_CHARGE);
	}

	private void registerChunkLoadEvent() {
		ServerChunkEvents.CHUNK_LOAD.register((world, chunk, generated) -> {
			scheduleBlockTicksForChunk(world, chunk);
		});
	}

	private void scheduleBlockTicksForChunk(ServerLevel world, LevelChunk chunk) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int minY = world.getMinY();
				int maxY = minY + world.getHeight();

				for (int y = minY; y < maxY; y++) {
					pos.set(chunk.getPos().getMinBlockX() + x, y, chunk.getPos().getMinBlockZ() + z);
					var state = chunk.getBlockState(pos);
					var block = state.getBlock();

					if (block == ModBlocks.CHISELED_DARK_PRISMARINE ||
							block == ModBlocks.CHISELED_PRISMARINE_BRICKS ||
							block == ModBlocks.CHISELED_DARK_DRIPSTONE_BRICKS ||
							block == ModBlocks.CHISELED_DRIPSTONE_BRICKS ||
							block == ModBlocks.REINFORCED_SPAWNER) {

						world.scheduleTick(pos.immutable(), block, 2);
					}
				}
			}
		}
	}

	private void registerProjectileDispenserBehavior(Item item) {
		DispenserBlock.registerBehavior(
				item,
				(pointer, stack) -> {
					Level world = pointer.level();
					Position position = DispenserBlock.getDispensePosition(pointer);
					Direction direction = pointer.state().getValue(DispenserBlock.FACING);

					ProjectileItem projectileItem = (ProjectileItem) stack.getItem();
					Projectile projectileEntity = projectileItem.asProjectile(world, position, stack, direction);

					projectileEntity.shoot(
							direction.getStepX(),
							direction.getStepY() + 0.1F,
							direction.getStepZ(),
							1.5F,
							0.1F
					);

					world.addFreshEntity(projectileEntity);
					stack.shrink(1);
					return stack;
				}
		);
	}
}