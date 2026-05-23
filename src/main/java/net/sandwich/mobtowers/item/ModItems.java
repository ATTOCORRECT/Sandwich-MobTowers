package net.sandwich.mobtowers.item;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sandwich.mobtowers.MobTowersMod;

public class ModItems {
	public static final DeferredRegister.Items ITEMS = 
		DeferredRegister.createItems(MobTowersMod.MOD_ID);

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
