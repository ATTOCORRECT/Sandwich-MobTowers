package net.sandwich.mobtowers.block.custom;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.sandwich.mobtowers.block.custom.states.SeepingActivation;

public class SeepingGrimstone extends Block {

	public static final EnumProperty<SeepingActivation> ACTIVATION = EnumProperty.create("activation", SeepingActivation.class);

	public SeepingGrimstone(Properties properties) {
		super(properties);
		this.registerDefaultState((BlockState)this.defaultBlockState().setValue(ACTIVATION, SeepingActivation.ACTIVE));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		// this is where the properties are actually added to the state
		builder.add(ACTIVATION);
	}
}
