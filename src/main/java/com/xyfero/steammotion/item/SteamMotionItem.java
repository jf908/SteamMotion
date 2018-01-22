package com.xyfero.steammotion.item;

import com.xyfero.steammotion.SteamMotion;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class SteamMotionItem extends Item {
    @Mod.EventBusSubscriber(modid = SteamMotion.MODID)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();

//            registry.register();
        }
    }
}
