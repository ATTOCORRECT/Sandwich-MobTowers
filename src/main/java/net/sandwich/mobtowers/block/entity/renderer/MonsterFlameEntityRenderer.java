package net.sandwich.mobtowers.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.sandwich.mobtowers.block.entity.MonsterFlameEntity;

public class MonsterFlameEntityRenderer implements BlockEntityRenderer<MonsterFlameEntity> {


	public MonsterFlameEntityRenderer(BlockEntityRendererProvider.Context context) {

	}

	@Override
	public void render(MonsterFlameEntity monsterFlameEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, int pPackedOverlay) {
		
	}

	private int getLightLevel(Level level, BlockPos pos) {
		int bLight = level.getBrightness(LightLayer.BLOCK, pos);
		int sLight = level.getBrightness(LightLayer.SKY, pos);
		return LightTexture.pack(bLight, sLight);
	}
	
}
