package net.sandwich.mobtowers.block.entity;

import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sandwich.mobtowers.MobTowersMod;
import net.sandwich.mobtowers.block.ModBlocks;

public class ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MobTowersMod.MOD_ID);

	public static final Supplier<BlockEntityType<MonsterFlameEntity>> MONSTER_FLAME_BE = BLOCK_ENTITIES.register("monster_flame_be", () -> BlockEntityType.Builder.of(
		MonsterFlameEntity::new, ModBlocks.MONSTER_FLAME.get()).build(null));

	public static void register(IEventBus eventBus) {
		BLOCK_ENTITIES.register(eventBus);
	}
}
