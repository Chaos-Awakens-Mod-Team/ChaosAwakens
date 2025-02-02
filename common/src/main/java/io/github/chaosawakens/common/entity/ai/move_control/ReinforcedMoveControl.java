package io.github.chaosawakens.common.entity.ai.move_control;

import io.github.chaosawakens.common.entity.base.AnimatableMonster;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;

public class ReinforcedMoveControl extends MoveControl {

    public ReinforcedMoveControl(Mob pMob) {
        super(pMob);
    }

    @Override
    public void tick() {
        if (mob instanceof AnimatableMonster animatableOwner && animatableOwner.isAttackingStatically()) {
            animatableOwner.setZza(0.0F);
            return;
        }

        if (this.operation == Operation.STRAFE) {
            float baseSpeed = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float adjustedSpeed = (float) this.speedModifier * baseSpeed;
            float forwardMovement = this.strafeForwards;
            float sideMovement = this.strafeRight;
            float moveDistSqrd = Math.min(Mth.sqrt(forwardMovement * forwardMovement + sideMovement * sideMovement), 1.0F);

            moveDistSqrd = adjustedSpeed / moveDistSqrd;
            forwardMovement *= moveDistSqrd;
            sideMovement *= moveDistSqrd;

            float xDirMovementMod = Mth.sin((float) Math.toRadians(mob.getYRot()));
            float zDirMovementMod = Mth.cos((float) Math.toRadians(mob.getYRot()));

            float xMovementPredictionOffset = forwardMovement * zDirMovementMod - sideMovement * xDirMovementMod;
            float zMovementPredictionOffset = sideMovement * zDirMovementMod + forwardMovement * xDirMovementMod;

            if (!isWalkable(xMovementPredictionOffset, zMovementPredictionOffset)) {
                this.strafeForwards = 1.0F;
                this.strafeRight = 0.0F;
            }

            this.mob.setSpeed(adjustedSpeed);
            this.mob.setZza(strafeForwards);
            this.mob.setXxa(strafeRight);
            this.operation = Operation.WAIT;
        } else if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;

            double xDelta = this.wantedX - this.mob.getX();
            double zDelta = this.wantedZ - this.mob.getZ();
            double yDelta = this.wantedY - this.mob.getY();

            double deltaSqrd = xDelta * xDelta + yDelta * yDelta + zDelta * zDelta;

            if (deltaSqrd < 2.5000003E-7D) {
                this.mob.setZza(0.0F);
                return;
            }

            float adjustedDirectionalMovementAngle = (float) (Math.toDegrees(Mth.atan2(zDelta, xDelta))) - 90.0F;

            this.mob.setYRot(rotlerp(this.mob.getYRot(), adjustedDirectionalMovementAngle, 180.0F));
            this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
        } else this.mob.setZza(0.0F);
    }

    public boolean isWalkable(float relativeXOffset, float relativeZOffset) {
        PathNavigation ownerPathNav = this.mob.getNavigation();
        NodeEvaluator curNodeEvaluator = ownerPathNav != null ? ownerPathNav.getNodeEvaluator() : null;

        return ownerPathNav == null || (curNodeEvaluator == null || curNodeEvaluator.getBlockPathType(this.mob.level(), Mth.floor(this.mob.getX() + relativeXOffset), this.mob.getBlockY(), Mth.floor(this.mob.getZ() + relativeZOffset)) == BlockPathTypes.WALKABLE);
    }
}
