package com.xyfero.steammotion.item;

import com.xyfero.steammotion.entity.EntityHook;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemHook extends SteamMotionItem {
    private EntityHook hookEntity;

    public ItemHook() {
        super("steam_hook");
    }

    private boolean fakeSwing;

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

        return new ActionResult<>(EnumActionResult.PASS, itemstack);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if(fakeSwing) return false;
        EntityHook hookEntity = EntityHook.getHook(entityLiving);
        if(hookEntity != null) {
            hookEntity.setDead();
        }
        return false;
    }
}
