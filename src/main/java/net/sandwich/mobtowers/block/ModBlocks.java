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
