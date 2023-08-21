package io.github.chaosawakens.api.animation;

import java.util.ArrayList;

import io.github.chaosawakens.common.network.packets.s2c.AnimationFunctionalProgressPacket;
import io.github.chaosawakens.manager.CANetworkManager;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.keyframe.BoneAnimation;

public class WrappedAnimationController<E extends IAnimatableEntity> {
	protected E animatable;
	protected String name;
	protected ExpandedAnimationState animationState = ExpandedAnimationState.FINISHED;	
	protected Animation currentAnimation = none();
	protected double transitionLength;
	protected double transitionProgress = 0;
	protected double animationLength;
	protected double animationProgress = 0;
	protected double animSpeedMultiplier = 1.0D;
	protected final AnimationController<E> controller;
	protected MinecraftServer server;
	
	public WrappedAnimationController(E animatable, int transitionTicks, AnimationController<E> controller) {
		this.animatable = animatable;
		this.transitionLength = transitionTicks;
		this.controller = controller;
		this.name = controller.getName();
		this.server = ((Entity) animatable).getServer();
	}
	
	public WrappedAnimationController(E animatable, AnimationController<E> controller) {
		this(animatable, animatable.animationInterval(), controller);
		this.name = controller.getName();
		this.server = ((Entity) animatable).getServer();
	}
	
	public void tick() {
		double tickProgressDelta = getSyncedProgress();
		
		switch (animationState) {
		case TRANSITIONING:
			if (this.transitionProgress >= this.transitionLength) {
				this.transitionProgress = 0;
				this.animationState = ExpandedAnimationState.RUNNING;
			} else {
				this.transitionProgress += tickProgressDelta;
			}
			break;
		case RUNNING:
			if (this.animationProgress >= this.animationLength) {
				this.animationProgress = 0;
				if (this.currentAnimation.loop == EDefaultLoopTypes.LOOP) {
					this.animationProgress = 0;
					this.animationState = ExpandedAnimationState.TRANSITIONING;
				} else {
					this.animationState = ExpandedAnimationState.FINISHED;
				}
			} else {
				this.animationProgress += tickProgressDelta;
			}
			break;
		case STOPPED:
			break;
		case FINISHED:
			break;
		}
	}
	
	public void playAnimation(IAnimationBuilder builder, boolean clearCache) {		
		if (builder != null && !getCurrentAnimation().animationName.equals(builder.getAnimationName()) || clearCache) {
			builder.playAnimation(clearCache);
			
			this.animationProgress = 0;
			this.animationLength = builder.getAnimation().animationLength;
			this.transitionProgress = 0;
			this.animationState = ExpandedAnimationState.TRANSITIONING;
			this.animSpeedMultiplier = builder.getWrappedAnimSpeed();
			
			this.currentAnimation = builder.getAnimation();
			this.controller.setAnimation(builder.getBuilder());
			this.controller.setAnimationSpeed(animSpeedMultiplier);
		}
	}
	
	public void stopAnimation(IAnimationBuilder targetAnim) {
		targetAnim.stopAnimation();
		
		this.animationProgress = 0;
		this.animationLength = 0;
		this.transitionProgress = 0;
		this.animationState = ExpandedAnimationState.FINISHED;
		this.currentAnimation = none();
		
		this.controller.setAnimation(null);
	}
	
	public double getSyncedProgress() {
		if (server != null) CANetworkManager.sendEntityTrackingPacket(new AnimationFunctionalProgressPacket(name, ((Entity) animatable).getId(), (Math.max(server.getNextTickTime() - Util.getMillis(), 0.0) / 50.0) * animSpeedMultiplier), (Entity) animatable);
		return server == null ? 0 : (Math.max(server.getNextTickTime() - Util.getMillis(), 0.0) / 50.0) * animSpeedMultiplier;
	}
	
	public void updateAnimProgress(double animationProgressDelta) {
		this.animationProgress += animationProgressDelta;
	}
	
	public double getAnimSpeed() {
		return animSpeedMultiplier;
	}
	
	public void setAnimSpeed(double animSpeedMultiplier) {
		this.animSpeedMultiplier = animSpeedMultiplier;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isCurrentAnimationFinished() {
		return animationState.equals(ExpandedAnimationState.FINISHED);
	}
	
	public boolean isAnimationFinished(String targetAnimName) {
		return currentAnimation != null && currentAnimation.animationName.equals(targetAnimName) && animationState.equals(ExpandedAnimationState.FINISHED);
	}
	
	public boolean isAnimationFinished(IAnimationBuilder targetAnim) {
		return isAnimationFinished(targetAnim.getAnimationName());
	}
	
	public boolean isPlayingAnimation(String targetAnimName) {
		return currentAnimation != null && currentAnimation.animationName.equals(targetAnimName) && (animationState.equals(ExpandedAnimationState.RUNNING) || animationState.equals(ExpandedAnimationState.TRANSITIONING));
	}
	
	public boolean isPlayingAnimation(IAnimationBuilder targetAnim) {
		return isPlayingAnimation(targetAnim.getAnimationName());
	}
	
	public ExpandedAnimationState getAnimationState() {
		return animationState;
	}

	public double getAnimationProgressTicks() {
		return Math.ceil(animationProgress) + 3;
	}
	
	public double getAnimationLength() {
		return Math.floor(animationLength) - 4;
	}
	
	public AnimationController<E> getWrappedController() {
		return controller;
	}
	
	public Animation getCurrentAnimation() {
		return currentAnimation;
	}
	
	public static Animation none() {
		Animation noneAnimation = new Animation();
		noneAnimation.animationName = "None";
		noneAnimation.boneAnimations = new ArrayList<BoneAnimation>();
		noneAnimation.animationLength = 0.0;
		return noneAnimation;
	}
}
