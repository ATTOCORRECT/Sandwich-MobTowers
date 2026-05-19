package net.sandwich.mobtowers.block;

import java.util.function.Supplier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sandwich.mobtowers.MobTowersMod;
import net.sandwich.mobtowers.block.custom.MonsterFlame;
import net.sandwich.mobtowers.block.custom.VoronoiBlock;
import net.sandwich.mobtowers.item.ModItems;

public class ModBlocks {
	public static final DeferredRegister.Blocks BLOCKS = 
		DeferredRegister.createBlocks(MobTowersMod.MOD_ID);


	public static final DeferredBlock<Block> VORONOI_BLOCK = registerBlock("voronoi_block",
		() -> new VoronoiBlock(BlockBehaviour.Properties.of()
		.strength(1)
		.sound(SoundType.AMETHYST)
	));
	
	public static final DeferredBlock<Block> GRIMSTONE = registerBlock("grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
		.strength(3, 6)
		.requiresCorrectToolForDrops()
		.sound(SoundType.DEEPSLATE)
	));

	public static final DeferredBlock<Block> COBBLED_GRIMSTONE = registerBlock("cobbled_grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
		.strength(3, 6)
		.requiresCorrectToolForDrops()
		.sound(SoundType.DEEPSLATE)
	));

	public static final DeferredBlock<Block> PALE_GRIMSTONE = registerBlock("pale_grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
		.strength(3, 6)
		.requiresCorrectToolForDrops()
		.sound(SoundType.DEEPSLATE)
	));

	public static final DeferredBlock<Block> COBBLED_PALE_GRIMSTONE = registerBlock("cobbled_pale_grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
		.strength(3, 6)
		.requiresCorrectToolForDrops()
		.sound(SoundType.DEEPSLATE)
	));

	public static final DeferredBlock<Block> TEMPERED_GRIMSTONE = registerBlock("tempered_grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
		.strength(3, 6)
		.requiresCorrectToolForDrops()
		.sound(SoundType.DEEPSLATE)
	));

	public static final DeferredBlock<Block> COBBLED_TEMPERED_GRIMSTONE = registerBlock("cobbled_tempered_grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
		.strength(3, 6)
		.requiresCorrectToolForDrops()
		.sound(SoundType.DEEPSLATE)
	));

	public static final DeferredBlock<Block> SEEPING_GRIMSTONE = registerBlock("seeping_grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
		.strength(3, 6)
		.requiresCorrectToolForDrops()
		.sound(SoundType.DEEPSLATE)
	));

	public static final DeferredBlock<Block> SEEPING_TEMPERED_GRIMSTONE = registerBlock("seeping_tempered_grimstone",
		() -> new Block(BlockBehaviour.Properties.of()
		.strength(3, 6)
		.requiresCorrectToolForDrops()
		.sound(SoundType.DEEPSLATE)
	));

	public static final DeferredBlock<Block> MONSTER_FLAME = registerBlock("monster_flame",
		() -> new MonsterFlame(BlockBehaviour.Properties.of()
		.strength(1)
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
}
