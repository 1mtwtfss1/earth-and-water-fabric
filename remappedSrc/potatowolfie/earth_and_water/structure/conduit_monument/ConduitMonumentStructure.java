package potatowolfie.earth_and_water.structure.conduit_monument;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import potatowolfie.earth_and_water.structure.ModStructureTypes;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public class ConduitMonumentStructure extends Structure {
    public static final MapCodec<ConduitMonumentStructure> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(
                settingsCodec(instance),
                Codec.floatRange(0.0F, 1.0F).fieldOf("medium_probability").forGetter((structure) -> {
                    return structure.mediumProbability;
                }),
                Codec.intRange(2, 5).fieldOf("min_ruins").forGetter((structure) -> {
                    return structure.minRuins;
                }),
                Codec.intRange(2, 5).fieldOf("max_ruins").forGetter((structure) -> {
                    return structure.maxRuins;
                })
        ).apply(instance, ConduitMonumentStructure::new);
    });

    public final float mediumProbability;
    public final int minRuins;
    public final int maxRuins;

    public ConduitMonumentStructure(Structure.StructureSettings config, float mediumProbability, int minRuins, int maxRuins) {
        super(config);
        this.mediumProbability = mediumProbability;
        this.minRuins = minRuins;
        this.maxRuins = maxRuins;
    }

    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (collector) -> {
            this.addPieces(collector, context);
        });
    }

    private void addPieces(StructurePiecesBuilder collector, Structure.GenerationContext context) {
        BlockPos blockPos = new BlockPos(context.chunkPos().getMinBlockX(), 90, context.chunkPos().getMinBlockZ());
        Rotation blockRotation = Rotation.getRandom(context.random());
        ConduitMonumentGenerator.addPieces(
                context.structureTemplateManager(),
                blockPos,
                blockRotation,
                collector,
                context.random(),
                this
        );
    }

    public StructureType<?> type() {
        return ModStructureTypes.CONDUIT_MONUMENT;
    }
}