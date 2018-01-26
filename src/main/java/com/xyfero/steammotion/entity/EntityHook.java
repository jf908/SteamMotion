package com.xyfero.steammotion.entity;

import com.xyfero.steammotion.item.ItemHook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

public class EntityHook extends Entity {

    private static final HashMap<EntityLivingBase, EntityHook> hooks = new HashMap<>();
    private ItemStack itemstack;

    private static final DataParameter<Integer> SHOOTER = EntityDataManager.createKey(EntityHook.class, DataSerializers.VARINT);
    private EntityLivingBase shooter;

    private static final DataParameter<Float> LENGTH = EntityDataManager.createKey(EntityHook.class, DataSerializers.FLOAT);
    private static final DataParameter<Byte> STATE = EntityDataManager.createKey(EntityHook.class, DataSerializers.BYTE);

    private BlockPos fixedTo;

    private Vec3d lastMotion;

    private int ticksInAir;

    private double skateAccel;
    private int skateDir;

    public EntityHook(World world) {
        super(world);
        setSize(0.5f, 0.5f);
    }

    /**
     * Assumed to be only called by the server
     *
     * @param world
     * @param entity
     */
    public EntityHook(World world, EntityLivingBase entity, ItemStack itemstack) {
        this(world);

        hooks.put(entity, this);
        shooter = entity;
        dataManager.set(SHOOTER, entity.getEntityId());
        this.itemstack = itemstack;

        shoot();
    }

    protected void entityInit() {
        dataManager.register(SHOOTER, -1);
        dataManager.register(STATE, (byte)0);
        dataManager.register(LENGTH, 0f);
    }

    private void shoot() {
        float yaw = shooter.rotationYaw % 360f;
        if(yaw < 0f) {
            yaw += 360f;
        }
        float pitch = shooter.rotationPitch % 360f;
        if(pitch < 0f) {
            pitch += 360f;
        }

        setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        setPosition(posX, posY, posZ);

        Vec3d vec = shooter.getLookVec().scale(4.0);
        motionX = vec.x;
        motionY = vec.y;
        motionZ = vec.z;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        if(world.isRemote) {
            System.out.println("Setting client position:" + new Vec3d(x,y,z).toString());
        }
        super.setPosition(x,y,z);
    }

    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 4096.0D;
    }

    private boolean posUpdated;

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

        switch(getState()) {
            case FLYING:
                handleFlying();
                break;
            case FIXED:
                if(!world.isRemote && !posUpdated) {
                    ((WorldServer)world).getEntityTracker().sendToTrackingAndSelf(this, new SPacketEntityTeleport(this));
                }
//                if(!world.isRemote) {
//                    ((WorldServer)world).getEntityTracker().sendToTrackingAndSelf(this, new SPacketEntityTeleport(this));
//                }
                System.out.println("Actual Pos" + getPositionVector());
                if(shooter.getPositionVector().distanceTo(getPositionVector()) < 3) {
                    if(world.getBlockState(getPosition()).getBlock().equals(Blocks.IRON_BARS)) {
                        setState(State.SKATING);
                        BlockPos bPos = getPosition();
                        shooter.setPositionAndUpdate(bPos.getX()+0.5, bPos.getY()-2, bPos.getZ()+0.5);
                        shooter.motionX = 0;
                        shooter.motionY = 0;
                        shooter.motionZ = 0;
                        return;
                    }
                }

                handlerPlayerMove();
                break;
            case SKATING:
                if(world.getBlockState(new BlockPos(shooter.getPositionVector().addVector(0,2,0))).getBlock().equals(Blocks.IRON_BARS)) {

                    float angle = (shooter.rotationYaw + 45f) % 360f;
                    if(angle < 0f) {
                        angle += 360f;
                    }

                    skateAccel *= 0.9f;

                    if(angle < 90f) {
                        if(world.getBlockState(new BlockPos(shooter.getPositionVector().addVector(0,2,1))).getBlock().equals(Blocks.IRON_BARS)) {
                            skateAccel += 0.1f;
                            skateDir = 1;
                        }
                    } else if(angle < 180f) {
                        if(world.getBlockState(new BlockPos(shooter.getPositionVector().addVector(-1,2,0))).getBlock().equals(Blocks.IRON_BARS)) {
                            skateAccel -= 0.1f;
                            skateDir = 0;
                        }
                    } else if(angle < 270f) {
                        if(world.getBlockState(new BlockPos(shooter.getPositionVector().addVector(0,2,-1))).getBlock().equals(Blocks.IRON_BARS)) {
                            skateAccel -= 0.1f;
                            skateDir = 1;
                        }
                    } else {
                        if(world.getBlockState(new BlockPos(shooter.getPositionVector().addVector(1,2,0))).getBlock().equals(Blocks.IRON_BARS)) {
                            skateAccel += 0.1f;
                            skateDir = 0;
                        }
                    }

                    double maxAccel = 1f;
                    skateAccel = Math.min(Math.max(skateAccel, -maxAccel), maxAccel);

                    if(skateDir == 0) {
                        shooter.motionX = skateAccel;
                        shooter.motionY = 0;
                        shooter.motionZ = 0;
                    } else {
                        shooter.motionX = 0;
                        shooter.motionY = 0;
                        shooter.motionZ = skateAccel;
                    }

//                    if(angle > 180f) {
//                        shooter.motionX = 1;
//                        shooter.motionY = 0;
//                        shooter.motionZ = 0;
//                    } else {
//                        shooter.motionX = -1;
//                        shooter.motionY = 0;
//                        shooter.motionZ = 0;
//                    }

                    posX = shooter.posX;
                    posY = shooter.posY + 2;
                    posZ = shooter.posZ;

                    for(int i=0; i<5; i++) {
                        world.spawnParticle(EnumParticleTypes.CRIT, posX, posY, posZ, rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
                    }
                } else {
                    System.out.println(world.getBlockState(new BlockPos(shooter.getPositionVector().addVector(0,1.8,0))).getBlock());
                    setDead();
                }
                break;
        }

        super.onUpdate();
    }

    private void handleFlying() {
        ++this.ticksInAir;
        RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, false, false, shooter);

        if(raytraceresult != null) {
            onImpact(raytraceresult);
            return;
        }

//        if(world.isRemote) return;

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        posX += motionX;
        posY += motionY;
        posZ += motionZ;

        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                float f1 = 0.25F;
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - motionX * 0.25D, posY - motionY * 0.25D, posZ - motionZ * 0.25D, motionX, motionY, motionZ);
            }
        }

        setPosition(posX, posY, posZ);
    }

    private void handlerPlayerMove() {
        if(shooter.getPositionVector().distanceTo(getPositionVector()) >= getLength()) {
//            Vec3d vec = new Vec3d(shooter.motionX, shooter.motionY, shooter.motionZ);

//            Vec3d newMotion = vec.crossProduct(shooter.getPositionVector().subtract(getPositionVector()).normalize());
//
//            if(newMotion.lengthVector() > 1.0) {
//                newMotion.normalize();
//            }

//            shooter.motionX = newMotion.x;
//            shooter.motionY = newMotion.y;
//            shooter.motionZ = newMotion.z;

//            shooter.motionX = - vec.x;
//            shooter.motionY = - vec.y;
//            shooter.motionZ = - vec.z;

//            Vec3d normal = shooter.getPositionVector().subtract(getPositionVector()).normalize();
//
//            Vec3d gravity = new Vec3d(0, -1f, 0);
//
//            Vec3d point = shooter.getPositionVector().add(gravity).subtract(normal.scale(gravity.dotProduct(normal)));
//            Vec3d motion = point.subtract(shooter.getPositionVector());
//            shooter.motionX = lastMotion.x + motion.x;
//            shooter.motionY = lastMotion.y + motion.y;
//            shooter.motionZ = lastMotion.z + motion.z;

            double amount = shooter.getPositionVector().distanceTo(getPositionVector()) - getLength();
            amount *= 0.5;
            Vec3d motion = getPositionVector().subtract(shooter.getPositionVector()).normalize().scale(amount);

            shooter.motionX += motion.x;
            shooter.motionY += motion.y;
            shooter.motionZ += motion.z;

            lastMotion = motion;
        } else {
//            shooter.motionX = lastMotion.x;
//            shooter.motionY = lastMotion.y;
//            shooter.motionZ = lastMotion.z;
        }

        if(!shooter.onGround) {
            float val = 1.1f;
            shooter.motionX *= val;
            shooter.motionZ *= val;
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
            resetMotion();
//            prevPosX = posX;
//            prevPosY = posY;
//            prevPosZ = posZ;

            if(!world.isRemote) {
                setState(State.FIXED);
                setPosition(result.hitVec.x, result.hitVec.y, result.hitVec.z);
                setLength((float)shooter.getPositionVector().distanceTo(getPositionVector()));

//                ((WorldServer)world).getEntityTracker().sendToTrackingAndSelf(this, new SPacketEntityTeleport(this));
            }
        }
    }

    public void reel() {
        if(getState() == State.FIXED && getLength() > 1.0) {
            setLength(getLength() - 0.5f);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setTag("direction", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("direction", 9) && compound.getTagList("direction", 6).tagCount() == 3) {
            NBTTagList nbttaglist1 = compound.getTagList("direction", 6);
            motionX = nbttaglist1.getDoubleAt(0);
            motionY = nbttaglist1.getDoubleAt(1);
            motionZ = nbttaglist1.getDoubleAt(2);
        } else {
            this.setDead();
        }
    }

    private boolean shouldStopHooking(EntityLivingBase thrower) {
        ItemStack itemstack = thrower.getHeldItemMainhand();
        ItemStack itemstack2 = thrower.getHeldItemOffhand();
        boolean flag = itemstack.getItem() instanceof ItemHook;
        boolean flag2 = itemstack2.getItem() instanceof ItemHook;

        boolean flag3 = getState() == State.FLYING ? getDistanceSq(thrower) <= 1024.0D : getDistanceSq(thrower) <= 1536.0D;

        if (!thrower.isDead && thrower.isEntityAlive() && (flag || flag2) && flag3) {
            return false;
        } else {
            setDead();
            return true;
        }
    }

    private float getLength() {
        return dataManager.get(LENGTH);
    }

    private void setLength(float len) {
        dataManager.set(LENGTH, len);
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    private State getState() {
        byte b = dataManager.get(STATE);
        switch(b) {
            case 0:
                return State.FLYING;
            case 1:
                return State.FIXED;
            case 2:
                return State.SKATING;
        }
        return State.FLYING;
    }

    private void setState(State state) {
        byte b = 0;
        switch(state) {
            case FLYING:
                b = 0;
                break;
            case FIXED:
                b = 1;
                break;
            case SKATING:
                b = 2;
                break;
        }
        dataManager.set(STATE, b);
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

    public void setDead() {
        if(!world.isRemote) {
            hooks.remove(shooter);
            if(itemstack != null) {
                itemstack.setItemDamage(0);
            }
        }
        super.setDead();
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

    public static EntityHook getHook(EntityLivingBase entity) {
        return hooks.get(entity);
    }

    static enum State {
        FLYING,
        FIXED,
        SKATING
    };
}
