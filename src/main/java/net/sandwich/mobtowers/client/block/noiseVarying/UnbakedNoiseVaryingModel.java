package net.sandwich.mobtowers.client.block.noiseVarying;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.NotNull;

public class UnbakedNoiseVaryingModel implements IUnbakedGeometry<UnbakedNoiseVaryingModel> {
	private final String[] importVariants;
	private final List<BlockModel> variants;

	public UnbakedNoiseVaryingModel(String[] variants) {
		this.importVariants = variants;
		this.variants = new ArrayList(this.importVariants.length);
	}

	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
		for(String variant : this.importVariants) {
			BlockModel checkedParent = resolveParent(modelGetter, variant);
			this.variants.add(checkedParent);
		}

	}

	private static @NotNull BlockModel resolveParent(Function<ResourceLocation, UnbakedModel> modelGetter, String variant) {
		Object var3 = modelGetter.apply(ResourceLocation.parse(variant));
		if (var3 instanceof BlockModel blockModel) {
			blockModel.resolveParents(modelGetter);
			return blockModel;
		} else {
			return (BlockModel)modelGetter.apply(ModelBakery.MISSING_MODEL_LOCATION);
		}
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
		BakedModel[] bakedVariants = new BakedModel[this.importVariants.length];

		for(int i = 0; i < bakedVariants.length; ++i) {
			BlockModel variant = (BlockModel)this.variants.get(i);
			
			bakedVariants[i] = variant.bake(baker, variant, spriteGetter, modelState, context.useBlockLight());
		}

		return new NoiseVaryingModel(bakedVariants);
	}
}
