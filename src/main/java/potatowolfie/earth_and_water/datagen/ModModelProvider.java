package potatowolfie.earth_and_water.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.world.level.block.Blocks;
import potatowolfie.earth_and_water.block.ModBlocks;
import potatowolfie.earth_and_water.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        BlockModelGenerators.BlockFamilyProvider polisheddripstoneTexturePool = blockStateModelGenerator.family(ModBlocks.POLISHED_DRIPSTONE);

        BlockModelGenerators.BlockFamilyProvider dripstonebricksTexturePool = blockStateModelGenerator.family(ModBlocks.DRIPSTONE_BRICKS);
        dripstonebricksTexturePool.stairs(ModBlocks.DRIPSTONE_BRICK_STAIRS);
        dripstonebricksTexturePool.slab(ModBlocks.DRIPSTONE_BRICK_SLAB);
        dripstonebricksTexturePool.wall(ModBlocks.DRIPSTONE_BRICK_WALL);

        BlockModelGenerators.BlockFamilyProvider darkdripstoneTexturePool = blockStateModelGenerator.family(ModBlocks.DARK_DRIPSTONE_BLOCK);
        darkdripstoneTexturePool.stairs(ModBlocks.DARK_DRIPSTONE_STAIRS);
        darkdripstoneTexturePool.slab(ModBlocks.DARK_DRIPSTONE_SLAB);
        darkdripstoneTexturePool.wall(ModBlocks.DARK_DRIPSTONE_WALL);

        BlockModelGenerators.BlockFamilyProvider polisheddarkdripstoneTexturePool = blockStateModelGenerator.family(ModBlocks.POLISHED_DARK_DRIPSTONE);
        polisheddarkdripstoneTexturePool.stairs(ModBlocks.POLISHED_DARK_DRIPSTONE_STAIRS);
        polisheddarkdripstoneTexturePool.slab(ModBlocks.POLISHED_DARK_DRIPSTONE_SLAB);
        polisheddarkdripstoneTexturePool.wall(ModBlocks.POLISHED_DARK_DRIPSTONE_WALL);

        BlockModelGenerators.BlockFamilyProvider darkdripstonebricksTexturePool = blockStateModelGenerator.family(ModBlocks.DARK_DRIPSTONE_BRICKS);
        darkdripstonebricksTexturePool.stairs(ModBlocks.DARK_DRIPSTONE_BRICK_STAIRS);
        darkdripstonebricksTexturePool.slab(ModBlocks.DARK_DRIPSTONE_BRICK_SLAB);
        darkdripstonebricksTexturePool.wall(ModBlocks.DARK_DRIPSTONE_BRICK_WALL);

        BlockModelGenerators.BlockFamilyProvider dripstoneTexturePool = blockStateModelGenerator.family(Blocks.DRIPSTONE_BLOCK);
        dripstoneTexturePool.stairs(ModBlocks.DRIPSTONE_STAIRS);
        dripstoneTexturePool.slab(ModBlocks.DRIPSTONE_SLAB);
        dripstoneTexturePool.wall(ModBlocks.DRIPSTONE_WALL);

        polisheddripstoneTexturePool.stairs(ModBlocks.POLISHED_DRIPSTONE_STAIRS);
        polisheddripstoneTexturePool.slab(ModBlocks.POLISHED_DRIPSTONE_SLAB);
        polisheddripstoneTexturePool.wall(ModBlocks.POLISHED_DRIPSTONE_WALL);

        BlockModelGenerators.BlockFamilyProvider limestoneTexturePool = blockStateModelGenerator.family(ModBlocks.LIMESTONE);
        limestoneTexturePool.stairs(ModBlocks.LIMESTONE_STAIRS);
        limestoneTexturePool.slab(ModBlocks.LIMESTONE_SLAB);
        limestoneTexturePool.wall(ModBlocks.LIMESTONE_WALL);

        BlockModelGenerators.BlockFamilyProvider polishedLimestoneTexturePool = blockStateModelGenerator.family(ModBlocks.POLISHED_LIMESTONE);
        polishedLimestoneTexturePool.stairs(ModBlocks.POLISHED_LIMESTONE_STAIRS);
        polishedLimestoneTexturePool.slab(ModBlocks.POLISHED_LIMESTONE_SLAB);
        polishedLimestoneTexturePool.wall(ModBlocks.POLISHED_LIMESTONE_WALL);

        BlockModelGenerators.BlockFamilyProvider limestoneBricksTexturePool = blockStateModelGenerator.family(ModBlocks.LIMESTONE_BRICKS);
        limestoneBricksTexturePool.stairs(ModBlocks.LIMESTONE_BRICK_STAIRS);
        limestoneBricksTexturePool.slab(ModBlocks.LIMESTONE_BRICK_SLAB);
        limestoneBricksTexturePool.wall(ModBlocks.LIMESTONE_BRICK_WALL);

        blockStateModelGenerator.createRotatedPillarWithHorizontalVariant(ModBlocks.LIMESTONE_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
        blockStateModelGenerator.createTrivialBlock(ModBlocks.CHISELED_LIMESTONE_BRICKS, TexturedModel.COLUMN_ALT);

        blockStateModelGenerator.createTrivialCube(ModBlocks.STEEL_BLOCK);
        blockStateModelGenerator.createTrivialCube(ModBlocks.OXYGEN_BLOCK);

        BlockModelGenerators.BlockFamilyProvider prismarinetilesTexturePool = blockStateModelGenerator.family(ModBlocks.PRISMARINE_TILES);
        prismarinetilesTexturePool.stairs(ModBlocks.PRISMARINE_TILE_STAIRS);
        prismarinetilesTexturePool.slab(ModBlocks.PRISMARINE_TILE_SLAB);
        prismarinetilesTexturePool.wall(ModBlocks.PRISMARINE_TILE_WALL);

        BlockModelGenerators.BlockFamilyProvider darkprismarineTexturePool = blockStateModelGenerator.family(Blocks.DARK_PRISMARINE);
        darkprismarineTexturePool.wall(ModBlocks.DARK_PRISMARINE_WALL);

        blockStateModelGenerator.createRotatedPillarWithHorizontalVariant(ModBlocks.DRIPSTONE_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
        blockStateModelGenerator.createRotatedPillarWithHorizontalVariant(ModBlocks.DARK_DRIPSTONE_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
        blockStateModelGenerator.createRotatedPillarWithHorizontalVariant(ModBlocks.DARK_PRISMARINE_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
        blockStateModelGenerator.createRotatedPillarWithHorizontalVariant(ModBlocks.PRISMARINE_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.BORE_ROD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.BRINE_ROD, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.STEEL_INGOT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.STEEL_NUGGET, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.STEEL_UPGRADE_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.BLOCK_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.GUARD_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.REINFORCED_KEY, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.WHIP, ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.BATTLE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.BORE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.BRINE_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
    }
}
