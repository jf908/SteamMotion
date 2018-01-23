package com.xyfero.steammotion.entity;

import com.xyfero.steammotion.SteamMotion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

public class SteamMotionEntity {
    private static int id = 0;

    @Mod.EventBusSubscriber(modid = SteamMotion.MODID)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
            ResourceLocation registryName = new ResourceLocation(SteamMotion.MODID, "steam_hook");

            EntityEntry entry = EntityEntryBuilder.create()
                    .entity(EntityHook.class)
                    .id(registryName, id++)
                    .name(registryName.toString())
                    .tracker(64, 20, true)
                    .build();

            event.getRegistry().register(entry);
        }
    }
}
