package com.xyfero.steammotion.item;

import com.xyfero.steammotion.SteamMotion;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class SteamMotionItem extends Item {

    public SteamMotionItem(final String name) {
        setRegistryName(SteamMotion.MODID,  name);
        setUnlocalizedName(getRegistryName().toString());

        setCreativeTab(CreativeTabs.TRANSPORTATION);
    }

    public static final ItemHook itemHook = new ItemHook();

    @Mod.EventBusSubscriber(modid = SteamMotion.MODID)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();

            registry.register(itemHook);
        }
    }
}
