package io.github.chaosawakens.common.entity.ai.body_rotation_control;

import io.github.chaosawakens.api.animation.faal.entity.WrappedAnimatableEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class BandaidBodyRotationControl extends BodyRotationControl {
    protected final Mob owner;
    protected static final int ROT_TICK_THRESHOLD = 7;
    protected static final int ROT_THRESHOLD = 75;
    protected int curRotTime;
    protected float targetHeadRot;
    protected double[] xRotHist = new double[ROT_TICK_THRESHOLD];
    protected double[] zRotHist = new double[ROT_TICK_THRESHOLD];

    public BandaidBodyRotationControl(Mob owner) {
        super(owner);
        this.owner = owner;
    }

    @Override
    public void clientTick() {
        for (int i = ROT_TICK_THRESHOLD - 1; i > 0; i--) {
            this.xRotHist[i] = xRotHist[i - 1];
            this.zRotHist[i] = zRotHist[i - 1];
        }

        this.xRotHist[0] = owner.getX();
        this.zRotHist[0] = owner.getZ();

        if (isOwnerMoving()) {
            owner.yBodyRot = owner.getYRot();

            rotateHeadIfNecessary();

            this.targetHeadRot = owner.yHeadRot;
            this.curRotTime = 0;
        } else if (owner.getPassengers().isEmpty() || !(owner.getPassengers().get(0) instanceof Mob)) {
            float rotLimit = ROT_THRESHOLD;

            if (Math.abs(owner.yHeadRot - targetHeadRot) > 15) {
                this.curRotTime = 0;
                this.targetHeadRot = owner.yHeadRot;
                rotateBodyIfNecessary();
            } else {
                final int rotSpeed = 10;

                this.curRotTime++;

                if (curRotTime > rotSpeed) rotLimit = Math.max(1 - (curRotTime - rotSpeed) / rotSpeed, 0) * ROT_THRESHOLD;

                owner.yBodyRot = approachRot(owner.yHeadRot, owner.yBodyRot, rotLimit);
            }
        }
    }

    protected boolean isOwnerMoving() {
        double dx = owner.getX() - owner.xo;
        double dz = owner.getZ() - owner.zo;
        double dxSqr = dx * dx;
        double dzSqr = dz * dz;

        return dxSqr + dzSqr > (owner instanceof WrappedAnimatableEntity animatableOwner ? animatableOwner.getMovementThreshold() : 2.500000277905201E-7);
    }

    private double meanDelta(double[] rotHist) {
        return mean(rotHist, 0) - mean(rotHist, ROT_TICK_THRESHOLD / 2);
    }

    private double mean(double[] rotHist, int startingIndex) {
        double curMean = 0;

        for (int i = 0; i < ROT_TICK_THRESHOLD / 2; i++) curMean += rotHist[i + startingIndex];

        return curMean / rotHist.length;
    }

    private static float approachRot(float curRot, float targetRot, float rotThreshold) {
        float rotDelta = Mth.degreesDifference(targetRot, curRot);

        if (rotDelta < -rotThreshold) rotDelta = -rotThreshold;
        else if (rotDelta >= rotThreshold) rotDelta = rotThreshold;

        return targetRot + rotDelta * 0.55F;
    }

    public int getRotationTickThreshold() {
        return ROT_TICK_THRESHOLD;
    }

    private void rotateBodyIfNecessary() {
        this.owner.yBodyRot = Mth.rotateIfNecessary(this.owner.yBodyRot, this.owner.yHeadRot, (float) this.owner.getMaxHeadYRot());
    }

    private void rotateHeadIfNecessary() {
        this.owner.yHeadRot = Mth.rotateIfNecessary(this.owner.yHeadRot, this.owner.yBodyRot, (float) this.owner.getMaxHeadYRot());
    }
}
