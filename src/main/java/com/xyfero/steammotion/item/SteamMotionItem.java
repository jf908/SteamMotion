package com.xyfero.steammotion.item;

import com.xyfero.steammotion.SteamMotion;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class SteamMotionItem extends Item {

    public SteamMotionItem(final String name) {
        setRegistryName(SteamMotion.MODID, name);
        setUnlocalizedName(getRegistryName().toString());

        setCreativeTab(CreativeTabs.TRANSPORTATION);
    }

    @GameRegistry.ObjectHolder("steammotion:steam_hook")
    public static final ItemHook ItemHook = new ItemHook();

    @Mod.EventBusSubscriber(modid = SteamMotion.MODID)
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();

            registry.register(ItemHook);
        }

        @SubscribeEvent
        public static void registerAllModels(final ModelRegistryEvent event) {
            ModelResourceLocation mrl = new ModelResourceLocation(ItemHook.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(ItemHook, 0, mrl);
        }
    }
}
