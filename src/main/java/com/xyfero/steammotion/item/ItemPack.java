package com.xyfero.steammotion.item;

import com.google.common.base.Preconditions;
import com.sun.javafx.geom.Vec2d;
import com.xyfero.steammotion.SteamMotion;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemPack extends ItemArmor {
    public ItemPack() {
        super(ArmorMaterial.IRON, -1, EntityEquipmentSlot.CHEST);

        final String name = "steam_pack";
        setRegistryName(SteamMotion.MODID, name);
        setUnlocalizedName(getRegistryName().toString());

        setCreativeTab(getCreativeTab().COMBAT);
    }

    private int timer = 0;

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if(player.moveStrafing != 0f) {
            double amount = 0.5f;
            float direction = player.rotationYaw;
            direction += player.moveStrafing * 90f;
            player.motionX += -Math.sin(Math.toRadians(-direction)) * amount;
            player.motionZ += -Math.cos(Math.toRadians(-direction)) * amount;

            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, player.posX, player.posY, player.posZ, 0, 0, 0);
        }

//        if(timer == 0) {
//            if(player.moveStrafing != 0f) {
//                float direction = player.rotationYaw;
//                direction += player.moveStrafing * 90f;
//                double amount = 1f;
//                player.motionX += -Math.sin(Math.toRadians(-direction)) * amount;
//                player.motionY += 1f;
//                player.motionZ += -Math.cos(Math.toRadians(-direction)) * amount;
//                timer = 60;
//            }
//        } else {
//            timer--;
//        }

    }
}
