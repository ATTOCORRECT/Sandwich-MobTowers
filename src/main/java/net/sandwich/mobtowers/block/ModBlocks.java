package net.sandwich.mobtowers.block;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sandwich.mobtowers.MobTowersMod;
import net.sandwich.mobtowers.block.custom.MonsterFlame;
import net.sandwich.mobtowers.block.custom.SeepingGrimstone;
import net.sandwich.mobtowers.block.custom.VoronoiBlock;
import net.sandwich.mobtowers.block.custom.states.SeepingActivation;
import net.sandwich.mobtowers.item.ModItems;
import net.sandwich.mobtowers.sound.ModSounds;

public class ModBlocks {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MobTowersMod.MOD_ID);

	public static final DeferredBlock<Block> VORONOI_BLOCK = registerBlock("voronoi_block",
		() -> new VoronoiBlock(BlockBehaviour.Properties.of()
			.strength(1)
			.sound(SoundType.AMETHYST)
	));

	public static final DeferredBlock<Block> NOISE_VARYING_BLOCK = registerBlock("noise_varying_block",
		() -> new Block(BlockBehaviour.Properties.of()
			.strength(1)
			.sound(SoundType.AMETHYST)
	));
	
	public static final DeferredBlock<Block> GRIMSTONE = registerBlock("grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
			.strength(3, 6)
			.requiresCorrectToolForDrops()
			.sound(ModSounds.GRIMSTONE_SOUNDS)
	));


	public static final DeferredBlock<Block> COBBLED_GRIMSTONE = registerBlock("cobbled_grimstone",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<SlabBlock> COBBLED_GRIMSTONE_SLAB = registerBlock("cobbled_grimstone_slab",
		() -> new SlabBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<StairBlock> COBBLED_GRIMSTONE_STAIRS = registerBlock("cobbled_grimstone_stairs",
		() -> new StairBlock(COBBLED_GRIMSTONE.get().defaultBlockState(), BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));


	public static final DeferredBlock<Block> PALE_GRIMSTONE = registerBlock("pale_grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> COBBLED_PALE_GRIMSTONE = registerBlock("cobbled_pale_grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> TEMPERED_GRIMSTONE = registerBlock("tempered_grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> COBBLED_TEMPERED_GRIMSTONE = registerBlock("cobbled_tempered_grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> SEEPING_GRIMSTONE = registerBlock("seeping_grimstone",
		() -> new SeepingGrimstone(BlockBehaviour.Properties.of()
			.strength(3, 6)
			.requiresCorrectToolForDrops()
			.sound(ModSounds.SEEPING_GRIMSTONE_SOUNDS)
			.lightLevel(state -> SeepingGrimstone.getLight(state))
			//.hasPostProcess(ModBlocks::always)
			.emissiveRendering((state, p_187413_, p_187414_) -> SeepingGrimstone.getActivation(state) != SeepingActivation.DORMANT)
	));

	public static final DeferredBlock<Block> MONSTER_FLAME = registerBlock("monster_flame",
		() -> new MonsterFlame(BlockBehaviour.Properties.of()
			.noOcclusion()
			.strength(-1.0F, 3600000.0F)
			.sound(SoundType.VAULT)
	));



	public static final DeferredBlock<StairBlock> GRIMSTONE_STAIRS = registerBlock("grimstone_stairs",
		() -> new StairBlock(GRIMSTONE.get().defaultBlockState(), BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<SlabBlock> GRIMSTONE_SLAB = registerBlock("grimstone_slab",
		() -> new SlabBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> GRIMSTONE_BRICKS = registerBlock("grimstone_bricks",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<WallBlock> GRIMSTONE_BRICK_WALL = registerBlock("grimstone_brick_wall",
		() -> new WallBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get()).forceSolidOn()
	));

	public static final DeferredBlock<StairBlock> GRIMSTONE_BRICK_STAIRS = registerBlock("grimstone_brick_stairs",
		() -> new StairBlock(GRIMSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<SlabBlock> GRIMSTONE_BRICK_SLAB = registerBlock("grimstone_brick_slab",
		() -> new SlabBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> POLISHED_GRIMSTONE = registerBlock("polished_grimstone",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<StairBlock> POLISHED_GRIMSTONE_STAIRS = registerBlock("polished_grimstone_stairs",
		() -> new StairBlock(POLISHED_GRIMSTONE.get().defaultBlockState(), BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<SlabBlock> POLISHED_GRIMSTONE_SLAB = registerBlock("polished_grimstone_slab",
		() -> new SlabBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> INDENTED_POLISHED_GRIMSTONE = registerBlock("indented_polished_grimstone",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> CHISELED_GRIMSTONE = registerBlock("chiseled_grimstone",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<RotatedPillarBlock> GRIMSTONE_PILLAR = registerBlock("grimstone_pillar",
		() -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
			.ofFullCopy(GRIMSTONE.get())
	));




	public static final DeferredBlock<SlabBlock> COBBLED_PALE_GRIMSTONE_SLAB = registerBlock("cobbled_pale_grimstone_slab",
		() -> new SlabBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<StairBlock> COBBLED_PALE_GRIMSTONE_STAIRS = registerBlock("cobbled_pale_grimstone_stairs",
		() -> new StairBlock(COBBLED_GRIMSTONE.get().defaultBlockState(), BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<StairBlock> PALE_GRIMSTONE_STAIRS = registerBlock("pale_grimstone_stairs",
		() -> new StairBlock(GRIMSTONE.get().defaultBlockState(), BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<SlabBlock> PALE_GRIMSTONE_SLAB = registerBlock("pale_grimstone_slab",
		() -> new SlabBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> PALE_GRIMSTONE_BRICKS = registerBlock("pale_grimstone_bricks",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<WallBlock> PALE_GRIMSTONE_BRICK_WALL = registerBlock("pale_grimstone_brick_wall",
		() -> new WallBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get()).forceSolidOn()
	));

	public static final DeferredBlock<StairBlock> PALE_GRIMSTONE_BRICK_STAIRS = registerBlock("pale_grimstone_brick_stairs",
		() -> new StairBlock(GRIMSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<SlabBlock> PALE_GRIMSTONE_BRICK_SLAB = registerBlock("pale_grimstone_brick_slab",
		() -> new SlabBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> PALE_POLISHED_GRIMSTONE = registerBlock("pale_polished_grimstone",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<StairBlock> PALE_POLISHED_GRIMSTONE_STAIRS = registerBlock("pale_polished_grimstone_stairs",
		() -> new StairBlock(POLISHED_GRIMSTONE.get().defaultBlockState(), BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<SlabBlock> PALE_POLISHED_GRIMSTONE_SLAB = registerBlock("pale_polished_grimstone_slab",
		() -> new SlabBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> PALE_INDENTED_POLISHED_GRIMSTONE = registerBlock("pale_indented_polished_grimstone",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> PALE_CHISELED_GRIMSTONE = registerBlock("pale_chiseled_grimstone",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<RotatedPillarBlock> PALE_GRIMSTONE_PILLAR = registerBlock("pale_grimstone_pillar",
		() -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
			.ofFullCopy(GRIMSTONE.get())
	));




	public static final DeferredBlock<SlabBlock> COBBLED_TEMPERED_GRIMSTONE_SLAB = registerBlock("cobbled_tempered_grimstone_slab",
		() -> new SlabBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<StairBlock> COBBLED_TEMPERED_GRIMSTONE_STAIRS = registerBlock("cobbled_tempered_grimstone_stairs",
		() -> new StairBlock(COBBLED_GRIMSTONE.get().defaultBlockState(), BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<StairBlock> TEMPERED_GRIMSTONE_STAIRS = registerBlock("tempered_grimstone_stairs",
		() -> new StairBlock(GRIMSTONE.get().defaultBlockState(), BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<SlabBlock> TEMPERED_GRIMSTONE_SLAB = registerBlock("tempered_grimstone_slab",
		() -> new SlabBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> TEMPERED_GRIMSTONE_BRICKS = registerBlock("tempered_grimstone_bricks",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<WallBlock> TEMPERED_GRIMSTONE_BRICK_WALL = registerBlock("tempered_grimstone_brick_wall",
		() -> new WallBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get()).forceSolidOn()
	));

	public static final DeferredBlock<StairBlock> TEMPERED_GRIMSTONE_BRICK_STAIRS = registerBlock("tempered_grimstone_brick_stairs",
		() -> new StairBlock(GRIMSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<SlabBlock> TEMPERED_GRIMSTONE_BRICK_SLAB = registerBlock("tempered_grimstone_brick_slab",
		() -> new SlabBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> TEMPERED_POLISHED_GRIMSTONE = registerBlock("tempered_polished_grimstone",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<StairBlock> TEMPERED_POLISHED_GRIMSTONE_STAIRS = registerBlock("tempered_polished_grimstone_stairs",
		() -> new StairBlock(POLISHED_GRIMSTONE.get().defaultBlockState(), BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<SlabBlock> TEMPERED_POLISHED_GRIMSTONE_SLAB = registerBlock("tempered_polished_grimstone_slab",
		() -> new SlabBlock(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> TEMPERED_INDENTED_POLISHED_GRIMSTONE = registerBlock("tempered_indented_polished_grimstone",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<Block> TEMPERED_CHISELED_GRIMSTONE = registerBlock("tempered_chiseled_grimstone",
		() -> new Block(BlockBehaviour.Properties
			.ofFullCopy(GRIMSTONE.get())
	));

	public static final DeferredBlock<RotatedPillarBlock> TEMPERED_GRIMSTONE_PILLAR = registerBlock("tempered_grimstone_pillar",
		() -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
			.ofFullCopy(GRIMSTONE.get())
	));

	private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
		DeferredBlock<T> toReturn = BLOCKS.register(name, block);
		registerBlockItem(name, toReturn);
		return toReturn;
	}

	private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block ) {
		ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}

	public static void register(IEventBus eventBus) {
		BLOCKS.register(eventBus);
	}

	private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
		return true;
	}

	// private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
	// 	return false;
	// }
}
