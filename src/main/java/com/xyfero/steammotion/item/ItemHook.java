package com.xyfero.steammotion.item;

import com.xyfero.steammotion.entity.EntityHook;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemHook extends SteamMotionItem {
    private EntityHook hookEntity;

    public ItemHook() {
        super("steam_hook");
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);

        if(!world.isRemote) {
            hookEntity = new EntityHook(world, player);
            world.spawnEntity(hookEntity);
        }

        Vec3d vec = player.getLookVec().add(player.getPositionVector()).addVector(0, player.getEyeHeight() - 0.5, 0);
        for(int i=0; i<5; i++) {
            double range = 0.5;
            double d0 = (itemRand.nextDouble() * range) - range/2;
            double d1 = (itemRand.nextDouble() * range) - range/2;
            double d2 = (itemRand.nextDouble() * range) - range/2;
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, vec.x + d0, vec.y + d1, vec.z + d2, 0f, 0f, 0f);
        }

        player.setActiveHand(hand);

        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        hookEntity.setDead();
    }
}
