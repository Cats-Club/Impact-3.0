package me.zero.clarinet.util;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;

public final class RayTraceUtils implements Helper {

    public static RayTraceResult tracePath(World world, float x, float y, float z, float tx, float ty, float tz, float borderSize, HashSet<Entity> excluded) {
        Vec3d startVec = new Vec3d(x, y, z);
        Vec3d lookVec = new Vec3d(tx - x, ty - y, tz - z);
        Vec3d endVec = new Vec3d(tx, ty, tz);
        float minX = (x < tx) ? x : tx;
        float minY = (y < ty) ? y : ty;
        float minZ = (z < tz) ? z : tz;
        float maxX = (x > tx) ? x : tx;
        float maxY = (y > ty) ? y : ty;
        float maxZ = (z > tz) ? z : tz;
        AxisAlignedBB bb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ).expand(borderSize, borderSize, borderSize);
        List<Entity> allEntities = world.getEntitiesWithinAABBExcludingEntity(null, bb);
        RayTraceResult blockHit = world.rayTraceBlocks(startVec, endVec);
        startVec = new Vec3d(x, y, z);
        endVec = new Vec3d(tx, ty, tz);
        float maxDistance = (float)endVec.distanceTo(startVec);
        if (blockHit != null) {
            maxDistance = (float)blockHit.hitVec.distanceTo(startVec);
        }
        Entity closestHitEntity = null;
        float closestHit = Float.POSITIVE_INFINITY;
        float currentHit = 0.0f;
        for (Entity ent : allEntities) {
            if (ent.canBeCollidedWith() && !excluded.contains(ent)) {
                float entBorder = ent.getCollisionBorderSize();
                AxisAlignedBB entityBb = ent.getEntityBoundingBox();
                if (entityBb == null) {
                    continue;
                }
                entityBb = entityBb.expand(entBorder, entBorder, entBorder);
                RayTraceResult intercept = entityBb.calculateIntercept(startVec, endVec);
                if (intercept == null) {
                    continue;
                }
                currentHit = (float)intercept.hitVec.distanceTo(startVec);
                if (currentHit >= closestHit && currentHit != 0.0f) {
                    continue;
                }
                closestHit = currentHit;
                closestHitEntity = ent;
            }
        }
        if (closestHitEntity != null) {
            blockHit = new RayTraceResult(closestHitEntity);
        }
        return blockHit;
    }

    public static AxisAlignedBB GetAABB(EntityPlayer player, int cur, Vec3d look) {
        boolean isXPos = look.x >= 0.0;
        boolean isYPos = look.y >= 0.0;
        boolean isZPos = look.z >= 0.0;
        int xAdd = isXPos ? 1 : -1;
        int yAdd = isYPos ? 1 : -1;
        int zAdd = isZPos ? 1 : -1;
        return new AxisAlignedBB(player.posX + look.x * cur, player.posY + look.y * cur, player.posZ + look.z * cur, player.posX + look.x * cur + xAdd, player.posY + look.y * cur + yAdd, player.posZ + look.z * cur + zAdd);
    }

    public static Entity rayCastEntity(EntityLivingBase target) {
        HashSet<Entity> excluded = new HashSet<>();
        excluded.add(mc.player);
        return tracePathD(mc.world, mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ, mc.player.posX, mc.player.posY + target.getEyeHeight(), target.posZ, 1.0f, excluded).entityHit;
    }

    public static EntityPlayer Raycast(int range, EntityPlayer player) {
        Vec3d v = player.getLookVec().normalize();
        EntityPlayer playerRet = null;
        for (int i = 1; i < range; i++) {
            AxisAlignedBB aabb = GetAABB(player, i, v);
            List list = player.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
            if (list.iterator().hasNext()) {
                Object obj = list.get(0);
                if (obj instanceof EntityLivingBase) {
                    EntityLivingBase el = (EntityLivingBase) obj;
                    if (el instanceof EntityPlayer && el != player) {
                        playerRet = (EntityPlayer) el;
                        break;
                    }
                }
            }
        }
        return playerRet;
    }

    public static RayTraceResult tracePathD(World w, double posX, double posY, double posZ, double v, double v1, double v2, float borderSize, HashSet<Entity> exclude) {
        return tracePath(w, (float)posX, (float)posY, (float)posZ, (float)v, (float)v1, (float)v2, borderSize, exclude);
    }

    public static RayTraceResult rayCast(EntityPlayerSP player, double x, double y, double z) {
        HashSet<Entity> excluded = new HashSet<>();
        excluded.add(player);
        return tracePathD(player.world, player.posX, player.posY + player.getEyeHeight(), player.posZ, x, y, z, 1.0f, excluded);
    }

    public static boolean canSeeBlock(BlockPos pos) {
        RayTraceResult position = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
        return position != null && position.getBlockPos() != null && position.getBlockPos().equals(pos);
    }
}
