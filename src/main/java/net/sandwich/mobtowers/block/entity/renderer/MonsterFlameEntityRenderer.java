package net.sandwich.mobtowers.block.entity.renderer;

import org.joml.Matrix4f;
import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
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
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.sandwich.mobtowers.MobTowersMod;
import net.sandwich.mobtowers.MobTowersModClient;
import net.sandwich.mobtowers.block.entity.MonsterFlameEntity;

public class MonsterFlameEntityRenderer implements BlockEntityRenderer<MonsterFlameEntity> {

	private final ModelPart flame;
	private final ModelPart jaw;
   	public static final Material FLAME_TEXTURE;
	public static final Material JAW_TEXTURE;


	static {
		FLAME_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "block/grimstone"));
		JAW_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "block/jaw"));
	}

	public MonsterFlameEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.flame = context.bakeLayer(MobTowersModClient.MF_FLAME);
		this.jaw = context.bakeLayer(MobTowersModClient.MF_JAW);
	}

	public static LayerDefinition createFlameLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("flame", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	public static LayerDefinition createLowerJawLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("lower_jaw", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.01F)), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void render(MonsterFlameEntity monsterFlameEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay) {
		float animTime = monsterFlameEntity.animationTime + pPartialTick;
		poseStack.pushPose();
		poseStack.scale(1.0f, 1.0f, 1.0f);
		poseStack.translate(0f, 0.5f + (animTime / 10), 0f);
		VertexConsumer jawconsumer = JAW_TEXTURE.buffer(bufferSource, RenderType::entitySolid);
		this.jaw.render(poseStack, jawconsumer, pPackedLight, pPackedOverlay);
		VertexConsumer flameconsumer = FLAME_TEXTURE.buffer(bufferSource, RenderType::entitySolid);
		this.flame.render(poseStack, flameconsumer, pPackedLight, pPackedOverlay);
		poseStack.popPose();
	}

	private int getLightLevel(Level level, BlockPos pos) {
		int bLight = level.getBrightness(LightLayer.BLOCK, pos);
		int sLight = level.getBrightness(LightLayer.SKY, pos);
		return LightTexture.pack(bLight, sLight);
	}
	
}
