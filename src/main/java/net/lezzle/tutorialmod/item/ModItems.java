package net.lezzle.tutorialmod.item;

import net.lezzle.tutorialmod.TutorialMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TutorialMod.MODID);

	public static final DeferredItem<Item> BLOOBER = ITEMS.register("bloober", 
	() -> new Item(new Item.Properties()));

	
	
	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}
}
