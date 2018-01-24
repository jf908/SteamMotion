package com.xyfero.steammotion.entity;

import com.google.common.base.Optional;
import com.xyfero.steammotion.item.ItemHook;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPane;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityHook extends EntityThrowable {

//    private EntityPlayer shooter;

    private static final DataParameter<Optional<UUID>> SHOOTER = EntityDataManager.createKey(EntityHook.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private State state = State.FLYING;
    private BlockPos fixedTo;

    private int ticks;
    private double length;

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

        shoot(player, player.rotationPitch, player.rotationYaw, 0, 3, 1);
        if (!player.onGround) {
            motionY -= player.motionY;
        }
    }

    protected void entityInit() {
        dataManager.register(SHOOTER, Optional.absent());
    }

    public void onUpdate() {
        EntityLivingBase thrower = getThrower();
        if(thrower == null) {
            if(!world.isRemote) {
                setDead();
            }
            return;
        }

        if(shouldStopHooking(thrower)) return;

        if(state == State.FLYING) {
            super.onUpdate();
        } else {
            onEntityUpdate();
        }

        switch(state) {
            case FLYING:
                break;
            case FIXED:
                if(thrower.getPositionVector().distanceTo(getPositionVector()) < 3) {
                    if(world.getBlockState(fixedTo).getBlock().equals(Blocks.IRON_BARS)) {
                        state = State.SKATING;
                        System.out.println("skate!");
                        thrower.setPosition(fixedTo.getX()+0.5, fixedTo.getY()-2, fixedTo.getZ()+0.5);
                        thrower.motionX = 0;
                        thrower.motionY = 0;
                        thrower.motionZ = 0;
                        return;
                    }
                }

                handlerPlayerMove();

//                if(thrower.getPositionVector().distanceTo(getPositionVector()) + 500 < new Vec3d(thrower.prevPosX, thrower.prevPosY, thrower.prevPosZ).distanceTo(getPositionVector())) {
//                    setDead();
//                    return;
//                }

                resetMotion();
//                movePlayer();
                break;
            case SKATING:
                if(world.getBlockState(thrower.getPosition().add(-0.5,2,-0.5)).getBlock().equals(Blocks.IRON_BARS)) {

                    float angle = thrower.rotationYaw % 360f;
                    if(angle < 0f) {
                        angle += 360f;
                    }

                    if(angle > 180f) {
                        thrower.motionX = 1;
                        thrower.motionY = 0;
                        thrower.motionZ = 0;
                    } else {
                        thrower.motionX = -1;
                        thrower.motionY = 0;
                        thrower.motionZ = 0;
                    }

                    posX = thrower.posX;
                    posY = thrower.posY + 2;
                    posZ = thrower.posZ;

                    world.spawnParticle(EnumParticleTypes.CRIT, posX, posY, posZ, 0, 0, 0);
                } else {
                    setDead();
                }
                break;
        }

        super.onUpdate();
    }

    private void handlerPlayerMove() {
        if(thrower.getPositionVector().distanceTo(getPositionVector()) >= length) {
            Vec3d vec = new Vec3d(thrower.motionX, thrower.motionY, thrower.motionZ);

//            Vec3d newMotion = vec.crossProduct(getPositionVector().subtract(thrower.getPositionVector()));
//
//            if(newMotion.lengthSquared() > 5.0) {
//                newMotion.normalize();
//            }

//            thrower.motionX = newMotion.x;
//            thrower.motionY = newMotion.y;
//            thrower.motionZ = newMotion.z;
        }
    }

    @Override
    public boolean hasNoGravity() {
        return true;
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
            state = State.FIXED;
            fixedTo = result.getBlockPos();

//            if(!world.isRemote) {
//                setPosition(result.hitVec.x, result.hitVec.y, result.hitVec.z);
//            }
            System.out.println(result.hitVec);

            resetMotion();

//            rotationYaw = prevRotationYaw;
//            rotationPitch = prevRotationPitch;

            length = thrower.getPositionVector().distanceTo(getPositionVector());
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

    private void resetMotion() {
        motionX = 0;
        motionY = 0;
        motionZ = 0;
    }

    static enum State {
        FLYING,
        FIXED,
        SKATING
    };
}
