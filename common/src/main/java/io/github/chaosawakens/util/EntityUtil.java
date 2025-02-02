package io.github.chaosawakens.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public final class EntityUtil {

    private EntityUtil() {
        throw new IllegalAccessError("Attempted to construct a Utility Class!");
    }

    public static <E extends Entity> List<E> getEntitiesAround(Entity user, Class<E> entityClass, double dX, double dY, double dZ, double radius) {
        Predicate<E> distPredicate = living -> living != user && (user.getTeam() != null && living.getTeam() != null ? !living.getTeam().equals(user.getTeam()) : living.isAlive()) && living.getClass() != user.getClass() && user.distanceTo(living) <= radius + living.getBbWidth() / 2F;
        return new ObjectArrayList<>(user.level().getEntitiesOfClass(entityClass, user.getBoundingBox().inflate(dX, dY, dZ), distPredicate));
    }

    public static <E extends Entity> List<E> getEntitiesAroundNoPredicate(LivingEntity user, Class<E> entityClass, double dX, double dY, double dZ) {
        return new ObjectArrayList<>(user.level().getEntitiesOfClass(entityClass, user.getBoundingBox().inflate(dX, dY, dZ)));
    }

    public static <E extends Entity> List<E> getEntitiesAround(Entity user, Class<E> entityClass, double dX, double dY, double dZ, Predicate<E> detectionConditions) {
        return new ObjectArrayList<>(user.level().getEntitiesOfClass(entityClass, user.getBoundingBox().inflate(dX, dY, dZ), detectionConditions));
    }

    public static List<LivingEntity> getAllEntitiesAround(Entity user, double dX, double dY, double dZ, double radius) {
        return getEntitiesAround(user, LivingEntity.class, dX, dY, dZ, radius);
    }

    public static void shoot(Entity target, double xMovement, double yMovement, double zMovement, float velocity, float inaccuracy) {
        Vec3 movementVec = new Vec3(xMovement, yMovement, zMovement).normalize().add(target.level().getRandom().triangle(0.0D, 0.0172275D * inaccuracy), target.level().getRandom().triangle(0.0D, 0.0172275D * inaccuracy), target.level().getRandom().triangle(0.0D, 0.0172275D * inaccuracy)).scale(velocity);

        target.setDeltaMovement(movementVec);

        double horizontalMovement = movementVec.horizontalDistance();

        target.setYRot((float) (Mth.atan2(movementVec.x, movementVec.z) * (180F / Math.PI)));
        target.setXRot((float) (Mth.atan2(movementVec.y, horizontalMovement) * (180F / Math.PI)));

        target.yRotO = target.getYRot();
        target.xRotO = target.getXRot();
    }
}
