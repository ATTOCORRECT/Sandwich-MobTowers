package net.sandwich.mobtowers.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.sandwich.mobtowers.MobTowersMod;

public class ModItems {
	public static final DeferredRegister.Items ITEMS = 
		DeferredRegister.createItems(MobTowersMod.MOD_ID);

	// beans
	public static final DeferredItem<Item> BEANS = ITEMS.register(
		"beans", 
		() -> new Item(new Item.Properties())
	);

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
