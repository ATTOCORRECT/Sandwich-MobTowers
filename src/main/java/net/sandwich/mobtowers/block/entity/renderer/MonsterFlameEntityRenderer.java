package net.sandwich.mobtowers.block.entity.renderer;

import org.joml.Matrix4f;
import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.sandwich.mobtowers.MobTowersMod;
import net.sandwich.mobtowers.block.entity.MonsterFlameEntity;

public class MonsterFlameEntityRenderer implements BlockEntityRenderer<MonsterFlameEntity> {


	public static final ResourceLocation LIGHT = ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "block/grimstone");

	@Override
	public void render(MonsterFlameEntity monsterFlameEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay) {
		poseStack.pushPose();
		poseStack.scale(1.0f, 1.0f, 1.0f);
		poseStack.translate(0f, 0.5f, 0f);
		renderBillboardQuadBright(poseStack, bufferSource.getBuffer(RenderType.solid()), 2.0f, LIGHT);
		System.out.println("Yep, I rendered");
		poseStack.popPose();
	}

	private int getLightLevel(Level level, BlockPos pos) {
		int bLight = level.getBrightness(LightLayer.BLOCK, pos);
		int sLight = level.getBrightness(LightLayer.SKY, pos);
		return LightTexture.pack(bLight, sLight);
	}

	private static void renderBillboardQuadBright(PoseStack matrixStack, VertexConsumer builder, float scale, ResourceLocation texture) {
		int b1 = LightTexture.FULL_BRIGHT >> 16 & 65535;
		int b2 = LightTexture.FULL_BRIGHT & 65535;
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
		matrixStack.pushPose();
		matrixStack.translate(0.5, 0.95, 0.5);
		Quaternionf rotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
		matrixStack.mulPose(rotation);
		Matrix4f matrix = matrixStack.last().pose();
		builder.addVertex(matrix, -scale, -scale, 0.0f).setColor(255, 255, 255, 255).setUv(sprite.getU0(), sprite.getV0()).setUv2(b1, b2).setNormal(1, 0, 0);
		builder.addVertex(matrix, -scale, scale, 0.0f).setColor(255, 255, 255, 255).setUv(sprite.getU0(), sprite.getV1()).setUv2(b1, b2).setNormal(1, 0, 0);
		builder.addVertex(matrix, scale, scale, 0.0f).setColor(255, 255, 255, 255).setUv(sprite.getU1(), sprite.getV1()).setUv2(b1, b2).setNormal(1, 0, 0);
		builder.addVertex(matrix, scale, -scale, 0.0f).setColor(255, 255, 255, 255).setUv(sprite.getU1(), sprite.getV0()).setUv2(b1, b2).setNormal(1, 0, 0);
		matrixStack.popPose();
	}
	
}
