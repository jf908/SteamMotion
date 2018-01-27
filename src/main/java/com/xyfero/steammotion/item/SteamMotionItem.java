package com.xyfero.steammotion.item;

import com.xyfero.steammotion.SteamMotion;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class SteamMotionItem extends Item {

    public SteamMotionItem(final String name) {
        setRegistryName(SteamMotion.MODID, name);
        setUnlocalizedName(getRegistryName().toString());

        setCreativeTab(CreativeTabs.TRANSPORTATION);
    }

    @GameRegistry.ObjectHolder("steammotion:steam_hook")
    public static final ItemHook ItemHook = new ItemHook();

    public static final ItemPack ItemPack = new ItemPack();

    @Mod.EventBusSubscriber(modid = SteamMotion.MODID)
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();

            registry.register(ItemHook);
            registry.register(ItemPack);
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void registerAllModels(final ModelRegistryEvent event) {
            ModelBakery.registerItemVariants(ItemHook, ItemHook.getRegistryName(), new ResourceLocation(SteamMotion.MODID, "steam_no_hook"));
            ModelLoader.setCustomMeshDefinition(ItemHook, new ItemMeshDefinition() {
                private ResourceLocation rl = new ResourceLocation(SteamMotion.MODID, "steam_no_hook");
                @Override
                public ModelResourceLocation getModelLocation(ItemStack stack) {
                    if(stack.getMetadata() == 1) {
                        return new ModelResourceLocation(rl, "inventory");
                    }
                    return new ModelResourceLocation(ItemHook.getRegistryName(), "inventory");
                }
            });

            ModelResourceLocation mrl = new ModelResourceLocation(ItemPack.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(ItemPack, 0, mrl);
        }
    }
}
