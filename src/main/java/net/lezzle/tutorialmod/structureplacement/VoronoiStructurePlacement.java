package net.lezzle.tutorialmod.structureplacement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.lezzle.tutorialmod.LezzleStructurePlacements;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.Optional;

public class VoronoiStructurePlacement extends RandomSpreadStructurePlacement {

	// Special codec where we tacked on a "min_distance_from_world_origin" field so
	// we can now have structures spawn based on distance from world center.
	public static final MapCodec<VoronoiStructurePlacement> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
			Vec3i.offsetCodec(16).optionalFieldOf("locate_offset", Vec3i.ZERO).forGetter(VoronoiStructurePlacement::locateOffset),
			StructurePlacement.FrequencyReductionMethod.CODEC.optionalFieldOf("frequency_reduction_method", StructurePlacement.FrequencyReductionMethod.DEFAULT).forGetter(VoronoiStructurePlacement::frequencyReductionMethod),
			Codec.floatRange(0.0F, 1.0F).optionalFieldOf("frequency", 1.0F).forGetter(VoronoiStructurePlacement::frequency),
			ExtraCodecs.NON_NEGATIVE_INT.fieldOf("salt").forGetter(VoronoiStructurePlacement::salt),
			StructurePlacement.ExclusionZone.CODEC.optionalFieldOf("exclusion_zone").forGetter(VoronoiStructurePlacement::exclusionZone),
			Codec.intRange(0, Integer.MAX_VALUE).fieldOf("spacing").forGetter(VoronoiStructurePlacement::spacing),
			Codec.intRange(0, Integer.MAX_VALUE).fieldOf("separation").forGetter(VoronoiStructurePlacement::separation),
			RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(VoronoiStructurePlacement::spreadType),
			Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("min_distance_from_world_origin").forGetter(VoronoiStructurePlacement::minDistanceFromWorldOrigin)
	).apply(instance, instance.stable(VoronoiStructurePlacement::new)));

	private final Optional<Integer> minDistanceFromWorldOrigin;

	public VoronoiStructurePlacement(Vec3i locationOffset,
								StructurePlacement.FrequencyReductionMethod frequencyReductionMethod,
								float frequency,
								int salt,
								Optional<ExclusionZone> exclusionZone,
								int spacing,
								int separation,
								RandomSpreadType spreadType,
								Optional<Integer> minDistanceFromWorldOrigin
	) {
		super(locationOffset, frequencyReductionMethod, frequency, salt, exclusionZone, spacing, separation, spreadType);
		this.minDistanceFromWorldOrigin = minDistanceFromWorldOrigin;

		// Helpful validation to ensure that spacing value is always greater than separation value
		if (spacing <= separation) {
			throw new RuntimeException("""
				Spacing cannot be less or equal to separation.
				Please correct this error as there's no way to spawn this structure properly
					Spacing: %s
					Separation: %s.
			""".formatted(spacing, separation));
		}
	}

	public Optional<Integer> minDistanceFromWorldOrigin() {
		return this.minDistanceFromWorldOrigin;
	}

	// Override this method to add coordinate checking.
	// The x and z here is in chunk positions.
	// What we do is we check if the structure is too close to world center and if so, return false.
	// Otherwise, if far enough away, run the normal structure position choosing code.
	// When this returns true, the structure's type class will be called to see if the structure layout can be made.
	@Override
	protected boolean isPlacementChunk(ChunkGeneratorStructureState chunkGeneratorStructureState, int x, int z) {

		boolean xm = ((x % 16) == 0);
		boolean zm = ((z % 16) == 0);

		ChunkPos chunkpos = this.getPotentialStructureChunk(chunkGeneratorStructureState.getLevelSeed(), x, z);
		return xm && zm;
	}

	@Override
	public StructurePlacementType<?> type() {
		return LezzleStructurePlacements.VORONOI_STRUCTURE_PLACEMENT.get();
	}

}
