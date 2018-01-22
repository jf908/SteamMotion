package com.xyfero.steammotion;

import com.xyfero.steammotion.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = SteamMotion.MODID, version = SteamMotion.VERSION)
public class SteamMotion {
    public static final String MODID = "steammotion";
    public static final String VERSION = "1.0";

    @Mod.Instance
    public static SteamMotion instance;

    @SidedProxy(clientSide="com.xyfero.steammotion.proxy.ClientProxy", serverSide="com.xyfero.steammotion.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void preInit(FMLInitializationEvent event) {
        proxy.init(event);
    }
}
