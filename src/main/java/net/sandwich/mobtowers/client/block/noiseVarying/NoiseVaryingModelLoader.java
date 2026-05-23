package net.sandwich.mobtowers.client.block.noiseVarying;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.ArrayList;
import java.util.List;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class NoiseVaryingModelLoader implements IGeometryLoader<UnbakedNoiseVaryingModel> {
	public static final NoiseVaryingModelLoader INSTANCE = new NoiseVaryingModelLoader();

	private NoiseVaryingModelLoader() {
	}

	public UnbakedNoiseVaryingModel read(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
		List<String> builder = new ArrayList();

		for(JsonElement entry : json.getAsJsonArray("variants")) {
			builder.add(entry.getAsString());
		}

		return new UnbakedNoiseVaryingModel((String[])builder.toArray((x$0) -> new String[x$0]));
	}
}