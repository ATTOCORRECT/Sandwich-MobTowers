package net.sandwich.mobtowers.sound;

import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sandwich.mobtowers.MobTowersMod;

public class ModSounds {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = 
		DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MobTowersMod.MOD_ID);
	

	public static final Supplier<SoundEvent> GRIMSTONE_BREAK = registerSoundEvent("block.grimstone.break");
	public static final Supplier<SoundEvent> GRIMSTONE_STEP = registerSoundEvent("block.grimstone.step");
	public static final Supplier<SoundEvent> GRIMSTONE_PLACE = registerSoundEvent("block.grimstone.place");
	public static final Supplier<SoundEvent> GRIMSTONE_HIT = registerSoundEvent("block.grimstone.hit");
	public static final Supplier<SoundEvent> GRIMSTONE_FALL = registerSoundEvent("block.grimstone.fall");

	public static final DeferredSoundType GRIMSTONE_SOUNDS = new DeferredSoundType(1, 1, GRIMSTONE_BREAK, GRIMSTONE_STEP, GRIMSTONE_PLACE, GRIMSTONE_HIT, GRIMSTONE_FALL);

	public static final Supplier<SoundEvent> SEEPING_GRIMSTONE_BREAK = registerSoundEvent("block.seeping_grimstone.break");
	public static final Supplier<SoundEvent> SEEPING_GRIMSTONE_STEP = registerSoundEvent("block.seeping_grimstone.step");
	public static final Supplier<SoundEvent> SEEPING_GRIMSTONE_PLACE = registerSoundEvent("block.seeping_grimstone.place");
	public static final Supplier<SoundEvent> SEEPING_GRIMSTONE_HIT = registerSoundEvent("block.seeping_grimstone.hit");
	public static final Supplier<SoundEvent> SEEPING_GRIMSTONE_FALL = registerSoundEvent("block.seeping_grimstone.fall");

	public static final DeferredSoundType SEEPING_GRIMSTONE_SOUNDS = new DeferredSoundType(1, 1, SEEPING_GRIMSTONE_BREAK, SEEPING_GRIMSTONE_STEP, SEEPING_GRIMSTONE_PLACE, SEEPING_GRIMSTONE_HIT, SEEPING_GRIMSTONE_FALL);

	public static final Supplier<SoundEvent> MONSTER_FLAME_DISABLE = registerSoundEvent("block.monster_flame.disable");
	public static final Supplier<SoundEvent> MONSTER_FLAME_ENABLE = registerSoundEvent("block.monster_flame.enable");

	public static final Supplier<SoundEvent> MONSTER_FLAME_GROWL = registerSoundEvent("block.monster_flame.growl");
	public static final Supplier<SoundEvent> DISTANT_MOB_SPAWN = registerSoundEvent("event.distant_mob_spawn");

	
	private static Supplier<SoundEvent> registerSoundEvent(String name) {
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, name);
		return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
	}
	
	public static void register(IEventBus eventBus) {
		SOUND_EVENTS.register(eventBus);
	}
}
