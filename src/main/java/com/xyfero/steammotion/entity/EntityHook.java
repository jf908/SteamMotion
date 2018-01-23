package com.xyfero.steammotion.entity;

import com.google.common.base.Optional;
import com.xyfero.steammotion.SteamMotion;
import com.xyfero.steammotion.item.ItemHook;
import com.xyfero.steammotion.item.SteamMotionItem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityHook extends Entity {

    private EntityPlayer shooter;

    private static final DataParameter<Optional<UUID>> PLAYER = EntityDataManager.createKey(EntityHook.class, DataSerializers.OPTIONAL_UNIQUE_ID);
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
        super(world);
        shooter = player;

        dataManager.set(PLAYER, Optional.of(player.getUniqueID()));

        shoot();
    }

    private void shoot() {
        if(shooter == null) return;

        Vec3d vec = shooter.getLookVec();
        setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.cameraYaw, shooter.cameraPitch);

        motionX = vec.x;
        motionY = vec.y;
        motionZ = vec.z;
        velocityChanged = true;
    }

    protected void entityInit() {
        dataManager.register(PLAYER, Optional.absent());
    }

    public void onUpdate() {
        super.onUpdate();

        if(shooter == null) {
            if(world.isRemote) {
                UUID id = dataManager.get(PLAYER).orNull();
                if(id == null) {
                    return;
                }
                Entity entity = world.getPlayerEntityByUUID(id);
                if(entity != null && entity instanceof EntityPlayer) {
                    shooter = (EntityPlayer)entity;
                }
            } else {
                setDead();
                return;
            }
        }

        if(shouldStopHooking()) return;

        ticks++;
        if(ticks > 1000) {
            setDead();
        }

        Vec3d vec = shooter.getLookVec();
        motionX = vec.x;
        motionY = vec.y;
        motionZ = vec.z;
        velocityChanged = true;
    }

    private boolean shouldStopHooking() {
        ItemStack itemstack = shooter.getHeldItemMainhand();
        ItemStack itemstack2 = shooter.getHeldItemOffhand();
        boolean flag = itemstack.getItem() instanceof ItemHook;
        boolean flag2 = itemstack2.getItem() instanceof ItemHook;

        if (!shooter.isDead && shooter.isEntityAlive() && (flag || flag2) && getDistanceSq(shooter) <= 1024.0D) {
            return false;
        } else {
            setDead();
            return true;
        }
    }

    @Override
    public void setDead() {
        System.out.println("dead!");
        this.isDead = true;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
    }

    public EntityPlayer getShooter() {
        return shooter;
    }
}
