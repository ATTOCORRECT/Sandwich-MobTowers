package net.sandwich.mobtowers.block;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.SoundType;
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
