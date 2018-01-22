package com.xyfero.steammotion.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityHook extends Entity {


    public EntityHook(World world, double x, double y, double z) {
        super(world);
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    protected void entityInit() {
//        this.getDataManager().register(DATA_HOOKED_ENTITY, Integer.valueOf(0));
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
