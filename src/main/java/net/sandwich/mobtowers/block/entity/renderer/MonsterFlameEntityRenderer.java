package net.sandwich.mobtowers.block.entity.renderer;

import java.awt.Color;

import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
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
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.sandwich.mobtowers.MobTowersMod;
import net.sandwich.mobtowers.MobTowersModClient;
import net.sandwich.mobtowers.Utils;
import net.sandwich.mobtowers.block.entity.MonsterFlame.MonsterFlameAnimationState;
import net.sandwich.mobtowers.block.entity.MonsterFlame.MonsterFlameEntity;

public class MonsterFlameEntityRenderer implements BlockEntityRenderer<MonsterFlameEntity> {

	private final ModelPart flame;
	private final ModelPart lower_jaw;
	private final ModelPart upper_jaw;
	public static final Material FLAME_TEXTURE;
	public static final Material FLAME_OVERLAY_TEXTURE;
	public static final Material JAW_TEXTURE;
	public static final Material JAW_EMISSIVE_TEXTURE;
	private final BlockEntityRenderDispatcher renderer;


	static {
		FLAME_OVERLAY_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "block/flame_overlay"));
		FLAME_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "block/flame"));
		JAW_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "block/jaw"));
		JAW_EMISSIVE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(MobTowersMod.MOD_ID, "block/jaw_emissive"));
		// Can we call it the cage texture if we use one texture for both halves?
	}

	public MonsterFlameEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.renderer = context.getBlockEntityRenderDispatcher();
		this.flame = context.bakeLayer(MobTowersModClient.MF_FLAME);
		this.lower_jaw = context.bakeLayer(MobTowersModClient.MF_LOWER_JAW);
		this.upper_jaw = context.bakeLayer(MobTowersModClient.MF_UPPER_JAW);
	}

	public static LayerDefinition createFlameLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("flame", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 0.0F), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	public static LayerDefinition createUpperJawLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("upper_jaw", CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.01F)), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public static LayerDefinition createLowerJawLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("lower_jaw", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.01F)), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void render(MonsterFlameEntity monsterFlameEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay) {
		
		boolean flameActive = (
			monsterFlameEntity.animationState != MonsterFlameAnimationState.REVERBERATING && 
			monsterFlameEntity.animationState != MonsterFlameAnimationState.STOPPED
		);

		int tint = monsterFlameEntity.tintColor;
		
		float flameSize = Utils.lerp(monsterFlameEntity.flameSizeOld, monsterFlameEntity.flameSize, pPartialTick);
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

		VertexConsumer jawconsumer = JAW_TEXTURE.buffer(bufferSource, RenderType::entityCutoutNoCull);
		this.upper_jaw.render(poseStack, jawconsumer, pPackedLight, pPackedOverlay);
		VertexConsumer jawconsumer_emit = JAW_EMISSIVE_TEXTURE.buffer(bufferSource, RenderType::entityTranslucentEmissive);
		if (flameActive) this.upper_jaw.render(poseStack, jawconsumer_emit, pPackedLight, pPackedOverlay, tint);
		poseStack.popPose(); // pop top jaw

		poseStack.pushPose(); // push bottom jaw
		poseStack.translate(0f, -1f * open , 0f);

		poseStack.translate(0.5f, 0.5f, 0.5f);
		poseStack.mulPose(new Quaternionf(new AxisAngle4f(-spin, 0f, 1f, 0f)));
		poseStack.translate(-0.5f, -0.5f, -0.5f);
		
		VertexConsumer jawconsumer2 = JAW_TEXTURE.buffer(bufferSource, RenderType::entityCutoutNoCull);
		this.lower_jaw.render(poseStack, jawconsumer2, pPackedLight, pPackedOverlay);
		VertexConsumer jawconsumer2_emit = JAW_EMISSIVE_TEXTURE.buffer(bufferSource, RenderType::entityTranslucentEmissive);
		if (flameActive) this.lower_jaw.render(poseStack, jawconsumer2_emit, pPackedLight, pPackedOverlay, tint);
		poseStack.popPose(); // pop bottom jaw

		poseStack.popPose(); // pop cage

		poseStack.pushPose(); // push flame

		Camera camera = this.renderer.camera;
		poseStack.translate(0.5F, 0.5F, 0.5F);
		poseStack.mulPose(new Quaternionf(new AxisAngle4f(-camera.getYRot() * ((float)Math.PI / 180F), 0f, 1f, 0f)));
		// poseStack.mulPose(new Quaternionf(new AxisAngle4f((float)Math.PI, 0f, 0f, 1f)));
		// poseStack.mulPose((new Quaternionf()).rotationYXZ(f3 * ((float)Math.PI / 180F), camera.getXRot() * ((float)Math.PI / 180F), (float)Math.PI));
		flameSize = (flameSize + 1) / 2;
		poseStack.scale(flameSize*2, flameSize*2, flameSize*2);
		poseStack.translate(-0.5F, -0.25F, 0F);
		// poseStack.translate(-0.5f, -0.5f, -0.5f);

		PoseStack.Pose posestack$pose = poseStack.last();

		VertexConsumer flameconsumer = FLAME_TEXTURE.buffer(bufferSource, RenderType::entityTranslucent);
		VertexConsumer flameoverlayconsumer = FLAME_OVERLAY_TEXTURE.buffer(bufferSource, RenderType::entityTranslucent);

		if (flameActive) {
			renderQuad(posestack$pose, flameconsumer, tint, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1);
			renderQuad(posestack$pose, flameoverlayconsumer, 0xFFFFFFFF, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1);
		}
		// this.flame.render(poseStack, flameconsumer, pPackedLight, pPackedOverlay);
		
		poseStack.popPose(); // push flame
	}

	private int getLightLevel(Level level, BlockPos pos) {
		int bLight = level.getBrightness(LightLayer.BLOCK, pos);
		int sLight = level.getBrightness(LightLayer.SKY, pos);
		return LightTexture.pack(bLight, sLight);
	}


	private static void renderQuad(PoseStack.Pose pose, VertexConsumer consumer, int color, int minY, int maxY, float minX, float minZ, float maxX, float maxZ, float minU, float maxU, float minV, float maxV) {
		addVertex(pose, consumer, color, maxY, minX, minZ, maxU, minV);
		addVertex(pose, consumer, color, minY, minX, minZ, maxU, maxV);
		addVertex(pose, consumer, color, minY, maxX, maxZ, minU, maxV);
		addVertex(pose, consumer, color, maxY, maxX, maxZ, minU, minV);
	}

	private static void addVertex(PoseStack.Pose pose, VertexConsumer consumer, int color, int y, float x, float z, float u, float v) {
		consumer.addVertex(pose, x, (float)y, z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(pose, 0.0F, 1.0F, 0.0F);
	}

	
}
