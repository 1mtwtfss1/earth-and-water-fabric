package potatowolfie.earth_and_water.structure;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import potatowolfie.earth_and_water.EarthWater;
import potatowolfie.earth_and_water.structure.ancient_ruins.AncientRuinsGenerator;
import potatowolfie.earth_and_water.structure.conduit_monument.ConduitMonumentGenerator;

public class ModStructurePieceTypes {
    public static final StructurePieceType CONDUIT_MONUMENT =
            register((StructurePieceType.StructureTemplateType) ConduitMonumentGenerator.Piece::new, "conduit_monument");

    public static final StructurePieceType ANCIENT_RUINS =
            register((StructurePieceType.StructureTemplateType) AncientRuinsGenerator.Piece::new, "ancient_ruins");

    private static StructurePieceType register(StructurePieceType type, String id) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PIECE,
                Identifier.fromNamespaceAndPath("earth-and-water", id), type);
    }

    private static StructurePieceType register(
            String id,
            StructurePieceType.ContextlessType type
    ) {
        return Registry.register(
                BuiltInRegistries.STRUCTURE_PIECE,
                Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, id),
                type
        );
    }

    public static void registerStructurePieceTypes() {
        EarthWater.LOGGER.info("Registering structure piece types for " + EarthWater.MOD_ID);
    }
}