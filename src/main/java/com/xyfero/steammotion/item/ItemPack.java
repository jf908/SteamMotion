package com.xyfero.steammotion.item;

import com.sun.javafx.geom.Vec2d;
import com.xyfero.steammotion.SteamMotion;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.fixes.ArmorStandSilent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemPack extends ItemArmor {
    private int timer = 2;

    public ItemPack() {
        super(ArmorMaterial.IRON, -1, EntityEquipmentSlot.CHEST);

        final String name = "steam_pack";
        setRegistryName(SteamMotion.MODID, name);
        setUnlocalizedName(getRegistryName().toString());

        setCreativeTab(getCreativeTab().COMBAT);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if(player.moveStrafing != 0f) {
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, player.posX, player.posY + 1.5, player.posZ, player.motionX, player.motionY, player.motionZ);
//            if(timer == 0) {
//                world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.PLAYERS,1F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
//                world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.PLAYERS,5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
//                timer = 2;
//            } else {
//                timer--;
//            }

            if(Vec2d.distance(0,0, player.motionX, player.motionZ) > 2) return;

            double amount = 0.2f;
            float direction = player.rotationYaw;
            direction += player.moveStrafing * 90f;
            player.motionX -= Math.sin(Math.toRadians(-direction)) * amount;
            player.motionY += 0.02f;
            player.motionZ -= Math.cos(Math.toRadians(-direction)) * amount;
        }
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return SteamMotion.proxy.getArmorModel();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return SteamMotion.MODID + ":textures/models/armor/steam_pack.png";
    }
}
