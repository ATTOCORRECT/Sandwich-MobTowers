package net.sandwich.mobtowers.block.custom.states;

import net.minecraft.util.StringRepresentable;

public enum SeepingActivation implements StringRepresentable {
	ACTIVE("active"),
	MID("mid"),
	DORMANT("dormant");

	private final String name;

	private SeepingActivation(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}