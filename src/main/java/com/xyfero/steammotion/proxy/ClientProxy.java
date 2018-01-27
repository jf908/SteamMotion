package com.xyfero.steammotion.proxy;

import com.xyfero.steammotion.SteamMotion;
import com.xyfero.steammotion.client.RenderHook;
import com.xyfero.steammotion.client.RenderPack;
import com.xyfero.steammotion.entity.EntityHook;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
//        "textures/entity/steam_hook.png"
        ResourceLocation rl = new ResourceLocation(SteamMotion.MODID, "textures/entities/hook.png");
        RenderingRegistry.registerEntityRenderingHandler(EntityHook.class, rm -> new RenderHook(rm, rl));
    }

    public void init(FMLInitializationEvent event) {

    }

    public ModelBiped getArmorModel() {
        return new RenderPack();
    }
}
