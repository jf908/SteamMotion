package com.xyfero.steammotion.block;

import com.google.common.eventbus.Subscribe;
import com.xyfero.steammotion.SteamMotion;
import com.xyfero.steammotion.tileentity.TileEntityBoiler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class SteamMotionBlock {
    public static void setBlockName(final Block block, final String name) {
        block.setRegistryName(SteamMotion.MODID, name);
        block.setUnlocalizedName(block.getRegistryName().toString());
    }

    public static final BlockBoiler BLOCK_BOILER = new BlockBoiler();

    @Mod.EventBusSubscriber(modid = SteamMotion.MODID)
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();

            registry.register(BLOCK_BOILER);

            GameRegistry.registerTileEntity(TileEntityBoiler.class, SteamMotion.MODID + "_boiler");
        }

        @SubscribeEvent
        public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
            final IForgeRegistry<Item> registry = event.getRegistry();

            ItemBlock boilerItem = new ItemBlock(BLOCK_BOILER);
            registry.register(boilerItem.setRegistryName(boilerItem.getBlock().getRegistryName()));
        }
    }
}
