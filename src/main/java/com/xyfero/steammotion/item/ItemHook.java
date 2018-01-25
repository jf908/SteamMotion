package com.xyfero.steammotion.item;

import com.xyfero.steammotion.SteamMotion;
import com.xyfero.steammotion.entity.EntityHook;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemHook extends SteamMotionItem {
    private EntityHook hookEntity;

    public ItemHook() {
        super("steam_hook");
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);

        if(!world.isRemote) {
            EntityHook hookEntity = EntityHook.getHook(player);
            if(hookEntity != null) {
                hookEntity.reel();
            } else {

                hookEntity = new EntityHook(world, player, itemstack);
                world.spawnEntity(hookEntity);

                itemstack.setItemDamage(1);
            }
        }

//        Vec3d vec = player.getLookVec().add(player.getPositionVector()).addVector(0, player.getEyeHeight() - 0.5, 0);
//        for(int i=0; i<5; i++) {
//            double range = 0.5;
//            double d0 = (itemRand.nextDouble() * range) - range/2;
//            double d1 = (itemRand.nextDouble() * range) - range/2;
//            double d2 = (itemRand.nextDouble() * range) - range/2;
//            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, vec.x + d0, vec.y + d1, vec.z + d2, 0f, 0f, 0f);
//        }

//        player.swingArm(hand);

        return new ActionResult<>(EnumActionResult.PASS, itemstack);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        EntityHook hookEntity = EntityHook.getHook(entityLiving);
        if(hookEntity != null) {
            hookEntity.setDead();
        }
        return false;
    }
}
