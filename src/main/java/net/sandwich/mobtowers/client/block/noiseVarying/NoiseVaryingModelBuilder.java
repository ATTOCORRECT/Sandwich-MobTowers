package net.sandwich.mobtowers.client.block.noiseVarying;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.sandwich.mobtowers.MobTowersMod;

public class NoiseVaryingModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
	private final List<T> variants = new ArrayList();

	public NoiseVaryingModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
		super(
		ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "noise_varying".toLowerCase(Locale.ROOT)), 
		parent, existingFileHelper, 
		false
		);
	}

	public NoiseVaryingModelBuilder<T> add(T builder) {
		builder.assertExistence();
		this.variants.add(builder);
		return this;
	}

	public NoiseVaryingModelBuilder<T> addAll(T[] builders) {
		Arrays.stream(builders).forEach(this::add);
		return this;
	}

	public T end() {
		Preconditions.checkArgument(!this.variants.isEmpty(), "Noise Varying builder cannot have zero variants.");
		return (T)super.end();
	}

	public JsonObject toJson(JsonObject json) {
		JsonObject mainJson = super.toJson(json);
		JsonArray variantsArray = new JsonArray();

		for (T variant : this.variants) {
			String locationString = variant.getLocation().toString();
			variantsArray.add(locationString);
		}

		mainJson.add("variants", variantsArray);
		return mainJson;
	}
}
