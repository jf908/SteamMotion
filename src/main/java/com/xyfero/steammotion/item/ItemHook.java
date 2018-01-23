package com.xyfero.steammotion.item;

import com.xyfero.steammotion.entity.EntityHook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemHook extends SteamMotionItem {
    public ItemHook() {
        super("steam_hook");
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);

        if(!world.isRemote) {
            EntityHook entity = new EntityHook(world, player);
            world.spawnEntity(entity);
            System.out.println("spawning...");
        }

        player.swingArm(hand);

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}
