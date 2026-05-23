package net.sandwich.mobtowers.client.block.noiseVarying;

import java.util.List;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

import org.jetbrains.annotations.Nullable;

public class NoiseVaryingModel extends BakedModelWrapper<BakedModel> {

	private static final ModelProperty<Integer> VARIANT = new ModelProperty<>();
	private final BakedModel[] variants;

	public NoiseVaryingModel(BakedModel[] variants) {
		super(variants[0]);
		this.variants = variants;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
		if (extraData.has(VARIANT)) {
			int variant = (Integer)extraData.get(VARIANT);
			return this.variants[variant].getQuads(state, side, rand, extraData, renderType);
		} else {
			return super.getQuads(state, side, rand, extraData, renderType);
		}
	}

	@Override
	public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
		return modelData.has(VARIANT) ? modelData : modelData.derive().with(VARIANT, calculateNoise(pos, this.variants.length)).build();
	}

	private int calculateNoise(BlockPos pos, int maxVariants) {
		int val = Math.abs(pos.getX() + pos.getY() + pos.getZ());
		return val % maxVariants; 
	}
}
