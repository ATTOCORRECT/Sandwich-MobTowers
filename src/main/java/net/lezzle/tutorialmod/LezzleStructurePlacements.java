package net.lezzle.tutorialmod;

import com.mojang.serialization.MapCodec;

import net.lezzle.tutorialmod.structureplacement.VoronoiStructurePlacement;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class LezzleStructurePlacements {


	public static final DeferredRegister<StructurePlacementType<?>> DEFERRED_REGISTRY_STRUCTURE_PLACEMENT_TYPE = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, TutorialMod.MODID);
	public static final DeferredHolder<StructurePlacementType<?>, StructurePlacementType<VoronoiStructurePlacement>> VORONOI_STRUCTURE_PLACEMENT = DEFERRED_REGISTRY_STRUCTURE_PLACEMENT_TYPE.register("voronoi_structure_placement", () -> explicitStructureTypeTyping(VoronoiStructurePlacement.CODEC));

    private static <T extends StructurePlacement> StructurePlacementType<T> explicitStructureTypeTyping(MapCodec<T> structurePlacementTypeCodec) {
        return () -> structurePlacementTypeCodec;
    }
}
