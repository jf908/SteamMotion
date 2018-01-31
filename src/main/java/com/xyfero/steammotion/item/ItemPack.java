package com.xyfero.steammotion.item;

import com.xyfero.steammotion.SteamMotion;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemPack extends ItemArmor {
    private int timer = 2;

    public ItemPack() {
        super(ArmorMaterial.IRON, -1, EntityEquipmentSlot.CHEST);
        setMaxDamage(15000);

        final String name = "steam_pack";
        setRegistryName(SteamMotion.MODID, name);
        setUnlocalizedName(getRegistryName().toString());

        setCreativeTab(getCreativeTab().COMBAT);
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
