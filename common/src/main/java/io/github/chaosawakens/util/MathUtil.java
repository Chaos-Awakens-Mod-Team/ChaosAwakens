package io.github.chaosawakens.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class MathUtil {

    private MathUtil() {
        throw new IllegalAccessError("Attempted to construct a Utility Class!");
    }

    public static double getRelativeAngleBetween(double x1, double z1, double x2, double z2) {
        return (Math.atan2(z2 - z1, x2 - x1) * (180 / Math.PI) - 90) % 360;
    }

    public static double getRelativeAngleBetweenBlockPositions(BlockPos posA, BlockPos posB) {
        return getRelativeAngleBetween(posA.getX(), posA.getZ(), posB.getX(), posB.getZ());
    }

    public static double getRelativeAngleBetweenEntities(Entity first, Entity second) {
        return getRelativeAngleBetween(first.getX(), first.getZ(), second.getX(), second.getZ());
    }

    public static boolean isWithinAngleRestriction(double relAngle, double angleRestriction) {
        return (relAngle <= angleRestriction / 2 && relAngle >= -angleRestriction / 2) || (relAngle >= 360 - angleRestriction / 2 || relAngle <= -360 + angleRestriction / 2);
    }

    public static boolean isBetween(int num, int min, int max) {
        return num >= min && num <= max;
    }

    public static boolean isBetween(double num, double min, double max) {
        return num >= min && num <= max;
    }

    public static boolean isBetween(float num, float min, float max) {
        return num >= min && num <= max;
    }

    public static boolean isBetween(long num, long min, long max) {
        return num >= min && num <= max;
    }

    public static ObjectArrayList<BlockPos> getDirectLineBetween(int startX, int startY, int startZ, int endX, int endY, int endZ) {
        ObjectArrayList<BlockPos> bresenhamLine = new ObjectArrayList<>();
        int dx = endX - startX;
        int dy = endY - startY;
        int dz = endZ - startZ;
        int steps = Math.max(Math.max(Math.abs(dx), Math.abs(dy)), Math.abs(dz));

        double xIncrement = (double) dx / steps;
        double yIncrement = (double) dy / steps;
        double zIncrement = (double) dz / steps;

        for (int i = 0; i <= steps; i++) {
            int x = startX + (int) Math.round(xIncrement * i);
            int y = startY + (int) Math.round(yIncrement * i);
            int z = startZ + (int) Math.round(zIncrement * i);

            bresenhamLine.add(BlockPos.containing(x, y, z));
        }

        return bresenhamLine;
    }

    public static ObjectArrayList<BlockPos> getDirectLineBetween(Vec3 start, Vec3 end) {
        return getDirectLineBetween((int) start.x, (int) start.y, (int) start.z, (int) end.x, (int) end.y, (int) end.z);
    }

    public static ObjectArrayList<BlockPos> getDirectLineBetween(BlockPos start, BlockPos end) {
        return getDirectLineBetween(start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ());
    }

    public static Vec3 ground(Vec3 basePos, Level curLevel, boolean fullColumnCheck) {
        if (!curLevel.getBlockState(BlockPos.containing(basePos)).getCollisionShape(curLevel, BlockPos.containing(basePos)).isEmpty() || !curLevel.getBlockState(BlockPos.containing(basePos).below()).getCollisionShape(curLevel, BlockPos.containing(basePos)).isEmpty()) return basePos; // Avoid unnecessary computation

        ClipContext colliderCtx = fullColumnCheck
                ? new ClipContext(Vec3.atCenterOf(BlockPos.containing(basePos.x(), curLevel.getMinBuildHeight(), basePos.z())), Vec3.atCenterOf(BlockPos.containing(basePos.x(), curLevel.getMaxBuildHeight(), basePos.z())), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, null)
                : new ClipContext(Vec3.atCenterOf(BlockPos.containing(basePos)), Vec3.atCenterOf(BlockPos.containing(basePos.x(), curLevel.getMinBuildHeight(), basePos.z())), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, null);
        BlockHitResult potentialHitResult = curLevel.clip(colliderCtx);

        return BlockHitResult.Type.MISS.equals(potentialHitResult.getType()) ? basePos : potentialHitResult.getLocation();
    }

    public static OscillationResult oscillate(OscillationResult result, float startValue, float endValue, float delta) {
        if (result.increasing) {
            result.value += delta;
            if (result.value >= endValue) {
                result.value = endValue;
                result.increasing = false;
            }
        } else {
            result.value -= delta;
            if (result.value <= startValue) {
                result.value = startValue;
                result.increasing = true;
            }
        }

        return result;
    }

    public static class OscillationResult {
        private float value;
        private boolean increasing;

        public OscillationResult(float value, boolean increasing) {
            this.value = value;
            this.increasing = increasing;
        }

        public float getValue() {
            return value;
        }

        public boolean isIncreasing() {
            return increasing;
        }

        public boolean isDecreasing() {
            return !increasing;
        }
    }
}
