package potatowolfie.earth_and_water.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import potatowolfie.earth_and_water.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModLootTableGenerator extends FabricBlockLootSubProvider {
    public ModLootTableGenerator(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        add(ModBlocks.DRIPSTONE_SLAB, createSlabItemTable(ModBlocks.DRIPSTONE_SLAB));
        add(ModBlocks.DRIPSTONE_BRICK_SLAB, createSlabItemTable(ModBlocks.DRIPSTONE_BRICK_SLAB));
        add(ModBlocks.POLISHED_DRIPSTONE_SLAB, createSlabItemTable(ModBlocks.POLISHED_DRIPSTONE_SLAB));
        add(ModBlocks.DARK_DRIPSTONE_SLAB, createSlabItemTable(ModBlocks.DARK_DRIPSTONE_SLAB));
        add(ModBlocks.DARK_DRIPSTONE_BRICK_SLAB, createSlabItemTable(ModBlocks.DARK_DRIPSTONE_BRICK_SLAB));
        add(ModBlocks.POLISHED_DARK_DRIPSTONE_SLAB, createSlabItemTable(ModBlocks.POLISHED_DARK_DRIPSTONE_SLAB));
        add(ModBlocks.PRISMARINE_TILE_SLAB, createSlabItemTable(ModBlocks.PRISMARINE_TILE_SLAB));

        dropSelf(ModBlocks.DRIPSTONE_STAIRS);
        dropSelf(ModBlocks.DRIPSTONE_WALL);
        dropSelf(ModBlocks.DRIPSTONE_BRICKS);
        dropSelf(ModBlocks.DRIPSTONE_BRICK_STAIRS);
        dropSelf(ModBlocks.DRIPSTONE_BRICK_WALL);
        dropSelf(ModBlocks.POLISHED_DRIPSTONE);
        dropSelf(ModBlocks.POLISHED_DRIPSTONE_STAIRS);
        dropSelf(ModBlocks.POLISHED_DRIPSTONE_WALL);
        dropSelf(ModBlocks.POLISHED_DRIPSTONE_TILES);
        dropSelf(ModBlocks.DRIPSTONE_PILLAR);
        dropSelf(ModBlocks.CHISELED_DRIPSTONE_BRICKS);

        dropSelf(ModBlocks.DARK_DRIPSTONE_BLOCK);
        dropSelf(ModBlocks.DARK_DRIPSTONE_STAIRS);
        dropSelf(ModBlocks.DARK_DRIPSTONE_WALL);
        dropSelf(ModBlocks.DARK_DRIPSTONE_BRICKS);
        dropSelf(ModBlocks.DARK_DRIPSTONE_BRICK_STAIRS);
        dropSelf(ModBlocks.DARK_DRIPSTONE_BRICK_WALL);
        dropSelf(ModBlocks.POLISHED_DARK_DRIPSTONE);
        dropSelf(ModBlocks.POLISHED_DARK_DRIPSTONE_STAIRS);
        dropSelf(ModBlocks.POLISHED_DARK_DRIPSTONE_WALL);
        dropSelf(ModBlocks.DARK_DRIPSTONE_PILLAR);
        dropSelf(ModBlocks.POINTED_DARK_DRIPSTONE);

        dropSelf(ModBlocks.DARK_PRISMARINE_PILLAR);
        dropSelf(ModBlocks.CHISELED_PRISMARINE_BRICKS);
        dropSelf(ModBlocks.PRISMARINE_PILLAR);
        dropSelf(ModBlocks.PRISMARINE_TILES);
        dropSelf(ModBlocks.PRISMARINE_TILE_STAIRS);
        dropSelf(ModBlocks.PRISMARINE_TILE_WALL);
        dropSelf(ModBlocks.MIXED_PRISMARINE_TILES);
        dropSelf(ModBlocks.CHISELED_DARK_PRISMARINE);
        dropSelf(ModBlocks.DARK_PRISMARINE_WALL);

        dropSelf(ModBlocks.LIMESTONE);
        dropSelf(ModBlocks.LIMESTONE_STAIRS);
        add(ModBlocks.LIMESTONE_SLAB, createSlabItemTable(ModBlocks.LIMESTONE_SLAB));
        dropSelf(ModBlocks.LIMESTONE_WALL);
        dropSelf(ModBlocks.POLISHED_LIMESTONE);
        dropSelf(ModBlocks.POLISHED_LIMESTONE_STAIRS);
        add(ModBlocks.POLISHED_LIMESTONE_SLAB, createSlabItemTable(ModBlocks.POLISHED_LIMESTONE_SLAB));
        dropSelf(ModBlocks.POLISHED_LIMESTONE_WALL);
        dropSelf(ModBlocks.LIMESTONE_BRICKS);
        dropSelf(ModBlocks.LIMESTONE_BRICK_STAIRS);
        add(ModBlocks.LIMESTONE_BRICK_SLAB, createSlabItemTable(ModBlocks.LIMESTONE_BRICK_SLAB));
        dropSelf(ModBlocks.LIMESTONE_BRICK_WALL);
        dropSelf(ModBlocks.LIMESTONE_PILLAR);
        dropSelf(ModBlocks.CHISELED_LIMESTONE_BRICKS);

        dropSelf(ModBlocks.OXYGEN_BLOCK);
        dropSelf(ModBlocks.STEEL_BLOCK);
    }
}
