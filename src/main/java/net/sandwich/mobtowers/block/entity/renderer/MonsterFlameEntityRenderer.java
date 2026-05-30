package net.sandwich.mobtowers.block.entity.renderer;

import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

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
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.sandwich.mobtowers.MobTowersMod;
import net.sandwich.mobtowers.MobTowersModClient;
import net.sandwich.mobtowers.Utils;
import net.sandwich.mobtowers.block.entity.MonsterFlame.MonsterFlameEntity;

public class MonsterFlameEntityRenderer implements BlockEntityRenderer<MonsterFlameEntity> {

	private final ModelPart flame;
	private final ModelPart jaw;
	public static final Material FLAME_TEXTURE;
	public static final Material JAW_TEXTURE;


	static {
		FLAME_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "block/grimstone"));
		JAW_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "block/jaw"));
		// Can we call it the cage texture if we use one texture for both halves?
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
		float open = Utils.lerp(monsterFlameEntity.openOld, monsterFlameEntity.open, pPartialTick);
		float pitch = Utils.lerpAngle(monsterFlameEntity.pitchOld, monsterFlameEntity.pitch, pPartialTick);
		float spin = Utils.lerpAngle(monsterFlameEntity.spinOld, monsterFlameEntity.spin, pPartialTick);
		Vector3f shake = Utils.lerp(monsterFlameEntity.shakeOld, monsterFlameEntity.shake, pPartialTick);

		poseStack.pushPose(); // push cage

		poseStack.translate(shake.x, shake.y, shake.z);

		// cage rotation
		poseStack.translate(0.5f, 0.5f, 0.5f);
		poseStack.mulPose(new Quaternionf(new AxisAngle4f(spin, 0f, 1f, 0f))); // YAW
		poseStack.mulPose(new Quaternionf(new AxisAngle4f(pitch, 0f, 0f, 1f))); // PITCH
		poseStack.translate(-0.5f, -0.5f, -0.5f);
		
		poseStack.pushPose(); // push top jaw
		poseStack.translate(0f, 1f * open , 0f);

		poseStack.translate(0.5f, 0.5f, 0.5f);
		poseStack.mulPose(new Quaternionf(new AxisAngle4f(spin, 0f, 1f, 0f)));
		poseStack.translate(-0.5f, -0.5f, -0.5f);

		VertexConsumer jawconsumer = JAW_TEXTURE.buffer(bufferSource, RenderType::entitySolid);
		this.jaw.render(poseStack, jawconsumer, pPackedLight, pPackedOverlay);
		poseStack.popPose(); // pop top jaw

		poseStack.pushPose(); // push bottom jaw
		poseStack.translate(0f, -1f * open , 0f);

		poseStack.translate(0.5f, 0.5f, 0.5f);
		poseStack.mulPose(new Quaternionf(new AxisAngle4f(-spin, 0f, 1f, 0f)));
		poseStack.translate(-0.5f, -0.5f, -0.5f);
		
		VertexConsumer jawconsumer2 = JAW_TEXTURE.buffer(bufferSource, RenderType::entitySolid);
		this.jaw.render(poseStack, jawconsumer2, pPackedLight, pPackedOverlay);
		poseStack.popPose(); // pop bottom jaw

		poseStack.popPose(); // pop cage

		poseStack.pushPose(); // push flameflame
		VertexConsumer flameconsumer = FLAME_TEXTURE.buffer(bufferSource, RenderType::entitySolid);
		this.flame.render(poseStack, flameconsumer, pPackedLight, pPackedOverlay);
	}

	private int getLightLevel(Level level, BlockPos pos) {
		int bLight = level.getBrightness(LightLayer.BLOCK, pos);
		int sLight = level.getBrightness(LightLayer.SKY, pos);
		return LightTexture.pack(bLight, sLight);
	}
	
}
