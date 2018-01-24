package com.xyfero.steammotion.entity;

import com.google.common.base.Optional;
import com.xyfero.steammotion.item.ItemHook;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPane;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityHook extends Entity {

//    private EntityPlayer shooter;

    private static final DataParameter<Integer> SHOOTER = EntityDataManager.createKey(EntityHook.class, DataSerializers.VARINT);
    private EntityLivingBase shooter;

    private State state = State.FLYING;
    private BlockPos fixedTo;

    private int ticksInAir;

    private double length;

    public EntityHook(World world) {
        super(world);
        setSize(0.5F, 0.5F);
    }

    /**
     * Assumed to be only called by the server
     *
     * @param world
     * @param entity
     */
    public EntityHook(World world, EntityLivingBase entity) {
        this(world);

        shooter = entity;
        dataManager.set(SHOOTER, entity.getEntityId());

        shoot();
    }

    protected void entityInit() {
        dataManager.register(SHOOTER, -1);
    }

    private void shoot() {
        setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        setPosition(posX, posY, posZ);

        Vec3d vec = shooter.getLookVec().scale(3.0);
        motionX = vec.x;
        motionY = vec.y;
        motionZ = vec.z;
    }

    public void onUpdate() {
        EntityLivingBase shooter = getShooter();
        if(shooter == null) {
            if(!world.isRemote) {
                setDead();
            }
            return;
        }

        if(shouldStopHooking(shooter)) return;

        super.onUpdate();

        switch(state) {
            case FLYING:
                handleFlying();
                break;
            case FIXED:
                if(shooter.getPositionVector().distanceTo(getPositionVector()) < 3) {
                    if(world.getBlockState(fixedTo).getBlock().equals(Blocks.IRON_BARS)) {
                        state = State.SKATING;
                        System.out.println("skate!");
                        shooter.setPosition(fixedTo.getX()+0.5, fixedTo.getY()-2, fixedTo.getZ()+0.5);
                        shooter.motionX = 0;
                        shooter.motionY = 0;
                        shooter.motionZ = 0;
                        return;
                    }
                }

                handlerPlayerMove();

//                if(thrower.getPositionVector().distanceTo(getPositionVector()) + 500 < new Vec3d(thrower.prevPosX, thrower.prevPosY, thrower.prevPosZ).distanceTo(getPositionVector())) {
//                    setDead();
//                    return;
//                }

                resetMotion();
//                movePlayerTowards();
                break;
            case SKATING:
                if(world.getBlockState(shooter.getPosition().add(-0.5,2,-0.5)).getBlock().equals(Blocks.IRON_BARS)) {

                    float angle = shooter.rotationYaw % 360f;
                    if(angle < 0f) {
                        angle += 360f;
                    }

                    if(angle > 180f) {
                        shooter.motionX = 1;
                        shooter.motionY = 0;
                        shooter.motionZ = 0;
                    } else {
                        shooter.motionX = -1;
                        shooter.motionY = 0;
                        shooter.motionZ = 0;
                    }

                    posX = shooter.posX;
                    posY = shooter.posY + 2;
                    posZ = shooter.posZ;

                    world.spawnParticle(EnumParticleTypes.CRIT, posX, posY, posZ, 0, 0, 0);
                } else {
                    setDead();
                }
                break;
        }

        super.onUpdate();
    }

    private void handleFlying() {
        ++this.ticksInAir;
        RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, this.ticksInAir >= 25, shooter);

        if(raytraceresult != null) {
            onImpact(raytraceresult);
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;

        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                float f1 = 0.25F;
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
            }
        }

//        this.motionX += this.accelerationX;
//        this.motionY += this.accelerationY;
//        this.motionZ += this.accelerationZ;
//        this.motionX *= (double)f;
//        this.motionY *= (double)f;
//        this.motionZ *= (double)f;
//        this.world.spawnParticle(this.getParticleType(), this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        setPosition(this.posX, this.posY, this.posZ);
    }

    private void handlerPlayerMove() {
        if(shooter.getPositionVector().distanceTo(getPositionVector()) >= length) {
            Vec3d vec = new Vec3d(shooter.motionX, shooter.motionY, shooter.motionZ);

//            Vec3d newMotion = vec.crossProduct(shooter.getPositionVector().subtract(getPositionVector()).normalize());
//
//            if(newMotion.lengthVector() > 1.0) {
//                newMotion.normalize();
//            }

//            shooter.motionX = newMotion.x;
//            shooter.motionY = newMotion.y;
//            shooter.motionZ = newMotion.z;

            shooter.motionX = - vec.x;
            shooter.motionY = - vec.y;
            shooter.motionZ = - vec.z;
        }
    }

    private void movePlayerTowards() {
        EntityLivingBase thrower = getShooter();

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
            setPosition(result.hitVec.x, result.hitVec.y, result.hitVec.z);
//            }
            System.out.println(result.hitVec);

            resetMotion();

//            rotationYaw = prevRotationYaw;
//            rotationPitch = prevRotationPitch;
            if(getShooter() == null) return;
            length = shooter.getPositionVector().distanceTo(getPositionVector());
        }
    }


    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setTag("direction", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("direction", 9) && compound.getTagList("direction", 6).tagCount() == 3) {
            NBTTagList nbttaglist1 = compound.getTagList("direction", 6);
            this.motionX = nbttaglist1.getDoubleAt(0);
            this.motionY = nbttaglist1.getDoubleAt(1);
            this.motionZ = nbttaglist1.getDoubleAt(2);
        } else {
            this.setDead();
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

    public EntityLivingBase getShooter() {
        if(shooter != null) {
            return shooter;
        }

        int id = dataManager.get(SHOOTER);
        Entity entity = world.getEntityByID(id);
        if(entity instanceof EntityLivingBase) {
            shooter = (EntityLivingBase)entity;
            return shooter;
        } else {
            return null;
        }
    }

    private void resetMotion() {
        motionX = 0;
        motionY = 0;
        motionZ = 0;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    static enum State {
        FLYING,
        FIXED,
        SKATING
    };
}
