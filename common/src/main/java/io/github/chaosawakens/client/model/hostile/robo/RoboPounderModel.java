package io.github.chaosawakens.client.model.hostile.robo;

import io.github.chaosawakens.CAConstants;
import io.github.chaosawakens.client.animation.baked.hostile.robo.robo_pounder.RoboPounderAOEAttackAnimation;
import io.github.chaosawakens.client.animation.baked.hostile.robo.robo_pounder.RoboPounderBaseAnimation;
import io.github.chaosawakens.client.animation.baked.hostile.robo.robo_pounder.RoboPounderMeleeAttackAnimation;
import io.github.chaosawakens.client.model.base.WrappedHierarchicalModel;
import io.github.chaosawakens.common.entity.hostile.robo.RoboPounder;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RoboPounderModel<RP extends RoboPounder> extends WrappedHierarchicalModel<RP> {
	public static final ModelLayerLocation BASE_LAYER = new ModelLayerLocation(CAConstants.prefix("robo_pounder"), "main");
	private final ModelPart root;
	private final ModelPart LowerBody;
	private final ModelPart RightLeg;
	private final ModelPart RightLeg2;
	private final ModelPart LeftLeg;
	private final ModelPart LeftLeg2;
	private final ModelPart UpperBody;
	private final ModelPart RightShoulder;
	private final ModelPart RightArm;
	private final ModelPart RightArm2;
	private final ModelPart RightHand;
	private final ModelPart FistAttack;
	private final ModelPart ArmSwing;
	private final ModelPart LeftShoulder;
	private final ModelPart LeftArm;
	private final ModelPart LeftArm2;
	private final ModelPart LeftHand;
	private final ModelPart FistAttack2;
	private final ModelPart ArmSwing2;
	private final ModelPart Head;
	private final ModelPart Head2;
	private final ModelPart RightHatch;
	private final ModelPart LittleDoor;
	private final ModelPart LittleDoor2;
	private final ModelPart PowerCells;
	private final ModelPart LeftHatch;
	private final ModelPart LittleDoor3;
	private final ModelPart LittleDoor4;
	private final ModelPart PowerCells2;
	private final ModelPart RightBackHatch;
	private final ModelPart HatchSegment1;
	private final ModelPart HatchSegment2;
	private final ModelPart LeftBackHatch;
	private final ModelPart HatchSegment3;
	private final ModelPart HatchSegment4;
	private final ModelPart GroundSlam;

	public RoboPounderModel(ModelPart root) {
		this.root = root;

		this.LowerBody = root.getChild("LowerBody");

		this.RightLeg = this.LowerBody.getChild("RightLeg");
		this.RightLeg2 = this.RightLeg.getChild("RightLeg2");

		this.LeftLeg = this.LowerBody.getChild("LeftLeg");
		this.LeftLeg2 = this.LeftLeg.getChild("LeftLeg2");

		this.UpperBody = this.LowerBody.getChild("UpperBody");

		this.RightShoulder = this.UpperBody.getChild("RightShoulder");

		this.RightArm = this.RightShoulder.getChild("RightArm");

		this.RightArm2 = this.RightArm.getChild("RightArm2");
		this.RightHand = this.RightArm2.getChild("RightHand");

		this.FistAttack = this.RightHand.getChild("FistAttack");
		this.ArmSwing = this.RightArm2.getChild("ArmSwing");

		this.LeftShoulder = this.UpperBody.getChild("LeftShoulder");

		this.LeftArm = this.LeftShoulder.getChild("LeftArm");
		this.LeftArm2 = this.LeftArm.getChild("LeftArm2");

		this.LeftHand = this.LeftArm2.getChild("LeftHand");

		this.FistAttack2 = this.LeftHand.getChild("FistAttack2");
		this.ArmSwing2 = this.LeftArm2.getChild("ArmSwing2");

		this.Head = this.UpperBody.getChild("Head");
		this.Head2 = this.Head.getChild("Head2");

		this.RightHatch = this.UpperBody.getChild("RightHatch");

		this.LittleDoor = this.RightHatch.getChild("LittleDoor");
		this.LittleDoor2 = this.RightHatch.getChild("LittleDoor2");

		this.PowerCells = this.RightHatch.getChild("PowerCells");

		this.LeftHatch = this.UpperBody.getChild("LeftHatch");

		this.LittleDoor3 = this.LeftHatch.getChild("LittleDoor3");
		this.LittleDoor4 = this.LeftHatch.getChild("LittleDoor4");

		this.PowerCells2 = this.LeftHatch.getChild("PowerCells2");

		this.RightBackHatch = this.UpperBody.getChild("RightBackHatch");

		this.HatchSegment1 = this.RightBackHatch.getChild("HatchSegment1");
		this.HatchSegment2 = this.RightBackHatch.getChild("HatchSegment2");

		this.LeftBackHatch = this.UpperBody.getChild("LeftBackHatch");

		this.HatchSegment3 = this.LeftBackHatch.getChild("HatchSegment3");
		this.HatchSegment4 = this.LeftBackHatch.getChild("HatchSegment4");

		this.GroundSlam = root.getChild("GroundSlam");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition rootPartDefinition = meshDefinition.getRoot();

		PartDefinition LowerBody = rootPartDefinition.addOrReplaceChild("LowerBody", CubeListBuilder.create(), PartPose.offset(0.0F, -28.0F, 1.0F));

		PartDefinition cube_r1 = LowerBody.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(267, 140).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 24.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition RightLeg = LowerBody.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-8.0F, 8.3431F, 1.1716F));

		PartDefinition cube_r2 = RightLeg.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(412, 277).addBox(-4.5F, -3.0F, -5.0F, 9.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 9.9497F, -15.6066F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r3 = RightLeg.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(50, 340).addBox(-5.0F, -16.0F, -13.0F, 11.0F, 28.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.6569F, -2.1716F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r4 = RightLeg.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 366).addBox(-5.0F, -7.0F, -8.0F, 10.0F, 18.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.4645F, -1.4645F, -0.7835F, -0.0617F, -0.0618F));

		PartDefinition cube_r5 = RightLeg.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(282, 420).addBox(-2.0F, -5.0F, -6.0F, 4.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.7102F, 4.5031F, -4.5031F, -0.7459F, -0.274F, -0.2849F));

		PartDefinition cube_r6 = RightLeg.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(364, 387).addBox(-5.05F, -8.0F, -6.0F, 10.0F, 16.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5229F, -2.762F, 2.762F, -0.7681F, 0.1841F, 0.1872F));

		PartDefinition RightLeg2 = RightLeg.addOrReplaceChild("RightLeg2", CubeListBuilder.create().texOffs(171, 321).addBox(-7.0F, -4.4006F, -7.3327F, 14.0F, 29.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(397, 404).addBox(-5.5F, 21.5994F, -5.8327F, 11.0F, 10.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0575F, -3.8389F));

		PartDefinition cube_r7 = RightLeg2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(402, 425).addBox(-4.75F, -6.5F, -4.0F, 10.0F, 13.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.5994F, -7.3327F, -0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r8 = RightLeg2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(114, 415).mirror().addBox(-3.0F, -4.0F, -8.5F, 5.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.884F, 25.5389F, -5.9645F, 0.0F, -0.2618F, 0.0F));

		PartDefinition cube_r9 = RightLeg2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(34, 431).mirror().addBox(-4.5F, -6.5694F, -10.5624F, 7.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.884F, 25.5389F, -5.9645F, -0.2182F, -0.2618F, 0.0F));

		PartDefinition cube_r10 = RightLeg2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(34, 431).addBox(-2.5F, -6.5694F, -10.5624F, 7.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.384F, 25.5389F, -5.9645F, -0.2182F, 0.2618F, 0.0F));

		PartDefinition cube_r11 = RightLeg2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(114, 415).addBox(-2.0F, -4.0F, -8.5F, 5.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.384F, 25.5389F, -5.9645F, 0.0F, 0.2618F, 0.0F));

		PartDefinition cube_r12 = RightLeg2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(192, 401).addBox(-6.0F, -6.5F, -3.5F, 12.0F, 9.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 25.5994F, 1.6673F, -0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r13 = RightLeg2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(436, 425).addBox(-4.5F, -10.0F, -3.5F, 9.0F, 20.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0868F, 14.4473F, 5.6548F, 0.7266F, -0.2553F, -0.2208F));

		PartDefinition cube_r14 = RightLeg2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(437, 149).addBox(-4.5F, -10.0F, -3.5F, 9.0F, 20.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0868F, 14.4473F, 5.6548F, 0.7266F, 0.2553F, 0.2208F));

		PartDefinition cube_r15 = RightLeg2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(107, 319).addBox(-8.5F, -7.0F, -8.0F, 17.0F, 20.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.2989F, -2.4108F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r16 = RightLeg2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(438, 318).addBox(-8.5F, -8.0F, -3.0F, 6.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, 8.4229F, -5.4738F, -0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r17 = RightLeg2.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(102, 0).addBox(-6.0F, -13.0F, -2.5F, 12.0F, 28.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.7678F, 5.3098F, 0.0873F, 0.0F, 0.0F));

		PartDefinition LeftLeg = LowerBody.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(8.0F, 8.3431F, 1.1716F));

		PartDefinition cube_r18 = LeftLeg.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(34, 412).addBox(-4.5F, -3.0F, -5.0F, 9.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 9.9497F, -15.6066F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r19 = LeftLeg.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(290, 331).addBox(-6.0F, -16.0F, -13.0F, 11.0F, 28.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.6569F, -2.1716F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r20 = LeftLeg.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(324, 365).addBox(-5.0F, -7.0F, -8.0F, 10.0F, 18.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.4645F, -1.4645F, -0.7835F, 0.0617F, 0.0618F));

		PartDefinition cube_r21 = LeftLeg.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(262, 408).addBox(-2.0F, -5.0F, -6.0F, 4.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.7102F, 4.5031F, -4.5031F, -0.7459F, 0.274F, 0.2849F));

		PartDefinition cube_r22 = LeftLeg.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(148, 387).addBox(-4.95F, -8.0F, -6.0F, 10.0F, 16.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5229F, -2.762F, 2.762F, -0.7681F, -0.1841F, -0.1872F));

		PartDefinition LeftLeg2 = LeftLeg.addOrReplaceChild("LeftLeg2", CubeListBuilder.create().texOffs(84, 51).addBox(-7.0F, -4.4006F, -7.3327F, 14.0F, 29.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(331, 404).addBox(-5.5F, 21.5994F, -5.8327F, 11.0F, 10.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0575F, -3.8389F));

		PartDefinition cube_r23 = LeftLeg2.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(368, 418).addBox(-5.25F, -6.5F, -4.0F, 10.0F, 13.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.5994F, -7.3327F, -0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r24 = LeftLeg2.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(224, 407).mirror().addBox(-2.0F, -4.0F, -8.5F, 5.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.884F, 25.5389F, -5.9645F, 0.0F, 0.2618F, 0.0F));

		PartDefinition cube_r25 = LeftLeg2.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(430, 296).mirror().addBox(-2.5F, -6.5694F, -10.5624F, 7.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.884F, 25.5389F, -5.9645F, -0.2182F, 0.2618F, 0.0F));

		PartDefinition cube_r26 = LeftLeg2.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(430, 296).addBox(-4.5F, -6.5694F, -10.5624F, 7.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.384F, 25.5389F, -5.9645F, -0.2182F, -0.2618F, 0.0F));

		PartDefinition cube_r27 = LeftLeg2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(224, 407).addBox(-3.0F, -4.0F, -8.5F, 5.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.384F, 25.5389F, -5.9645F, 0.0F, -0.2618F, 0.0F));

		PartDefinition cube_r28 = LeftLeg2.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(398, 336).addBox(-6.0F, -6.5F, -3.5F, 12.0F, 9.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 25.5994F, 1.6673F, -0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r29 = LeftLeg2.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(64, 435).addBox(-4.5F, -10.0F, -3.5F, 9.0F, 20.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0868F, 14.4473F, 5.6548F, 0.7266F, 0.2553F, 0.2208F));

		PartDefinition cube_r30 = LeftLeg2.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(90, 435).addBox(-4.5F, -10.0F, -3.5F, 9.0F, 20.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0868F, 14.4473F, 5.6548F, 0.7266F, -0.2553F, -0.2208F));

		PartDefinition cube_r31 = LeftLeg2.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 319).addBox(-8.5F, -7.0F, -8.0F, 17.0F, 20.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 17.2989F, -2.4108F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r32 = LeftLeg2.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(116, 438).addBox(2.5F, -8.0F, -3.0F, 6.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 8.4229F, -5.4738F, -0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r33 = LeftLeg2.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -13.0F, -2.5F, 12.0F, 28.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.7678F, 5.3098F, 0.0873F, 0.0F, 0.0F));

		PartDefinition UpperBody = LowerBody.addOrReplaceChild("UpperBody", CubeListBuilder.create(), PartPose.offset(0.0F, -6.6536F, -1.1601F));

		PartDefinition cube_r34 = UpperBody.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(152, 415).addBox(-9.0F, -13.0F, -9.0F, 8.0F, 20.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.3464F, 19.5449F, -0.6847F, -0.6591F, 0.4636F));

		PartDefinition cube_r35 = UpperBody.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(332, 198).addBox(-6.5F, -18.0F, -4.0F, 17.0F, 22.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.3892F, -18.9301F, 7.8662F, -0.5208F, -0.6178F, 0.7805F));

		PartDefinition cube_r36 = UpperBody.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(340, 331).addBox(-10.5F, -18.0F, -4.0F, 17.0F, 22.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(146, 184).addBox(-10.5F, -18.0F, -6.5F, 21.0F, 32.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.3892F, -18.9301F, 7.8662F, -0.5208F, 0.6178F, -0.7805F));

		PartDefinition cube_r37 = UpperBody.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(0, 189).addBox(-27.0F, -22.0F, -8.0F, 21.0F, 32.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.2635F, -6.1895F, 16.8618F, -0.5208F, -0.6178F, 0.7805F));

		PartDefinition cube_r38 = UpperBody.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(249, 249).addBox(-10.5F, -1.0F, -10.5F, 20.0F, 10.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.6895F, 0.5983F, 0.2444F, -0.7703F, -0.1719F));

		PartDefinition cube_r39 = UpperBody.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(0, 400).addBox(-12.0F, -8.5F, -18.0F, 16.0F, 13.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.3464F, 3.1601F, 0.4754F, -0.7268F, -0.3295F));

		PartDefinition cube_r40 = UpperBody.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(285, 400).addBox(-4.0F, -8.5F, -18.0F, 16.0F, 13.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-16.0F, -8.5F, -18.0F, 34.0F, 17.0F, 34.0F, new CubeDeformation(0.0F))
				.texOffs(50, 242).addBox(1.0F, -15.5F, -8.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -20.3464F, 3.1601F, 0.4754F, 0.7268F, 0.3295F));

		PartDefinition RightShoulder = UpperBody.addOrReplaceChild("RightShoulder", CubeListBuilder.create().texOffs(158, 364).addBox(-12.0F, -8.1036F, -7.0F, 18.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-15.0F, -22.2429F, 2.1601F));

		PartDefinition cube_r41 = RightShoulder.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(363, 286).addBox(-12.0F, -6.0F, -7.0F, 18.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.8964F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition RightArm = RightShoulder.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(100, 354).addBox(-8.25F, 12.75F, -9.5F, 10.0F, 12.0F, 19.0F, new CubeDeformation(0.0F))
				.texOffs(68, 249).addBox(-7.75F, 9.75F, -9.0F, 16.0F, 24.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(247, 357).addBox(-1.25F, 10.75F, -9.5F, 10.0F, 12.0F, 19.0F, new CubeDeformation(0.0F))
				.texOffs(128, 140).addBox(-11.75F, -12.25F, -11.0F, 22.0F, 22.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(-18.25F, 1.1464F, 0.0F));

		PartDefinition RightArm2 = RightArm.addOrReplaceChild("RightArm2", CubeListBuilder.create().texOffs(62, 162).addBox(-9.7091F, -3.6717F, -11.0F, 20.0F, 22.0F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(376, 365).addBox(-9.7091F, -13.6667F, -1.0F, 20.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0409F, 31.4167F, 0.0F));

		PartDefinition cube_r42 = RightArm2.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(54, 300).addBox(0.0F, -19.0F, 0.0F, 20.0F, 20.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(293, 236).addBox(0.0F, -4.0F, 3.0F, 4.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.7091F, 17.3333F, 0.0F, 0.0F, 0.1745F, 0.0F));

		PartDefinition cube_r43 = RightArm2.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(315, 140).addBox(-10.0F, -4.0F, -4.0F, 4.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(122, 285).addBox(-10.0F, -19.0F, -7.0F, 20.0F, 20.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6455F, 17.3333F, -5.1572F, 0.0F, -0.1745F, 0.0F));

		PartDefinition RightHand = RightArm2.addOrReplaceChild("RightHand", CubeListBuilder.create().texOffs(76, 211).addBox(-9.0318F, 7.495F, -11.0F, 20.0F, 16.0F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(386, 312).addBox(-5.0318F, 10.495F, -9.0F, 18.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(385, 182).addBox(-5.0318F, 10.495F, 1.0F, 18.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(178, 441).addBox(-2.0318F, -8.5F, 2.0F, 6.0F, 16.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(240, 388).addBox(-3.0318F, 6.5F, -9.0F, 8.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(437, 195).addBox(-2.0318F, -8.5F, -8.0F, 6.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.6773F, 10.8333F, 0.0F));

		PartDefinition cube_r44 = RightHand.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(244, 430).addBox(-11.0F, -4.0F, -4.0F, 5.0F, 18.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(317, 166).addBox(-10.0F, 1.0F, -7.0F, 20.0F, 18.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9682F, 6.5F, -5.1572F, 0.0F, -0.1745F, 0.0F));

		PartDefinition cube_r45 = RightHand.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(218, 430).addBox(-1.0F, -4.0F, 3.0F, 5.0F, 18.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(236, 313).addBox(0.0F, 1.0F, 0.0F, 20.0F, 18.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0318F, 6.5F, 0.0F, 0.0F, 0.1745F, 0.0F));

		PartDefinition FistAttack = RightHand.addOrReplaceChild("FistAttack", CubeListBuilder.create(), PartPose.offset(-5.3738F, 9.5F, 0.0868F));

		PartDefinition cube_r46 = FistAttack.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(145, 234).addBox(-12.0F, -15.0F, -7.65F, 16.0F, 36.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.4056F, -3.0F, -5.244F, 0.0F, -0.1745F, 0.0F));

		PartDefinition cube_r47 = FistAttack.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(274, 185).addBox(-2.0F, -14.995F, -0.35F, 14.0F, 36.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.658F, -3.0F, -0.0868F, 0.0F, 0.1745F, 0.0F));

		PartDefinition ArmSwing = RightArm2.addOrReplaceChild("ArmSwing", CubeListBuilder.create().texOffs(0, 43).addBox(0.0F, -34.0F, -1.0F, 0.0F, 34.0F, 42.0F, new CubeDeformation(0.0F)), PartPose.offset(0.2909F, 34.3383F, 12.0F));

		PartDefinition LeftShoulder = UpperBody.addOrReplaceChild("LeftShoulder", CubeListBuilder.create().texOffs(363, 254).addBox(-6.0F, -8.1036F, -7.0F, 18.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(15.0F, -22.2429F, 2.1601F));

		PartDefinition cube_r48 = LeftShoulder.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(331, 140).addBox(-6.0F, -6.0F, -7.0F, 18.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.8964F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition LeftArm = LeftShoulder.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(208, 345).addBox(-1.75F, 12.75F, -9.5F, 10.0F, 12.0F, 19.0F, new CubeDeformation(0.0F))
				.texOffs(0, 238).addBox(-8.25F, 9.75F, -9.0F, 16.0F, 24.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(318, 268).addBox(-8.75F, 10.75F, -9.5F, 10.0F, 12.0F, 19.0F, new CubeDeformation(0.0F))
				.texOffs(62, 118).addBox(-10.25F, -12.25F, -11.0F, 22.0F, 22.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(18.25F, 1.1464F, 0.0F));

		PartDefinition LeftArm2 = LeftArm.addOrReplaceChild("LeftArm2", CubeListBuilder.create().texOffs(0, 140).addBox(-10.2909F, -3.6717F, -11.0F, 20.0F, 22.0F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(194, 140).addBox(-10.2909F, -13.6667F, -1.0F, 20.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0409F, 31.4167F, 0.0F));

		PartDefinition cube_r49 = LeftArm2.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(250, 279).addBox(-20.0F, -19.0F, 0.0F, 20.0F, 20.0F, 14.0F, new CubeDeformation(0.0F))
				.texOffs(118, 249).addBox(-4.0F, -4.0F, 3.0F, 4.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.7091F, 17.3333F, 0.0F, 0.0F, -0.1745F, 0.0F));

		PartDefinition cube_r50 = LeftArm2.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(269, 236).addBox(6.0F, -4.0F, -4.0F, 4.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(0, 280).addBox(-10.0F, -19.0F, -7.0F, 20.0F, 20.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.6455F, 17.3333F, -5.1572F, 0.0F, 0.1745F, 0.0F));

		PartDefinition LeftHand = LeftArm2.addOrReplaceChild("LeftHand", CubeListBuilder.create().texOffs(205, 162).addBox(-10.9682F, 7.495F, -11.0F, 20.0F, 16.0F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(385, 158).addBox(-12.9682F, 10.495F, -9.0F, 18.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(84, 94).addBox(-12.9682F, 10.495F, 1.0F, 18.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(437, 173).addBox(-3.9682F, -8.5F, 2.0F, 6.0F, 16.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(287, 381).addBox(-4.9682F, 6.5F, -9.0F, 8.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(348, 432).addBox(-3.9682F, -8.5F, -8.0F, 6.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.6773F, 10.8333F, 0.0F));

		PartDefinition cube_r51 = LeftHand.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(427, 245).addBox(6.0F, -4.0F, -4.0F, 5.0F, 18.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(304, 299).addBox(-10.0F, 1.0F, -7.0F, 20.0F, 18.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9682F, 6.5F, -5.1572F, 0.0F, 0.1745F, 0.0F));

		PartDefinition cube_r52 = LeftHand.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(430, 110).addBox(-4.0F, -4.0F, 3.0F, 5.0F, 18.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(309, 236).addBox(-20.0F, 1.0F, 0.0F, 20.0F, 18.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0318F, 6.5F, 0.0F, 0.0F, -0.1745F, 0.0F));

		PartDefinition FistAttack2 = LeftHand.addOrReplaceChild("FistAttack2", CubeListBuilder.create(), PartPose.offset(5.3738F, 9.5F, 0.0868F));

		PartDefinition cube_r53 = FistAttack2.addOrReplaceChild("cube_r53", CubeListBuilder.create().texOffs(145, 234).mirror().addBox(-4.0F, -15.0F, -7.65F, 16.0F, 36.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.4056F, -3.0F, -5.244F, 0.0F, 0.1745F, 0.0F));

		PartDefinition cube_r54 = FistAttack2.addOrReplaceChild("cube_r54", CubeListBuilder.create().texOffs(274, 185).mirror().addBox(-12.0F, -14.995F, -0.35F, 14.0F, 36.0F, 15.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.658F, -3.0F, -0.0868F, 0.0F, -0.1745F, 0.0F));

		PartDefinition ArmSwing2 = LeftArm2.addOrReplaceChild("ArmSwing2", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(0.0F, -34.0F, -1.0F, 0.0F, 34.0F, 42.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.2909F, 34.3383F, 12.0F));

		PartDefinition Head = UpperBody.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -28.5964F, -6.4863F));

		PartDefinition cube_r55 = Head.addOrReplaceChild("cube_r55", CubeListBuilder.create().texOffs(390, 206).addBox(-5.5F, -5.0F, -5.5F, 12.0F, 11.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.75F, -0.3536F, 0.0F, -0.7854F, 0.0F));

		PartDefinition Head2 = Head.addOrReplaceChild("Head2", CubeListBuilder.create(), PartPose.offset(0.0F, -5.25F, -0.3536F));

		PartDefinition cube_r56 = Head2.addOrReplaceChild("cube_r56", CubeListBuilder.create().texOffs(210, 376).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 13.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition RightHatch = UpperBody.addOrReplaceChild("RightHatch", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.4246F, -17.7517F, -9.8165F, 0.4754F, 0.7268F, 0.3295F));

		PartDefinition LittleDoor = RightHatch.addOrReplaceChild("LittleDoor", CubeListBuilder.create().texOffs(372, 438).addBox(-3.0F, -6.5F, -3.5F, 6.0F, 13.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.0F, 0.0F));

		PartDefinition LittleDoor2 = RightHatch.addOrReplaceChild("LittleDoor2", CubeListBuilder.create().texOffs(314, 425).addBox(-5.0F, -6.5F, -3.5F, 10.0F, 13.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 0.0F, 0.0F));

		PartDefinition PowerCells = RightHatch.addOrReplaceChild("PowerCells", CubeListBuilder.create().texOffs(246, 200).addBox(-7.0F, -10.0F, 0.0F, 14.0F, 10.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(246, 210).addBox(1.0F, -2.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 194).addBox(1.5F, -8.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(254, 218).addBox(1.0F, -11.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(76, 222).addBox(-6.0F, -2.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(68, 291).addBox(-6.0F, -11.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 238).addBox(-5.5F, -8.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.5F, 0.0F));

		PartDefinition LeftHatch = UpperBody.addOrReplaceChild("LeftHatch", CubeListBuilder.create(), PartPose.offsetAndRotation(7.4246F, -17.7517F, -9.8165F, 0.4754F, -0.7268F, -0.3295F));

		PartDefinition LittleDoor3 = LeftHatch.addOrReplaceChild("LittleDoor3", CubeListBuilder.create().texOffs(428, 356).addBox(-3.0F, -6.5F, -3.5F, 6.0F, 13.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 0.0F, 0.0F));

		PartDefinition LittleDoor4 = LeftHatch.addOrReplaceChild("LittleDoor4", CubeListBuilder.create().texOffs(184, 421).addBox(-5.0F, -6.5F, -3.5F, 10.0F, 13.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, 0.0F));

		PartDefinition PowerCells2 = LeftHatch.addOrReplaceChild("PowerCells2", CubeListBuilder.create().texOffs(246, 140).addBox(-7.0F, -10.0F, 0.0F, 14.0F, 10.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(76, 214).addBox(-6.0F, -2.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(146, 184).addBox(-5.5F, -8.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(76, 206).addBox(-6.0F, -11.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 148).addBox(1.0F, -2.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 140).addBox(1.0F, -11.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(0, 184).addBox(1.5F, -8.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.5F, 0.0F));

		PartDefinition RightBackHatch = UpperBody.addOrReplaceChild("RightBackHatch", CubeListBuilder.create().texOffs(100, 385).addBox(-6.5F, -9.0F, -5.0F, 14.0F, 20.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(420, 88).addBox(-8.0F, -10.0F, 2.0F, 15.0F, 20.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(420, 66).addBox(-8.0F, -10.0F, -4.0F, 15.0F, 20.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(420, 44).addBox(-8.0F, -10.0F, -1.0F, 15.0F, 20.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.9725F, -23.517F, 13.2779F, -0.5208F, 0.6178F, -0.7805F));

		PartDefinition HatchSegment1 = RightBackHatch.addOrReplaceChild("HatchSegment1", CubeListBuilder.create().texOffs(378, 233).addBox(-17.0F, 0.0F, -5.5F, 17.0F, 9.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(8.5F, -11.0F, 0.0F));

		PartDefinition HatchSegment2 = RightBackHatch.addOrReplaceChild("HatchSegment2", CubeListBuilder.create().texOffs(223, 189).addBox(0.0F, -17.0F, -5.5F, 0.0F, 17.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.5F, 11.0F, 0.0F));

		PartDefinition LeftBackHatch = UpperBody.addOrReplaceChild("LeftBackHatch", CubeListBuilder.create().texOffs(52, 382).addBox(-7.5F, -9.0F, -5.0F, 14.0F, 20.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(420, 22).addBox(-7.0F, -10.0F, 2.0F, 15.0F, 20.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(420, 0).addBox(-7.0F, -10.0F, -4.0F, 15.0F, 20.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 420).addBox(-7.0F, -10.0F, -1.0F, 15.0F, 20.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.9725F, -23.517F, 13.2779F, -0.5208F, -0.6178F, 0.7805F));

		PartDefinition HatchSegment3 = LeftBackHatch.addOrReplaceChild("HatchSegment3", CubeListBuilder.create().texOffs(1, 120).addBox(0.0F, 0.0F, -5.5F, 17.0F, 9.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.5F, -11.0F, 0.0F));

		PartDefinition HatchSegment4 = LeftBackHatch.addOrReplaceChild("HatchSegment4", CubeListBuilder.create().texOffs(59, 108).addBox(0.0F, -17.0F, -5.5F, 0.0F, 17.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(8.5F, 11.0F, 0.0F));

		PartDefinition GroundSlam = rootPartDefinition.addOrReplaceChild("GroundSlam", CubeListBuilder.create().texOffs(60, 0).addBox(-40.0F, 0.0F, -40.0F, 80.0F, 0.0F, 80.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, -4.0F));

		return LayerDefinition.create(meshDefinition, 1024, 1024);
	}

	@Override
	public void setupAnim(RP pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

		// Base
		animate(pEntity.idleAnimState, RoboPounderBaseAnimation.IDLE, pAgeInTicks);
		animate(pEntity.deathAnimState, RoboPounderBaseAnimation.DEATH, pAgeInTicks);

		// Attack
		animate(pEntity.leftPistonPunchAttackAnim, RoboPounderMeleeAttackAnimation.PISTON_PUNCH_ATTACK_LEFT, pAgeInTicks);
		animate(pEntity.rightPistonPunchAttackAnim, RoboPounderMeleeAttackAnimation.PISTON_PUNCH_ATTACK_RIGHT, pAgeInTicks);

		animate(pEntity.leftSideSweepAttackAnim, RoboPounderMeleeAttackAnimation.SIDE_SWEEP_ATTACK_LEFT, pAgeInTicks);
		animate(pEntity.rightSideSweepAttackAnim, RoboPounderMeleeAttackAnimation.SIDE_SWEEP_ATTACK_RIGHT, pAgeInTicks);

		animate(pEntity.dysonDashAttackAnim, RoboPounderAOEAttackAnimation.DYSON_DASH_ATTACK, pAgeInTicks);

		animate(pEntity.leftDomeStompAttackAnim, RoboPounderAOEAttackAnimation.DOME_STOMP_ATTACK_LEFT, pAgeInTicks);
		animate(pEntity.rightDomeStompAttackAnim, RoboPounderAOEAttackAnimation.DOME_STOMP_ATTACK_RIGHT, pAgeInTicks);

		// Walk Cycle
		if (pEntity.isMoving() && !pEntity.isFunctionallyAnimatingAttack()) {
	//		animateWalk(RoboPounderBaseAnimation.WALK_BODY, pLimbSwing, pLimbSwingAmount, 1.0F, 1.0F);
	//		animateWalk(RoboPounderBaseAnimation.WALK_LEGS, pLimbSwing, pLimbSwingAmount, 1.0F, 1.0F);
		}
	}

	@Override
	public @NotNull ModelPart root() {
		return root;
	}

	@Override
	public @Nullable ModelPart head() {
		return Head;
	}
}