package com.xyfero.steammotion.entity;

import com.google.common.base.Optional;
import com.xyfero.steammotion.item.ItemHook;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityHook extends EntityThrowable {

//    private EntityPlayer shooter;

    private static final DataParameter<Optional<UUID>> SHOOTER = EntityDataManager.createKey(EntityHook.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private int ticks;
    private boolean fixed;

    public EntityHook(World world) {
        super(world);
    }

    /**
     * Assumed to be only called by the server
     *
     * @param world
     * @param player
     */
    public EntityHook(World world, EntityPlayer player) {
        super(world, player);

        dataManager.set(SHOOTER, Optional.of(player.getUniqueID()));
    }

//    private void shoot() {
//        if(shooter == null) return;
//
//        Vec3d vec = shooter.getLookVec();
//        setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.cameraYaw, shooter.cameraPitch);
//
//        motionX = vec.x;
//        motionY = vec.y;
//        motionZ = vec.z;
//        velocityChanged = true;
//    }

    protected void entityInit() {
        dataManager.register(SHOOTER, Optional.absent());
    }

    public void onUpdate() {
        super.onUpdate();

        EntityLivingBase thrower = getThrower();
        if(thrower == null) {
            if(!world.isRemote) {
                setDead();
            }
            return;
        }

        if(shouldStopHooking(thrower)) return;

        if(fixed) {
            if(thrower.getPositionVector().distanceTo(getPositionVector()) < 1) {
                setDead();
                return;
            }

            motionX = 0;
            motionY = 0;
            motionZ = 0;

            movePlayer();
        } else {
            Vec3d vec = thrower.getLookVec();
            motionX = vec.x;
            motionY = vec.y;
            motionZ = vec.z;
        }

//        if(shooter == null) {
//            if(world.isRemote) {
//                UUID id = dataManager.get(PLAYER).orNull();
//                if(id == null) {
//                    return;
//                }
//                Entity entity = world.getPlayerEntityByUUID(id);
//                if(entity != null && entity instanceof EntityPlayer) {
//                    shooter = (EntityPlayer)entity;
//                }
//            } else {
//                setDead();
//                return;
//            }
//        }
//
//
//        ticks++;
//        if(ticks > 1000) {
//            setDead();
//        }
    }

    private void movePlayer() {
        EntityLivingBase thrower = getThrower();

        Vec3d pos = getPositionVector();
        pos = pos.subtract(thrower.getPositionVector());
        pos = pos.normalize();

        thrower.motionX = pos.x;
        thrower.motionY = pos.y;
        thrower.motionZ = pos.z;
    }

    public void onImpact(RayTraceResult result) {
        if(result.typeOfHit == RayTraceResult.Type.BLOCK) {
            fixed = true;
        }
    }

    private boolean shouldStopHooking(EntityLivingBase thrower) {
        ItemStack itemstack = thrower.getHeldItemMainhand();
        ItemStack itemstack2 = thrower.getHeldItemOffhand();
        boolean flag = itemstack.getItem() instanceof ItemHook;
        boolean flag2 = itemstack2.getItem() instanceof ItemHook;

        if (!thrower.isDead && thrower.isEntityAlive() && (flag || flag2) && getDistanceSq(thrower) <= 1024.0D) {
            return false;
        } else {
            setDead();
            return true;
        }
    }

//    @Override
//    public void setDead() {
//        System.out.println("dead!");
//        this.isDead = true;
//    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public EntityLivingBase getThrower() {
        if(thrower != null) {
            return thrower;
        }

        thrower = super.getThrower();
        if(thrower != null) {
            return thrower;
        }

        UUID id = dataManager.get(SHOOTER).orNull();
        if(id == null) {
            return null;
        } else {
            thrower = world.getPlayerEntityByUUID(id);
            return thrower;
        }
    }
}
