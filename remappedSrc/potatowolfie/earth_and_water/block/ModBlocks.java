package potatowolfie.earth_and_water.block;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import potatowolfie.earth_and_water.EarthWater;
import potatowolfie.earth_and_water.block.custom.*;

import static net.minecraft.world.level.block.Blocks.*;

public class ModBlocks {
    public static final Block STEEL_BLOCK = registerBlock("steel_block",
            new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_LIGHT_GRAY)
                            .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                            .requiresCorrectToolForDrops()
                            .strength(5.0F, 6.0F)
                            .sound(SoundType.METAL)
                            .setId(createBlockRegistryKey("steel_block"))
            ));

    public static final Block DRIPSTONE_PILLAR = registerBlock("dripstone_pillar",
            new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                    .setId(createBlockRegistryKey("dripstone_pillar"))));
    public static final Block DARK_DRIPSTONE_PILLAR = registerBlock("dark_dripstone_pillar",
            new RotatedPillarBlock(BlockBehaviour.Properties.of().sound(SoundType.DRIPSTONE_BLOCK)
                    .strength(4f).requiresCorrectToolForDrops()
                    .setId(createBlockRegistryKey("dark_dripstone_pillar"))));
    public static final Block DARK_PRISMARINE_PILLAR = registerBlock("dark_prismarine_pillar",
            new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(DARK_PRISMARINE)
                    .setId(createBlockRegistryKey("dark_prismarine_pillar"))));
    public static final Block PRISMARINE_PILLAR = registerBlock("prismarine_pillar",
            new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(PRISMARINE)
                    .setId(createBlockRegistryKey("prismarine_pillar"))));

    public static final Block DRIPSTONE_BRICKS = registerBlock("dripstone_bricks",
            new Block(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                    .setId(createBlockRegistryKey("dripstone_bricks"))));
    public static final Block DRIPSTONE_BRICK_STAIRS = registerBlock("dripstone_brick_stairs",
            new StairBlock(ModBlocks.DRIPSTONE_BRICKS.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                            .setId(createBlockRegistryKey("dripstone_brick_stairs"))));
    public static final Block DRIPSTONE_BRICK_SLAB = registerBlock("dripstone_brick_slab",
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                    .setId(createBlockRegistryKey("dripstone_brick_slab"))));
    public static final Block DRIPSTONE_BRICK_WALL = registerBlock("dripstone_brick_wall",
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                    .setId(createBlockRegistryKey("dripstone_brick_wall"))));

    public static final Block POLISHED_DRIPSTONE = registerBlock("polished_dripstone",
            new Block(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                    .setId(createBlockRegistryKey("polished_dripstone"))));
    public static final Block POLISHED_DRIPSTONE_STAIRS = registerBlock("polished_dripstone_stairs",
            new StairBlock(ModBlocks.POLISHED_DRIPSTONE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                            .setId(createBlockRegistryKey("polished_dripstone_stairs"))));
    public static final Block POLISHED_DRIPSTONE_SLAB = registerBlock("polished_dripstone_slab",
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                    .setId(createBlockRegistryKey("polished_dripstone_slab"))));
    public static final Block POLISHED_DRIPSTONE_WALL = registerBlock("polished_dripstone_wall",
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                    .setId(createBlockRegistryKey("polished_dripstone_wall"))));

    public static final Block DRIPSTONE_STAIRS = registerBlock("dripstone_stairs",
            new StairBlock(Blocks.DRIPSTONE_BLOCK.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                            .setId(createBlockRegistryKey("dripstone_stairs"))));
    public static final Block DRIPSTONE_SLAB = registerBlock("dripstone_slab",
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                    .setId(createBlockRegistryKey("dripstone_slab"))));
    public static final Block DRIPSTONE_WALL = registerBlock("dripstone_wall",
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                    .setId(createBlockRegistryKey("dripstone_wall"))));

    public static final Block CHISELED_DRIPSTONE_BRICKS = registerBlock("chiseled_dripstone_bricks",
            new ChiseledDripstoneBricksBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .sound(SoundType.DRIPSTONE_BLOCK)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 1.0F)
                    .lightLevel(state -> state.getValue(ChiseledDripstoneBricksBlock.POWERED) ? 12 : 0)
                    .setId(createBlockRegistryKey("chiseled_dripstone_bricks"))));
    public static final Block CHISELED_DARK_DRIPSTONE_BRICKS = registerBlock("chiseled_dark_dripstone_bricks",
            new ChiseledDarkDripstoneBricksBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIAMOND)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .lightLevel(state -> state.getValue(ChiseledDarkDripstoneBricksBlock.POWERED) ? 12 : 0)
                    .setId(createBlockRegistryKey("chiseled_dark_dripstone_bricks"))));

    public static final Block DARK_DRIPSTONE_BLOCK = registerBlock("dark_dripstone_block",
            new Block(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                    .setId(createBlockRegistryKey("dark_dripstone_block"))));
    public static final Block DARK_DRIPSTONE_STAIRS = registerBlock("dark_dripstone_stairs",
            new StairBlock(ModBlocks.DARK_DRIPSTONE_BLOCK.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                            .setId(createBlockRegistryKey("dark_dripstone_stairs"))));
    public static final Block DARK_DRIPSTONE_SLAB = registerBlock("dark_dripstone_slab",
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                    .setId(createBlockRegistryKey("dark_dripstone_slab"))));
    public static final Block DARK_DRIPSTONE_WALL = registerBlock("dark_dripstone_wall",
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BLOCK)
                    .setId(createBlockRegistryKey("dark_dripstone_wall"))));

    public static final Block POLISHED_DARK_DRIPSTONE = registerBlock("polished_dark_dripstone",
            new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_DRIPSTONE)
                    .setId(createBlockRegistryKey("polished_dark_dripstone"))));
    public static final Block POLISHED_DARK_DRIPSTONE_STAIRS = registerBlock("polished_dark_dripstone_stairs",
            new StairBlock(ModBlocks.POLISHED_DARK_DRIPSTONE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(POLISHED_DRIPSTONE)
                            .setId(createBlockRegistryKey("polished_dark_dripstone_stairs"))));
    public static final Block POLISHED_DARK_DRIPSTONE_SLAB = registerBlock("polished_dark_dripstone_slab",
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_DRIPSTONE)
                    .setId(createBlockRegistryKey("polished_dark_dripstone_slab"))));
    public static final Block POLISHED_DARK_DRIPSTONE_WALL = registerBlock("polished_dark_dripstone_wall",
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_DRIPSTONE)
                    .setId(createBlockRegistryKey("polished_dark_dripstone_wall"))));

    public static final Block DARK_DRIPSTONE_BRICKS = registerBlock("dark_dripstone_bricks",
            new Block(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BRICKS)
                    .setId(createBlockRegistryKey("dark_dripstone_bricks"))));
    public static final Block DARK_DRIPSTONE_BRICK_STAIRS = registerBlock("dark_dripstone_brick_stairs",
            new StairBlock(ModBlocks.DARK_DRIPSTONE_BRICKS.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BRICKS)
                            .setId(createBlockRegistryKey("dark_dripstone_brick_stairs"))));
    public static final Block DARK_DRIPSTONE_BRICK_SLAB = registerBlock("dark_dripstone_brick_slab",
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BRICKS)
                    .setId(createBlockRegistryKey("dark_dripstone_brick_slab"))));
    public static final Block DARK_DRIPSTONE_BRICK_WALL = registerBlock("dark_dripstone_brick_wall",
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(DRIPSTONE_BRICKS)
                    .setId(createBlockRegistryKey("dark_dripstone_brick_wall"))));

    public static final Block POINTED_DARK_DRIPSTONE = registerBlock("pointed_dark_dripstone",
            new PointedDarkDripstoneBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_BROWN)
                    .forceSolidOn()
                    .requiresCorrectToolForDrops()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .noOcclusion()
                    .sound(SoundType.POINTED_DRIPSTONE)
                    .randomTicks()
                    .strength(1.5F, 3.0F)
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor(Blocks::never)
                    .setId(createBlockRegistryKey("pointed_dark_dripstone"))));

    public static final Block CHISELED_PRISMARINE_BRICKS = registerBlock("chiseled_prismarine_bricks",
            new ChiseledPrismarineBricksBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)
                    .lightLevel(state -> state.getValue(ChiseledPrismarineBricksBlock.ACTIVE) ? 12 : 0)
                    .setId(createBlockRegistryKey("chiseled_prismarine_bricks"))));
    public static final Block PRISMARINE_TILES = registerBlock("prismarine_tiles",
            new Block(BlockBehaviour.Properties.ofFullCopy(PRISMARINE)
                    .setId(createBlockRegistryKey("prismarine_tiles"))));
    public static final Block PRISMARINE_TILE_STAIRS = registerBlock("prismarine_tile_stairs",
            new StairBlock(ModBlocks.PRISMARINE_TILES.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(PRISMARINE)
                            .setId(createBlockRegistryKey("prismarine_tile_stairs"))));
    public static final Block PRISMARINE_TILE_SLAB = registerBlock("prismarine_tile_slab",
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(PRISMARINE)
                    .setId(createBlockRegistryKey("prismarine_tile_slab"))));
    public static final Block PRISMARINE_TILE_WALL = registerBlock("prismarine_tile_wall",
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(PRISMARINE)
                    .setId(createBlockRegistryKey("prismarine_tile_wall"))));

    public static final Block MIXED_PRISMARINE_TILES = registerBlock("mixed_prismarine_tiles",
            new Block(BlockBehaviour.Properties.ofFullCopy(PRISMARINE)
                    .setId(createBlockRegistryKey("mixed_prismarine_tiles"))));
    public static final Block POLISHED_DRIPSTONE_TILES = registerBlock("polished_dripstone_tiles",
            new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_DARK_DRIPSTONE)
                    .setId(createBlockRegistryKey("polished_dripstone_tiles"))));
    public static final Block CHISELED_DARK_PRISMARINE = registerBlock("chiseled_dark_prismarine",
            new ChiseledDarkPrismarineBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND)
                    .instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)
                    .setId(createBlockRegistryKey("chiseled_dark_prismarine"))));
    public static final Block DARK_PRISMARINE_WALL = registerBlock("dark_prismarine_wall",
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(DARK_PRISMARINE)
                    .setId(createBlockRegistryKey("dark_prismarine_wall"))));

    public static final Block LIMESTONE = registerBlock("limestone",
            new Block(BlockBehaviour.Properties.ofFullCopy(TUFF)
                    .setId(createBlockRegistryKey("limestone"))));
    public static final Block LIMESTONE_STAIRS = registerBlock("limestone_stairs",
            new StairBlock(ModBlocks.LIMESTONE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(TUFF)
                            .setId(createBlockRegistryKey("limestone_stairs"))));
    public static final Block LIMESTONE_SLAB = registerBlock("limestone_slab",
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(TUFF)
                    .setId(createBlockRegistryKey("limestone_slab"))));
    public static final Block LIMESTONE_WALL = registerBlock("limestone_wall",
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(TUFF)
                    .setId(createBlockRegistryKey("limestone_wall"))));
    public static final Block POLISHED_LIMESTONE = registerBlock("polished_limestone",
            new Block(BlockBehaviour.Properties.ofFullCopy(POLISHED_TUFF)
                    .setId(createBlockRegistryKey("polished_limestone"))));
    public static final Block POLISHED_LIMESTONE_STAIRS = registerBlock("polished_limestone_stairs",
            new StairBlock(ModBlocks.POLISHED_LIMESTONE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(POLISHED_TUFF)
                            .setId(createBlockRegistryKey("polished_limestone_stairs"))));
    public static final Block POLISHED_LIMESTONE_SLAB = registerBlock("polished_limestone_slab",
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_TUFF)
                    .setId(createBlockRegistryKey("polished_limestone_slab"))));
    public static final Block POLISHED_LIMESTONE_WALL = registerBlock("polished_limestone_wall",
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_TUFF)
                    .setId(createBlockRegistryKey("polished_limestone_wall"))));
    public static final Block LIMESTONE_BRICKS = registerBlock("limestone_bricks",
            new Block(BlockBehaviour.Properties.ofFullCopy(TUFF_BRICKS)
                    .setId(createBlockRegistryKey("limestone_bricks"))));
    public static final Block LIMESTONE_BRICK_STAIRS = registerBlock("limestone_brick_stairs",
            new StairBlock(ModBlocks.LIMESTONE_BRICKS.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(TUFF_BRICKS)
                            .setId(createBlockRegistryKey("limestone_brick_stairs"))));
    public static final Block LIMESTONE_BRICK_SLAB = registerBlock("limestone_brick_slab",
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(TUFF_BRICKS)
                    .setId(createBlockRegistryKey("limestone_brick_slab"))));
    public static final Block LIMESTONE_BRICK_WALL = registerBlock("limestone_brick_wall",
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(TUFF_BRICKS)
                    .setId(createBlockRegistryKey("limestone_brick_wall"))));
    public static final Block LIMESTONE_PILLAR = registerBlock("limestone_pillar",
            new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LIMESTONE)
                    .setId(createBlockRegistryKey("limestone_pillar"))));
    public static final Block CHISELED_LIMESTONE_BRICKS = registerBlock("chiseled_limestone_bricks",
            new Block(BlockBehaviour.Properties.ofFullCopy(LIMESTONE_BRICKS)
                    .setId(createBlockRegistryKey("chiseled_limestone_bricks"))));

    public static final Block OXYGEN_BLOCK = registerBlock("oxygen_block",
            new OxygenBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NETHER)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 3)
                    .strength(0.5F)
                    .isValidSpawn((state, world, pos, entityType) -> entityType.fireImmune())
                    .hasPostProcess(Blocks::always)
                    .emissiveRendering(Blocks::always)
                    .setId(createBlockRegistryKey("oxygen_block"))));

    public static final Block OXYGEN_BUBBLE = registerBlock("oxygen_bubble",
            new OxygenBubbleBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .replaceable()
                    .noCollision()
                    .noLootTable()
                    .pushReaction(PushReaction.DESTROY)
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .setId(createBlockRegistryKey("oxygen_bubble"))));

    public static final Block REINFORCED_SPAWNER = registerBlock("reinforced_spawner",
            new ReinforcedSpawnerBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(50.0F)
                    .sound(SoundType.TRIAL_SPAWNER)
                    .isViewBlocking(Blocks::never)
                    .noOcclusion()
                    .pushReaction(PushReaction.BLOCK)
                    .lightLevel(state -> state.getValue(ReinforcedSpawnerBlock.ACTIVE) ? 4 : 0)
                    .setId(createBlockRegistryKey("reinforced_spawner"))
            )
    );

    private static ResourceKey<Block> createBlockRegistryKey(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, name));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, createBlockRegistryKey(name), block);
    }

    private static void customBuildingBlocks(FabricItemGroupEntries entries) {
        entries.addBefore(IRON_CHAIN, STEEL_BLOCK);
        entries.addAfter(CHISELED_TUFF_BRICKS, DRIPSTONE_BLOCK);
        entries.addAfter(DRIPSTONE_BLOCK, DRIPSTONE_STAIRS);
        entries.addAfter(DRIPSTONE_STAIRS, DRIPSTONE_SLAB);
        entries.addAfter(DRIPSTONE_SLAB, DRIPSTONE_WALL);
        entries.addAfter(DRIPSTONE_WALL, POLISHED_DRIPSTONE);
        entries.addAfter(POLISHED_DRIPSTONE, POLISHED_DRIPSTONE_STAIRS);
        entries.addAfter(POLISHED_DRIPSTONE_STAIRS, POLISHED_DRIPSTONE_SLAB);
        entries.addAfter(POLISHED_DRIPSTONE_SLAB, POLISHED_DRIPSTONE_WALL);
        entries.addAfter(POLISHED_DRIPSTONE_WALL, DRIPSTONE_BRICKS);
        entries.addAfter(DRIPSTONE_BRICKS, DRIPSTONE_BRICK_STAIRS);
        entries.addAfter(DRIPSTONE_BRICK_STAIRS, DRIPSTONE_BRICK_SLAB);
        entries.addAfter(DRIPSTONE_BRICK_SLAB, DRIPSTONE_BRICK_WALL);
        entries.addAfter(DRIPSTONE_BRICK_WALL, CHISELED_DRIPSTONE_BRICKS);
        entries.addAfter(CHISELED_DRIPSTONE_BRICKS, DRIPSTONE_PILLAR);
        entries.addAfter(DRIPSTONE_PILLAR, POLISHED_DRIPSTONE_TILES);
        entries.addAfter(POLISHED_DRIPSTONE_TILES, DARK_DRIPSTONE_BLOCK);
        entries.addAfter(DARK_DRIPSTONE_BLOCK, DARK_DRIPSTONE_STAIRS);
        entries.addAfter(DARK_DRIPSTONE_STAIRS, DARK_DRIPSTONE_SLAB);
        entries.addAfter(DARK_DRIPSTONE_SLAB, DARK_DRIPSTONE_WALL);
        entries.addAfter(DARK_DRIPSTONE_WALL, POLISHED_DARK_DRIPSTONE);
        entries.addAfter(POLISHED_DARK_DRIPSTONE, POLISHED_DARK_DRIPSTONE_STAIRS);
        entries.addAfter(POLISHED_DARK_DRIPSTONE_STAIRS, POLISHED_DARK_DRIPSTONE_SLAB);
        entries.addAfter(POLISHED_DARK_DRIPSTONE_SLAB, POLISHED_DARK_DRIPSTONE_WALL);
        entries.addAfter(POLISHED_DARK_DRIPSTONE_WALL, DARK_DRIPSTONE_BRICKS);
        entries.addAfter(DARK_DRIPSTONE_BRICKS, DARK_DRIPSTONE_BRICK_STAIRS);
        entries.addAfter(DARK_DRIPSTONE_BRICK_STAIRS, DARK_DRIPSTONE_BRICK_SLAB);
        entries.addAfter(DARK_DRIPSTONE_BRICK_SLAB, DARK_DRIPSTONE_BRICK_WALL);
        entries.addAfter(DARK_DRIPSTONE_BRICK_WALL, CHISELED_DARK_DRIPSTONE_BRICKS);
        entries.addAfter(CHISELED_DARK_DRIPSTONE_BRICKS, DARK_DRIPSTONE_PILLAR);
        entries.addAfter(DARK_DRIPSTONE_PILLAR, LIMESTONE);
        entries.addAfter(LIMESTONE, LIMESTONE_STAIRS);
        entries.addAfter(LIMESTONE_STAIRS, LIMESTONE_SLAB);
        entries.addAfter(LIMESTONE_SLAB, LIMESTONE_WALL);
        entries.addAfter(LIMESTONE_WALL, POLISHED_LIMESTONE);
        entries.addAfter(POLISHED_LIMESTONE, POLISHED_LIMESTONE_STAIRS);
        entries.addAfter(POLISHED_LIMESTONE_STAIRS, POLISHED_LIMESTONE_SLAB);
        entries.addAfter(POLISHED_LIMESTONE_SLAB, POLISHED_LIMESTONE_WALL);
        entries.addAfter(POLISHED_LIMESTONE_WALL, LIMESTONE_BRICKS);
        entries.addAfter(LIMESTONE_BRICKS, LIMESTONE_BRICK_STAIRS);
        entries.addAfter(LIMESTONE_BRICK_STAIRS, LIMESTONE_BRICK_SLAB);
        entries.addAfter(LIMESTONE_BRICK_SLAB, LIMESTONE_BRICK_WALL);
        entries.addAfter(LIMESTONE_BRICK_WALL, CHISELED_LIMESTONE_BRICKS);
        entries.addAfter(CHISELED_LIMESTONE_BRICKS, LIMESTONE_PILLAR);
        entries.addAfter(PRISMARINE_BRICK_SLAB, CHISELED_PRISMARINE_BRICKS);
        entries.addAfter(CHISELED_PRISMARINE_BRICKS, PRISMARINE_TILES);
        entries.addAfter(PRISMARINE_TILES, PRISMARINE_TILE_STAIRS);
        entries.addAfter(PRISMARINE_TILE_STAIRS, PRISMARINE_TILE_SLAB);
        entries.addAfter(PRISMARINE_TILE_SLAB, PRISMARINE_TILE_WALL);
        entries.addAfter(PRISMARINE_TILE_WALL, MIXED_PRISMARINE_TILES);
        entries.addAfter(PRISMARINE, PRISMARINE_PILLAR);
        entries.addAfter(DARK_PRISMARINE_SLAB, DARK_PRISMARINE_WALL);
        entries.addAfter(DARK_PRISMARINE_WALL, DARK_PRISMARINE_PILLAR);
        entries.addAfter(DARK_PRISMARINE_PILLAR, CHISELED_DARK_PRISMARINE);
    }

    private static void customNaturalBlocks(FabricItemGroupEntries entries) {
        entries.addAfter(MAGMA_BLOCK, OXYGEN_BLOCK);
        entries.addAfter(POINTED_DRIPSTONE, DARK_DRIPSTONE_BLOCK);
        entries.addAfter(DARK_DRIPSTONE_BLOCK, POINTED_DARK_DRIPSTONE);
        entries.addAfter(POINTED_DARK_DRIPSTONE, LIMESTONE);
    }

    private static void customSpawnEggs(FabricItemGroupEntries entries) {
        entries.addAfter(TRIAL_SPAWNER, REINFORCED_SPAWNER);
    }

    private static void registerBlockItem(String name, Block block) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, name));
        Registry.register(BuiltInRegistries.ITEM, itemKey,
                new BlockItem(block, new Item.Properties()
                        .useBlockDescriptionPrefix()
                        .setId(itemKey)));
    }

    public static void registerModBlocks () {
        EarthWater.LOGGER.info("Registering Mod Blocks for " + EarthWater.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(ModBlocks::customBuildingBlocks);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(ModBlocks::customNaturalBlocks);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(ModBlocks::customSpawnEggs);
    }
}