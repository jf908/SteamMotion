package com.xyfero.steammotion.entity;

import com.xyfero.steammotion.SteamMotion;
import com.xyfero.steammotion.item.ItemHook;
import com.xyfero.steammotion.item.SteamMotionItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityHook extends Entity {

    private EntityPlayer shooter;

    private int ticks;
    private boolean fixed;

    public EntityHook(World world) {
        super(world);
    }

    public EntityHook(World world, EntityPlayer player) {
        super(world);
        shooter = player;
    }

    private void shoot() {
        if(shooter == null) return;

        Vec3d vec = shooter.getLookVec();
    }

    protected void entityInit() {
//        this.getDataManager().register(DATA_HOOKED_ENTITY, Integer.valueOf(0));
    }

    public void onUpdate() {
        super.onUpdate();

        if(shouldStopHooking()) return;

        ticks++;
        if(ticks > 1000) {
            setDead();
        }
//        if(fixed) {
//
//        } else {
//
//        }
    }

    private boolean shouldStopHooking() {
        ItemStack itemstack = shooter.getHeldItemMainhand();
        ItemStack itemstack2 = shooter.getHeldItemOffhand();
        boolean flag = itemstack.getItem() instanceof ItemHook;
        boolean flag2 = itemstack2.getItem() instanceof ItemHook;

        if (shooter != null && !shooter.isDead && shooter.isEntityAlive() && (flag || flag2) && getDistanceSq(shooter) <= 1024.0D) {
            return false;
        } else {
            setDead();
            return true;
        }
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
}
