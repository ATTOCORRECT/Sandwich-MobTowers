package net.sandwich.mobtowers.particle;

import java.util.function.Supplier;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sandwich.mobtowers.MobTowersMod;

public class ModParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
		DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, MobTowersMod.MOD_ID);


	public static void register(IEventBus eventBus) {
		PARTICLE_TYPES.register(eventBus);
	}

	public static final Supplier<SimpleParticleType> TOWER_FLAME = PARTICLE_TYPES.register(
		"tower_flame", 
		() -> new SimpleParticleType(true)
	);

	public static final Supplier<SimpleParticleType> MONSTER_SPAWN_FLAME = PARTICLE_TYPES.register(
		"monster_spawn_flame", 
		() -> new SimpleParticleType(true)
	);
}
