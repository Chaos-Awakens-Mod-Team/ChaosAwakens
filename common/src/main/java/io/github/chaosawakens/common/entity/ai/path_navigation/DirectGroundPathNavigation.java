package io.github.chaosawakens.common.entity.ai.path_navigation;

import io.github.chaosawakens.CAConstants;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

public class DirectGroundPathNavigation extends GroundPathNavigation {
    public static final float EPSILON = 1.0E-6F;

    public DirectGroundPathNavigation(Mob entity, Level world) {
        super(entity, world);
    }

    @Override
    protected PathFinder createPathFinder(int maxNodes) {
        this.nodeEvaluator = new WalkNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new BandaidPathFinder(nodeEvaluator, maxNodes);
    }

    @Override
    protected void followThePath() {
        Path curPath = Objects.requireNonNull(path);
        Vec3 pathEntityTempPos = getTempMobPos();
        Mob pathEntity = mob;
        int pathLength = curPath.getNodeCount();

        final Vec3 center = pathEntityTempPos.add(-pathEntity.getBbWidth() * 0.5F, 0.0F, -pathEntity.getBbWidth() * 0.5F);
        final Vec3 maxArea = center.add(pathEntity.getBbWidth(), pathEntity.getBbHeight(), pathEntity.getBbWidth());
        Vec3 entityPos = new Vec3(pathEntity.getX(), pathEntity.getY(), pathEntity.getZ());

        for (int i = path.getNextNodeIndex(); i < curPath.getNodeCount(); i++) {
            if (path.getNode(i).y != Math.floor(pathEntityTempPos.y) && hasValidPathType(path.getNode(i).type)) {
                pathLength = i;
                break;
            }
        }

        if (tryTruncateNodes(curPath, pathLength, entityPos, center, maxArea)) {
            if (followingPath(curPath, 0.2F) || (elevationChangedFor(curPath) && followingPath(curPath, mob.getBbWidth() * 0.5F)) && canCutCorner(path.getNextNode().type)) {
                path.setNextNodeIndex(path.getNextNodeIndex() + 1);
            }
        }

        doStuckDetection(pathEntityTempPos);
    }

    protected static int leti(float c, int step) {
        return Mth.floor(c - step * EPSILON);
    }

    protected static int teti(float c, int step) {
        return Mth.floor(c + step * EPSILON);
    }

    protected static float switchAxis(Vec3 pos, int axisIndex) {
        switch (axisIndex) {
            default: return 0.0F;
            case 1: return (float) pos.x;
            case 2: return (float) pos.y;
            case 3: return (float) pos.z;
        }
    }

    private boolean followingPath(Path curPath, float threshold) {
        Mob pathEntity = mob;
        final Vec3 pathPos = curPath.getNextEntityPos(pathEntity);

        float distX = Mth.abs((float) (pathEntity.getX() - pathPos.x));
        float distY = Mth.abs((float) (pathEntity.getY() - pathPos.y));
        float distZ = Mth.abs((float) (pathEntity.getZ() - pathPos.z));

        return distX < threshold && distY < 1.0D && distZ < threshold;
    }

    @Override
    protected boolean canMoveDirectly(Vec3 pPosVec31, Vec3 pPosVec32) {
        return true;
    }

    protected boolean elevationChangedFor(Path ePath) {
        final int curNode = ePath.getNextNodeIndex();
        final int curNodeY = ePath.getNode(curNode).y;
        final int targetNode = (int) Math.min(ePath.getNodeCount(), curNode + Math.ceil(mob.getBbWidth() / 2.0D) + 1.0F);

        for (int progress = curNode + 1; progress < targetNode; progress++) {
            if (ePath.getNode(progress).y != curNodeY) return true;
        }

        return false;
    }

    private boolean tryTruncateNodes(Path pathToTrim, int pathLength, Vec3 pathEntityPos, Vec3 center, Vec3 maxArea) {
        for (int length = pathLength; --length > pathToTrim.getNextNodeIndex();) {
            final Vec3 dist = pathToTrim.getEntityPosAtNode(mob, length).subtract(pathEntityPos);
            if (sweepThrough(pathEntityPos, center, maxArea)) {
                pathToTrim.setNextNodeIndex(length);
                return false;
            }
        }
        return true;
    }

    protected boolean sweepThrough(Vec3 pathVec, Vec3 center, Vec3 max) {
        float l = 0.0F;
        float ml = (float) pathVec.length();

        if (ml < EPSILON) return true;

        final float[] trailEdge = new float[3];
        final int[] leadEdgeI = new int[3];
        final int[] trailEdgeI = new int[3];
        final int[] step = new int[3];
        final float[] trailDelta = new float[3];
        final float[] trailNext = new float[3];
        final float[] normalised = new float[3];

        for (int i = 0; i < 3; i++) {
            float axis = switchAxis(pathVec, i);
            boolean direction = axis >= 0.0F;
            step[i] = direction ? 1 : -1;
            float lead = switchAxis(direction ? max : center, i);
            trailEdge[i] = switchAxis(direction ? center : max, i);
            leadEdgeI[i] = leti(lead, step[i]);
            trailEdgeI[i] = teti(trailEdge[i], step[i]);
            normalised[i] = axis / ml;
            trailDelta[i] = Mth.abs(ml / axis);
            float dist = direction ? (leadEdgeI[i] + 1 - lead) : (lead - leadEdgeI[i]);
            trailNext[i] = trailDelta[i] < Float.POSITIVE_INFINITY ? trailDelta[i] * dist : Float.POSITIVE_INFINITY;
        }

        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        do {
            int axis = (trailNext[0] < trailNext[1]) ? ((trailNext[0] < trailNext[2]) ? 0 : 2) : ((trailNext[1] < trailNext[2]) ? 1 : 2);
            float nextDirection = trailNext[axis] - l;

            l = trailNext[axis];
            leadEdgeI[axis] += step[axis];
            trailNext[axis] += trailDelta[axis];

            for (int i = 0; i < 3; i++) {
                trailEdge[i] += nextDirection * normalised[i];
                trailEdgeI[i] = teti(trailEdge[i], step[i]);
            }

            int stepx = step[0];
            int x0 = (axis == 0) ? leadEdgeI[0] : trailEdgeI[0];
            int x1 = leadEdgeI[0] + stepx;
            int stepy = step[1];
            int y0 = (axis == 1) ? leadEdgeI[1] : trailEdgeI[1];
            int y1 = leadEdgeI[1] + stepy;
            int stepz = step[2];
            int z0 = (axis == 2) ? leadEdgeI[2] : trailEdgeI[2];
            int z1 = leadEdgeI[2] + stepz;
            for (int x = x0; x != x1; x += stepx) {
                for (int z = z0; z != z1; z += stepz) {
                    for (int y = y0; y != y1; y += stepy) {
                        BlockState block = this.level.getBlockState(pos.set(x, y, z));
                        if (!block.isPathfindable(this.level, pos, PathComputationType.LAND)) return false;
                    }

                    BlockPathTypes below = this.nodeEvaluator.getBlockPathType(this.level, x, y0 - 1, z);
                    if (below == BlockPathTypes.WATER || below == BlockPathTypes.LAVA || below == BlockPathTypes.OPEN) return false;

                    BlockPathTypes in = this.nodeEvaluator.getBlockPathType(this.level, x, y0, z);
                    float priority = this.mob.getPathfindingMalus(in);

                    if (priority < 0.0F || priority >= 8.0F) return false;
                    if (in == BlockPathTypes.DAMAGE_FIRE || in == BlockPathTypes.DANGER_FIRE || in == BlockPathTypes.DAMAGE_OTHER || in == BlockPathTypes.WATER) return false;
                }
            }
        } while (l <= ml);

        return true;
    }

    @Override
    protected boolean hasValidPathType(BlockPathTypes type) {
        if (type == BlockPathTypes.WATER) return false;
        else if (type == BlockPathTypes.LAVA) return false;
        else return type != BlockPathTypes.OPEN;
    }

    @Override
    public boolean canCutCorner(BlockPathTypes pPathType) {
        return pPathType != BlockPathTypes.LAVA && super.canCutCorner(pPathType);
    }

    public static class BandaidPathFinder extends PathFinder {
        public BandaidPathFinder(NodeEvaluator processor, int maxVisitedNodes) {
            super(processor, maxVisitedNodes);
        }

        @Nullable
        @Override
        public Path findPath(@NotNull PathNavigationRegion region, @NotNull Mob pathfindingEntity, @NotNull Set<BlockPos> targetPosSet, float range, int accuracy, float ySearchMultiplier) {
            Path superPath = super.findPath(region, pathfindingEntity, targetPosSet, range, accuracy, ySearchMultiplier);
            return superPath == null ? null : new BandaidPath(superPath);
        }
    }

    public static class BandaidPath extends Path {

        public BandaidPath(Path origin) {
            super(copyPathPoints(origin), origin.getTarget(), origin.canReach());
        }

        public static ObjectArrayList<Node> copyPathPoints(Path origin) {
            ObjectArrayList<Node> pathpoints = new ObjectArrayList<>();
            for (int count = 0; count < origin.getNodeCount(); count++) {
                pathpoints.add(origin.getNode(count));
            }
            return pathpoints;
        }

        @Override
        public @NotNull Vec3 getEntityPosAtNode(Entity pathEntity, int index) {
            Node nextPos = getNode(index);
            double x = nextPos.x + Math.floor(pathEntity.getBbWidth() + 1.0F) * 0.5D;
            double y = nextPos.y;
            double z = nextPos.z + Math.floor(pathEntity.getBbWidth() + 1.0F) * 0.5D;
            Vec3 newNextPos = new Vec3(x, y, z);
            return newNextPos;
        }

        public static void divertPath(Mob pathEntity, Path from, Path to, double speedMultiplier) {
            if (pathEntity.getNavigation().isDone()) return;
            if (pathEntity.getNavigation().getPath().equals(from)) {
                if (pathEntity.getNavigation().isInProgress()) {
                    pathEntity.getNavigation().stop();
                    to = pathEntity.getNavigation().createPath(to.getTarget(), 0);
                    pathEntity.getNavigation().moveTo(to, speedMultiplier);
                }
            }
        }
        public static void divertPath(Mob pathEntity, Path from, Path to) {
            divertPath(pathEntity, from, to, 1);
        }
        @Override
        public String toString() {
            if (getNextNodeIndex() < getNodeCount()) {
                return CAConstants.MOD_NAME + " Path: " + "[length = " + getNodeCount() + ", start pos = " + getNodePos(0) + ", next target pos = " + getNextNodePos() + ", final destination pos = " + getEndNode().asBlockPos() + "]";
            }
            return "NULL OR INVALID PATH";
        }
    }
}
